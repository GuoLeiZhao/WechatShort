<template>
	<view>
		<view class="bg-white padding">
			<view class="box" style="">
				<view class="text-19">总资产(元)</view>
				<view class="text-bold padding-tb"><text style="font-size: 68rpx;">{{inviterRecord}}</text>元</view>
				<!-- <view>累计提现：325.05元</view> -->
			</view>
			<view class="padding-top flex align-center justify-between">
				<view>
					<view class="text-19">总资产(元)</view>
					<view class="text-bold "><text style="font-size: 48rpx;">{{inviterRecord}}</text></view>
				</view>
				<view class="btn" @click="getOut">提现</view>
			</view>
		</view>
		
		
		<view class="margin-top padding bg-white radius">
			<view class="flex align-center">
				<view class="linh"></view>
				<view class="u-font-16 text-bold " style="color: #333333;" >账单明细</view>
			</view>
			<view style="text-align: left">
				<view v-for="(item, index) in userList" :key="index" class="item">
					<view>
						<view style="color: #333333;font-weight: bold;">{{item.title}}</view>
						<view class="margin-top-sm" style="color: #999999;">{{item.createTime}}</view>
					</view>
					<view>
						<text v-if="item.type==1" style="color:  #333333;font-size:34upx;font-weight: 600"> + ¥{{item.money}}</text>
						<text v-else style="color: #333333;font-size: 34upx;font-weight: 600"> - ¥{{item.money}}</text>
					</view>
				</view>
				<view v-if="userList.length===0" style="text-align: center;margin-top: 60px">暂无记录</view>
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
				userList: [],
				inviterRecord: 0
			}
		},
		onLoad() {
			this.getData()
			this.getInviter()
		},
		onShow() {
			this.getInviter()
		},
		methods: {
			getData() {
				let data = {
					page: this.page,
					limit: this.limit
				}
				this.$u.api.queryInviter(data).then(res => {
					if (res.code == 0) {
						this.inviterRecord = res.data.inviteMoney.money
					} else {
						uni.showToast({
							title: res.msg,
							duration: 1000,
							icon: 'none'
						});
						uni.removeStorageSync('token')
						uni.removeStorageSync('userName')
						uni.removeStorageSync('openId')
						uni.removeStorageSync('avatar')
						uni.removeStorageSync('phone')
						uni.removeStorageSync('invitationCode')
						uni.removeStorageSync('userId')
						uni.removeStorageSync('zhiFuBao')
						uni.removeStorageSync('zhiFuBaoName')
						setTimeout(function() {
							uni.navigateBack()
						},1000)
					}
				})
			},
			getInviter() {
				let data = {
					page: this.page,
					limit: this.limit
				}
				this.$u.api.moneyList(data).then(res => {
					if (res.code == 0) {
						if(this.page == 1) {
							this.userList = res.data.records
							uni.stopPullDownRefresh();
							return
						}
						this.userList = [...this.userList, ...res.data.records]
					}else {
						uni.showToast({
							title: res.msg,
							duration: 1000,
							icon: 'none'
						});
					}
				})
			},
			getOut() {
				uni.navigateTo({
					url: '/me/invite/cashDetail'
				});
			},
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.getInviter()
		},
		onPullDownRefresh: function() {
			this.page = 1;
			// this.userList = []
			this.getInviter()
		},
	}
</script>

<style>
	.box{
		background: linear-gradient(90deg, #90A7FF 0%, #5074FF 100%);
		border-radius: 16rpx;
		color: #FFFFFF;
		padding: 50rpx 0;
		text-align: center;
		
	}
	.btn {
		background: #5074FF;
		color: #FFFFFF;
		font-weight: 500;
		font-size: 32rpx;
		width: 160rpx;
		height: 70rpx;
		text-align: center;
		line-height: 70rpx;
		border-radius: 12rpx;
	}
	.linh{
		width:8rpx;
		height: 32rpx;
		background: #2474FF;
		margin-right: 15rpx;
		border-radius: 4rpx;
		
	}
	.item {
		/* background: white; */
		/* padding: 32rpx; */
		margin: 32rpx 0;
		font-size: 28rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		/* box-shadow: 7px 9px 34px rgba(0, 0, 0, 0.1); */
		/* border-radius: 16upx; */
	}
</style>
