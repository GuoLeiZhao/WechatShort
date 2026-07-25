<template>
	<view style="padding-bottom: 40rpx;">
		<view class="plot flex align-center justify-center">
			<view class="plot-box">
				<view class="plot-box-item flex justify-between" @click="goCourse(item.courseId,item.courseDetailsId)"
					v-for="(item,index) in jqList" :key="index">
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
				jqList: [], //剧情列表
				page: 1,
				limit: 10,
				pages: 1,
				status: 'loadmore',
			};
		},
		onLoad() {
			this.getJqList()
		},
		onReachBottom() {
			if (this.page < this.pages) {
				this.page += 1
				this.status = 'loading'
				this.getJqList()
			} else {
				this.status = 'nomore'
			}
		},
		onPullDownRefresh() {
			this.page = 1
			this.getJqList()
		},
		methods: {
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
		}
	}
</script>

<style lang="scss" scoped>
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
			height: 260rpx;
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
			
			overflow: hidden;
			
			display: -webkit-box;
			-webkit-box-orient: vertical;
			-webkit-line-clamp: 5;
			overflow: hidden;
			text-overflow: ellipsis;

      ::v-deep p {
				display: -webkit-box;
				-webkit-box-orient: vertical;
				-webkit-line-clamp: 5;
				overflow: hidden;
				text-overflow: ellipsis;
			}
			
		}
	}
</style>