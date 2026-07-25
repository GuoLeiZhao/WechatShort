<template>
	<view>
		<swiper :circular="true" :style="isopen==true?'height:calc(100vh - 148rpx)':''" class="swipers" @change="change"
			:current="current" :vertical="true" :indicator-dots="false" :autoplay="false" :interval="3000"
			:duration="300">
			<swiper-item class="swipers-item" v-for="(item,index) in swiperList" :key="item.courseDetailsId">
				<view class="swipers-items">
					<!-- 视频 -->
					<video @timeupdate="timeupdate" 
						@waiting="waiting()" 
						object-fit="cover" 
						v-if="current == index"
						:play-strategy="2" 
						:show-loading="true" 
						codec="software" 
						:muted="false"
						:show-center-play-btn="true" 
						:loop="true" 
						@ended="ended"
						@play="videoPlay('myVideo'+item.courseDetailsId)" :enable-progress-gesture="false"
						:ref="'myVideo'+item.courseDetailsId" 
						:id="'myVideo'+item.courseDetailsId" 
						:src="item.videoUrl"
						:poster="item.titleImg" 
						:autoplay="item.autoPlay" 
						initial-time="0"
						class="swipers-items-video"
					></video>
					<!-- :autoplay="item.autoPlay" -->
					<!-- 没有视频权限则显示封面图 -->
					<image v-else @click="showPay = true" :src="item.titleImg" class="swipers-items-imgsbg"
						mode="aspectFill"></image>
					<!-- 视频信息 -->
					<view class="choose-btn flex justify-between align-center" @click="goCourse(item.courseId,item.courseDetailsId)">
						<span class="choose-btn-span">选集</span>
						<u-image width="28rpx" height="28rpx" src="https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/vedio/up.png" />
					</view>
					<view class="swipers-items-info" @click="goCourse(item.courseId,item.courseDetailsId)">
						<image style="width: 32rpx;height: 32rpx;" src="https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/vedio/icon.png"/>
						<!-- 标题 -->
						<view class="swipers-items-info-title">
							<!-- 【{{ title }}】 第 {{num}} 集 · 共 {{meunList.length}} 集 -->
							【{{ title }}】
						</view>
					</view>
					<!-- 右侧操作 -->
					<view class="swipers-items-right">
						<view class="swipers-items-right-item">
							<view class="swipers-items-right-item-img" @click.stop="dianzan(item)">
								<u-icon name="heart-fill" v-if="item.isGood!=null && item.isGood!=0" color="red"
									size="60"></u-icon>
								<u-icon name="heart-fill" v-else color="#ffffff" size="60"></u-icon>

							</view>
							<view class="swipers-items-right-item-txt">
								{{item.goodNum}}
							</view>
						</view>
						<view class="swipers-items-right-item" @click="share(item)">
							<view class="swipers-items-right-item-img">
								<image src="https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/me/share.png" mode=""></image>
							</view>
							<view class="swipers-items-right-item-txt">
								分享
							</view>
						</view>
						<view class="swipers-items-right-item" v-if="item.isCollect==null || item.isCollect == 0">
							<view class="swipers-items-right-item-img" @click.stop="shoucang(item)">
								<image src="https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/me/shuqian.png" style="height: 60rpx;" mode=""></image>
							</view>
							<view class="swipers-items-right-item-txt">
								追剧
							</view>
						</view>
						<view class="swipers-items-right-item" v-else>
							<view class="swipers-items-right-item-img" @click.stop="shoucang(item)">
								<image src="https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/me/shuqian_s.png" style="height: 60rpx;" mode="">
								</image>
							</view>
							<view class="swipers-items-right-item-txt">
								已追
							</view>
						</view>
					</view>
				</view>
			</swiper-item>
		</swiper>
		<!-- #ifndef MP-WEIXIN -->
		<tki-qrcode ref="qrcode" v-if="isStart" :val="erweima" :size="200" background="#fff" foreground="#000"
			pdground="#000" :onval="true" :loadMake="true" @result="qrR" :show="false"></tki-qrcode>
		<!-- @success：成功事件  imgSrc：图片地址 QrSrc：二维码图片地址  
		Title：标题 PriceTxt：价格 OriginalTxt：原始价格 LineType -->
		<cc-poster v-if="haibaoShow==true && erweima" @error="posterError" @success="posterSuccess" :imgSrc="imgSrc"
			:QrSrc="qrCodeData" :Title="title" :PriceTxt="title" :LineType="false"></cc-poster>
		<!-- #endif -->

		<!-- #ifdef MP-WEIXIN -->
		<cc-poster v-if="haibaoShow==true && qrCodeData" @error="posterError" @success="posterSuccess" :imgSrc="imgSrc"
			:QrSrc="qrCodeData" :Title="title" :PriceTxt="title" :LineType="false"></cc-poster>
		<!-- #endif -->
		<u-popup width="686" v-model="showImg" mode="center" border-radius="14">
			<image :show-menu-by-longpress='true' :src="haibaoImg" style="width: 100%;" mode="widthFix"></image>
		</u-popup>
	</view>
