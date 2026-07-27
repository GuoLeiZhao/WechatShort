<template>
	<view style="padding-bottom: 20rpx;">
		<!-- 顶部栏：与微信胶囊按钮同一行，左侧标题右侧留给胶囊 -->
		<view class="top-bar flex align-center">
			<span class="top-bar-title">剧场</span>
		</view>
		<view class="video-list-box">
			<view class="video-list flex flex-wrap">
				<view class="video-item"
				@click="posterSuccess(item)"
				 v-for="(item, idx) in courseList" :key="idx">
					<u-image :src="item.titleImg" width="343rpx" height="457rpx" border-radius="0"/>
					<view class="item-info flex flex-direction">
						<view class="item-title">{{item.title}}</view>
						<!-- 分类暂不显示：后台「类别」选择器被注释，剧目挂不上分类 -->
						<view class="item-sub-title">{{item.courseLabel}}</view>
					</view>
				</view>
			</view>
		</view>
		<u-loadmore v-if="courseList.length > 0" :status="status" />
		<empty v-if="courseList.length == 0" />

		<!-- 关注公众号悬浮窗 -->
 		<view class="follow flex align-center justify-center" v-if="isShowFollow">
 			<view class="follow-box flex align-center justify-between">
 				<view class="follow-box-l flex align-center">
 					<image src="https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/logo.png" mode=""></image>
 					<view class="follow-box-l-txt">
 						<view class="follow-box-l-txt-title">
 							关注公众号
 						</view>
 						<view class="follow-box-l-txt-con">
 							{{gzhText}}
 						</view>
 					</view>
 				</view>
 				<view class="follow-box-r" @click="openFollow()">
 					关注
 				</view>
 				<view class="follow-box-close" @click="isShowFollow = false">
 					<u-icon name="close-circle-fill" color="#999999" size="28"></u-icon>
 				</view>
 			</view>
 		</view>
		<!-- #ifdef MP-WEIXIN -->
		<!-- <uni-popup ref="popusAuthorization" type="center" :maskClick="false">
			<view class="contentview">
				<view class="title">隐私保护指引</view>
				<view class="des" @click="openPrivacyContract">
					在使用当前小程序服务之前，请仔细阅读<text
						style="color: #5074FF;">{{privacyContractName}}</text>。如你同意{{privacyContractName}}，请点击“同意”开始使用。
				</view>
				<view class="btns">
					<button class="item reject" @click="exitMiniProgram">拒绝</button>
					<button id="agree-btn" class="item agree" open-type="agreePrivacyAuthorization"
						@agreeprivacyauthorization="handleAgreePrivacyAuthorization">同意</button>
				</view>
			</view>
			
		</uni-popup> -->
		<!-- #endif -->
		
		<u-mask :show="reciveKanDianFlag" :mask-click-able="false" class="flex flex-direction justify-center align-center">
			<view class="kd-bg flex flex-direction align-center">
				<u-image style="margin-top: 78rpx;" width="264rpx" height="80rpx" src="https://wefly.work/img/20250324/c887d8aaf58c45abbf4e2c551743d685.webp"/>
				<span class="kd-title">{{title && '' !== title ? title : '限时福利'}}</span>
				<span class="kd-content">获得看点</span>
				<span class="kd-num">{{num && '' !== num ? num : '0'}} <span class="kd-unit">个</span></span>
				<view class="kd-btn" @click="getKanDian">
					确认查收
				</view>
				
			</view>
		</u-mask>
		<u-mask :show="getKanDianFlag" :mask-click-able="false" class="flex flex-direction justify-center align-center">
			<view class="kd-succ-bg flex flex-direction align-center">
				<span class="kd-succ-title">
					查收成功
				</span>
				<span class="kd-succ-btn" @click="getKanDianFlag=false">
					去看剧
				</span>
			</view>
		</u-mask>
	</view>
</template>

