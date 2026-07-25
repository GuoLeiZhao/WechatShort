<template>
	<view class="page-box">
		<view class="order" v-if="courseList.length" v-for="(item, index) in courseList" :key="index"
			@click="goCourse(item.courseId)">
			<view class="item">
				<view class="left">
					<image :src="item.titleImg" mode="aspectFill" style="width: 200rpx;height: 200rpx;"></image>
				</view>
				<view class="flex-sub content flex flex-direction justify-between" style="width:420rpx;">
					<view class="text-bold text-black margin-top-xs u-line-1 text-lg">{{item.title}}
					</view>
					<view class="text-gray text-26 margin-top">最近{{item.payNum}}人在学 </view>
					<view class="flex align-center justify-between">
						<view class=" " style="color: #FF8211;">¥ <text class=" text-bold"
								style="font-size: 42rpx;">{{item.price}}</text></view>
						<view class="btn">马上学习</view>
					</view>
				</view>
				<!-- <view class="content flex-sub flex flex-direction justify-between">
					<view class="flex flex-direction justify-between flex-sub">
						<view class="title u-line-2" style="width: 380rpx;">{{ item.title }}</view>
						<view class="buttom margin-top">
							<view class="price">
								￥{{ priceInt(item.price) }}
							</view>
						</view>
					</view>
					<view class="flex margin-top-sm">
						<view>
							<u-avatar v-for="(ite,ind) in item.avatar" :key='ind' v-if="ind < 3" class="head" size="44" :src="ite"></u-avatar>
						</view>
						<view class="text-gray number">{{item.courseCount}}人已观看</view>
					</view>
				</view> -->
			</view>
		</view>
		<!-- 空数据 -->
		<empty v-if="!courseList.length" title="暂无资源"></empty>
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
				courseList: [],
			}
		},
		onLoad() {
			this.userId = uni.getStorageSync('userId')
			this.getCourseList()
		},
		methods: {
			// 我的资源列表
			getCourseList() {
				uni.showLoading({
					title: '加载中'
				})
				let data = {
					userId: this.userId,
					page: this.page,
					limit: this.limit,
				}
				this.$u.api.selectCourse(data).then(res => {
					if (res.code == 0) {
						res.data.list.forEach(ret => {
							if (ret.avatar) {
								ret.avatar = ret.avatar.split(',')
							}
						})
						if (this.page == 1) {
							this.courseList = res.data.list
							uni.stopPullDownRefresh();
							return
						}
						this.courseList = [...this.courseList, ...res.data.list]
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
			// 跳转资源详情
			goCourse(e) {
				console.log(e)
				let userId = uni.getStorageSync('userId')
				
				if (!userId) {
					let that = this;
					// 1.wx获取登录用户code
					uni.login({
						provider: 'weixin',
						success: function(loginRes) {
							let data = {
								code: loginRes.code
							}
							that.$u.api.wxLogin(data).then(res => {
								if (res.code == 0 && res.data) {
									uni.setStorageSync('openId', res.data.open_id)
									uni.setStorageSync('unionId', res.data.unionId)
									uni.setStorageSync('sessionkey', res.data.session_key)
								}
							})
							uni.navigateTo({
								url: '/pages/index/course/courseDet?id=' + e
							});
						}
					});
				} else {
					uni.navigateTo({
						url: '/pages/index/course/courseDet?id=' + e
					});
				}
			},

		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.getCourseList();
		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.courseList = []
			this.getCourseList();
		},
		computed: {
			// 价格小数
			priceDecimal() {
				return val => {
					if (val !== parseInt(val)) return val.slice(-2);
					else return '00';
				};
			},
			// 价格整数
			priceInt() {
				return val => {
					// if (val !== parseInt(val)) return val.split('.')[0];
					// else return val;
					return val
				};
			}
		},

	}
</script>

<style lang="scss" scoped>
	.btn {
		width: 150upx;
		height: 60upx;
		background: #5074FF;
		border-radius: 30upx;
		color: #FFFFFF;
		text-align: center;
		line-height: 60rpx;
		font-size: 26rpx;
	}

	.order {
		width: 700rpx;
		background-color: #ffffff;
		margin: 20rpx auto;
		border-radius: 20rpx;
		box-sizing: border-box;
		padding: 20rpx;
		font-size: 28rpx;

		.top {
			display: flex;
			justify-content: space-between;

			.left {
				display: flex;
				align-items: center;

				.store {
					margin: 0 10rpx;
					font-size: 32rpx;
					font-weight: bold;
				}
			}

			.right {
				color: $u-type-warning-dark;
			}
		}

		.item {
			display: flex;

			.left {
				margin-right: 20rpx;

				image {
					width: 280rpx;
					height: 240rpx;
					border-radius: 10rpx;
				}
			}

			.content {
				.title {
					font-size: 16px;
					line-height: 50rpx;
					font-weight: bold;
				}

				.buttom {
					display: flex;
					justify-content: space-between;
					border-bottom: 1px solid #E6E6E6;

					.price {
						font-size: 18px;
						font-weight: bold;
						color: #FF3838;
					}

					.number {
						font-size: 14px;
						color: #999999;
						line-height: 24px;
					}
				}

				.head1 {
					position: relative;
					left: -5px;
				}

				.head2 {
					position: relative;
					left: -10px;
				}

				.number {
					line-height: 50rpx;
				}
			}

		}

		.total {
			margin-top: 20rpx;
			text-align: right;
			font-size: 24rpx;

			.total-price {
				font-size: 32rpx;
			}
		}

		.bottom {
			display: flex;
			margin-top: 40rpx;
			padding: 0 10rpx;
			justify-content: space-between;
			align-items: center;

			.btn {
				line-height: 52rpx;
				width: 160rpx;
				border-radius: 26rpx;
				border: 2rpx solid $u-border-color;
				font-size: 26rpx;
				text-align: center;
				color: $u-type-info-dark;
			}

			.evaluate {
				color: $u-type-warning-dark;
				border-color: $u-type-warning-dark;
			}
		}
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
