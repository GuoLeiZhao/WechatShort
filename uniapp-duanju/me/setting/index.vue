<template>
	<view class="">
<!-- 		<view class="flex justify-between padding bg-white solid-bottom" @click="goNav('/me/setting/bind')">
			<view>修改手机号</view>
			<view><u-icon name="arrow-right"></u-icon></view>
		</view> -->
		<view class="flex justify-between padding bg-white solid-bottom" @click="goNav('/me/feedbackIndex/feedbackIndex')">
			<view>帮助中心</view>
			<view><u-icon name="arrow-right"></u-icon></view>
		</view>
		<view class="flex justify-between padding bg-white solid-bottom" @click="goNav('/me/feedback/index')">
			<view>意见反馈</view>
			<view><u-icon name="arrow-right"></u-icon></view>
		</view>
		<view class="flex justify-between padding bg-white solid-bottom" @click="goNav('/me/setting/xieyi')">
			<view>用户协议</view>
			<view><u-icon name="arrow-right"></u-icon></view>
		</view>
		<view class="flex justify-between padding bg-white solid-bottom" @click="goNav('/me/setting/mimi')">
			<view>隐私协议</view>
			<view><u-icon name="arrow-right"></u-icon></view>
		</view>
		<view class="flex justify-between padding bg-white solid-bottom" @click="goNav('/me/setting/logOff')">
			<view>注销账号</view>
			<view><u-icon name="arrow-right"></u-icon></view>
		</view>
		<view class="flex justify-between padding bg-white solid-bottom" @click="loginOut">
			<view>退出登录</view>
			<view><u-icon name="arrow-right"></u-icon></view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
			}
		},
		onLoad() {
			
		},
		methods: {
			goNav(e) {
				uni.navigateTo({
					url: e
				})
			},
			// 退出登录
			loginOut() {
				uni.showModal({
					title: '退出提醒',
					content: '确定要退出登录么',
					success: e => {
						if (e.confirm) {
							this.avatar = 'https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/me/head.png';
							this.userName = '';
							// this.isLogin = false
							// 清除本地数据
							uni.removeStorageSync('token')
							uni.removeStorageSync('userName')
							uni.removeStorageSync('avatar')
							uni.removeStorageSync('phone')
							uni.removeStorageSync('invitationCode')
							uni.removeStorageSync('sex')
							uni.removeStorageSync('userId')
							// uni.removeStorageSync('openId')
							uni.removeStorageSync('zhiFuBao')
							uni.removeStorageSync('zhiFuBaoName')
							uni.removeStorageSync('isVIP')
							uni.showToast({
								icon:'none',
								title:'退出登录成功'
							})
							setTimeout(function(){
								uni.navigateBack()
							},1500)
						}
					}
				});
			}
		}
		
	}
</script>

<style>
</style>
