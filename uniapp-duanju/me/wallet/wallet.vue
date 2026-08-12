<template>
	<view class="box flex flex-direction align-center justify-center">
		<view class="top flex flex-direction align-center justify-center">
			<view class="title-box flex flex-direction justify-center ">
				<span class="title" @click="test">当前余额</span>
				<view class="num-box flex">
					<span class="num">{{formatNumber(moneyNum)}}</span>
					<span @click="goNav('/me/wallet/mingxi')" class="mingxi">明细</span>
					<u-image class="img" src="https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/me/go-arrow.png" width="28rpx" height="28rpx" />
				</view>
				
			</view>
		</view>
		<view class="pay-box flex flex-direction">
			<view class="money">
				<view class="flex justify-between" style="align-items: baseline;">
					<span class="title">看点充值</span>
					<span class="tips">看点为虚拟商品，概不退换</span>
				</view>
				<view class="money-item-box flex flex-wrap">
					<view :class="currSelect == 'money-' + idx ? 'money-item-active' : 'money-item-inactive'" 
						v-for="(item, idx) in wallet" 
						:key="idx"
						@click="currSelect = 'money-' + idx"
					>
						<view class="flex align-center">
							<u-image width="35rpx" height="35rpx" src="https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/details/view.png"/>
							<span class="title-num">{{item.max}}</span>
						</view>
						<span class="payMoney">
							¥{{item.value}}
						</span>
						<view class="float">
							额外送{{item.min}}看点
						</view>
					</view>
				</view>
			</view>
			<!-- <view class="vip">
				<span class="title">VIP充值</span>
				<view class="vip-item-box flex flex-wrap">
					<view :class="currSelect == 'vip-' + idx ? 'vip-item-active' : 'vip-item-inactive'" 
						v-for="(item, idx) in VIPlist" 
						:key="idx"
						@click="currSelect = 'vip-' + idx"
					>
						<span class="vip-title">
							{{item.type}}卡VIP
						</span>
						<span class="payMoney">
							¥ {{item.money}}
						</span>
						<view class="tips flex align-center justify-center">
							每日仅需¥{{item.need}}
						</view>
					</view>
				</view>
			</view> -->
			
			<view class="btn-box">
				<view class="btn flex justify-center align-center" @click="pay()">
					确认支付
				</view>
			</view>
		</view>
		
	</view>	
</template>

