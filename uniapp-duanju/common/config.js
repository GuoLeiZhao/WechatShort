// 后端服务地址（部署时改成你自己的域名）
const ROOTPATH1 = "https://wefly.work/sqx_fast";
const ROOTPATH = "https://wefly.work/sqx_fast";   // 后台服务域名
const ROOTPATH2 = "https://wefly.work";           // 后台服务域名（不带 /sqx_fast）

const TENANT = 'app1'          // 租户标识，对应后端 application.yml 的 wx.miniapp name
const offerId = '1450217844'   // 巨量引擎 offerId（用抖音投放才需要，可按需修改）

module.exports = {
	APIHOST: ROOTPATH,
	APIHOST1: ROOTPATH1,
	APIHOST2: ROOTPATH2,
	TENANT: TENANT,
	offerId: offerId,
};
