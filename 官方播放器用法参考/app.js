// app.js
const playletPlugin = requirePlugin('playlet-plugin')
const { SDK } = require('./lib/dn-api-miniprogram/index.js')
const {
  SERVER_BASE_URL,
  initRemoteConfig
} = require('./common/config')

// ---- 登录 & 用户初始化（接口来自第三方系统）----
const LOGIN_API_BASE = 'https://your-admin-domain.com/sqx_fast/app'

const wxLogin = () =>
  new Promise((resolve, reject) => {
    wx.login({
      success: resolve,
      fail: reject,
    })
  })

const wxRequest = (options) =>
  new Promise((resolve, reject) => {
    const token = wx.getStorageSync('token')
    wx.request({
      ...options,
      header: {
        token,
        ...(options.header || {}),
      },
      success: (res) => resolve(res),
      fail: (err) => reject(err),
    })
  })

async function loginAndInitUser() {
  try {
    const loginRes = await wxLogin()
    const code = (loginRes && loginRes.code) || ''
    if (!code) return

    // 1. 微信 code 换取 openId / sessionKey
    const resp = await wxRequest({
      url: `${LOGIN_API_BASE}/Login/chengguangjuchang/wxQbxLogin?code=${code}`,
      method: 'POST',
      data: { code },
    })
    const data = resp && resp.data
    if (!data || data.code !== 0 || !data.data) return

    const { open_id, unionid, session_key } = data.data
    wx.setStorageSync('openId', open_id)
    wx.setStorageSync('unionId', unionid)
    wx.setStorageSync('sessionkey', session_key)

     // 初始化 QQ 广告 SDK
     const app = getApp();
     if (app && app.initQqAd) {
       app.initQqAd({
         openId: open_id,
       });
     }

    // 2. 写入或更新用户记录，获取业务 token
    const insertResp = await wxRequest({
      url: `${LOGIN_API_BASE}/Login/insertWxUser`,
      method: 'POST',
      header: {
        'content-type': 'application/json',
        accept: '*/*',
      },
      data: {
        openId: open_id,
        unionId: unionid || '',
      },
    })

    const insertData = insertResp && insertResp.data
    if (
      insertData &&
      insertData.code === 0 &&
      insertData.user &&
      insertData.token
    ) {
      const { token, user } = insertData
      wx.setStorageSync('token', token)
      wx.setStorageSync('userId', user.userId)
      wx.setStorageSync('invitationCode', user.invitationCode)

      // 设置用户唯一标识并上报注册事件
      const app = getApp()
      
      if (app && app.globalData && app.globalData.DNSDK) {
        app.globalData.DNSDK.setUserUniqueId(user.userId + '')
        app.globalData.DNSDK.track('REGISTER')
      }
    }
  } catch (err) {
    console.error('登录或初始化用户失败', err)
  }
}

/**
 * 从后台获取剧集状态配置
 * @param {string} dramaId
 * @returns {Promise<{serialList: Array, freeList: Array} | null>}
 */
function fetchDramaConfig(dramaId) {
  return new Promise((resolve) => {
    wx.request({
      url: `${SERVER_BASE_URL}/drama/config`,
      method: 'GET',
      data: {
        dramaId
      },
      success(res) {
        if (res.statusCode === 200 && res.data && res.data.code === 0) {
          resolve(res.data.data)
        } else {
          console.error('获取剧集配置失败', res)
          resolve(null)
        }
      },
      fail(err) {
        console.error('请求剧集配置异常', err)
        resolve(null)
      }
    })
  })
}

