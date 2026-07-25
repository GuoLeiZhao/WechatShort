<template>
	<view style="padding-bottom: 50rpx;">
		<view class="padding margin radius bg-white" style="">
			<view class="text-19">当前收益</view>
			<view class="flex justify-between margin-top">
				<view class="text-bold text-black">¥<text style="font-size: 34px;">{{inviterRecord}}</text></view>
				<view class="" style="position: relative;overflow: hidden;width: 90px;">
					<view style="position: absolute;bottom:0;">
						<!-- <view class="flex"> -->
							<button  @tap="getOut" class="cu-btn round">立即提现</button>
						<!-- </view> -->
					</view>
				</view>
			</view>
		</view>
		<view class="margin padding bg-white radius">
			<view class="u-font-16 text-bold margin-bottom">邀请明细</view>
			<view class="flex justify-between" style="line-height: 30px;" v-for="(item,index) in userList" >
				<view class="flex" style="width: 200rpx;">
					<u-image :src="item.avatar" width="30px" mode="widthFix"></u-image>
					<text class="margin-left-sm">{{item.userName}}</text>
				</view>
				<view style="width: 140rpx;">邀请好友</view>
				<view style="width: 120rpx;">
					<text class="margin-left text-red">赚{{item.money}}</text>
				</view>
			</view>
			<empty v-if="userList.length == 0" />
		</view>
	</view>
</template>

<script>
	import empty from '../../components/empty.vue'
	export default {
		components:{
			empty
		},
		data() {
			return {
				page: 1,
				limit: 10,
				userList: [],
				inviterRecord: ''
			}
		},
		onLoad() {
			this.getData()
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
					}
				})
			},
			getInviter() {
				let data = {
					page: this.page,
					limit: this.limit
				}
				this.$u.api.inviter(data).then(res => {
					if (res.code == 0) {
						this.userList = res.data.list
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
		}
	}
</script>

<style>
	.cu-btn {
		background: #E5EBFF;
		color: #5074FF;
		font-weight: bold;
		font-size: 14px;
	}
</style>
