<template>
	<view class="box flex flex-direction align-center justify-center">
		<view class="top flex flex-direction align-center justify-center">
			<view class="person flex align-center" @click="token?goNav('/pages/me/userinfo'): ()=>{return}">
				<u-image :src="avatar ? avatar:'https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/logo.png'" shape="circle" width="120rpx" height="120rpx" />
				<view class="info flex flex-direction">
					<span class="name">{{userName ? userName:'未登录111'}}</span>
					<view v-if="invitationCode" class="id flex ">
						<u-image src="https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/me/id.jpg" width="20rpx" height="20rpx" />
						<span class="id-num">{{ invitationCode }}</span>
					</view>
				</view>
			</view>
			<view class="data flex align-center justify-center">
				<view class="item flex flex-direction align-center justify-center" @click="$Request.sysLog('浏览【我的】页面，跳转【我的喜欢】');goNav('/me/jilu/myLove')">
					<view class="num">
						{{myLoveNum}}
					</view>
					<view class="name">
						我的喜欢
					</view>
				</view>
				<view class="item flex flex-direction align-center justify-center" @click="$Request.sysLog('浏览【我的】页面，跳转【我的追剧】');goNav('/me/jilu/histor')">
					<view class="num">
						{{myZhui}}
					</view>
					<view class="name">
						我的追剧
					</view>
				</view>
				<view class="item flex flex-direction align-center justify-center" @click="$Request.sysLog('浏览【我的】页面，跳转【我的积分】');goNav('/me/jifen/jifen')">
					<view class="num">
						{{jifen}}
					</view>
					<view class="name">
						我的积分
					</view>
				</view>
				
			</view>
		</view>
		<view class="func-title flex flex-direction align-center" v-if="false">
			<span class="title" @tap="test">
				我的钱包
			</span>
			<view class="func-box flex justify-center">
				<view class="func-item money flex align-center" @click="moneyClick">
					<u-image src="https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/me/money.png" width="56rpx" height="54rpx" />
					<view class="func-inner-box flex flex-direction justify-center ">
						<view class="flex align-center ">
							<span class="inner-money">看点余额</span>
							<view class="right-triangle"/>
						</view>
						<view class="money-num">
							{{formatNumber(moneyNum)}}
						</view>
					</view>
				</view>
				<view class="func-item vip flex align-center" @click="vipClick" >
					<u-image src="https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/me/vip.png" width="56rpx" height="54rpx" />
					<view class="func-inner-box flex flex-direction justify-center ">
						<view class="flex align-center ">
							<span class="inner-money">{{isVIP?'会员用户':'开通会员'}}</span>
							<view class="right-triangle"/>
						</view>
						<view class="money-num">
							{{endTime ? formatDateTime2Date(endTime)+'到期':'升级VIP，省更多钱'}}
						</view>
					</view>
				</view>
			</view>
		</view>
		<view class="page-nav-list flex flex-direction align-center">
			<view @click="$Request.sysLog('浏览【我的】页面，跳转【消息中心】');goNav('/me/message/index')" class="list-item flex align-center">
				<span>消息中心</span>
				<view class="right-triangle"/>
			</view>
			<view @click="$Request.sysLog('浏览【我的】页面，跳转【观看历史】');goNav('/me/jilu/jilu')" class="list-item flex align-center">
				<span>观看历史</span>
				<view class="right-triangle"/>
			</view>
			<!-- <view @click="goNav('/me/invite/invite')" class="list-item flex align-center">
				<span>分享好友</span>
				<view class="right-triangle"/>
			</view>
			<view @click="goNav('/me/invite/inviteDet')" class="list-item flex align-center">
				<span>我的团队</span>
				<view class="right-triangle"/>
			</view> -->
			<view @click="$Request.sysLog('浏览【我的】页面，跳转【联系客服】');goMsg()" class="list-item flex align-center">
				<span>联系客服</span>
				<view class="right-triangle"/>
			</view>
			<view @click="$Request.sysLog('浏览【我的】页面，跳转【帮助中心】');goNav('/me/feedbackIndex/feedbackIndex')" class="list-item flex align-center">
				<span>帮助中心</span>
				<view class="right-triangle"/>
			</view>
			<view @click="$Request.sysLog('浏览【我的】页面，跳转【意见反馈】');goNav('/me/feedback/index')" class="list-item flex align-center">
				<span>意见反馈</span>
				<view class="right-triangle"/>
			</view>
			<view @click="$Request.sysLog('浏览【我的】页面，跳转【设置中心】');goNav('/me/setting/index')" class="list-item flex align-center">
				<span>设置中心</span>
				<view class="right-triangle"/>
			</view>
		</view>
		<u-modal v-model="canNotPayShow" title="对不起" content="iOS 系统不允许进行支付操作" confirm-text="我知道了"/>
	</view>
