<template>
	<view>
		<view class="page-box">
			<view class="centre">
				<image src="https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/learn/none.webp" mode=""></image>
				<view class="tips">
					暂未登录
				</view>
				<view class="btn" @click="goNav()">{{content}}</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			content: {
				type: String,
				default: '立即登录'
			}
		},
		methods: {
			goNav() {
				uni.navigateTo({
					url:'/pages/login/login'
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
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
