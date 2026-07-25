<template>
	<view>
		<view class="jf flex align-center justify-between">
			<view class="jf-l">
				<view class="jf-l-t">
					当前积分
				</view>
				<view class="jf-l-b">
					{{num}}
				</view>
			</view>
			<view class="jf-r">
				<view class="jf-r-t">
					兑换说明
				</view>
				<view class="jf-r-b">
					{{bili}}:1兑换，最少{{bili}}积分
				</view>
			</view>
		</view>
		<view class="listTitle flex align-center">
			<u-icon name="order" color="#2e2f33" size="50"></u-icon>
			<text>
				兑换数量
			</text>
		</view>
		<view class="list">
			<view class="list-input">
				<u-input v-model="value" placeholder="请输入兑换数量" type="number" :border="true" />
			</view>
			<view class="list-btn">
				<view @click="userIntegral()" class="list-btn-s flex align-center justify-center">
					提交兑换
				</view>
			</view>
			
		</view>
		
	</view>
</template>

<script>
	import empty from '../../components/empty.vue'
	export default {
		components:{
			empty
		},
		data() {
			return {
				num:0,
				value:'',
				bili:0,
			};
		},
		onShow() {
			this.getNum()
			this.getBili()
		},
		methods:{
			//兑换积分
			userIntegral(){
				if(!this.value){
					uni.showToast({
						title:'请输入兑换的积分数量',
						icon:'none'
					})
					return
				}
				if(Number(this.value) < Number(this.bili)){
					uni.showToast({
						title:'兑换的积分数量不能小于'+this.bili+'积分',
						icon:'none'
					})
					return
				}
				let data = {
					integral:this.value
				}
				this.$Request.postT('/app/integral/creditsExchange',data).then(res=>{
					if(res.code == 0){
						uni.showToast({
							title:'积分兑换成功'
						})
						this.getNum()
					}else{
						uni.showToast({
							title:res.msg,
							icon:'none'
						})
					}
				})
			},
			//获取积分兑换比例
			getBili(){
				this.$u.get('/app/common/type/104').then(res => { // 积分兑换比例 104
					if (res.code == 0 && res.data) {
						this.bili = res.data.value
					}
				});
			},
			//获取当前积分
			getNum(){
				this.$Request.getT('/app/integral/selectByUserId').then(res=>{
					uni.stopPullDownRefresh()
					if(res.code == 0){
						this.num = res.data.integralNum
					}else{
						this.num = 0
					}
				})
			},
		}
	}
</script>

<style lang="scss">
	page{
		background-color: #ffffff;
	}
	.jf{
		width: 100%;
		height: 252rpx;
		background: linear-gradient(90deg, #90A7FF 0%, #5074FF 100%);
		padding: 0 40rpx;
		.jf-l-t{
			color: #ffffff;
			font-size: 28rpx;
		}
		.jf-l-b{
			font-size: 40rpx;
			color: #ffffff;
			font-weight: 600;
			margin-top: 30rpx;
		}
		.jf-r{
			color: #ffffff;
			font-size: 28rpx;
			.jf-r-b{
				margin-top: 20rpx;
			}
		}
	}
	.listTitle{
		width: 100%;
		height: 110rpx;
		margin-top: -40rpx;
		background-color: #ffffff;
		border-radius: 30rpx 30rpx 0 0;
		padding-left: 20rpx;
		text{
			color: #2e2f33;
			font-size: 32rpx;
			font-weight: bold;
			margin-left: 20rpx;
			padding-top: 8rpx;
			
		}
	}
	.list{
		width: 100%;
		.list-input{
			padding: 20rpx;
		}
		.list-btn{
			padding: 40rpx;
			
		}
		.list-btn-s{
			width: 100%;
			height: 88rpx;
			color: #ffffff;
			background-color: #5074FF;
			border-radius: 8rpx;
		}
	}

</style>
