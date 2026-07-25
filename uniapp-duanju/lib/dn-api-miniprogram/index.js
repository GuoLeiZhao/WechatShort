// 轻量版 SDK：封装 e.qq.com 用户行为上报
// 使用方式：
// const sdk = new SDK({ user_action_set_id, secret_key, appid, openid, access_token, account_id, auto_track })
// sdk.setUserUniqueId(userId)
// sdk.track('REGISTER', { userId })

class SDK {
	static _debug = false;

	static setDebug(enable) {
		SDK._debug = !!enable;
	}

	constructor(options = {}) {
		const {
			user_action_set_id,
			secret_key,
			appid,
			openid,
			access_token,
			account_id,
			auto_track = true,
		} = options;

		this.config = {
			user_action_set_id,
			secret_key,
			appid,
			openid,
			access_token,
			account_id,
			auto_track,
		};

		this.userUniqueId = null;
	}

	setOpenId(openid) {
		this.config.openid = openid;
	}

	setUserUniqueId(userUniqueId) {
		this.userUniqueId = userUniqueId ? String(userUniqueId) : null;
		if (SDK._debug) console.log('[DNSDK] setUserUniqueId', this.userUniqueId);
	}

	// actionType 例如：'REGISTER'、'PURCHASE' 等
	// extra 可包含 { userId, action_time, trace: { click_id } ... }
	track(actionType, extra = {}) {
		try {
			if (!actionType) return;

			const timestampSec = Math.floor(Date.now() / 1000);
			const action_time = extra.action_time || timestampSec;
			const click_id = (extra.trace && extra.trace.click_id) || this._getClickIdFromStorage();

			// 当 REGISTER 未显式传参时，自动填充 user_id
			const finalExtra = { ...extra };
			if (!finalExtra.user_id) {
                finalExtra.user_id = {
                    wechat_openid: this.config.openid || '',
                    wechat_app_id: this.config.appid || '',
                };
            }

			const payload = {
				account_id: this.config.account_id,
				user_action_set_id: this.config.user_action_set_id,
				actions: [
					{
						action_time,
						action_type: actionType,
						trace: click_id ? { click_id } : undefined,
						// 透传其它自定义信息（如 user_id 等）
						...this._sanitizeCustomFields(finalExtra)
					}
				]
			};

			if (SDK._debug) console.log('[DNSDK] track payload', payload);

			return this._postToEQq(payload);
		} catch (err) {
			if (SDK._debug) console.warn('[DNSDK] track error', err);
		}
	}

	_postToEQq(body) {
		return new Promise((resolve, reject) => {
			const url = 'https://api.e.qq.com/v3.0/user_actions/add';
			const timestampSec = Math.floor(Date.now() / 1000);
			const headers = {
				'access-token': this.config.access_token,
				'timestamp': String(timestampSec),
				'nonce': this._generateNonce(),
				'Content-Type': 'application/json',
				'Accept': '*/*',
			};

			uni.request({
                url,
                data: body,
                method: 'POST',
                header: headers,
                success: (res) => {
                    if (SDK._debug) console.log('[DNSDK] track response', res);
                    resolve(res.data);
                },
                fail: (e) => {
                    if (SDK._debug) console.warn('[DNSDK] track fail', e);
                    reject(e);
                }
            });
		});
	}

	_generateNonce() {
		// 简单随机串，满足接口需要
		return Math.random().toString(36).slice(2) + Math.random().toString(36).slice(2);
	}

	_getClickIdFromStorage() {
		try {
			if (typeof uni !== 'undefined' && uni.getStorageSync) {
				const v = uni.getStorageSync('gdt_vid');
				return v || '';
			}
		} catch (e) {}
		return '';
	}

	_sanitizeCustomFields(extra) {
		const cloned = { ...extra };
		delete cloned.action_time;
		if (cloned.trace && !cloned.trace.click_id) delete cloned.trace;
		return cloned;
	}
}

export { SDK };
export default SDK;


