<template>
	<view class="">
		<!-- <u-sticky :enable="enable"> -->
			<view class="search-box">
				<u-search style="width: 100%;" placeholder="搜索更多资源" v-model="keyword" :show-action="false"
					:animation="true" @search="getCourseList()"></u-search>
			</view>
		<!-- </u-sticky> -->
		<view class="swiper flex align-center justify-center">
			<view class="swiper-box">
				<swiper :indicator-dots="true" class="swiper " :autoplay="true" interval="5000" duration="500"
					:circular="true" style="width: 100%;height: 350rpx;">
					<swiper-item v-for="(item,index) in swiperList" :key='index' @tap="goPage(item.url)">
						<image :src="item.imageUrl" mode="scaleToFill"
							style="width: 100%;height: 100%;border-radius: 24rpx;"></image>
					</swiper-item>
				</swiper>
			</view>
		</view>
		<view class="padding-lr">
			<view class="" v-if="courseList.length">
				<!-- <view class="vidoList flex align-center justify-between flex-wrap">
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
				</view> -->
				<videoList @success="posterSuccess" :list="courseList" />
			</view>
			<empty title="暂无视频" :isShow='false' v-else></empty>
			<u-loadmore v-if="courseList.length > 0" :status="status" />
		</view>

	</view>
</template>

<script>
	import videoList from '../../../components/videoList/videoList.vue'
	import empty from '@/components/empty.vue'
	export default {
		components: {
			empty,
			videoList
		},
		data() {
			return {
				enable: true,
				status: 'loadmore',
				swiperList: [], //轮播图列表
				courseList: [], //资源列表
				page: 1,
				limit: 10,
				id: '',

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
				sort: '',
				count: 0,
				keyword: '',
				isPrice: '', //是否免费 2:免费
			}
		},
		onShow() {
			this.enable = true
			this.getCourseList()
		},
		onHide() {
			this.enable = false
		},
		onLoad(option) {
			uni.setNavigationBarTitle({
				title: option.title
			})
			this.getBannerList()
			if (option.sort) {
				this.sort = option.sort
			}
			if (option.isPrice) {
				this.isPrice = option.isPrice
			}

		},
		methods: {
			//点击回调
			posterSuccess(item) {
				let userId = uni.getStorageSync('userId')
				if (userId) {
					// #ifndef MP-WEIXIN
					uni.navigateTo({
						url: '/me/detail/detail?id=' + item.courseId + '&courseDetailsId=' + item.courseDetailsId
					})
					// #endif
					// #ifdef MP-WEIXIN
					uni.navigateTo({
						url: '/me/detail/detailMPWechat?id=' + item.courseId + '&courseDetailsId=' + item.courseDetailsId
					})
					// #endif
				} else {
					uni.navigateTo({
						url: "/pages/login/login"
					})
				}
			},
			//最新热播
			getCourseList() {
				let data = {
					limit: this.limit,
					page: this.page,
					sort: this.sort ? this.sort : '',
					title: this.keyword,
				}
				if (this.isPrice) {
					data.isPrice = this.isPrice
				}
				this.$u.api.courseList(data).then(res => {
					if (res.code == 0) {
						this.pages = res.data.totalPage
						if (this.page < this.pages) {
							this.status = 'loadmore'
						} else {
							this.status = 'nomore'
						}
						// res.data.list.forEach(ret => {
						// 	ret.courseLabel = ret.courseLabel ? ret.courseLabel.split(',') : []
						// })
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

		},
		onReachBottom: function() {
			if (this.page < this.pages) {
				this.page += 1
				this.status = 'loading'
				this.getCourseList()

			} else {
				this.status = 'nomore'
			}

		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.getCourseList()
		},
	}
</script>

<style lang="scss" scoped>
	.search-box {
		width: 100%;
		padding: 10rpx 2.5%;
		display: flex;
		justify-content: space-between;
		background-color: #fff;
		position: fixed;
		top: 0;
		z-index: 100;
		// margin-top: -100rpx;
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

	.swiper {
		width: 100%;
		height: auto;
		margin-bottom: 10rpx;
		margin-top: 10rpx;

		.swiper-box {
			width: 686rpx;
			height: 100%;
			
			margin-top: 74rpx;
		}
	}

	.active {
		color: #5074FF;
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