<template>
	<view style="text-align: left">
		<view v-if="list.length" v-for="(item, index) in list" :key="index" class="item">
			<view>
				<view style="margin-bottom: 8upx;text-align: right;">
					<text style="margin-bottom: 8upx;color: #0e80d2" v-if="item.state==1"> 提现成功</text>
					<text style="margin-bottom: 8upx;color: #0e80d2" v-if="item.state==0"> 提现中</text>
					<text style="margin-bottom: 8upx;color: #FD6416" v-if="item.state==-1"> 提现失败</text>
				</view>
				
				<view style="color: #999999;font-size: 28upx;">
					<view style="margin-bottom: 8upx"> 收款人账号：{{item.zhifubao}}</view>
					<view style="margin-bottom: 8upx"> 收款人姓名：{{item.zhifubaoName}}</view>
					<view style="margin-bottom: 8upx"> 发起时间：{{item.createAt}}</view>
					<view style="margin-bottom: 8upx" v-if="item.state==1">成功时间 {{item.outAt}}</view>
					<view style="margin-bottom: 8upx;color: #FD6416" v-if="item.state==-1">{{item.refund}}</view>
					
					<view style="margin-bottom: 8upx;text-align: right;">
						<!-- 提现金额： -->
						<text style="color: #FD6416;font-size: 32upx;font-weight: 600"> ￥{{item.money}}</text>
					</view>
				</view>
			</view>
		</view>
		
		<view class="page-box" v-if="!list.length">
			<view class="centre">
				<image src="https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/learn/none.webp" mode=""></image>
				<view class="tips">
					暂无记录
				</view>
			</view>
		</view>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				list: [],
				page: 1,
				limit: 10
			}
		},
		onLoad: function(e) {
			this.getMoney();
		},

		methods: {
			getMoney() {
				let that = this;
				let token = uni.getStorageSync('token')
				
				if (token) {
					//可以提现金额查询预估收入查询
					let data = {
						page : that.page,
						limit : that.limit
					}
					that.$u.api.selectPay(data).then(res => {
						if(that.page == 1) {
							that.list = res.data.list
							uni.stopPullDownRefresh();
							return
						}
						that.list = res.data.list;
					})
					// this.$Request.getT("/cash/selectCashOutList/" + userId).then(res => {
					// 	if (res.status === 0 && res.data) {
					// 		that.list = res.data;
					// 	}
					// 	uni.hideLoading();
					// });
				}

			},
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.getMoney();
		},
		onPullDownRefresh: function() {
			this.page = 1;
			// that.list = []
			this.getMoney();
		},
	}
</script>

<style lang='scss' scoped>
	/* @import "https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/css/index.css"; */

	page {
		background: #FFFFFF;
	}

	.item {
		background: white;
		padding: 32rpx;
		margin: 32rpx;
		font-size: 28rpx;
		box-shadow: 7px 9px 34px rgba(0, 0, 0, 0.1);
		border-radius: 16upx;
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
