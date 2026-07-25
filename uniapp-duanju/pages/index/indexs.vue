<template>
	<view class="" style="padding-bottom: 50rpx;">
		<!-- #ifdef H5 -->
		<u-sticky :enable="enable" :offset-top="-88">
			<!-- #endif -->
			<!-- #ifndef H5 -->
			<u-sticky :enable="enable">
				<!-- #endif -->
				<view class="tabs flex align-center justify-center">
					<view class="tabs-box flex align-center flex-wrap">
						<view class="tabs-box-item" @click="selectCur(index)" :class="tabCur == index?'activeTabs':''"
							v-for="(item,index) in tabsList" :key="index">
							{{item}}
						</view>
					</view>
				</view>
			</u-sticky>
			<!-- 推荐短剧 -->
			<block v-if="tabCur==0">
				<view style="background-color: #ffffff;padding-bottom: 20rpx;"
					class="padding-lr padding-top-sm flex align-center justify-between">
					<view style="width: 90%;" @click.stop="goSearch">
						<u-search placeholder="搜索更多资源" v-model="keyword" shape="round" :disabled="false"
							:show-action="false" :animation="true"></u-search>
					</view>
					<view class="" @click.stop="goMsglist">
						<image
							src="https://your-api-domain.com/file/uploadPath/2023/03/07/74ec77d19676555558e5e04464fe0359.png"
							style="width: 45rpx;height: 40rpx;"></image>
					</view>
				</view>
				<view class="padding-lr padding-top" v-if="swiperList.length>0">
					<swiper :indicator-dots="true" class="swiper " :autoplay="true" interval="5000" duration="500"
						:circular="true" style="width: 100%;height: 350rpx;">
						<swiper-item v-for="(item,index) in swiperList" :key='index' @tap="goPage(item.url)">
							<image :src="item.imageUrl" mode="scaleToFill"
								style="width: 100%;height: 100%;border-radius: 24rpx;"></image>
						</swiper-item>
					</swiper>
				</view>

				<view class="padding-top" v-if="noticeList.length>0">
					<view class=" flex align-center margin-lr padding-lr" v-if="noticeList.length > 0"
						style="background: #ffffff;border-radius: 16rpx;">
						<view style="margin-top: 8upx;" class="margin-right">
							<image src="https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/index/5.png" style="width:116upx;height: 24upx;">
							</image>
						</view>
						<swiper :autoplay="true" :vertical="true" :interval="4000" :circular="true"
							:indicator-dots="false" class="index_gonggao">
							<block v-for="(item, index) in noticeList">
								<swiper-item @tap='goMsg(item.url)'>
									<view class="">{{item.title}}</view>
								</swiper-item>
							</block>
						</swiper>
					</view>
				</view>

				<view class="padding-tb-sm flex align-center justify-center" v-if="gridList.length>0">
					<view class=""
						style="width: 686rpx;background-color: #ffffff;border-radius: 24rpx;padding: 20rpx 0;">
						<u-grid :col="4" :border="false">
							<u-grid-item v-for="(item,index) in gridList" :key="index" @click="goGridList(item)">
								<view class="">
									<image style="width: 80rpx;height: 80rpx;" :src="item.imageUrl" mode=""></image>
								</view>
								<view class="" style="font-size: 24rpx;">
									{{item.name}}
								</view>
							</u-grid-item>
						</u-grid>
					</view>

					<!-- <u-grid :col="5" :border="false">
					<u-grid-item v-for="(item,index) in gridList" :key="index" @click="goGridList(item)">
						<u-image width="64rpx" height="64rpx" :src="item.imageUrl"></u-image>
						<view class="grid-text margin-top-xs" style="font-size: 26rpx;">{{item.name}}</view>
					</u-grid-item>
				</u-grid> -->
				</view>
				<!-- 本周热门 -->
				<view class="padding-lr" v-if="rmList.length != 0">
					<view class="flex justify-between margin-tb">
						<view class="flex align-center">
							<image src="https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/index/zv1.png"
								style="width: 60rpx;height: 60rpx;margin-right: 10rpx;" mode=""></image>
							<view class="text-black text-lg text-bold">本周热门</view>
						</view>
						<view class="text-gray text-26" style="color: #aeb2c1;" @click="huanyihuan()">
							换一换
						</view>
					</view>
					<view class="vidoList flex align-center justify-between flex-wrap">
						<view class="vidoList-item" style="margin-bottom: 0;"
							@click="goCourse(item.courseId,item.courseDetailsId)" v-for="(item, index) in rmList"
							:key="index">
							<view class="vidoList-item-img">
								<image :src="item.titleImg" mode="aspectFill"></image>
							</view>
							<view class="vidoList-item-title">
								{{item.title}}
							</view>
						</view>
						<view class="vidoList-item" style="height: 0;"></view>
						<view class="vidoList-item" style="height: 0;"></view>
					</view>
				</view>
				<!-- 排行榜 -->
				<view class="padding-lr" v-if="phList.length!=0">
					<view class="flex justify-between margin-tb">
						<view class="flex align-center">
							<image src="https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/index/zb1.png"
								style="width: 60rpx;height: 60rpx;margin-right: 10rpx;" mode=""></image>
							<view class="text-black text-lg text-bold">本周排行榜</view>
						</view>
						<view class="text-gray text-26" style="color: #aeb2c1;">
							每周热剧TOP3
						</view>
					</view>
					<view class="vidoList flex align-center justify-between flex-wrap">
						<view class="vidoList-item" style="margin-bottom: 0;"
							@click="goCourse(item.courseId,item.courseDetailsId)" v-for="(item, index) in phList"
							:key="index">
							<view class="vidoList-item-img">
								<image :src="item.titleImg" mode="aspectFill"></image>
							</view>
							<view class="vidoList-item-title">
								{{item.title}}
							</view>
						</view>
						<view class="vidoList-item" style="height: 0;"></view>
						<view class="vidoList-item" style="height: 0;"></view>
					</view>
				</view>
				<!-- 最新热播 -->
				<view class="padding-lr">
					<view class="flex justify-between margin-tb">
						<view class="flex align-center">
							<image src="https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/index/cnxh.png"
								style="width: 60rpx;height: 60rpx;margin-right: 10rpx;" mode=""></image>
							<view class="text-black text-lg text-bold">最新热播</view>
						</view>
						<view style="color: #aeb2c1;" class="text-gray text-26"
							@click="goNav('/pages/index/course/courseList?title=最新热播')">
							更多
							<u-icon name="arrow-right"></u-icon>
						</view>
					</view>
					<view class="vidoList flex align-center justify-between flex-wrap">
						<view class="vidoList-item" @click="goCourse(item.courseId,item.courseDetailsId)"
							v-for="(item, index) in courseList" :key="index">
							<view class="vidoList-item-img">
								<image :src="item.titleImg" mode="aspectFill"></image>
							</view>
							<view class="vidoList-item-title">
								{{item.title}}
							</view>
						</view>
						<view class="vidoList-item" style="height: 0;"></view>
						<view class="vidoList-item" style="height: 0;"></view>
					</view>
					<empty v-if="courseList.length == 0" />
					<u-loadmore v-if="courseList.length > 0" :status="status" />
				</view>

			</block>
			<!-- 剧情介绍 -->
			<block v-if="tabCur==1">
				<view class="plot flex align-center justify-center">
					<view class="plot-box">
						<view class="plot-box-item flex justify-between"
							@click="goCourse(item.courseId,item.courseDetailsId)" v-for="(item,index) in jqList"
							:key="index">
							<view class="plot-box-item-l">
								<image :src="item.titleImg" mode=""></image>
							</view>
							<view class="plot-box-item-r">
								<view class="plot-box-item-r-title">
									{{item.title}}
								</view>
								<view class="plot-box-item-r-content" v-html="item.details">

								</view>
							</view>
						</view>
					</view>
				</view>
				<empty v-if="jqList.length == 0" />
				<u-loadmore v-if="jqList.length > 4" :status="status" />
			</block>
			<!-- 剧情壁纸 -->
			<block v-if="tabCur==2">
				<view class="videoImg flex align-center justify-center">
					<view class="videoImg-box flex justify-between align-center flex-wrap">
						<view class="videoImg-box-item" v-for="(item,index) in wallpaperList" :key="index">
							<image @click="priveImg(item.imageUrl)" :src="item.imageUrl" mode="aspectFill"></image>
						</view>
					</view>
				</view>
				<empty v-if="wallpaperList.length == 0" />
				<u-loadmore v-if="wallpaperList.length >= 4" :status="status" />
			</block>
			<uni-popup ref="popusAuthorization" type="center" :maskClick="false">
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
			</uni-popup>
	</view>
