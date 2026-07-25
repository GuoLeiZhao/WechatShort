<template>
	<view>
		<view class="wrap">
			<!-- <u-swiper v-if="!videoUrl" :list="courseDet.img" :height='350'></u-swiper> -->

			<swiper v-if="!videoUrl" class="screen-swiper" :circular="true" :autoplay="true" interval="3000"
				duration="500">
				<swiper-item v-for="(item,index) in courseDet.bannerImg" :key="index" @click="previewImg(item)">
					<image :src="item" style="width: 100%;height: 375rpx;" mode="aspectFill"></image>
				</swiper-item>
			</swiper>
			<video v-if="videoUrl&&courseDet.courseType==1" :src="videoUrl" id="videoplay"
				style="width: 100%;height: 375rpx;"></video>
			<view class="bg-white padding margin-bottom-xs">
				<view class="flex align-center justify-between margin-bottom-xs">
					<view style="color: #FF8211;font-weight: bold;">￥<text
							style="font-size: 50rpx;">{{courseDet.price}}</text></view>
					<view style="color:#999999;">最近{{courseDet.payNum}}人在学</view>
				</view>
				<view class="text-lg text-bold " style="color: #333333;">{{courseDet.title}}</view>
			</view>

			<scroll-view scroll-x class="bg nav bg-white u-border-bottom ">
				<view class="flex text-center">
					<view class="cu-item flex-sub  text-bold" style="position: relative;"
						:class="item.name==TabCur?'text-blue cur text-30':'text-black text-30'"
						v-for="(item,index) in tabList" :key="index" :data-id="item.name" @tap="tabSelect(item)">
						{{item.name}}
						<view :class="item.name==TabCur?'line':''" class=""></view>
					</view>

				</view>
			</scroll-view>
			<view>
				<!-- v-if="TabCur == '介绍'" -->
				<view class="" v-if="TabCur == '介绍'">
					<view class="page-box bg-white padding">
						<!-- <view class="text-bold text-lg text-black margin-bottom">资源介绍</view> -->
						<u-parse :html="courseDet.details"></u-parse>
					</view>
					<view class="page-box bg-white padding" v-if="courseDet.courseType==2 && courseDet.isMyCourse != 1">
						<view class="text-bold text-lg text-black margin-bottom">网盘链接</view>
						<view class="flex" v-if="courseDet.listsDetail">
							<textarea disabled style="width: 100%;" :value="courseDet.listsDetail[0].videoUrl"
								placeholder="" />
							<u-button shape="circle" size="mini" @click="copy(courseDet.listsDetail[0].videoUrl)"
								:custom-style="copyStyle">一键复制</u-button>
						</view>
					</view>

					<view class="navigation" style="position: fixed;bottom: 0;width: 100%;font-size: 32rpx;z-index: 999;">
						<view class="left">
							<view class="item" @click="courseCollect">
								<u-image v-if="courseDet.isCollect == 0" src="https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/index/course/collect_.png"
									width="44rpx" height="44rpx">
								</u-image>
								<u-image v-else src="https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/index/course/collect.png" width="44rpx"
									height="44rpx">
								</u-image>
								<view class="text">收藏</view>
							</view>
						</view>

						<view class="left" v-if="courseDet.courseType==3&&courseDet.isMyCourse == 2">
							<view class="item" @click="yulan">
								<u-image src="https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/index/course/zixun.png" width="44rpx" height="44rpx">
								</u-image>
								<view class="text">预览</view>
							</view>
						</view>
						<view class="right padding-right" v-if="courseDet.isMyCourse == 1 && !isVIP">
							<view class="cart btn" @click="bugCourse">购买资源 {{'¥'+courseDet.price}}</view>
							<view class="buy btn" @click="openVIP">开通会员免费</view>
						</view>
						<view class="right padding-right" v-else>
							<u-button v-if="courseDet.isMyCourse == 1" shape="circle" :custom-style="customStyle"
								@click="bugCourse" style="width: 100%;">免费获取</u-button>
							<!-- 	<u-button v-else shape="circle" disabled :custom-style="customStyle1" @click="bugCourse"
								style="width: 100%;">{{isVIP? '会员免费': '马上学习'}}</u-button> -->
							<u-button v-else shape="circle" :custom-style="customStyle1" @click="bindlearn"
								plain="false" style="width: 100%;">马上学习</u-button>
						</view>
					</view>
				</view>
				<!-- v-if="TabCur == '目录'" -->
				<view :class="TabCur == '介绍'?'margin-top-sm':''"
					v-if="TabCur != '评论'&&(courseDet.courseType==1||courseDet.courseType==4)">
					<view class="page-box bg-white padding">
						<view class="text-bold text-black padding-bottom-sm">课程资源</view>

						<view v-for="(item,index) in courseDet.listsDetail" :key='index' class="mubox"
							@click="playVideo(item,index)" v-if="courseDet.courseType==1&&courseDet.listsDetail">
							<image v-if="tabindex==index"
								src="https://your-cdn-domain.com/file/uploadPath/2023/03/09/e6cbaa09a7f4afb2e0b9b21e06751257.png"
								style="width: 28rpx;height:34rpx"></image>
							<view class="flex-sub" style="line-height: 50rpx;margin-left: 20rpx;"
								:class="tabindex==index?'act':''">
								{{item.courseDetailsName?item.courseDetailsName: ''}}
							</view>
							<u-button shape="circle" :ripple="true" size="mini" @click="playVideo(item,index)"
								:custom-style="subStyle" v-if="tabindex==index&&item.palyVideo">暂停
							</u-button>
							<u-button shape="circle" :ripple="true" size="mini" @click="playVideo(item,index)"
								:custom-style="subStyle"
								v-if="tabindex==index&&!item.palyVideo&&courseDet.courseType==1">播放
							</u-button>
						</view>
						<view v-for="(item,index) in courseDet.listsDetail" :key='index' class="mubox"
							@click="playVoice(item.videoUrl,item.palyVideo,index)"
							v-if="courseDet.courseType==4&&courseDet.listsDetail">
							<image v-if="tabindex==index"
								src="https://your-cdn-domain.com/file/uploadPath/2023/03/09/e6cbaa09a7f4afb2e0b9b21e06751257.png"
								style="width: 28rpx;height:34rpx"></image>
							<view class="flex-sub" style="line-height: 50rpx;margin-left: 20rpx;"
								:class="tabindex==index?'act':''">
								{{item.courseDetailsName?item.courseDetailsName: ''}}
							</view>

							<u-button shape="circle" :ripple="true" size="mini"
								@click="playVoice(item.videoUrl,item.palyVideo,index)" :custom-style="subStyle"
								v-if="tabindex==index&&item.palyVideo">暂停
							</u-button>
							<u-button shape="circle" :ripple="true" size="mini"
								@click="playVoice(item.videoUrl,item.palyVideo,index)" :custom-style="subStyle"
								v-if="tabindex==index&&!item.palyVideo&&courseDet.courseType==4">播放
							</u-button>
						</view>
						<!-- <video  :src="courseDet.listsDetail[0].videoUrl" id="videoplay" style="width: 100%;"></video> -->
						<view v-if="!courseDet.listsDetail" class="text-center  padding-tb">
							~ 暂无目录 ~
						</view>
					</view>

					<view class="navigation"
						style="position: fixed;bottom: 0;width: 100%;font-size: 32rpx;z-index: 99;">
						<view class="left">
							<view class="item" @click="courseCollect">
								<u-image v-if="courseDet.isCollect == 0" src="https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/index/course/collect_.png"
									width="44rpx" height="44rpx">
								</u-image>
								<u-image v-else src="https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/index/course/collect.png" width="44rpx"
									height="44rpx"></u-image>
								<view class="text">收藏</view>
							</view>
						</view>
						<view class="left" v-if="courseDet.courseType==3&&courseDet.isMyCourse == 2">
							<view class="item" @click="yulan">
								<u-image src="https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/index/course/zixun.png" width="44rpx" height="44rpx">
								</u-image>
								<view class="text">预览</view>
							</view>
						</view>
						<view class="right padding-right" v-if="courseDet.isMyCourse == 1 && !isVIP">
							<view class="cart btn" @click="bugCourse">购买资源 {{'¥'+courseDet.price}}</view>
							<view class="buy btn " @click="openVIP">开通会员免费</view>
						</view>
						<view class="right padding-right" v-else>
							<u-button v-if="courseDet.isMyCourse == 1" shape="circle" :custom-style="customStyle"
								@click="bugCourse" style="width: 100%;">免费获取</u-button>
							<!-- 	<u-button v-else shape="circle" disabled :custom-style="customStyle1" @click="bugCourse"
								style="width: 100%;">{{isVIP? '会员免费': '马上学习'}}</u-button> -->
							<u-button v-else shape="circle" :custom-style="customStyle1" @click="bindlearn"
								style="width: 100%;">马上学习</u-button>
						</view>
					</view>
				</view>
				<!-- v-if="TabCur == '评论'" -->
				<view class="margin-top-sm">
					<view class="page-box bg-white">
						<view class="text-bold text-black padding-lr padding-top-sm">评论</view>
						<view v-if="commentList.length" class="comment" v-for="(item, index) in commentList"
							:key="index">
							<view class="left">
								<image :src="item.avatar" mode="aspectFill"></image>
							</view>
							<view class="right">
								<view class="top">
									<view class="name">{{ item.userName }}</view>
									<view class="like" :class="{ highlight: item.isGood }">
										<u-icon v-if="!item.isGood" name="heart" :size="30" color="#9a9a9a"
											@click="getLike(item)"></u-icon>
										<u-icon v-if="item.isGood" name="heart-fill" :size="30" @click="getLike(item)">
										</u-icon>
										<view class="num" style="margin-left: 4rpx;">{{ item.goodsNum }}</view>
									</view>
								</view>
								<view class="bottom">
									{{ item.createTime }}
								</view>
								<view class="content">{{ item.content }}</view>
							</view>
						</view>
						<view v-if="!commentList.length" class="text-center padding-tb">
							~ 暂无评论 ~
						</view>
					</view>
					<!-- <view style="width: 100%;height: 100rpx;"></view> -->
					<view class="bg-white padding-tb-sm padding-lr flex"
						style="position: fixed;bottom: 0;width: 100%;font-size: 32rpx;z-index: 999;"
						v-if="courseDet.isMyCourse == 2&&TabCur == '评论'">
						<u-input placeholder="发条有爱评论~" v-model="value" :type="type" :border="border" class="radius"
							style="flex: 1;background: #F1F5F8;color: #CCCCCC;padding: 0 20rpx;" />
						<text class="text-bold" style="line-height: 70rpx;margin-left: 20rpx;color:#333333;"
							@tap="insertComment">发送</text>
					</view>
				</view>
			</view>
			<view class="margin-top" style="padding-bottom: 100rpx;">
				<view v-for="(item,index) in courseDet.img" :key="index" style="width: 100%;">
					<image :src="item" mode="widthFix" style="width: 100%;" @click="previewImg(item)"></image>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				TabCur: '介绍',
				tabList: [{
						name: '介绍',
						index: 0
					},
					{
						name: '目录',
						index: 1
					},
					{
						name: '评论',
						index: 2
					}
				],
				customStyle: {
					width: '100%',
					backgroundColor: '#5074FF',
					color: '#FFF'
				},
				customStyle1: {
					width: '100%',
					backgroundColor: '#5074FF',
					color: '#FFF',
					border: 'none'
				},
				subStyle: {
					width: '100rpx',
					backgroundColor: '#5074FF',
					color: '#FFF'
				},
				copyStyle: {
					width: '150rpx',
					backgroundColor: '#5074FF',
					color: '#FFF'
				},
				current: 0,
				swiperCurrent: 0,
				dx: 0,
				loadStatus: ['loadmore', 'loadmore', 'loadmore', 'loadmore'],
				commentList: [], //评论列表
				courseDet: {}, //资源详情
				value: '',
				courseId: '',
				page: 1,
				limit: 10,
				videoUrl: '',
				isLogin: false,
				userId: '',
				isVIP: false, //是否会员
				isBuy: false, //是否购买
				flag: true,
				tabindex: 0,

				isPlay: false,
				AUDIO: uni.createInnerAudioContext(),
				musicIndex: '',
				yinpinlist: '',
			};
		},
		onLoad(option) {
			this.courseId = option.id
			this.userId = uni.getStorageSync('userId')
			if (this.userId) {
				this.isLogin = true
			}
			let that = this
			//语音自然播放结束
			that.AUDIO.onEnded((res) => {
				// console.log("this.musicIndex___:" + that.musicIndex)
				let musicIndexs = that.musicIndex;
				if (musicIndexs != '') {
					that.courseDet.listsDetail[musicIndexs].palyVideo = false
				} else {
					that.courseDet.listsDetail[0].palyVideo = false
				}
			});
		},
		onUnload() {
			this.AUDIO.stop();
			let musicIndexs = this.musicIndex;
			if (musicIndexs != '') {
				this.courseDet.listsDetail[musicIndexs].palyVideo = false
			}
		},
		onHide() {
			this.AUDIO.stop();
			let musicIndexs = this.musicIndex;
			if (musicIndexs != '') {
				this.courseDet.listsDetail[musicIndexs].palyVideo = false
			}
		},
		onShow() {
			this.page = 1;
			this.commentList = []
			this.getVIP()
			this.getDataList(this.courseId);
			this.getCommentList(this.courseId)
		},
		methods: {
			bindlearn() {
				// console.log('999')
				// uni.switchTab({
				// 	url: '/pages/learn/index'
				// });
				if (this.courseDet.courseType == 3) { //文档资源
					this.yulan()
				} else if (this.courseDet.courseType == 4) { //音频资源
					this.TabCur = '目录'
					this.playVideo(this.yinpinlist, 0)
				} else if (this.courseDet.courseType == 2) { //网盘资源

					this.copy(this.courseDet.listsDetail[0].videoUrl)

				} else if (this.courseDet.courseType == 1) { //视频资源
					this.TabCur = '目录'
					this.playVideo(this.yinpinlist, 0)

				}
			},
			yulan() {
				//文件预览
				if (this.courseDet.isMyCourse == 1 && !this.isVIP) {
					this.bugCourse()
				} else {
					let url = this.courseDet.listsDetail[0].videoUrl
					// #ifdef MP-WEIXIN
					uni.showLoading({
						title: '下载中...'
					});
					uni.downloadFile({
						url: url,
						success: function(res) {
							uni.hideLoading();
							var filePath = res.tempFilePath;
							uni.openDocument({
								filePath: filePath,
								showMenu: true,
								success: function(res) {
									console.log('打开文档成功');
								}
							});
						}
					});
					// #endif
					// #ifndef MP-WEIXIN
					uni.navigateTo({
						url: '/pages/index/webView?url=' + url
					})
					// #endif
				}

			},
			// 获取是否是会员
			getVIP() {
				let data = {
					userId: uni.getStorageSync('userId')
				}
				this.$u.api.userVip(data).then(res => {
					if (res.code == 0 && res.data && res.data.isVip == 2) {
						uni.setStorageSync('isVIP', true)
						this.isVIP = true
					} else {
						uni.setStorageSync('isVIP', false)
						this.isVIP = false
					}
				})
			},
			// 资源详情
			getDataList(id) {
				let data = {
					id
				}
				this.$u.api.courseDet(data).then(res => {
					if (res.code == 0) {
						if (res.data.bannerImg) {
							res.data.bannerImg = res.data.bannerImg ? res.data.bannerImg.split(',') : []
						}
						if (res.data.courseType == 2 || res.data.courseType == 3) {
							this.tabList = [{
									name: '介绍'
								},
								{
									name: '评论'
								}
							]
							this.$nextTick(function() {
								if (res.data.img) {
									res.data.img = res.data.img ? res.data.img.split(',') : []
									this.courseDet = res.data
								}
							})

						} else if (res.data.courseType == 1 || res.data.courseType == 4) {
							if (res.data.img) {
								res.data.img = res.data.img ? res.data.img.split(',') : []
							}
							if (res.data.listsDetail) {
								let data = []
								let list = res.data.listsDetail
								list.forEach(d => {
									d.palyVideo = false
									data.push(d)
									// console.log(d)
									// this.courseDet.listsDetail.push(d)
								})
								this.courseDet.listsDetail = data
								this.yinpinlist = this.courseDet.listsDetail[0]
							}
							this.courseDet = res.data
							console.log(this.courseDet)
						}

					} else {
						uni.showToast({
							title: res.msg,
							duration: 1500,
							icon: 'none'
						});
					}

				})
			},
			// 获取评论
			getCommentList(e) {
				let data = {
					courseId: e,
					page: this.page,
					limit: this.limit
				}
				this.$u.api.courseComment(data).then(res => {
					if (res.code == 0) {
						this.commentList = [...this.commentList, ...res.data.list]
					} else {
						uni.showToast({
							title: res.msg,
							duration: 1000,
							icon: 'none'
						});
					}
				})
			},
			copy(e) {
				uni.setClipboardData({
					data: e,
					success: function() {
						console.log('success');
					}
				});
			},
			// 开通会员
			openVIP() {
				uni.navigateTo({
					url: '/pages/me/vip/index'
				})
			},
			// 购买资源
			bugCourse(e) {
				let that = this
				if (that.flag) {
					that.flag = false
					let data = {
						courseId: that.courseDet.courseId
					}
					that.$u.api.courseOrder(data).then(res => {
						if (res.code == 0 && res.data.flag == 1) {
							uni.showToast({
								title: '已获取资源',
								icon: 'success'
							})
							that.getDataList(that.courseId);
						} else if (res.code == 0 && res.data.flag == 2) {
							uni.navigateTo({
								url: '/pages/index/course/orderDet?courseId=' + that.courseId
							})
						} else {
							uni.showToast({
								title: res.msg,
								icon: 'none',
								// duration: 1500
							})
							// that.getDataList(that.courseId);
						}
					})
					setTimeout(() => {
						that.flag = true
					}, 1500)

				}

			},
			callPay: function(response) {
				if (typeof WeixinJSBridge === "undefined") {
					if (document.addEventListener) {
						document.addEventListener('WeixinJSBridgeReady', this.onBridgeReady(response), false);
					} else if (document.attachEvent) {
						document.attachEvent('WeixinJSBridgeReady', this.onBridgeReady(response));
						document.attachEvent('onWeixinJSBridgeReady', this.onBridgeReady(response));
					}
				} else {
					this.onBridgeReady(response);
				}
			},
			onBridgeReady: function(response) {
				let that = this;
				if (!response.package) {
					return;
				}
				console.log("response)))):" + JSON.stringify(response))
				WeixinJSBridge.invoke(
					'getBrandWCPayRequest', {
						"appId": response.appid, //公众号名称，由商户传入
						"timeStamp": response.timestamp, //时间戳，自1970年以来的秒数
						"nonceStr": response.noncestr, //随机串
						"package": response.package,
						"signType": response.signType, //微信签名方式：
						"paySign": response.sign //微信签名
					},
					function(res) {
						if (res.err_msg === "get_brand_wcpay_request:ok") {
							// 使用以上方式判断前端返回,微信团队郑重提示：
							//res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
							uni.showLoading({
								title: '支付成功'
							});
							setTimeout(function() {
								uni.hideLoading();
								that.courseDet = []
								that.getDataList(that.courseId);
							}, 1000);
						} else {
							uni.hideLoading();
						}
						WeixinJSBridge.log(response.err_msg);
					}
				);
			},
			// 发表评论
			insertComment() {
				if (!this.isLogin) {
					uni.showToast({
						title: '请先登录在发表评论',
						duration: 1000,
						icon: 'none'
					});
					return
				}
				if (this.value == '') {
					uni.showToast({
						title: '请输入内容',
						duration: 1000,
						icon: 'none'
					});
					return
				}
				let data = {
					content: this.value,
					courseId: this.courseId,
				}
				this.$u.api.insertComment(data).then(res => {
					if (res.code == 0) {
						this.page = 1;
						this.commentList = []
						this.getCommentList(this.courseId)
						this.value = ''
					} else {
						uni.showToast({
							title: res.msg,
							duration: 1000,
							icon: 'none'
						});
					}
				})
			},
			// 收藏资源
			courseCollect() {
				let type = this.courseDet.isCollect == 0 ? 1 : 0
				if (!this.isLogin) {
					uni.showModal({
						title: '提示',
						content: '请登录后再收藏',
						confirmText: '现在就去',
						cancelText: '稍后再去',
						success: function(res) {
							if (res.confirm) {
								console.log('用户点击确定');
								uni.navigateTo({
									url: '/pages/login/login'
								})
							} else if (res.cancel) {
								console.log('用户点击取消');
							}
						}
					})
				}

				let data = {
					courseId: this.courseId,
					type: type
				}
				this.$u.api.courseCollect(data).then(res => {
					if (res.code == 0) {
						this.courseDet.isCollect = this.courseDet.isCollect == 0 ? 1 : 0
					} else {
						uni.showToast({
							title: res.msg,
							duration: 1000,
							icon: 'none'
						});
					}
				})
			},
			// 播放音频
			playVoice(voiceIntroduce, isPlay, index) {
				this.tabindex = index
				this.AUDIO.stop();
				if (voiceIntroduce) {
					this.isPlay = this.courseDet.listsDetail[index].palyVideo
					this.AUDIO.src = voiceIntroduce;
					if (this.isPlay == true) {
						this.AUDIO.stop();
						this.courseDet.listsDetail[index].palyVideo = false
					} else {
						let musicIndexs = this.musicIndex;
						if (musicIndexs != '') {
							this.courseDet.listsDetail[musicIndexs].palyVideo = false
						} else if (musicIndexs == 0) {
							this.courseDet.listsDetail[0].palyVideo = false
						}
						this.musicIndex = index;
						this.AUDIO.stop();

						this.AUDIO.play();
						this.courseDet.listsDetail[index].palyVideo = true
					}
				} else {
					uni.showToast({
						title: '请先点击下方按钮获取资源',
						duration: 1000,
						icon: 'none'
					});
				}
				// this.isPlay = !this.isPlay;
			},

			// playVideos(e, index) {
			// 	this.tabindex = index
			// 	let palyVideo = e.palyVideo
			// 	// console.log(e.palyVideo,'this.courseDet[index].e.palyVideo')
			// 	// console.log(this.courseDet.listsDetail[index].palyVideo,'this.courseDet[index].e.palyVideo')
			// 	if (e.videoUrl) {
			// 		// alert(palyVideo)
			// 		// return
			// 		if (!palyVideo) {
			// 			// this.courseDet.listsDetail[index].palyVideo = true
			// 			this.videoUrl = e.videoUrl
			// 			// let vaidetext = uni.createVideoContext('videoplay')

			// 			const vaidetext = uni.createInnerAudioContext();
			// 			vaidetext.autoplay = true;
			// 			vaidetext.src = this.videoUrl 
			// 			vaidetext.onPlay(() => {
			// 			  console.log('开始播放');
			// 			});
			// 			vaidetext.onError((res) => {
			// 			  console.log(res.errMsg);
			// 			  console.log(res.errCode);
			// 			});


			// 			setTimeout(function() {
			// 				vaidetext.play()
			// 			}, 100)
			// 			// alert(this.courseDet.listsDetail[index].palyVideo)
			// 			// this.$forceUpdate()
			// 		} else {
			// 			// this.courseDet.listsDetail[index].palyVideo = false
			// 			this.videoUrl = e.videoUrl
			// 			let vaidetext = uni.createVideoContext('videoplay')
			// 			setTimeout(function() {
			// 				vaidetext.stop()
			// 			}, 100)
			// 		}

			// 	} else {
			// 		uni.showToast({
			// 			title: '请先点击下方按钮获取资源',
			// 			duration: 1000,
			// 			icon: 'none'
			// 		});
			// 	}

			// },
			// 播放视频
			playVideo(e, index) {
				this.tabindex = index
				let palyVideo = e.palyVideo
				// console.log(e.palyVideo,'this.courseDet[index].e.palyVideo')
				// console.log(this.courseDet.listsDetail[index].palyVideo,'this.courseDet[index].e.palyVideo')
				if (e.videoUrl) {
					// alert(palyVideo)
					// return
					if (!palyVideo) {
						this.courseDet.listsDetail[index].palyVideo = true
						this.videoUrl = e.videoUrl
						let vaidetext = uni.createVideoContext('videoplay')
						setTimeout(function() {
							vaidetext.play()
						}, 100)
						// alert(this.courseDet.listsDetail[index].palyVideo)
						// this.$forceUpdate()
					} else {
						this.courseDet.listsDetail[index].palyVideo = false
						this.videoUrl = e.videoUrl
						let vaidetext = uni.createVideoContext('videoplay')
						setTimeout(function() {
							vaidetext.pause()
						}, 100)
					}

				} else {
					uni.showToast({
						title: '请先点击下方按钮获取资源',
						duration: 1000,
						icon: 'none'
					});
				}

			},
			tabSelect(e) {
				console.log(e)
				this.TabCur = e.name;
				// this.TabCur = e.currentTarget.dataset.id;
				// this.scrollLeft = (e.currentTarget.dataset.id - 1) * 60
			},
			// 点赞
			getLike(e) {
				let data = {
					courseCommentId: e.courseCommentId
				}
				this.commentList.forEach(res => {
					if (res.courseCommentId == e.courseCommentId) {
						res.isGood = !res.isGood
						if (res.isGood) {
							res.goodsNum += 1
						} else {
							res.goodsNum -= 1
						}
					}
				})

				this.$u.api.updateGood(data).then(res => {
					if (res.msg == '操作成功！' && res.code == 0) {
						// this.page = 1;
						// this.commentList = []
						// this.getCommentList(this.courseId)
					}
				})
			},
			previewImg(logourl) {
				let imgsArray = [];
				imgsArray[0] = logourl
				uni.previewImage({
					current: 0,
					urls: imgsArray
				});
			},
		},
		onReachBottom: function() {
			if (this.TabCur == '评论') {
				this.page = this.page + 1;
				this.getCommentList(this.courseId);
			}

		},
		onPullDownRefresh: function() {
			if (this.TabCur == '评论') {
				this.page = 1;
				this.commentList = []
				this.getCommentList(this.courseId);
			}

		},
	};
