<template>
	<view class="content">
		<u-sticky :enable="enable">
			<view class="search-box">
				<u-search style="width: 100%;" placeholder="搜索更多资源" :focus="true" v-model="keyword" :show-action="true"
					:animation="true" action-text="取消" @custom="goBack()" @search="doSearch(false)"></u-search>
			</view>
		</u-sticky>
		<view class="search-keyword" v-if="isSearch">
			<view class="keyword-block" v-if="hotKeywordList.length !=0">
				<view class="keyword-list-header">
					<view>热搜</view>
					<view>
						<image @tap="hotToggle" :src="'/static/images/index/attention'+forbid+'.png'"></image>
					</view>
				</view>
				<view class="keyword" v-if="forbid==''">
					<view v-for="(keyword,index) in hotKeywordList" @tap="doSearchs(keyword)" :key="index"
						v-if="keyword">
						{{keyword}}
					</view>
				</view>
				<view class="hide-hot-tis" v-else>
					<view>当前搜热已隐藏</view>
				</view>
			</view>
			<view class="keyword-block" v-if="oldKeywordList.length>0">
				<view class="keyword-list-header">
					<view>历史记录</view>
					<view>
						<image @tap="oldDelete" src="/static/images/index/delete.png"></image>
					</view>
				</view>
				<view class="keyword">
					<view v-for="(keyword,index) in oldKeywordList" @tap="doSearch(keyword)" :key="index"
						v-if="keyword">
						{{keyword}}
					</view>
				</view>
			</view>
		</view>
		<view class="search-list flex align-center justify-center" v-else>
			<view class="search-list-box flex align-center justify-between flex-wrap">
				<swiper class="swiper" :indicator-dots="true" :autoplay="true" interval="5000" duration="500"
					:circular="true" style="width: 100%;height: 350rpx;margin-bottom: 20rpx;">
					<swiper-item v-for="(item,index) in swiperList" :key='index' @tap="goPage(item.url)">
						<image :src="item.imageUrl" mode="scaleToFill"
							style="width: 100%;height: 100%;border-radius: 24rpx;"></image>
					</swiper-item>
				</swiper>
				<videoList @success="posterSuccess" :list="keywordList" />
			</view>

		</view>

		<empty title="暂无视频" v-if="isSearch == false && keywordList.length==0" />
	</view>
</template>

