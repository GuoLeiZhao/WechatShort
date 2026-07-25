<template>
	<view>
		<view class="list flex align-center justify-between flex-wrap">
			<view class="list-item" v-for="(item,index) in list" :key="index" @click="goCourse(item.courseId,item.courseDetailsId)">
				<view class="list-item-img">
					<image :src="item.titleImg" mode="aspectFill"></image>
				</view>
				<view class="list-item-title">
					{{item.title}}
				</view>
			</view>
			<view class="list-item" style="height: 0;"></view>
		</view>
		<view class="" style="margin: 20rpx;" v-if="list.length > 0">
			<u-loadmore :status="status" />
		</view>

		<empty v-if="list.length == 0" title="暂无记录" />
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
				list: [],
				page: 1,
				pages: 1,
				limit: 10,
			};
		},
		onShow() {
			this.getList()
		},
		onPullDownRefresh() {
			this.page = 1
			this.getList()
		},
		onReachBottom() {
			if (this.page < this.pages) {
				this.page += 1
				this.status = 'loading'
				this.getList()
			} else {
				this.status = 'nomore'
			}
		},
		methods: {
			// 跳转资源详情
			goCourse(e,courseDetailsId) {
				// #ifdef MP-WEIXIN
				uni.navigateTo({
					url: '/me/detail/detailMPWechat?id=' + e + '&courseDetailsId=' + courseDetailsId
				})
				// #endif
				
				// #ifndef MP-WEIXIN
				uni.navigateTo({
					url: '/me/detail/detail?id=' + e + '&courseDetailsId=' + courseDetailsId
				})
				// #endif
			},
			//获取观看记录
			getList() {
				let data = {
					page: this.page,
					limit: this.limit,
					classify: 2
				}
				this.$Request.getT('/app/courseCollect/selectByUserId', data).then(res => {
					uni.stopPullDownRefresh()
					if (res.code == 0) {
						this.pages = res.data.pages
						if (this.page < this.pages) {
							this.status = 'loadmore'
						} else {
							this.status = 'nomore'
						}
						if (this.page == 1) {
							this.list = res.data.records
						} else {
							this.list = [...this.list, ...res.data.records]
						}
					}
				})
			},
		}
	}
</script>

<style lang="scss">
	.list {
		width: 100%;
		height: auto;
		margin-top: 20rpx;
		padding: 0 20rpx;

		.list-item {
			width: calc((100% - 40rpx) / 3);
			height: 100%;
			margin-bottom: 20rpx;
			.list-item-img {
				width: 100%;
				height: 280rpx;

				image {
					width: 100%;
					height: 100%;
					border-radius: 24rpx 24rpx 0 0;
				}
			}

			.list-item-title {
				width: 100%;
				text-align: center;
				line-height: 60rpx;
				background-color: #ffffff;
				border-radius: 0 0 24rpx 24rpx;
				padding: 0 10rpx;
				overflow: hidden;
				white-space: nowrap;
				text-overflow: ellipsis;
				-o-text-overflow: ellipsis;
			}
		}

	}
</style>