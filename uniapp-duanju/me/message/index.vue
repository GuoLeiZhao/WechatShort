<!-- 消息中心 -->
<template>
	<view>
		
		<!-- #ifndef H5 -->
		<u-navbar :title-bold="true" title="消息中心">
			<!-- <view class="slot-wrap margin-right" slot="right">全部已读</view> -->
		</u-navbar>
		<!-- #endif -->
		<view v-if="msgList.length" class="flex margin-lr padding-tb-sm u-border-bottom" v-for="(item,index) in msgList" :key='index' >
			<u-image src="@/me/static/message/xitong.png" width="82rpx" mode="widthFix"></u-image>
			<view class="flex-sub margin-left">
				<view class="flex justify-between">
					<view class="text-bold text-lg">{{item.title}}</view>
					<view class="text-gray">{{item.createAt}}</view>
				</view>
				<view class="text-gray">{{item.content}}</view>
			</view>
		</view>
		<empty v-if="!msgList.length" title="暂无消息" :isShow="false" ></empty>
		<!-- <view class="page-box" v-if="!msgList.length">
			<view class="centre">
				<image src="../static/none.png" mode=""></image>
				<view class="tips">
					暂无消息
				</view>
			</view>
		</view> -->
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
				page: 1,
				limit: 10,
				msgList: []
			}
		},
		onLoad() {
			this.getMsg()
		},
		methods: {
			getMsg() {
				uni.showLoading({
					title: '加载中'
				})
				let data = {
					page: this.page,
					limit: this.limit,
					state: 5
				}
				this.$u.api.message(data).then(res => {
					if (res.code == 0) {
						if( this.page == 1) {
							this.msgList = res.data.list
							uni.stopPullDownRefresh();
							return
						}
						this.msgList = [...this.msgList, ...res.data.list]
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
			}
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.getMsg()
		},
		onPullDownRefresh: function() {
			this.page = 1;
			// this.msgList = []
			this.getMsg()
		},
	}
</script>

<style lang="scss" scoped>
	page {
		background-color: white;
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
			// border: 1px dotted #000000;
		}
		.tips {
			font-size: 34rpx;
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
</style>
