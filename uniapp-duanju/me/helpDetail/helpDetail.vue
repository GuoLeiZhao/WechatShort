<template>
	<view>

		<!-- <view class="helpTitle">{{ resultData.helpWordTitle }}</view> -->
		<view class="helpCon" v-html="content"></view>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				helpWordId: '',
				resultData:[],
				content:''
			};
		},
		onLoad(options) {
			this.helpWordId = options.helpWordId
			uni.setNavigationBarTitle({
				title: options.title
			});
			if (this.helpWordId) {
				this.getContent()
			}
		},
		methods: {
			getContent() {
				let data = {
					helpWordId: this.helpWordId,
				}
				this.$u.api.helpDet(data).then(res => {
					if (res.code == 0) {
						this.resultData = res.data
						this.content = res.data.helpWordContent.replace(new RegExp("img","g"),'img style="width:100%;height:auto;"')
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
				
			}
		}
	};
</script>

<style>
	page {
		background-color: #FFFFFF;
	}

	.helpTitle {
		font-size: 40rpx;
		font-weight: bold;
		margin: 50rpx 30rpx 30rpx;
		color: #000;
	}

	.helpCon {
		font-size: 30rpx;
		margin: 30rpx 30rpx 50rpx;
		color: #000;
		line-height: 2em;
	}
</style>
