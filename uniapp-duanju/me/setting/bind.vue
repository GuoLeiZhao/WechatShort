<template>
	<view class="container">
		<view class="cu-form-group" style="margin: 30upx;border: 2upx solid whitesmoke;margin-bottom: 20px;border-radius: 30px">
			<view class="title">手机号</view>
			<input type="number" :value="mobile" placeholder="请输入新换绑的手机号" maxlength="11" data-key="mobile" @input="inputChange" />
		</view>
		 <view class="cu-form-group" style="margin: 30upx;border: 2upx solid whitesmoke;margin-bottom: 20px;border-radius: 30px">
		 	<text class="title">验证码</text>
		 	<input type="number" :value="code" placeholder="请输入验证码" maxlength="6" data-key="code" @input="inputChange"
		 	 @confirm="toLogin" />
		 	<button class="send-msg" @click="sendMsg" :disabled="sending">{{ sendTime }}</button>
		 </view>
		 
		<button class="confirm-btn" @click="toLogin" :disabled="logining">立即换绑
		</button>
	</view>
	</view>
</template>

<script>
	// import listCell from '@/components/com-input';
	export default {
		components: {
			// listCell
		},
		data() {
			return {
				mobile: '',
				code: '',
				logining: false,
				sending: false,
				sendTime: '获取验证码',
				count: 60,
			}
		},

		methods: {
			inputChange(e) {
				const key = e.currentTarget.dataset.key;
				this[key] = e.detail.value;
			},
			navBack() {
				uni.navigateBack();
			},
			countDown() {
				const {
					count
				} = this;
				if (count === 1) {
					this.count = 60;
					this.sending = false;
					this.sendTime = '获取验证码'
				} else {
					this.count = count - 1;
					this.sending = true;
					this.sendTime = count - 1 + '秒后重新获取';
					setTimeout(this.countDown.bind(this), 1000);
				}
			},
			sendMsg() {
			    const {mobile} = this;
			    if (!mobile) {
					uni.showToast({
						title: '请输入手机号',
						icon: 'none',
						duration: 1000
					})
			    } else if (mobile.length !== 11) {
					uni.showToast({
						title: '请输入正确的手机号',
						icon: 'none',
						duration: 1000
					})
			    } else {
					uni.showLoading({
						title: '正在发送验证码...'
					})
					this.$u.get('/app/Login/sendMsg/'+ mobile + '/login').then(res => {
			            if (res.code === 0) {
			            	this.sending = true;
							uni.showToast({
								title: '验证码发送成功请注意查收',
								icon: 'none',
								duration: 1000
							})
			            	this.countDown();
			            	uni.hideLoading();
			            } else {
			            	uni.hideLoading();
			            	uni.showModal({
			            		showCancel: false,
			            		title: '短信发送失败',
			            		content: res.msg ? res.msg : '请一分钟后再获取验证码'
			            	});
			            }
			        });
			    }
			},
			toLogin() {
				const {
					mobile,
					code,
				} = this;
				if (!mobile) {
					uni.showToast({
						title: '请输入手机号',
						icon: 'none',
						duration: 1000
					})
				} else if (code.length == 0) {
					uni.showToast({
						title: '请输入验证码',
						icon: 'none',
						duration: 1000
					})
				} else {
					this.logining = true;
					uni.showLoading({
						title: '更换中...'
					})
					this.$u.post('/app/user/updatePhone?phone=' + mobile + '&msg=' + code ).then(res => {
						if (res.code === 0) {
							// this.$queue.remove('invitation');
							// uni.setStorageSync('token', res.token)
							// uni.setStorageSync('userId', res.userId)
							// uni.setStorageSync('mobile', res.mobile)
			
							uni.showToast({
								title: '更换成功',
								icon: 'none'
							})
							setTimeout(function() {
								uni.navigateBack()
							}, 1000)
						} else {
							uni.hideLoading();
							uni.showModal({
								showCancel: false,
								title: '更换失败',
								content: res.msg,
							});
							this.logining = false;
						}
					});
				}
			},
		},

	}
</script>

<style lang='scss'>
	page {
		background: #fff;
	}

	.send-msg {
		border-radius: 30px;
		color: white;
		height: 30px;
		font-size: 14px;
		line-height: 30px;
		background: #5074FF;
	}

	.container {
		top: 0;
		padding-top: 32upx;
		position: relative;
		width: 100%;
		height: 100%;
		overflow: hidden;
		background: #fff;
	}

	.wrapper {
		position: relative;
		z-index: 90;
		background: #fff;
		padding-bottom: 20px;
	}

	

	.confirm-btn {
		width: 300px;
		height: 42px;
		line-height: 42px;
		border-radius: 30px;
		margin-top: 70px;
		background: #5074FF;
		color: #fff;
		font-size: 32rpx;

		&:after {
			border-radius: 60px;
		}
	}

</style>
