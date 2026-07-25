<template>
	<view class="box flex flex-direction align-center justify-center">
		<view class="top flex justify-between">
			<view class="title-box flex flex-direction ">
				<span class="title">当前积分</span>
				<span class="num-box flex">{{num}}</span>
			</view>
			<view @click="goDuihuan" class="duihuan flex align-center justify-center">
				积分兑换
			</view>
		</view>
		<view class="detail flex flex-direction ">
			<view class="tabs flex align-end">
				<span class="charge tab flex align-center justify-center" :class="{ active: chargeActive }" @click="activeCharge" >积分明细</span>
				<span class="pay tab flex align-center justify-center" :class="{ active: payActive }" @click="activePay" >兑换记录</span>
			</view>
			<view v-if="chargeActive" class="details charge">
				<scroll-view
					@scrolltolower="scrolltolower" 
					:refresher-enabled="refresherTriggered" 
					@scrolltoupper="scrolltoupper" 
					scroll-y="true" 
					style="width: 100%;height: 100%;padding-bottom: 50rpx;"
				>
					<view class="charge-item flex align-center justify-between"
						 v-for="(item,index) in list" :key="index"
					>
						<view class="charge-item-l flex flex-direction">
							<span class="charge-item-l-title">
								{{item.content}}
							</span>
							<span class="charge-item-l-time">
								{{item.createTime}}
							</span>
						</view>
						<span class="charge-item-r" v-if="item.type == 1">
							+{{item.num}}
						</span>
						<span class="charge-item-r" v-else>
							-{{item.num}}
						</span>
					</view>
					<view class="btm">
						<u-loadmore v-if="list.length>0" :status="status" />
						<empty :isShow="true" title="暂无明细" v-if="list.length == 0" />
					</view>
				</scroll-view>
			</view>
			
			<view v-else class="details pay">
				
				<scroll-view 
					@scrolltolower="scrolltolowerPay" 
					:refresher-enabled="payRefresherTriggered" 
					@scrolltoupper="scrolltoupperPay" 
					scroll-y="true" 
					style="width: 100%;height: 100%;padding-bottom: 50rpx;"
				>
					<view class="pay-item flex justify-start"
						 v-for="(item,index) in payList" :key="index"
						 @click="goDetail(item)"
					>
						<view class="img-box">
							<u-image width="136rpx" height="136rpx" mode="aspectFit" :src="item.goodImg" />
						</view>
						<view class="pay-item-r flex flex-direction">
							<span class="pay-item-r-title">
								{{item.goodName}}
							</span>
							<view class="pay-item-r-bottom flex align-center justify-between">
								<span class="pay-item-r-time">
									{{item.createTime}}
								</span>
								<view>
									<span class="charge-item-r-money">
										{{item.num}}
									</span>
									<span class="charge-item-r-money-unit">
										积分
									</span>
								</view>
							</view>
						</view>
					</view>
					<view class="btm">
						<u-loadmore v-if="payList.length>0" :status="payStatus" />
						<empty :isShow="true" title="暂无明细" v-if="payList.length == 0" />
					</view>
				</scroll-view>
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
				boxStyle:{
					margin:0,
					padding:'200rpx 0 0 0',
				},
				num:0,
				
				list:[],
				status: 'loadmore',
				page:1,
				limit:10,
				pages:1,
				refresherTriggered:true,
				
				payList:[],
				payStatus: 'loadmore',
				payPage:1,
				payLimit:10,
				payPages:1,
				payRefresherTriggered:true,
				
				chargeActive: false,
				payActive: true,
			};
		},
		onShow() {
			this.page = 1
			this.payPage = 1
			this.getNum()
			this.getList()
			this.getPayList()
		},
		onPullDownRefresh() {
			this.getNum()
		},
		methods:{
			goDuihuan() {
				uni.switchTab({
					url: '/pages/coupon/coupon'
				})
			},
			activeCharge(){
				this.chargeActive = true;
				this.payActive = false
				this.list = []
				this.page = 1
				this.getList()
			},
			activePay(){
				this.chargeActive = false;
				this.payActive = true
				this.payList = []
				this.payPage = 1
				this.getPayList()
			},
			goNav(url){
				uni.navigateTo({
					url:url
				})
			},
			goDetail(item){
				console.log(item);
				// 非商品购买没有详情页
				if(3 == item.classify){
					console.log('go')
					uni.navigateTo({
						url: '/pages/coupon/success?item=' + JSON.stringify(item)
					})
				}
				
			},
			//上拉刷新
			scrolltoupper(){
				this.page = 1
				this.refresherTriggered = true
				this.getList()
			},
			//加载更多
			scrolltolower(){
				console.log('scrolltolower')
				if(this.page < this.pages){
					this.status = 'loading'
					this.page += 1
					this.getList()
				}else{
					this.status = 'nomore'
				}
				
			},
			//获取积分明细
			getList(){
				let data = {
					page:this.page,
					limit:this.limit
				}
				this.$Request.getT('/app/integral/details',data).then(res=>{
					setTimeout(()=>{
						this.refresherTriggered = false
					},1500)
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
			//上拉刷新
			scrolltoupperPay(){
				
				this.payPage = 1
				this.payRefresherTriggered = true
				this.getPayList()
			},
			//加载更多
			scrolltolowerPay(){
				console.log('scrolltolowerPay')
				if(this.payPage < this.payPages){
					this.payStatus = 'loading'
					this.payPage += 1
					this.getPayList()
				}else{
					this.payStatus = 'nomore'
				}
				
			},
			getPayList(){
				let data = {
					page:this.payPage,
					limit:this.payLimit
				}
				this.$Request.getT('/app/integral/payDetails',data).then(res=>{
					setTimeout(()=>{
						this.payRefresherTriggered = false
					},1500)
					if(res.code == 0){
						this.payPages = res.data.pages
						if(this.payPage < this.payPages){
							this.payStatus = 'loadmore'
						}else{
							this.payStatus = 'nomore'
						}
						if(this.payPage == 1){
							this.payList = res.data.records
						}else{
							this.payList = [...this.payList,...res.data.records]
						}
					}
				})
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

<style lang="scss" scoped>
	page{
		background-color: #ffffff;
		margin-top: -136rpx;
	}
	
	.box {
		width: 750rpx;
	}
	
	.top {
		width: 750rpx;
		height: 720rpx;
		
		padding: 0 48rpx 0 40rpx;
		
		background-image: url('https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/jifen/bg.png');
		background-repeat: no-repeat;
		background-size: cover;
		
		.title-box {
			margin-top: 212rpx;
			width: 670rpx;
			.title {
				margin-left: 8rpx;
				height: 44rpx;
				font-size: 32rpx;
				font-family: PingFangSC, PingFang SC;
				font-weight: 500;
				color: #FFFFFF;
				line-height: 44rpx;
			}
			.num-box {
				margin-top: 10rpx;
				height: 96rpx;
				line-height: 96rpx;
				
				height: 96rpx;
				font-size: 96rpx;
				font-family: BebasNeue;
				color: #FFFFFF;
				line-height: 96rpx;
			}
			
		}
		
		
		.duihuan {
			margin-top: 314rpx;
			
			width: 172rpx;
			height: 56rpx;
			background: #FFFFFF;
			box-shadow: 0rpx 0rpx 16rpx 0rpx rgba(254,125,13,0.24);
			border-radius: 28rpx;
			
			font-size: 28rpx;
			font-family: PingFangSC, PingFang SC;
			font-weight: 500;
			color: #FF6C0C;
			line-height: 40rpx;
		}
	}
	
	.detail {
		z-index: 100;
		margin-top: -284rpx;
		width: 100%;
		background: #F3F5F8;
		border-radius: 32rpx 32rpx 0rpx 0rpx;
		
		.tabs {
			
			height: 96rpx;
			border-radius: 32rpx 32rpx 0rpx 0rpx;
			background-image: url('https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/jifen/title-bg.png');
			background-repeat: no-repeat;
			background-size: cover;
			
			.tab {
				width: 338rpx;
				height: 96rpx;
				font-size: 32rpx;
				font-family: PingFangSC, PingFang SC;
				font-weight: 400;
				color: #666666;
				line-height: 48rpx;
			}
			
			.active {
				background-repeat: no-repeat;
				background-size: cover;
				
				width: 412rpx;
				height: 112rpx;
				font-size: 32rpx;
				font-family: PingFangSC, PingFang SC;
				font-weight: 500;
				color: #333333;
				line-height: 48rpx;
			}
			
			.charge.active {
				background-image: url('https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/jifen/left.png');
			}
			
			.pay.active {
				background-image: url('https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/jifen/right.png');
			}
		}
	
		.details {
			height: 1100rpx;
			margin-top: 8rpx;
		}
		
		.details.charge {
			
			.charge-item {
				margin-left: 24rpx;
				margin-top: 16rpx;
				padding: 24rpx 32rpx;
				width: 702rpx;
				height: 148rpx;
				background: #FFFFFF;
				border-radius: 16rpx;
				
				.charge-item-l-title {
					height: 48rpx;
					font-size: 32rpx;
					font-family: PingFangSC, PingFang SC;
					font-weight: 500;
					color: #333333;
					line-height: 48rpx;
				}
				
				.charge-item-l-time {
					margin-top: 16rpx;
					height: 36rpx;
					font-size: 24rpx;
					font-family: PingFangSC, PingFang SC;
					font-weight: 400;
					color: #666666;
					line-height: 36rpx;
				}
				
				.charge-item-r {
					height: 36rpx;
					font-size: 36rpx;
					font-family: PingFangSC, PingFang SC;
					font-weight: 500;
					color: #000000;
					line-height: 36rpx;
				}
			}
		}
		.details.pay {
			.pay-item {
				margin-top: 16rpx;
				margin-left: 24rpx;
				padding: 24rpx 32rpx;
				width: 702rpx;
				height: 196rpx;
				background: #FFFFFF;
				border-radius: 16rpx;
				
				.img-box{
					width: 148rpx;
					height: 148rpx;
					border-radius: 16rpx;
					border: 1rpx solid #EFEFEF;
				}
				
				.pay-item-r {
					margin-left: 20rpx;
					
					.pay-item-r-title {
						width: 474rpx;
						height: 96rpx;
						font-size: 32rpx;
						font-family: PingFangSC, PingFang SC;
						font-weight: 500;
						color: #333333;
						line-height: 48rpx;
					}
					.pay-item-r-bottom {
						margin-top: 16rpx;
						
						.pay-item-r-time {
							width: 242rpx;
							height: 36rpx;
							font-size: 24rpx;
							font-family: PingFangSC, PingFang SC;
							font-weight: 400;
							color: #666666;
							line-height: 36rpx;
						}
						
						.charge-item-r-money {
							height: 36rpx;
							font-size: 24rpx;
							font-family: BebasNeue;
							color: #FF6C0C;
							line-height: 36rpx;
						}
						
						.charge-item-r-money-unit {
							margin-left: 8rpx;
							width: 48rpx;
							height: 36rpx;
							font-size: 24rpx;
							font-family: PingFangSC, PingFang SC;
							font-weight: 400;
							color: #666666;
							line-height: 36rpx;
						}
					}
				}
				
				
				
			}
			
		}
		
		.details .btm {
			margin-top: 16rpx;
		}
	}
	

</style>
