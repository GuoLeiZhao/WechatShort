const plugin = requirePlugin('playlet-plugin')
const { AD_UNIT_ID, COUNTDOWN_SECONDS, UNLOCK_EPISODES } = require('../../common/config')

let countdownTimer = null

class VideoAdHandler {
  constructor() {
    this.videoAd = null
  }

  init() {
    if (!wx.createRewardedVideoAd) return
    this.videoAd = wx.createRewardedVideoAd({ adUnitId: AD_UNIT_ID })
  }

  load() {
    if (!this.videoAd) return Promise.resolve()
    return this.videoAd.load().catch(err => {
      console.error('广告预加载失败', err)
    })
  }

  show(onFail) {
    if (!this.videoAd) {
      onFail && onFail()
      return
    }
    this.videoAd.show().catch((err) => {
      console.error('激励视频广告显示失败', err)
      if (errCode == 2007) {
        onFail && onFail(err)
      }
    })
  }

  onClose(cb) {
    if (!this.videoAd) return
    this.videoAd.onClose(cb)
  }

  onLoad(cb) {
    if (!this.videoAd) return
    this.videoAd.onLoad(cb)
  }

  onError(cb) {
    if (!this.videoAd) return
    this.videoAd.onError(cb)
  }

  destroy() {
    if (!this.videoAd) return
    try { this.videoAd.destroy() } catch (e) { /* ignore */ }
    this.videoAd = null
  }
}

let videoAd = null

Component({
  properties: {
    playerId: {
      type: String,
      value: ''
    },
    serialNo: {
      type: Number,
      value: 0
    },
    item: {
      type: Object,
      value: {}
    },
    countdownSeconds: {
      type: Number,
      value: COUNTDOWN_SECONDS
    },
    unlockEpisodes: {
      type: Number,
      value: UNLOCK_EPISODES
    }
  },

  data: {
    countdown: 0,
    isCountdownActive: false,
    showUnlockText: false,
    unlocked: false,
  },

  lifetimes: {
    created() {
      this._unlockedSet = new Set()
      this._storageKey = 'unlocked_serials_default'
      videoAd = new VideoAdHandler()
      videoAd.init()
      videoAd.onLoad(() => console.log('激励视频广告已加载'))
      videoAd.onError(err => console.error('激励视频广告加载失败', err))
      videoAd.onClose((res) => {
        if (res && res.isEnded) {
          this.handleUnlock(this.properties.item.serialNo || this.data.serialNo)
        } else {
          console.log('用户未完整观看广告，不解锁')
        }
      })
    },

    attached() {
      this._initStorageKey()
      this.listenAdUnlock()
      // 直接进入已锁定剧集时，observer 可能在 attached 前就触发，这里补一次
      if (this.data.item && this.data.item.type === 'unlock' && !this.data.isCountdownActive) {
        const serialNo = Number(this.data.item.serialNo || this.data.serialNo)
        if (serialNo && this._unlockedSet.has(serialNo)) {
          this.handleUnlock(serialNo)
        } else {
          this._startUnlockFlow()
        }
      }
    },

    detached() {
      this.stopCountdown()
      videoAd && videoAd.destroy()
      videoAd = null
    }
  },

  observers: {
    'item.type': function (type) {
      if (type === 'unlock') {
        this.setData({ unlocked: false })
        this._initStorageKey()
        this.listenAdUnlock()

        const serialNo = Number(this.properties.item.serialNo || this.data.serialNo)
        if (serialNo && this._unlockedSet.has(serialNo)) {
          this.handleUnlock(serialNo)
          return
        }

        this._startUnlockFlow()
      }
    }
  },

  methods: {
    getPlayerManager() {
      return plugin.PlayletManager.getPageManager(this.data.playerId)
    },

    _initStorageKey() {
      try {
        const info = this.getPlayerManager().getInfo()
        const dramaId = (info && info.dramaId) || wx.getStorageSync('current_drama_id') || 'default'
        this._storageKey = `unlocked_serials_${dramaId}`
        wx.setStorageSync('current_drama_id', dramaId)
      } catch (e) {
        const dramaId = wx.getStorageSync('current_drama_id') || 'default'
        this._storageKey = `unlocked_serials_${dramaId}`
      }
      const saved = wx.getStorageSync(this._storageKey) || []
      this._unlockedSet = new Set(saved)
    },

    _startUnlockFlow() {
      try {
        this.getPlayerManager().startBackgroundPreview({
          loop: false,
          mask: false,
          muted: false,
        })
      } catch (e) {
        console.error('startBackgroundPreview 失败', e)
      }
      this.startCountdown()
    },

    listenAdUnlock() {
      if (this._listenersRegistered) return
      this._listenersRegistered = true
      const pm = this.getPlayerManager()

      pm.onCheckIsCanPlay((param) => {
        const serialNo = Number(param.serialNo)
        const status = this._unlockedSet.has(serialNo) ? 1 : 2
        pm.isCanPlay({
          serialNo,
          serialList: [{ start_serial_no: serialNo, end_serial_no: serialNo, status }],
        })
      })

      pm.onUseAdUnlock(() => {
        console.log('pm.onUseAdUnlock triggered')
        this.showAd()
      })
    },

    showAd() {
      videoAd && videoAd.show(() => {
        this.handleUnlock(this.properties.item.serialNo || this.data.serialNo)
      })
    },

    preloadAd() {
      videoAd && videoAd.load()
    },

    startCountdown() {
      this.stopCountdown()
      const seconds = this.data.countdownSeconds
      this.setData({ countdown: seconds, isCountdownActive: true, showUnlockText: false })

      countdownTimer = setInterval(() => {
        const cur = this.data.countdown
        if (cur <= 1) {
          clearInterval(countdownTimer)
          countdownTimer = null
          this.getPlayerManager().stopBackgroundPreview()
          this.setData({ countdown: 0, isCountdownActive: false, showUnlockText: true })
          this.showAd()
        } else {
          this.setData({ countdown: cur - 1 })
        }
      }, 1000)
    },

    stopCountdown() {
      if (countdownTimer) {
        clearInterval(countdownTimer)
        countdownTimer = null
      }
      this.setData({ countdown: 0, isCountdownActive: false, showUnlockText: false })
    },

    onGoBack() {
      videoAd && videoAd.show()
      this.getPlayerManager().navigateBack({
        url: 'pages/theater/theater',
      })
    },

    onNextUnlock() {
      this.stopCountdown()
      this.getPlayerManager().stopBackgroundPreview()
      this.setData({ showUnlockText: true })
    },

    handleUnlock(serialNo) {
      if (!serialNo) return
      this.stopCountdown()
      this.setData({ unlocked: true })
      const num = Number(serialNo)
      this._unlockedSet.add(num)
      wx.setStorageSync(this._storageKey, Array.from(this._unlockedSet))
      const pm = this.getPlayerManager()
      pm.isCanPlay({
        serialNo: num,
        serialList: [{ start_serial_no: num, end_serial_no: num, status: 1 }],
      })
    },
  }
})