</script>


<style lang="scss" scoped>
	page {
		background: #F5F5F5;
	}

	.headtop {
		position: fixed;
		top: 0;
	}

	.det {
		img {
			width: 100%;
		}
	}

	.navigation {
		display: flex;
		border: 2rpx solid #f2f2f2;
		background-color: #ffffff;
		padding: 10rpx 0;

		.left {
			display: flex;
			font-size: 20rpx;

			.item {
				margin: 0 30rpx;

				&.car {
					text-align: center;
					position: relative;

					.car-num {
						position: absolute;
						top: -10rpx;
						right: -10rpx;
					}
				}
			}
		}

		.right {
			flex: 1;
			display: flex;
			font-size: 28rpx;
			align-items: center;

			.btn {
				width: 100%;
				line-height: 80rpx;
				padding: 0 10rpx;
				color: #ffffff;
				text-align: center;
			}

			.cart {
				width: 50%;
				border-radius: 39px 0px 0px 39px;
				background-color: #FF5A00;
			}

			.buy {
				width: 50%;
				border-radius: 0px 39px 39px 0px;
				background-color: #5074FF;
			}
		}
	}

	// .wrap {
	// 	display: flex;
	// 	flex-direction: column;
	// 	height: calc(100vh - var(--window-top));
	// 	width: 100%;
	// }

	.swiper-box {
		flex: 1;
	}

	.swiper-item {
		height: 100%;
	}

	// 评论
	.comment {
		display: flex;
		padding: 30rpx;
		// margin-bottom: 30rpx;

		.left {
			image {
				width: 64rpx;
				height: 64rpx;
				border-radius: 50%;
				background-color: #f2f2f2;
			}
		}

		.right {
			flex: 1;
			padding-left: 20rpx;
			font-size: 30rpx;

			.top {
				display: flex;
				justify-content: space-between;
				align-items: center;
				margin-bottom: 10rpx;

				.name {
					color: #333;
					font-weight: bold;
				}

				.like {
					display: flex;
					align-items: center;
					color: #9a9a9a;
					font-size: 26rpx;

					.num {
						margin-right: 4rpx;
						color: #9a9a9a;
					}
				}

				.highlight {
					color: #5677fc;

					.num {
						color: #5677fc;
					}
				}
			}

			.content {
				margin-top: 20rpx;
				margin-bottom: 10rpx;
			}

			.bottom {

				display: flex;
				font-size: 24rpx;
				color: #9a9a9a;

				.reply {
					color: #5677fc;
					margin-left: 10rpx;
				}
			}
		}
	}

	.text-blue {
		color: #5074FF;
	}

	.line {
		width: 40rpx;
		height: 6rpx;
		background: #5074FF;
		border-radius: 8rpx;
		// position: absolute;
		// top: 80rpx;
		// left: 95rpx;
	}

	.mubox {
		display: flex;
		align-items: center;
		font-size: bold;
		color: #333;
		font-size: 30rpx;
		margin-bottom: 20rpx;
		padding: 20rpx;
		background: #F5F5F5;
		border-radius: 16rpx;
	}

	.act {
		color: #5074FF !important;
	}
</style>