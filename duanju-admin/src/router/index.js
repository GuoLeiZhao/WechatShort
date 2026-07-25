/**
 * 全站路由配置
 *
 * 建议:
 * 1. 代码中路由统一使用name属性跳转(不使用path属性)
 */
import Vue from 'vue'
import Router from 'vue-router'
import http from '@/utils/httpRequest'
import {isURL} from '@/utils/validate'
import {clearLoginInfo} from '@/utils'

Vue.use(Router)

// 开发环境不使用懒加载, 因为懒加载页面太多的话会造成webpack热更新太慢, 所以只有生产环境使用懒加载
const _import = require('./import-' + process.env.NODE_ENV)

// 全局路由(无需嵌套上左右整体布局)
const globalRoutes = [
	{path: '/404', component: _import('common/404'), name: '404', meta: {title: '404未找到'}},
	{path: '/login', component: _import('common/login'), name: 'login', meta: {title: '登录'}}
]

// 主入口路由(需嵌套上左右整体布局)
const mainRoutes = {
	path: '/',
	component: _import('main'),
	name: 'main',
	redirect: {name: 'home'},
	meta: {title: '主入口整体布局'},
	children: [
		// 通过meta对象设置路由展示方式
		// 1. isTab: 是否通过tab展示内容, true: 是, false: 否
		// 2. iframeUrl: 是否通过iframe嵌套展示内容, '以http[s]://开头': 是, '': 否
		// 提示: 如需要通过iframe嵌套展示内容, 但不通过tab打开, 请自行创建组件使用iframe处理!
		{path: '/home', component: _import('common/home'), name: 'home', meta: {title: '首页'}},
		{path: '/userList', component: _import('user/userList'), name: 'userList', meta: {title: '用户列表', isTab: true}},
		{path: '/allocationList',component: _import('allocation/allocationList'),name: 'allocationList',meta: {title: '配置列表', isTab: true}},
		{path: '/financeList', component: _import('finance/financeList'), name: 'financeList', meta: {title: '财务中心', isTab: true}},
		{path: '/message', component: _import('message/message'), name: 'message', meta: {title: '消息中心', isTab: true}},
		{path: '/taskConfig', component: _import('taskConfig/taskConfig'), name: 'taskConfig', meta: {title: '任务配置', isTab: true}},
		{path: '/bannerList', component: _import('banner/bannerList'), name: 'bannerList', meta: {title: '首页装修', isTab: true}},
		{path: '/mission', component: _import('mission/mission'), name: 'mission', meta: {title: '资源中心', isTab: true}},
		{path: '/orderCenter', component: _import('orderCenter/orderCenter'), name: 'orderCenter', meta: {title: '订单中心', isTab: true}},
		{path: '/system', component: _import('mission/system'), name: 'system', meta: {title: '系统任务', isTab: true}},
		{path: '/missionsye', component: _import('sysmission/missionsye'), name: 'missionsye', meta: {title: '活动派送', isTab: true}},
		{path: '/materialsList', component: _import('materials/materialsList'), name: 'materialsList', meta: {title: '帮助中心', isTab: true}},
		{path: '/missionAdd', component: _import('sysmission/missionAdd'), name: 'missionAdd', meta: {title: '发布任务', isTab: false}},
		{path: '/missionRedact',component: _import('sysmission/missionRedact'),name: 'missionRedact',meta: {title: '修改任务', isTab: false}},
		{path: '/userDetail', component: _import('user/userDetail'), name: 'userDetail', meta: {title: '用户详情', isTab: false}},
		{path: '/userDetail1', component: _import('user/userDetail1'), name: 'userDetail1', meta: {title: '用户详情', isTab: false}},
		{path: '/missionDetails',component: _import('mission/missionDetails'),name: 'missionDetails',meta: {title: '任务详情', isTab: false}},
		{path: '/fitmentList', component: _import('fitment/fitmentList'), name: 'fitmentList', meta: {title: '首页装修', isTab: false}},
		{path: '/landingPage', component: _import('landingPage/landingPage'), name: 'landingPage', meta: {title: '落地页管理', isTab: true}},
		{path: '/integral', component: _import('integral/integral'), name: 'integral', meta: {title: '积分推送', isTab: true}},
		{path: '/integralDetail',component: _import('integral/integralDetail'),name: 'integralDetail',meta: {title: '活动详情', isTab: false}},
		{path: '/campus', component: _import('campus/campus'), name: 'campus', meta: {title: '社区列表', isTab: true}},
		{path: '/missionComplain',component: _import('mission/missionComplain'),name: 'missionComplain',meta: {title: '任务投诉', isTab: true}},
		{path: '/autonym', component: _import('autonym/autonym'), name: 'autonym', meta: {title: '实名认证', isTab: true}},
		{path: '/locality', component: _import('locality/locality'), name: 'locality', meta: {title: '本地圈', isTab: true}},
		{path: '/materialLink', component: _import('locality/materialLink'), name: 'materialLink', meta: {title: '素材链接', isTab: true}},
		{path: '/viplist', component: _import('viplist/index'), name: 'viplist', meta: {title: '会员列表', isTab: true}},
		// 2023.03.09 新增
		{path: '/riderTop', component: _import('riderTop/riderTop'), name: 'riderTop', meta: {title: '邀请排行榜', isTab: true}},
		{path: '/messageZx', component: _import('message/messageZx'), name: 'messageZx', meta: {title: '注销信息', isTab: true}},
		{path: '/app', component: _import('app/app'), name: 'app', meta: { title: '升级配置', isTab: true } },
		{path: '/enter-wechat', component: _import('enter-wechat/enter-wechat'), name: 'enter-wechat', meta: { title: '企业微信管理', isTab: true } },
		// 商品管理
		{path: '/goodsList', component: _import('goods/goodsList'), name: 'goodsList', meta: {title: '商品管理', isTab: true}},
		// 订单管理
		{path: '/orderList', component: _import('order/orderList'), name: 'orderList', meta: {title: '订单管理', isTab: true}},
		{path: '/inviteUrl', component: _import('activity/invite'), name: 'invite', meta: {title: '看点赠送', isTab: true}},

	],
	beforeEnter (to, from, next) {
		let token = Vue.cookie.get('token')
		if (!token || !/\S/.test(token)) {
			clearLoginInfo()
			next({name: 'login'})
		}
		next()
	}
}

