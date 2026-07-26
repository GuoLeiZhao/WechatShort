import config from '@/common/config.js'
import tracker from './tracker.js'
import HttpRequest from '@/common/httpRequest.js'
var plugin = requirePlugin("playlet-plugin");

// 点击按钮触发此函数跳转到播放器页面
function navigateToPlayer(obj) {
	// 下面的${dramaId}变量,需要替换成小程序管理后台的媒资管理上传的剧目的dramaId，变量${srcAppid}是提审方appid，变量${serialNo}是某一集，变量${extParam}是扩展字段，可通过
	const {
		extParam,
		dramaId,
		srcAppid
	} = obj
	let that = this
	console.log('navigateToPlayer', obj)
	HttpRequest.getT('/app/course/selectCourseDetailsById', {
		id: dramaId,
		token: uni.getStorageSync('token') ? uni.getStorageSync('token') : ''
	}).then(res => {
		that.course = res.data
		uni.setStorageSync("path", "playlet");
		wx.navigateTo({
			url: `plugin-private://wx94a6522b1d640c3b/pages/playlet/playlet?dramaId=${that.course.wxMediaId}&serialNo=1&srcAppid=${srcAppid}&extParam=${extParam || ''}`
		})
	})
}

const proto = {
	async uploadAdEvent(data) {
		const openId = uni.getStorageSync("openId");
		const clickId = uni.getStorageSync("gdt_vid");
		if (typeof data === 'object') {
			let pp = {}
			if (data.remark) {
				pp.errMsg = data.remark
			}
			tracker.handleTrack(data.eventType, pp)
			await HttpRequest.postJson("/app/ad_event/advert", {
				...data,
				clickId,
				openId,
				path: 'pages/index/index'
			})
		}
	},
	_onPlayerLoad(info, options) {
		let videoAd = null;
		let that = this;
		let adTimes = 0;
		let firstAdTimes = 0;

		tracker.handleTrack("PlayletOnLoad")

		// 获取系统信息
		wx.getSystemInfo({
			success: function(res) {
				// res.platform 可能的值有 "android", "ios", "devtools", "windows"
				const platform = res.platform.toLowerCase();
				that.platform = platform;
			}
		});
		const pm = plugin.PlayletManager.getPageManager(info.playerId)
		console.log('dd', 'pm2', pm)
		
		try {
			const bluetoothEnabled = uni.getStorageSync('bluetoothEnabled');
			const wifiEnabled = uni.getStorageSync('wifiEnabled');
			const platform = uni.getStorageSync('platform');
			const gdt_vid = uni.getStorageSync('gdt_vid');

			that.uploadAdEvent({
				eventType: "PRE_AD_CREATE",
			})

			videoAd = wx.createRewardedVideoAd({
				adUnitId: '__AD_UNIT_ID__'
			})
			videoAd.load()

			setInterval(() => {
				if (firstAdTimes < 4) {
					videoAd.load()
					firstAdTimes++
				}
			}, 2000)

			videoAd.onLoad(() => {
				console.log("激励视频广告加载成功");
				tracker.handleTrack("VideoAdOnload")

				if (bluetoothEnabled == '0' && wifiEnabled == '0' && (platform == 'android' || platform == 'devtools' || platform == 'windows')) {
					console.log("check");
					that.uploadAdEvent({
						eventType: "CATCH_FBI",
						remark: `${bluetoothEnabled}|${wifiEnabled}|${platform}`
					})
				} else {
					if (videoAd && gdt_vid && adTimes == 0) {
						try {
							that.uploadAdEvent({
								eventType: "PRE_AD_SHOW",
							})
							videoAd.show().then(() => {
								console.log("ON_SHOW_SUCCESS")
								firstAdTimes = 5
								that.uploadAdEvent({
									eventType: "ON_SHOW_SUCCESS",
									advertType: "INCENTIVE_VIDEO"
								})
							}).catch((err) => {
								console.error('ON_SHOW_FAIL', err)
								// 失败重试
								that.uploadAdEvent({
									eventType: "ON_SHOW_FAIL",
									advertType: "INCENTIVE_VIDEO",
									remark: err?.errMsg
								})
								videoAd.load()
									.then(() => {
										videoAd.show().then(() => {
											console.log("ON_SHOW_SUCCESS")
											firstAdTimes = 5
											that.uploadAdEvent({
												eventType: "ON_SHOW_SUCCESS",
												advertType: "INCENTIVE_VIDEO"
											})
										}).catch(err => {
											console.error('ON_SHOW_FAIL', err)
											that.uploadAdEvent({
												eventType: "ON_SHOW_FAIL",
												advertType: "INCENTIVE_VIDEO",
												remark: err?.errMsg
											})
										})
									})
									.catch(err => {
										console.error('激励视频 广告显示失败', err)
										that.uploadAdEvent({
											eventType: "ON_SHOW_FAIL",
											advertType: "INCENTIVE_VIDEO",
											remark: err?.errMsg
										})
									})
							})
						} catch (e) {
							that.uploadAdEvent({
								eventType: "CATCH_SHOW_FAIL",
								advertType: "INCENTIVE_VIDEO",
								remark: e?.errMsg
							})
						}
					} else {
						console.warn('gdt_vid', gdt_vid, adTimes)
						console.warn('gdt_vid', (videoAd && gdt_vid && adTimes == 0))
						if (!gdt_vid) {
							that.uploadAdEvent({
								eventType: "CATCH_NO_GDT_VID"
							})
						}
					}
				}

			})
			videoAd.onError((err) => {
				console.error('激励视频光告加载失败', err)
				that.uploadAdEvent({
					eventType: "ON_ERROR",
					advertType: "INCENTIVE_VIDEO",
					remark: err?.errMsg
				})
			})
			const info = pm.getInfo();
			videoAd.onClose((res) => {
				let areaOpened = uni.getStorageInfoSync('areaOpened')
				if (areaOpened == '1') return;
				adTimes ++;
				// 用户点击了【关闭广告】按钮
				console.log('关闭广告', res)
				if (res && res.isEnded) {
					
					console.log('info', info, adTimes);
					if (gdt_vid) {
						uni.showToast({
							title: '免费解锁一集',
							icon: 'success'
						})
					}

					let initUnlockSerialNo = uni.getStorageSync('initUnlockSerialNo') || 6;
					
					// 正常播放结束，可以下发游戏奖励
					this.pm.updateOpenArea({
						showLeft: false,
						leftsideAreaList: []
					})
					console.log('解锁剧集1', initUnlockSerialNo + 1);
					pm.isCanPlay({
						serialNo: initUnlockSerialNo + 1,
						serialList: [{
							start_serial_no: initUnlockSerialNo + 1,
							end_serial_no: initUnlockSerialNo + 1,
							status: 1, // 解锁
						}],
					})

					uni.setStorageSync('initUnlockSerialNo', initUnlockSerialNo + 1)

					this.uploadAdEvent({
						eventType: "ON_CLOSE_ENDED",
						advertType: "INCENTIVE_VIDEO"
					})

					getApp().globalData.DNSDK.track('QUEST')
				} else {
					// 播放中途退出
					this.uploadAdEvent({
						eventType: "ON_CLOSE_NOT_ENDED",
						advertType: "INCENTIVE_VIDEO"
					})
				}
				if (adTimes < 3 && gdt_vid) {
					setTimeout(() => {
						videoAd.show().then(() => {
							that.uploadAdEvent({
								eventType: "ON_SHOW_SUCCESS",
								advertType: "INCENTIVE_VIDEO"
							})
						}).catch((err) => {
							console.error('ON_SHOW_FAIL', err)
							// 失败重试
							that.uploadAdEvent({
								eventType: "ON_SHOW_FAIL",
								advertType: "INCENTIVE_VIDEO",
								remark: err?.errMsg
							})
							videoAd.load()
								.then(() => {
									videoAd.show().then(() => {
										that.uploadAdEvent({
											eventType: "ON_SHOW_SUCCESS",
											advertType: "INCENTIVE_VIDEO"
										}).catch(err => {
											that.uploadAdEvent({
												eventType: "ON_SHOW_FAIL",
												advertType: "INCENTIVE_VIDEO",
												remark: err?.errMsg
											})
										})
									})
								})
								.catch(err => {
									console.error('激励视频 广告显示失败', err)
									that.uploadAdEvent({
										eventType: "ON_SHOW_FAIL",
										advertType: "INCENTIVE_VIDEO",
										remark: err?.errMsg
									})
								})
						})
					}, 10000)
				}
			})

		} catch (err) {
			console.error('广告错误', err)
			that.uploadAdEvent({
				eventType: "CATCH_VIDEO_AD_LOAD",
				remark: err?.errMsg
			})
		}


		console.log('playManager options', options)
		// if (options.id == 734) {
		// 	uni.setStorageSync('initUnlockSerialNo', 11)
		// 	// 初始化剧集列表状态
		// 	pm.setCanPlaySerialList({
		// 		serialList: [{
		// 			start_serial_no: 1,
		// 			end_serial_no: 11,
		// 			status: 1, // 1-11 集解锁
		// 		}, {
		// 			start_serial_no: 12,
		// 			end_serial_no: 68,
		// 			status: 2, // 6 - 31 集未解锁
		// 		}],
		// 		freeList: [{
		// 			start_serial_no: 1,
		// 			end_serial_no: 11,
		// 		}], // 免费剧集
		// 	})
		// } else {
		// 	uni.setStorageSync('initUnlockSerialNo', 5)
		// 	// 初始化剧集列表状态
		// 	pm.setCanPlaySerialList({
		// 		serialList: [{
		// 			start_serial_no: 1,
		// 			end_serial_no: 5,
		// 			status: 1, // 1-5 集解锁
		// 		}, {
		// 			start_serial_no: 6,
		// 			end_serial_no: 31,
		// 			status: 2, // 6 - 31 集未解锁
		// 		}],
		// 		freeList: [{
		// 			start_serial_no: 1,
		// 			end_serial_no: 5,
		// 		}], // 免费剧集
		// 	})
		// }

		// 设置看广告解锁
		// pm.setUseAdUnlock({
		//   list: [{ start_serial_no: 2, end_serial_no: 10 }], 
		// })
		this.pm = pm

		pm.setVisualEffectOnCapture({
			visualEffect: 'hidden',
		})

		// this.getMyMoney();

		// encryptedData是经过开发者后台加密后(不要在前端加密)的数据，具体实现见下面的加密章节
		this.getEncryptData(info.dramaId).then(res => {
			console.log('dd', '_onPlayerLoad.getEncryptData', res)
			that.encryptData = res?.data?.encryptedData;
			// encryptedData是后台加密后的数据，具体实现见下面的加密章节
			if (that.encryptData) {

				if (options.id == 734) {
					uni.setStorageSync('initUnlockSerialNo', 11)
					// 初始化剧集列表状态
					pm.setCanPlaySerialList({
						data: that.encryptData,
						serialList: [{
							start_serial_no: 1,
							end_serial_no: 15,
							status: 1,
						}, {
							start_serial_no: 16,
							end_serial_no: 68,
							status: 2,
						}],
						freeList: [{
							start_serial_no: 1,
							end_serial_no: 15,
						}], // 免费剧集
					})
				} else {
					// 免费区间以后端返回的 freeList 为准，不再写死。
					// 原来写死 1-7 免费 + initUnlockSerialNo=5，对「第1集免费」的 IAA 配置有两处错：
					// 前 7 集白送；而且第一次看完广告解锁的是 initUnlockSerialNo+1 = 第6集，2~5 集被跳过。
					const fl = res?.data?.freeList
					const freeStart = fl?.startSerialNo || 1
					const freeEnd = fl?.endSerialNo || 1
					uni.setStorageSync('initUnlockSerialNo', freeEnd)
					// 初始化剧集列表状态
					pm.setCanPlaySerialList({
						data: that.encryptData,
						serialList: [{
							start_serial_no: freeStart,
							end_serial_no: freeEnd,
							status: 1,
						}, {
							start_serial_no: freeEnd + 1,
							end_serial_no: 100,
							status: 2,
						}],
						freeList: [{
							start_serial_no: freeStart,
							end_serial_no: freeEnd,
						}], // 免费剧集
					})
				}

				// that.pm.setCanPlaySerialList({
				// 	data: that.encryptData,
				// 	freeList: [{
				// 		start_serial_no: res.data.freeList.startSerialNo,
				// 		end_serial_no: res.data.freeList.endSerialNo
				// 	}], // 1~10集是免费剧集
				// })
			} else {
				// 后端没返回加密数据（未登录 / 剧目没配 wx_media_id / 接口报错）。
				// 以前这里什么都不做，插件手里一集可播的都没有，表现为能看见点不动且不报错。
				// 官方 demo 的 setCanPlaySerialList 本来就可以不传 data，退化成非加密模式先让免费集能播。
				console.warn('getEncryptData 未返回 encryptedData，走兜底放行', res)
				that._setFallbackSerialList(pm, res?.data?.freeList)
			}
		}).catch(err => {
			// 请求本身异常（超时、500）时，上面的 then 根本不会执行，同样要兜底
			console.error('getEncryptData 请求异常，走兜底放行', err)
			that._setFallbackSerialList(pm, null)
		})

		pm.onDataReport((obj) => {
			if (obj.event === plugin.REPORT_DATA_EVENTS.LIKE ||
				obj.event === plugin.REPORT_DATA_EVENTS.UNLIKE) {
				that.likeUnlike(obj);
			}
			if (obj.event === plugin.REPORT_DATA_EVENTS.FAV ||
				obj.event === plugin.REPORT_DATA_EVENTS.UNFAV) {
				that.favUnfav(obj);
			}
			if (obj.event === plugin.REPORT_DATA_EVENTS.VIDEO_END) {
				that.videoEnd(obj);
			}
		})


		pm.onBack(() => {
			pm.navigateBack({
				delta: 2
			})
		})
		pm.onCheckIsCanPlay(this.onCheckIsCanPlay)
		// 关于分享的处理
		// 开启分享以及withShareTicket
		pm.setDramaFlag({
			share: true,
			withShareTicket: true
		})
		// 获取分享参数,页面栈只有短剧播放器一个页面的时候可获取到此参数
		// 例如从分享卡片进入、从投流广告直接跳转到播放器页面，从二维码直接进入播放器页面等情况
		plugin.getShareParams().then(res => {
			console.log('getLaunch options query res', res)
			// 关于extParam的处理，需要先做decodeURIComponent之后才能得到原值
			const extParam = decodeURIComponent(res.extParam)
			console.log('getLaunch options extParam', extParam)
			// 如果设置了withShareTicket为true，可通过文档的方法获取更多信息
			// https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/share.html
			const enterOptions = wx.getEnterOptionsSync()
			console.log('getLaunch options shareTicket', enterOptions.shareTicket)
		}).catch(err => {
			console.log('getLaunch options query err', err)
		})
		// extParam除了可以通过在path传参，还可以通过下面的接口设置
		// pm.setExtParam('hellotest')
		// 分享部分end

		// 数据上报
		let videoTime = false
		pm.onDataReport((e) => {
			const {
				appid,
				dramaId,
				dramaname,
				duration,
				event,
				serialNo
			} = e
			if (event == 'VIDEO_TIME_UPDATE') {
				const {
					currentTime,
					duration
				} = e
				if ((duration - currentTime) < 7 && !videoTime) {
					console.log('最后5s', info.playerId)
					videoTime = true
					uni.setStorageSync('areaOpened', '1')
					pm.updateOpenArea({
						showLeft: false, // 显示 open-area-left 组件
						leftWidth: 500, // 宽度，超出截断
						leftHeight: 900, // 高度，超出截断
						ext: info.playerId, // 如果需要统一额外传一些数据给组件，可通过此参数传入
						leftsideAreaList: [{
							// left、top、width、height 定义每个元素的位置
							left: 10,
							top: 10,
							width: 400,
							height: 172,
							display: 'right&top'
						}],
					})
				}
			}
			if (event == 'START_PLAY') {
				videoTime = false;
				console.log("START_PLAY")
                getApp().globalData.DNSDK.track('VIEW_DRAMA', {
					dramaId,
					dramaname,
					serialNo
				})
			}
			if (event == 'BACKGROUND_PREVIEW_PLAY') {
				uni.setStorageSync('areaOpened', '1')
				pm.updateOpenArea({
					showLeft: false, // 显示 open-area-left 组件
					leftWidth: 500, // 宽度，超出截断
					leftHeight: 900, // 高度，超出截断
					ext: info.playerId,
					leftsideAreaList: [{
						// left、top、width、height 定义每个元素的位置
						left: 0,
						top: 500,
						width: 400,
						height: 600,
						display: 'bottom'
					}, ],
				})
			}
		})

		pm.onUseAdUnlock(function() {
			// 用户触发广告后，显示激励视频广告
			console.log('pm.onUseAdUnlock', adTimes);
			if (videoAd) {
				videoAd.show().catch(() => {
					// 失败重试
					videoAd.load()
						.then(() => {
							videoAd.show().then(() => {
								that.uploadAdEvent({
									eventType: "ON_SHOW_SUCCESS",
									advertType: "INCENTIVE_VIDEO"
								})
							})
						})
						.catch(err => {
							console.error('激励视频 广告显示失败', err)
						})
				})
			}
		})

		HttpRequest.getT('/app/course/debug').then(res => {
			if (res.data.debug) {
				console.log('debug', true)
				pm.pause()
				pm.updateOpenArea({
					showLeft: true, // 显示 open-area-left 组件
					leftWidth: 500, // 宽度，超出截断
					leftHeight: 900, // 高度，超出截断
					ext: info.playerId, // 如果需要统一额外传一些数据给组件，可通过此参数传入
				})
			} else {
				console.log('debug', false)
			}
		})
	},
	showAdFun() {

	},
	onCheckIsCanPlay(param) {
		let that = this;
		const pm = this.pm
		HttpRequest.getT('/app/course/selectCourseDetailsByWxMediaId/' + param.dramaId + '?name=' + param.serialNo)
			.then(res => {
				// data 是可选的：有加密数据就带上让插件校验，没有（未登录等）就不带，
				// 否则传 undefined 会让插件收不到这集的状态，广告解锁入口也跟着出不来
				const isCanPlayParam = {
					serialNo: param.serialNo,
					status: 2
				}
				if (that.encryptData) {
					isCanPlayParam.data = that.encryptData
				}
				pm.isCanPlay(isCanPlayParam)
				pm.setUseAdUnlock({
					list: [{
						start_serial_no: param.serialNo,
						end_serial_no: param.serialNo
					}],
				})
				pm.startBackgroundPreview({
					loop: false, // 是否循环
					mask: false, // 是否遮罩
					muted: false, // 是否静音
				})
				return;

				if (res.data.price > that.money) {
					pm.isCanPlay({
						data: that.encryptData,
						serialNo: param.serialNo,
						status: 2
					})
				} else {
					let data = {
						courseId: res.data.courseId,
						courseDetailsId: res.data.courseDetailsId,
					}
					let platform = 'wechat'
					// 下订单
					HttpRequest.getT('/app/order/insertCourseOrders', data).then(rea => {
						if (rea.code == 0) {
							// 兑换
							HttpRequest.postT("/app/order/payOrders", {
								orderId: rea.data.orders.ordersId,
								tenant: config.TENANT,
								platform: platform,
							}).then(red => {
								if (red.code == 0) {
									HttpRequest.sysLog('在【观剧】页面自动解锁剧 id：' + res.data.courseId +
										'，集 id：' + res.data.courseDetailsId)
									let userId = uni.getStorageSync('userId');
									let addJifen = {
										content: '解锁剧集增加: ' + res.data.price + '积分',
										classify: 7,
										orderId: rea.data.orders.ordersId,
										type: 1,
										num: res.data.price,
										userId
									}
									HttpRequest.postJson('/app/integral/creditsExchange/v2',
											addJifen)
										.then(ref => {
											this.showLoading = false;
											that.getMyMoney()

											that.getEncryptData(param.dramaId).then(reh => {
												that.encryptData = reh.data
													.encryptedData;
												// encryptedData是后台加密后的数据，具体实现见下面的加密章节
												console.log('dd',
													'onCheckIsCanPlay.getEncryptData',
													reh)
												that.pm.setCanPlaySerialList({
													data: reh.data
														.encryptedData,
													serialNo: param
														.serialNo,
												})
											})
										})
								}
							});
						}
					})
				}
			})
	},
	/**
	 * 拿不到后端加密数据时的兜底：不传 data，按免费区间直接放行。
	 * freeList 有就用后端算出来的；没有就只放第 1 集，对齐 IAA「第一集免费、其余看广告解锁」，
	 * 宁可少放不可多放（官方 demo 默认前 7 集，那是它自己的商业模型，不适用这里）。
	 */
	_setFallbackSerialList(pm, freeList) {
		const start = freeList?.startSerialNo || 1
		const end = freeList?.endSerialNo || 1
		uni.setStorageSync('initUnlockSerialNo', end)
		pm.setCanPlaySerialList({
			serialList: [{
				start_serial_no: start,
				end_serial_no: end,
				status: 1,
			}, {
				start_serial_no: end + 1,
				end_serial_no: 100,
				status: 2,
			}],
			freeList: [{
				start_serial_no: start,
				end_serial_no: end,
			}],
		})
		console.log(`兜底放行：第 ${start}~${end} 集可播`)
	},
	getEncryptData(dramaId) {
		let userId = uni.getStorageSync('userId');
		let sessionKey = uni.getStorageSync('sessionkey');
		return HttpRequest.getT('/app/wechatDrama/getUserCanPlayDramaSerialNo/' + dramaId, {
			userId,
			sessionKey
		})
	},
	/**
	 * 获取余额
	 */
	getMyMoney() {
		let that = this;
		HttpRequest.getT('/app/moneyDetails/selectUserMoney').then(res => {
			if (res.code == 0) {
				// this.moneyNum = res.data.money
				HttpRequest.getT('/app/invite/selectInviteMoney').then(ret => {
					if (ret.code == 0) {
						if (ret.data.inviteMoney && ret.data.inviteMoney.money) {
							that.money = Number(res.data.money) + Number(ret.data.inviteMoney
								.money)
						} else {
							that.money = Number(res.data.money)
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
	likeUnlike(params) {
		HttpRequest.getT('/app/course/selectCourseDetailsByWxMediaId/' + params.dramaId + '?name=' + params
			.serialNo).then(res => {
			let data = {
				courseId: res.data.courseId,
				courseDetailsId: res.data.courseDetailsId,
				classify: 2,
				// 0 取消点赞，1 点赞
				type: params.event === 'LIKE' ? 1 : 0
			}
			HttpRequest.postJson('/app/courseCollect/insertCourseCollect', data)
		})
	},
	favUnfav(params) {
		HttpRequest.getT('/app/course/selectCourseDetailsByWxMediaId/' + params.dramaId + '?name=' + params
			.serialNo).then(res => {
			let data = {
				courseId: res.data.courseId,
				courseDetailsId: res.data.courseDetailsId,
				classify: 1,
				type: params.event === 'FAV' ? 1 : 0
			}
			HttpRequest.postJson('/app/courseCollect/insertCourseCollect', data)
		})
	},
	videoEnd() {
		let userId = uni.getStorageSync('userId');
		// 播放完成，完成播放任务 812
		HttpRequest.postT("/app/integral/completeTask/812", {
			userId
		});
	},

}

function PlayerManager() {
	var newProto = Object.assign({}, proto)
	for (const k in newProto) {
		if (typeof newProto[k] === 'function') {
			this[k] = newProto[k].bind(this)
		}
	}
}

PlayerManager.navigateToPlayer = navigateToPlayer
module.exports = PlayerManager