</template>

<script>
	import config from '../../common/config.js'
	export default {
		data() {
			return {
				canNotPayShow: false,
				isShoMoney: false, //是否显示余额
				avatar: 'https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/logo.png',
				img: 'https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/me/huiyuan.png',
				swiperList: [{
					imageUrl: 'https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/me/banner.png',
				}],
				isLogin: false,
				userName: '未登录',
				isVIP: false,
				invitationCode: '',
				token: '',
				endTime: '', //会员到期时间
				moneyNum: 0, //余额
				jifen: 0, //积分
				isVips: '否', //是否显示会员
				myLoveNum: 0, //我的喜欢个数
				myZhui: 0, //我的追剧数量
			}
		},
		onLoad() {
			this.getBannerList()
		},
		onShow() {
			this.$Request.sysLog('浏览【我的】页面')
			this.isVips = uni.getStorageSync('isVips') ? uni.getStorageSync('isVips') : '否'
			this.token = uni.getStorageSync('token')
			if (this.token) {
				this.getMyMoney()
				this.getJifen()
				this.$u.api.userinfo().then(res => {
					if (res.code == 0 && res.data) {
						uni.setStorageSync('zhiFuBao', res.data.zhiFuBao)
						uni.setStorageSync('zhiFuBaoName', res.data.zhiFuBaoName)
						// uni.setStorageSync('userName', res.data.userName)
						const openId = uni.getStorageSync('openId')
						uni.setStorageSync('avatar', res.data.avatar)
						this.userName = openId.slice(-6)
						this.avatar = res.data.avatar
						this.invitationCode = res.data.invitationCode
					}
				})

				let data = {
					userId: uni.getStorageSync('userId')
				}
				this.$u.api.userVip(data).then(res => {
					if (res.code == 0 && res.data && res.data.isVip == 2) {
						this.isVIP = true;
						this.endTime = res.data.endTime
						uni.setStorageSync('isVIP', true)
					} else {
						this.isVIP = false;
						this.endTime = ''
						uni.setStorageSync('isVIP', false)
					}
				})

				this.isLogin = true
				this.userName = uni.getStorageSync('userName')
				this.isVIP = uni.getStorageSync('isVIP')
				this.getMyLoveNum()
				this.getMyZhuiNum()
			} else {
				this.isLogin = false
				this.isVIP = false
				this.endTime = '' //会员到期时间
				this.moneyNum = 0 //余额
				this.jifen = 0 //积分
				this.userName = '未登录'
				this.invitationCode = ''
				this.myLoveNum = 0
				this.myZhui = 0
			}
			this.avatar = uni.getStorageSync('avatar') ? uni.getStorageSync('avatar') : 'https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/logo.png'
		},
		methods: {
			test(){
				uni.navigateTo({
					url: 'plugin-private://wx94a6522b1d640c3b/pages/playlet/playlet?dramaId=100056&srcAppid=wx68a05819ade65197'
				});
			},
			// 充值 / 会员购买入口，微信小程序端不编译（对应入口已由 v-if="false" 隐藏）
			// #ifndef MP-WEIXIN
			moneyClick() {
				let platform = uni.getStorageSync('platform')
				if(platform === 'ios') {
					this.canNotPayShow = true;
				}else {
					this.$Request.sysLog('浏览【我的】页面，跳转【看点余额页面】');
					this.goNav('/me/wallet/wallet')
				}
			},
			vipClick() {
				let platform = uni.getStorageSync('platform')
				if(platform === 'ios') {
					this.canNotPayShow = true;
				}else {
					this.$Request.sysLog('浏览【我的】页面，跳转【会员购买页】');
					this.goNav('/pages/me/vip/index')
				}

			},
			// #endif
			login() {
				// #ifdef MP-TOUTIAO
				let that = this;
				tt.login({
					force: true,
					success(res) {
						console.log(res)
						that.$Request.postJson('/app/Login/' + config.TENANT + "/dyLogin", {
							code: res.code,
							anonymousCode: res.anonymousCode
						}).then(ret => {
							if (ret.code == 0) {
								that.$queue.setData("phone", ret.user.phone);
								that.$queue.setData("userId", ret.user.userId);
								that.$queue.setData("token", ret.token);
								uni.setStorageSync('sex', ret.user.sex)
								uni.setStorageSync('openId', ret.user.openId)
								that.$queue.setData("userName", ret.user.userName);
								that.$queue.setData("avatar", ret.user.avatar);
								that.$queue.setData("invitationCode", ret.user.invitationCode);
								that.$queue.setData("inviterCode", ret.user.inviterCode);
								uni.switchTab({
									url: '/pages/index/index'
								})
							}
						})
					},
					fail(res) {
						console.log(`登陆失败，请重试`);
					},
				});
				// #endif
				
				// #ifndef MP-TOUTIAO
				this.goNav('/pages/login/login')
				// #endif
			},
			//获取我喜欢的数量
			getMyLoveNum() {
				let data = {
					page: 1,
					limit: 1,
					classify: 2
				}
				this.$Request.getT('/app/courseCollect/selectByUserId', data).then(res => {
					if (res.code == 0) {
						this.myLoveNum = res.data.total
					} else {
						this.myLoveNum = 0
					}
				})
			},
			//获取我追剧的数量
			getMyZhuiNum() {
				let data = {
					page: 1,
					limit: 1,
					classify: 1
				}
				this.$Request.getT('/app/courseCollect/selectByUserId', data).then(res => {
					if (res.code == 0) {
						this.myZhui = res.data.total
					} else {
						this.myZhui = 0
					}
				})
			},
			/**
			 * 获取积分
			 */
			getJifen() {
				this.$Request.getT('/app/integral/selectByUserId').then(res => {
					if (res.code == 0) {
						this.jifen = res.data.integralNum
					} else {
						this.jifen = 0
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
			 * @param {Date} num
			 * @param 保留两位小数
			 */
			formatDateTime2Date(date) {
				// 判断是否为整数
				if(date && date !== ''){
					return date.split(' ')[0]
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
			goPage(url) {
				if (url) {
					if (url.indexOf('/pages/') !== -1 || url.indexOf('/me/') !== -1) {
						uni.navigateTo({
							url
						});
					} else {
						//#ifndef H5
						uni.navigateTo({
							url: '/pages/index/webView?url=' + url
						});
						//#endif
						//#ifdef H5
						window.location.href = url;
						//#endif	
					}
				}
			},
			goMsg() {
				let kefu = uni.getStorageSync('kefu'); // 用户端联系方式 1 手机号 2企业微信
				let kefuPhone = uni.getStorageSync('kefuPhone');
				if (kefu == 1) {
					uni.navigateTo({
						url: '/me/setting/kefu'
					})
				} else if (kefu == 3) {
					uni.makePhoneCall({
						phoneNumber: kefuPhone //仅为示例
					});
				} else if (kefu == 2) {
					// #ifdef MP-WEIXIN
					let that = this
					try {
						wx.openCustomerServiceChat({
							extInfo: {
								url: uni.getStorageSync('kefuUrl')
							},
							corpId: uni.getStorageSync('kefuAppId'),
							success(res) {},
							fail(res) {
								console.error(res)
							}
						})
					} catch (error) {
						console.error("catchcatch" + error)
						uni.showToast({
							title: '请更新至微信最新版本'
						});
					}
					// #endif
					// #ifndef MP-WEIXIN
					let url = uni.getStorageSync('kefuUrl');
					if (url.indexOf('/pages/') !== -1 || url.indexOf('/my/') !== -1) {
						uni.navigateTo({
							url
						});
					} else {
						//#ifndef H5
						uni.navigateTo({
							url: '/pages/index/webView?url=' + url
						});
						//#endif
						//#ifdef H5
						window.location.href = url;
						//#endif
					}
					// #endif
				}
			},
			goNav(e) {
				console.log(e)
				let token = uni.getStorageSync('token')
				if (token) {
					console.log('token ok，go！', e)
					uni.navigateTo({
						url: e
					})
				} else {
					uni.navigateTo({
						url: "/pages/login/login"
					})
				}

			},
			// 获取轮播图列表
			getBannerList() {
				this.$u.api.bannerList({
					classify: '3'
				}).then(res => {
					if (res.code == 0 && res.data) { 
						let arr = []
						res.data.forEach(ret => {
							if (ret.state == 1) {
								arr.push(ret)
							}
						})
						this.swiperList = arr
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						});
					}
				})
			},
		}
	}
</script>

<style lang="scss" scoped>
	page {
		background-color: #F5F7FA;
		margin-top: -88rpx;
	}
	
	.right-triangle {
		width: 0;
		height: 0;
		border-top: 9rpx solid transparent;
		border-left: 12rpx solid;
		border-bottom: 9rpx solid transparent;
		margin-left: 8rpx;
	}
	
	.box {
		
		.top {
			height: 400rpx;
			width: 750rpx;
			background-image: url('https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/me/bg.png');
			background-repeat: no-repeat;
			background-size: cover;
			
			.person {
				width: 100%;
				padding: 60rpx 0 0 32rpx;
				
				.info {
					margin-left: 18rpx;
					
					.name {
						height: 36rpx;
						font-size: 36rpx;
						font-family: PingFangSC;
						font-weight: 500;
						color: #262626;
						line-height: 36rpx;
					}
					
					.id {
						margin-top: 16rpx;
						width: 200rpx;
						line-height: 36rpx;
						
						.id-num {
							margin-left: 8rpx;
						
							font-size: 24rpx;
							font-family: PingFangSC;
							font-weight: 400;
							color: #A8ABB0;
						}
					}
					
				}
				
			}
		
			.data {
				
				margin-top: 24rpx;
				
				.item {
					width: 250rpx;
					
					.num {
						height: 48rpx;
						font-size: 32rpx;
						font-family: PingFangSC;
						font-weight: 500;
						color: #262626;
						line-height: 48rpx;
					}
					
					.name {
						margin-top: 4rpx;
						height: 36rpx;
						font-size: 24rpx;
						font-family: PingFangSC;
						font-weight: 400;
						color: #666666;
						line-height: 36rpx;
					}
				}
			}
		}
		
		.func-title {
			margin-top: 12rpx;
			width: 702rpx;
			height: 256rpx;
			background: #FFFFFF;
			border-radius: 16rpx;
			
			.title {
				padding-top: 32rpx;
				width: 638rpx;
				font-size: 32rpx;
				font-family: PingFangSC;
				font-weight: 500;
				color: #333333;
				line-height: 48rpx;
			}
			.func-box {
				margin-top: 24rpx;
				width: 654rpx;
				
				.func-item {
					padding: 20rpx;
					width: 320rpx;
					height: 120rpx;
					
					border-radius: 16rpx 16rpx 0rpx 0rpx;
					
					.func-inner-box {
						margin-left: 12rpx;
						
						.inner-money {
							width: 112rpx;
							height: 40rpx;
							font-size: 28rpx;
							font-family: PingFangSC;
							font-weight: 500;
							line-height: 40rpx;
						}
						.money-num {
							margin-top: 4rpx;
							height: 36rpx;
							font-size: 24rpx;
							font-family: PingFangSC;
							font-weight: 400;
							
							line-height: 36rpx;
						}
					}
				}
				.func-item.money {
					background: linear-gradient(90deg, #FDEADE 0%, #FFDEC8 100%);
					.inner-money {
						color: #A65621;
					}
					.money-num {
						color: #AF917C;
					}
					.right-triangle {
						border-left-color:  #A65621;
					}
				}
				
				.func-item.vip {
					margin-left: 14rpx;
					background: linear-gradient(90deg, #FFF6D4 0%, #FFEDBF 100%);
					.inner-money {
						color: #B27905;
					}
					.money-num {
						color: #B69961;
					}
					.right-triangle {
						border-left-color:  #DFA01F;
					}
				}
			}
		}
	
		.page-nav-list {
			margin-top: 24rpx;
			margin-bottom: 18rpx;
			width: 702rpx;
			// height: 768rpx;
			background: #FFFFFF;
			border-radius: 16rpx;
			
			.list-item {
				width: 638rpx;
				height: 96rpx;
				position: relative;
				
				span {
					height: 32rpx;
					font-size: 24rpx;
					font-family: PingFangSC;
					font-weight: 500;
					color: #262626;
					line-height: 32rpx;
				}
				
				.right-triangle {
					position: absolute;
					right: 0;
					border-left-color:  #98A1B4;
				}
			}
			.list-item:not(:last-child) {
				box-shadow: inset 0rpx -1rpx 0rpx 0rpx #F0F0F0;
			}
		}
	}
	

</style>