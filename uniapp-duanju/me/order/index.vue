<!-- 我的订单 -->
<template>
	<view>
		<view class="margin bg-white padding-sm radius" v-if="orderList.length" v-for="(item,index) in orderList" :key='index'>
			<view class="flex" @click="goNav(item.courseId)">
				<u-image v-if="item.course.titleImg" width="260rpx" height="160rpx" border-radius="10" :src="item.course.titleImg"></u-image>
				<u-image v-if="!item.course.titleImg" width="260rpx" height="160rpx" border-radius="10" src="@/me/static/order/tuceng.png"></u-image>
				<view class="flex-sub margin-left flex flex-direction justify-between">
					<view class=" text-bold text-lg text-black u-line-2" style="width: 380rpx;height: 90rpx;">{{item.course?item.course.title:'资源已下架'}}</view>
					<view class="text-right">
						<view v-if="item.payWay != 5" class="text-red">实付：¥ <text class="u-font-18 text-bold">{{item.payMoney}}</text></view>
						<view v-if="item.payWay == 5" class="text-red text-bold">会员免费获取</view>
					</view>
				</view>
			</view>
			<view class="flex justify-between margin-tb">
				<view>支付时间</view>
				<view class="text-gray">{{item.createTime}}</view>
			</view>
			<view class="flex justify-between">
				<view>订单编号</view>
				<view class="text-gray">{{item.ordersNo}}</view>
			</view>
		</view>
		<!-- <view class="page-box" v-if="!orderList.length" >
			<view class="centre">
				<image src="https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/learn/none.png" mode=""></image>
				<view class="tips">
					暂无资源
				</view>
				<view class="btn">立即购买</view>
			</view>
		</view> -->
		<empty v-if="!orderList.length" title="暂无订单" :isShow="false" ></empty>
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
				userId: '',
				orderList: []
			}
		},
		onLoad() {
			this.userId = uni.getStorageSync('userId')
			this.getOrderList()
		},
		methods: {
			getOrderList() {
				uni.showLoading({
					title: '加载中'
				})
				let data = {
					userId: this.userId,
					page: this.page,
					limit: this.limit,
				}
				this.$u.api.orderList(data).then(res => {
					if(res.code == 0) {
						if( this.page == 1) {
							this.orderList = res.data.records
							uni.stopPullDownRefresh();
							return
						}
						this.orderList = [...this.orderList,...res.data.records]
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
			goNav(e) {
				uni.navigateTo({
					url:'/pages/index/course/courseDet?id=' + e
				})
			}
			
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.getOrderList();
		},
		onPullDownRefresh: function() {
			this.page = 1;
			// this.orderList = []
			this.getOrderList();
		},
	}
</script>

<style lang="scss" scoped>
	page {
		/* background-color: #FFFFFF; */
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
			border: 1px dotted #000000;
		}
		.tips {
			font-size: 40rpx;
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
