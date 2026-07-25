<template>
	<view>
		<view class="page-box">
			<view class="centre" :style="isShow==true?'margin:0;padding:200rpx 0 0 0':''">
				<image src="https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/learn/none.webp" mode=""></image>
				<view class="tips">
					<!-- 暂无资源 -->
					{{title}}
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			title: {
				type: String,
				default: '暂无数据'
			},
			isShow:{
				type:Boolean,
				default:false,
			}
		},
		methods: {
			goNav() {
				uni.switchTab({
					url: '/pages/index/index'
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

		.btn1 {
			margin: 80rpx auto;
			width: 600rpx;
			line-height: 90rpx;
			border-radius: 44rpx;
			color: #ffffff;
			font-size: 34rpx;
			background: #5074FF;
			display: flex;
			align-items: center;
			justify-content: center;
		}
	}
</style>