<template>
	<view style="width: 100%;">
		<web-view :src="url" style="width: 100%;"></web-view>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				url: null, //要打开的外部链接
				viewerUrl: '/hybrid/html/web/viewer.html',
				webviewStyles: {
					width: '750px',
					height: '100%',
				},

			}
		},
		onReady() {
			uni.setNavigationBarTitle({
				title:'预览'
			})
			// #ifdef APP-PLUS
			console.log('App plus2')
			var currentWebview = this.$mp.page.$getAppWebview() //获取当前页面的webview对象
			setTimeout(function() {
				wv = currentWebview.children()[0]
				wv.setStyle({
					scalable: true
				})
			}, 1000); //如果是页面初始化调用时，需要延时一下
			// #endif
		},
		onLoad: function(option) {
			this.url = option.url
			// this.url="https://your-cdn-domain.com/file/uploadPath/2023/03/08/b6c0dd4821cc9ce70c897adca3ad22c2.pdf"
			// h5，使用h5访问的时候记得跨域
			// #ifdef H5
			// this.url="https://your-cdn-domain.com/file/uploadPath/2023/03/08/b6c0dd4821cc9ce70c897adca3ad22c2.pdf"
			// this.url = `${this.viewerUrl}?file=${encodeURIComponent(option.url)}`;
			// this.url = `${this.viewerUrl}?file=${'https://your-cdn-domain.com/file/uploadPath/2023/03/08/b6c0dd4821cc9ce70c897adca3ad22c2.pdf'}`;
			// #endif

			// 在安卓和ios手机上
			// 判断是手机系统：安卓，使用pdf.js
			// #ifdef APP-PLUS
			// if(plus.os.name === 'Android') {
			// 	this.url = `${this.viewerUrl}?file=${encodeURIComponent(option.url)}`;
			// }
			// // ios，直接访问pdf所在路径
			// else {
			// 	this.url = encodeURIComponent(option.url);
			// }
			// #endif

		}
	}
</script>
<style lang="scss">
	.webview {
		width: 100%;
		height: 100%;
	}
</style>