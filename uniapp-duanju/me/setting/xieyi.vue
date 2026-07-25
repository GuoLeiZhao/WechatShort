<template>
    <view style="line-height: 26px;padding: 32upx;" class="home1">
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
				// this.$Request.getT('/agreement/find?id=1').then(res =>{
				// 	if(res.status === 0){
				// 		this.content = res.data.content;
				// 	}
				// });
				this.$u.get('app/common/type/154').then(res => {
					this.content = res.data.value
					
				});
			}
		}
    }
</script>

<style>
	page{
		/* background: #1c1b20; */
	}
</style>
