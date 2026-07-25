<template>
	<view class="box flex flex-direction align-center">
		<view class="time flex justify-between align-center">
			<span>兑换时间</span>
			<span>2024年1月8日</span>
		</view>
		<view class="detail">
			<view class="info-title flex justify-start">
				<view class="img-box flex align-center justify-center">
					<u-image width="137rpx" height="137rpx" mode="aspectFit" :src="info.goodImg" />
				</view>
				<view class="item-r flex flex-direction justify-center">
					<span class="item-r-title">
						{{info.goodName}}
					</span>
					<view class="item-r-btm">
						<span class="item-r-btm-num">{{info.num}}</span>
						<span class="item-r-btm-unit">积分</span>
					</view>
				</view>
			</view>
			<view class="info-desc flex flex-direction justify-between">
				<view class="item-row flex justify-between">
					<span class="item-row-l">收件人姓名</span>
					<span class="item-row-r">{{order.buyerName}}</span>
				</view>
				<view class="item-row flex justify-between">
					<span class="item-row-l">手机号</span>
					<span class="item-row-r">{{order.buyerPhone}}</span>
				</view>
				<view class="item-row flex justify-between">
					<span class="item-row-l">省市区</span>
					<span class="item-row-r">{{order.buyerCity}}</span>
				</view>
				<view class="item-row flex justify-between">
					<span class="item-row-l">详细地址</span>
					<span class="item-row-r">{{order.buyerAddress}}</span>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				info: {},
				order: {},
			}
		},
		onLoad(options) {
			
			this.info = JSON.parse(options.item)
			console.log("onload", this.info)
			this.getItem(this.info)
		},
		methods: {
			getItem(item){
				if (3 == item.classify){
					// 购买货品需要查询订单
					this.$Request.getT('/app/shop/order/' + item.orderId).then(res => {
						if (res.code == 0) {
							this.order = res.data
						} else {
							uni.showToast({
								title: res.msg,
								icon: 'none'
							});
						}
					})
					
				}
			}
		}
	}
</script>

<style scoped lang="scss">
	page{
		background-color: #F5F7FA;
	}
	
	.box {
		padding-top: 56rpx;
	}
	
	.time{
		padding: 0 24rpx;
		width: 702rpx;
		height: 88rpx;
		background: #FFFFFF;
		border-radius: 16rpx;
		
		font-size: 28rpx;
		font-family: PingFangSC, PingFang SC;
		font-weight: 400;
		color: #333333;
		line-height: 40rpx;
	}
	
	.detail {
		padding: 32rpx 24rpx;
		margin-top: 16rpx;
		width: 702rpx;
		height: 564rpx;
		background: #FFFFFF;
		border-radius: 16rpx;
		
		.info-title {
			.img-box{
				width: 148rpx;
				height: 148rpx;
				border-radius: 16rpx;
				border: 1rpx solid #EFEFEF;
			}
			
			.item-r {
				margin-left: 20rpx;
				
				.item-r-title {
					width: 474rpx;
					height: 96rpx;
					font-size: 32rpx;
					font-family: PingFangSC, PingFang SC;
					font-weight: 500;
					color: #333333;
					line-height: 48rpx;
				}
				
				.item-r-btm {
					margin-top: 12rpx;
					
					.item-r-btm-num {
						height: 40rpx;
						font-size: 28rpx;
						font-family: BebasNeue;
						color: #FF6C0C;
						line-height: 40rpx;
					}
					
					.item-r-btm-unit {
						margin-left: 8rpx;
						width: 48rpx;
						height: 36rpx;
						font-size: 24rpx;
						font-family: PingFangSC, PingFang SC;
						font-weight: 400;
						color: #666666;
						line-height: 36rpx;
					}
				}
			}
		}
	
		.info-desc {
			height: 304rpx;
			margin-top: 48rpx;
			
			.item-row {
				
				.item-row-l {
					height: 40rpx;
					font-size: 28rpx;
					font-family: PingFangSC, PingFang SC;
					font-weight: 400;
					color: #333333;
					line-height: 40rpx;
				}
				
				.item-row-r {
					height: 40rpx;
					font-size: 28rpx;
					font-family: PingFangSC, PingFang SC;
					font-weight: 400;
					color: #999999;
					line-height: 40rpx;
				}
			}
		}
	}

</style>
