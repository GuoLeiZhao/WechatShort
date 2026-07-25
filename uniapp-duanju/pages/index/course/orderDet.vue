<template>
	<view>
		<view class="bg-white padding-sm margin flex" style="border-radius: 24rpx;">
			<u-image width="200rpx" height="200rpx" border-radius="10rpx" :src="courseList.titleImg">
			</u-image>
			<view class="flex flex-direction justify-between margin-left-sm" style="width:420rpx;">
				<view class="text-bold text-black margin-top-xs u-line-1 text-lg">{{courseList.title}}
				</view>
				<!-- <view class="text-gray text-26 margin-top">最近在学{{courseList.payNum}}</view> -->
				<view class="flex justify-between ">
					<view class=" text-bold " style="color: #FF8211;">¥<text
							style="font-size: 42rpx;">{{courseList.price}}</text></view>
				</view>
			</view>
		</view>

		<view class="popup_pay">
			<view class="text-lg text-bold">支付方式</view>
			<view class="flex align-center justify-between" style="height: 100upx;" v-for="(item,index) in openLists"
				:key='index'>
				<image :src="item.image" style="width: 55upx;height: 55upx;border-radius: 50upx;">
				</image>
				<view style="font-size: 30upx;margin-left: 20upx;width: 70%;">
					{{item.text}}
				</view>
				<radio-group name="openWay" style="margin-left: 45upx;" @tap.stop='selectWay(item)'>
					<label class="tui-radio">
						<radio color="red" :checked="openWay === item.id ? true : false" />
					</label>
				</radio-group>
			</view>
			<!-- <view class="pay_btn" @click="pay()">确认支付</view> -->
		</view>

		<view class="taber">
			<view class="flex align-center" style="color: #FF8211;">
				<text style="color: #333;">实付款：</text>
				<view class=" text-bold " style="color: #FF8211;">¥<text
						style="font-size: 42rpx;">{{courseList.price}}</text></view>
			</view>
			<view class="btn" @click="pay">确定并支付</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				courseId: '',
				courseList: [],

				openLists: [],
				openWay: 1,
				flag: true
			}
		},
		onLoad(option) {
			// #ifdef APP
			this.openLists = [{
					image: 'https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/index/weixin.png',
					text: '微信支付',
					id: 1
				}, {
					image: 'https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/index/zhifubao.png',
					text: '支付宝',
					id: 2
				}],
				this.openWay = 1;
			// #endif 

			// #ifdef MP-WEIXIN
			this.openLists = [{
					image: 'https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/index/weixin.png',
					text: '微信支付',
					id: 1
				}],
				this.openWay = 1;
			// #endif

			// #ifdef H5
			let ua = navigator.userAgent.toLowerCase();
			if (ua.indexOf('micromessenger') !== -1) {
				this.openLists = [{
						image: 'https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/index/weixin.png',
						text: '微信支付',
						id: 1
					}, {
						image: 'https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/index/zhifubao.png',
						text: '支付宝',
						id: 2
					}],
					this.openWay = 1;
			} else {
				this.openLists = [{
						image: 'https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/index/zhifubao.png',
						text: '支付宝',
						id: 2
					}],
					this.openWay = 2;
			}
			// this.openLists = [{
			// 		image: 'https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/index/zhifubao.png',
			// 		text: '支付宝',
			// 		id: 2
			// 	}],
			// 	this.openWay = 2;
			// #endif
			if (option.courseId) {
				this.courseId = option.courseId
				this.getDataList(this.courseId)
			}
		},
		methods: {
			selectWay(e) {
				this.openWay = e.id;
			},
			// 资源详情
			getDataList(id) {
				let data = {
					id
				}
				this.$u.api.courseDet(data).then(res => {
					if (res.code == 0) {
						this.courseList = res.data
					} else {
						uni.showToast({
							title: res.msg,
							duration: 1500,
							icon: 'none'
						});
					}

				})
			},
			pay() {
				let that = this
				if (that.flag) {
					that.flag = false
					let data = {
						courseId: that.courseList.courseId
					}
					that.$u.api.courseOrder(data).then(res => {
						if (res.code == 0 && res.data.flag == 1) {
							uni.showToast({
								title: '已获取资源',
								icon: 'success'
							})
							that.getDataList(that.courseId);
						} else if (res.code == 0 && res.data.flag == 2) {
							uni.showLoading({
								title: '支付中...'
							})
							if (that.openWay == 1) {
								// #ifdef MP-WEIXIN
								that.$u.post('app/wxPay/wxPayJsApiOrder?orderId=' + res.data.orders.ordersId, {})
									.then(
										ret => {
											uni.requestPayment({
												provider: 'wxpay',
												timeStamp: ret.data.timestamp,
												nonceStr: ret.data.noncestr,
												package: ret.data.package,
												signType: ret.data.signType,
												paySign: ret.data.sign,
												success: function(suc) {
													console.log('success:' + JSON.stringify(suc));
													uni.hideLoading();
													uni.showToast({
														title: '支付成功',
														icon: 'none'
													})
													setTimeout(function() {
														uni.navigateTo({
															url: '/me/order/index'
														})
													}, 1000)
												},
												fail: function(err) {
													uni.hideLoading();
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
								if (ua.indexOf('micromessenger') !== -1) {
									that.$u.post('/app/wxPay/wxPayMpOrder?orderId=' + res.data.orders.ordersId)
										.then(
											res => {
												if (res.code === 0) {
													that.callPay(res.data);
												} else {
													uni.hideLoading();
													uni.showToast({
														icon: 'none',
														title: '支付失败!'
													});
												}
											});
								}
								// #endif
								// #ifdef APP
								console.log(res.data.orders.ordersId,
									'res.data.orders.ordersIdres.data.orders.ordersId')
								// let data = {
								// 	orderId: res.data.orders.ordersId,
								// }
								that.$u.post('/app/wxPay/payAppOrder?orderId=' + res.data.orders.ordersId).then(
									rea => {
										console.log(rea)
										if (rea.code == 0) {
											that.isCheckPay(rea.code, 'wxpay', JSON.stringify(rea.data));
										}
									});
								// #endif
							} else if (that.openWay == 2) {
								// #ifdef H5
								that.$u.post('/app/aliPay/payOrder?orderId=' + res.data.orders.ordersId +
									'&classify=2').then(
									res => {
										if (res.code === 0) {
											uni.hideLoading();
											const div = document.createElement('div')
											div.innerHTML = res.data //此处form就是后台返回接收到的数据
											document.body.appendChild(div)
											document.forms[0].submit()
										} else {
											uni.hideLoading();
											uni.showToast({
												icon: 'none',
												title: '支付失败!'
											});
										}
									});
								// #endif
								// #ifdef APP
								console.log('---------', res.data.orders.ordersId)
								// let data = {
								// 	orderId: res.data.orders.ordersId,
								// 	classify:1
								// }
								that.$u.post('/app/aliPay/payOrder?orderId=' + res.data.orders.ordersId +
									'&classify=1').then(
									rea => {
										console.log('---------', rea)
										that.setPayment('alipay', rea.data);
									});
								// #endif
							}


						} else {
							uni.showToast({
								title: res.msg,
								icon: 'none',
								// duration: 1500
							})
							// that.getDataList(that.courseId);
						}
					})
					setTimeout(() => {
						that.flag = true
					}, 1500)

				}

			},
			isCheckPay(code, name, order) {
				if (code == 0) {
					console.log('999999999999')
					this.setPayment(name, order);
				} else {
					uni.hideLoading();
					uni.showToast({
						title: '支付信息有误'
					});
				}
			},
			setPayment(name, order) {
				console.log(777777777, name, order)
				uni.requestPayment({
					provider: name,
					orderInfo: order, //微信、支付宝订单数据
					success: function(res) {
						uni.hideLoading();
						uni.showToast({
							title: '支付成功',
							icon: 'none'
						})
						setTimeout(function() {
							uni.navigateTo({
								url: '/me/order/index'
							})
						}, 1000)
					},
					fail: function(err) {
						uni.hideLoading();
					},
					complete() {
						uni.hideLoading();
					}
				});
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
				console.log("response)))):" + JSON.stringify(response))
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
						if (res.err_msg === "get_brand_wcpay_request:ok") {
							// 使用以上方式判断前端返回,微信团队郑重提示：
							//res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。

							uni.hideLoading();
							uni.showToast({
								title: '支付成功',
								icon: 'none'
							})
							uni.navigateTo({
								url: '/me/order/index'
							})
						} else {
							uni.hideLoading();
						}
						WeixinJSBridge.log(response.err_msg);
					}
				);
			},
		}

	}
</script>

<style>
	page {
		background: #F5F5F5;
	}

	.popup_pay {
		background: #FFFFFF;
		border-radius: 24rpx;
		margin: 30rpx 30rpx;
		padding: 30rpx 30rpx 20rpx 30rpx;
	}

	.taber {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		z-index: 99;
		background: #FFFFFF;
		padding: 10rpx 30rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
	}

	.btn {
		width: 260rpx;
		height: 78rpx;
		background: #5074FF;
		border-radius: 39rpx;
		color: #FFFFFF;
		text-align: center;
		line-height: 78rpx;
		font-size: 30rpx;
	}
</style>