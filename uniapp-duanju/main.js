import Vue from 'vue'
import App from './App'


import HttpRequest from './common/httpRequest'
import HttpCache from './common/cache'
import queue from './common/queue'
// import chargeDialog from './components/charge-dialog/charge-dialog.vue'

import wechatMP from './common/sdk/wechatMP.js';
import pageTrackerMixin from "./common/pageTrackerMixin";
// Vue.component('charge-dialog',chargeDialog)

Vue.config.productionTip = false
Vue.prototype.$Request = HttpRequest;
Vue.prototype.$queue = queue;
Vue.prototype.$wechatMP = wechatMP;

Vue.prototype.$Sysconf = HttpRequest.config;
Vue.prototype.$SysCache = HttpCache;

App.mpType = 'app'


// 引入全局uView
import uView from "uview-ui";
Vue.use(uView);

Vue.mixin(pageTrackerMixin) // 全局混入

const app = new Vue({
    ...App
})

// http拦截器，将此部分放在new Vue()和app.$mount()之间，才能App.vue中正常使用
import httpInterceptor from '@/common/http.interceptor.js'
Vue.use(httpInterceptor, app)

// http接口API集中管理引入部分
import httpApi from '@/common/http.api.js'
Vue.use(httpApi, app)

// #ifdef H5

var originalOpen = XMLHttpRequest.prototype.open; // 保存原始 open 函数
    
function interceptRequests() {
	
	XMLHttpRequest.prototype.open = function(method, url, async, user, pass) {
		if (0 < url.indexOf('h-gy.getui.net')){
			// 在这里修改 URL
			url = url.replace('h-gy.getui.net', 'your-api-domain.com/getui');
		}
		
		if(0 < url.indexOf('h-gy.getui.net')){
			url = url.replace('api.next.bspapp.com', 'your-api-domain.com/bspapp');
		}
		// 调用原始的 open 方法
		return originalOpen.call(this, method, url, async == undefined ? true : async , user, pass);
	};
}

window.addEventListener('DOMContentLoaded', interceptRequests);

// #endif
app.$mount()