</template>

<script>
	import config from '../../common/config.js'
	import tkiQrcode from '../../components/tki-qrcode/tki-qrcode.vue';
	export default {
		components: {
			tkiQrcode
		},
		data() {
			return {
				tabBarJson: {},
				isStart: false, //是否开始生成二维码
				erweima: '',
				qrCodeData: '',
				haibaoImg: '',
				showImg: false,
				haibaoShow: false,
				title: '',
				imgSrc: '',
				current: 0,
				maskCustomStyle: {
					background: 'rgba(0, 0, 0, 0.5)'
				},
				show: false,
				videoList: [],
				isVIP: false, //是否是vip
				courseId: '',
				meunList: [], //菜单
				meunTop: 0, //返回图标距离顶部的距离
				num: 1, //当前播放的集数
				showPay: false, //购买视频的弹窗
				videoContextId: '', //记录播放的视频组件的id
				page: 1,
				pages: 1,
				limit: 99999,
				courseId: 0,
				courseDetailsId: 0,
				isopen: false, //是否是公众号页面

			};
		},
		computed: {
			swiperList() {
				return this.videoList
			}
		},
		onShow() {
			this.$Request.sysLog('浏览【推荐】页面')
			this.getDataList()
			
			console.log('getDataList');
			
			// #ifdef H5
			// 修改 tabbar 为 dark 模式
			wx.setTabBarStyle({
				"color": "#ffffff7f",
				"selectedColor": "#ffffffff",
				"backgroundColor": "#000",
				"borderStyle": "#FFFFFF",
			});
			
			this.tabBarJson.dark.forEach((item, idx) => {
				uni.setTabBarItem(item);
			})
			// #endif
		},
		onHide() {
			this.$nextTick(() => {
				if (this.videoContext) { //判断之前是否有视频的上下文
					this.meunList = [];
					this.videoList = [];
					this.videoContext.stop();
					this.videoContext = null;
					this.current = 0;
				}
			})
			// #ifdef H5
			// 修改 tabbar 回 bright
			uni.setTabBarStyle({
				"color": "#3333337f",
				"selectedColor": "#333333ff",
				"backgroundColor": "#FFFFFF",
				"borderStyle": "#000",
			});

			this.tabBarJson.bright.forEach((item, idx) => {
				uni.setTabBarItem(item);
			})
			// #endif
			console.log('视频页面隐藏啦～～～')
		},
		onLoad(e) {
			console.log('onload');
			// #ifdef H5
			let ua = navigator.userAgent.toLowerCase();
			if (ua.indexOf('micromessenger') !== -1) {
				//是公众号页面
				this.isopen = true;
			} else {
				this.isopen = false;
			}
			// #endif
			// #ifdef MP-WEIXIN
			if (e.scene) { //这里为线上操作
				//此处的二维码  page/index/index?brokerId=123
				let scene = decodeURIComponent(e.scene);
				scene = scene.split(',')
				uni.setStorageSync('invitation', scene[0])
				this.courseId = scene[1]
				this.courseDetailsId = scene[2]
				this.getDataList(this.courseId, this.courseDetailsId);
			}
			// #endif
			if (e.invitation) {
				uni.setStorageSync('invitation', e.invitation)
			}
			if (e.id) {
				this.courseId = e.id
			}
			if (e.courseDetailsId) {
				this.courseDetailsId = e.courseDetailsId
			}
			// this.getDataList()
			this.tabBarJson = require('@/static/json/tabBar.json')
		},
		onReady() {
			// #ifdef MP-WEIXIN
			let info = uni.getMenuButtonBoundingClientRect()
			this.meunTop = info.top + ((info.height - 19) / 2)
			// #endif
			// #ifndef MP-WEIXIN
			this.meunTop = 37
			// #endif
		},
		watch: {
			//监听当前播放的集数
			current() {
				this.comNumVideo()
			}
		},
		methods: {
			//播放进度变化回掉
			timeupdate(e) {
				//隐藏loding
				uni.hideLoading()
			},
			//缓冲中
			waiting(e) {
				//在h5状态下视频出现缓冲则显示loding
				// #ifdef H5
				uni.showLoading()
				// #endif
			},
			openShow() {
				this.show = true
				this.$nextTick(() => {
					this.scrollIntoViews = this.scrollIntoView
				})

			},
			//生成失败
			posterError() {
				this.haibaoImg = ''
				this.showImg = false
				// 生成完成后初始化分享
				this.haibaoShow = false
				this.isStart = false
				this.imgSrc = ''
				uni.showToast({
					title: '海报生成失败',
					mask: false,
					duration: 2000,
					icon: "none"
				});
			},
			/**
			 * @param {String} path //返回的二维码地址
			 */
			qrR(path) {
				this.qrCodeData = path
				this.haibaoShow = true
			},
			/**生成成功
			 * @param {String} img 成功回调的图片
			 */
			posterSuccess(img) {
				console.log(img)
				this.haibaoImg = img.tempFilePath
				this.showImg = true
				// 生成完成后初始化分享
				this.haibaoShow = false
				this.isStart = false
				this.imgSrc = ''
				uni.hideLoading()
				uni.showToast({
					title: '长按图片保存海报',
					mask: false,
					duration: 2000,
					icon: "none"
				});
			},
			//分享
			share(item) {
				this.imgSrc = item.titleImg
				this.title = item.title ? item.title : ''
				// #ifndef MP-WEIXIN
				let urls = config.APIHOST2 + '/pages/video/video?id=' + item.courseId + '&courseDetailsId=' + item
					.courseDetailsId + '&invitation=' + uni.getStorageSync('invitationCode')
				this.erweima = urls
				this.isStart = true
				// #endif


				// #ifdef MP-WEIXIN
				let that = this
				let data = {
					invitationCode: uni.getStorageSync('invitationCode') + ',' + item.courseId + ',' + item
						.courseDetailsId,
					page: 'pages/video/video'
				}
				uni.downloadFile({
					url: config.APIHOST + '/app/invite/mpCreateQr?invitationCode=' + data.invitationCode +
						'&page=' + data.page,
					success(res) {
						if (res.statusCode === 200) {
							that.qrCodeData = res.tempFilePath
							that.haibaoShow = true
						} else {
							uni.showToast({
								title: '分享失败',
								icon: 'none'
							})
						}
					}
				})
				// #endif
			},
			// 跳转资源详情
			goCourse(e, courseDetailsId) {
				let userId = uni.getStorageSync('userId')
				if (userId) {
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
				} else {
					uni.navigateTo({
						url: "/pages/login/login"
					})
				}
			},
			//收藏
			shoucang(item) {
				if (uni.getStorageSync('token')) {
					let data = {
						courseId: item.courseId,
						courseDetailsId: item.courseDetailsId,
						classify: 1,
						type: item.isCollect == 0 ? 1 : 0
					}
					this.$Request.postJson('/app/courseCollect/insertCourseCollect', data).then(res => {
						if (res.code == 0) {
							if (data.type == 1) {
								item.isCollect = 1
							} else {
								item.isCollect = 0
							}
						}
					})
				} else {
					uni.navigateTo({
						url: "/pages/login/login"
					})
				}
			},
			//点赞
			dianzan(item) {
				if (uni.getStorageSync('token')) {
					let data = {
						courseId: item.courseId,
						courseDetailsId: item.courseDetailsId,
						classify: 2,
						type: item.isGood == 0 ? 1 : 0
					}
					this.$Request.postJson('/app/courseCollect/insertCourseCollect', data).then(res => {
						if (res.code == 0) {
							if (data.type == 1) {
								item.isGood = 1
								item.goodNum += 1
							} else {
								item.isGood = 0
								item.goodNum -= 1
							}
						}
					})
				} else {
					uni.navigateTo({
						url: "/pages/login/login"
					})
				}

			},
			//计算正在观看哪个视频
			comNumVideo() {
				let courseDetailsId = this.videoList[this.current].courseDetailsId
				this.meunList.map((item, index) => {
					if (item.courseDetailsId == courseDetailsId) { //找到了
						this.num = index + 1
					}
				})
			},
			// 资源详情
			getDataList() {
				let data = {
					page: this.pages,
					limit: this.limit,
					token: uni.getStorageSync('token') ? uni.getStorageSync('token') : ''
				}
				this.$Request.getT('/app/course/selectCourseDetailsList', data).then(res => {
					console.log(res)
					if (res.code == 0) {
						this.pages = res.data.totalPage
						this.totalNum = res.data.totalCount
						if (res.data.list) {
							let arr = JSON.parse(JSON.stringify(res.data.list))
							arr.map((item, index) => {
								item.num = index + 1
								item.autoPlay = true
							})
							//菜单数组
							this.meunList = arr
							if (this.courseId && this.courseDetailsId) {
								let indexs = this.meunList.findIndex(item => item.courseDetailsId === Number(this
									.courseDetailsId));
								let lengths = this.meunList.length - (indexs + 1)
								this.videoList = [this.meunList[indexs]]
								//有最少两条
								if (lengths >= 2) {
									this.videoList = [...this.videoList, ...this.meunList.slice(indexs + 1,
										indexs + 3)]
								}
								//只剩一条数据
								if (lengths == 1) {
									//把剩下的一条给放进去
									this.videoList = [
										//选中的那条
										...this.videoList,
										//身下的一条
										this.meunList[this.meunList.length - 1],
										//最后一条则放总数组的第一条
										this.meunList[0]
									]
								}
								//选择的就是最后一条数据
								if (lengths == 0) {
									//后两条拿总数组的前两条就可以了
									this.videoList = [...this.videoList, ...this.meunList.slice(0, 2)]
								}
							}
							// //视频数组//直接拿前三条
							this.videoList = this.meunList.slice(0, 3)
							let numIdCurr = this.videoList[0].courseDetailsId;
							this.title = this.videoList[0].title
							this.videoContextId = 'myVideo' + numIdCurr;
							this.$nextTick(() => {
								this.videoContext = uni.createVideoContext(this.videoContextId, this);
								this.videoContext.play();
							})

							if (uni.getStorageSync('token')) { //如果有token则插入
								//插入历史记录
								this.setHistor(this.videoList[this.current].courseId, this.videoList[this.current]
									.courseDetailsId)
							}
							this.$forceUpdate()
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
			//记录观看记录
			setHistor(courseId, courseDetailsId) {
				//如果没有登录则不进行记录直接return
				if (!uni.getStorageSync('token')) {
					return
				}
				let data = {
					courseId: courseId,
					courseDetailsId: courseDetailsId,
					classify: 3,
					type: 1
				}
				this.$Request.postJson('/app/courseCollect/insertCourseCollect', data).then(res => {})
			},
			//swiper上下切换事件
			change(e) {
				this.$Request.sysLog('在【推荐】页面进行一次“滑动”操作')
				//拿出当前的swiper索引
				let current = Number(e.detail.current)
				//考虑向上滑动的时候
				if (current == 2 && this.current == 0) {
					//向上滑到头的时候先拿第一条数据的courseDetailsId
					let courseDetailsId = this.videoList[0].courseDetailsId
					// 根据courseDetailsId在meunList中找到这个数据的下标
					let indexs = this.meunList.findIndex(item => item.courseDetailsId === courseDetailsId);
					if (indexs != -1) { //找到了，indexs就是对应下标
						//先判断meunList数组剩下的元素是否够三条
						const lengths = (this.meunList.slice(0, indexs)).length
						if (lengths >= 3) { //够三条
							// 因为轮播图开启了首尾衔接,所以在滑动到尾部重新进入第一页之前替换全部数据为之前的三条
							//因为slice(str,end)，包含str，不包含end，所以拿三条数据则-3
							this.videoList = this.meunList.slice(indexs - 3, indexs)
						} else { //不够三条
							let arr = new Array(3)
							switch (lengths) {
								//只有一条
								case 1:
									//只有一条的时候需要给数组补两条凑够三条，把meunList数组的前两条拿出来补在后面
									arr[2] = this.meunList[indexs - 1] //把剩下的一条取出来放到要滑动的那一页
									//因为剩下的数据不够了，则拿meunList末尾的两条数据补齐
									//第二个用最后一条数据
									arr[1] = this.meunList[this.meunList.length - 1]
									//第一个用倒数第二条数据
									arr[0] = this.meunList[this.meunList.length - 2]
									//赋值
									this.videoList = arr
									break;
									//只有二条
								case 2:
									//还剩下两条数据的时候
									//把剩下的倒数第一条取出来放到要滑动的那一页
									arr[2] = this.meunList[indexs - 1]
									//把剩下的最后一条放到第二个元素
									arr[1] = this.meunList[indexs - 2]
									//最后一个元素则由meunList的最后一条补齐
									arr[0] = this.meunList[this.meunList.length - 1]
									//最后赋值
									this.videoList = arr
									break;
								default:
									//一条都没有的时候 [1,2,3,4,5,6]
									//直接把meunList末尾的三条放入数组即可
									this.videoList = this.meunList.slice(-3)
									break;
							}
						}
					}
				}
				//考虑向下滑动
				if (current == 0 && this.current == 2) { //是否滑动到第一条，虽然刚进入页面current为0，但是首次不触发
					//拿出当前的courseDetailsId
					let courseDetailsId = this.videoList[2].courseDetailsId
					//记录当前数据在meunList中的下标
					//根据courseDetailsId找出meunList中对应的数据的下标
					let indexs = this.meunList.findIndex(item => item.courseDetailsId === courseDetailsId);
					if (indexs != -1) { //找到了，indexs就是对应下标
						//先判断meunList数组剩下的元素是否够三条
						const lengths = (this.meunList.slice(indexs + 1, this.meunList.length)).length
						if (lengths >= 3) { //够三条
							// 因为轮播图开启了首尾衔接,所以在滑动到尾部重新进入第一页之前替换全部数据为之后的三条
							this.videoList = this.meunList.slice(indexs + 1, indexs + 4)
						} else { //不够三条
							let arr = new Array(3)
							switch (lengths) {
								//只有一条
								case 1:
									//只有一条的时候需要给数组补两条凑够三条，把meunList数组的前两条拿出来补在后面
									arr[0] = this.meunList[indexs + 1]
									arr[1] = this.meunList[0]
									arr[2] = this.meunList[1]
									this.videoList = arr
									break;
								case 2:
									//只有二条
									arr[0] = this.meunList[indexs + 1]
									arr[1] = this.meunList[indexs + 2]
									arr[2] = this.meunList[0]
									this.videoList = arr
									break;
								default:
									//刚好没有数据了 直接拿meunList的前三条数据
									this.videoList = this.meunList.slice(0, 3)
									break;
							}
						}
					}
				}
				//获取当前播放的视频在菜单数组中的位置
				let scrollIndex = this.meunList.findIndex(ite => ite.courseDetailsId == this.videoList[current]
					.courseDetailsId);
				//记录当前播放位置
				this.scrollIntoView = 'video' + scrollIndex;
				this.startPlay(current)
				this.current = current
				//插入历史记录
				this.setHistor(this.videoList[this.current].courseId, this.videoList[this.current].courseDetailsId)
			},
			startPlay(current) {
				if (this.videoContext) { //判断之前是否有视频的上下文
					this.videoContext.stop();
					this.videoContext = null;
				}
				let numIdCurr = this.videoList[current].courseDetailsId;
			
				this.title = this.videoList[current].title
				// 播放时记录当前播放的id
				this.videoContextId = 'myVideo' + numIdCurr;
				this.videoContext = uni.createVideoContext(this.videoContextId, this);
				this.$nextTick(() => {
					//播放当前的
					this.videoContext.play();
					this.$forceUpdate();
				})

			},
			//在播放时候的回调
			videoPlay(videoId) {

			},
			//监听视频播放完成
			ended() {
				// 播放完成，完成播放任务 812
				let userId = uni.getStorageSync('userId');
				this.$Request.postT("/app/integral/completeTask/812", {userId});
				this.startPlay(this.current)
			},
			//返回
			goBack() {
				uni.navigateBack()
			},
		},
	}
</script>

<style lang="scss">
	page {
		background-color: #000;
	}

	.list {
		width: 100%;
		height: 100%;
		background-color: #202020;
		color: #ffffff;

		.list-title {
			height: 100rpx;
			width: 100%;
			// padding-top: 30rpx;
			font-size: 32rpx;
			font-weight: bold;

			.list-title-icon {
				padding-left: 30rpx;
				padding-right: 30rpx;
			}

		}

		.list-item {
			padding: 0 30rpx;

			.list-item-box {
				width: 100%;
				line-height: 90rpx;
				background-color: #fff;
				color: #000;
				font-weight: bold;
				text-align: center;
				margin-top: 40rpx;
				border-radius: 20rpx;
			}

		}

		.list-box {
			width: 100%;
			height: calc(100% - 100rpx);
		}

		.list-box-item {
			width: calc(100% - 40rpx);

			margin: 0 auto;
			margin-bottom: 20rpx;
		}

		.list-box-item-l {
			width: 140rpx;
			height: 100%;
			border-radius: 10rpx;

			image {
				width: 100%;
				height: 160rpx;
				border-radius: 10rpx;
			}
		}

		.list-box-item-r {
			width: calc(100% - 160rpx);
			height: 100%;
			color: #ffffff;
			font-size: 32rpx;

			.list-box-item-r-title {
				width: 100%;
				font-weight: bold;
			}

			.list-box-item-r-titles {
				margin-top: 10rpx;

			}

			.list-box-item-r-content {
				width: 100%;
				color: rgba(255, 255, 255, 0.6);
				margin-top: 10rpx;
				overflow: hidden; //超出的文本隐藏
				display: -webkit-box;
				-webkit-line-clamp: 2; // 超出多少行
				-webkit-box-orient: vertical;
			}
		}
	}

	.swipers {
		width: 100%;

		/* #ifdef H5 */
		height: calc(100vh - 88rpx);
		/* #endif */
		/* #ifndef H5 */
		height: calc(100vh);

		/* #endif */
		.swipers-item {
			width: 100%;
			height: 100%;
		}

		.swipers-items {
			width: 100%;
			height: 100%;
			position: relative;
			background-color: #000;
		}

		.swipers-items-imgsbg {
			position: absolute;
			width: 100%;
			height: 90%;
			top: 50%;
			left: 50%;
			transform: translate(-50%, -50%);
		}

		.swipers-items-video {
			width: 100%;
			height: 100%;
		}

		.swipers-items-back {
			position: absolute;
			// top: 75rpx;
			left: 20rpx;
			z-index: 999;
		}

		.choose-btn {
			padding: 12rpx 24rpx;
			background-image: url('https://your-oss-bucket.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/details/btn.png');
			background-repeat: no-repeat;
			background-size: cover;
			width: 140rpx;
			height: 64rpx;
			
			position: absolute;
			bottom: 150rpx;
			right: 24rpx;
			
			.choose-btn-span {
				width: 56rpx;
				height: 40rpx;
				font-size: 28rpx;
				font-family: PingFangSC, PingFang SC;
				font-weight: 400;
				color: #FFFFFF;
				line-height: 40rpx;
			}
			
		}
		.swipers-items-info {
			
			height: 72rpx;
			background: linear-gradient(180deg, rgba(0,0,0,0.5) 0%, rgba(0,0,0,0.4) 100%);
			backdrop-filter: blur(1px);
			
			width: 100%;
			position: absolute;
			bottom: 80rpx;
			padding-left: 20rpx;
			color: #ffffff;
			font-size: 30rpx;
			display: flex;
			align-items: center;
			z-index: 999;
			// height: 72rpx;

			.swipers-items-info-content {
				margin-top: 10rpx;
				height: 40rpx;
				font-size: 28rpx;
				font-family: PingFangSC;
				font-weight: 400;
				color: #FFFFFF;
				line-height: 40rpx;
			}
		}

		.swipers-items-right {
			width: 60rpx;
			position: absolute;
			right: 20rpx;
			top: 50%;
			transform: translate(0, -50%);
			z-index: 999;

			.swipers-items-right-item {
				width: 100%;
				margin-bottom: 40rpx;
			}

			.swipers-items-right-item-img {
				image {
					width: 60rpx;
					height: 60rpx;
				}
			}

			.swipers-items-right-item-txt {
				width: 100%;
				text-align: center;
				font-size: 24rpx;
				color: #ffffff;
				font-weight: bold;
				margin-top: 10rpx;
			}
		}
	}
</style>