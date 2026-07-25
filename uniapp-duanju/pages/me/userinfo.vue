<template>
	<view>
		<view class="usermain">
			<view class="usermain-item u-border-bottom">
				<view>头像</view>
				<view>
					<!-- #ifdef MP-WEIXIN -->
					<button open-type="chooseAvatar" @chooseavatar="onChooseAvatar">
						<image style="width: 111rpx;height: 111rpx;border-radius: 50%"
							:src="headImg?headImg:'https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/logo.png'"></image>
					</button>
					<!-- #endif -->
					<!-- #ifndef MP-WEIXIN -->
					<image :src="headImg?headImg:'https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/logo.png'" mode="" @click="uploadImg"
						style="width: 111rpx;height: 111rpx;border-radius: 50%;"></image>
					<!-- #endif -->
				</view>
			</view>
			<view class="usermain-item item-padding u-border-bottom">
				<view>用户名</view>
				<view>
					<view class="cu-form-group">
						<input type="nickname" v-model="userName" placeholder="请输入用户名" />
					</view>
				</view>
			</view>
			<!-- <view class="usermain-item item-padding">
				<view  >姓名</view>
				<view class="cu-form-group">
					<input    v-model="realName" placeholder="请填写您的真实姓名" />
				</view>
			</view> -->

			<!-- <view class="usermain-item item-padding u-border-bottom">
				<view>手机</view>
				<view>
					<view class="cu-form-group">
						<input disabled v-model="phone" placeholder="无" />
					</view>
				</view>
			</view> -->
			<!-- <view class="usermain-item item-padding" @click="goMyAddress">

				<view  >地址管理</view>
				<view>
					<view class="cu-form-group" >
						<image src="../static/right.png" style="width: 12rpx; height: 19rpx;margin-left: 10rpx;" mode=""></image>
					</view>
				</view>
			</view> -->
		</view>
		<view class="footer-btn">
			<view class="usermain-btn" @click="messagebtn()">保存</view>
		</view>
	</view>
</template>

