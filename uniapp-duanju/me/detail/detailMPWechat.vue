<template>
	<view class="duanju">
<!--    <view>内容加载中...</view>-->
  </view>
</template>

<script>
import tracker from '../../common/tracker'
import empty from '../../components/empty.vue'
	// 引用的playerManager.js的代码可参考上面章节示例
	const PlayerManager = require('../../common/playerManager')
	const playletPlugin = requirePlugin('playlet-plugin')

	export default {
    components:{
      empty
    },
		data() {
			return {
				platform: '',
				playerManager: null,
        options: {}
			}
		},
		onLoad(option) {
      uni.showToast({
        title: '加载中，请稍等',
        icon: 'loading',
        duration: 5000,
        mask: true
      });
			uni.setStorageSync("path", "detailMpWechat");
			console.log('/me/detail/detailMPWecha', 'option', option)
      this.options = option
      try {
        const app = getApp()
        app.checkWechatSessionKey();
      } catch (e) {
        console.error("checkSession err" ,e)
      }

      try {
        // 注册播放器页面的onLoad事件
        playletPlugin.onPageLoad(this._onPlayerLoad.bind(this))
      } catch (e) {
        console.error("playletPlugin pageLoad error", e)
      }

			let that = this
			// 获取系统信息
			wx.getSystemInfo({
				success: function(res) {
					console.log(res);
					// res.platform 可能的值有 "android", "ios", "devtools", "windows"
					const platform = res.platform.toLowerCase();

					// 根据平台设置不同的样式或逻辑
					console.log('platform', platform);
					that.platform = platform;
				}
			});
			if (option.id) {
				this.courseId = option.id
				if (option.courseDetailsId) {
					this.courseDetailsId = option.courseDetailsId
				}

			}
      tracker.handleTrack("DetailMPWechatOnLoad")
		},
		onShow() {
			const path = uni.getStorageSync("path");
			if (path == 'playlet') {
				uni.switchTab({
					url: "/pages/index/index"
				})
				return;
			}
			// uni.$on('back', (data) => {
			// 	console.log()
			// 	uni.navigateBack({
			// 		delta: 2
			// 	})
			// })
			this.$Request.sysLog('浏览【观剧】页面，观看剧 id：' + this.courseId + '，集 id：' + this.courseDetailsId)
			// // 调用playerManager.js提供的navigateToPlayer，可参考上面章节的示例代码


      tracker.handleTrack("DetailMPWechatOnShow")

			// todo 查询
			setTimeout(() => {
				PlayerManager.navigateToPlayer({
					srcAppid: '__WX_APPID__',
					dramaId: this.courseId,
					extParam: encodeURIComponent('origin_drama=' + this.courseId + '&origin_serial=' + this
						.courseDetailsId), // 需要encode
				})
			}, 300)
		},
    onHide() {
      tracker.handleTrack("DetailMPWechatOnHide")
    },
		methods: {
			_onPlayerLoad(info) {
				const playerManager = new PlayerManager()
				this.playerManager = playerManager;
				playerManager._onPlayerLoad(info, this.options)
			},
		}
	}
</script>

<style scoped>
.duanju {
	width: 100%;
	height: 100%;
}
</style>