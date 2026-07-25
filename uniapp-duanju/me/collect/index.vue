<template>
	<view>
		<view class="margin bg-white padding-sm radius" v-if="collectList.length" v-for="(item,index) in collectList" :key='index' @click="goDet(item.courseId)" >
			<view class="flex">
				<u-image width="280rpx" height="200rpx" :src="item.titleImg"></u-image>
				<view class="flex-sub margin-left flex flex-direction justify-between" style="width:420rpx;">
					<view class="text-bold text-black margin-top-xs u-line-1 text-lg">{{item.title}}
					</view>
					<view class="text-gray text-26 margin-top">最近{{item.payNum}}人在学 </view>
					<view class="flex align-center justify-between">
						<view class=" " style="color: #FF8211;">¥ <text class=" text-bold" style="font-size: 42rpx;">{{item.price}}</text></view>
						<view class="btn">马上学习</view>
					</view>
				</view>
			</view>
		</view>
		<!-- 空数据 -->
		<empty v-if="!collectList.length"></empty>
	</view>
</template>

<script>
	import empty from '@/components/empty.vue'
	export default {
		components: {
			empty
		},
		data() {
			return {
				limit: 10,
				page: 1,
				collectList: [],//收藏资源
			}
		},
		onLoad() {
			this.getCollectList()
		},
		methods: {
			// 获取收藏信息
			getCollectList() {
				uni.showLoading({
					title: '加载中'
				})
				let data = {
					userId: this.userId,
					page: this.page,
					limit: this.limit,
				}
				this.$u.api.collectList(data).then(res => {
					if(res.code == 0) {
						if( this.page == 1) {
							this.collectList = res.data.records
							uni.stopPullDownRefresh();
							return
						}
						this.collectList = [...this.collectList, ...res.data.records]
					} else {
						uni.showToast({
							title: res.msg,
							duration: 1000,
							icon: 'none'
						});
					}
					uni.hideLoading()
					uni.stopPullDownRefresh();
				})
			},
			goDet(e) {
				uni.navigateTo({
					url: '/pages/index/course/courseDet?id=' + e
				})
			}
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.getCollectList();
		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.getCollectList();
		},
	}
</script>

<style>
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