</template>

<script>
	import empty from '../../components/empty.vue'
	export default {
		components: {
			empty
		},
		data() {
			return {
				status: 'loadmore',
				enable: true,
				tabCur: 0,
				tabsList: ['推荐短剧', '剧情介绍', '剧情壁纸'],
				keyword: '',
				swiperList: [], //轮播图列表
				gridList: [], //金刚区列表
				noticeList: [],
				notice: [],
				courseList: [], //资源列表
				current: 0,
				swiperCurrent: 0,
				tabsHeight: 0,
				dx: 0,
				loadStatus: ['loadmore', 'loadmore', 'loadmore', 'loadmore'],
				page: 1,
				limit: 10,
				newTab: 0,
				datalist: [{
					id: 1,
					name: '综合'
				}, {
					id: 2,
					name: '人气'
				}, {
					id: 3,
					name: '价格',
					label: '0'
				}],
				dataIndex: 0,
				sort: 1,
				count: 0,
				rmList: [], //本周热门
				rePage: 1, //热门页数
				pageSizeRm: 1, //热门总页数
				phList: [], //排行榜数据
				pages: 1, //总页数
				jqList: [], //剧情列表
				wallpaperList: [], //壁纸数组
				bgImg: '',
				tuiguang: '',
				invitationCode: 0,
				privacyContractName: '',
			};
		},
		onHide() {
			this.enable = false
		},
		onShareAppMessage(res) {
			return {
				path: '/pages/index/index?invitation=' + this.invitationCode, //这是为了传参   onload(data){let id=data.id;} 
				title: this.tuiguang,
				imageUrl: this.bgImg
			}
		},
		onLoad(options) {
			// #ifdef MP-WEIXIN
			let that = this
			wx.getPrivacySetting({
				success: res => {
					console.log("是否需要授权：", res.needAuthorization, "隐私协议的名称为：", res.privacyContractName)
					if (res.needAuthorization) {
						that.privacyContractName = res.privacyContractName;
						that.$refs.popusAuthorization.open();
					}
				},
				fail: () => {},
				complete: () => {},
			})
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

		},
		onShow() {
			this.page = 1
			this.enable = true
			this.getBannerList();
			this.getGardList();
			this.getMsg()
			this.getRmList()
			this.getPhList()
			this.getCourseList()
		},
		methods: {
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
			/**
			 * @param {Object} url 图片链接
			 * 预览图片
			 */
			priveImg(url) {
				console.log(url)
				uni.previewImage({
					urls: [url],
					current: 0
				})
			},
			getWallpaperList() {
				let data = {
					classify: 10,
					page: this.page,
					limit: 10,
				}
				// selectBannerPage
				this.$u.api.selectBannerPage(data).then(res => {
					uni.stopPullDownRefresh()
					if (res.code == 0) {
						this.pages = res.data.totalPage
						if (this.page < this.pages) {
							this.status = 'loadmore'
						} else {
							this.status = 'nomore'
						}
						if (this.page == 1) {
							this.wallpaperList = res.data.list
						} else {
							this.wallpaperList = [...this.wallpaperList, ...res.data.list]
						}
					}
				})
			},
			/**
			 * 获取剧情列表
			 */
			getJqList() {
				let data = {
					limit: this.limit,
					page: this.page,
					sort: '',
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
							this.jqList = res.data.list
						} else {
							this.jqList = [...this.jqList, ...res.data.list]
						}

					}
				})
			},
			/**
			 * 获取排行榜视频
			 */
			getPhList() {
				let data = {
					page: 1,
					limit: 3,
					sort: 1,
				}
				this.$u.api.courseList(data).then(res => {
					if (res.code == 0) {
						this.phList = res.data.list
					}
				})
			},
			/**
			 * @param {Number} start
			 * @param {number} end
			 * @param 传入两个数字，开始跟结束，返回两个数之间的随机数
			 */
			getRandomNumber(start, end) {
				// 验证参数是否为有效的整数
				if (!Number.isInteger(start) || !Number.isInteger(end)) {
					throw new Error('参数必须是整数');
				}
				// 验证结束数是否大于等于开始数
				if (end < start) {
					throw new Error('结束数必须大于等于开始数');
				}
				// 计算区间范围
				const range = end - start + 1;
				// 生成随机数
				const randomNumber = Math.floor(Math.random() * range) + start;
				// 返回随机数
				return randomNumber;
			},
			/**
			 * 点击换一换
			 */
			huanyihuan() {
				this.rePage = this.getRandomNumber(1, this.pageSizeRm)
				this.getRmList()
			},
			/**
			 * 获取热门视频
			 */
			getRmList() {
				let data = {
					page: this.rePage,
					limit: 3,
					sort: 2,
				}
				this.$u.api.courseList(data).then(res => {
					if (res.code == 0) {
						this.pageSizeRm = res.data.totalPage
						this.rmList = res.data.list
					}
				})
			},
			//切换类型
			selectCur(index) {
				this.page = 1
				this.status = 'loadmore'
				this.tabCur = index
				if (this.tabCur == 0) { //短剧推荐
					this.getCourseList()
				} else if (this.tabCur == 1) { //剧情介绍
					this.getJqList()
				} else {
					this.getWallpaperList() //壁纸
				}
			},
			goMsglist() {
				uni.navigateTo({
					url: '/me/message/index'
				})
			},
			goPage(url) {
				let token = uni.getStorageSync('token')
				if (token) {
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
				} else {
					uni.navigateTo({
						url: '/pages/login/login'
					})
				}
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
			// 获取资源列表
			getCourseList() {
				let data = {
					limit: this.limit,
					page: this.page,
					sort: '',
				}
				this.$u.api.courseList(data).then(res => {
					if (res.code == 0) {
						this.pages = res.data.totalPage
						if (this.page < this.pages) {
							this.status = 'loadmore'
						} else {
							this.status = 'nomore'
						}
						res.data.list.forEach(ret => {
							ret.courseLabel = ret.courseLabel ? ret.courseLabel.split(',') : []
						})
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
			// 点击tab栏切换
			change(index) {
				// this.courseList=[]
				this.swiperCurrent = index;
			},
			// 拖动页面切换
			animationfinish({
				detail: {
					current
				}
			}) {
				this.courseList = []
				this.$refs.tabs.setFinishCurrent(current);
				this.swiperCurrent = current;
				this.current = current;

				this.tabList.forEach(res => {
					// console.log(res.classification_name,'************',this.tabList[this.swiperCurrent].classification_name )
					if (res.classification_name == this.tabList[this.swiperCurrent].classification_name) {
						this.newTab = res.classification_id
						this.getCourseList(res.classification_id);
					}
				})
			},
			// 动画
			transition({
				detail: {
					dx
				}
			}) {
				this.$refs.tabs.setDx(dx);
			},
			goNav(e) {
				uni.navigateTo({
					url: e
				})
			},
			// 跳转资源列表
			goGridList(e) {
				if (uni.getStorageSync('token')) {
					uni.navigateTo({
						url: e.url
					})
				} else {
					uni.navigateTo({
						url: "/pages/login/login"
					})
				}

				// uni.navigateTo({
				// 	url: '/pages/index/course/courseList?id=' + e.id + '&title=' + e.name
				// });
			},
			// 跳转资源详情
			goCourse(e, courseDetailsId) {
				let userId = uni.getStorageSync('userId')
				if (userId) {
					
					// #ifndef MP-WEIXIN
					uni.navigateTo({
						url: '/me/detail/detail?id=' + e + '&courseDetailsId=' + courseDetailsId
					})
					// #endif
					// #ifdef MP-WEIXIN
					uni.navigateTo({
						url: '/me/detail/detailMPWechat?id=' + e + '&courseDetailsId=' + courseDetailsId
					})
					// #endif
				} else {
					uni.navigateTo({
						url: "/pages/login/login"
					})
				}
			},
			// 跳转搜索
			goSearch() {
				uni.navigateTo({
					url: '/pages/index/search/index'
				});
			},

		},
		onReachBottom: function() {
			console.log(this.page, '111111111')
			console.log(this.pages, '111111111')
			if (this.page < this.pages) {
				this.page += 1
				console.log(this.page, '22222222222')
				this.status = 'loading'
				if (this.tabCur == 0) { //推荐短剧
					this.getCourseList()
				} else if (this.tabCur == 1) { //剧情介绍
					this.getJqList()
				} else {
					this.getWallpaperList() //壁纸
				}

			} else {
				this.status = 'nomore'
			}


		},
		onPullDownRefresh: function() {
			this.page = 1;
			if (this.tabCur == 0) { //推荐短剧
				this.getCourseList()
			} else if (this.tabCur == 1) { //剧情介绍
				this.getJqList()
			} else {
				this.getWallpaperList() //壁纸
			}

		},
	};
</script>

<style lang="scss" scoped>
	.activeTabs {
		color: #5074FF !important;
	}

	// /deep/.u-grid-item:nth-of-type(4n - 3){
	// 	border-radius: 24rpx 0 0 24rpx;
	// }
	// /deep/.u-grid-item:nth-of-type(4n){
	// 	border-radius:  0 24rpx 24rpx 0;
	// }
  ::v-deep.u-grid-item {
		border-radius: 24rpx !important;
	}

	.tabs {
		width: 100%;
		height: 80rpx;
		background-color: #FFFFFF;
		// box-shadow: 0px 4rpx 16rpx 0px rgba(0, 0, 0, .08);

		.tabs-box {
			width: 686rpx;
			height: 100%;
		}

		.tabs-box-item {
			color: #999999;
			margin-right: 40rpx;
			font-weight: 500;
			// font-weight: bold;
		}
	}

	.plot {
		width: 100%;
		height: auto;
		padding-top: 20rpx;

		.plot-box {
			width: 686rpx;
			height: 100%;
		}

		.plot-box-item {
			width: 100%;
			margin-bottom: 20rpx;
			background-color: #FFFFFF;
			border-radius: 24rpx;
			padding: 20rpx;
		}

		.plot-box-item-l {
			width: 200rpx;
			height: 260rpx;
			border-radius: 16rpx;

			image {
				width: 200rpx;
				height: 260rpx;
				border-radius: 16rpx;
			}

		}

		.plot-box-item-r {
			width: calc(686rpx - 260rpx);
		}

		.plot-box-item-r-title {
			font-size: 32rpx;
			font-weight: bold;
			color: #2e2f33;
		}

		.plot-box-item-r-content {
			font-size: 28rpx;
			color: #2e2f33;
			margin-top: 10rpx;
		}
	}

	.videoImg {
		width: 100%;
		padding-top: 20rpx;
		height: auto;

		.videoImg-box {
			width: 686rpx;
			height: 100%;
		}

		.videoImg-box-item {
			width: 49%;
			height: 500rpx;
			border-radius: 18rpx;
			margin-bottom: 20rpx;

			image {
				width: 100%;
				height: 500rpx;
				border-radius: 18rpx;
			}
		}
	}

	.vidoList {
		width: 100%;
		height: auto;
		margin-bottom: 20rpx;

		.vidoList-item {
			width: calc((100% - 40rpx) / 3);
			height: 356rpx;
			background-color: #FFFFFF;
			border-radius: 24rpx;
			margin-bottom: 20rpx;
		}

		.vidoList-item-img {
			width: 100%;
			height: 280rpx;
			border-radius: 24rpx 24rpx 0 0;

			image {
				width: 100%;
				height: 100%;
				border-radius: 24rpx 24rpx 0 0;
			}
		}

		.vidoList-item-title {
			width: 100%;
			text-align: center;
			background-color: #FFFFFF;
			border-radius: 0 0 24rpx 24rpx;
			padding: 20rpx;
			overflow: hidden;
			text-overflow: ellipsis; //溢出用省略号显示
			white-space: nowrap; // 默认不换行；
		}
	}

	.bg {
		background: #FFFFFF;
	}

	// .wrap {
	// 	display: flex;
	// 	flex-direction: column;
	// 	height: calc(100vh - var(--window-top));
	// 	width: 100%;
	// }

	// .swiper-box {
	// 	flex: 1;
	// }

	.index_gonggao {
		color: #666666;
		font-size: 26rpx;
		width: 550rpx;
		height: 66rpx;
		// background: #EAF4FE;
		border-radius: 50rpx;
		align-items: center;
		line-height: 50rpx;
		padding: 10rpx 15rpx;

	}

	.line {
		width: 8rpx;
		height: 32rpx;
		background: #5074FF;
		border-radius: 4rpx;
		margin-right: 15rpx;
	}

	.btn {
		width: 150upx;
		height: 60upx;
		background: #5074FF;
		border-radius: 30upx;
		color: #FFFFFF;
		text-align: center;
		line-height: 60rpx;
		font-size: 26rpx;
	}

	.active {
		color: #5074FF;
	}

	.privacy {
		position: fixed;
		top: 0;
		right: 0;
		bottom: 0;
		left: 0;
		background: rgba(0, 0, 0, .5);
		z-index: 9999999;
		display: flex;
		align-items: center;
		justify-content: center;
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
</style>