// 如果没有通过拦截器配置域名的话，可以在这里写上完整的URL(加上域名部分)
// let hotSearchUrl = 'http://192.168.1.16:8080/sqx_fast/';
// let indexUrl = 'http://192.168.1.16:8080/sqx_fast/';

// 此处第二个参数vm，就是我们在页面使用的this，你可以通过vm获取vuex等操作，更多内容详见uView对拦截器的介绍部分：
// https://uviewui.com/js/http.html#%E4%BD%95%E8%B0%93%E8%AF%B7%E6%B1%82%E6%8B%A6%E6%88%AA%EF%BC%9F
const install = (Vue, vm) => {
	let wxLogin = (params = {}) => vm.$u.get('app/Login/wxLogin', params); //微信小程序登陆
	let insertWxUser = (params = {}) => vm.$u.post('app/Login/insertWxUser', params); //小程序登录新增或修改个人信息
	let selectPhone = (params = {}) => vm.$u.post('app/Login/selectPhone', params); //获取手机号
	let userVip = (params = {}) => vm.$u.get('app/UserVip/selectUserVip', params); //用户会员信息
	let userinfo = (params = {}) => vm.$u.get('app/user/selectUserById', params); //个人信息
	let updateUser = (params = {}) => vm.$u.post('app/user/updateUser', params); //修改个人信息

	let bannerList = (params = {}) => vm.$u.get('banner/selectBannerList', params); //轮播图
	let gaidList = (params = {}) => vm.$u.get('app/banner/clickBanner', params); //金刚图列表
	let msg = (params = {}) => vm.$u.get('app/message/selectMessage', params); //公告
	let selectBannerPage = (params = {}) => vm.$u.get('app/banner/selectBannerPage', params); //剧情壁纸

	let courseClass = (params = {}) => vm.$u.get('app/courseClassification/selectClassification', params); //资源分类
	let courseList = (params = {}) => vm.$u.get('app/course/selectCourse', params); //资源列表
	let courseDet = (params = {}) => vm.$u.get('app/course/selectCourseById', params); //资源详情
	let courseComment = (params = {}) => vm.$u.get('app/courseComment/selectCourseComment', params); //获取资源评论列表
	let insertComment = (params = {}) => vm.$u.post('app/courseComment/insertCourseComment', params); //发表评论
	let courseCollect = (params = {}) => vm.$u.post('app/courseCollect/insertCourseCollect', params); //收藏资源
	let courseOrder = (params = {}) => vm.$u.get('/app/order/insertCourseOrders', params); //获取订单号
	let updateGood = (params = {}) => vm.$u.get('/app/courseComment/updateGoodsNum', params); //获取点赞

	let SearchList = (params = {}) => vm.$u.get('app/appSearchController/selectAppSearchNum', params); //热搜记录
	let SearchDet = (params = {}) => vm.$u.get('app/appSearchController/deleteAppSearch', params); //删除历史记录
	let search = (params = {}) => vm.$u.get('app/course/selectCourseTitle', params); //搜索
	let searchs = (params = {}) => vm.$u.get('app/course/selectCourseTitles', params); //搜索
	// app/course/selectCourse

	// 学习
	let latelyCourse = (params = {}) => vm.$u.get('app/CourseUser/selectLatelyCourse', params); //最近学习
	let selectCourse = (params = {}) => vm.$u.get('app/CourseUser/selectCourseUser', params); //已购资源
	let collectList = (params = {}) => vm.$u.get('app/courseCollect/selectByUserId', params); //资源收藏列表

	// 我的
	let orderList = (params = {}) => vm.$u.get('app/order/selectOrderByUserId', params); //我的订单
	let inviter = (params = {}) => vm.$u.get('app/invite/selectInviteByUserIdLists', params); //我的邀请
	let moneyList = (params = {}) => vm.$u.get('app/invite/queryUserMoneyDetails', params); //钱包明细
	let queryInviter = (params = {}) => vm.$u.get('app/invite/selectInviteMoney', params); //邀请战绩
	let vipDet = (params = {}) => vm.$u.get('app/VipDetails/selectVipDetails', params); //会员价格列表
	let VipOrders = (params = {}) => vm.$u.get('app/order/insertVipOrders', params); //购买会员
	let integral = (params = {}) => vm.$u.get('app/integral/selectByUserId', params); //查看用户积分
	let integralDet = (params = {}) => vm.$u.get('app/integral/details', params); //查看积分获取列表
	let message = (params = {}) => vm.$u.get('app/message/selectMessageByUserId', params); //查询用户消息
	let wxPay = (params = {}) => vm.$u.post('app/wxPay/wxPayJsApiOrder', params); //微信支付

	let userMoney = (params = {}) => vm.$u.get('app/invite/selectUserMoney', params); //查看钱包
	let cashMoney = (params = {}) => vm.$u.get('app/cash/cashMoney', params); //申请提现
	let selectPay = (params = {}) => vm.$u.get('app/cash/selectPayDetails', params); //提现记录
	let moneyDet = (params = {}) => vm.$u.get('app/cash/queryUserMoneyDetails', params); //钱包明细
	let type = (params = {}) => vm.$u.get('app/common/type', params); //钱包明细


	let help = (params = {}) => vm.$u.get('app/helpWord/selectHelpList', params); //帮助中心 
	let helpDet = (params = {}) => vm.$u.get('app/helpWord/selectHelpWordDetails', params); //帮助中心 详情
	
	//获取视频
	let selectCourseDetailsById = (params = {}) => vm.$u.get('app/course/selectCourseDetailsById', params);

	let log = (data) => vm.$u.post('app_log', {data});
	// 将各个定义的接口名称，统一放进对象挂载到vm.$u.api(因为vm就是this，也即this.$u.api)下
	vm.$u.api = {
		wxLogin,
		insertWxUser,
		selectPhone,
		userVip,
		userinfo,
		updateUser,

		bannerList,
		gaidList,
		msg,
		selectBannerPage,
		courseClass,
		courseList,
		courseDet,
		courseComment,
		insertComment,
		collectList,
		updateGood,

		SearchList,
		SearchDet,
		search,
		searchs,

		courseCollect,
		courseOrder,
		latelyCourse,
		selectCourse,

		orderList,
		inviter,
		queryInviter,
		vipDet,
		integral,
		integralDet,
		message,
		VipOrders,
		wxPay,
		moneyList,
		userMoney,
		cashMoney,
		selectPay,
		moneyDet,
		type,

		help,
		helpDet,
		selectCourseDetailsById,
		
		log
	};
}

export default {
	install
}
