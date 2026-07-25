<template>
	<view style="font-size: 14px;line-height: 26px;padding: 32upx;" class="home1">
		<view style="font-size: 28upx;" v-html="content"></view>
	</view>
</template>

<script>
	export default {
	    data() {
	        return {
				content:''
			}
	    },
		onLoad() {
			this.getGuize();
		},
	    methods: {
			getGuize(){
				this.$u.get('app/common/type/155').then(res => {
					this.content = res.data.value
				});
			}
		}
	}
</script>

<style>
	page {
		/* background: #1c1b20; */
	}
</style>
