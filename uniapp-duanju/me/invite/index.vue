<!-- 我的邀请 -->
<template>
	<view>
		<u-image src="@/me/static/invite/juxing.png" alt="" width="100%" mode="widthFix"></u-image>
		<view class="padding" style="position: relative;height: 360rpx;">
			<u-image src="@/me/static/invite/fenxiang.png" alt="" width="100%" height="100%"></u-image>
			<view class="text-center padding-top yaoqing">
				<view class="margin-top-xl margin-bottom-sm padding-top-sm text-xxl text-red text-bold">
					{{invitationCode}}
				</view>
				<!-- #ifndef MP-WEIXIN -->
				<u-button @tap="sharurl" :custom-style="customStyle" :hair-line="false" shape="circle" size="mini"
					:ripple="true">一键分享</u-button>
				<!-- #endif -->
				<!-- #ifdef MP-WEIXIN -->
				<u-button open-type="share" :custom-style="customStyle" :hair-line="false" shape="circle" size="mini"
					:ripple="true">一键分享</u-button>
				<!-- #endif -->
				<u-button style="margin-left: 50rpx;" @click.stop="onSaveImg()" :custom-style="customStyle"
					:hair-line="false" shape="circle" size="mini" :ripple="true">保存海报</u-button>
			</view>
		</view>
		<view class="margin padding bg-white radius" v-if="inviterList.length">
			<view class="flex justify-between" style="line-height: 80rpx;" v-for="(item, index) in inviterList"
				:key='index'>
				<view class="flex">
					<!-- <u-image :src="item.avatar == null?avatar:item.avatar" width="40px" mode="widthFix"></u-image> -->
					<image src="../static/invite/5.png" style="width: 80rpx;" mode="widthFix"></image>
					<text class="margin-left-sm">{{item.userName}}</text>
				</view>
				<view style="width: 160rpx;">
					<text>获得</text>
					<text class="margin-left-xs text-red">{{item.money}}</text>
				</view>
			</view>
		</view>
		<view class="padding" style="position: relative;height: 160px;">
			<u-image src="@/me/static/invite/yaoqing.png" alt="" width="100%" height="100%"></u-image>
			<view class="text-center padding-top flex justify-center flex-direction zhanji">
				<view class="flex justify-around margin-top-sm" @click="goNav('/me/invite/inviteDet')">
					<view>
						<view class="margin-bottom-sm">已邀请</view>
						<view class="text-red"><text class="text-bold u-font-18">{{inviterNumber}}</text>人</view>
					</view>
					<view>
						<view class="margin-bottom-sm">累计收益</view>
						<view class="text-red"><text class="text-bold u-font-18">{{cumulativeRevenue}}</text>元</view>
					</view>
					<view>
						<view class="margin-bottom-sm">已提现</view>
						<view class="text-red">¥<text class="text-bold u-font-18">{{withdrawn}}</text></view>
					</view>
				</view>
			</view>
		</view>
		<view class="padding" style="position: relative;height: 250rpx;">
			<u-image src="@/me/static/invite/jiqiao.png" alt="" width="100%" height="100%"></u-image>
			<view class="padding-top jiqiao" style="">
				<view class="padding-top-xl padding-lr padding-bottom" style="line-height: 48rpx;">
					<view class="flex padding-sm">
						<text class="cuIcon-title text-yellow"></text>
						<view>邀请好友可得开通会员及消费金额 {{price}}%的佣金奖励</view>
					</view>
				</view>
			</view>
		</view>

		<!-- #ifndef MP-WEIXIN -->
		<tki-qrcode ref="qrcode" :val="erweima" :size="200" background="#fff" foreground="#000" pdground="#000"
			:onval="true" :loadMake="true" @result="qrR" :show="false"></tki-qrcode>
		<view class="cu-modal" :class="modalName == 'Image' ? 'show' : ''" @tap="hideModal">
			<view class="cu-dialog" v-if="bgImg && erweimapath && haibaoShow" @tap="hideModal">
				<view class="bg-img">
					<wm-poster @success="posterSuccess" :imgSrc="bgImg" :Referrer="'我的邀请码:'+invitationCode"
						:QrSrc="erweimapath" :LineType="false"></wm-poster>
				</view>
			</view>
		</view>
		<!-- #endif -->

		<!-- #ifdef MP-WEIXIN -->

		<view @tap="hideModal" :class="modalName == 'Image' ? 'show' : ''" class="modal"
			style="text-align: center;display: flex;justify-content: center;">
			<view style="width:100%;margin: auto;">
				<image :src="h5SaveImg" mode="widthFix" style="width: 90%;"></image>
			</view>
		</view>
		<canvas canvas-id="poster" class="poster_canvas"></canvas>
		<!-- #endif -->
	</view>
