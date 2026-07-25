<template>
	<view class="cash">
		<view style="background-color: #5074FF;height: 400upx;border-bottom-right-radius: 40upx;border-bottom-left-radius: 40upx;">
			<view style="font-size: 32upx;color: #FFFFFF;padding-top: 100upx;">可提现总额</view>
			<view style="font-size: 40upx;color: #FFFFFF;padding-top: 20upx;">¥ {{mayMoney}}</view>

			<view style="width: 90%;height: max-content;margin-left: 40upx;background-color: #FFFFFF;box-shadow: rgba(183, 183, 183, 0.3) 0px 1px 10px;margin-top: 50upx;border-radius: 20upx;">
				<view style="display: flex;flex-direction: row;padding: 20upx;">
					<view style="font-size: 32upx;color: #333333;">提现金额 <text style="font-size: 28upx;color: #5074FF;" v-if="shouxufei">（注：提现手续费为{{shouxufei * 100}}%）</text>
					</view>
				</view>
				<view style="display: flex;flex-direction: row;padding: 20upx;">
					<view style="font-size: 40upx;color: #333333;">¥</view>
					<input type="number" v-model="money" placeholder="请输入金额" style="font-size: 40upx;color: #333333;text-align: left;margin-left: 10upx;width: 100%;" />
				</view>
				<view style="background: #E6E6E6;width: 100%;height: 1upx;"></view>

				<view style="display: flex;flex-direction: row;flex-wrap: wrap;">
					<view style="display: flex;flex-direction: row;" v-for="(item, index) in moneyList" :key="index">
						<view>
							<view style="padding: 20upx;" @click="getOut1(item.money)">
								<view style="padding-top: 40upx;width: 180upx; height: 120upx;background-color: #FFFFFF;border:1px solid #5074FF;border-radius: 10upx;">
									{{ item.money }}
								</view>
							</view>
						</view>
					</view>
				</view>
			</view>
			<view @click="getOut" style="margin: 32upx;font-size: 18px;background: #5074FF;color: white;border-radius: 10px;height: 40px;line-height: 40px">
				提现
			</view>

			<view style="display: flex;width: 100%;justify-content: center;">
				<view style="color: grey;padding-bottom: 30px;padding-top: 20upx;width: 33%;" @click="goZhifuBao">提现账号</view>
				<view style="color: grey;padding-bottom: 30px;padding-top: 20upx;width: 33%;" @click="goqianbao">钱包明细</view>
				<view style="color: grey;padding-bottom: 30px;padding-top: 20upx;width: 33%;" @click="gojilu">提现记录</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				money: '',
				mayMoney: '0',
				shouxufei: '',
				moneyList: [{
						money: '10'
					},
					{
						money: '20'
					},
					{
						money: '50'
					},
					{
						money: '100'
					},
					{
						money: '200'
					},
					{
						money: '500'
					}
				],
				value: 0,
				min: '',
				token: '',
				userId: '',
				zhifubao: '',
				zhifubaoName: '',
			};
		},
		onLoad() {
			this.getshouxufei();
		},
		onShow() {
			this.token = uni.getStorageSync('token')
			this.userId = uni.getStorageSync('userId')
			this.zhifubao = uni.getStorageSync('zhiFuBao')
			this.zhifubaoName = uni.getStorageSync('zhiFuBaoName')
			
			
			this.getcashMoney()
			
		},
		onNavigationBarButtonTap() {
			// this.list();
		},
		methods: {
			//获取收取费
			getshouxufei() {
				this.$u.get('app/common/type/152').then(res => {
					if (res.code == 0) {
						if (res.data && res.data.value) {
							this.shouxufei = res.data.value;
						}
					}
				});
				this.$u.get('app/common/type/112').then(res => {
					if (res.code == 0) {
						if (res.data && res.data.value) {
							this.cashMoney = res.data.value;
						}
					}
				});
				
			},
			// 可提现金额
			getcashMoney() {
				let data = {
					page: this.page,
					limit: this.limit
				}
				this.$u.api.queryInviter(data).then(res => {
					if (res.code == 0) {
						this.mayMoney = res.data.inviteMoney.money
					} else {
						uni.showToast({
							title: res.msg,
							duration: 1000,
							icon: 'none'
						});
					}
				})
			},
			// 提现记录
			gojilu() {
				uni.navigateTo({
					url: '/me/invite/cashList'
				});
			},
			//钱包明细
			goqianbao() {
				uni.navigateTo({
					url: '/me/invite/moneyList'
				});
			},
			list() {
				// uni.navigateTo({
				// 	url: '/me/invite/cashList'
				// })
			},
			goZhifuBao() {
				uni.navigateTo({
					url: '/me/invite/zhifubao'
				});
			},
			getMoney() {
				let that = this;
				let token = that.token
				let userId = that.userId
				if (token) {
					//this.$queue.showLoading("加载中...");
					//可以提现金额查询预估收入查询
					let data = {
						money: that.money
					}
					this.$u.api.cashMoney(data).then(res => {
						if(res.code == 0) {
							uni.showToast({
								title: '提现申请成功，预计三个工作日到账',
								icon:'none'
							})
							that.money = ''
							setTimeout(function() {
								that.getcashMoney()
							},1500)
							
						} else {
							uni.showToast({
								title: res.msg,
								icon:'none'
							})
						}
					})
				}
			},
			//校验用户输入金额
			checkMobile(money) {
				return RegExp(/^1[34578]\d{9}$/).test(money);
			},
			getOut() {
				let that = this;
				let token = that.token
				let userId = that.userId
				console.log(token)
				let cashMoney = that.cashMoney;
				
				if (token) {
					if (that.zhifubao && that.zhifubaoName) {
						if (!/^\d+$/.test(that.money)) {
							uni.showToast({
								icon: 'none',
								title: '请输入正确金额，不能包含中文，英文，特殊字符和小数'
							});
							return;
						}
						let shouxufei = parseFloat(that.money * that.shouxufei).toFixed(2);
						if (parseFloat(that.mayMoney).toFixed(1) >= parseFloat(that.money)+shouxufei*1) {
							if (parseFloat(that.money).toFixed(1) >= parseFloat(cashMoney)) {
								if (that.shouxufei > 0) {
									
									uni.showModal({
										title: "提现申请提示",
										content: '请仔细确认收款人信息\n\n收款人姓名:' + that.zhifubaoName + '\n\n提现金额:' + that.money + '元\n\n提现手续费：' + shouxufei +
											'\n\n收款人账号：' + that.zhifubao + '',
										success: (e) => {
											if (e.confirm) {
												// that.money = money
												that.getMoney();
											}
										}
									});
								} else {
									uni.showModal({
										title: "提现申请提示",
										content: '请仔细确认收款人信息\n\n收款人姓名:' + that.zhifubaoName + '\n\n提现金额:' + that.money + '元\n\n收款人账号：' + that.zhifubao +
											'',
										success: (e) => {
											if (e.confirm) {
												// that.money = money
												that.getMoney();
											}
										}
									});
								}

							} else {
								uni.showToast({
									icon: 'none',
									title: "提现金额必须大于或等于" + cashMoney + "元才可提现"
								});
							}
						} else {
							uni.showToast({
								icon: 'none',
								title: "您的余额不足"
							});
						}
					} else {
						uni.navigateTo({
							url: "/me/invite/zhifubao"
						})
					}
				} else {
					uni.navigateTo({
						url: '/pages/login/login'
					});
				}
			},
			getOut1(money) {
				let that = this;
				let token = that.token
				let userId = that.userId
				if (token) {
					if (that.zhifubao && that.zhifubaoName) {
						if (parseFloat(this.mayMoney).toFixed(1) >= parseFloat(money)) {
							if (parseFloat(money).toFixed(1) >= 10) {
								if (this.shouxufei > 0) {
									let shouxufei = parseFloat(money * this.shouxufei).toFixed(2);
									uni.showModal({
										title: '提现申请提示',
										content: '请仔细确认收款人信息\n\n收款人姓名:' + that.zhifubaoName + '\n\n提现金额:' + money + '元\n\n提现手续费：' + shouxufei +
											'\n\n收款人账号：' + that.zhifubao + '',
										success: e => {
											if (e.confirm) {
												this.money = money
												that.getMoney();
												// uni.showToast({
												// 	icon: 'none',
												// 	title: '提现成功'
												// })
											}
										}
									});
								} else {
									uni.showModal({
										title: '提现申请提示',
										content: '请仔细确认收款人信息\n\n收款人姓名:' + that.zhifubaoName + '\n\n提现金额:' + money + '元\n\n收款人账号：' + that.zhifubao +
											'',
										success: e => {
											if (e.confirm) {
												this.money = money
												that.getMoney();
												// uni.showToast({
												// 	icon: 'none',
												// 	title: '提现成功'
												// })
											}
										}
									});
								}
							} else {
								uni.showToast({
									icon: 'none',
									title: '提现金额必须大于或等于10元才可提现'
								});
							}
						} else {
							uni.showToast({
								icon: 'none',
								title: '您的余额不足'
							});
						}
					} else {
						uni.navigateTo({
							url: '/me/invite/zhifubao'
						});
					}
				} else {
					uni.navigateTo({
						url: '/pages/login/login'
					});
				}
			}
		}
	};
</script>

<style lang="less">
	// @import 'https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/css/index.css';

	.view2-view-text {
		font-size: 14px;
		color: #000000;
		margin-left: 20upx;
		width: 80%;
	}

	.view2-view-image-right {
		width: 18upx;
		height: 30upx;
		margin-left: 50upx;
	}

	.cash {
		text-align: center;
		background: white;
		height: 100%;
		position: absolute;
		width: 100%;

		.cash-top {
			padding: 32upx 32upx 50upx 32upx;
			/* border-bottom: 1px solid gainsboro; */
			background: #5074FF;
		}

		.leiji {
			font-size: 14px;
			color: #ffffff;
			margin-bottom: 10px;
		}
	}
</style>
