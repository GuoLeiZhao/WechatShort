<template>
	<view class="container">
		<!-- <list-cell title="收款人姓名" type="text" placeholder="请输入支付宝收款人姓名" v-model="zhiFuBaoName"></list-cell>

		<list-cell title="支付宝账号" type="number" placeholder="请输入要绑定的支付宝手机号" v-model="zhiFuBao"></list-cell>

        
		<button :class="zhiFuBao&&zhiFuBaoName?'confirm-btn':'confirm-btn1'" @click="toLogin" :disabled="logining">绑定账户</button>
	    <view style="padding: 32upx 64upx;font-size: 24upx;color: #999999;">提示：请正确填写收款人的支付宝账户和真实的收款人姓名，否则将无法正常收款</view> -->
	
	</view>
</template>

<script>
	// import listCell from '@/me/components/com-input';
	// export default {
	// 	components: {
	// 		listCell
	// 	},
	// 	data() {
	// 		return {
	// 			zhiFuBao: '',
	// 			zhiFuBaoName: '',
	// 			logining: false
	// 		}
	// 	},
	// 	onLoad() {
	// 		let userId = uni.getStorageSync('userId');
			
	// 		if (userId) {
	// 			this.$u.api.userinfo().then(res => {
	// 				if(res.code == 0) {
	// 					if (res.data.zhiFuBao) {
	// 						this.zhiFuBao = res.data.zhiFuBao;
	// 					}
	// 					if (res.data.zhiFuBaoName) {
	// 						this.zhiFuBaoName = res.data.zhiFuBaoName;
	// 					}
	// 					// console.log(this.zhiFuBao )
	// 					// console.log(this.zhiFuBaoName )
	// 				}
	// 			})
	// 		}

	// 	},
	// 	methods: {
	// 		inputChange(e) {
	// 			const key = e.currentTarget.dataset.key;
	// 			this[key] = e.detail.value;
	// 		},
	// 		navBack() {
	// 			uni.navigateBack();
	// 		},

	// 		toLogin() {
	// 			// console.log(this.zhiFuBao )
	// 			// console.log(this.zhiFuBaoName )
	// 			// const {
	// 			// 	zhiFuBao,
	// 			// 	zhiFuBaoName
	// 			// } = this;
	// 			let zhiFuBao = this.zhiFuBao
	// 			let zhiFuBaoName = this.zhiFuBaoName
	// 			console.log( zhiFuBao)
	// 			console.log( zhiFuBaoName)
	// 			if (!zhiFuBao) {
	// 				uni.showToast({
	// 					title: '请设置收款人姓名',
	// 					icon:'none'
	// 				})
	// 			} else if (!zhiFuBaoName) {
	// 				uni.showToast({
	// 					title: '请设置收款人支付宝账号',
	// 					icon:'none'
	// 				})
	// 			} else {
	// 				if(this.zhiFuBao.length == 11) {
	// 					this.$u.post('app/user/updateUser?zhiFuBao='+zhiFuBao+'&zhiFuBaoName='+zhiFuBaoName).then(res => {
	// 						// console.log(res);
	// 						uni.setStorageSync('zhiFuBao',zhiFuBao)
	// 						uni.setStorageSync('zhiFuBaoName',zhiFuBaoName)
	// 						uni.showToast({
	// 							title:'修改成功',
	// 							icon:'none',
	// 							complete() {
	// 								setTimeout(function() {
	// 									uni.navigateBack();
	// 								},1000)
	// 							}
	// 						})
							
	// 					});
	// 				} else {
	// 					uni.showToast({
	// 						title: '请输入正确的手机号',
	// 						icon:'none'
	// 					})
	// 				}
					
	// 			}
	// 			// if (!zhifubao) {
	// 			// 	this.$queue.showToast("请设置收款人支付宝账号");
	// 			// } else if (!zhifubaoName) {
	// 			// 	this.$queue.showToast("请设置收款人姓名");
	// 			// } else {
	// 				// this.$queue.showLoading("修改中...");
	// 				// this.$Request.postT("/cash/userinfo?userId=" + userId + "&zhifubao=" + zhifubao + "&zhifubaoName=" + zhifubaoName).then(
	// 				// 	res => {
	// 				// 		if (res.status === 0) {
	// 				// 			if (res.data.zhifubao) {
	// 				// 				this.zhifubao = res.data.zhifubao;
	// 				// 			}
	// 				// 			if (res.data.zhifubaoName) {
	// 				// 				this.zhifubaoName = res.data.zhifubaoName;
	// 				// 			}
	// 				// 			this.navBack();
	// 				// 		} else {
	// 				// 			// this.$queue.showToast(res.msg)
	// 				// 		}
	// 				// 		uni.hideLoading();
	// 				// 	});
	// 			// }
	// 		},
	// 	},

	// }
</script>

<style lang='scss'>
	/* page {
		background: #FFFFFF;
	}

	.container {
		padding-top: 32upx;
		position: relative;
		width: 100%;
		height: 100%;
		overflow: hidden;
		background: #fff;
	}

	.confirm-btn1 {
		width: 300px;
		height: 42px;
		line-height: 42px;
		border-radius: 30px;
		margin-top: 70upx;
		/* background: whitesmoke; 
		background: #5074FF;
		color: #FFF;
		font-size: 32rpx;

		&:after {
			border-radius: 60px;
		}
	}



	.confirm-btn {
		width: 300px;
		height: 42px;
		line-height: 42px;
		border-radius: 30px;
		margin-top: 70upx;
		background: #5074FF;
		color: #fff;
		font-size: 32rpx;
	
		&:after {
			border-radius: 60px;
		}
	} */

</style>