<script>
	import videoList from '../../../components/videoList/videoList.vue'
	import empty from '../../../components/empty.vue'
	export default {
		components: {
			empty,
			videoList
		},
		data() {
			return {
				swiperList: [], //轮播图列表
				enable: true,
				defaultKeyword: "",
				keyword: "",
				oldKeywordList: [], //历史记录
				hotKeywordList: [], //热搜
				keywordList: [], //搜索列表
				forbid: '',
				isShowKeywordList: false,
				limit: 10,
				page: 1,
				noData: true,
				count: 0,
				isSearch: true,
			}
		},
		onShow() {
			this.enable = true
			if (uni.getStorageSync('moreSearch')) {
				this.hotKeywordList = (uni.getStorageSync('moreSearch')).split(',')
			} else {
				this.hotKeywordList = []
			}
		},
		onHide() {
			this.enable = false
		},
		onLoad() {
			// this.init();
			// this.getSearchList()
			this.getBannerList()
		},
		methods: {
			//点击回调
			posterSuccess(item) {
				let userId = uni.getStorageSync('userId')
				if (userId) {
					uni.navigateTo({
						url: '/me/detail/detail?id=' + item.courseId + '&courseDetailsId=' + item.courseDetailsId
					})
				} else {
					uni.navigateTo({
						url: "/pages/login/login"
					})
				}
			},
			doSearchs(keyWord) {
				this.keyword = keyWord
				this.doSearch(false)
			},
			// 跳转资源详情
			goCourse(e) {
				let userId = uni.getStorageSync('userId')
				if (userId) {
					// uni.navigateTo({
					// 	url: '/pages/index/course/courseDet?id=' + e
					// });
					uni.navigateTo({
						url: '/me/detail/detail?id=' + e
					})
				} else {
					uni.navigateTo({
						url: "/pages/login/login"
					})
				}
			},
			getBannerList() {
				this.$u.api.bannerList({
					classify: '1'
				}).then(res => {
					if (res.code == 0) {
						res.data.forEach(d => {
							if (d.state == 1) {
								this.swiperList.push(d)
							}
						})

					} else {
						uni.showToast({
							title: res.msg,
							duration: 1000,
							icon: 'none'
						});
					}
				})
			},
			// 获取搜索历史
			getSearchList() {
				this.$u.api.SearchList().then(res => {
					if (res.code == 0) {
						this.hotKeywordList = res.data.allSerchName
						this.oldKeywordList = res.data.userSearchName
					}
				})
			},
			//清除历史搜索
			oldDelete() {
				uni.showModal({
					content: '确定清除历史搜索记录？',
					success: (res) => {
						if (res.confirm) {
							console.log('用户点击确定');
							this.$u.api.SearchDet().then(res => {
								if (res.code == 0) {
									this.getSearchList()
								} else {
									uni.showToast({
										title: res.msg,
										duration: 1000,
										icon: 'none'
									});
								}
							})
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				});
			},
			//执行搜索
			doSearch(keyword) {
				this.isSearch = false
				this.isShowKeywordList = true;
				this.noData = true
				let data = {
					title: this.keyword,
					limit: this.limit,
					page: this.page,
				}
				let token = uni.getStorageSync('token')
				if (token) {
					this.$u.api.search(data).then(res => {
						uni.stopPullDownRefresh()
						if (res.code == 0) {
							this.count = res.data.totalPage
							if (this.page == 1) {
								this.keywordList = res.data.list
							} else {
								this.keywordList = [...this.keywordList, ...res.data.list]
							}
						} else {
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					})
				} else {
					this.$u.api.searchs(data).then(res => {
						if (res.code == 0) {
							this.count = res.data.totalPage
							if (this.page == 1) {
								this.keywordList = res.data.list
							} else {
								this.keywordList = [...this.keywordList, ...res.data.list]
							}
						} else {
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					})
				}
			},
			// 点击取消返回首页
			goBack() {
				uni.navigateBack()
			},
			//热门搜索开关
			hotToggle() {
				this.forbid = this.forbid ? '' : '_forbid';
			},


		},
		onReachBottom: function() {
			if (this.page < this.count) {
				this.page += 1
				this.doSearch();
			}
		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.doSearch();
		},
	}
</script>
<style lang="scss" scoped>
	.search-box {
		width: 100%;
		/* background-color: rgb(242, 242, 242); */
		padding: 15upx 2.5%;
		display: flex;
		justify-content: space-between;
		// position: sticky;
		// top: 0;
		background-color: #fff;
	}

	.search-box .mSearch-input-box {
		width: 100%;
	}

	.search-box .input-box {
		width: 85%;
		flex-shrink: 1;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.search-box .search-btn {
		width: 15%;
		margin: 0 0 0 2%;
		display: flex;
		justify-content: center;
		align-items: center;
		flex-shrink: 0;
		font-size: 28upx;
		color: #fff;
		background: linear-gradient(to right, #ff9801, #ff570a);
		border-radius: 60upx;
	}

	.search-box .input-box>input {
		width: 100%;
		height: 60upx;
		font-size: 32upx;
		border: 0;
		border-radius: 60upx;
		-webkit-appearance: none;
		-moz-appearance: none;
		appearance: none;
		padding: 0 3%;
		margin: 0;
		background-color: #ffffff;
	}

	.placeholder-class {
		color: #9e9e9e;
	}

	.search-keyword {
		width: 100%;
	}

	.search-list {
		width: 100%;
		margin-top: 20rpx;

		.search-list-box {
			width: 686rpx;
			height: 100%;
		}
	}

	.keyword-list-box {
		height: calc(100vh - 110upx);
		padding-top: 10upx;
		/* border-radius: 20upx 20upx 0 0; */
		/* background-color: #fff; */
	}

	.keyword-entry-tap {
		background-color: #eee;
	}

	.keyword-entry {
		width: 94%;
		height: 80upx;
		margin: 0 3%;
		font-size: 30upx;
		color: #333;
		display: flex;
		justify-content: space-between;
		align-items: center;
		border-bottom: solid 1upx #e7e7e7;
	}

	.keyword-entry image {
		width: 60upx;
		height: 60upx;
	}

	.keyword-entry .keyword-text,
	.keyword-entry .keyword-img {
		height: 80upx;
		display: flex;
		align-items: center;
	}

	.keyword-entry .keyword-text {
		width: 90%;
	}

	.keyword-entry .keyword-img {
		width: 10%;
		justify-content: center;
	}


	.keyword-block {
		padding: 10upx 0;
	}

	.keyword-block .keyword-list-header {
		width: 94%;
		padding: 10upx 3%;
		font-size: 27upx;
		color: #333;
		display: flex;
		justify-content: space-between;
	}

	.keyword-block .keyword-list-header image {
		width: 40upx;
		height: 40upx;
	}

	.keyword-block .keyword {
		width: 94%;
		padding: 3px 3%;
		display: flex;
		flex-flow: wrap;
		justify-content: flex-start;
	}

	.keyword-block .hide-hot-tis {
		display: flex;
		justify-content: center;
		font-size: 28upx;
		color: #6b6b6b;
	}

	.keyword-block .keyword>view {
		display: flex;
		justify-content: center;
		align-items: center;
		border-radius: 60upx;
		padding: 0 20upx;
		margin: 10upx 20upx 10upx 0;
		height: 60upx;
		font-size: 28upx;
		// background-color: rgb(242, 242, 242);
		background: #E6EBFF;
		color: #6b6b6b;
	}

	.centre {
		text-align: center;
		margin: 200rpx auto;
		font-size: 32rpx;

		image {
			width: 360rpx;
			height: 360rpx;
			// margin-bottom: 20rpx;
			margin: 0 auto 20rpx;
			// border: 1px dotted #000000;
		}

		.tips {
			font-size: 34rpx;
			color: #5074FF;
			margin-top: 20rpx;
		}

		.btn {
			margin: 80rpx auto;
			width: 600rpx;
			border-radius: 32rpx;
			line-height: 90rpx;
			color: #ffffff;
			font-size: 34rpx;
			background: #5074FF;
		}
	}
</style>