<template>
	<view>
		<!-- #ifndef MP-WEIXIN -->
		<view class="list flex align-center justify-center" v-for="(item,index) in list" :key="index" @click="goCourse(item.courseId,item.courseDetailsId)">
			<view class="list-box flex align-center justify-between">
				<view class="list-box-l">
					<image :src="item.titleImg" mode="aspectFill"></image>
				</view>
				<view class="list-box-r flex flex-wrap">
					<view class="list-box-r-title">
						{{item.title}}
					</view>
					<view class="list-box-r-jilu">
						看到{{item.courseDetailsName}}
					</view>
					<view class="list-box-r-new flex align-center justify-between">
						<view class="list-box-r-new-l">
							{{item.isOver==1?'完结':'更新'+item.courseDetailsCount+'集'}}
						</view>
						<view class="list-box-r-new-r">
							继续观看
						</view>
					</view>
				</view>
			</view>
		</view>
		<view class="" style="margin: 20rpx;" v-if="list.length > 0">
			<u-loadmore :status="status" />
		</view>
		<empty v-if="list.length == 0" title="暂无记录" />
		<!-- #endif -->
		
		<!-- #ifdef MP-WEIXIN -->
		<fav-list
		    :showHeader="true"
		    url="/pages/index/index"
		></fav-list>
		<!-- #endif -->
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
				list:[],
				page:1,
				pages:1,
				limit:10,
			};
		},
		onShow() {
			this.getList()
		},
		onPullDownRefresh() {
			this.page = 1
			this.getList()
		},
		onReachBottom() {
			if(this.page < this.pages){
				this.page += 1
				this.status = 'loading'
				this.getList()
			}else{
				this.status = 'nomore'
			}
		},
		methods:{
			// 跳转资源详情
			goCourse(e,courseDetailsId) {
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
			},
			//获取观看记录
			getList(){
				let data = {
					page:this.page,
					limit:this.limit,
					classify:1
				}
				this.$Request.getT('/app/courseCollect/selectByUserId',data).then(res=>{
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
			height: 100%;
			background-color: #ffffff;
			border-radius: 24rpx;
		}
		.list-box-l{
			width: 150rpx;
			height: 200rpx;
			border-radius: 24rpx;
			image{
				width: 100%;
				height: 100%;
				border-radius: 24rpx;
			}
		}
		.list-box-r{
			width: calc(100% - 170rpx);
			height: 200rpx;
			padding: 20rpx;
			.list-box-r-title{
				width: 100%;
				color: #2e2f33;
				font-size: 32rpx;
				font-weight: bold;
			}
			.list-box-r-jilu{
				width: 100%;
				color: #5074FF;
				font-size: 28rpx;
			}
			.list-box-r-new{
				width: 100%;
				font-size: 28rpx;
				.list-box-r-new-l{
					color: #aeb2c1;
					
				}
				.list-box-r-new-r{
					padding:10rpx 20rpx;
					background-color: #5074FF;
					color: #ffffff;
					border-radius: 24rpx;
				}
				
				
			}
		}
	}
</style>
