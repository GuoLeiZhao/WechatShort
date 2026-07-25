<template>
	<view class="box">
		<view class="pic">
			<image class="img" :src="item.pic" :alt="item.name" />
		</view>
		<view class="real-title flex align-center justify-between">
			<view class="align-end">
				<span class="title-l">积分</span>
				<span class="title-l-n">{{item.price}}</span>
			</view>
			
			<view v-if="item.price > jifen" class="title-middle">
				再得{{ item.price - jifen}}积分可兑换
			</view>
			<view class="title-r">
				已有积分：{{jifen}}
			</view>
		</view>
		<view class="title">
			<span class="sub-title">{{item.name}} </span>
			<view class="price">
				<span class="num">{{item.price}}</span>
				<span class="unit">积分</span>
				<span class="buy-num">已兑换200+</span>
			</view>
		</view>
		<view class="detail">
			<span>商品详情</span>
			<view class="desc">
				<rich-text :nodes="item.desc"></rich-text>
			</view>
		</view>
		<view class="btn-box">
			<view class="btn float" @click="buy">
				<span>立即兑换</span>
			</view>
		</view>
		
		
		<u-popup v-model="show"
			mode='bottom' 
			border-radius="32"
			height="960"
		>
			<view class="detail-box">
				<view class="item">
					<view class="image">
						<image class="img" :src="item.pic" :alt="item.name" />
					</view>
					<view class="title-price">
						<span>{{item.name}} </span>
						<view class="price">
							<span>{{item.price}}</span><span>积分</span>
						</view>
					</view>
				</view>
				<span class="tips">请填写收货地址</span>
				<view class="deliver">
					<view class="name-phone">
						<view class="name form-item">
							<span>姓名</span>
							<u-input placeholder="请填写姓名" v-model="name"/>
						</view>
						<view class="phone form-item">
							<span>手机号</span>
							<u-input placeholder="请填写手机" type="number" v-model="phone"/>
						</view>
					</view>
					
					<view class="province form-item">
						<span>省市区</span>
						<u-input @click="province_show = true" :select-open="province_show" type='select' placeholder="请选择省市区" v-model="province"/>
						<u-select v-model="province_show" mode="mutil-column-auto" :list="list" @confirm="confirmProvince"></u-select>
					</view>
					
					<view class="address form-item">
						<span>详细地址</span>
						<u-input placeholder="请填写详细地址" v-model="address"/>
					</view>
				</view>
				<view class="btn" @click="submit">
					<span>确认兑换</span>
				</view>
			</view>
		</u-popup>
		<u-popup v-model="success_show" 
			mode='center' 
			border-radius="32"
			height="480"
			width="590"
			class="success-show"
		>
			<view class="success-item">
				<image src="https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/coupon/pic_16.png" alt="" />
				<span class="success-tips">恭喜兑换成功</span>
				<view class="know" @click="know">
					<span>我知道了</span> 
				</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				itemId: 0,
				item: {},
				province_show: false,
				show: false,
				success_show: false,
				name: '',
				phone: '',
				province: '',
				address: '',
				list: [],
				jifen: 0,
			}
		},
		onLoad(options) {
			this.itemId = options.id
			this.loadData()
			this.list = require('@/static/json/district.json')
		},
		onShow() {
			this.$Request.sysLog('浏览【物品详情页】物品 id: ' + this.itemId)
			this.getCoupon()
		},
		methods: {
			/**
			 * 处理富文本里的图片宽度自适应
			 * 1.去掉img标签里的style、width、height属性
			 * 2.img标签添加style属性：max-width:100%;height:auto
			 * 3.修改所有style里的width属性为max-width:100%
			 * 4.去掉<br/>标签
			 * @param html
			 * @returns {void|string|*}
			 */
			formatRichText(html){
				let newContent= html.replace(/<img[^>]*>/gi,function(match,capture){
					match = match.replace(/style="[^"]+"/gi, '').replace(/style='[^']+'/gi, '');
					match = match.replace(/width="[^"]+"/gi, '').replace(/width='[^']+'/gi, '');
					match = match.replace(/height="[^"]+"/gi, '').replace(/height='[^']+'/gi, '');
					return match;
				});
				newContent = newContent.replace(/style="[^"]+"/gi,function(match,capture){
					match = match.replace(/width:[^;]+;/gi, 'width:100%;').replace(/width:[^;]+;/gi, 'width:100%;');
					return match;
				});
				newContent = newContent.replace(/<br[^>]*\/>/gi, '');
				newContent = newContent.replace(/\<img/gi, '<img style="width:100%;height:auto;display:block;margin:10px 0;"');
				return newContent;
			},
			buy() {
				this.$Request.sysLog('浏览【物品详情页】物品 id: ' + this.itemId+'，点击兑换按钮')
				this.show = true
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
			confirmProvince(res){
				this.province = res.map(r => r.value).join('')
			},
			submit() {
				if(this.name == ''){
					uni.showToast({
						title: '请正确填写姓名！！',
						icon: 'error'
					})
					return;
				}
				let reg = /^[1][3,4,5,7,8,9][0-9]{9}$/
				if(this.phone == '' || !reg.test(this.phone)){
					uni.showToast({
						title: '请正确填写手机号！！',
						icon: 'error'
					})
					return;
				}
				
				if(this.province == ''){
					uni.showToast({
						title: '请正确选择省市区！！',
						icon: 'error'
					})
					return;
				}
				if(this.address == ''){
					uni.showToast({
						title: '请正确填写详细地址！！',
						icon: 'error'
					})
					return;
				}
				
				
				if (uni.getStorageSync('token')) {
					const order = {
						itemId: this.item.id,
						itemName: this.item.name,
						itemPic: this.item.pic,
						buyerName: this.name,
						buyerPhone: this.phone,
						buyerCity: this.province,
						buyerAddress: this.address,
						orderUserId: uni.getStorageSync('userId')
					}
					
					this.$Request.postJson('/app/shop/order', order).then(res => {
						if (res.code == 0) {
							this.$Request.sysLog('浏览【物品详情页】物品 id: ' + this.itemId+'，用户兑换成功')
							this.show = false
							this.success_show = true
							
							this.name = '';
							this.phone = '';
							this.province = '';
							this.address = '';
						} else {
							this.$Request.sysLog('浏览【物品详情页】物品 id: ' + this.itemId+'，用户兑换失败，原因：' + res.msg)
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
			know() {
				this.success_show = false
			},
			loadData(){
				this.$Request.get('/app/shop/item/selectById/' + this.itemId).then(res => {
					this.item = res.data
					// #ifdef MP-WEIXIN
					this.item.desc = this.formatRichText(this.item.desc)
					// #endif
				});
			}
		}
	}
</script>

<style scoped lang="scss">
	.box {
		width: 750rpx;
		buy-numckground-color: #F5F7FA;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}
	
	.rich-inner-cls {
		margin-top: 12rpx!important;
		max-width: 622rpx!important;
		margin-bottom: 12rpx!important;
	}
	
	
	.pic {
		width: 750rpx;
		height: 528rpx;
		background: linear-gradient(180deg, #FFFFFF 0%, #F5F7FA 100%);
		border: 2rpx solid #FFFFFF;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		 
		image {
			max-width: 460rpx;
			max-height: 460rpx;
		}
		
		.return {
			width: 32rpx;
			height: 32rpx;
			background: #FFFFFF;
		}
	}
	
	.real-title {
		width: 750rpx;
		height: 124rpx;
		padding: 38rpx 32rpx;
		background-image: url('https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/coupon/pic_17.png');
		background-repeat: no-repeat;
		background-size: cover;
		
		.title-l {
			width: 48rpx;
			height: 32rpx;
			font-size: 24rpx;
			font-family: PingFangSC, PingFang SC;;;
			font-weight: 400;
			color: #FFFFFF;
			line-height: 32rpx;
			
		}
		
		.title-l-n {
			margin-left: 8rpx;
			height: 60rpx;
			font-size: 40rpx;
			font-family: PingFangSC, PingFang SC;;;
			font-weight: 500;
			color: #FFFFFF;
			line-height: 60rpx;
		}
		
		.title-middle {
			margin-left: 16rpx;
			height: 48rpx;
			padding: 6rpx 16rpx 8rpx 24rpx;
			background-image: url('https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/coupon/pic_18.png');
			background-repeat: no-repeat;
			background-size: cover;
			
			font-size: 20rpx;
			font-family: PingFangSC, PingFang SC;;;
			font-weight: 500;
			color: #FA542E;
			line-height: 34rpx;
		}
	
		.title-r {
			margin-left: 100rpx;
			padding: 2rpx;
			height: 40rpx;
			background: #FFE0BD;
			border-radius: 4rpx;
			
			font-size: 24rpx;
			font-family: PingFangSC, PingFang SC;;
			font-weight: 400;
			color: #7D5C32;
			line-height: 36rpx;
		}
		
	}

	.title {
		margin-top: 32rpx;
		padding: 32rpx 24rpx;
		width: 686rpx;
		height: 248rpx;
		background: #FFFFFF;
		border-radius: 16rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		
		.sub-title {
			display: block;
			width: 614rpx;
			// min-height: 96rpx;
			font-size: 32rpx;
			font-family: PingFangSC;
			font-weight: 500;
			color: #333333;
			line-height: 48rpx;
			
			margin: 32rpx 24rpx 0rpx 24rpx;
		}
		
		.price {
			width: 614rpx;
			margin-top: 40rpx;
			display: flex;
			line-height: 48rpx;
			margin-bottom: 32rpx;
			align-items: center;
			
			.num {
				height: 48rpx;
				font-size: 48rpx;
				font-family: BebasNeue;
				color: #FF6C0C;
			}
			
			.unit {
				height: 40rpx;
				font-size: 28rpx;
				font-family: PingFangSC;
				font-weight: 500;
				color: #333333;
				margin-left: 8rpx;
			}
			
			.buy-num {
				position: absolute;
				width: 152rpx;
				height: 40rpx;
				font-size: 28rpx;
				font-family: PingFangSC;
				font-weight: 400;
				color: #999999;
				// margin-left: 268rpx;
				right: 70rpx;
			}
		}
		
	}

	.detail {
		width: 686rpx;
		min-height: 454rpx;
		background: #FFFFFF;
		border-radius: 16rpx;
		
		margin-top: 24rpx;
		padding-top: 32rpx;
		margin-bottom: 174rpx;;
		
		span {
			width: 128rpx;
			height: 48rpx;
			font-size: 32rpx;
			font-family: PingFangSC;
			font-weight: 500;
			color: #333333;
			line-height: 48rpx;
			
			margin-left: 24rpx;
			
		}
		
		.desc {
			margin: 32rpx 24rpx 32rpx 24rpx;
			
			// 居中
			display: flex;
			flex-direction: column;
			align-items: center;
			justify-content: center;
			
			
			image {
				margin-top: 12rpx;
				max-width: 622rpx;
				margin-bottom: 12rpx;
			}
			
			// #ifdef H5
			img {
				margin-top: 12rpx;
				max-width: 622rpx;
				margin-bottom: 12rpx;
			}
			// #endif
			
			p {
				margin-top: 12rpx;
				max-width: 622rpx;
				margin-bottom: 12rpx;
			}
		}
	}

	.btn {
		width: 686rpx;
		height: 104rpx;
		background: linear-gradient(87deg, #FF6C0C 0%, #FFA51C 100%);
		border-radius: 52rpx;
		text-align: center;
		display: flex;
		align-items: center;
		justify-content: center;
		
		margin-top: 114rpx;
		margin-bottom: 32rpx;
		
		span {
			width: 128rpx;
			height: 48rpx;
			font-size: 32rpx;
			font-family: PingFangSC;
			font-weight: 500;
			color: #FFFFFF;
			line-height: 48rpx;
		}
	}

	.btn.float {
		
		margin-top: 0rpx;
		margin-bottom: 0rpx;
	}
	
	.btn-box {
		position: fixed;
		bottom: 0rpx;
		width: 750rpx;
		height: 160rpx;
		// background-color: rgba(255, 255, 255, 1);
		
		display: flex;
		align-items: center;
		justify-content: center;
	}
	
	.detail-box {
		height: 960rpx;
		background: #F5F7FA;
		// background-color: red;
		border-radius: 32rpx 32rpx 0rpx 0rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding-top: 32rpx;
		
		.item {
			display: flex;
			align-items: center;
			justify-content: center;
			width: 686rpx;
			height: 248rpx;
			background: #FFFFFF;
			border-radius: 16rpx;
			padding: 38rpx 36rpx 38rpx 38rpx;
			
			.image {
				width: 184rpx;
				height: 184rpx;
				border-radius: 16rpx;
				border: 1rpx solid #EFEFEF;
				padding: 6rpx;
				display: flex;
				align-items: center;
				justify-content: center;
			}
			
			image{
				max-width: 172rpx;
				max-height: 172rpx;
			}
			
			.title-price {
				display: flex;
				flex-direction: column;
				margin-left: 16rpx;
				
				span:nth-of-type(1) {
					width: 418rpx;
					min-height: 96rpx;
					font-size: 32rpx;
					font-family: PingFangSC;
					font-weight: 500;
					color: #333333;
					line-height: 48rpx;
					margin-bottom: 40rpx;
				}
				
				.price {
					line-height: 48rpx;
					
					span:nth-of-type(1) {
						width: 96rpx;
						height: 48rpx;
						font-size: 48rpx;
						font-family: BebasNeue;
						color: #FF6C0C;
					}
					
					
					span:nth-of-type(2) {
						width: 56rpx;
						height: 40rpx;
						font-size: 28rpx;
						font-family: PingFangSC;
						font-weight: 500;
						color: #333333;
						margin-left: 8rpx;
					}
				}
			}
		}
	
		.tips {
			width: 686rpx;
			height: 52rpx;
			font-size: 36rpx;
			font-family: PingFangSC;
			font-weight: 500;
			color: #333333;
			line-height: 52rpx;
			margin-top: 24rpx;
		}
		
		.deliver {
			width: 686rpx;
			height: 328rpx;
			background: #FFFFFF;
			border-radius: 16rpx;
			margin-top: 24rpx;
			
			display: flex;
			flex-direction: column;
			align-items: center;
			justify-content: center;
			
			.form-item {
				display: flex;
				align-items: center;
				height: 104rpx;
				width: 622rpx;
				border-bottom: 1rpx solid #EFEFEF;
				span {
					height: auto;
					font-size: 28rpx;
					font-family: PingFangSC;
					font-weight: 400;
					color: #999999;
					line-height: 40rpx;
					margin-right: 24rpx;
					border-bottom: 0;
				}
				
				u-input {
					font-size: 28rpx;
				}
			}
			
			.name-phone {
				display: flex;
				width: 622rpx;
				u-input {
					width: 200rpx;
				}
			}
			
		}
		
		
	}

	.success-show ::v-deep .u-mode-center-box {
		overflow: unset !important;
	}
	.success-show ::v-deep .uni-scroll-view {
		overflow: unset !important;
	}

	.success-item {
		background-image: url('https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/coupon/pic_14.png');
		background-repeat: no-repeat;
		background-size: cover;
		height: 100%;
		width: 100%;
		
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		position: relative;
		
		image {
			width: 480rpx;
			height: 480rpx;
			top: -240rpx;
			position: absolute;
		}
		
		.success-tips {
			width: 336rpx;
			height: 56rpx;
			font-size: 56rpx;
			font-weight: bolder;
			color: #FFFFFF;
			line-height: 56rpx;
			// -webkit-text-stroke: 1px rgba(255,255,255,0.5);
			text-stroke: 1px rgba(255,255,255,0.5);
			background: linear-gradient(90deg, #FF6D0C 0%, #FFA41C 100%);
			-webkit-background-clip: text;
			-webkit-text-fill-color: transparent;
			
			margin-bottom: 90rpx;
		}
		
		.know {
			position: absolute;
			width: 406rpx;
			height: 132rpx;
			
			background-image: url('https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/coupon/pic_15.png');
			background-repeat: no-repeat;
			background-size: cover;
			bottom: 18rpx;
			
			display: flex;
			align-items: center;
			justify-content: center;
			text-align: center;
			
			span {
				
				font-size: 32rpx;
				font-family: PingFangSC;
				font-weight: 500;
				color: #FFFFFF;
				line-height: 48rpx;
			}
		}
	}
	
	
</style>
