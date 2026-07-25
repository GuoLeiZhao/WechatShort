/**
* 瀑布流组件
*/
<template>
	<view>
		<view class="list flex align-center justify-center">
			<view class="list-box flex justify-between flex-wrap">
				<!-- 左边数据 -->
				<view class="list-box-ite">
					<view @click="clickItem(item)" class="list-box-ite-item" v-for="(item,index) in arrListLeft"
						:key="index">
						<view class="list-box-ite-item-img">
							<u-lazy-load border-radius="24rpx 24rpx 0 0" :image="item.titleImg"></u-lazy-load>
							<!-- <view class="list-box-ite-item-img-t" v-if="item.courseDetailsName">
								{{item.courseDetailsName}}
							</view> -->
						</view>
						<view class="list-box-ite-item-txt">
							<view class="list-box-ite-item-txt-t">
								{{item.title}}
							</view>
							<view class="list-box-ite-item-txt-l" v-if="item.courseLabel">
								{{item.courseLabel}}
							</view>
						</view>
					</view>
				</view>
				<!-- 右边数据 -->
				<view class="list-box-ite">
					<view @click="clickItem(item)" class="list-box-ite-item" v-for="(item,index) in arrListRight"
						:key="index">
						<view class="list-box-ite-item-img">
							<u-lazy-load border-radius="24rpx 24rpx 0 0" :image="item.titleImg"></u-lazy-load>
							<view class="list-box-ite-item-img-t" v-if="item.courseDetailsName">
								{{item.courseDetailsName}}
							</view>
						</view>
						<view class="list-box-ite-item-txt">
							<view class="list-box-ite-item-txt-t">
								{{item.title}}
							</view>
							<view class="list-box-ite-item-txt-l" v-if="item.courseLabel">
								{{item.courseLabel}}
							</view>
						</view>
					</view>
				</view>

			</view>
		</view>
	</view>
</template>

<script>
	export default {
		name: "videoList",
		props: {
			list: {
				type: Array,
				default: () => []
			},
		},
		data() {
			return {
				arrListLeft: [], //左边数据
				arrListRight: [], //右边数据
			};
		},
		watch: {
			list: {
				handler() {
					console.log(1111111)
					this.spliceArrayListr()
				},
				deep: true,
			}
		},
		created() {
			this.spliceArrayListr()
		},
		methods: {
			/**
			 * @param {Object} item
			 * 点击事件
			 */
			clickItem(item) {
				this.$emit('success', item);
			},
			/**
			 * @param {Array} list 原数组
			 * @param {number} type 1:是从奇数开始，2是从偶数开始
			 * 拆分数据
			 */
			spliceArrayListr() {
				this.arrListRight = []
				this.arrListLeft = []
				this.list.map((item, index) => {
					if (index % 2 === 0) {
						this.arrListLeft.push(item)
					} else {
						this.arrListRight.push(item)
					}
				})
			},
		}
	}
</script>

<style lang="scss">
	.list {
		width: 100%;
		height: auto;

		.list-box {
			width: 686rpx;
			height: 100%;

		}

		.list-box-ite {
			width: calc((100% - 20rpx) / 2);
			height: auto;
		}

		.list-box-ite-item {
			width: 100%;
			height: auto;
			margin-bottom: 20rpx;

			.list-box-ite-item-img {
				width: 100%;
				border-radius: 24rpx 24rpx 0 0;
				min-height: 300rpx;
				position: relative;

				image {
					width: 100%;
					min-height: 300rpx;
					border-radius: 24rpx 24rpx 0 0;
				}

				.list-box-ite-item-img-t {
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

			.list-box-ite-item-txt {
				padding: 10rpx 20rpx;
				background-color: #ffffff;
				border-radius: 0 0 24rpx 24rpx;
			}

			.list-box-ite-item-txt-t {
				color: #333333;
				font-size: 30rpx;
				font-weight: bold;
			}

			.list-box-ite-item-txt-l {
				color: #999999;
				font-size: 22rpx;
				margin-top: 10rpx;
			}
		}







		.list-box-item {
			width: calc((100% - 20rpx) / 2);
			// height: 100%;
			min-height: 320rpx;
			border-radius: 24rpx;
			background-color: #ffffff;
			margin-bottom: 20rpx;
		}

		.list-box-item-img {
			width: 100%;
			height: 200rpx;
			border-radius: 24rpx 24rpx 0 0;

			image {
				width: 100%;
				height: 200rpx;
				border-radius: 24rpx 24rpx 0 0;
			}

		}

		.list-box-item-txt {
			width: 100%;
			// height: 120rpx;
			padding: 20rpx 0;
			border-radius: 0 0 24rpx 24rpx;
		}

		.list-box-item-txt-t {
			width: 100%;
			padding: 0 20rpx;
			color: #333333;
			font-size: 30rpx;
			font-weight: bold;
			overflow: hidden;
			white-space: nowrap;
			text-overflow: ellipsis;
		}

		.list-box-item-txt-l {
			color: #999999;
			font-size: 22rpx;
			margin-top: 10rpx;
			padding: 0 20rpx;
		}
	}
</style>