<template>
	<view>
		<view class="wrap">
			<view class="head">
				<scroll-view scroll-x class="bg nav bg-white u-border-bottom">
					<view class="flex text-center">
						<view class="cu-item flex-sub  text-bold" :class="item.name==TabCur?' cur ':'text-black'"
							v-for="(item,index) in tabList" :key="index" :data-id="item.name" @tap="tabSelect">
							{{item.name}}
							<view v-if="item.name==TabCur"
								style="width: 64rpx;height: 8rpx;;background: #5074FF;margin: -20rpx auto;border-radius: 10rpx;">
							</view>
						</view>
					</view>
				</scroll-view>
			</view>
			<view class="headlen">
				<view v-if="TabCur == tabList[0].name">
					<view class="page-box" v-if="latelyCourseList.length && userId">
						<view class="order" v-for="(item, index) in latelyCourseList" :key="index"
							@click="goCourseDet(item)">
							<view class="item">
								<view class="left">
									<image :src="item.titleImg" mode="aspectFill"
										style="border-radius: 10rpx;width: 180rpx;height: 180rpx;"></image>
								</view>
								<view class="flex-sub content flex flex-direction justify-between"
									style="width:420rpx;">
									<view class="text-bold text-black margin-top-xs u-line-1 text-lg">{{item.title}}
									</view>
									<view class="text-gray text-26 margin-top">最近{{item.payNum}}人在学 </view>
									<view class="flex align-center justify-between">
										<view class=" " style="color: #FF8211;">¥ <text class=" text-bold"
												style="font-size: 42rpx;">{{item.price}}</text></view>
										<view class="btn">马上学习</view>
									</view>
								</view>
							</view>
						</view>
					</view>
					<!-- 空数据 -->
					<empty v-else-if="!latelyCourseList.length && userId"></empty>
					<noLogin v-if="userId === ''"></noLogin>
				</view>
				<view v-if="TabCur == tabList[1].name">
					<view class="page-box" v-if="selectCourseList.length  && userId">
						<view class="order" v-for="(item, index) in selectCourseList" :key="index"
							@click="goCourseDet(item)">
							<view class="item">
								<view class="left">
									<image :src="item.titleImg" mode="aspectFill"
										style="border-radius: 10rpx;width: 180rpx;height: 180rpx;"></image>
								</view>
								<view class="flex-sub content flex flex-direction justify-between"
									style="width:420rpx;">
									<view class="text-bold text-black margin-top-xs u-line-1 text-lg">{{item.title}}
									</view>
									<view class="text-gray text-26 margin-top">最近{{item.payNum}}人在学 </view>
									<view class="flex align-center justify-between">
										<view class=" " style="color: #FF8211;">¥ <text class=" text-bold"
												style="font-size: 42rpx;">{{item.price}}</text></view>
										<view class="btn">马上学习</view>
									</view>
								</view>
							</view>
						</view>
					</view>
					<!-- 空数据 -->
					<empty v-else-if="!selectCourseList.length && userId"></empty>
					<noLogin v-if="userId === ''"></noLogin>
				</view>
				<view v-if="TabCur == tabList[2].name">
					<view class="page-box" v-if="collectList.length && userId">
						<view class="order" v-for="(item,index) in collectList" :key='index' @click="goCourseDet(item)">
							<view class="item">
								<view class="left">
									<image :src="item.titleImg" mode="aspectFill"
										style="border-radius: 10rpx;width: 180rpx;height: 180rpx;"></image>
								</view>
								<view class="flex-sub content flex flex-direction justify-between"
									style="width:420rpx;">
									<view class="text-bold text-black margin-top-xs u-line-1 text-lg">{{item.title}}
									</view>
									<view class="text-gray text-26 margin-top">最近{{item.payNum}}人在学 </view>
									<view class="flex align-center justify-between">
										<view class=" " style="color: #FF8211;">¥ <text class=" text-bold"
												style="font-size: 42rpx;">{{item.price}}</text></view>
										<view class="btn">马上学习</view>
									</view>
								</view>
							</view>
						</view>
					</view>
					<!-- 空数据 -->
					<empty v-else-if="!collectList.length && userId" content="去添加"></empty>
					<noLogin v-if="userId === ''"></noLogin>
				</view>
			</view>

		</view>
	</view>
