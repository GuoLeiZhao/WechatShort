'use strict';
exports.main = async (event, context) => {
	// const apiHost = "http://your-server-ip:8183/sqx_fast"
	const apiHost = "https://your-admin-domain.com/sqx_fast"
	
	// event里包含着客户端提交的参数
	const res = await uniCloud.getPhoneNumber({
		appid: '__UNI__EEA2A90', // 替换成自己开通一键登录的应用的DCloud appid
		provider: 'univerify',
		access_token: event.access_token,
		openid: event.openid
	})
	console.log(res); // res里包含手机号
	// res :
	// 	{
	// 		code: 0,
	// 		errCode: 0,
	// 		errMsg: '',
	// 		success: true,
	// 		phoneNumber: '15088656986'
	// 	}

	// 执行用户信息入库等操作，正常情况下不要把完整手机号返回给前端
	// 如果数据库不在uniCloud上，可以通过 uniCloud.httpclient API，将手机号通过http方式传递给其他服务器的接口，详见：https://doc.dcloud.net.cn/uniCloud/cf-functions?id=httpclient
	
	const loginRes = await uniCloud.httpclient.request(apiHost + '/app/Login/uniappLoginByPhone?phone=' + res.phoneNumber, {
		method:"POST",
		dataType:"json",
	})
	
	
	return loginRes;
}