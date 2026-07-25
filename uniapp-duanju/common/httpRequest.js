import configdata from './config'
import cache from './cache'

module.exports = {
	sysLog: function(operation){
		return
		let that = this;
		let userId = uni.getStorageSync('userId');
		let userName = uni.getStorageSync('userName');
		
		let data = {
			userid: userId,
			username: userName,
			operation
		}
		this.postJson('/app/sys/log', data);
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
	logout: function() {
		let that = this;
		uni.removeStorageSync("token")
		uni.removeStorageSync("userId")
		uni.removeStorageSync("phone")
		uni.removeStorageSync("openid")
		uni.removeStorageSync("userName")
		uni.removeStorageSync("relation")
		uni.removeStorageSync("relation_id")
		uni.removeStorageSync("isInvitation")
		uni.removeStorageSync("zhiFuBao")
		uni.removeStorageSync("zhiFuBaoName")

		uni.showToast({
			title: '用户信息失效，请重新登录！',
			icon: 'none'
		})

	},
	post: function(url, data, header) {
		let that = this;
		header = header || "application/x-www-form-urlencoded";
		url = that.config("APIHOST") + url;
		return new Promise((succ, error) => {
			uni.request({
				url: url,
				data: data,
				method: "POST",
				header: {
					"content-type": header
				},
				success: function(result) {
					if (result.data.code == 401) {
						that.logout();
					}
					succ.call(self, result.data)
				},
				fail: function(e) {
					error.call(self, e)
				}
			})
		})
	},
	postT: function(url, data, header) {
		let that = this;
		header = header || "application/x-www-form-urlencoded";
		url = that.config("APIHOST1") + url;
		// let token = uni.getStorageSync("token");
		let token = uni.getStorageSync("token");
		if (token) {
			return new Promise((succ, error) => {
				uni.request({
					url: url,
					data: data,
					method: "POST",
					header: {
						"content-type": header,
						"token": token
					},
					success: function(result) {
						if (result.data.code == 401) {
							that.logout();
						}

						succ.call(self, result.data)
					},
					fail: function(e) {
						error.call(self, e)
					}
				})
			})
		} else {
			return new Promise((succ, error) => {
				uni.request({
					url: url,
					data: data,
					method: "POST",
					header: {
						"content-type": header,
					},
					success: function(result) {

						succ.call(self, result.data)
					},
					fail: function(e) {
						error.call(self, e)
					}
				})
			})
		}
	},

	postJson: function(url, data, header) {
		let that = this;
		header = header || "application/json";
		url = that.config("APIHOST1") + url;
		// let token = uni.getStorageSync("token");
		let token = uni.getStorageSync("token");
		if (token) {
			return new Promise((succ, error) => {
				uni.request({
					url: url,
					data: data,
					method: "POST",
					timeout: 1000,
					header: {
						"content-type": header,
						"token": token
					},
					success: function(result) {
						if (result.data.code == 401) {
							that.logout();
						}
						succ.call(self, result.data)
					},
					fail: function(e) {
						error.call(self, e)
					}
				})
			})
		} else {
			return new Promise((succ, error) => {
				uni.request({
					url: url,
					data: data,
					method: "POST",
					header: {
						"content-type": header,
					},
					success: function(result) {
						if (result.data.code == 401) {
							that.logout();
						}
						succ.call(self, result.data)
					},
					fail: function(e) {
						error.call(self, e)
					}
				})
			})
		}
	},
	getT: function(url, data, header) {
		let that = this;
		header = header || "application/x-www-form-urlencoded";
		url = that.config("APIHOST1") + url;
		// let token = uni.getStorageSync("token");
		let token = uni.getStorageSync("token");
		if (token) {
			return new Promise((succ, error) => {
				uni.request({
					url: url,
					data: data,
					method: "GET",
					header: {
						"content-type": header,
						"token": token
					},
					success: function(result) {
						if (result.data.code == 401) {
							that.logout();
						}
						succ.call(self, result.data)
					},
					fail: function(e) {
						error.call(self, e)
					}
				})
			})
		} else {
			return new Promise((succ, error) => {
				uni.request({
					url: url,
					data: data,
					method: "GET",
					header: {
						"content-type": header
					},
					success: function(result) {
						if (result.data.code == 401) {
							that.logout();
						}
						succ.call(self, result.data)
					},
					fail: function(e) {
						error.call(self, e)
					}
				})
			})
		}

	},
	posts: function(url, data, header) {
		let that = this;
		header = header || "application/x-www-form-urlencoded";
		return new Promise((succ, error) => {
			uni.request({
				url: url,
				data: data,
				method: "POST",
				header: {
					"content-type": header
				},
				success: function(result) {
					if (result.data.code == 401) {
						that.logout();
					}
					succ.call(self, result.data)
				},
				fail: function(e) {
					error.call(self, e)
				}
			})
		})
	},
	postF: function(url, data, header) {
		let that = this;
		header = header || "application/json";
		url = that.config("APIHOST") + url;
		return new Promise((succ, error) => {
			uni.request({
				url: url,
				data: data,
				method: "POST",
				header: {
					"content-type": header
				},
				success: function(result) {
					if (result.data.code == 401) {
						that.logout();
					}
					succ.call(self, result.data)
				},
				fail: function(e) {
					error.call(self, e)
				}
			})
		})
	},
	postFs: function(url, data, header) {
		let that = this;
		header = header || "application/json";
		return new Promise((succ, error) => {
			uni.request({
				url: url,
				data: data,
				method: "POST",
				header: {
					"content-type": header
				},
				success: function(result) {
					if (result.data.code == 401) {
						that.logout();
					}
					succ.call(self, result.data)
				},
				fail: function(e) {
					error.call(self, e)
				}
			})
		})
	},
	get: function(url, data, header) {
		let that = this;
		header = header || "application/x-www-form-urlencoded";
		url = that.config("APIHOST") + url;
		return new Promise((succ, error) => {
			uni.request({
				url: url,
				data: data,
				method: "GET",
				header: {
					"content-type": header
				},
				success: function(result) {
					if (result.data.code == 401) {
						that.logout();
					}
					succ.call(self, result.data)
				},
				fail: function(e) {
					error.call(self, e)
				}
			})
		})
	},
	getJd: function(url, data, header) {
		let that = this;
		header = header || "application/x-www-form-urlencoded";
		return new Promise((succ, error) => {
			uni.request({
				url: url,
				data: data,
				method: "GET",
				header: {
					"content-type": header
				},
				success: function(result) {
					if (result.data.code == 401) {
						that.logout();
					}
					succ.call(self, result.data)
				},
				fail: function(e) {
					error.call(self, e)
				}
			})
		})
	},
	get1: function(url, data, header) {
		let that = this;
		header = header || "application/json";
		url = that.config("APIHOST") + url;
		return new Promise((succ, error) => {
			uni.request({
				url: url,
				data: data,
				method: "GET",
				success: function(result) {
					if (result.data.code == 401) {
						that.logout();
					}
					succ.call(self, result.data)
				},
				fail: function(e) {
					error.call(self, e)
				}
			})
		})
	}
}