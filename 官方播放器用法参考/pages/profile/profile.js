// pages/profile/profile.js
Page({
  data: {
    userInfo: {
      nickname: 'IEng08',
      userId: '835jgG',
      avatar: 'https://via.placeholder.com/200'
    },
    stats: {
      likes: 1,
      following: 0,
      points: 0
    },
    menuList: [
      {
        id: 1,
        title: '消息中心',
        icon: '/images/message.png'
      },
      {
        id: 2,
        title: '观看历史',
        icon: '/images/history.png'
      },
      {
        id: 3,
        title: '联系客服',
        icon: '/images/service.png'
      },
      {
        id: 4,
        title: '帮助中心',
        icon: '/images/help.png'
      },
      {
        id: 5,
        title: '意见反馈',
        icon: '/images/feedback.png'
      },
      {
        id: 6,
        title: '设置中心',
        icon: '/images/settings.png'
      }
    ],
    statusBarHeight: 0
  },

  onLoad(options) {
    // 获取状态栏高度
    const systemInfo = wx.getSystemInfoSync()
    this.setData({
      statusBarHeight: systemInfo.statusBarHeight
    })
  },

  onMoreTap() {
    // 点击更多按钮
    wx.showActionSheet({
      itemList: ['分享', '关于'],
      success: (res) => {
        console.log('选择了第' + (res.tapIndex + 1) + '个选项')
      }
    })
  },

  onSettingsTap() {
    // 点击设置按钮
    wx.navigateTo({
      url: '/pages/settings/settings'
    })
  },

  onMenuTap(e) {
    const { id } = e.currentTarget.dataset
    const menuMap = {
      1: '/pages/message/message',
      2: '/pages/history/history',
      3: '/pages/service/service',
      4: '/pages/help/help',
      5: '/pages/feedback/feedback',
      6: '/pages/settings/settings'
    }
    const url = menuMap[id]
    if (url) {
      wx.navigateTo({
        url: url
      })
    }
  },

  onStatTap(e) {
    const { type } = e.currentTarget.dataset
    const pageMap = {
      likes: '/pages/likes/likes',
      following: '/pages/following/following',
      points: '/pages/points/points'
    }
    const url = pageMap[type]
    if (url) {
      wx.navigateTo({
        url: url
      })
    }
  }
})

