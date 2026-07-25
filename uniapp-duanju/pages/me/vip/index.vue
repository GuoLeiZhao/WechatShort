<!-- 会员中心 -->
<template>
	<view>
		<view class="margin-top margin-lr radius u-relative"
			style="overflow: hidden;height: 260rpx;border-radius: 40rpx;">
			<u-image src="https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/me/beijing@2x.png" width="100%" height="400rpx"></u-image>
			<view class="u-abso margin" style="top: 0;">
				<view class="text-bold u-font-40">{{userName}}</view>
				<view class="margin-top-sm" style="color: #604320;" v-if="!isVIP">购买会员，畅享 {{courseCount}} 部短剧！！</view>
				<view class="margin-top-sm" style="color: #604320;" v-else>会员到期：{{vipData}}</view>
			</view>
		</view>
		<view class="margin-lr padding bg-white"
			style="border-radius: 40rpx;position: relative;top: -80rpx;color: #000;">
			<view class="flex justify-between" style="align-items: baseline;">
				<span class="u-font-16 text-bold margin-bottom">开通会员</span>
				<span class="tips">会员为虚拟商品，概不退换</span>
			</view>
			<view class="flex justify-between margin-bottom" v-for="(item, index) in VIPlist" :key="index">
				<view>
					<view class="u-font-40">{{item.money}}</view>
					<view class="" style="color: #7F8299;">{{item.type}}卡 • {{item.money}}元/{{item.type}}</view>
				</view>
				<view>
					<view class="btn-bg" @click="bugVIP(item.id)">购买</view>
				</view>
			</view>
		</view>

		<u-popup v-model="showPay" mode="bottom" border-radius="14" :closeable="true">
			<view
				style="width: 100%;text-align: center;font-size:38rpx;font-weight: bold;margin-top:15rpx;margin-bottom: 80rpx;">
				选择支付方式
			</view>
			<view style="display: flex;height: 100upx;align-items: center;padding: 20upx 30rpx;"
				v-for="(item,index) in openLists" :key='index'>
				<view style="display: flex;width:80%;align-items: center;">
					<image :src="item.image" style="width: 55upx;height: 55upx;"></image>
					<view style="font-size: 30upx;margin-left: 20upx;width: 70%;">{{item.text}}
					</view>
				</view>
				<view style="width: 20%;display: flex;justify-content: flex-end;">
					<radio-group name="openWay" style="margin-left: 20upx;" @tap='selectWay(item.id)'>
						<label class="tui-radio">
							<radio color="#1789FD" :checked="openWay === item.id ? true : false" />
						</label>
					</radio-group>
				</view>
			</view>
			<view
				style="width: 690rpx;height: 80rpx;background:#1789FD;color:#FFFFFF;text-align: center;line-height: 80rpx;border-radius: 50rpx;margin: 30rpx;"
				@tap="pay()">确认支付</view>
		</u-popup>
	</view>
</template>

<script>
	import config from '../../../common/config.js'
	export default {
		data() {
			return {
				userName: '匿名',
				VIPlist: '',
				showPay: false,
				openWay: 0,
				openLists: [],
				vipId: '',
				vipData: '',
				isVIP: false,
				courseCount: 0,
			}
		},
		onLoad() {
			// #ifdef APP
			this.openLists = [{
					image: '/static/images/pay/weixin.jpg',
					text: '微信',
					id: 1
				}, {
					image: '/static/images/pay/zhifubao.jpg',
					text: '支付宝',
					id: 2
				}],
				this.openWay = 1;
			// #endif 

			// #ifdef MP-WEIXIN
			this.openLists = [{
					image: '/static/images/pay/weixin.jpg',
					text: '微信',
					id: 1
				}],
				this.openWay = 1;
			// #endif

			// #ifdef H5
			let ua = navigator.userAgent.toLowerCase();
			if (ua.indexOf('micromessenger') !== -1) {
				this.openLists = [{
						image: '/static/images/pay/weixin.jpg',
						text: '微信',
						id: 1
					}],
					this.openWay = 1;
			} else {
				this.openLists = [{
						image: '/static/images/pay/zhifubao.jpg',
						text: '支付宝',
						id: 2
					}],
					this.openWay = 2;
			}

			// #endif
			this.userName = uni.getStorageSync('userName')
			this.getVIPDet()
			this.getvipdata()
		
		},
		onShow() {
			this.getVideoCount()	
		},
		methods: {
			getvipdata() {
				let data = {
					userId: uni.getStorageSync('userId')
				}
				this.$u.api.userVip(data).then(res => {
					if (res.code == 0 && res.data && res.data.isVip == 2) {
						this.isVIP = true;
						this.vipData = res.data.endTime
					} else {
						this.isVIP = false

					}
				})
			},
			getVideoCount(){
				this.$Request.getT('/app/course/selectCount').then(res => {
					this.courseCount = res.data
				})
			},
			selectWay: function(id) {
				this.openWay = id;
			},
			getVIPDet() {
				this.$u.api.vipDet().then(res => {
					if (res.code == 0) {
						res.data.forEach(ret => {
							switch (ret.vipNameType) {
								case 0:
									ret.type = '月'
									break;
								case 1:
									ret.type = '季'
									break;
								case 2:
									ret.type = '年'
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
			bugVIP(e) {
				this.vipId = e
				this.openWay=1
				this.pay()
				// this.showPay = true
			},
			pay() {
				let that = this
				let data = {
					vipDetailsId: that.vipId
				}
				let userId = uni.getStorageSync('userId')
				
				let platform = this.platform
				
				
				
				that.$u.api.VipOrders(data).then(res => {
					if (that.openWay == 1) {
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
					} else if (that.openWay == 2) {
						// #ifdef H5
						this.$u.post('/app/aliPay/payOrder?orderId=' + res.data.ordersId + '&classify=2').then(
							red => {
								if (red.code === 0) {
									const div = document.createElement('div')
									div.innerHTML = red.data //此处form就是后台返回接收到的数据
									document.body.appendChild(div)
									document.forms[0].submit()
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
				let that = this
				uni.requestPayment({
					provider: name,
					orderInfo: order, //微信、支付宝订单数据
					success: function(res) {
						uni.hideLoading();
						that.showPay = false
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
							that.showPay = false
							uni.showToast({
								title: '支付成功',
								icon: 'success'
							})
							
							that.$Request.sysLog('在【会员购买页】页面充值成功，vipId: ' + that.vipId)

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
						} else if (res.err_msg === 'get_brand_wcpay_request:cancel'){
							that.$Request.sysLog('在【会员购买页】页面用户取消充值，vipId: ' + that.vipId)
						} else {
							that.$Request.sysLog('在【会员购买页】页面充值失败，vipId: ' + that.vipId)
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
	.custom-style {
		/* background: linear-gradient(90deg, #DBC084 0%, #E9D4A6 100%); */
		/* padding: 0px 16px; */
		/* border: 0px; */
		color: #604320;
	}

	.btn-bg {
		width: 64px;
		height: 28px;
		background: linear-gradient(90deg, #DBC084 0%, #E9D4A6 100%);
		color: #604320;
		border-radius: 28px;
		text-align: center;
		line-height: 28px;
		margin-top: 4px;
	}
	
	.tips {
		font-size: 20rpx;
		color: #ababab;
	}
</style>