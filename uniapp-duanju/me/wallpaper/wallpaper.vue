<template>
	<view>
		<view class="videoImg flex align-center justify-center">
			<view class="videoImg-box flex justify-between align-center flex-wrap">
				<view class="videoImg-box-item" v-for="(item,index) in wallpaperList" :key="index">
					<image @click="priveImg(item.imageUrl)" :src="item.imageUrl" mode="aspectFill"></image>
				</view>
			</view>
		</view>
		<empty v-if="wallpaperList.length == 0" />
		<u-loadmore v-if="wallpaperList.length >= 4" :status="status" />
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
				wallpaperList: [], //壁纸列表
				page: 1,
				limit: 10,
				pages: 1,
				status: 'loadmore',
			};
		},
		onLoad() {
			this.getWallpaperList() //壁纸
		},
		onReachBottom() {
			if (this.page < this.pages) {
				this.page += 1
				this.status = 'loading'
				this.getWallpaperList()
			} else {
				this.status = 'nomore'
			}
		},
		onPullDownRefresh() {
			this.page = 1
			this.getWallpaperList()
		},
		methods: {
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
			//获取壁纸列表
			getWallpaperList() {
				let data = {
					classify: 10,
					page: this.page,
					limit: this.limit,
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
		}
	}
</script>

<style lang="scss">
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
</style>