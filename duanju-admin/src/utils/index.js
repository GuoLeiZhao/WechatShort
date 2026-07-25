import Vue from 'vue'
import router from '@/router'
import store from '@/store'

/**
 * 获取uuid
 */
export function getUUID () {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, c => {
    return (c === 'x' ? (Math.random() * 16 | 0) : ('r&0x3' | '0x8')).toString(16)
  })
}

/**
 * 是否有权限
 * @param {*} key
 */
export function isAuth (key) {
  return JSON.parse(sessionStorage.getItem('permissions') || '[]').indexOf(key) !== -1 || false
}

/**
 * 树形数据转换
 * @param {*} data
 * @param {*} id
 * @param {*} pid
 */
export function treeDataTranslate (data, id = 'id', pid = 'parentId') {
  var res = []
  var temp = {}
  for (var i = 0; i < data.length; i++) {
    temp[data[i][id]] = data[i]
  }
  for (var k = 0; k < data.length; k++) {
    if (temp[data[k][pid]] && data[k][id] !== data[k][pid]) {
      if (!temp[data[k][pid]]['children']) {
        temp[data[k][pid]]['children'] = []
      }
      if (!temp[data[k][pid]]['_level']) {
        temp[data[k][pid]]['_level'] = 1
      }
      data[k]['_level'] = temp[data[k][pid]]._level + 1
      temp[data[k][pid]]['children'].push(data[k])
    } else {
      res.push(data[k])
    }
  }
  return res
}

/**
 * 清除登录信息
 */
export function clearLoginInfo () {
  Vue.cookie.delete('token')
  store.commit('resetStore')
  router.options.isAddDynamicMenuRoutes = false
}

export function copyText(copyText) {
	const text = document.createElement('input'); // 创建节点
	text.setAttribute('readonly', 'readonly');
	text.value = copyText; // 赋值
	document.body.appendChild(text); // 插入节点
	text.setSelectionRange(0, text.value.length);
	text.select(); // 选中节点
	document.execCommand('copy'); // 执行浏览器复制方法
	if (document.body.removeChild(text)) {
		this.$message({ type: 'success', message: '复制成功' })
	} else {
		this.$message({ type: 'error', message: '复制失败' })
	}
}


export const clearData = (data) => {
	if (typeof data !== 'object') return {}
	const p = JSON.parse(JSON.stringify(data));
	const newPar = {};
	const keyList = Object.keys(p);
	keyList.forEach(v => {
		// 如果对象属性的值不为空，就保存该属性（这里我做了限制，如果属性的值为0，保存该属性。如果属性的值全部是空格，属于为空。
		if ((p[v] === 0 || p[v] === false || p[v]) && p[v].toString().replace(/(^\s*)|(\s*$)/g, '') !== '') {
		newPar[v] = p[v]
		}
	})
	return newPar
}
