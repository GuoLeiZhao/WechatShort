<template>
	<view class="box flex flex-direction align-center">
		
		<view class="pay-num flex flex-direction align-center justify-center">
			<span class="title">支付金额</span>
			<span class="num flex flex-direction align-center justify-center">{{parsePrice(price)}}</span>
		</view>
		<view class="pay-way-list flex align-center justify-center">
			<u-radio-group style="width: 100%;" size="44" v-model="payAway">
				<view class="pay-way-item flex align-center justify-between" v-for="(item,index) in payList"
					:key="index">
					<view class="pay-box-item-l flex align-center">
						<u-image class="img" width="48rpx" height="48rpx" :src="item.imgurl" mode=""/>
						{{item.name}}
					</view>
					<view class="pay-box-item-r">
						<u-radio active-color="5074FF" :name="item.payAway"></u-radio>
					</view>
				</view>
			</u-radio-group>
		</view>
		<view class="submit flex align-center justify-center" @click="getOrderInfo()">
			确认支付
		</view>
	</view>
</template>

<script>
	//导入地址
	import config from '../../common/config.js'
	export default {
		data() {
			return {
				price: 0, //单价
				payList: [],
				orderId: '',
				payAway: 1,
				vip: {},
				platform: 'android',
			};
		},
		onLoad(option) {
			// #ifdef APP
			this.payList = [{
					imgurl: 'https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/pay/weixin.jpg',
					name: '微信支付',
					payAway: 1
				},
				{
					imgurl: 'https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/pay/zhifubao.jpg',
					name: '支付宝支付',
					payAway: 2
				}
			]
			this.payAway = 1
			// #endif
			// #ifdef MP-WEIXIN
			this.payList = [{
					imgurl: 'https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/pay/weixin.jpg',
					name: '微信支付',
					payAway: 1
				}
			]
			this.payAway = 1
			
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
			// #ifdef H5
			let ua = navigator.userAgent.toLowerCase();
			if (ua.indexOf('micromessenger') !== 1) {
				this.payList = [
					{
						imgurl: 'https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/pay/weixin.jpg',
						name: '微信支付',
						payAway: 1
					}
				]
				this.payAway = 1
			} else {
				this.payList = [{
						imgurl: 'https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/pay/weixin.jpg',
						name: '微信支付',
						payAway: 1
					}
				]
				this.payAway = 1
			}
			// #endif
			console.log(option.info)
			if (option.info) {
				let info = JSON.parse(option.info)
				this.price = info.price
				this.orderId = info.orderId
				this.vip = info.vip
			}
		},
		methods: {
			/**
			 * @param {Number} price
			 * 价格保留两位小数
			 */
			parsePrice(price) {
				return price.toFixed(2)
			},
			//生成订单
			getOrderInfo() {
				if(this.orderId){
					console.log('payOrder')
					this.payOrder(this.orderId)
				} else if (this.vip) {
					console.log('payVip')
					this.payVip()
				} else {
					console.log('pay')
					this.pay()
				}
				
			},
			completeTask(){
				// 充值完成 814 任务
				let userId = uni.getStorageSync('userId');
				this.$Request.postT("/app/integral/completeTask/814", {userId});
			},
			/**
			 * @param {Object} orderId 订单id
			 */
			payOrder(orderId) {
				let that = this
				switch (this.payAway) { //1:微信支付,2:支付宝支付 3:零钱支付
					case 1: //微信支付
						// #ifdef MP-WEIXIN
						that.$Request.postT("/app/wxPay/wxPayJsApiOrder", {
							orderId: orderId,
						}).then(red => {
							if (red.code == 0) {
								uni.requestPayment({
									provider: 'wxpay',
									timeStamp: red.data.timestamp,
									nonceStr: red.data.noncestr,
									package: red.data.package,
									signType: red.data.signType,
									paySign: red.data.sign,
									success: function(redd) {
										uni.showLoading({
											title: '支付成功'
										});
										that.completeTask();
										uni.hideLoading();
										setTimeout(function() {
											let data = {
												flag: true
											}
											uni.$emit('back', data)
											uni.navigateBack();
										}, 1000)
									},
									fail: function(err) {
										uni.hideLoading();
										that.$queue.showToast(
											'支付失败');
									}
								});
							} else {
								uni.showToast({
									title: red.msg,
									icon: 'none'
								})
							}
						});
						// #endif

						// #ifdef H5
						let ua = navigator.userAgent.toLowerCase();
						console.log(ua)
						if (ua.indexOf('micromessenger') !== -1) {
							that.$Request.postT("/app/wxPay/wxPayMpOrder", {
								orderId: orderId,
							}).then(red => {
								if (red.code == 0) {
									that.callPay(red.data);
								} else {
									that.isPay = true
									uni.showToast({
										title: red.msg,
										icon: 'none'
									})
								}
							});
						}

						// #endif

						// #ifdef APP-PLUS
						that.$Request.postT("/app/wxPay/payAppOrder", {
							orderId: orderId,
						}).then(red => {
							if (red.code == 0) {
								console.log(red, '+++++++++++++++++++++')
								that.setPayment('wxpay', JSON.stringify(red.data));
							} else {
								that.isPay = true
								uni.showToast({
									title: red.msg,
									icon: 'none'
								})
							}
						});
						// #endif
						break;
					case 2: //支付宝
						// #ifdef H5
						that.$Request.postT("/app/aliPay/payOrder", {
							orderId: orderId,
							classify: 2
						}).then(red => {
							if (red.code == 0) {
								const div = document.createElement('div')
								div.innerHTML = red.data //此处form就是后台返回接收到的数据
								document.body.appendChild(div)
								document.forms[0].submit()
								that.completeTask();
							} else {
								uni.showToast({
									title: red.msg,
									icon: 'none'
								})
							}
						});
						// #endif
						// #ifdef APP-PLUS
						that.$Request.postT("/app/aliPay/payOrder", {
							orderId: orderId,
							classify: 1
						}).then(red => {
							if (red.code == 0) {
								that.setPayment('alipay', red.data);
							} else {
								uni.showToast({
									title: red.msg,
									icon: 'none'
								})
							}
						});
						// #endif
						break;
					default: //零钱
						that.$Request.postT("/app/order/payOrders", {
							orderId: orderId,
							platform: PLATFORM,
						}).then(res => {
							if (res.code == 0) {
								uni.showToast({
									title: '支付成功'
								})
								setTimeout(function() {
									let data = {
										flag: true
									}
									uni.$emit('back', data)
									uni.navigateBack();

								}, 1000)
							} else {
								uni.showToast({
									title: res.msg,
									icon: 'none'
								})
							}
						});
						break;
				}
			},
			pay() {
				if (this.price == 0 || this.price == '') {
					return
				}
				if (this.payAway == 1) {
					// #ifdef APP-PLUS
					// 微信APP支付 根据订单id获取支付信息
					this.$Request.postT("/app/wxPay/payMoney", {
						classify: 1,
						money: this.price,
					}).then(ret => {
						this.isCheckPay(ret.code, 'wxpay', JSON.stringify(ret.data));
					});
					// #endif
			
					// #ifdef MP-WEIXIN
					let platform = this.platform
					let userId = uni.getStorageSync('userId');
					if (platform === 'android') {
						let sessionKey = uni.getStorageSync('sessionkey')
						let signData = {
							offerId: config.offerId,
							buyQuantity: parseInt(this.price * 100),
							env: 0,
							currencyType: 'CNY',
							attach: '',
						};
						this.$Request.postJson('/app/wechatOfficeAccount/getSign/' + config.TENANT, {
							sessionKey,
							signData,
							classify: 3,
							userId,
							money: parseFloat(this.price),
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
									that.$Request.sysLog('在【购买确认页】页面充值成功，金额：' + that.waitPay)
								},
								fail({
									errMsg,
									errCode
								}) {
									if (errCode === -2) {
										that.showPay = false;
										that.$Request.sysLog('在【购买确认页】页面用户取消充值，金额：' + that.waitPay)
									} else {
										that.$Request.sysLog('在【购买确认页】页面充值失败，金额：' + that.waitPay + " 原因: " + errMsg)
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
							money: this.money,
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
									that.completeTask();
									uni.showToast({
										title: '支付成功',
										icon: 'success'
									})
									setTimeout(d => {
										uni.navigateBack(1)
									}, 1000);
								},
								fail: function(err) {
									console.log('fail:' + JSON.stringify(err));
									uni.showToast({
										title: '支付失败',
										icon: 'none'
									})
								}
							});
						});
					}
					// #endif
			
					// #ifdef H5
					let ua = navigator.userAgent.toLowerCase();
					if (ua.indexOf('micromessenger') !== -1) {
						let data = {
							classify: 2,
							money: this.price,
						}
						this.$Request.postT('/app/wxPay/payMoney', data).then(res => {
							if (res.code == 0) {
								this.callPay(res.data);
							} else {
								uni.showToast({
									icon: 'none',
									title: '支付失败!'
								});
							}
						});
					}
					// #endif
			
				} else {
					// APP支付宝支付
					// #ifdef APP
					this.$Request.postT("/app/aliPay/payMoney", {
						classify: 1,
						money: this.money
					}).then(ret => {
						console.log(ret)
						this.isCheckPay(ret.code, 'alipay', ret.data);
					});
					// #endif
					// #ifdef H5
					let data = {
						classify: 2,
						money: this.money
					}
					this.$Request.postT('/app/aliPay/payMoney', data).then(
						res => {
							if (res.code === 0) {
								const div = document.createElement('div')
								div.innerHTML = res.data //此处form就是后台返回接收到的数据
								document.body.appendChild(div)
								document.forms[0].submit()
								that.completeTask();
							} else {
								uni.showToast({
									icon: 'none',
									title: '支付失败!'
								});
							}
						});
					// #endif
				}
			},
			payVip() {
				let that = this
				let data = {
					vipDetailsId: that.vip.id
				}
				let userId = uni.getStorageSync('userId')
				that.$u.api.VipOrders(data).then(res => {
					if (that.payAway == 1) {
						// #ifdef MP-WEIXIN
						that.$u.post('app/wxPay/wxPayJsApiOrder?orderId=' + res.data.ordersId).then(ret => {
							uni.requestPayment({
								provider: 'wxpay',
								timeStamp: ret.data.timestamp,
								nonceStr: ret.data.noncestr,
								package: ret.data.package,
								signType: ret.data.signType,
								paySign: ret.data.sign,
								success: function(suc) {
									console.log('success:' + JSON.stringify(suc));
									that.showPay = false
									that.completeTask();
									uni.showToast({
										title: '支付成功',
										icon: 'success'
									})
			
									let data = {
										userId: uni.getStorageSync('userId')
									}
									that.$u.api.userVip(data).then(res => {
										if (res.code == 0 && res.data && res.data
											.isVip ==
											2) {
											uni.setStorageSync('isVIP', true)
										} else {
											uni.setStorageSync('isVIP', false)
										}
									})
									setTimeout(d => {
										let data = {
											flag: true
										}
										uni.$emit('back', data)
										uni.navigateBack(1)
									}, 1000);
								},
								fail: function(err) {
									console.log('fail:' + JSON.stringify(err));
									uni.showToast({
										title: '支付失败',
										icon: 'none'
									})
								}
							});
						});
						// #endif
						// #ifdef H5
						let ua = navigator.userAgent.toLowerCase();
						console.log('VipOrders H5')
						if (ua.indexOf('micromessenger') !== -1) {
							this.$u.post('/app/wxPay/wxPayMpOrder?orderId=' + res.data.ordersId).then(red => {
								if (red.code === 0) {
									this.callPay(red.data);
								} else {
									uni.showToast({
										icon: 'none',
										title: '支付失败!'
									});
								}
							});
						}
						// #endif
						// #ifdef APP
						this.$u.post("/app/wxPay/payAppOrder?orderId=" + res.data.ordersId).then(red => {
							if (red.code == 0) {
								that.setPayment('wxpay', JSON.stringify(red
									.data));
							} else {
								uni.showToast({
									title: red.msg,
									icon: 'none'
								})
							}
						});
						// #endif
					} else if (that.payAway == 2) {
						// #ifdef H5
						this.$u.post('/app/aliPay/payOrder?orderId=' + res.data.ordersId + '&classify=2').then(
							red => {
								if (red.code === 0) {
									const div = document.createElement('div')
									div.innerHTML = red.data //此处form就是后台返回接收到的数据
									document.body.appendChild(div)
									document.forms[0].submit()
									that.completeTask();
								} else {
									uni.showToast({
										icon: 'none',
										title: '支付失败!'
									});
								}
							});
						// #endif
						// #ifdef APP
						this.$u.post('/app/aliPay/payOrder?orderId=' + res.data.ordersId + '&classify=1').then(
							red => {
								if (red.code === 0) {
									console.log('1111111')
									that.setPayment('alipay', red.data);
								} else {
									uni.showToast({
										icon: 'none',
										title: '支付失败!'
									});
								}
							});
						// #endif
					}
			
			
				})
			},
			isCheckPay(status, name, order) {
				if (status == 0) {
					this.setPayment(name, order);
				} else {
					uni.hideLoading();
					uni.showToast({
						title: '支付信息有误',
						icon: 'none'
					});
				}
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
							that.completeTask();
							that.$Request.sysLog('在【购买确认页】页面充值成功')
							setTimeout(function() {
								let data = {
									flag: true
								}
								uni.$emit('back', data)
								uni.navigateBack();
							}, 1000)
						} else if (res.err_msg === 'get_brand_wcpay_request:cancel'){
							that.$Request.sysLog('在【购买确认页】页面用户取消充值')
						} else {
							that.$Request.sysLog('在【购买确认页】页面充值失败')
							uni.hideLoading();
						}
						WeixinJSBridge.log(response.err_msg);
					}
				);
			},
			setPayment(name, order) {
				let that = this;
				uni.requestPayment({
					provider: name,
					orderInfo: order, //微信、支付宝订单数据
					success: function(res) {
						uni.showLoading({
							title: '支付成功'
						});
						uni.hideLoading();
						that.completeTask();
						setTimeout(function() {
							let data = {
								flag: true
							}
							uni.$emit('back', data)
							uni.navigateBack();
						}, 1000)
					},
					fail: function(err) {
						uni.hideLoading();
						console.log(err, 12)
					}
				});
			},
		}
	}