<script>
	import configdata from '../../common/config.js';
	export default {
		data() {
			return {
				phone: '',
				headImg: 'https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/logo.png',
				userName: '',
				nickName: '',
				userId: '',
				realName: '',
				weChatId: "",
				password: '',
				platform: '',
				createTime: '',
				money: '',
				jiFen: '',
				status: '',
				zhiFuBao: '',
				zhiFuBaoName: ''
			};
		},
		onLoad(e) {
			this.getUserInfo()
		},
		methods: {
			goMyAddress() {
				uni.navigateTo({
					url: '../jifen/myaddress'
				});
			},
			//微信填写能力获取头像
			onChooseAvatar(e) {
				console.log(e.detail.avatarUrl)
				let that = this;
				let url = ''
				let token = uni.getStorageSync('token');
				uni.showLoading({
					title: '上传中...'
				});
				uni.uploadFile({
					url: configdata.APIHOST + '/alioss/upload',
					// url: 'https://your-cdn-domain.com/sqx_fast/alioss/upload', //仅为示例，非真实的接口地址
					filePath: e.detail.avatarUrl,
					// filePath: res.tempFilePaths[0],
					header: {
						token: token
					},
					name: 'file',
					success: uploadFileRes => {
						url = JSON.parse(uploadFileRes.data);

						that.headImg = url.data
						uni.hideLoading();
					}
				});

			},
			uploadImg() {
				let token = uni.getStorageSync('token')

				if (!token) {
					this.goLoginInfo();
					return;
				}
				let that = this;
				var url = null;
				uni.showActionSheet({
					// itemList按钮的文字接受的是数组
					itemList: ["查看头像", "从相册选择图片"],
					success(e) {
						var index = e.tapIndex
						if (index === 0) {
							// 用户点击了预览当前图片
							// 可以自己实现当前头像链接的读取
							let url = that.headImg;
							let arr = []
							arr.push(url)
							uni.previewImage({
								// 预览功能图片也必须是数组的
								urls: arr
							})
						} else if (index === 1) {
							uni.chooseImage({
								count: 1, //默认9
								sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
								sourceType: ['album'], //从相册选择
								success: function(res) {
									uni.showLoading({
										title: '上传中...'
									});
									let token = uni.getStorageSync('token');
									let userId = uni.getStorageSync('userId');
									uni.uploadFile({
										url: configdata.APIHOST + '/alioss/upload',
										// url: 'https://your-cdn-domain.com/sqx_fast/alioss/upload', //仅为示例，非真实的接口地址
										filePath: res.tempFilePaths[0],
										name: 'file',
										success: uploadFileRes => {
											url = JSON.parse(uploadFileRes.data);
											console.log(url)
											that.headImg = url.data
											uni.hideLoading();
											// that.$u.post('/app/user/updateUsers',{
											// 	headImg: url.data
											// },{
											// 	header: "application/json"
											// }).then(res => {
											// 	uni.showToast({
											// 		title: '更新成功',
											// 		icon: "none"
											// 	})
											// 	that.getUserInfo();
											// })
											// uni.request({
											//     url: 'https://your-cdn-domain.com/sqx_fast/app/user/updateUsers',
											//     data: data,
											//     method: "POST",
											//     header: {
											//         "content-type": header,
											//         "token": token
											//     },
											//     success: function (result) {
											//         console.error(result);
											//         succ.call(self, result.data)
											//     },
											//     fail: function (e) {
											//         error.call(self, e)
											//     }
											// })

											// that.$Request.postJson('/user/update',{
											// 	userId: userId,
											// 	headImg:url.data
											// }).then(res => {
											// 	uni.hideLoading();
											// 	if (res.status === 0) {
											// 		that.$queue.showToast("更新成功");
											// 		that.getUserInfo(userId);
											// 	}
											// });
										}
									});
								}
							});
						}
					}
				})
			},
			config: function(name) {
				var info = null;
				if (name) {
					var name2 = name.split("."); //字符分割
					if (name2.length > 1) {
						info = configdata[name2[0]][name2[1]] || null;
					} else {
						info = configdata[name] || null;
					}
					if (info == null) {
						let web_config = cache.get("web_config");
						if (web_config) {
							if (name2.length > 1) {
								info = web_config[name2[0]][name2[1]] || null;
							} else {
								info = web_config[name] || null;
							}
						}
					}
				}
				return info;
			},
			getUserInfo() {
				let userId = uni.getStorageSync('userId')
				this.$u.api.userinfo().then(res => {
					if (res.code == 0) {
						this.userName = res.data.userName;
						this.phone = res.data.phone;
						this.headImg = res.data.avatar;
					}
				})
				// this.$Request.getT('/user/find?id=' + userId).then(res => {
				// 	if (res.status === 0) {
				// 		this.userName = res.data.userName;
				// 		this.phone = res.data.phone;
				// 		this.headImg = res.data.headImg;
				// 		this.realName = res.data.realName;
				// 		this.nickName = res.data.nickName;
				// 		this.userId = res.data.userId;
				// 		this.password = res.data.password;
				// 		this.createTime = res.data.createTime;
				// 		this.money = res.data.money;
				// 		this.platform = res.data.platform;

				// 		this.jiFen = res.data.jiFen;
				// 		this.weChatId=res.data.weChatId;
				// 		this.status = res.data.status;
				// 		this.zhiFuBao = res.data.zhiFuBao;
				// 		this.zhiFuBaoName = res.data.zhiFuBaoName;
				// 	} else {
				// 		uni.showModal({
				// 			showCancel: false,
				// 			title: '未知错误',
				// 			content: res.msg
				// 		});
				// 		this.$queue.logout();
				// 	}
				// 	uni.hideLoading();
				// });
			},
			// 保存
			messagebtn() {
				if (!this.userName) {
					// this.$queue.showToast('用户名不能为空');
					uni.showToast({
						title: "用户名不能为空",
						icon: "none"
					})
					return
				}
				uni.showModal({
					title: '温馨提示',
					content: '确定保存信息',
					success: e => {
						if (e.confirm) {
							this.$u.post("/app/user/updateUsers", {
								userName: this.userName,
								avatar: this.headImg,
								phone: this.phone,
							}).then(res => {
								if (res.code === 0) {
									uni.showToast({
										title: '保存成功',
										icon: "none"
									})
									setTimeout(function() {
										uni.navigateBack()
									}, 1000)
								} else {
									uni.showToast({
										title: res.msg,
										icon: "none"
									})
								}
							});
						}
					}
				});
			},
			// userphone(){
			// 	uni.navigateTo({
			// 		url:'/pages/my/userphone'
			// 	})
			// }
		}
	};
</script>

<style>
	page {
		background: #FFFFFF;
	}

	button::after {
		border: none;
		background-color: none;
	}

	button {
		position: relative;
		display: block;
		margin-left: auto;
		margin-right: auto;
		padding-left: 0px;
		padding-right: 0px;
		box-sizing: border-box;
		text-decoration: none;
		line-height: 1.35;
		overflow: hidden;
		color: #666666;
		/* background-color: #fff; */
		background-color: rgba(255, 255, 255, 0) !important;
		width: 100%;
		height: 100%;
	}

	.usermain {
		background: #FFFFFF;
	}

	.usermain-item {
		display: flex;
		align-items: center;
		margin-left: 40rpx;
		padding: 30rpx 40rpx 30rpx 0;
		justify-content: space-between;
		/* border-bottom: 2rpx solid #f2f2f2; */
	}

	.usermain-item.item-padding {
		padding: 0 40rpx 0 0;
	}

	.cu-form-group {
		padding: 0;
		background: #FFFFFF;
		text-align: right;
	}

	.cu-form-group input {
		background: #FFFFFF;
		font-size: 28rpx;
		color: #333333;

	}

	.footer-btn {
		margin-top: 150rpx;
	}

	.footer-btn .usermain-btn {
		color: #FFFFFF;
		background: #5E81F9;
		text-align: center;
		width: 450rpx;
		height: 80rpx;
		font-size: 28rpx;
		line-height: 80rpx;
		margin: 0 auto;
		border-radius: 40rpx;
	}
</style>