</template>

<script>
	import empty from '@/components/empty.vue'
	import noLogin from '@/components/noLogin.vue'
	export default {
		components: {
			empty,
			noLogin
		},
		data() {
			return {
				TabCur: '最近学习',
				orderList: [
					[],
					[],
					[]
				],
				tabList: [{
						name: '最近学习'
					},
					{
						name: '已购资源'
					},
					{
						name: '收藏资源'
					}
				],
				current: 0,
				swiperCurrent: 0,
				tabsHeight: 0,
				dx: 0,
				loadStatus: ['loadmore', 'loadmore', 'loadmore', 'loadmore'],
				userId: '',

				limit: 10,
				latelyCourseList: [], //最近学习
				latelyCoursePage: 1,
				selectCourseList: [], //已购资源
				selectCoursePage: 1,
				collectList: [], //收藏资源
				collectPage: 1,
				totalCount: 0
			};
		},
		onLoad() {

		},
		onShow() {
			console.log(this.latelyCourseList)
			this.userId = uni.getStorageSync('userId') ? uni.getStorageSync('userId') : ''
			if (this.userId) {
				// this.latelyCourseList = []
				// this.selectCourseList = []
				// this.collectList = []
				this.getLatelyCourse()
				this.getSelectCourse()
				this.getCollectList()
			}
		},
		methods: {
			tabSelect(e) {
				// console.log(e.currentTarget.dataset)
				this.TabCur = e.currentTarget.dataset.id;
				this.userId = uni.getStorageSync('userId') ? uni.getStorageSync('userId') : ''
				// console.log("this.userId____:" + this.userId)
				if (this.userId) {
					switch (this.TabCur) {
						case '最近学习': //最近学习
							this.latelyCoursePage = 1
							this.getLatelyCourse()
							break;
						case '已购资源': //已购资源
							this.selectCoursePage = 1
							this.getSelectCourse()
							break;
						case '收藏资源': //已购资源
							this.collectPage = 1;
							this.getCollectList()
							break;
					}
				}
			},
			// 最近学习
			getLatelyCourse() {
				uni.showLoading({
					title: '加载中'
				})
				let data = {
					userId: this.userId,
					page: this.latelyCoursePage,
					limit: this.limit,
				}
				this.$u.api.latelyCourse(data).then(res => {
					uni.hideLoading()
					uni.stopPullDownRefresh();
					if (res.code == 0) {
						res.data.list.forEach(ret => {
							if (ret.avatar) {
								ret.avatar = ret.avatar.split(',')
							}
						})
						this.totalCount = res.data.totalCount
						if (this.latelyCoursePage == 1) {
							this.latelyCourseList = res.data.list;
						} else {
							this.latelyCourseList = [...this.latelyCourseList, ...res.data.list]
						}
					} else {
						uni.showToast({
							title: res.msg,
							duration: 1000,
							icon: 'none'
						});
					}
					

				})
			},
			// 已购资源
			getSelectCourse() {
				uni.showLoading({
					title: '加载中'
				})
				let data = {
					userId: this.userId,
					page: this.selectCoursePage,
					limit: this.limit,
				}
				this.$u.api.selectCourse(data).then(res => {
					uni.hideLoading()
					uni.stopPullDownRefresh();
					if (res.code == 0) {
						res.data.list.forEach(ret => {
							if (ret.avatar) {
								ret.avatar = ret.avatar.split(',')
							}
						})
						this.totalCount = res.data.totalCount
						if (this.selectCoursePage == 1) {
							this.selectCourseList = res.data.list;
						} else {
							this.selectCourseList = [...this.selectCourseList, ...res.data.list]
						}
					} else {
						uni.showToast({
							title: res.msg,
							duration: 1000,
							icon: 'none'
						});
					}
					
				})
			},
			// 获取收藏信息
			getCollectList() {
				uni.showLoading({
					title: '加载中'
				})
				let data = {
					userId: this.userId,
					page: this.collectPage,
					limit: this.limit,
				}
				this.$u.api.collectList(data).then(res => {
					uni.hideLoading()
					uni.stopPullDownRefresh();
					if (res.code == 0) {
						this.totalCount = res.data.totalCount
						if (this.collectPage == 1) {
							this.collectList = res.data.records;
						} else {
							this.collectList = [...this.collectList, ...res.data.records]
						}
					} else {
						uni.showToast({
							title: res.msg,
							duration: 1000,
							icon: 'none'
						});
					}
				
				})
			},
			// 查看资源详情
			goCourseDet(e) {
				let userId = uni.getStorageSync('userId')
				if (userId) {
					uni.navigateTo({
						url: '/pages/index/course/courseDet?id=' + e.courseId
					});
				} else {
					uni.navigateTo({
						url: "/pages/login/login"
					})
				}
			},

		},
		computed: {
			// 价格整数
			priceInt() {
				return val => {
					// if (val !== parseInt(val)) return val.split('.')[0];
					// else return val;
					return val
				};
			}
		},
		onReachBottom: function() {
			this.userId = uni.getStorageSync('userId') ? uni.getStorageSync('userId') : ''
			if (this.userId) {
				if (this.TabCur == '最近学习') { //最近学习
					if (this.totalCount == this.latelyCourseList.length) {
						uni.showToast({
							title: '已经到底了~',
							icon: 'none'
						})
						return
					} else {
						this.latelyCoursePage = this.latelyCoursePage + 1;
						this.getLatelyCourse()
					}
				} else if (this.TabCur == '已购资源') { //已购资源
					if (this.totalCount == this.selectCourseList.length) {
						uni.showToast({
							title: '已经到底了~',
							icon: 'none'
						})
						return
					} else {
						this.selectCoursePage = this.selectCoursePage + 1;
						this.getSelectCourse()
					}
				} else if (this.TabCur == '收藏资源') { //收藏资源
					if (this.totalCount == this.collectList.length) {
						uni.showToast({
							title: '已经到底了~',
							icon: 'none'
						})
						return
					} else {
						this.collectPage = this.collectPage + 1;
						this.getCollectList()
					}
				}
				// switch (this.TabCur) {
				// 	case '最近学习': //最近学习
				// 		this.latelyCoursePage = this.latelyCoursePage + 1;
				// 		this.getLatelyCourse()
				// 		break;
				// 	case '已购资源': //已购资源
				// 		this.selectCoursePage = this.selectCoursePage + 1;
				// 		this.getSelectCourse()
				// 		break;
				// 	case '收藏资源': //评论
				// 		this.collectPage = this.collectPage + 1;
				// 		this.getCollectList()
				// 		break;
				// }
			}
		},
		onPullDownRefresh: function() {
			this.userId = uni.getStorageSync('userId') ? uni.getStorageSync('userId') : ''
			if (this.userId) {
				if (this.TabCur == '最近学习') { //最近学习
					if (this.totalCount == this.latelyCourseList.length) {
						uni.showToast({
							title: '已经到底了~',
							icon: 'none'
						})
						return
					} else {
						this.latelyCoursePage = 1
						this.getLatelyCourse()
					}
				} else if (this.TabCur == '已购资源') { //已购资源
					if (this.totalCount == this.selectCourseList.length) {
						uni.showToast({
							title: '已经到底了~',
							icon: 'none'
						})
						return
					} else {
						this.selectCoursePage = 1
						this.getSelectCourse()
					}
				} else if (this.TabCur == '收藏资源') { //收藏资源
					if (this.totalCount == this.collectList.length) {
						uni.showToast({
							title: '已经到底了~',
							icon: 'none'
						})
						return
					} else {
						this.collectPage = 1
						this.getCollectList()
					}
				}
				// switch (this.TabCur) {
				// 	case '最近学习': //最近学习
				// 		this.latelyCoursePage = 1
				// 		// this.latelyCourseList = []
				// 		this.getLatelyCourse()
				// 		break;
				// 	case '已购资源': //已购资源
				// 		this.selectCoursePage = 1
				// 		// this.selectCourseList = []
				// 		this.getSelectCourse()
				// 		break;
				// 	case '收藏资源': //已购资源
				// 		this.collectPage = 1;
				// 		// this.collectList = []
				// 		this.getCollectList()
				// 		break;
				// }
			}
		},
	};
