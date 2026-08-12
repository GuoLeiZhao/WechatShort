<script>
	import {
		wrapApp
	} from "./common/autoTracker";
	import tracker from "./common/tracker";
	import config from './common/config.js'
	import {
		SDK
	} from './lib/dn-api-miniprogram/index.js';

	export default wrapApp({
		globalData: {
			DNSDK: null,
		},
		onLaunch: function(option) {
			try {
				console.log('app.vue', 'onLaunch', option)
				/// log
				const systemSetting = wx.getSystemSetting()
				const res = wx.getSystemInfoSync()
				const deviceInfo = wx.getDeviceInfo()
				const appBaseInfo = wx.getAppBaseInfo()
				const data = {
					systemSetting: systemSetting,
					res: res,
					deviceInfo: deviceInfo,
					appBaseInfo: appBaseInfo,
					option: option
				}
				uni.setStorage({
					key: 'bluetoothEnabled',
					data: systemSetting.bluetoothEnabled ? '1' : '0',
				});
				uni.setStorage({
					key: 'wifiEnabled',
					data: systemSetting.wifiEnabled ? '1' : '0',
				});
				uni.setStorage({
					key: 'platform',
					data: res.platform,
				});
				uni.setStorage({
					key: 'gdt_vid',
					data: option.query?.gdt_vid ? option.query?.gdt_vid : '',
				});
				const dataStr = JSON.stringify(data)
				this.appLog('/app_log', {
					data: dataStr
				});

				// this.$trackEvent(`moon_app_launch_log`, {
				//   extra: JSON.stringify(option)
				// });
			} catch (e) {

			}

			//#ifdef MP-WEIXIN
			let that = this;
			that.login()
			//#endif

			tracker.handleTrack("AppOnLaunch")

		},
		onShow: function() {
			console.log('App Show')
			// #ifdef MP-WEIXIN
			// 获取系统信息
			wx.getSystemInfo({
				success: function(res) {
					console.log(res);
					// res.platform 可能的值有 "android", "ios", "devtools", "windows"
					const platform = res.platform.toLowerCase();
					uni.setStorageSync('platform', platform)
				}
			});
			this.checkWechatSessionKey()
			// #endif

			tracker.handleTrack("AppOnShow")

		},
		onHide: function() {
			console.log('App Hide')
			tracker.flushEvents();
			tracker.handleTrack("AppOnHide")
		},
		methods: {
			appLog(url, data) {
				uni.request({
					url: config["APIHOST1"] + url,
					data: data,
					method: "POST",
					success: function(result) {},
					fail: function(e) {}
				})
			},
			wxLogin(url, data) {
				return new Promise((resolve, reject) => {
					uni.request({
						url: config["APIHOST1"] + url,
						data: data,
						method: "GET",
						success: function(result) {
							resolve(result.data)
						},
						fail: function(e) {
							reject(e)
						}
					})
				})
			},
			insertWxUser(url, data) {
				return new Promise((resolve, reject) => {
					uni.request({
						url: config["APIHOST1"] + url,
						data: data,
						method: "POST",
						success: function(result) {
							resolve(result.data)
						},
						fail: function(e) {
							reject(e)
						}
					})
				})
			},
			// #ifdef MP-WEIXIN
			login() {
				let that = this;
				// 1.wx获取登录用户code
				uni.login({
					provider: 'weixin',
					success: function(loginRes) {
						let data = {
							code: loginRes.code
						}
						that.wxLogin('/app/Login/wxLogin', data).then(res => {
							if (res.code == 0 && res.data) {
								uni.setStorageSync('openId', res.data.open_id)
								uni.setStorageSync('unionId', res.data.unionId)
								uni.setStorageSync('sessionkey', res.data.session_key)
								that.initQqAd({
									openId: res.data.open_id,
								})
								let sendData = {
									openId: uni.getStorageSync('openId'),
									unionId: uni.getStorageSync('unionId'),
								};
								that.getWeixinInfo(sendData);
							}
						})
					}
				});
			},
			//获取个人信息
			getWeixinInfo(sendData) {
				let that = this;
				let postData = {
					openId: sendData.openId, //小程序openId
					unionId: sendData.unionId, //unionId
				};
				that.insertWxUser('/app/Login/insertWxUser', postData).then(res => {
					if (res.code == 0) {
						uni.setStorageSync('token', res.token)
						uni.setStorageSync('userId', res.user.userId)
						uni.setStorageSync('openId', res.user.openId)
						that.globalData.DNSDK.setUserUniqueId(res.user.userId + '')
					  that.globalData.DNSDK.track('REGISTER')

						return
						let data = {
							userId: uni.getStorageSync('userId')
						}
						that.$u.api.userVip(data).then(res => {
							if (res.code == 0 && res.data && res.data.isVip == 2) {
								uni.setStorageSync('isVIP', true)
							} else {
								uni.setStorageSync('isVIP', false)
							}
						})
						uni.navigateBack();
					}
				})
			},
			checkWechatSessionKey() {
				let that = this;
				console.log('checkWechatSessionKey...')
				let userId = uni.getStorageSync('userId')

				if (!userId) {
					this.login()
				}

				// 判断是否过期
				uni.checkSession({
					success(res) {
						console.log('checkWechatSessionKey.success res ', res)
					},
					fail(res) {
						console.log('checkWechatSessionKey.fail res ', res)
						// session_key 已经失效，需要重新执行登录流程
						that.login() // 重新登录，更新session_key
					}
				})
			},
			// #endif
			initQqAd({
				openId,
			}) {
				SDK.setDebug(false);
				const sdk = new SDK({
					// 数据源ID，必填
					user_action_set_id: '__GDT_ACTION_SET_ID__',
					// 账户ID，用于上报接口 body.account_id
					account_id: '__GDT_ACCOUNT_ID__',
					// 访问令牌，用于上报接口 header.access-token
					access_token: '__GDT_ACCESS_TOKEN__',
					// 微信小程序APPID（可选）
					appid: 'wx72e7f15848221f6a',
					// 微信 openid，openid 和 unionid 只能填一个（优先填写openid）, 可以调用 setOpenId 设置
					openid: openId,
					// 是否开启自动采集，选填，默认为true
					auto_track: true,
				});
				this.globalData.DNSDK = sdk
			}
		}
	})
</script>

<style lang="scss">
	/* 注意要写在第一行，同时给style标签加入lang="scss"属性 */
	@import "uview-ui/index.scss";
	@import 'components/colorui/main.css';
	@import 'components/colorui/icon.css';

	/* 引入字体 */
	@font-face {
		font-family: 'BebasNeue';
		src: url('https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/font/BebasNeue-Regular.ttf');
	}

	/* 引入字体 */
	@font-face {
		font-family: 'WenYiHei';
		src: url('https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/font/WenYiHei.ttf');
	}
</style>