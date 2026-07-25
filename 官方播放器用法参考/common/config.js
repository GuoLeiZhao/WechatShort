/** 后台接口地址 */
const SERVER_BASE_URL = 'https://your-api-domain.com/api'
// const SERVER_BASE_URL = 'http://localhost:8185/api'

// ---- 本地默认值（后台未配置时兜底） ----
let AD_UNIT_ID = '__AD_UNIT_ID__'
let COUNTDOWN_SECONDS = 3
let UNLOCK_EPISODES = 1
let SRC_APPID = '__WX_APPID__'

/**
 * 从后台拉取配置并覆盖本地默认值
 * 在 app.js onLaunch 中尽早调用，返回 Promise
 */
function initRemoteConfig() {
  return new Promise((resolve) => {
    wx.request({
      url: `${SERVER_BASE_URL}/sys/config/public`,
      method: 'GET',
      success(res) {
        if (res.statusCode === 200 && res.data && res.data.code === 0) {
          const data = res.data.data || {}
          if (data.ad_unit_id)        AD_UNIT_ID        = data.ad_unit_id
          if (data.countdown_seconds) COUNTDOWN_SECONDS = Number(data.countdown_seconds)
          if (data.unlock_episodes)   UNLOCK_EPISODES   = Number(data.unlock_episodes)
          if (data.src_appid)         SRC_APPID         = data.src_appid
          console.log('远程配置已加载', data)
        } else {
          console.warn('远程配置加载失败，使用本地默认值', res)
        }
        resolve()
      },
      fail(err) {
        console.warn('远程配置请求异常，使用本地默认值', err)
        resolve()
      }
    })
  })
}

// 使用 getter 确保组件/页面拿到的始终是最新值
module.exports = {
  SERVER_BASE_URL,
  initRemoteConfig,
  get AD_UNIT_ID()        { return AD_UNIT_ID },
  get COUNTDOWN_SECONDS() { return COUNTDOWN_SECONDS },
  get UNLOCK_EPISODES()   { return UNLOCK_EPISODES },
  get SRC_APPID()         { return SRC_APPID },
}