</script>

<style>
	/* #ifndef H5 */
	page {
		height: 100%;
		background-color: #f2f2f2;
	}

	/* #endif */
</style>

<style lang="scss" scoped>
	.head {
		position: fixed;
		/* #ifdef H5 */
		top: 0rpx;
		/* #endif */
		/* #ifndef H5 */
		top: 0;
		/* #endif */
		left: 0;
		right: 0;
		z-index: 999;
	}

	.headlen {
		/* #ifdef H5 */
		margin-top: 80rpx;
		/* #endif */
		/* #ifndef H5 */
		margin-top: 90rpx;
		/* #endif */
	}

	.page-box {
		padding-bottom: 100px;
	}

	.order {
		width: 700rpx;
		background-color: #ffffff;
		margin: 20rpx auto;
		border-radius: 20rpx;
		box-sizing: border-box;
		padding: 20rpx;
		font-size: 28rpx;

		.top {
			display: flex;
			justify-content: space-between;

			.left {
				display: flex;
				align-items: center;

				.store {
					margin: 0 10rpx;
					font-size: 32rpx;
					font-weight: bold;
				}
			}

			.right {
				color: $u-type-warning-dark;
			}
		}

		.item {
			display: flex;

			.left {
				margin-right: 20rpx;

				image {
					width: 280rpx;
					height: 240rpx;
					border-radius: 10rpx;
				}
			}

			.content {
				.title {
					font-size: 16px;
					line-height: 50rpx;
					font-weight: bold;
					overflow: hidden;
					white-space: nowrap;
					text-overflow: ellipsis;
				}

				.buttom {
					display: flex;
					justify-content: space-between;
					border-bottom: 1px solid #E6E6E6;

					.price {
						font-size: 18px;
						font-weight: bold;
						color: #FF8211;
					}

					.number {
						font-size: 14px;
						color: #999999;
						line-height: 24px;
					}
				}

				.head1 {
					position: relative;
					left: -5px;
				}

				.head2 {
					position: relative;
					left: -10px;
				}

				.number {
					line-height: 50rpx;
				}
			}

		}

		.total {
			margin-top: 20rpx;
			text-align: right;
			font-size: 24rpx;

			.total-price {
				font-size: 32rpx;
			}
		}

		.bottom {
			display: flex;
			margin-top: 40rpx;
			padding: 0 10rpx;
			justify-content: space-between;
			align-items: center;

			.btn {
				line-height: 52rpx;
				width: 160rpx;
				border-radius: 26rpx;
				border: 2rpx solid $u-border-color;
				font-size: 26rpx;
				text-align: center;
				color: $u-type-info-dark;
			}

			.evaluate {
				color: $u-type-warning-dark;
				border-color: $u-type-warning-dark;
			}
		}
	}

	.centre {
		text-align: center;
		margin: 200rpx auto;
		font-size: 32rpx;

		image {
			width: 360rpx;
			height: 360rpx;
			// margin-bottom: 20rpx;
			margin: 0 auto 20rpx;
			border: 1px dotted #000000;
		}

		.tips {
			font-size: 40rpx;
			color: #5074FF;
			margin-top: 20rpx;
		}

		.btn {
			margin: 80rpx auto;
			width: 600rpx;
			border-radius: 32rpx;
			line-height: 90rpx;
			color: #ffffff;
			font-size: 34rpx;
			background: #5074FF;
		}
	}

	.wrap {
		display: flex;
		flex-direction: column;
		height: calc(100vh - var(--window-top));
		width: 100%;

	}

	.swiper-box {
		flex: 1;
	}

	.swiper-item {
		height: 100%;
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
</style>