const router = new Router({
	mode: 'history', //  hash
	scrollBehavior: () => ({y: 0}),
	isAddDynamicMenuRoutes: false, // 是否已经添加动态(菜单)路由
	routes: globalRoutes.concat(mainRoutes)
})

router.beforeEach((to, from, next) => {
	// 添加动态(菜单)路由
	// 1. 已经添加 or 全局路由, 直接访问
	// 2. 获取菜单列表, 添加并保存本地存储
	if (router.options.isAddDynamicMenuRoutes || fnCurrentRouteType(to, globalRoutes) === 'global') {
		next()
	} else {
		http({
			url: http.adornUrl('/sys/menu/nav'),
			method: 'get',
			params: http.adornParams()
		}).then(({data}) => {
			if (data && data.code === 0) {
				fnAddDynamicMenuRoutes(data.menuList)
				router.options.isAddDynamicMenuRoutes = true
				sessionStorage.setItem('menuList', JSON.stringify(data.menuList || '[]'))
				sessionStorage.setItem('permissions', JSON.stringify(data.permissions || '[]'))
				next({...to, replace: true})
			} else {
				sessionStorage.setItem('menuList', '[]')
				sessionStorage.setItem('permissions', '[]')
				next()
			}
		}).catch((e) => {
			console.log(`%c${e} 请求菜单列表和权限失败，跳转至登录页！！`, 'color:blue')
			router.push({name: 'login'})
		})
	}
})

/**
 * 判断当前路由类型, global: 全局路由, main: 主入口路由
 * @param {*} route 当前路由
 */
function fnCurrentRouteType (route, globalRoutes = []) {
	var temp = []
	for (var i = 0; i < globalRoutes.length; i++) {
		if (route.path === globalRoutes[i].path) {
			return 'global'
		} else if (globalRoutes[i].children && globalRoutes[i].children.length >= 1) {
			temp = temp.concat(globalRoutes[i].children)
		}
	}
	return temp.length >= 1 ? fnCurrentRouteType(route, temp) : 'main'
}

/**
 * 添加动态(菜单)路由
 * @param {*} menuList 菜单列表
 * @param {*} routes 递归创建的动态(菜单)路由
 */
function fnAddDynamicMenuRoutes (menuList = [], routes = []) {
	var temp = []
	for (var i = 0; i < menuList.length; i++) {
		if (menuList[i].list && menuList[i].list.length >= 1) {
			temp = temp.concat(menuList[i].list)
		} else if (menuList[i].url && /\S/.test(menuList[i].url)) {
			menuList[i].url = menuList[i].url.replace(/^\//, '')
			var route = {
				path: menuList[i].url.replace('/', '-'),
				component: null,
				name: menuList[i].url.replace('/', '-'),
				meta: {
					menuId: menuList[i].menuId,
					title: menuList[i].name,
					isDynamic: true,
					isTab: true,
					iframeUrl: ''
				}
			}
			// url以http[s]://开头, 通过iframe展示
			if (isURL(menuList[i].url)) {
				route['path'] = `i-${menuList[i].menuId}`
				route['name'] = `i-${menuList[i].menuId}`
				route['meta']['iframeUrl'] = menuList[i].url
			} else {
				try {
					route['component'] = _import(`modules/${menuList[i].url}`) || null
				} catch (e) {
				}
			}
			routes.push(route)
		}
	}
	if (temp.length >= 1) {
		fnAddDynamicMenuRoutes(temp, routes)
	} else {
		mainRoutes.name = 'main-dynamic'
		mainRoutes.children = routes
		router.addRoutes([
			mainRoutes,
			{path: '*', redirect: {name: '404'}}
		])
		sessionStorage.setItem('dynamicMenuRoutes', JSON.stringify(mainRoutes.children || '[]'))
		console.log('\n')
		console.log('%c!<-------------------- 动态(菜单)路由 s -------------------->', 'color:blue')
		console.log(mainRoutes.children)
		console.log('%c!<-------------------- 动态(菜单)路由 e -------------------->', 'color:blue')
	}
}

export default router
