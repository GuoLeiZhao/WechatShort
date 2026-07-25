<!-- 我的积分 -->
<template>
	<view class="">
		<view class="margin radius" style="background: url(@/me/static/integral/bg.png) 100% no-repeat;position: relative;">
			<image src="@/me/static/integral/bg.png" style="width: 100%;height: 220rpx;"></image>
			<view class="padding" style="position: absolute;top: 0;width: 640rpx;">
				<view class="text-19">当前积分</view>
				<view class="flex justify-between margin-top">
					<view class="text-bold" style="font-size: 34px;">{{integralNum}}</view>
					<view class="" style="position: relative;overflow: hidden;width: 90px;">
						<!-- <view style="position: absolute;bottom:0;">
							<view class="flex">
								<u-image src="@/me/static/integral/integrate.png" width="43rpx" height="37rpx" style="width: 43rpx;height: 37rpx;"></u-image>
								<view class="margin-left-sm">积分明细</view>
							</view>
						</view> -->
					</view>
				</view>
			</view>
		</view>
		<u-gap height="30" bg-color="#F7F7F7"></u-gap>
		<view class="margin">
			<view class="flex justify-between u-border-bottom padding-tb" v-for="(item,index) in integralList" :key="index" >
				<view class="u-font-16 text-bold">{{item.content}}</view>
				<view class="flex justify-between" style="width: 120rpx;border: 2rpx solid #FFA800;background: rgba(255, 247, 226, 0.5);border-radius: 36rpx;height: 40rpx;">
					<u-image src="@/me/static/integral/jinbi.png" width="36rpx" height="36rpx"></u-image>
					<view class="margin-right-sm" style="line-height: 36rpx;">+2</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				page: 1,
				limit: 10,
				integralList: [],
				integralNum: 0
			}
		},
		onLoad() {
			this.getIntegral()
			this.getIntegralDet()
		},
		methods: {
			getIntegral() {
				this.$u.api.integral().then(res => {
					if (res.code == 0) {
						this.integralNum = res.data.integralNum
					}else {
						uni.showToast({
							title: res.msg,
							duration: 1000,
							icon: 'none'
						});
					}
				})
			},
			getIntegralDet() {
				let data = {
					page: this.page,
					limit: this.limit
				}
				this.$u.api.integralDet(data).then(res => {
					if (res.code == 0) {
						this.integralList = res.data.records
					}else {
						uni.showToast({
							title: res.msg,
							duration: 1000,
							icon: 'none'
						});
					}
				})
			}
		}
		
	}
</script>

<style>
	page {
		background-color: #FFFFFF;
	}
</style>
