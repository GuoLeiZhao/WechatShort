<template>
	<view>
		<u-mask class="flex justify-center align-center" :show="showLoading" @click="show = false">
			<u-loading :show="showLoading"></u-loading>
		</u-mask>

		<swiper :circular="true" class="swipers" @change="change" :current="current" :vertical="true"
			:indicator-dots="false" :autoplay="false" :interval="3000" :duration="300">
			<swiper-item class="swipers-item" v-for="(item,index) in swiperList" :key="item.courseDetailsId">
				<view class="swipers-items">
					<!-- 视频 -->
					<video @timeupdate="timeupdate" @click="videoItemClick" @waiting="waiting()" object-fit="cover"
						v-if="current === index && item.videoUrl" :play-strategy="2" :show-loading="true"
						codec="software" :muted="false" :show-center-play-btn="false" :loop="false" @ended="ended"
						@play="videoPlay('myVideo'+item.courseDetailsId)" @pause="videoPause"
						:enable-progress-gesture="false" :poster="item.titleImg" :ref="'myVideo'+item.courseDetailsId"
						:id="'myVideo'+item.courseDetailsId" :src="item.videoUrl" :autoplay="item.autoPlay"
						class="swipers-items-video"></video>
					<!-- :autoplay="item.autoPlay" -->
					<!-- 没有视频权限则显示封面图 -->
					<image v-else @click="showPay = true" :src="item.titleImg" class="swipers-items-imgsbg"
						mode="aspectFill"></image>
					<!-- 返回图标 -->
					<!-- <u-icon name="arrow-left" @click="goBack()" class="swipers-items-back" :style="{top:meunTop+'px'}"
						color="#ffffff" size="38">返回</u-icon> -->

					<view class="navigate-back flex justify-between" :style="{top:meunTop+'px'}" @click="navigateBack">
						<u-icon name="arrow-left" @click="goBack" class="swipers-items-back" color="#ffffff"
							size="38"></u-icon>
						<span>返回</span>
					</view>
					<!-- 视频信息 -->
					<view v-if="showControls" class="choose-btn flex justify-between align-center" @click="openShow()">
						<span class="choose-btn-span">选集</span>
						<u-image width="28rpx" height="28rpx"
							src="https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/vedio/up.png"></u-image>
					</view>
					<view v-if="showControls" class="swipers-items-info" @click="openShow()">
						<image style="width: 32rpx;height: 32rpx;"
							src="https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/vedio/icon.png">
						</image>
						<!-- 标题 -->
						<view class="swipers-items-info-title">
							【{{ title }}】 第 {{num}} 集 · 共 {{meunList.length}} 集
						</view>
					</view>
					<!-- 右侧操作 -->
					<view class="swipers-items-right">
						<view class="swipers-items-right-item">
							<view class="swipers-items-right-item-img" @click.stop="dianzan(item)">
								<u-icon name="heart-fill" v-if="item.isGood!=0" color="red" size="60"></u-icon>
								<u-icon name="heart-fill" v-else color="#ffffff" size="60"></u-icon>

							</view>
							<view class="swipers-items-right-item-txt">
								{{item.goodNum}}
							</view>
						</view>
						<view class="swipers-items-right-item" @click="share(item)">
							<view class="swipers-items-right-item-img">
								<!-- #ifndef MP-WEIXIN -->
								<image
									src="https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/me/share.png"
									mode=""></image> 
								<!-- #endif -->
								<!-- #ifdef MP-WEIXIN -->
								<button open-type="share" class="share-btn" ></button>
								<!-- #endif -->
							</view>
							<view class="swipers-items-right-item-txt">
								分享
							</view>
						</view>
						<view class="swipers-items-right-item" v-if="item.isCollect == 0">
							<view class="swipers-items-right-item-img" @click.stop="shoucang(item)">
								<image
									src="https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/me/shuqian.png"
									style="height: 60rpx;" mode="">
								</image>
							</view>
							<view class="swipers-items-right-item-txt">
								追剧
							</view>
						</view>
						<view class="swipers-items-right-item" v-else>
							<view class="swipers-items-right-item-img" @click.stop="shoucang(item)">
								<image
									src="https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/me/shuqian_s.png"
									style="height: 60rpx;" mode="">
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
		<!-- 选集弹窗 -->
		<u-popup :mask-custom-style="maskCustomStyle" v-model="show" height="960rpx" border-radius="32" mode="bottom">
			<view class="episode-list flex align-center justify-center flex-direction">
				<view class="title-box flex align-center">
					<u-image :width="128" :height="168" :border-radius="8" :src="info.titleImg"></u-image>
					<view class="list-title flex flex-direction justify-center ">
						<span class="list-main-title">{{title}}</span>
						<view class="flex align-center ">
							<span class="list-sub-title">
								{{info.isOver == 1?'已完结':'连载中'}} · 共 {{meunList.length}} 集
							</span>
							<view v-if="videoList[current] && videoList[current].isCollect"
								class="zhui has-shoucang flex align-center justify-center"
								@click.stop="shoucang(videoList[current])">已追</view>
							<view v-else class="zhui shoucang flex align-center justify-center"
								@click.stop="shoucang(videoList[current])">追剧</view>
						</view>
					</view>
				</view>

				<view class="list-box flex flex-wrap">
					<view class="list-item-box flex align-center justify-center" v-for="(item,index) in meunList"
						:key="index" @click="selectPlay(item)">
						<view v-if="menuIdx == index" class="playing flex align-center justify-center">在看</view>
						<span v-else>{{index + 1}}</span>
						<view v-if="1 == item.isPrice" class="item-icon flex align-center justify-center">
							付费
						</view>
					</view>
				</view>
			</view>

		</u-popup>
		<!-- 购买视频 -->
		<u-popup :closeable="true" close-icon-size="28" close-icon-color="#CC5800" height="960rpx"
			:mask-custom-style="maskCustomStyle" v-model="showPay" border-radius="32" mode="bottom"
			@close="showPayClose">
			<view class="pay-box flex flex-direction">
				<view class="title-box flex flex-direction">
					<view class="title flex align-center">
						<span>解锁本集：</span>
						<span>{{videoList[current]?videoList[current].price?videoList[current].price:0:0}} 看点</span>
					</view>
					<view class="title-item flex align-center justify-center">
						<span>剩余看点：</span>
						<span>{{moneyNum}}</span>
					</view>
				</view>
				<view class="kandian-pay-box flex flex-wrap justify-between ">
					<!-- max 是 充值看点 -->
					<!-- min 是 赠送看点 -->
					<view class="kd-title flex align-center">
						<u-image :width="32" :height="32"
							src="https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/details/view.png"></u-image>
						<span class="kd-cz-title">看点充值</span>
					</view>

					<view class="pay-item flex-direction justify-center align-center"
						:class="item.remark=='推荐' ? 'tehui':'normal'" v-for="(item, idx) in paySend" :key="idx"
						@click="kandianPay(item)">
						<view v-if="item.remark" class="float">
							{{item.remark}}
						</view>

						<view class="money flex align-end justify-center">
							<span class="m-unit">¥</span>
							<span class="m-num">{{item.value}}</span>
						</view>
						<view class="kd flex justify-center align-center">
							<view class="kd-num">
								{{item.max}}看点
							</view>
							<view class="kd-send">
								+{{item.min}}看点
							</view>
						</view>
						<view class="remark">
							{{item.desc}}
						</view>
					</view>
				</view>
			</view>
		</u-popup>

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

		<u-mask :show="supFirstShow" :mask-click-able="false">
			<view class="suprise-first flex flex-direction justify-center align-center">
				<view class="suprise-title">
					{{cheapShopItem.supName ? cheapShopItem.supName : ''}}
				</view>
				<view class="suprise-item flex flex-direction align-center justify-center">
					<u-image :src="cheapShopItem.pic" width="346rpx" height="346rpx" mode="aspectFit"
						class="suprise-item-pic"></u-image>
					<view class="suprise-item-title">
						<text>{{cheapShopItem.name}}</text>
					</view>
				</view>
				<view class="suprise-btn flex align-center justify-center"
					@click="$Request.sysLog('在【观剧】页面第二次弹出挽回商品');supSecondShow=true; supFirstShow=false">
					免费领取
				</view>
				<u-image @click="supFirstShow=false;qiweiShow=true"
					src="https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/details/coupon/close.png"
					width="64rpx" height="64rpx" mode="aspectFit" class="suprise-item-pic">
				</u-image>
			</view>
		</u-mask>

		<u-mask :show="supSecondShow" :mask-click-able="false">
			<view class="flex flex-direction justify-center align-center" style="width: 100%; height:100vh;">
				<view class="suprise-snd flex flex-direction justify-center align-center">
					<view class="snd-title">
						看剧就能得积分
					</view>
					<view class="snd-sub-title">
						解锁短剧，获得积分
					</view>
					<view class="snd-content-box">
						<view class="snd-content-item flex justify-center align-center">
							<u-image :src="cheapShopItem.pic" mode="aspectFit" width="112rpx" height="112rpx"></u-image>
							<span class="snd-content-item-title flex justify-center align-center">
								<text>{{cheapShopItem.name}}</text>
							</span>
						</view>
						<view class="snd-content-jifen flex justify-center align-center">
							<view class="snd-content-jifen-item flex flex-direction justify-center align-center">
								<span>{{jifen}}</span>
								<span>已有积分</span>
							</view>
							<view class="snd-content-jifen-item flex flex-direction justify-center align-center">
								<span>{{jifen > cheapShopItem.price ? cheapShopItem.price - jifen : 0}}</span>
								<span>还差积分</span>
							</view>
						</view>
					</view>
					<view @click="supSecondShow=false;showPay=true;$Request.sysLog('在【观剧】页面弹出支付页面')"
						class="snd-btn flex justify-ceounter align-center">
						去解锁
					</view>
				</view>
				<u-image @click="supSecondShow=false;qiweiShow=true"
					src="https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/details/coupon/close.png"
					width="64rpx" height="64rpx" mode="aspectFit" style="margin-top: 96rpx;"></u-image>
			</view>
		</u-mask>

		<u-mask :show="qiweiShow" :mask-click-able="false">
			<view class="flex flex-direction justify-center align-center" style="width: 100%; height:100vh;">
				<u-image
					src="https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/details/qiwei.jpg"
					class="suprise-srd flex flex-direction justify-center align-center" width="526rpx" height="710rpx"
					mode="aspectFit">
				</u-image>
				<u-image @click="qiweiShow=false"
					src="https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/details/coupon/close.png"
					width="64rpx" height="64rpx" mode="aspectFit" style="margin-top: 96rpx;">
				</u-image>
			</view>
		</u-mask>
	</view>
