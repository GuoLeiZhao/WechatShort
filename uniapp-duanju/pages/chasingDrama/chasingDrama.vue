<template>
	<view style="padding-bottom: 100rpx;">
		<view v-if="list.length>0" class="itemTitle flex align-center justify-center">
			<view class="itemTitle-box flex align-center justify-between">
				<view class="itemTitle-box-l">
					最近观看
				</view>
				<view class="itemTitle-box-r" @click="goNav('/me/jilu/jilu')">
					更多
				</view>
			</view>
		</view>
		<view v-if="list.length>0" class="zuijin flex align-center justify-center">
			<view class="zuijin-box flex justify-between">
				<view class="zuijin-box-item" @click="goCourse(item.courseId,item.courseDetailsId)"
					v-for="(item,index) in list" :key="index">
					<view class="zuijin-box-item-img">
						<image :src="item.titleImg" mode="aspectFill"></image>
						<view class="zuijin-box-item-img-t" v-if="item.courseDetailsName">
							{{item.courseDetailsName}}
						</view>
					</view>
					<view class="zuijin-box-item-txt flex align-center flex-wrap">
						<view class="zuijin-box-item-txt-t">
							{{item.title}}
						</view>
						<view class="zuijin-box-item-txt-l" v-if="item.courseLabel">
							{{item.courseLabel}}
						</view>
					</view>
				</view>
			</view>
		</view>
		<!-- #ifndef MP-WEIXIN -->
		<view v-if="zhuiju.length>0" class="itemTitle flex align-center justify-center">
			<view class="itemTitle-box flex align-center justify-between">
				<view class="itemTitle-box-l">
					我的追剧
				</view>
				<view class="itemTitle-box-r" @click="goNav('/me/jilu/histor')">
					更多
				</view>
			</view>
		</view>
		<view v-if="zhuiju.length>0" class="zuijin flex align-center justify-center">
			<view class="zuijin-box flex justify-between">
				<view @click="goCourse(item.courseId,item.courseDetailsId)" class="zuijin-box-item"
					v-for="(item,index) in zhuiju" :key="index">
					<view class="zuijin-box-item-img">
						<image :src="item.titleImg" mode="aspectFill"></image>
						<view class="zuijin-box-item-img-t" v-if="item.courseDetailsName">
							{{item.courseDetailsName}}
						</view>
					</view>
					<view class="zuijin-box-item-txt flex align-center flex-wrap">
						<view class="zuijin-box-item-txt-t">
							{{item.title}}
						</view>
						<view class="zuijin-box-item-txt-l" v-if="item.courseLabel">
							{{item.courseLabel}}
						</view>
					</view>
				</view>
			</view>
		</view>
		<!-- #endif -->
		<!-- #ifdef MP-WEIXIN -->
		<fav-list 
			url="/pages/index/index"
		/></fav-list>
		<!-- #endif -->
	</view>
</template>

<script>
	export default {
		data() {
			return {
				isErQd: false, //昨天是否签到
				isQd: false, //今日是否已签到
				day: 0, //签到天数
				list: [],
				zhuiju: [],
			};
		},
		onLoad() {


		},
		onShow() {
			this.$Request.sysLog('浏览【追剧】页面')
			if (uni.getStorageSync('token')) {
				this.newLook()
				this.myVideo()
			}
		},
		methods: {
			// 跳转资源详情
			goCourse(e, courseDetailsId) {
				if (uni.getStorageSync('token')) {
					// #ifndef MP-WEIXIN
					uni.navigateTo({
						url: '/me/detail/detail?id=' + e + '&courseDetailsId=' + courseDetailsId
					})
					// #endif
					// #ifdef MP-WEIXIN
					uni.navigateTo({
						url: '/me/detail/detailMPWechat?id=' + e + '&courseDetailsId=' + courseDetailsId
					})
					// #endif
				} else {
					uni.navigateTo({
						url: '/pages/login/login'
					})
				}

			},
			
			//跳转
			goNav(url) {
				if (uni.getStorageSync('token')) {
					uni.navigateTo({
						url: url
					})
				} else {
					uni.navigateTo({
						url: '/pages/login/login'
					})
				}
			},
			//我的追剧
			myVideo() {
				let data = {
					page: 1,
					limit: 2,
					classify: 1, //1收藏 2点赞 3历史记录
				}
				this.$Request.getT('/app/courseCollect/selectByUserId', data).then(res => {
					if (res.code == 0) {
						this.zhuiju = res.data.records
					}
				})
			},
			//最近观看
			newLook() {
				let data = {
					page: 1,
					limit: 2,
					classify: 3, //1收藏 2点赞 3历史记录
				}
				this.$Request.getT('/app/courseCollect/selectByUserId', data).then(res => {
					if (res.code == 0) {
						this.list = res.data.records
						this.$nextTick(() => {
							this.videPage = 1
						})
					}
				})
			},			
		}
	}
</script>