App({
  onLaunch(options) {
    // 尽早拉取远程配置（异步，不阻塞启动流程）
    initRemoteConfig()

    // 登录并初始化用户信息（异步，不阻塞启动流程）
    loginAndInitUser()

    console.log('options', JSON.stringify(options))
    try {
      wx.setStorageSync('scene', options.scene)
      wx.setStorageSync(
        'gdt_vid',
        (options && options.query && options.query.gdt_vid) || ''
      )
    } catch (error) {
      console.error(error)
    }

    playletPlugin.onPageLoad(async (info) => {

      const pm = playletPlugin.PlayletManager.getPageManager(info.playerId)

      // dramaId 由插件从 info 对象传入
      const dramaId = info.dramaId

      // ---- 初始化剧集列表状态 ----
      let serialList = null
      let freeList = null

      if (dramaId) {
        const config = await fetchDramaConfig(dramaId)
        if (config) {
          serialList = config.serialList
          freeList = config.freeList
        }
      }

      // 后台无配置（含 code:500 "剧目不存在"）时的兜底默认值
      // 前 7 集免费，第 8 集起需解锁。

      const FREE_COUNT = 7
      console.log('event', "mianfei ")
      if (!serialList) {
        serialList = [{
          start_serial_no: 1,
          end_serial_no: FREE_COUNT,
          status: 1
        },
        {
          start_serial_no: FREE_COUNT + 1,
          end_serial_no: 100,
          status: 2
        },
        ]
        freeList = [{
          start_serial_no: 1,
          end_serial_no: FREE_COUNT
        }]
        console.log(`剧 ${dramaId} 无后台配置，默认前 ${FREE_COUNT} 集免费`)
      }

      // 后台有 serialList 但未配置 freeList 时的兜底：重建 serialList，默认前 15% 集（向上取整）免费
      if (serialList && (!freeList || freeList.length === 0)) {
        const maxEpisode = serialList.reduce((max, item) => Math.max(max, item.end_serial_no), 0)
        const freeCount = Math.max(1, Math.ceil(maxEpisode * 0.15))
        serialList = [{
          start_serial_no: 1,
          end_serial_no: freeCount,
          status: 1
        },
        {
          start_serial_no: freeCount + 1,
          end_serial_no: maxEpisode,
          status: 2
        },
        ]
        freeList = [{
          start_serial_no: 1,
          end_serial_no: freeCount
        }]
        console.log(`剧 ${dramaId} 无免费集配置，共 ${maxEpisode} 集，默认前 ${freeCount} 集（15%）免费`)
      }

      // 合并本地已解锁的集数
      const localUnlocked = wx.getStorageSync(`unlocked_serials_${dramaId}`) || []
      localUnlocked.forEach(no => {
        serialList.push({
          start_serial_no: no,
          end_serial_no: no,
          status: 1
        })
      })

      pm.setCanPlaySerialList({
        serialList,
        freeList
      })

      // ---- 数据上报监听 ----
      pm.onDataReport((obj) => {

        const event = obj.event

        if (event === 'AD_WILL_PLAY_TIPS_SHOW') {
          console.log('AD_WILL_PLAY_TIPS_SHOW: 广告倒计时提示已展示')
        }

        if (event === 'CLICK_CANCEL_AD_TIPS') {
          // 用户点击"暂不观看"，插件自动停留在解锁页，无需额外处理
          console.log('CLICK_CANCEL_AD_TIPS: 用户暂不观看广告')
        }

        if (event === 'CHANGE_SERIAL' && obj.mediaItem) {
          if (!obj.mediaItem.isCanPlay) { } else {
            // 进入已解锁集：收起 open area
            pm.updateOpenArea({
              leftsideAreaList: []
            })
          }
        }


        if (event === 'SERIAL_LOCK') {
          // 进入付费集：展示倒计时解锁面板 + 左上角返回按钮
          pm.updateOpenArea({
            leftsideAreaList: [{
              left: 0,
              top: 0,
              width: 88,
              height: 40,
              type: 'btn',
              serialNo: obj.serialNo,
            },
            {
              left: 0,
              right: 0,
              bottom: 0,
              height: 60,
              type: 'unlock',
              serialNo: obj.serialNo,
            },
            ],
          })
        }

      })

      // ---- 用户进入未解锁剧集时触发 ----
      pm.onCheckIsCanPlay(param => {
        console.log('onCheckIsCanPlay', param)
        const {
          serialNo,
          needToCancelAdTimer
        } = param

        const localUnlocked = wx.getStorageSync(`unlocked_serials_${dramaId}`) || []
        const isLocallyUnlocked = localUnlocked.includes(Number(serialNo))

        pm.isCanPlay({
          serialNo,
          serialList: [{
            start_serial_no: serialNo,
            end_serial_no: serialNo,
            status: isLocallyUnlocked ? 1 : 2,
          }],
        })

        if (!isLocallyUnlocked) {
          // 设置广告解锁，解锁按钮文案变为"看广告后播放"
          pm.setUseAdUnlock({
            list: [{
              start_serial_no: serialNo,
              end_serial_no: serialNo,
            }],
          })
        }

        if (needToCancelAdTimer) {
          // 用户已看过广告倒计时，插件自动取消倒计时弹窗
          console.log('needToCancelAdTimer: 跳过倒计时逻辑')
        }

        // 背景预览由组件的 item.type observer 触发，此处不重复调用
      })

      pm.setDramaFlag({
        share: false,
        withShareTicket: false
      })

    })

    wx.setVisualEffectOnCapture({
      visualEffect: 'hidden',
    });

    wx.hideShareMenu({
      menus: ['shareAppMessage', 'shareTimeline']
    })

  },
  initQqAd({
    openId,
  }) {
    console.log('initQqAd', openId);
    SDK.setDebug(true);
    const sdk = new SDK({
      // 数据源ID，必填
      user_action_set_id: '__GDT_ACTION_SET_ID__',
      // 账户ID，用于上报接口 body.account_id
      account_id: '__GDT_ACCOUNT_ID__',
      // 访问令牌，用于上报接口 header.access-token
      access_token: '__GDT_ACCESS_TOKEN__',
      // 微信小程序APPID（可选）
      appid: "__WX_APPID__",
      // 微信 openid，openid 和 unionid 只能填一个（优先填写openid）, 可以调用 setOpenId 设置
      openid: openId,
      // 是否开启自动采集，选填，默认为true
      auto_track: true,
    });
    const app = getApp();
    if (app && app.globalData) {
      app.globalData.DNSDK = sdk;
    }
  },
  globalData: {
    userInfo: null,
    DNSDK: null,
  }
})