<script>
	import config from '../../common/config.js'
	import empty from '../../components/empty.vue'
	import videoList from '../../components/videoList/videoList.vue'
	import tracker from '../../common/tracker.js'
	
	export default {
		components: {
			videoList,
			empty
		},
		data() {
			return {
				title: '',
				num: 0,
				reciveKanDianFlag: false,
				getKanDianFlag: false,
				isShowFollow: false, //是否显示关注公众号
 				gzhText: '',
				status: 'loadmore',
				swiperList: [], //轮播图列表
				noticeList: [], //公告列表
				keyword: '',
				gridList: [], //获取金刚区类表
				current: 0, //tabs索引
				tabsList: [{
						name: '本周热门',
						id: 2,
					},
					{
						name: '本周排行榜	',
						id: 1,
					},
					{
						name: '最新热播',
						id: '',
					}
				], //tabs列表
				limit: 10,
				page: 1,
				pages: 1,
				courseList: [], //视频列表
				privacyContractName: '',
				bgImg: '',
				tuiguang: '',
			};
		},
		onShareAppMessage(res) {
			return {
				path: '/pages/index/index?invitation=' + this.invitationCode, //这是为了传参   onload(data){let id=data.id;} 
				title: this.tuiguang,
				imageUrl: this.bgImg
			}
		},
		onLoad(options) {
			uni.setStorageSync("path", "home");
			tracker.flushSession()
			console.log('options', options);
			// #ifdef MP-WEIXIN
			// let that = this
			// try {
			// 	wx.getPrivacySetting({
			// 		success: res => {
			// 			console.log("是否需要授权：", res.needAuthorization, "隐私协议的名称为：", res.privacyContractName)
			// 			if (res.needAuthorization) {
			// 				that.privacyContractName = res.privacyContractName;
			// 				that.$refs.popusAuthorization.open();
			// 			}
			// 		},
			// 		fail: () => {},
			// 		complete: () => {},
			// 	})
			// } catch (e) {
			// 	//TODO handle the exception
			// }
			// #endif
			this.getBgImg()
			this.invitationCode = uni.getStorageSync('invitationCode')
			// #ifdef MP-WEIXIN
			if (options.scene) { //这里为线上操作
				//此处的二维码  page/index/index?brokerId=123
				let scene = decodeURIComponent(options.scene);
				console.log('index scene: ', scene) //brokerId=123  为字符串，需要我们去分割
				uni.setStorageSync('invitation', scene)
			}

			// #endif
			if (options.invitation) {
				uni.setStorageSync('invitation', options.invitation)
			}
			
			// #ifdef H5
 			//首页关注公众号文案	253
 			this.$u.get('/app/common/type/253').then(res => { //热搜词
 				if (res.code == 0 && res.data && res.data.value) {
 					this.gzhText = res.data.value;
 					this.isShowFollow = true;
 				}
 			});
 			// #endif
			if(options.from=='ad'){
				this.$trackEvent(`moon_from_ad`, {
					link: `/me/detail/detailMPWechat?id=${options.id}&courseDetailsId=${options.courseDetailsId}`,
					extra: JSON.stringify(options)
				});
				wx.navigateTo({
					url: `/me/detail/detailMPWechat?id=${options.id}&courseDetailsId=${options.courseDetailsId}`
				})
			}
			let that = this
			uni.login({
				provider: 'weixin',
				success: function(loginRes) {
					let data = {
						code: loginRes.code
					}
					console.log('login', data)
					that.$u.api.wxLogin(data).then(res => {
						console.log('login', res)
						if (res.code == 0 && res.data) {
							uni.setStorageSync('openId', res.data.open_id)
							uni.setStorageSync('unionId', res.data.unionid)
							uni.setStorageSync('sessionkey', res.data.session_key)
							let sendData = {
								openId: uni.getStorageSync('openId'),
								unionId: uni.getStorageSync('unionId'),
							};
							console.log('login', sendData)
							that.getWeixinInfo(sendData);
						}
					})
				}
			});
		},
		onShow() {
			tracker.flushSession()
			// #ifdef MP-WEIXIN
			// 首页广告
			// 若在开发者工具中无法预览广告，请切换开发者工具中的基础库版本
			// 在页面中定义插屏广告
      let interstitialAd = null

      // 在页面onLoad回调事件中创建插屏广告实例
      if (wx.createInterstitialAd) {
        interstitialAd = wx.createInterstitialAd({
          adUnitId: '__AD_UNIT_ID__'
        })
        interstitialAd.onLoad(() => {})
        interstitialAd.onError((err) => {
          console.error('插屏广告加载失败', err)
        })
        interstitialAd.onClose(() => {})
      }

      if (interstitialAd) {
        interstitialAd.show().catch((err) => {
          console.error('插屏广告显示失败', err)
        })
      }

			setTimeout(()=>{
				// 在适合的场景显示插屏广告
				if (interstitialAd) {
				  interstitialAd.show().catch((err) => {
				    console.error('插屏广告显示失败', err)
				  })
				}
			}, 1000*31)
			// #endif
			
			this.$Request.sysLog('浏览【首页】')
			
			this.getBannerList()
			this.getMsg()
			this.getGardList()
			this.getCourseList()

			//#ifdef H5
			this.initShare();	
			
			let that = this
			this.$u.get('/app/common/type/108').then(res => { // 是否开启公众号自动登陆 108
				if (res.code == 0 && res.data) {
					if (res.data.value == '是') {
						let ua = navigator.userAgent.toLowerCase();
						if (ua.indexOf('micromessenger') !== -1) {
							let openid = uni.getStorageSync('openId');
							let unionid = uni.getStorageSync('unionid');
							let userId = uni.getStorageSync('userId');
							if (userId) {
								if (!openid) {
									if (window.location.href.indexOf('?code=') !== -1 || window.location.href.indexOf('&code=') !== -1) {
										let code;
										if (window.location.href.indexOf('?code=') !== -1) {
											code = window.location.href.split('?code=')[1].split('&')[0];
										} else {
											code = window.location.href.split('&code=')[1].split('&')[0];
										}
										if (userId) {
											that.$u.get('/app/user/openId/' + code + '/' + userId/v2).then(
												res => {
													uni.setStorageSync('openid', res.data.openid)
													uni.setStorageSync('openId', res.data.openid)
													uni.setStorageSync('unionid', res.data.unionid)
												});
										}
									} else {
										
										let hrefFinal = 
											'https://open.weixin.qq.com/connect/oauth2/authorize?appid=' +
											that.$queue.getWxAppid() +
											'&redirect_uri=' +
											config.APIHOST2 + '&response_type=code&scope=snsapi_userinfo#wechat_redirect';
											
										window.location.href = hrefFinal
									}
								}
							} else if(openid) {
								this.$Request.get('/app/Login/openid/login?openId=' + openid+'&unionId=' + unionid).then(res => {
									if (res.code == 0) {
										this.$queue.setData("userId", res.user.userId);
										this.$queue.setData("token", res.token);
										this.$queue.setData("userName", res.user.userName);
										this.$queue.setData("avatar", res.user.avatar);
										this.$queue.setData("invitationCode", res.user.invitationCode);
										this.$queue.setData("inviterCode", res.user.inviterCode);
										
									} else {
										uni.navigateTo({
											url: '/pages/login/bind'
										});
									}
								});
							} else if (window.location.href.indexOf('?code=') !== -1 || window.location.href.indexOf('&code=') !== -1) {
								let code;
								if (window.location.href.indexOf('?code=') !== -1) {
									code = window.location.href.split('?code=')[1].split('&')[0];
								} else {
									code = window.location.href.split('&code=')[1].split('&')[0];
								}
								this.$Request.get('/app/Login/getOpenId/v2?code=' + code).then(ret => {
									uni.setStorageSync('openid', ret.data.openid)
									uni.setStorageSync('unionid', ret.data.unionid)
									// uni.setStorageSync('openidheadimgurl', ret.data.headimgurl)
									// uni.setStorageSync('openidnickname', ret.data.nickname)
							
									this.$Request.get('/app/Login/openid/login?openId=' + ret.data.openid+'&unionId='+ret.data.unionid).then(
										res => {
											if (res.code == 0) {
												this.$queue.setData("phone", res.user.phone);
												this.$queue.setData("userId", res.user.userId);
												this.$queue.setData("token", res.token);
												uni.setStorageSync('sex', res.user.sex)
												uni.setStorageSync('openId', res.user.openId)
												this.$queue.setData("userName", res.user.userName);
												this.$queue.setData("avatar", res.user.avatar);
												this.$queue.setData("invitationCode", res.user
													.invitationCode);
												this.$queue.setData("inviterCode", res.user
													.inviterCode);
												uni.switchTab({
													url: '/pages/index/index'
												})
											} else {
												uni.navigateTo({
													url: '/pages/login/bind'
												});
											}
										});
								});
								
							}
						}
					}
				}
			});
			
			// 获取当前小程序的页面栈
			let pages = getCurrentPages();
			// 数组中索引最大的页面--当前页面 的 参数
			let options = pages[pages.length-1].options;
			// 领取活动看点
			if(options && options.activity_invite_code){
				let that = this;
				let activity_invite_code = options.activity_invite_code;
				this.activity_invite_code = activity_invite_code;
				console.log('options.activity_invite_code', options.activity_invite_code)
				this.$u.get('/app/activity/invite/selectByHash/' + activity_invite_code).then(res => { 
					if(res.code == 0){
						that.num = res.data.num;
						that.title = res.data.title;
						that.reciveKanDianFlag = true;
					}
				});
			}
			//#endif
		},
		onReachBottom() {
			if (this.page < this.pages) {
				this.page += 1
				this.status = 'loading'
				this.getCourseList()
			} else {
				this.status = 'nomore'
			}
		},
		onPullDownRefresh() {
			this.page = 1
			this.getCourseList()
		},
		methods: {
			//获取个人信息
			getWeixinInfo(sendData) {
				let that = this;
				let postData = {
					openId: sendData.openId, //小程序openId
					unionId: sendData.unionId, //unionId
				};
				console.log('login', postData)
				that.$u.api.insertWxUser(postData).then(res => {
					uni.hideLoading();
					console.log('login', res)
					if (res.code == 0) {
						uni.setStorageSync('token', res.token)
						uni.setStorageSync('userId', res.user.userId)
						uni.setStorageSync('openId', res.user.openId)
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
			getKanDian(){
				if(null == this.activity_invite_code || '' == this.activity_invite_code){
					this.reciveKanDianFlag = false;
				}
				let userId = uni.getStorageSync('userId')
				// if (userId) {
					this.$u.get('/app/activity/invite/receiveByHash/' + this.activity_invite_code).then(res => {
						if (res.code == 0) {
							this.reciveKanDianFlag = false;
							this.getKanDianFlag = true;
						} else {
							uni.showToast({
								title: res.msg,
								duration: 1000,
								icon: 'none'
							});
						}
					});
				// } else {
				// 	uni.navigateTo({
				// 		url: "/pages/login/login"
				// 	})
				// }
			},
			initShare(){
				// #ifdef H5
				let ua = navigator.userAgent.toLowerCase();
				if (ua.indexOf('micromessenger') !== -1) {
					/*初始化分享*/
					this.$wechatMP.initShare(this, {
						title: '精选短剧',
						desc: '??欢迎来到精选短剧！??这里汇集了各种精彩短剧，让你免费观看，点击链接，尽情享受??网剧盛宴！',
						link: config.APIHOST2 + '/pages/index/index',
						imgUrl: 'https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/duanju-mp-index/img/share_logo.jpg'
					});
				}
				// #endif
			},
			//跳转关注公众号页面
 			openFollow() {
 				uni.navigateTo({
 					url: '/pages/me/erweimaRegister'
 				})
 			},
			// 打开隐私协议页面
			openPrivacyContract() {
				let that = this;
				wx.openPrivacyContract({
					fail: () => {
						that.$queue.showToast('遇到错误无法打开！');
					}
				})
			},
			// 拒绝隐私协议
			exitMiniProgram() {
				// 直接退出小程序
				wx.exitMiniProgram()
			},
			// 同意隐私协议
			handleAgreePrivacyAuthorization() {
				this.$refs.popusAuthorization.close();
			},
			//获取背景图
			getBgImg() {
				this.$u.get('/app/banner/selectBannerList?classify=5').then(res => {
					if (res.code == 0) {
						this.bgImg = res.data[0]?.imageUrl
						this.tuiguang = res.data[0]?.describes
					} else {
						uni.showToast({
							title: res.msg,
							duration: 1000,
							icon: 'none'
						});
					}
				});
			},
			//点击回调
			posterSuccess(item) {
				let userId = uni.getStorageSync('userId')
				// if (userId) {
					// #ifdef MP-WEIXIN
					uni.navigateTo({
						url: '/me/detail/detailMPWechat?id=' + item.courseId + '&courseDetailsId=' + item.courseDetailsId
					})
					// #endif
					
					// #ifndef MP-WEIXIN
					uni.navigateTo({
						url: '/me/detail/detail?id=' + item.courseId + '&courseDetailsId=' + item.courseDetailsId
					})
					// #endif
				// } else {
				// 	uni.navigateTo({
				// 		url: "/pages/login/login"
				// 	})
				// }
			},
			// 获取资源列表
			getCourseList() {
				let data = {
					limit: this.limit,
					page: this.page,
					sort: this.tabsList[this.current].id,
				}
				this.$u.api.courseList(data).then(res => {
					uni.stopPullDownRefresh()
					if (res.code == 0) {
						this.pages = res.data.totalPage
						if (this.page < this.pages) {
							this.status = 'loadmore'
						} else {
							this.status = 'nomore'
						}
						if (this.page == 1) {
							this.courseList = res.data.list
						} else {
							this.courseList = [...this.courseList, ...res.data.list]
						}
					} else {
						uni.showToast({
							title: res.msg,
							duration: 1000,
							icon: 'none'
						});
					}
					uni.stopPullDownRefresh();

				})
			},
			//切换类型
			selectTabs(index) {
				this.current = index
				this.page = 1

				this.getCourseList()
			},
			// 跳转资源列表
			goGridList(e) {
				// this.$Request.sysLog('浏览【"'+e.name+'"】页面')
				// if (uni.getStorageSync('token')) {
					uni.navigateTo({
						url: e.url
					})
				// } else {
				// 	uni.navigateTo({
				// 		url: "/pages/login/login"
				// 	})
				// }
			},
			//处理分割金刚区
			processArray(arr) {
				let arr2 = [];
				let child = [];
				for (let i = 0; i < arr.length; i++) {
					child.push(arr[i]);

					if (child.length === 5) {
						arr2.push({
							child: child
						});
						child = [];
					}
				}
				// 如果arr数组的长度不是5的倍数，将剩余的元素添加到arr2数组的最后一个child中
				if (child.length > 0) {
					arr2.push({
						child: child
					});
				}

				return arr2;
			},
			// 获取金刚区列表
			getGardList() {
				this.$u.api.bannerList({
					classify: '2',
				}).then(res => {
					if (res.code == 0) {
						let arr = []
						res.data.forEach(ret => {
							if (ret.state == 1) {
								arr.push(ret)
							}
						})
						this.gridList = arr
					} else {
						uni.showToast({
							title: res.msg,
							duration: 1000,
							icon: 'none'
						});
					}
				})
			},
			// 跳转搜索
			goSearch() {
				uni.navigateTo({
					url: '/pages/index/search/index'
				});
			},
			// 跳转公告链接
			goMsg(e) {
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
			},
			//轮播图跳转
			goPage(url) {
				let token = uni.getStorageSync('token')
				// if (token) {
					if (url) {
						if (url.indexOf('/pages/') !== -1 || url.indexOf('/me/') !== -1) {
							uni.navigateTo({
								url: 'plugin-private://wx94a6522b1d640c3b' + url
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
				// } else {
				// 	uni.navigateTo({
				// 		url: '/pages/login/login'
				// 	})
				// }
			},
			// 公告
			getMsg() {
				let data = {
					page: 1,
					limit: 5,
					state: 1
				}
				this.$u.api.msg(data).then(res => {
					if (res.code == 0) {
						this.notice = res.data.list
						res.data.list.forEach(res => {
							this.noticeList.push(res)
						})
					} else {
						uni.showToast({
							title: res.msg,
							duration: 1000,
							icon: 'none'
						});
					}
				})
			},
			// 获取轮播图列表
			getBannerList() {
				this.$u.api.bannerList({
					classify: '1'
				}).then(res => {
					if (res.code == 0) {
						res.data.forEach(d => {
							if (d.state == 1) {
								this.swiperList.push(d)
								console.log(this.swiperList)
							}
						})

					} else {
						uni.showToast({
							title: res.msg,
							duration: 1000,
							icon: 'none'
						});
					}
				})
			},
		},
	}
</script>

<style lang="scss">
	page {
		// 中性浅灰，比原来的 #F5F7FF 少一层蓝调，白卡片压在上面更干净
		background-color: #F5F5F7
	}

	.follow {
		width: 100%;
		position: fixed;
		bottom: 190rpx;
		left: 0;
 		z-index: 999;

		.follow-box {
			width: 686rpx;
			background-color: #ffffff;
			border-radius: 18rpx;
			padding: 20rpx 30rpx;
			position: relative;

			.follow-box-close {
				position: absolute;
				top: 10rpx;
				right: 10rpx;

			}

			.follow-box-l {
				image {
					width: 80rpx;
					height: 80rpx;
					border-radius: 12rpx;
					margin-right: 20rpx;
				}
			}

			.follow-box-l-txt-title {
				font-size: 28rpx;
				font-weight: bold;
			}

			.follow-box-l-txt-con {
				font-size: 26rpx;
				color: #999999;
			}

			.follow-box-r {
				background-color: #5074FF;
				color: #ffffff;
				padding: 10rpx 26rpx;
				border-radius: 30rpx;
			}
		}
	}

	/* 顶部栏。navigationStyle 为 custom，页面内容从屏幕最顶开始，
	   所以用 --status-bar-height 顶开状态栏；88rpx 正好是胶囊按钮那一行的高度，
	   标题因此和胶囊水平对齐，不再留出整块空白。 */
	.top-bar {
		width: 750rpx;
		height: 88rpx;
		padding: 0 32rpx;
		box-sizing: border-box;
		// 白底顶栏，跟下方浅灰页面自然分层，不用分割线
		background-color: #FFFFFF;
		// #ifdef MP-WEIXIN
		padding-top: var(--status-bar-height);
		height: calc(88rpx + var(--status-bar-height));
		// #endif

		.top-bar-title {
			font-weight: bold;
			font-size: 36rpx;
			color: #222222;
			line-height: 88rpx;
			letter-spacing: 1rpx;
		}
	}

	.video-list-box {
		// 顶部栏之下直接是卡片，无分区标题
		margin-top: 24rpx;
		width: 750rpx;
		padding: 0 24rpx;
		box-sizing: border-box;
		margin-bottom: 24rpx;

		.video-list {
			// 内容宽 702rpx（750 - 左右各 24），两列各 343rpx，中间正好剩 16rpx
			justify-content: space-between;
			.video-item {
				width: 343rpx;
				// 不用 flex 的 row-gap，安卓旧 WebView 支持不稳，改用 margin
				margin-bottom: 24rpx;
				background: #FFFFFF;
				border-radius: 20rpx;
				// 让封面的直角被卡片圆角裁掉
				overflow: hidden;
				.item-info {
					padding: 16rpx 20rpx 20rpx;
				}
				.item-title {
					font-family: PingFangSC, PingFang SC;
					font-weight: bold;
					font-size: 32rpx;
					color: #333333;
					line-height: 44rpx;
					text-align: left;
					font-style: normal;
					// 剧名过长省略，防止两列卡片高度不齐
					overflow: hidden;
					text-overflow: ellipsis;
					white-space: nowrap;
				}
				.item-sub-title {
					font-family: PingFangSC, PingFang SC;
					font-weight: 400;
					font-size: 24rpx;
					color: #999999;
					line-height: 36rpx;
					font-style: normal;
					margin-top: 8rpx;
					overflow: hidden;
					text-overflow: ellipsis;
					white-space: nowrap;
				}
			}
		}
	}

	.contentview {
		width: 632rpx;
		padding: 48rpx;
		box-sizing: border-box;
		background: #fff;
		border-radius: 16rpx;
	}

	.contentview .title {
		text-align: center;
		color: #333;
		font-weight: bold;
		font-size: 32rpx;
	}

	.contentview .des {
		font-size: 26rpx;
		color: #666;
		margin-top: 40rpx;
		text-align: justify;
		line-height: 1.6;
	}

	.contentview .des .link {
		color: #5074FF;
		text-decoration: underline;
	}

	button::after {
		border: none;
	}

	.btns {
		margin-top: 48rpx;
		display: flex;
	}

	.btns .item {
		justify-content: space-between;
		width: 244rpx;
		height: 80rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		border-radius: 16rpx;
		box-sizing: border-box;
		border: none;
	}

	.btns .reject {
		background: #f4f4f5;
		color: #909399;
	}

	.btns .agree {
		background: #5074FF;
		color: #fff;
	}
	
	.kd-bg{
		background-image: url('https://wefly.work/img/20250324/da1c6faf31054891a8d2b611faded94f.webp');
		background-repeat: no-repeat;
		background-size: cover;
		width: 628rpx;
		height: 858rpx;
		
		.kd-title {
			margin-top: 36rpx;
			width: 144rpx;
			height: 52rpx;
			font-weight: 500;
			font-size: 36rpx;
			color: #AB3000;
			line-height: 52rpx;
			text-align: center;
			font-style: normal;
		}
		
		.kd-content {
			margin-top: 72rpx;
			width: 240rpx;
			height: 56rpx;
			font-family: WenYiHei;
			font-weight: normal;
			font-size: 56rpx;
			color: #FDF2BF;
			line-height: 56rpx;
			letter-spacing: 2px;
			text-align: left;
			font-style: normal;
		}
		
		.kd-num{
			margin-top: 62rpx;
			width: 264rpx;
			height: 96rpx;
			font-family: WenYiHei;
			font-weight: normal;
			font-size: 96rpx;
			color: #FDF2BF;
			line-height: 96rpx;
			letter-spacing: 2px;
			text-align: left;
			font-style: normal;
			text-align: center;
			
			.kd-unit {
				width: 36rpx;
				height: 36rpx;
				font-weight: 400;
				font-size: 36rpx;
				color: #FDF2BF;
				line-height: 36rpx;
				text-align: left;
				font-style: normal;
			}
		}
		
		.kd-btn {
			margin-top: 120rpx;
			width: 192rpx;
			height: 48rpx;
			font-weight: 500;
			font-size: 48rpx;
			color: #FFFFFF;
			line-height: 48rpx;
			text-align: left;
			font-style: normal;
		}
		
	}
	
	.kd-succ-bg {
		background-image: url('https://wefly.work/img/20250324/31484d6dd0cc45aab2b002f8b51f6eae.webp');
		background-repeat: no-repeat;
		background-size: cover;
		width: 750rpx;
		height: 750rpx;
		position: relative;
		
		.kd-succ-title {
			width: 192rpx;
			height: 48rpx;
			font-weight: 500;
			font-size: 48rpx;
			color: #510303;
			line-height: 48rpx;
			text-align: left;
			font-style: normal;
			margin-top: 96rpx;
		}
		
		.kd-succ-btn {
			width: 144rpx;
			height: 48rpx;
			font-weight: 500;
			font-size: 48rpx;
			color: #FFFFFF;
			line-height: 48rpx;
			text-align: left;
			font-style: normal;
			position: absolute;
			bottom: 152rpx;
		}
	}
	
</style>