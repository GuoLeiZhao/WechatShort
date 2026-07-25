<template>
	<view style="padding-bottom: 20rpx;">
		<view v-for="(item,index) in list" :key="index" class="list flex align-center justify-center">
			<view class="list-box">
				<view class="list-box-title">
					{{item.title}}
				</view>
				<view class="list-box-content">
					{{item.content}}
				</view>
				<view class="list-box-time flex align-center justify-between">
					{{item.createTime}}
					<text>{{item.type==1?'+':'-'}}{{item.money}}</text>
				</view>
			</view>
		</view>
		<view  v-if="list.length > 3" class="loadmore">
			<u-loadmore :status="status" />
		</view>
		
		<empty v-if="list.length==0" title="暂无明细" />
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
				status: 'loadmore',
				list:[],//余额明细
				page:1,
				pages:1,
				limit:10,
			};
		},
		onLoad() {
			this.getList()
		},
		//加载更多
		onReachBottom() {
			if(this.page < this.pages){
				this.page += 1
				this.status = 'loading'
				this.getList()
			}else{
				this.status = 'nomore'
			}
		},
		onPullDownRefresh() {
			this.page = 1
			this.getList()
		},
		methods:{
			getList(){
				let data = {
					page:this.page,
					limit:this.limit
				}
				this.$Request.getT('/app/moneyDetails/queryUserMoneyDetails',data).then(res=>{
					uni.stopPullDownRefresh()
					if(res.code == 0){
						this.pages = res.data.pages
						if(this.page < this.pages){
							this.status = 'loadmore'
						}else{
							this.status = 'nomore'
						}
						if(this.page == 1){
							this.list = res.data.records
						}else{
							this.list = [...this.list,...res.data.records]
						}
					}
				})
			},
		}
	}
</script>

<style lang="scss">
	.list{
		width: 100%;
		height: auto;
		margin-top: 20rpx;
		.list-box{
			width: 686rpx;
			height:100%;
			border-radius: 24rpx;
			background-color: #ffffff;
			padding: 20rpx;
		}
		.list-box-title{
			width: 100%;
			font-size: 32rpx;
			font-weight: bold;
		}
		.list-box-content{
			width: 100%;
			margin-top: 20rpx;
		}
		.list-box-time{
			width: 100%;
			margin-top: 20rpx;
			color: #999999;
			text{
				color: #000;
				font-size: 32rpx;
				font-weight: bold;
			}
		}
	}
	.loadmore{
		margin: 20rpx 0;
	}
</style>