</template>

<script>
	let settingWritePhotosAlbum = false;
	import tkiQrcode from '../../components/tki-qrcode/tki-qrcode.vue';
	import wmPoster from '../components/wm-poster/wm-posterorders.vue';
	import config from '../../common/config.js'
	export default {
		components: {
			tkiQrcode,
			wmPoster
		},
		data() {
			return {
				erweimapath: '',
				poster: {},
				qrShow: false,
				haibaoImg: null,
				haibaoShow: false,
				modalName: '',
				canvasId: 'default_PosterCanvasId',
				avatar: 'https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/logo.png',
				customStyle: {
					background: '#FFE0E0',
					border: '0',
					color: 'rgb(254,30,35)',
					fontSize: '12px',
					fontWeight: '500',
					padding: '15px 20px',
				},
				page: 1,
				limit: 10,
				inviterName: '', //邀请码
				cumulativeRevenue: '', //累计收益
				inviterNumber: '', //邀请人数
				withdrawn: '', //已提现
				inviterList: [],
				erweima: '',
				bgImg: '',
				tuiguang: '',
				invitationCode: 0,
				price: 0,
				h5SaveImg: '',
				modalName: '',
			}
		},
		onShareAppMessage(res) {
			return {
				path: '/pages/index/index?invitation=' + this.invitationCode, //这是为了传参   onload(data){let id=data.id;} 
				title: this.tuiguang,
				imageUrl: this.bgImg
			}
		},
		onLoad() {
			this.getInviter()
			this.queryInviter()

			this.invitationCode = uni.getStorageSync('invitationCode')

			this.$u.get('app/common/type/80').then(res => {
				this.price = res.data.value * 100
			});
			this.getBgImg()

			// #ifdef MP-WEIXIN

			this.erweima = config.APIHOST2 + '/sqx_fast/app/invite/mpCreateQr?invitationCode=' + this
				.invitationCode
			// #endif
			// #ifndef MP-WEIXIN
			this.erweima = config.APIHOST2 + '?invitation=' + this
				.invitationCode
			// #endif
		},
		methods: {
			sharurl() {
				let that = this;
				uni.showModal({
					title: '链接推广',
					content: this.tuiguang + this.erweima,
					showCancel: true,
					cancelText: '关闭',
					confirmText: '一键复制',
					success: res => {
						if (res.confirm) {
							uni.setClipboardData({
								data: this.tuiguang + this.erweima,
								success: function() {
									console.log('success');
									uni.showToast({
										title: '复制成功',
										duration: 1000,
										icon: 'none'
									});
								}
							});
						}
					}
				});
			},
			posterSuccess(haibaoImg) {
				this.haibaoImg = haibaoImg;
				this.modalName = 'Image';
				uni.hideLoading();
			},
			showModal() {
				if (!this.haibaoImg) {
					this.haibaoShow = true;
					uni.showLoading({
						title: '海报生成中'
					});
				} else {
					this.modalName = 'Image';
				}
			},
			hideModal() {
				this.modalName = null;
			},
			qrR(path) {
				this.erweimapath = path;
			},
			getInviter() {
				let data = {
					page: this.page,
					limit: this.limit
				}
				this.$u.api.inviter(data).then(res => {
					if (res.code == 0) {
						this.inviterList = res.data.list
					} else {
						uni.showToast({
							title: res.msg,
							duration: 1000,
							icon: 'none'
						});
					}
				})
			},
			queryInviter() {
				this.$u.api.queryInviter().then(res => {
					if (res.code == 0) {
						this.cumulativeRevenue = res.data.inviteMoney.moneySum //累计收益
						this.inviterNumber = res.data.inviteCount //邀请人数
						this.withdrawn = res.data.inviteMoney.cashOut //已提现
					} else {
						uni.showToast({
							title: res.msg,
							duration: 1000,
							icon: 'none'
						});
					}
				})
			},


			goNav(e) {
				uni.navigateTo({
					url: e
				})
			},

			//获取背景图
			getBgImg() {
				this.$u.get('app/banner/selectBannerList?classify=5').then(res => {
					if (res.code == 0) {
						this.bgImg = res.data[0].imageUrl
						this.tuiguang = res.data[0].describes
						console.log(this.bgImg)
					} else {
						uni.showToast({
							title: res.msg,
							duration: 1000,
							icon: 'none'
						});
					}
				});
			},

			//生成h5海报
			createH5Poster() {
				let that = this;
				return new Promise((resolve, reject) => {
					uni.showLoading({
						title: '海报生成中'
					});
					const ctx = uni.createCanvasContext('poster');
					ctx.fillRect(0, 0, 375, 673);
					ctx.setFillStyle("#FFF");
					ctx.fillRect(0, 0, 375, 673);
					let imgUrl = that.bgImg;
					uni.downloadFile({
						url: imgUrl,
						success: (res) => {
							console.log(res, '***************')
							if (res.statusCode === 200) {
								uni.downloadFile({
									url: that.erweima,
									success: (res2) => {
										console.log(res2)
										if (res.statusCode === 200) {
											ctx.drawImage(res.tempFilePath, 0, 0, 375,
												500);
											// 长按识别二维码访问
											let textTop = 0;
											ctx.setFontSize(19);
											ctx.setFillStyle('#333');
											ctx.fillText("长按识别图中二维码", 17, textTop + 590);
											// 二维码
											ctx.drawImage(res2.tempFilePath, 238, textTop +
												526, 120, 120);
											ctx.draw(true, () => {
												// canvas画布转成图片并返回图片地址
												uni.canvasToTempFilePath({
													canvasId: 'poster',
													width: 375,
													height: 673,
													success: (res) => {
														console.log(
															"海报制作成功！"
														);
														resolve(res
															.tempFilePath
														);
													},
													fail: () => {
														uni
															.hideLoading();
														reject();
													}
												})
											});
										} else {
											uni.hideLoading();
											uni.showToast({
												title: '海报制作失败，图片下载失败',
												icon: 'none'
											});
										}
									},
									fail: err => {
										console.log(err)
										uni.hideLoading();
										uni.showToast({
											title: '海报制作失败，图片下载失败',
											icon: 'none'
										});
									},
									complete: com => {
										console.log(com)
										uni.showToast({
											title: com,
											icon: 'none'
										});
									},
								});
							} else {
								uni.hideLoading();
								uni.showToast({
									title: '海报制作失败，图片下载失败',
									icon: 'none'
								});
							}
						},
						fail: err => {
							// that.yu.toast(err)
							console.log(err)
							uni.hideLoading();
							uni.showToast({
								title: '海报制作失败，图片下载失败',
								icon: 'none',
							});
						}
					});
				});
			},

			//生成海报
			createPoster() {
				let that = this;
				return new Promise((resolve, reject) => {
					uni.showLoading({
						title: '海报生成中'
					});
					const ctx = uni.createCanvasContext('poster');
					ctx.fillRect(0, 0, 375, 673);
					ctx.setFillStyle("#FFF");
					ctx.fillRect(0, 0, 375, 673);
					let imgUrl = that.bgImg;
					uni.downloadFile({
						url: imgUrl,
						success: (res) => {
							console.log(res, '***************')
							if (res.statusCode === 200) {
								uni.downloadFile({
									url: config.APIHOST +
										'/app/invite/mpCreateQr?invitationCode=' + that
										.invitationCode,
									// url: 'https://your-cdn-domain.com/sqx_fast/app/invite/mpCreateQr?invitationCode=' +
									// 	that.invitationCode,
									success: (res2) => {
										console.log(res2)
										if (res.statusCode === 200) {
											ctx.drawImage(res.tempFilePath, 0, 0, 375,
												500);
											// 长按识别二维码访问
											let textTop = 0;
											ctx.setFontSize(19);
											ctx.setFillStyle('#333');
											ctx.fillText("长按识别图中二维码", 17, textTop + 590);
											// 二维码
											ctx.drawImage(res2.tempFilePath, 238, textTop +
												526, 120, 120);
											ctx.draw(true, () => {
												// canvas画布转成图片并返回图片地址
												uni.canvasToTempFilePath({
													canvasId: 'poster',
													width: 375,
													height: 673,
													success: (res) => {
														console.log(
															"海报制作成功！"
														);
														resolve(res
															.tempFilePath
														);
													},
													fail: () => {
														uni
															.hideLoading();
														reject();
													}
												})
											});
										} else {
											uni.hideLoading();
											uni.showToast({
												title: '海报制作失败，图片下载失败',
												icon: 'none'
											});
										}
									},
									fail: err => {
										console.log(err)
										uni.hideLoading();
										uni.showToast({
											title: '海报制作失败，图片下载失败',
											icon: 'none'
										});
									},
									complete: com => {
										console.log(com)
										uni.showToast({
											title: com,
											icon: 'none'
										});
									},
								});
							} else {
								uni.hideLoading();
								uni.showToast({
									title: '海报制作失败，图片下载失败',
									icon: 'none'
								});
							}
						},
						fail: err => {
							// that.yu.toast(err)
							console.log(err)
							uni.hideLoading();
							uni.showToast({
								title: '海报制作失败，图片下载失败',
								icon: 'none',
							});
						}
					});
				});
			},
			// 保存图片
			async onSaveImg() {
				// #ifndef MP-WEIXIN
				this.showModal();
				// #endif

				// #ifdef MP-WEIXIN
				let imgUrl = await this.createPoster();
				uni.showLoading({
					title: '海报下载中'
				});
				if (settingWritePhotosAlbum) {
					uni.getSetting({
						success: res => {
							if (res.authSetting['scope.writePhotosAlbum']) {
								uni.saveImageToPhotosAlbum({
									filePath: imgUrl,
									success: () => {
										uni.hideLoading();
										uni.showToast({
											title: '保存成功'
										});
									}
								});
							} else {
								uni.showModal({
									title: '提示',
									content: '请先在设置页面打开“保存相册”使用权限',
									confirmText: '去设置',
									cancelText: '算了',
									success: data => {
										if (data.confirm) {
											uni.hideLoading();
											uni.openSetting();
										}
									}
								});
							}
						}
					});
				} else {
					uni.hideLoading();
					settingWritePhotosAlbum = true;
					uni.authorize({
						scope: 'scope.writePhotosAlbum',
						success: () => {
							uni.saveImageToPhotosAlbum({
								filePath: imgUrl,
								success: () => {
									uni.hideLoading();
									uni.showToast({
										title: '保存成功'
									});
								}
							});
						}
					});
				}
				// #endif
			},
		}
	}
