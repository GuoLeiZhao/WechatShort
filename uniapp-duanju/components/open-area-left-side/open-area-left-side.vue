<template>
	<view>
		<view class="container" v-if="item && item.display == 'right&top'">
			<view class="content">
				<view class="content-left">{{countdown}}秒 后播放广告</view>
				<view class="content-right" @tap="close()">暂不观看</view>
			</view>
		</view>

		<view v-if="item && item.display == 'bottom'" class="bottom-container">
			<view class="bottom-content">
				<view class="bottom-content-left" @tap="close()">下次再说</view>
				<view class="bottom-content-right">{{countdown}} 秒后自动跳转广告</view>
			</view>
		</view>
	</view>
</template>

<script>
import HttpRequest from '@/common/httpRequest.js'
import tracker from "../../common/tracker";
	var plugin = requirePlugin("playlet-plugin");
	export default {
		props: {
			ext: {},
			item: {}
		},
		data() {
			return {
				countdown: 5, // 倒计时秒数
				intervalId: null, // 定时器id
				videoAd: null, // 激励广告
				pm: null,
			};
		},
		mounted() {
			console.log('item', this.item)
			this.pm = plugin.PlayletManager.getPageManager(this.ext)
			this.getAd();
		},
		methods: {
      async uploadAdEvent(data) {
        const openId = uni.getStorageSync("openId");
        const clickId = uni.getStorageSync("gdt_vid");
        if (typeof data === 'object') {
          let pp = {}
          if (data.remark) {
            pp.errMsg = data.remark
          }
          tracker.handleTrack(data.eventType, pp)
          await HttpRequest.postJson("/app/ad_event/advert", {
            ...data,
            clickId,
            openId,
            path: 'components/open-area-left-side'
          })
        }
      },
			startCountdown() {
				if (this.item && this.item.display == 'bottom') {
					this.countdown = 7;
				}
				this.intervalId = setInterval(() => {
					if (this.countdown > 1) {
						this.countdown--;
					} else {
						clearInterval(this.intervalId);
						this.intervalId = null;
						this.countdown = 0;
						this.showAd()
					}
				}, 1000);
			},

			getAd() {
        const that = this;
				let videoAd = null;
				if (wx.createRewardedVideoAd) {
					videoAd = wx.createRewardedVideoAd({
						adUnitId: '__AD_UNIT_ID__'
					});
					this.startCountdown()
					videoAd.onLoad(() => {});
					videoAd.onError((err) => {
						console.error('激励视频广告加载失败', err);
					});
					videoAd.onClose(res => {
						this.pm.updateOpenArea({
							showLeft: false,
							leftsideAreaList: []
						})
						if (res && res.isEnded) {
							const info = this.pm.getInfo();
              let initUnlockSerialNo = uni.getStorageSync('initUnlockSerialNo') || 6;
							console.log('解锁剧集2', initUnlockSerialNo + 1);
							this.pm.isCanPlay({
								serialNo: initUnlockSerialNo + 1,
								serialList: [{
									start_serial_no: initUnlockSerialNo + 1,
									end_serial_no: initUnlockSerialNo + 1,
									status: 1, // 解锁
								}],
							})
              uni.setStorageSync('initUnlockSerialNo', initUnlockSerialNo + 1)
						} else {
							// 播放中途退出
						}
					})
				}

				this.videoAd = videoAd
			},

			showAd() {
        const that = this;
				let videoAd = this.videoAd
				if (videoAd) {
					videoAd.show().then(() => {
            that.uploadAdEvent({
              eventType: "ON_SHOW_SUCCESS",
              advertType: "INCENTIVE_VIDEO"
            })
          }).catch((err) => {
            that.uploadAdEvent({
              eventType: "ON_SHOW_FAIL",
              advertType: "INCENTIVE_VIDEO",
              remark: err?.errMsg
            })
						videoAd.load()
							.then(() => {
                videoAd.show().then(() => {
                  that.uploadAdEvent({
                    eventType: "ON_SHOW_SUCCESS",
                    advertType: "INCENTIVE_VIDEO"
                  })
                }).catch(err => {
                  that.uploadAdEvent({
                    eventType: "ON_SHOW_FAIL",
                    advertType: "INCENTIVE_VIDEO",
                    remark: err?.errMsg
                  })
                })
              })
							.catch(err => {
								console.error('激励视频广告显示失败', err);
                that.pm.updateOpenArea({
									showLeft: false, // 显示 open-area-left 组件
									leftsideAreaList: []
								})
                that.uploadAdEvent({
                  eventType: "ON_SHOW_FAIL",
                  advertType: "INCENTIVE_VIDEO",
                  remark: err?.errMsg
                })
							});
					});
				}
			},

			close() {
				this.$trackEvent(`moon_close_ad_${this.item ? this.item.display : 'unknown'}`);
				console.log('关闭广告', this.pm)
				clearInterval(this.intervalId);
				this.intervalId = null;
				uni.setStorageSync('areaOpened', '0')
				this.pm.updateOpenArea({
					showLeft: false, // 显示 open-area-left 组件
					leftsideAreaList: []
				})
			}
		}
	};
</script>

<style>
	.container {
		display: flex;
		flex-direction: column;
		align-items: flex-end;
	}

	.content {
		width: 440rpx;
		display: flex;
		flex-direction: row;
		justify-content: center;
		color: aliceblue;
		font-size: 30rpx;
	}

	.content-left {
		background: rgba(255, 255, 255, 0.08);
		border: 1px solid rgba(255, 255, 255, 0.25);
		box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
		padding: 20rpx;
		border-radius: 50rpx;
		border-top-right-radius: 0;
		border-bottom-right-radius: 0;
	}

	.content-right {
		background: rgba(255, 255, 255, 0.08);
		border: 1px solid rgba(255, 255, 255, 0.25);
		box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
		padding: 20rpx;
		border-radius: 50rpx;
		border-left: none;
		border-top-left-radius: 0;
		border-bottom-left-radius: 0;
	}

	.bottom-container {}

	.bottom-content {
		color: white;
		display: flex;
		flex-direction: row;
		justify-content: space-around;
		gap: 40rpx;
		margin: 40rpx;
	}

	.bottom-content-left {
		flex: 1;
		background: rgba(255, 255, 255, 0.08);
		border: 1px solid rgba(255, 255, 255, 0.25);
		box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
		padding: 20rpx;
		border-radius: 10rpx;
		text-align: center;
	}

	.bottom-content-right {
		flex: 1;
		background: rgba(255, 255, 255, 0.08);
		border: 1px solid rgba(255, 255, 255, 0.25);
		box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
		padding: 20rpx;
		border-radius: 10rpx;
		text-align: center;
	}
</style>