</template>

<script scoped>
	//导入地址
	import config from '../../common/config.js'
	import {
		showToast
	} from '../../common/queue.js';
	import tkiQrcode from '../../components/tki-qrcode/tki-qrcode.vue';
	// #ifdef MP-WEIXIN
	var videoAd = null;
	// #endif
	export default {
		components: {
			tkiQrcode
		},
		data() {
			return {
				platform: 'android',
				showControls: false,
				supFirstShow: false,
				supSecondShow: false,
				qiweiShow: false,
				cheapShopItem: {},
				payConfirm: {
					type: "",
					title: "",
					img: "",
					num: 0,
					payWay: "",
				},
				customStyle: {
					background: '#202020'
				},
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
				videoContext: null, //记录当前视频的上下文
				isVIP: false, //是否是vip
				courseId: '',
				meunList: [], //菜单
				meunTop: 0, //返回图标距离顶部的距离
				menuIdx: 0, //返回图标距离顶部的距离
				num: 1, //当前播放的集数
				showPay: false, //购买视频的弹窗
				needAd: false,
				showConfirm: false, // 兑换确认的弹窗
				info: {}, //整部的信息
				courseDetailsId: '', //详情id
				scrollIntoView: '', //当前播放视频的位置
				scrollIntoViews: 'video0', //当前播放视频的位置
				isVips: '否', //是否显示会员
				adUnitId: '', //广告id
				isGuanggao: '否',
				VIPlist: [],
				paySend: [],
				jifen: 0,
				moneyNum: 0,
				showLoading: false,
				waitPay: 0,
				isopen: false,
			};
		},
		computed: {
			swiperList() {
				return this.videoList
			}
		},
		onLoad(e) {
			let that = this
			// #ifdef MP-WEIXIN
			if (e.scene) { //这里为线上操作
				//此处的二维码  page/index/index?brokerId=123
				let scene = decodeURIComponent(e.scene);
				scene = scene.split(',')
				uni.setStorageSync('invitation', scene[0])
				this.courseId = scene[1]
				this.courseDetailsId = scene[2]
			}
			this.isGuanggao = uni.getStorageSync('isGuanggao')
			this.adUnitId = uni.getStorageSync('adUnitId')
			if (this.isGuanggao == '是' && this.adUnitId) {
				this.adLoad()
			}

			// 获取系统信息
			wx.getSystemInfo({
				success: function(res) {
					console.log(res);
					// res.platform 可能的值有 "android", "ios", "devtools", "windows"
					const platform = res.platform.toLowerCase();

					// 根据平台设置不同的样式或逻辑
					console.log('platform', platform);
					that.platform = platform;

				}
			});

			// #endif
			if (e.invitation) {
				uni.setStorageSync('invitation', e.invitation)
			}
			if (e.id) {
				this.courseId = e.id
				if (e.courseDetailsId) {
					this.courseDetailsId = e.courseDetailsId
				}

			}
			// #ifdef H5
			let ua = navigator.userAgent.toLowerCase();
			if (ua.indexOf('micromessenger') !== -1) {
				this.isopen = true;
			} else {
				this.isopen = false;
			}
			// #endif
		},
		onShow() {
			this.$Request.sysLog('浏览【观剧】页面，观看剧 id：' + this.courseId + '，集 id：' + this.courseDetailsId)

			// this.getCheapItem(); 
			if (uni.getStorageSync('token') && this.courseId && this.courseDetailsId) {
				this.getDataList(this.courseId, this.courseDetailsId);
				this.paySendFunc();
				this.getJifen();
				this.getVIPDet();
				this.getMyMoney();
			}
			if (!uni.getStorageSync('token') && this.courseId && this.courseDetailsId) {
				uni.showModal({
					title: '提示',
					content: '请登录后观看',
					confirmText: '去登录',
					complete(res) {
						if (res.confirm) {
							uni.navigateTo({
								url: "/pages/login/login"
							})
						} else {
							uni.switchTab({
								url: '/pages/index/index'
							})
						}
					}
				})
			}
			this.isVips = uni.getStorageSync('isVips') ? uni.getStorageSync('isVips') : '否'
			let isVip = uni.getStorageSync('isVIP')
			let that = this
			uni.$on('back', (data) => {
				if (data.flag == true && isVip == true) {
					that.showPay = false
					that.getDataList(that.courseId, that.courseDetailsId, true);
				}
			})
			this.videoItemClick();
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
		onShareAppMessage(res) {
			return {
				path: '/me/detail/detail?id=' + this.courseId, //这是为了传参   onload(data){let id=data.id;} 
				title: this.title+" - 第" + this.num + "集",
				imageUrl: this.videoList[this.current].titleImg
			}
		},
		watch: {
			//监听当前播放的集数
			current() {
				this.comNumVideo()
			}
		},
		methods: {
			videoItemClick() {
				let that = this
				if (this.showControls) {
					this.showControls = false;
					clearTimeout(that.timer);
					return;
				}
				this.showControls = true;
				// #ifdef H5
				this.timer = setTimeout(() => {
					that.showControls = false;
					clearTimeout(that.timer);
				}, 3000)
				// #endif

				// #ifndef H5
				this.timer = setTimeout(() => {
					that.showControls = false;
					clearTimeout(that.timer);
				}, 5000)
				// #endif
			},
			initShare() {
				// #ifdef H5
				let item = this.videoList[this.current]
				let link = config.APIHOST2 + '/me/detail/detail?id=' + item.courseId + '&courseDetailsId=' + item
					.courseDetailsId
				let desc = this.info.details
				desc = desc.replaceAll('<p>', '').replaceAll('</p>', '')
				let shareData = {
					title: this.info.title,
					desc: desc,
					link: link,
					imgUrl: this.info.titleImg
				}
				let ua = navigator.userAgent.toLowerCase();
				if (ua.indexOf('micromessenger') !== -1) {
					/*初始化分享*/
					this.$wechatMP.initShare(this, shareData);
				}
				// #endif
			},
			navigateBack() {
				let pages = getCurrentPages();
				if (pages.length <= 1) {
					uni.switchTab({
						url: '/pages/index/index'
					})
				} else {
					uni.navigateBack();
				}

			},
			showPayClose() {
				this.showPay = false;
				if (this.needAd) {
					this.getCheapItem()
				}
				this.needAd = false

				this.$Request.sysLog('在【观剧】页面关闭购买视频弹窗')
			},
			getCheapItem() {
				let userId = uni.getStorageSync('userId');

				this.$Request.getT('/app/shop/item/' + userId).then(res => {
					if (res.code == 0) {
						this.$Request.sysLog('在【观剧】页面第一次弹出挽回商品，商品 id 为: ' + res.data.data.id)
						this.cheapShopItem = res.data.data
						this.supFirstShow = true
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none',
							duration: 2000
						})
					}
				})

			},
			//获取
			adLoad() {
				let that = this
				if (wx.createRewardedVideoAd) {
					videoAd = wx.createRewardedVideoAd({
						adUnitId: that.adUnitId //你的广告key,从微信小程序后台>流量主>广告管理获取
					});
					console.log(videoAd, '广告广告广告广告11111')
					videoAd.onError(err => {
						console.log(err);
					});
					videoAd.onClose((status) => {
						if (status && status.isEnded || status === undefined) {
							//广告播放完成后的的业务逻辑
							console.log('播放成功')
							uni.showToast({
								title: '加载中'
							})
							let data = {
								courseId: that.videoList[that.current].courseId,
								courseDetailsId: that.videoList[that.current].courseDetailsId
							}
							that.$Request.postT("/app/course/courseNotify", data).then(res => {
								uni.hideLoading();
								if (res.code == 0) {
									uni.showToast({
										title: '解锁成功',
										icon: 'none',
										duration: 2000
									})
									that.showPay = false
									that.getDataList(that.courseId, that.courseDetailsId, true);
								} else {
									uni.showToast({
										title: res.msg,
										icon: 'none',
										duration: 2000
									})
								}
							});
						} else {
							//广告播放失败或者未播放完成
							console.log('播放失败或者未播放完成')
							wx.showToast({
								title: '未完整观看视频不能获取奖励哦',
								icon: 'none'
							})
						}
					});
				}
			},
			//拉取广告
			openVideoAd() {
				let that = this
				console.log(videoAd, 222222222)
				if (videoAd) {
					videoAd.show().catch(err => {
						console.log(' 广告拉取失败，重试')
						// 广告拉取失败，重试
						videoAd.load().then(() => {
							videoAd.show();
						});
					})
				}
			},
			/**
			 * 获取积分
			 */
			getJifen() {
				this.$Request.getT('/app/integral/selectByUserId').then(res => {
					if (res.code == 0) {
						this.jifen = res.data.integralNum
					} else {
						this.jifen = 0
					}
				})
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
			goPage(url) {
				if (url) {
					if (url.indexOf('/pages/') !== -1 || url.indexOf('/me/') !== -1) {
						uni.navigateTo({
							url
						});
					} else {
						//#ifndef H5
						uni.navigateTo({
							url: '/pages/index/webView?url=' + url
						});
						//#endif
						//#ifdef H5
						window.location.href = url;
						//#endif	
					}
				}
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
			paySendFunc() {
				let that = this

				this.$u.get('/app/common/type/condition/charge').then(res => {
					if (res.code == 0 && res.data) {
						console.log('paysend', res.data)
						this.paySend = res.data
					}
				});
			},
			getVIPDet() {
				this.$u.api.vipDet().then(res => {
					if (res.code == 0) {
						console.log('getVIPDet', res.data)
						res.data.forEach(ret => {
							switch (ret.vipNameType) {
								case 0:
									ret.type = '月'
									ret.need = this.formatNumber(ret.money / 30)
									break;
								case 1:
									ret.type = '季'
									ret.need = this.formatNumber(ret.money / 90)
									break;
								case 2:
									ret.type = '年'
									ret.need = this.formatNumber(ret.money / 365)
									break;
							}
						})
						this.VIPlist = res.data
					} else {
						uni.showToast({
							title: res.msg,
							duration: 1000,
							icon: 'none'
						});
					}
				})
			},
			//播放进度变化回掉
			timeupdate(e) {
				//隐藏loding
				uni.hideLoading()
			},
			kandianPay(item) {
				let that = this
				let data = {
					price: parseFloat(item.value)
				}
				// #ifdef H5
				let ua = navigator.userAgent.toLowerCase();
				if (ua.indexOf('micromessenger') !== -1) {
					let data = {
						classify: 2,
						money: parseFloat(item.value),
						courseId: that.videoList[that.current].courseId,
						courseDetailsId: that.videoList[that.current].courseDetailsId
					}
					this.waitPay = parseFloat(item.value);
					this.$Request.postT('/app/wxPay/payMoney', data).then(res => {
						if (res.code == 0) {
							that.callPay(res.data);
						} else {
							uni.showToast({
								icon: 'none',
								title: '支付失败!'
							});
						}
					});
				} else {
					uni.navigateTo({
						url: '/me/payOrder/payOrder?info=' + JSON.stringify(data)
					})
				}
				// #endif

				// #ifdef MP-WEIXIN
				let platform = this.platform
				let userId = uni.getStorageSync('userId');
				if (platform === 'android') {
					let sessionKey = uni.getStorageSync('sessionkey')
					let signData = {
						offerId: config.offerId,
						buyQuantity: parseInt(item.min),
						env: 0,
						currencyType: 'CNY',
						attach: '',
					};
					this.$Request.postJson('/app/wechatOfficeAccount/getSign/' + config.TENANT, {
						sessionKey,
						signData,
						classify: 3,
						userId,
						courseId: that.videoList[that.current].courseId,
						courseDetailsId: that.videoList[that.current].courseDetailsId,
						money: parseFloat(item.value),
					}).then(ret => {
						// 安卓需要走虚拟支付
						wx.requestVirtualPayment({
							signData: ret.signData,
							paySig: ret.paySign,
							signature: ret.userSign,
							mode: 'short_series_coin',
							success(res) {
								// 使用以上方式判断前端返回,微信团队郑重提示：
								//res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
								uni.removeStorageSync('EditAddress')
								uni.showLoading({
									title: '支付成功'
								});
								uni.hideLoading();
								that.completeTask();
								setTimeout(function() {
									that.showPay = false;
									that.startPlay(that.current)
								}, 1000)
								that.$Request.sysLog('在【观剧】页面充值成功，金额：' + that.waitPay)
							},
							fail({
								errMsg,
								errCode
							}) {
								if (errCode === -2) {
									that.showPay = false;
									that.$Request.sysLog('在【观剧】页面用户取消充值，金额：' + that.waitPay)
								} else {
									that.$Request.sysLog('在【观剧】页面充值失败，金额：' + that.waitPay + " 原因: " + errMsg)
									console.log(12345);
									uni.hideLoading();
								}
								console.error(errMsg, errCode)
							},
						})
					})
					
				} else {
					// IOS 正常走商户支付
					this.$Request.postT('/app/wxPay/payMoney', {
						classify: 3,
						money: parseFloat(item.value),
					}).then(ret => {
						uni.hideLoading()
						console.log(ret.data)
						uni.requestPayment({
							provider: 'wxpay',
							timeStamp: ret.data.timestamp,
							nonceStr: ret.data.noncestr,
							package: ret.data.package,
							signType: ret.data.signType,
							paySign: ret.data.sign,
							success: function(suc) {
								console.log('success:' + JSON.stringify(suc));
								uni.showToast({
									title: '支付成功',
									icon: 'success'
								})
								that.completeTask();
								setTimeout(function() {
									that.showPay = false;
									that.startPlay(that.current)
								}, 1000)
								that.$Request.sysLog('在【观剧】页面充值成功，金额：' + that.waitPay)
							},
							fail: function(err) {
								console.log('fail:' + JSON.stringify(err));
								uni.showToast({
									title: '支付失败',
									icon: 'none'
								})
								that.$Request.sysLog('在【观剧】页面充值失败，金额：' + that.waitPay)
								uni.hideLoading();
							}
						});
					});
				}
				// #endif

			},
			completeTask() {
				// 充值完成 814 任务
				let userId = uni.getStorageSync('userId');
				this.$Request.postT("/app/integral/completeTask/814", {
					userId
				});
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
						console.log('onBridgeReady', res)
						if (res.err_msg === "get_brand_wcpay_request:ok") {
							// 使用以上方式判断前端返回,微信团队郑重提示：
							//res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
							uni.removeStorageSync('EditAddress')
							uni.showLoading({
								title: '支付成功'
							});
							uni.hideLoading();
							that.completeTask();
							setTimeout(function() {
								that.showPay = false;
								that.startPlay(that.current)
							}, 1000)
							that.$Request.sysLog('在【观剧】页面充值成功，金额：' + that.waitPay)
						} else if (res.err_msg === 'get_brand_wcpay_request:cancel') {
							that.showPay = false;
							that.$Request.sysLog('在【观剧】页面用户取消充值，金额：' + that.waitPay)
						} else {
							that.$Request.sysLog('在【观剧】页面充值失败，金额：' + that.waitPay)
							console.log(12345);
							uni.hideLoading();
						}
						WeixinJSBridge.log(response.err_msg);
					}
				);
			},
			vipPay(item) {
				let data = {
					price: parseFloat(item.money),
					vip: item,
				}
				uni.navigateTo({
					url: '/me/payOrder/payOrder?info=' + JSON.stringify(data)
				})
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
			/**
			 * @param {String} path //返回的二维码地址
			 */
			qrR(path) {
				this.qrCodeData = path
				this.haibaoShow = true

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
			/**生成成功
			 * @param {String} img 成功回调的图片
			 */
			posterSuccess(img) {
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
				// #ifndef MP-WEIXIN
				let urls = config.APIHOST2 + '/me/detail/detail?id=' + item.courseId + '&courseDetailsId=' + item
					.courseDetailsId + '&invitation=' + uni.getStorageSync('invitationCode')
				this.erweima = urls
				this.isStart = true
				// #endif


				// #ifdef MP-WEIXIN
				let that = this
				let data = {
					invitationCode: uni.getStorageSync('invitationCode') + ',' + item.courseId + ',' + item
						.courseDetailsId,
					page: 'me/detail/detail'
				}
				uni.downloadFile({
					url: config.APIHOST + '/app/invite/mpCreateQr?invitationCode=' + data.invitationCode +
						'&page=' + data.page,
					success(res) {
						if (res.statusCode === 200) {
							that.qrCodeData = res.tempFilePath
							that.haibaoShow = true
						} else {
							uni.hideLoading()
							uni.showToast({
								title: '分享失败',
								icon: 'none'
							})
						}
					}
				})
				// #endif
			},

			/**
			 * @param {Object} type 类型 
			 * 1:购买整部视频
			 * 2:购买单集视频
			 */
			payVideo(type) {
				// 首先生成订单
				let courseId = this.videoList[this.current].courseId
				let courseDetailsId = this.videoList[this.current].courseDetailsId
				let titleImg = this.videoList[this.current].titleImg
				let courseDetailsName = this.videoList[this.current].courseDetailsName
				let name = this.info.title
				let zongPrice = this.info.price
				let price = this.videoList[this.current].price
				let data = {
					courseId,
					courseDetailsId,
					titleImg,
					courseDetailsName,
					price,
					zongPrice,
					type,
					name
				}
				uni.navigateTo({
					url: '/me/payOrder/payOrder?info=' + JSON.stringify(data)
				})
			},

			//收藏
			shoucang(item) {
				if (!item) {
					item = this.videoList[this.current]
				}

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
			},
			//点赞
			dianzan(item) {
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
			},
			//计算正在观看哪个视频
			comNumVideo() {
				let courseDetailsId = this.videoList[this.current].courseDetailsId
				this.meunList.map((item, index) => {
					if (item.courseDetailsId == courseDetailsId) { //找到了
						this.num = item.num
					}
				})
			},
			//选择集数后在更新剩下的数据
			setVideoList(index) {
				//判断后续是否还有两条视频
				let lengths = this.meunList.length - (index + 1)
				//有最少两条
				if (lengths >= 2) {
					//直接拿两条赋值进去
					this.videoList = [...this.videoList, ...this.meunList.slice(index + 1, index + 3)]
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
				//更新视图
				this.$forceUpdate()
			},
			//选择播放
			selectPlay(item) {
				// 根据选择的courseDetailsId拿到meunList列表中的相同数据的下标
				const index = this.meunList.findIndex(menu => menu.courseDetailsId === item.courseDetailsId);
				this.courseDetailsId = item.courseDetailsId
				this.courseId = item.courseId
				this.menuIdx = index;
				this.videoList = [this.meunList[index]]
				this.current = 0
				//重新计算一下当前观看的是哪个视频
				this.comNumVideo()
				// 把选择的视频的自动播放设置为true
				this.videoList[this.current].autoPlay = true
				//让当前选择的视频播放
				this.startPlay(this.current)
				//更新视图
				this.$forceUpdate()
				//关闭选择弹窗
				this.show = false
				//记录当前播放位置
				this.scrollIntoView = 'video' + index
				//插入历史记录
				this.setHistor(this.videoList[this.current].courseId, this.videoList[this.current]
					.courseDetailsId)
				//等视图跟新完了，把之前剩下的数据补进去在$nextTick中处理
				this.$nextTick(() => {
					this.setVideoList(index)
				})
			},
			// 资源详情
			getDataList(id, courseDetailsId, type, beforePlay) {
				let data = {
					id,
					token: uni.getStorageSync('token') ? uni.getStorageSync('token') : ''
				}
				this.$u.api.selectCourseDetailsById(data).then(res => {
					console.log(11111111111, res, type)
					if (res.code == 0) {
						if (res.data.listsDetail) {

							this.info = res.data
							this.title = this.info.title
							let arr = JSON.parse(JSON.stringify(res.data.listsDetail))
							arr.map((item, index) => {
								item.num = index + 1
								if (item.videoUrl) {
									item.autoPlay = true
								} else {
									item.autoPlay = false
								}
								item.viewInfo = 'video' + index
							})
							//菜单数组
							this.meunList = arr
							if (type == true) { //购买视频后返回的

								// 继续播放解锁的视频
								let courseDetailsId = this.videoList[this.current].courseDetailsId
								this.meunList.some((item) => {
									if (item.courseDetailsId == courseDetailsId) {
										this.videoList[this.current].videoUrl = item.videoUrl

										if (beforePlay) {
											beforePlay()
										}

										//自动播放
										this.startPlay(this.current)

										// 解锁视频完成 813 任务
										let userId = uni.getStorageSync('userId');
										this.$Request.postT("/app/integral/completeTask/813", {
											userId
										});
									}
									return item.courseDetailsId == courseDetailsId
								})

							} else { //直接跳转进来的
								let indexs = -1
								console.log(courseDetailsId, '-------------')
								// let courseDetailsIds = courseDetailsId ? courseDetailsId : this.meunList[0]
								// 	.courseDetailsId
								if (courseDetailsId) { //从记录进来的
									this.meunList.map((item, index) => {
										if (item.courseDetailsId == courseDetailsId) {
											indexs = index
										}
									})
									console.log(courseDetailsId, '记录id')
									console.log(indexs, '记录id')
									if (indexs != -1) { //找到了
										if (Number(indexs + 1) === this.meunList.length) { //最后一条
											if (this.meunList.length == 1) { //只有一条
												this.videoList = this.meunList.slice(0, 3)
											} else if (this.meunList.length == 2) {
												this.videoList = this.meunList.slice(0, 3)
											} else {
												this.videoList = [
													this.meunList[this.meunList.length - 1],
													this.meunList[0],
													this.meunList[1],
												]
											}

										} else if (Number(indexs) === Number(this.meunList.length - 1)) { //倒数第二条
											if (this.meunList.length == 1) { //只有一条
												this.videoList = this.meunList.slice(0, 3)
											} else if (this.meunList.length == 2) { //有两条的时候
												this.videoList = [
													this.meunList[1],
													this.meunList[0],
												]
											} else {
												this.videoList = [
													this.meunList[this.meunList.length - 2],
													this.meunList[this.meunList.length - 1],
													this.meunList[0],
												]
											}

										} else {
											//如果不是最后一条，也不是倒数第二条，则从找到的位置开始往后拿三条数据放入数组
											this.videoList = this.meunList.slice(indexs, indexs + 3)
										}
									} else {
										//没找到直接拿前三条数据即可
										this.videoList = this.meunList.slice(0, 3)
									}
								} else { //新的视频
									// //视频数组//直接拿前三条
									this.videoList = this.meunList.slice(0, 3)
									indexs = 0
								}
								console.log(this.videoList)
								//重新计算当前在播放哪个视频
								this.comNumVideo()
								if (indexs != -1 && this.meunList.length > 0) {
									this.$nextTick(() => {
										this.scrollIntoView = 'video' + indexs
									})

								}
							}
							let numIdCurr = this.videoList[0].courseDetailsId
							this.videoContextId = 'myVideo' + numIdCurr

							this.$nextTick(() => {
								this.videoContext = uni.createVideoContext(this.videoContextId, this)
								this.videoContext.play()
							})
							this.$forceUpdate()
							//插入历史记录
							this.setHistor(this.videoList[this.current].courseId, this.videoList[this.current]
								.courseDetailsId)

							this.initShare()
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
			setHistor(courseId, courseDetailsId) {
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
				this.$Request.sysLog('在【观剧】页面进行一次“滑动”操作')
				//拿出当前的swiper索引
				let current = Number(e.detail.current)
				// 根据courseDetailsId在meunList中找到这个数据的下标
				let indexs = -1;
				//考虑向上滑动的时候
				console.log(current, '滑动的current')
				console.log(this.current, '当前的current')
				console.log(this.videoList, '当前的current')
				//没有三条的时候
				if (current == 1 && this.current == 0) {

				}
				// 有三条的时候
				if (current == 2 && this.current == 0) {
					//向上滑到头的时候先拿第一条数据的courseDetailsId
					let courseDetailsId = this.videoList[0].courseDetailsId
					indexs = this.meunList.findIndex(item => item.courseDetailsId === courseDetailsId);
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
					indexs = this.meunList.findIndex(item => item.courseDetailsId === courseDetailsId);
					//记录当前数据在meunList中的下标
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
				// this.courseId = this.videoList[current].courseId
				// this.courseDetailsId = this.videoList[current].courseDetailsId
				//获取当前播放的视频在菜单数组中的位置
				let scrollIndex = this.meunList.findIndex(ite => ite.courseDetailsId == this.videoList[current]
					.courseDetailsId)
				//记录当前播放位置
				this.scrollIntoView = 'video' + scrollIndex
				//控制播放
				this.startPlay(current)
				//设置轮播图索引
				this.current = current
				//插入历史记录
				this.setHistor(this.videoList[this.current].courseId, this.videoList[this.current].courseDetailsId)
			},
			startPlay(current) {
				if (this.videoContext) { //判断之前是否有视频的上下文
					this.videoContext.stop()
					this.videoContext = null
				}
				let currVideo = this.videoList[current];
				let numIdCurr = this.videoList[current].courseDetailsId
				this.menuIdx = this.meunList.findIndex(menu => menu.courseDetailsId === numIdCurr);
				if (this.videoList[current].videoUrl) { //已经购买可直接播放

					this.$Request.sysLog('在【观剧】页面观看剧 id：' + currVideo.courseId + '，集 id：' + numIdCurr)
					// 播放时记录当前播放的id
					this.videoContextId = 'myVideo' + numIdCurr
					this.videoContext = uni.createVideoContext(this.videoContextId, this)
					this.$nextTick(() => {
						//播放当前的
						this.videoContext.play()
						this.$forceUpdate()
					})

				} else if (currVideo.price <= this.moneyNum) {

					// 如果看点余额足够
					this.showLoading = true;

					let that = this;
					let data = {
						courseId: currVideo.courseId,
						courseDetailsId: currVideo.courseDetailsId,
					}
					// #ifdef MP-WEIXIN
					let platform = 'wechat'
					// #endif

					// 下订单
					this.$Request.getT('/app/order/insertCourseOrders', data).then(res => {
						if (res.code == 0) {
							// 兑换
							that.$Request.postT("/app/order/payOrders", {
								orderId: res.data.orders.ordersId,
								tenant: config.TENANT,
								platform: platform,
							}).then(red => {
								if (red.code == 0) {
									that.$Request.sysLog('在【观剧】页面自动解锁剧 id：' + currVideo.courseId +
										'，集 id：' + numIdCurr)
									let userId = uni.getStorageSync('userId');
									let addJifen = {
										content: '解锁剧集增加: ' + currVideo.price + '积分',
										classify: 7,
										orderId: res.data.orders.ordersId,
										type: 1,
										num: currVideo.price,
										userId
									}
									that.$Request.postJson('/app/integral/creditsExchange/v2', addJifen)
										.then(ref => {
											this.showLoading = false;
											this.getMyMoney()
											that.getDataList(currVideo.courseId, currVideo
												.courseDetailsId, true, () => {
													that.showConfirm = false;
													that.payConfirm = {};
													that.showPay = false;
												});
										})
								} else {
									uni.showToast({
										title: res.msg,
										icon: 'none'
									})
								}
							});
						} else {
							this.showLoading = false;
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					})

				} else {
					//没有视频链接则表示没有权限，需要购买 弹出购买弹窗
					this.showPay = true
					this.needAd = true;
					this.$Request.sysLog('在【观剧】页面弹出解锁窗体剧 id：' + currVideo.courseId + '，集 id：' + numIdCurr)
				}
			},
			//播放时的回掉
			videoPlay(videoId) {
				let that = this
				// #ifdef H5
				this.timer = setTimeout(() => {
					that.showControls = false;
					clearTimeout(that.timer);
				}, 3000)
				// #endif

				// #ifndef H5
				this.timer = setTimeout(() => {
					that.showControls = false;
					clearTimeout(that.timer);
				}, 5000)
				// #endif
			},
			//暂停时的回掉
			videoPause() {
				this.showControls = true;
				clearTimeout(this.timer);
				return;
			},
			//监听视频播放完成
			ended() {
				let current = this.current;
				// 查看是否有下一个视频
				if (this.current < this.videoList.length) {
					// 有则自动翻页
					if (current == 2) {
						current = 0
					} else {
						current += 1
					}
				}

				let next = {
					'detail': {
						current,
					}
				}
				this.change(next)

				// 播放完成，完成播放任务 812
				let userId = uni.getStorageSync('userId');
				this.$Request.postT("/app/integral/completeTask/812", {
					userId
				});
				// this.startPlay(this.current)
			},
			//返回
			goBack() {
				const pages = getCurrentPages();
				if (pages.length > 1) {
					uni.navigateBack()
				} else if (pages.length == 1) {
					uni.switchTab({
						url: '/pages/index/index'
					})
				} else {
					uni.switchTab({
						url: '/pages/index/index'
					})
				}
			},
		},
	}
</script>

<style lang="scss" scoped>
	page {
		background-color: #000;
	}

  ::v-deep.u-drawer-bottom {
		background-color: #202020 !important;
	}

	.navigate-back {
		height: 32rpx;
		z-index: 100;
		position: absolute;
		color: #FFFFFF;
		top: 64rpx;
		font-size: 32rpx;
		left: 24rpx;
		line-height: 36rpx;

		span:nth-of-type(1) {
			margin-left: 64rpx;
		}
	}

	.episode-list {
		width: 750rpx;
		height: auto;
		background: #FFFFFF;
		border-radius: 32rpx 32rpx 0rpx 0rpx;

		.title-box {
			width: 702rpx;
			height: 248rpx;
			padding: 48rpx 0 32rpx 0;
			box-shadow: inset 0rpx -1rpx 0rpx 0rpx #EFEFEF;

			.list-title {
				height: 168rpx;

				.list-main-title {
					height: auto;
					font-size: 32rpx;
					font-family: PingFangSC, PingFang SC;
					font-weight: 500;
					color: #333333;
					line-height: 48rpx;
					margin-top: 30rpx;
					margin: 0 0 24rpx 16rpx;
				}

				.list-sub-title {
					margin-left: 16rpx;
					height: 36rpx;
					font-size: 24rpx;
					font-family: PingFangSC, PingFang SC;
					font-weight: 400;
					color: #999999;
					line-height: 36rpx;
				}

				.zhui {
					width: 126rpx;
					height: 48rpx;
					border-radius: 24rpx;

					font-size: 24rpx;
					font-family: PingFangSC, PingFang SC;
					font-weight: 400;
					line-height: 36rpx;

					position: absolute;
					right: 24rpx;
				}

				.shoucang {
					background: linear-gradient(135deg, #FE9001 0%, #FE7511 100%);
					color: #FFFFFF;
				}

				.has-shoucang {
					background-color: #F7F7F7;
					color: #000;
				}
			}
		}

		.list-box {
			width: 100%;
			height: auto;
			padding: 24rpx 4rpx 24rpx 24rpx;

			.list-item-box {
				width: 100rpx;
				height: 100rpx;
				background: #F7F7F7;
				border-radius: 8rpx;
				border: 1rpx solid rgba(153, 153, 153, 0.08);
				margin-right: 20rpx;
				margin-bottom: 20rpx;

				font-size: 32rpx;
				font-family: PingFangSC, PingFang SC;
				font-weight: 400;
				color: #333333;
				line-height: 48rpx;
				position: relative;

				.playing {
					width: 100%;
					height: 100%;
					background-color: rgba(0, 0, 0, 0.5);
					position: absolute;
					color: #FFFFFF;
					font-size: 28rpx;
					z-index: 100;
				}

				.item-icon {
					width: 56rpx;
					height: 24rpx;
					background: #FEDD9A;
					border-radius: 0rpx 8rpx 0rpx 8rpx;

					font-size: 16rpx;
					font-family: PingFangSC, PingFang SC;
					font-weight: 400;
					color: #7E4011;
					line-height: 16rpx;

					position: absolute;
					right: 0rpx;
					top: 0rpx;
				}
			}
		}
	}

	.swipers {
		width: 100%;
		height: 100vh;

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
			background-image: url('https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/details/btn.png');
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
			background: linear-gradient(180deg, rgba(0, 0, 0, 0.5) 0%, rgba(0, 0, 0, 0.4) 100%);
			backdrop-filter: blur(1px);

			width: 100%;
			height: auto;
			position: absolute;
			bottom: 100rpx;
			padding-left: 20rpx;
			color: #ffffff;
			font-size: 30rpx;
			z-index: 999;
			display: flex;
			align-items: center;

			.swipers-items-info-content {
				margin-top: 10rpx;
				height: 40rpx;
				font-size: 28rpx;
				font-family: PingFangSC, PingFang SC;
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
				
				.share-btn {
					width: 60rpx;
					height: 60rpx;
					border: none;
					outline: none;
					background: transparent;
					background-image: url('https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/me/share.png');
					background-repeat: no-repeat;
					background-size: cover;
				}
				
				button::after {
					border: none;
					outline: none;
				}
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

	.pay-box {
		min-height: 960rpx;
		width: 100%;
		background-color: #FFFFFF;

		.title-box {
			background-image: url('https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/details/bg.png');
			background-repeat: no-repeat;
			background-size: cover;
			height: 164rpx;
			width: 100%;

			padding-left: 24rpx;
			padding-top: 32rpx;

			// position: absolute;
			// top: 0;

			.title {
				height: 48rpx;

				span:nth-of-type(1) {
					width: 160rpx;
					height: 48rpx;
					font-size: 32rpx;
					font-family: PingFangSC, PingFang SC;
					font-weight: 500;
					color: #975507;
					line-height: 48rpx;
				}

				span:nth-of-type(2) {
					height: 48rpx;
					font-size: 32rpx;
					font-family: PingFangSC, PingFang SC;
					font-weight: 500;
					color: #FF6C0C;
					line-height: 48rpx;
				}

			}

			.title-item {
				margin-top: 16rpx;
				width: 226rpx;
				height: 56rpx;
				background: linear-gradient(90deg, #FAE8AA 0%, #FDF3BD 100%);
				box-shadow: inset 8rpx 0rpx 8rpx 0rpx rgba(255, 224, 133, 0.63);
				border-radius: 28rpx;
				opacity: 0.8;

				span:nth-of-type(1) {
					width: 120rpx;
					height: 36rpx;
					font-size: 24rpx;
					font-family: PingFangSC, PingFang SC;
					font-weight: 400;
					color: #666666;
					line-height: 36rpx;
				}

				span:nth-of-type(2) {
					height: 40rpx;
					font-size: 28rpx;
					font-family: PingFangSC, PingFang SC;
					font-weight: 400;
					color: #FF6C0C;
					line-height: 40rpx;
				}
			}

		}

		.kandian-pay-box {
			margin-bottom: 32rpx;
			width: 750rpx;
			background: #FFFFFF;
			box-shadow: 0rpx 4rpx 16rpx 0rpx rgba(0, 0, 0, 0.01);
			border-radius: 24rpx 24rpx 0rpx 0rpx;
			padding: 40rpx 24rpx 32rpx 24rpx;

			.kd-title {

				width: 702rpx;
				margin-bottom: 6rpx;

				.kd-cz-title {
					margin-left: 8rpx;
					width: 128rpx;
					height: 48rpx;
					font-size: 32rpx;
					font-family: PingFangSC, PingFang SC;
					font-weight: 500;
					color: #333333;
					line-height: 48rpx;
				}
			}

			.pay-item {
				margin-top: 32rpx;
				height: 240rpx;
				background-repeat: no-repeat;
				background-size: cover;
				width: 342rpx;
				position: relative;

				.float {
					width: 96rpx;
					height: 44rpx;
					background: linear-gradient(270deg, #FE8E16 0%, #FF3D01 100%);
					border-radius: 12rpx 12rpx 12rpx 0rpx;

					font-size: 24rpx;
					font-family: PingFangSC, PingFang SC;
					font-weight: 500;
					color: #FFFFFF;
					line-height: 44rpx;
					text-align: center;

					position: absolute;
					top: -22rpx;
				}

				.money {
					margin-top: 36rpx;
					height: 72rpx;

					.m-unit {
						margin-bottom: 6rpx;
						width: 16rpx;
						height: 40rpx;
						font-size: 40rpx;
						font-family: BebasNeue;
						color: #333333;
						line-height: 40rpx;
					}

					.m-num {
						margin-left: 8rpx;
						height: 72rpx;
						font-size: 72rpx;
						font-family: BebasNeue;
						color: #333333;
						line-height: 72rpx;
					}
				}

				.kd {
					height: 40rpx;
					margin-top: 18rpx;
					font-size: 28rpx;
					font-family: PingFangSC, PingFang SC;
					font-weight: 400;
					line-height: 40rpx;

					.kd-num {
						color: #333333;
					}

					.kd-send {
						color: #FF6C0C
					}
				}

				.remark {
					text-align: center;
					margin-top: 26rpx;
					height: 40rpx;
					font-size: 28rpx;
					font-family: PingFangSC, PingFang SC;
					font-weight: 500;
					color: #A05700;
					line-height: 40rpx;
				}
			}

			.pay-item.tehui {
				background-image: url('https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/details/selected_bg.png');
			}

			.pay-item.normal {
				background-image: url('https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/details/kd_bg.png');
			}
		}
	}

	.suprise-first {
		height: 100vh;

		// position: fixed;
		.suprise-title {
			padding-top: 10rpx;
			width: 582rpx;
			height: 90rpx;
			font-size: 68rpx;
			font-family: WenYiHei, PingFangSC;
			font-weight: normal;
			color: #FFFFFF;
			line-height: 90rpx;
			text-shadow: 0px 0px 32px rgba(255, 234, 131, 0.86);
			text-align: center;
		}

		.suprise-item {
			background-image: url('https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/details/coupon/fst-bg.png');
			background-repeat: no-repeat;
			background-size: cover;
			height: 658rpx;
			width: 800rpx;
			padding-top: 150rpx;
			padding-right: 15rpx;

			.suprise-item-title {

				margin-top: 8rpx;
				padding: 12rpx 16rpx;
				width: 346rpx;
				height: 104rpx;
				background: #FFECD7;
				border-radius: 8rpx;
				border: 2rpx solid #FFD7BA;

				font-size: 28rpx;
				font-family: PingFangSC, PingFang SC;
				font-weight: 500;
				color: #FF5200;
				line-height: 40rpx;

				text {
					height: 80rpx;
					display: -webkit-box;
					-webkit-box-orient: vertical;
					-webkit-line-clamp: 2;
					overflow: hidden;
					text-overflow: ellipsis;
				}

			}
		}

		.suprise-btn {
			background-image: url('https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/details/coupon/btn1-bg.png');
			background-repeat: no-repeat;
			background-size: cover;
			width: 406rpx;
			height: 132rpx;
			margin-top: 70rpx;
			margin-bottom: 46rpx;

			font-size: 40rpx;
			font-family: PingFangSC, PingFang SC;
			font-weight: 500;
			color: #FFFFFF;
			line-height: 60rpx;
		}
	}

	.suprise-snd {
		background-image: url('https://duanju-dev.oss-cn-hangzhou.aliyuncs.com/front/uniapp-duanju/static/images/details/coupon/2nd-bg.png');
		background-repeat: no-repeat;
		background-size: cover;
		width: 526rpx;
		height: 602rpx;

		.snd-title {
			margin-top: 20rpx;
			width: 336rpx;
			height: 48rpx;
			font-size: 48rpx;
			font-family: WenYiHei, PingFangSC;
			font-weight: normal;
			color: #861700;
			line-height: 48rpx;
			text-shadow: 0px 0px 32px rgba(255, 234, 131, 0.86);
		}

		.snd-sub-title {
			margin-top: 16rpx;
			width: 252rpx;
			height: 40rpx;
			font-size: 28rpx;
			font-family: PingFangSC, PingFang SC;
			font-weight: 500;
			color: #861700;
			line-height: 40rpx;
			text-shadow: 0px 0px 32px rgba(255, 234, 131, 0.86);
		}

		.snd-content-box {
			margin-top: 36rpx;

			.snd-content-item {
				padding: 0 72rpx;

				.snd-content-item-title {
					margin-left: 16rpx;
					width: 254rpx;
					height: 112rpx;
					background: #FFD183;
					border-radius: 16rpx;
					padding: 0 14rpx;

					font-size: 28rpx;
					font-family: PingFangSC, PingFang SC;
					font-weight: 500;
					color: #925101;
					line-height: 40rpx;

					text {
						height: 80rpx;
						display: -webkit-box;
						-webkit-box-orient: vertical;
						-webkit-line-clamp: 2;
						overflow: hidden;
						text-overflow: ellipsis;
					}
				}
			}

			.snd-content-jifen {
				width: 100%;
				padding: 0 28rpx;
				margin-top: 12rpx;

				.snd-content-jifen-item {
					width: 235rpx;
					text-align: center;

					span:nth-of-type(1) {
						height: 66rpx;
						font-size: 48rpx;
						font-family: PingFangSC, PingFang SC;
						font-weight: 500;
						color: #FFFFFF;
						line-height: 66rpx;
					}

					span:nth-of-type(2) {
						width: 112rpx;
						height: 40rpx;
						font-size: 28rpx;
						font-family: PingFangSC, PingFang SC;
						font-weight: 500;
						color: #FFFFFF;
						line-height: 40rpx;
					}
				}


			}

		}

		.snd-btn {
			margin-top: 62rpx;
			width: 426rpx;
			height: 88rpx;
			background: linear-gradient(180deg, #FC3B17 0%, #F48038 100%);
			border-radius: 16rpx;

			font-size: 40rpx;
			font-family: PingFangSC, PingFang SC;
			font-weight: 500;
			color: #FFFFFF;
			line-height: 60rpx;
		}
	}
</style>