</script>

<style lang="scss" scoped>
	.box {
		background-color: rgba(245, 247, 250, 1);
		padding: 160rpx 32rpx 60rpx 32rpx;
		position: relative;
		
		height: 100vh;
		
		.pay-num {
			.title {
				width: 128rpx;
				height: 48rpx;
				font-size: 32rpx;
				font-family: PingFangSC;
				font-weight: 400;
				color: #333333;
				line-height: 48rpx;
			}
			
			.num {
				margin-top: 16rpx;
				
				width: 182rpx;
				height: 72rpx;
				font-size: 84rpx;
				font-family: BebasNeue;
				color: #333333;
				line-height: 72rpx;
			}
		}
		
		.pay-way-list {
			margin-top: 88rpx;
			border-radius: 16rpx;
			background-color: #FFFFFF;
			
			.pay-way-item {
				padding: 40rpx 32rpx;
				height: 112rpx;
				width: 686rpx;
				box-shadow: inset 0rpx -1rpx 0rpx 0rpx #EFEFEF;
				
				.pay-box-item-l .img{
					margin-right: 30rpx;
				}
				
				.pay-box-item-r {
					height: 48rpx;
					font-size: 32rpx;
					font-family: PingFangSC;
					font-weight: 500;
					color: #333333;
					line-height: 48rpx;
				}
			}
		}
		
		.submit {
			position: absolute;
			bottom: 60rpx;
			
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
	
</style>