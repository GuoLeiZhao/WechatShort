// pages/theater/theater.js
const { SRC_APPID, SERVER_BASE_URL } = require('../../common/config')

// 短剧插件固定 appid，不随后台配置变化
const PLUGIN_APPID = 'wx94a6522b1d640c3b'


Page({
  data: {
    statusBarHeight: 0,
    srcAppid: SRC_APPID,
    dramaId: '',
    gdt_vid: '',

    dramas: [],
    page: 1,
    limit: 10,
    hasMore: true,
    loading: false
  },

  onLoad() {
    const gdt_vid = wx.getStorageSync('gdt_vid')
    const { statusBarHeight } = wx.getSystemInfoSync()

    this.setData({
      statusBarHeight,
      gdt_vid,
      dramaId: wx.getStorageSync('dramaId'),
      srcAppid: SRC_APPID
    })

    this.loadDramas()
  },

  // 下拉刷新
  onPullDownRefresh() {
    this.setData({ dramas: [], page: 1, hasMore: true }, () => {
      this.loadDramas(() => wx.stopPullDownRefresh())
    })
  },

  // 触底加载更多
  onReachBottom() {
    if (!this.data.hasMore || this.data.loading) return
    this.loadDramas()
  },

  loadDramas(callback) {
    const { page, limit, loading, hasMore } = this.data
    if (loading || !hasMore) return

    this.setData({ loading: true })

    wx.request({
      url: `${SERVER_BASE_URL}/app/drama/list`,
      method: 'GET',
      data: { page, limit },
      success: (res) => {
        if (res.statusCode === 200 && res.data && res.data.code === 0) {
          const { dramas: newItems = [], hasMore: more, currPage } = res.data
          const mapped = newItems.map(item => ({
            id: item.dramaId,
            title: item.dramaName,
            poster: item.coverUrl,
            description: item.description
          }))
          this.setData({
            dramas: [...this.data.dramas, ...mapped],
            page: currPage + 1,
            hasMore: more
          })
        } else {
          console.warn('获取剧目列表失败', res)
        }
      },
      fail(err) {
        console.warn('获取剧目列表请求异常', err)
      },
      complete: () => {
        this.setData({ loading: false })
        callback && callback()
      }
    })
  },

  // 点击看剧
  onWatch(e) {
    const dramaId = e.currentTarget.dataset.id
    this.navigateToDrama(dramaId)
  },

  // 统一跳转到短剧插件
  navigateToDrama(dramaId) {
    const { srcAppid } = this.data

    if (!srcAppid) {
      wx.showToast({ icon: 'none', title: '请先设置srcAppid' })
      return
    }

    const targetDramaId = dramaId || this.data.dramaId
    if (!targetDramaId) {
      console.warn('navigateToDrama: 无有效dramaId')
      return
    }

    wx.setStorageSync('current_drama_id', targetDramaId)

    const unlockedSerials = wx.getStorageSync(`unlocked_serials_${targetDramaId}`) || []
    const serialNoParam = unlockedSerials.length > 0 ? `&serialNo=${Math.max(...unlockedSerials)}` : ''

    wx.navigateTo({
      url: `plugin-private://${PLUGIN_APPID}/pages/playlet/playlet?dramaId=${targetDramaId}&srcAppid=${srcAppid}${serialNoParam}`
    })
  }
})
