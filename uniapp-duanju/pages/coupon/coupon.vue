<template>
	<view v-if="'coupon' === curr" class="box">
		<view class="bg"></view>
		<view class="content">
			<view class="tabs" :style="tabsBgColor">
				<span 
					class="active-tab" 
					style="padding-left: 16rpx;"
				>福利
					<image class="active-img" src="https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/coupon/pic_13.png" alt="" />
				</span>
				<span @click="goTab('/pages/coupon/good')" 
					class="inactive-tab" 
					style="padding-left: 16rpx;"
				>商城
					<image class="active-img" src="https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/coupon/pic_13.png" alt="" />
				</span>
			</view>
			<view class="curr-coupons">
				<text class="title">当前看点</text>
				<view class="coupons-item">
					<text v-if="userId !== ''">{{moneyNum}}</text>
					<text v-else>0</text>
					
					<!-- <button @click="$Request.sysLog('浏览【福利-福利】页面，用户点击去兑换'); goTab('/pages/coupon/good')"><span>去兑换</span></button> -->
				</view>
			</view>
			<view class="sign-in">
				<view class="flex justify-between" style="width: 622rpx;">
					<span class="title">
						每日签到
					</span>
					<span class="title-sub">
						已连续签到 {{total}} 天
					</span>
				</view>
				<view class="day">
					<view v-for="(item,index) in signDays" :key="index" class="day-item">
						<view class="jifen">
							<span>{{item.value}}</span>
							<image v-if="day != item.max && latestWeek[index] == '1'" src="https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/coupon/pic_6.png" alt="" />
							<image v-if="day != item.max && latestWeek[index] == '0'" src="https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/coupon/pic_10.png" alt="" />
							<image v-if="day != item.max && latestWeek[index] == null" src="https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/coupon/pic_7.png" alt="" />
							<image v-else-if="(!isSign && day == item.max)" src="https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/coupon/pic_10.png" alt="" />
							<image v-else-if="(isSign && day == item.max)" src="https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/coupon/pic_8.png" alt="" />
							<image v-else src="https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/coupon/pic_7.png" alt="" />
						</view>
						<span v-if="day != item.max && latestWeek[index] != null" class="status">已签</span>
						<span v-else-if="day == item.max" class="status">今天</span>
						<span v-else class="status">{{item.min}}</span>
					</view>
				</view>
				
				<view class="get" @click="getFuli"><span>点击签到</span></view>
				<image src="https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/coupon/pic_11.png" alt="" class="float-l" />
				<image src="https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/coupon/pic_12.png" alt="" class="float-r" />
			</view>
			<view class="day-task">
				<span class="title">做任务得看点</span>
				<view class="task">
					<view class="task-item">
						<image src="https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/coupon/pic_2.png" alt="" />
						<view class="task-content">
							<view class="desc">
								<span class="detail">看一集短剧</span>
								<span class="jifen">+10看点</span>
							</view>
							<view class="progress flex align-center">
								<u-line-progress
									class="line-progress"
									active-color="#717171" 
									inactive-color="#E7E7E7" 
									:percent="task[812] == true?100:0"
									:height="8"
									:show-percent="false"
								/>
								<span>
									{{task[812] == true ? 1 : 0}} / 1
								</span>
							</view>
						</view>
						
						<view v-if="task[812] == true" class="btn btn-inactive">已领取</view>
						<view v-else @click="goTab('/pages/video/video')" class="btn btn-acitve">去完成</view>
					</view>
					<view class="task-item">
						<image src="https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/coupon/pic_5.png" alt="" />
						<view class="task-content">
							<view class="desc">
								<span class="detail">收藏一部短剧</span>
								<span class="jifen">+20看点</span>
							</view>
							<view class="progress flex align-center">
								<u-line-progress
									class="line-progress"
									active-color="#717171" 
									inactive-color="#E7E7E7" 
									:percent="task[815] == true?100:0"
									:height="8"
									:show-percent="false"
								/>
								<span>
									{{task[815] == true ? 1 : 0}} / 1
								</span>
							</view>
						</view>
						<view v-if="task[815] == true" class="btn btn-inactive">已领取</view>
						<view v-else @click="goTab('/pages/video/video')" class="btn btn-acitve">去完成</view>
					</view>
					<view class="task-item">
						<image src="https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/coupon/pic_3.png" alt="" />
						<view class="task-content">
						<view class="desc">
							<span class="detail">点赞10集短剧</span>
							<span class="jifen">+30看点</span>
						</view><view class="progress flex align-center">
								<u-line-progress
									class="line-progress"
									active-color="#717171" 
									inactive-color="#E7E7E7" 
									:percent="task[824] / 10 * 100"
									:height="8"
									:show-percent="false"
								/>
								<span>
									{{task[824]}} / 10
								</span>
							</view>
						</view>
						<view v-if="task[824] == 10" class="btn btn-inactive">已领取</view>
						<view v-else @click="goTab('/pages/video/video')" class="btn btn-acitve">去完成</view>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				curr: 'coupon',
				task: {
					824: 0
				},
				total: 0,
				latestWeek: [],
				day: 0,
				isSign: false,
				tabsBgColor: {},
				tabsHeight: 100,
				middlTop: 800,
				couponCls: 'active-tab',
				shopCls: 'inactive-tab',
				userId: '',
				// 数据相关
				// 积分
				jifen: 0,
				moneyNum: 0,
				// 商品
				page: {
					currPage: 1,
					pageSize: 10
				},
				itemList: [],
				signDays: [],
			}
		},
		onLoad(){
			this.userId = uni.getStorageSync('userId') ? uni.getStorageSync('userId') : ''
			uni.createSelectorQuery().select('.tabs').boundingClientRect(data => {
				this.tabsHeight = data.height
			}).exec()
			uni.createSelectorQuery().select('.middle').boundingClientRect(data => {
				this.middlTop = data.top
			}).exec()
			this.getSignDays();
		},
		onShow(){
			this.$Request.sysLog('浏览【福利-福利】页面')
			this.itemList = []
			this.page = {
				currPage: 1,
				pageSize: 10
			}
			if(this.userId !== ''){
				this.getCoupon();
				this.getTask();
				this.getSign();
				this.getMyMoney()
			}
			
			this.getSignDays()
			if (this.$refs.goods){
				this.$refs.goods.clear()
			}
			
		},
		onPageScroll(e){
			let scrollY = parseInt(e.scrollTop);
			let navigateBarHeight = this.tabsHeight * 1;
			let alpha = (scrollY / navigateBarHeight);
			if(alpha > 1){
				alpha = 1
			}
			
			this.tabsBgColor = { 'background': 'rgba(255, 255, 255, ' + alpha + ')'}
			if(scrollY > (this.middlTop - navigateBarHeight * 2)) {
				this.couponCls = 'inactive-tab';
				this.shopCls = 'active-tab';
			}else{ 
				this.shopCls = 'inactive-tab';
				this.couponCls  = 'active-tab';
			}
		},
		methods: {
			goTab(url) {
				uni.switchTab({
					url: url
				})
			},
			goPage(url){
				uni.navigateTo({
					url: url
				})
			},
			active(type) {
				if (type == 'shop'){
					this.couponCls = 'inactive-tab';
					this.shopCls = 'active-tab';
					
					uni.pageScrollTo({
						selector: '.task-item:last-child'
					});
					
				} else {
					this.shopCls = 'inactive-tab';
					this.couponCls  = 'active-tab';
					
					uni.pageScrollTo({
						scrollTop: 0
					});
					
				}
			},
			more(){
				this.couponCls = 'inactive-tab';
				this.shopCls = 'active-tab';
				
				uni.pageScrollTo({
					selector: '.middle'
				});
			},
			getTask(){
				let userId = this.userId;
				this.$Request.getT('/app/integral/isCompleteTask/812', {userId}).then(res => {
					this.task[812] = res.data
				})
				this.$Request.getT('/app/integral/isCompleteTask/815', {userId}).then(res => {
					this.task[815] = res.data
				})
				this.$Request.getT('/app/integral/isCompleteTask/824', {userId}).then(res => {
					this.task[824] = null == res.data ? 0 : res.data
				})
			},
			getSignDays() {
				this.$u.get('/app/common/type/condition/task_sign_in').then(res => {
					if (res.code == 0 && res.data) {
						this.signDays = res.data
						console.log('signDays', this.signDays)
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				});
			},
			getSign(){
				this.sign=[]
				let userId = this.userId
				this.$Request.getT('/app/sign/selectIntegralDay/v2', {userId}).then(res => {
					if (res.code == 0) {
						// 看今天是周几，0 为 周日
						let date = new Date();
						this.day = date.getDay();
						// 今天是否签到
						this.isSign = res.data.isSign;
						// 最近一周签到
						this.latestWeek = res.data.latestWeek
						// 连续签到
						this.total = res.data.total;
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			getCoupon(){
				this.$Request.getT('/app/integral/selectByUserId').then(res => {
					if (res.code == 0) {
						this.jifen = res.data.integralNum
					} else {
						this.jifen = 0
					}
				})
			},
			/**
			 * @param {Number} num
			 * @param 保留两位小数
			 */
			formatNumber(num) {
				// 判断是否为整数
				if (Number.isInteger(num)) {
					return num.toFixed(2);
				} else {
					return num.toFixed(2).replace(/\.?0+$/, '');
				}
			},
			
			/**
			 * 获取余额
			 */
			getMyMoney() {
				this.$Request.getT('/app/moneyDetails/selectUserMoney').then(res => {
					if (res.code == 0) {
						// this.moneyNum = res.data.money
						this.$Request.getT('/app/invite/selectInviteMoney').then(ret => {
							if (ret.code == 0) {
								if (ret.data.inviteMoney && ret.data.inviteMoney.money) {
									this.moneyNum = Number(res.data.money) + Number(ret.data.inviteMoney
										.money)
								} else {
									this.moneyNum = Number(res.data.money)
								}
							}
						})
					} else {
			
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			getFuli(){
				this.$Request.sysLog('浏览【福利-福利】页面，用户进行签到')
				if(this.isSign){
					uni.showToast({
						title: '今日已签到'
					})
				}
				let userId = this.userId
				if (userId) {
					// 签到
					this.$Request.getT('/app/integral/signIn/v2', {userId}).then(res => {
						if (res.code == 0) {
							uni.showToast({
								title: '今日已签到'
							})
							this.getSign()
							this.getCoupon();
						} else {
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					})
				} else {
					uni.navigateTo({
						url: '/pages/login/login'
					})
				}
			},
		}
	}
</script>

<style scoped lang="scss">
	.box {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-items: center;
		width: 750rpx;
		background-color: #F5F7FA;
		
	}
	
	.bg {
		background-image: url('https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/coupon/pic_1.png');
		background-repeat: no-repeat;
		background-size: cover;
		height: 720rpx;
		width: 100%;
	}
	
	.content {
		// margin-top: 88rpx;
		position: absolute;
		width: 100%;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-items: center;
		
	}
	
	.tabs {
		position: fixed;
		line-height: 52rpx;
		height: 100rpx;
		width: 100%;
		display: flex;
		justify-content: center;
		align-items: baseline;
		margin-bottom: 32rpx;
		z-index: 999999;
		// padding-top: 88rpx;
		padding-top: 30rpx;
		
		box-shadow: inset 0rpx -1rpx 0rpx 0rpx #EFEFEF;
		
		.active-tab {
			width: 72rpx;
			height: 52rpx;
			font-size: 36rpx;
			font-weight: 500;
			color: #333333;
			line-height: 52rpx;
		
			.active-img {
				width: 22rpx;
				height: 20rpx;
				position: absolute;
				// right: 8rpx;
				// bottom: 25rpx;
				// left: 36rpx;
				// bottom: 44rpx;
			}
		}
		
		.inactive-tab {
			width: 56rpx;
			height: 40rpx;
			font-size: 28rpx;
			font-weight: 400;
			color: #666666;
			line-height: 40rpx;
			
			.active-img {
				display: none;
			}
		}
		
	}
	
	.curr-coupons {
		width: 100%;
		// padding: 180rpx 40rpx 8rpx 40rpx;
		padding: 100rpx 40rpx 8rpx 40rpx;
		text-align: left;
		justify-items: left;
	
		.title {
			padding-left: 8rpx;
			width: 128rpx;
			height: 44rpx;
			font-size: 32rpx;
			font-weight: 500;
			color: #666666;
			line-height: 44rpx;
		}
		.coupons-item {
			display: flex;
			align-items: center;
			justify-content: center;
			
			button {
				width: 152rpx;
				height: 56rpx;
				background: #FFFFFF;
				box-shadow: 0rpx 0rpx 16rpx 0rpx rgba(254,125,13,0.24);
				border-radius: 28rpx;
				line-height: 56rpx;
				span {
					width: 84rpx;
					height: 40rpx;
					font-size: 28rpx;
					font-weight: 500;
					color: #FF6C0C;
					line-height: 40rpx;
				}
				
			}
			
			text {
				flex: 2;
				padding-left: 8rpx;
				width: 160rpx;
				height: 96rpx;
				font-size: 80rpx;
				color: #333333;
				line-height: 96rpx;
				font-family: BebasNeue;
			}
			
			
		} 
	}

	.sign-in {
		// padding: 0 50rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		
		margin-top: 8rpx;
		width: 686rpx;
		height: 448rpx;
		background: linear-gradient(135deg, #FE9001 0%, #FE7511 100%);
		border-radius: 16rpx;
		border: 2rpx solid;
		border-image: linear-gradient(180deg, rgba(255, 255, 255, 0.65), rgba(255, 255, 255, 0)) 2 2;
		
		.title {
			display: block;
			margin-top: 32rpx;
			height: 48rpx;
			font-size: 32rpx;
			font-weight: 500;
			color: #FFFFFF;
			line-height: 48rpx;
		}
		.title-sub {
			margin-top: 32rpx;
			height: 48rpx;
			font-size: 20rpx;
			font-weight: 500;
			color: #FFFFFF;
			line-height: 48rpx;
		}
		
		.day {
			display: flex;
			
			margin-left: 7rpx;
			margin-right: 7rpx;
			margin-top: 32rpx;
			
			.day-item {
				text-align: center;
				padding-left: 9rpx;
				padding-right: 9rpx;
				
				.jifen {
					width: 78rpx;
					height: 136rpx;
					background: rgba(153,78,0,0.12);
					border-radius: 43rpx;
				
					display: flex;
					flex-direction: column;
					align-items: center;
					justify-content: center;
					text-align: center;
					
					span {
						margin-bottom: 16rpx;
						width: 46rpx;
						height: 28rpx;
						font-size: 28rpx;
						font-family: PingFangSC;
						font-weight: 500;
						color: #FFFFFF;
						line-height: 28rpx;
					}
					
					image {
						width: 52rpx;
						height: 52rpx;
						
					}
				}
				
				.status {
					margin-top: 8rpx;
					width: 48rpx;
					height: 36rpx;
					font-size: 24rpx;
					font-weight: 400;
					color: #FFFFFF;
					line-height: 36rpx;
				}
				
			}
		}
		
		.float-l {
			position: absolute;
			width: 58rpx;
			height: 62rpx;
			left: 16rpx;
			top: 238rpx;
			// top: 330rpx
		}
		
		.float-r {
			position: absolute;
			width: 60rpx;
			height: 50rpx;
			right: 8rpx;
			top: 243rpx;
			// top: 335rpx
		}
		
		.get{
			background-image: url('https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/coupon/pic_9.png');
			background-repeat: no-repeat;
			background-size: cover;
			height: 120rpx;
			width: 534rpx;
			
			text-align: center;
			font-size: 32rpx;
			font-family: PingFangSC;
			font-weight: 500;
			color: #D33500;
			line-height: 120rpx;
			
			margin-top: 16rpx;
			margin-bottom: 20rpx;
		}
	}

	.day-task {
		width: 686rpx;
		background: #FFFFFF;
		border-radius: 16rpx;
		margin-top: 24rpx;
		padding-left: 32rpx;
		
		.title {
			display: block;
			margin-top: 40rpx;
			margin-bottom: 32rpx;
			font-size: 32rpx;
			font-family: PingFangSC;
			font-weight: 500;
			color: #333333;
			line-height: 48rpx;
		}
		
		.task {
			
			position: relative;
			
			.task-item {
				display: flex;
				margin-top: 32rpx;
				padding-bottom: 32rpx;
				
				image {
					width: 80rpx;
					height: 80rpx;
				}
				.task-content {
					display: flex;
					flex-direction: column;
					.desc {
						display: flex;
						margin-left: 16rpx;
						
						.detail {
							height: 40rpx;
							font-size: 28rpx;
							font-family: PingFangSC;
							font-weight: 500;
							color: #333333;
							line-height: 40rpx;
						}
						
						.jifen {
							margin-left: 12rpx;
							
							width: 102rpx;
							height: 36rpx;
							font-size: 24rpx;
							font-family: PingFangSC;
							font-weight: 400;
							color: #FF6C0C;
							line-height: 36rpx;
						}
					}
					
					.progress {
						height: 30rpx;
						
						span {
							margin-left: 16rpx;
							font-size: 20rpx;
							color: #868585;
						}
					}
					
					.line-progress {
						margin-left: 16rpx;
						margin-top: 10rpx;
						width: 168rpx
					}
				}
								
				.btn {
					display: flex;
					align-items: center;
					justify-content: center;
					
					position: absolute;
					right: 32rpx;
					
					width: 148rpx;
					height: 56rpx;
					border-radius: 28rpx;
					
					font-size: 28rpx;
					font-family: PingFangSC;
					font-weight: 500;
					line-height: 40rpx;
				}
				
				.btn-acitve {
					background: #FF6C0C;
					color: #FFFFFF;
				}
				
				.btn-inactive {
					background: #F6F6F5;
					color: #999999;
				}
			}
			.task-item:not(:last-child) {
			   border-bottom: 1rpx solid #efefef;
			}
		}
		
		
	}

	.middle {
		display: flex;
		line-height: 52rpx;
		margin-top: 24rpx;
		
		.tuijian {
			width: 144rpx;
			height: 52rpx;
			font-size: 36rpx;
			font-family: PingFangSC;
			font-weight: 500;
			color: #333333;
			line-height: 52rpx;
			position: absolute;
			left: 32rpx;
		}
		
		.more {
			width: 112rpx;
			height: 40rpx;
			font-size: 28rpx;
			font-family: PingFangSC;
			font-weight: 400;
			color: #333333;
			line-height: 52rpx;
		
			position: absolute;
			right: 32rpx;
		}
	}
	
</style>