</script>

<style>
	.modal {
		position: fixed;
		top: 0;
		right: 0;
		bottom: 0;
		left: 0;
		z-index: 1110;
		opacity: 0;
		outline: 0;
		text-align: center;
		-ms-transform: scale(1.185);
		transform: scale(1.185);
		backface-visibility: hidden;
		perspective: 2000upx;
		background: rgba(0, 0, 0, 0.6);
		transition: all 0.3s ease-in-out 0s;
		pointer-events: none;
	}

	.modal.show {
		opacity: 1;
		transition-duration: 0.3s;
		-ms-transform: scale(1);
		transform: scale(1);
		overflow-x: hidden;
		overflow-y: auto;
		pointer-events: auto;
	}

	page {
		background: #E0EFFF;
	}

	img {
		display: block;
	}

	.poster_canvas {
		width: 750upx;
		height: 1334upx;
		position: fixed;
		top: -10000upx;
		left: 0;
	}

	.yaoqing {
		width: 100%;
		height: 136px;
		position: absolute;
		margin: auto;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		/* background-image: url('@/me/static/invite/fenxiang.png'); */
		/* background-size: 100%; */
		/* background-repeat: no-repeat; */
	}

	.zhanji {
		width: 100%;
		height: 119px;
		position: absolute;
		margin: auto;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		/* background-image: url('@/me/static/invite/yaoqing.png'); */
		/* background-size: 100%; */
		/* background-repeat: no-repeat; */
	}

	.jiqiao {
		width: 100%;
		height: 160rpx;
		position: absolute;
		margin: auto;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		/* 	background-image: url('@/me/static/invite/jiqiao.png');
		background-size: 100%;
		background-repeat: no-repeat; */
	}
</style>