<script>
	//导入地址
	import config from '../../common/config.js'
	export default {
		data() {
			return {
				openLists: [],
				openWay: 1,
				wallet: [],
				VIPlist: [],
				money: '',
				placeholder: '',
				moneyNum: 0,
				// money-item-active money-item-inactive
				currSelect: 'money-0',
				waitPay: 0,
				platform: 'android',
			}
		},
		onLoad() {
			// #ifdef MP-WEIXIN
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
			// #endif
		},
		onShow() {
			this.$Request.sysLog('浏览【余额充值】页面')
			this.getMyMoney()
			this.getMoneyList()
			this.getVIPDet()
		},
		methods: {
			test(){
				uni.navigateTo({
					url: 'plugin-private://wx94a6522b1d640c3b/pages/playlet/playlet?dramaId=100056&srcAppid=wx72e7f15848221f6a&serialNo=1&extParam=&bookId=41000101464&sid=1&channelId=34025&referral_id=25451857&from=no_back'
				});
			},
			/**
			 * 获取充值列表
			 */
			getMoneyList() {
				this.$Request.get('/app/common/type/condition/charge').then(res => {
					if (res.code == 0) {
						let arr = []
						this.wallet = res.data
					}
				});
			},
			getVIPDet() {
				this.$u.api.vipDet().then(res => {
					if (res.code == 0) {
						res.data.forEach(ret => {
							switch (ret.vipNameType) {
								case 0:
									ret.type = '月'
									ret.need = this.formatNumber(ret.money / 30)
									break;
								case 1:
									ret.type = '季'
									ret.need = this.formatNumber(ret.money / 90)
									break;
								case 2:
									ret.type = '年'
									ret.need = this.formatNumber(ret.money / 365)
									break;
							}
						})
						this.VIPlist = res.data
					} else {
						uni.showToast({
							title: res.msg,
							duration: 1000,
							icon: 'none'
						});
					}
				})
			},
			/**
			 * @param {Number} num
			 * @param 保留两位小数
			 */
			formatNumber(num) {
				// 判断是否为整数
				if (Number.isInteger(num)) {
					return num.toFixed(2);
				} else {
					return num.toFixed(2).replace(/\.?0+$/, '');
				}
			},
			/**
			 * 获取余额
			 */
			getMyMoney() {
				this.$Request.getT('/app/moneyDetails/selectUserMoney').then(res => {
					if (res.code == 0) {
						// this.moneyNum = res.data.money
						this.$Request.getT('/app/invite/selectInviteMoney').then(ret => {
							if (ret.code == 0) {
								if (ret.data.inviteMoney && ret.data.inviteMoney.money) {
									this.moneyNum = Number(res.data.money) + Number(ret.data.inviteMoney
										.money)
								} else {
									this.moneyNum = Number(res.data.money)
								}
							}
						})
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			//获取充值列表 
			getwalletList() {
				this.$Request.get("/app/payClassify/selectPayClassifyList").then(res => {
					if (res.code == 0) {
						this.wallet = res.data
					}
				});
			},
			goNav(url) {
				uni.navigateTo({
					url
				})
			},
			selectWay: function(item) {
				this.openWay = item.id;
			},
			pay(){
				
				if(!this.currSelect){
					uni.showToast({
						title: '未选择支付选项',
						icon: 'error'
					})
				}
				
				let type = this.currSelect.split('-')[0]
				let idx = this.currSelect.split('-')[1]
				
				// if ('money' == type){
				// 	let data = {
				// 		price: parseFloat(this.wallet[idx].value)
				// 	}
				// 	uni.navigateTo({
				// 		url: '/me/payOrder/payOrder?info=' + JSON.stringify(data)
				// 	})
				// 	this.$Request.sysLog('在【余额充值】页面充值看点，用户跳转【确认支付页面】, price: ' + parseFloat(this.wallet[idx].value))
				// } else {
				// 	let data = {
				// 		price: parseFloat(this.VIPlist[idx].money),
				// 		vip: this.VIPlist[idx],
				// 	}
				// 	this.$Request.sysLog('在【余额充值】页面充值 VIP，用户跳转【确认支付页面】, vipId: ' + this.VIPlist[idx].id)
				// 	uni.navigateTo({
				// 		url: '/me/payOrder/payOrder?info=' + JSON.stringify(data)
				// 	})
				// }
				
				let that = this
				let price = parseFloat(this.wallet[idx].value)
				this.waitPay = price;
				// #ifdef H5
				let ua = navigator.userAgent.toLowerCase();
				if (ua.indexOf('micromessenger') !== -1) {
					let data = {
						classify: 2,
						money: price
					}
					this.$Request.postT('/app/wxPay/payMoney', data).then(res => {
						if (res.code == 0) {
							that.callPay(res.data);
						} else {
							uni.showToast({
								icon: 'none',
								title: '支付失败!'
							});
						}
					});
				} else {
					uni.navigateTo({
						url: '/me/payOrder/payOrder?info=' + JSON.stringify(data)
					})
				}
				// #endif
				
				let userId = uni.getStorageSync('userId');
				
				// #ifdef MP-WEIXIN
				let platform = this.platform
				if (platform === 'android') {
					let sessionKey = uni.getStorageSync('sessionkey')
					let signData = {
						offerId: config.offerId,
						buyQuantity: parseInt(price * 100),
						env: 0,
						currencyType: 'CNY',
						attach: '',
					};
					this.$Request.postJson('/app/wechatOfficeAccount/getSign/' + config.TENANT, {
						sessionKey,
						signData,
						classify: 3,
						userId,
						money: parseFloat(price),
					}).then(ret => {
						// 安卓需要走虚拟支付
						wx.requestVirtualPayment({
							signData: ret.signData,
							paySig: ret.paySign,
							signature: ret.userSign,
							mode: 'short_series_coin',
							success(res) {
								// 使用以上方式判断前端返回,微信团队郑重提示：
								//res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
								uni.removeStorageSync('EditAddress')
								uni.showLoading({
									title: '支付成功'
								});
								uni.hideLoading();
								that.completeTask();
								setTimeout(function() {
									that.showPay = false;
									that.startPlay(that.current)
								}, 1000)
								that.$Request.sysLog('在【余额充值】页面充值成功，金额：' + that.waitPay)
							},
							fail({
								errMsg,
								errCode
							}) {
								if (errCode === -2) {
									that.showPay = false;
									that.$Request.sysLog('在【余额充值】页面用户取消充值，金额：' + that.waitPay)
								} else {
									that.$Request.sysLog('在【余额充值】页面充值失败，金额：' + that.waitPay + " 原因: " + errMsg)
									console.log(12345);
									uni.hideLoading();
								}
								console.error(errMsg, errCode)
							},
						})
					})
					
				} else {
					
					// 微信小程序支付
					this.$Request.postT('/app/wxPay/payMoney', {
						classify: 3,
						money: price,
					}).then(ret => {
						uni.hideLoading()
						console.log(ret.data)
						uni.requestPayment({
							provider: 'wxpay',
							timeStamp: ret.data.timestamp,
							nonceStr: ret.data.noncestr,
							package: ret.data.package,
							signType: ret.data.signType,
							paySign: ret.data.sign,
							success: function(suc) {
								console.log('success:' + JSON.stringify(suc));
								uni.showToast({
									title: '支付成功',
									icon: 'success'
								})
								that.$Request.sysLog('在【余额充值】页面充值成功，金额：' + that.waitPay)
							},
							fail: function(err) {
								console.log('fail:' + JSON.stringify(err));
								uni.showToast({
									title: '支付失败',
									icon: 'none'
								})
								that.$Request.sysLog('在【余额充值】页面充值失败，金额：' + that.waitPay)
								uni.hideLoading();
							}
						});
					});
				}
				// #endif
				
				// #ifdef MP-TOUTIAO
				this.$Request.postJson('/app/douyin/'+config.TENANT+'/createOrder', {
					userId,
					money: parseFloat(price),
				}).then(res => {
					tt.pay({
						orderInfo: {
							order_id: res.orderId,
							order_token: res.orderToken
						},
						service: 5,
						success(res) {
							//res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
							uni.removeStorageSync('EditAddress')
							if (res.code == 0) {
								uni.showLoading({
									title: '支付成功'
								});
								uni.hideLoading();
								that.completeTask();
								setTimeout(function() {
									that.showPay = false;
									that.startPlay(that.current)
								}, 1000)
								that.$Request.sysLog('在【余额充值】页面充值成功，金额：' + that.waitPay)
							} else if (res.code == 1) {
								that.showPay = false;
								that.$Request.sysLog('在【余额充值】页面用户支付超时，金额：' + that.waitPay)
							} else if (res.code == 2) {
								that.showPay = false;
								that.$Request.sysLog('在【余额充值】页面用户支付失败，金额：' + that.waitPay)
							} else if (res.code == 3) {
								that.showPay = false;
								that.$Request.sysLog('在【余额充值】页面用户支付关闭，金额：' + that.waitPay)
							} else if (res.code == 4) {
								that.showPay = false;
								that.$Request.sysLog('在【余额充值】页面用户取消充值，金额：' + that.waitPay)
							} else if (res.code == 9) {
								that.showPay = false;
								that.$Request.sysLog('在【余额充值】页面用户 订单状态开发者自行获取，金额：' + that.waitPay)
							}
							
							console.error(res)
						}
					})
				})
				
				// #endif
			},
			callPay: function(response) {
				if (typeof WeixinJSBridge === "undefined") {
					if (document.addEventListener) {
						document.addEventListener('WeixinJSBridgeReady', this.onBridgeReady(response), false);
					} else if (document.attachEvent) {
						document.attachEvent('WeixinJSBridgeReady', this.onBridgeReady(response));
						document.attachEvent('onWeixinJSBridgeReady', this.onBridgeReady(response));
					}
				} else {
					this.onBridgeReady(response);
				}
			},
			onBridgeReady: function(response) {
				let that = this;
				if (!response.package) {
					return;
				}
				WeixinJSBridge.invoke(
					'getBrandWCPayRequest', {
						"appId": response.appid, //公众号名称，由商户传入
						"timeStamp": response.timestamp, //时间戳，自1970年以来的秒数
						"nonceStr": response.noncestr, //随机串
						"package": response.package,
						"signType": response.signType, //微信签名方式：
						"paySign": response.sign //微信签名
					},
					function(res) {
						console.log('onBridgeReady', res)
						if (res.err_msg === "get_brand_wcpay_request:ok") {
							// 使用以上方式判断前端返回,微信团队郑重提示：
							//res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
							uni.removeStorageSync('EditAddress')
							uni.showLoading({
								title: '支付成功'
							});
							uni.hideLoading();
							that.$Request.sysLog('在【余额充值】页面充值成功，金额：' + that.waitPay)
						} else if (res.err_msg === 'get_brand_wcpay_request:cancel') {
							that.$Request.sysLog('在【余额充值】页面用户取消充值，金额：' + that.waitPay)
						} else {
							that.$Request.sysLog('在【余额充值】页面充值失败，金额：' + that.waitPay)
							console.log(12345);
							uni.hideLoading();
						}
						WeixinJSBridge.log(response.err_msg);
					}
				);
			}
		}
	}
</script>

<style lang="scss" scoped>
	page {
		background-color: #F7F7F7;
		margin-top: -152rpx;
	}
	.box {
		
		.top {
			height: 452rpx;
			width: 750rpx;
			background-image: url('https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/me/payBg.png');
			background-repeat: no-repeat;
			background-size: cover;
			
			.title-box {
				width: 670rpx;
				margin-top: 135rpx;
				.title {
					height: 44rpx;
					font-size: 32rpx;
					font-family: PingFangSC;
					font-weight: 500;
					color: #666666;
					line-height: 44rpx;
				}
				.num-box {
					margin-top: 10rpx;
					height: 96rpx;
					line-height: 96rpx;
					
					.num {
						font-size: 96rpx;
						font-family: BebasNeue;
						color: #333333;
						line-height: 96rpx;
					}
					.mingxi {
						margin-top: 36rpx;
						margin-left: 16rpx;
						width: 64rpx;
						height: 48rpx;
						font-size: 32rpx;
						font-family: PingFangSC;
						font-weight: 400;
						color: #999999;
						line-height: 48rpx;
					}
					
					.img {
						margin-top: 18rpx;
						margin-left: 8rpx;
					}
				}
				
				
			}
		}
		
		.pay-box {
			padding: 40rpx 32rpx 32rpx 32rpx;
			margin-top: -40rpx;
			width: 100%;
			height: auto;
			background: #FFFFFF;
			border-radius: 32rpx 32rpx 0rpx 0rpx;
			
			.title {
				height: 48rpx;
				font-size: 32rpx;
				font-family: PingFangSC;
				font-weight: 500;
				color: #333333;
				line-height: 48rpx;
			}
			
			.tips {
				font-size: 20rpx;
				color: #ababab;
			}
			
			.money {
				.money-item-box {
					margin-top: 6rpx;
					justify-content: space-between;
					width: 100%;
					height: auto;
					
					.money-item-active {
						
						margin-top: 18rpx;
						display: flex;
						flex-direction: column;
						justify-content: center;
						align-items: center;
						
						width: 334rpx;
						height: 216rpx;
						background: #FFF9F5;
						border-radius: 16rpx;
						border: 2rpx solid #FF6C0C;
						
						position: relative;
						
					}
									
					.money-item-inactive {
						margin-top: 18rpx;
						
						display: flex;
						flex-direction: column;
						justify-content: center;
						align-items: center;
						
						width: 334rpx;
						height: 216rpx;
						background: #F9F9F9;
						border-radius: 16rpx;
						
						position: relative;
						
					}
					
					.title-num {
						margin-left: 16rpx;
						height: 72rpx;
						font-size: 48rpx;
						font-family: PingFangSC;
						font-weight: 500;
						color: #333333;
						line-height: 72rpx;
					}
					
					.payMoney {
						height: 48rpx;
						font-size: 32rpx;
						font-family: PingFangSC;
						font-weight: 400;
						color: #FF6C0C;
						line-height: 48rpx;
					}
					
					.float {
						position: absolute;
						left: 0;
						top: 0;
						
						padding: 4rpx 8rpx;
						height: 40rpx;
						background: linear-gradient(87deg, #FF6C0C 0%, #FFA51C 100%);
						border-radius: 16rpx 0rpx 8rpx 0rpx;
						font-size: 20rpx;
						font-family: PingFangSC;
						font-weight: 400;
						color: #FFFFFF;
						line-height: 32rpx;
					}
					
				}
				
			}
			
			.vip {
				margin-top: 32rpx;
				height: auto;
				margin-bottom: 230rpx;;
				
				.vip-item-box {
					margin-top: 6rpx;
					justify-content: space-between;
					width: 100%;
					
					.vip-item-active {
						margin-top: 18rpx;
						display: flex;
						flex-direction: column;
						justify-content: center;
						align-items: center;
						
						width: 334rpx;
						height: 276rpx;
						background: #FFF9F5;
						border-radius: 16rpx;
						border: 2rpx solid #FF6C0C;
						
						
						.tips {
							margin-top: 40rpx;
							
							width: 286rpx;
							height: 48rpx;
							background: #FFF3EB;
							
							font-size: 20rpx;
							font-family: PingFangSC;
							font-weight: 400;
							color: #FFA266;
							line-height: 32rpx;
						}
					}
					.vip-item-inactive {
						margin-top: 18rpx;
						display: flex;
						flex-direction: column;
						justify-content: center;
						align-items: center;
						
						width: 334rpx;
						height: 276rpx;
						background: #F9F9F9;
						border-radius: 16rpx;
						
						.tips {
							margin-top: 40rpx;
							
							width: 286rpx;
							height: 48rpx;
							background: #F0F0F0;
							
							font-size: 20rpx;
							font-family: PingFangSC;
							font-weight: 400;
							color: #999999;
							line-height: 32rpx;
						}
					}
					
					.vip-title {
						height: 40rpx;
						font-size: 28rpx;
						font-family: PingFangSC;
						font-weight: 500;
						color: #333333;
						line-height: 40rpx;
					}
					
					.payMoney {
						margin-top: 16rpx;
						height: 60rpx;
						font-size: 40rpx;
						font-family: PingFangSC;
						font-weight: 500;
						color: #FF6C0C;
						line-height: 60rpx;
					}
					
				}
			}
		
			.btn-box {
				padding: 24rpx 0 48rpx 0;
				position: fixed;
				bottom: 0;
				// background: rgba(255, 255, 255, 0.9);
				background: #FFFFFF;
				
				.btn {
						
					width: 686rpx;
					height: 104rpx;
					background: linear-gradient(87deg, #FF6C0C 0%, #FFA51C 100%);
					border-radius: 52rpx;
					
					font-size: 32rpx;
					font-family: PingFangSC;
					font-weight: 500;
					color: #FFFFFF;
					line-height: 48rpx;
				}
			}
			
		}
	}

	
</style>