<style lang="scss">
	page {
		background-color: #F5F7FF;
	}

	.itemTitle {
		width: 100%;
		height: auto;
		// #ifndef MP-WEIXIN
		margin-top: 46rpx;
		// #endif
		// #ifdef MP-WEIXIN
		margin-top: 180rpx;
		// #endif

		.itemTitle-box {
			width: 686rpx;
			height: 100%;

		}

		.itemTitle-box-l {
			color: #333333;
			font-size: 32rpx;
			font-weight: bold;
		}

		.itemTitle-box-r {
			color: #999999;
			font-size: 28rpx;

		}
	}

	.zuijin {
		width: 100%;
		margin-top: 26rpx;

		.zuijin-box {
			width: 686rpx;
			height: 100%;

		}

		.zuijin-box-item {
			width: calc((686rpx - 20rpx) / 2);
			height: 100%;
		}

		.zuijin-box-item-img {
			width: 100%;
			height: 204rpx;
			border-radius: 24rpx 24rpx 0 0;
			position: relative;

			image {
				width: 100%;
				height: 100%;
				border-radius: 24rpx 24rpx 0 0;
			}

			.zuijin-box-item-img-t {
				position: absolute;
				bottom: 10rpx;
				right: 0;
				max-width: 80%;
				border-radius: 10rpx;
				background-color: rgba(51, 51, 51, 0.7);
				color: #FFFFFF;
				font-size: 22rpx;
				padding: 10rpx;
				overflow: hidden;
				white-space: nowrap;
				text-overflow: ellipsis;

			}
		}

		.zuijin-box-item-txt {
			width: 100%;
			height: 100rpx;
			background-color: #ffffff;
			border-radius: 0 0 24rpx 24rpx;
			align-content: center;
		}

		.zuijin-box-item-txt-t {
			width: 100%;
			padding: 0 20rpx;
			color: #333333;
			font-size: 30rpx;
			font-weight: bold;
			overflow: hidden;
			white-space: nowrap;
			text-overflow: ellipsis;

		}

		.zuijin-box-item-txt-l {
			width: 100%;
			padding: 0 20rpx;
			color: #999999;
			font-size: 22rpx;
		}
	}

	.title {
		width: 100%;
		padding-top: 30rpx;

		.title-box {
			width: 686rpx;
			background-color: #E5EAFF;
			border-radius: 24rpx;
			padding: 20rpx;
		}

		.title-box-title {
			font-size: 36rpx;
			font-weight: 700;
			color: #333;
		}

		.title-box-title-r {
			background-color: #5074FE;
			font-size: 26rpx;
			color: #ffffff;
			font-weight: 500;
			border-radius: 40rpx;
			padding: 20rpx 0;
			width: 166rpx;
		}

		.title-box-tishi {
			font-size: 24rpx;
			margin-top: 20rpx;
			color: #999999;
		}

		.title-box-day {
			width: 100%;
			margin-top: 30rpx;

			.title-box-day-item-lin {
				width: 30rpx;
				height: 4rpx;
				background: #bfbfbf;
				margin: 0 4rpx;
				margin-top: 30rpx;
			}

			.title-box-day-item-num {
				width: 60rpx;
				height: 60rpx;
				border-radius: 50%;
				// border: 1px solid #999999;
				color: #999999;

				image {
					width: 100%;
					height: 100%;
					border-radius: 50%;
				}
			}

			.title-box-day-item-day {
				width: 100%;
				text-align: center;
				font-size: 24rpx;
				margin-top: 20rpx;
			}

		}

		.title-box-btn {
			margin-top: 30rpx;
			width: 100%;
			border-radius: 44rpx;
			// background: linear-gradient(270deg, #fcae3a, #f15d25);
			background-color: #5074FF;
			color: #fff;
			text-align: center;
			padding: 22rpx 0;
			font-size: 32rpx;
		}
	}

	// .zhuiju {
	// 	width: 100%;
	// 	height: auto;
	// 	margin-top: 60rpx;

	// 	.zhuiju-box {
	// 		width: 686rpx;
	// 		height: 100%;
	// 		// box-shadow: rgba(0, 0, 0, 0.2) 0px 0px 20rpx 0px;
	// 		background-color: #ffffff;
	// 		border-radius: 20rpx;
	// 		padding: 20rpx;
	// 	}

	// 	.zhuiju-box-zj {
	// 		width: 100%;
	// 		padding: 20rpx 20rpx 0 20rpx;
	// 		// padding-top: 20rpx;

	// 		.zhuiju-box-zj-title {
	// 			color: #2e2f33;
	// 			font-size: 32rpx;
	// 			font-weight: 600;

	// 			text {
	// 				font-size: 28rpx;
	// 				color: #aeb2c1;
	// 				font-weight: 500;
	// 			}
	// 		}

	// 		.zhuiju-box-zj-content {
	// 			width: 100%;
	// 			margin-top: 20rpx;
	// 			margin-bottom: 20rpx;

	// 			.zhuiju-box-zj-content-item {
	// 				width: calc((100% - 60rpx) / 3);
	// 			}

	// 			.zhuiju-box-zj-content-item-image {
	// 				width: 100%;
	// 				height: 280rpx;
	// 				border-radius: 16rpx;
	// 				border-radius: 24rpx 24rpx 0 0;

	// 				image {
	// 					width: 100%;
	// 					height: 280rpx;
	// 					border-radius: 16rpx;
	// 					border-radius: 24rpx 24rpx 0 0;
	// 				}
	// 			}

	// 			.zhuiju-box-zj-content-item-txt {
	// 				width: 100%;
	// 				padding: 20rpx;
	// 				text-align: center;
	// 				// background-color: #ffffff;
	// 				background-color: #f2f2f2;
	// 				border-radius: 0 0 24rpx 24rpx;
	// 				overflow: hidden;
	// 				text-overflow: ellipsis; //溢出用省略号显示
	// 				white-space: nowrap; // 默认不换行；
	// 			}
	// 		}
	// 	}
	// }
</style>