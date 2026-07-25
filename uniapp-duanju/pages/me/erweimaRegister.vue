<template>
	<view style="text-align: center;height: 100%;position: absolute;width:100%;background: #FFFFFF;">
		<image style="width: 150px;height: 150px;margin-top: 40px" :src="erweima">
		</image>
		<view v-if="isWeiXin" style="font-size: 28upx;color: grey;margin-top: 20px">长按识别上方二维码</view>
		<view v-if="!isWeiXin" style="font-size: 16px;margin-top: 10px">微信扫描访问</view>
		<!-- <view v-if="isWeiXin" style="font-size: 16px;margin-top: 10px">微信关注【{{name}}】访问</view>
		<view v-if="isWeiXin" style="font-size: 28upx;color: grey;margin-top: 40px" @click="rests">无法识别？</view> -->
	</view>
</template>
<script>
	export default {
		data() {
			return {
				isWeiXin: false,
				name: '',
				content: '',
				erweima: '',
				webviewStyles: {
					progress: {
						color: '#FF3333'
					}
				}
			}
		},
		onLoad() {
			this.$Request.getT("/app/common/type/2").then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						this.erweima = res.data.value;
					}
				}
			});
		},
		onPullDownRefresh: function() {
			uni.stopPullDownRefresh(); // 停止刷新
		},
		methods: {
			rests() {
				uni.showToast({
					title: "已刷新请再次长按识别",
					mask: false,
					duration: 1500,
					icon: "none"
				});
				window.location.reload();
			}
		},

	}
</script>

<style>
</style>