const TRACK_SERVER = "https://your-api-domain.com/sqx_fast/user_event/record"; // 你的服务端埋点上报地址
const STORAGE_KEY = "TRACK_EVENTS_CACHE";
const MAX_CACHE = 10; // 超过10条就上报
const UPLOAD_INTERVAL = 5000; // 5秒定时上报
const SESSION_KEY = "TRACK_SESSION_ID";

import config from './config'

function getCache() {
  try {
    return uni.getStorageSync(STORAGE_KEY) || [];
  } catch {
    return [];
  }
}

function setCache(events) {
  uni.setStorageSync(STORAGE_KEY, events);
}

function addEvent(event) {
  const cache = getCache();
  cache.push(event);
  setCache(cache);

  if (cache.length >= MAX_CACHE) {
    flushEvents();
  }
}

function flushEvents() {
  const cache = getCache();
  if (!cache.length) return;

  // uni.request({
  //   url: TRACK_SERVER,
  //   method: "POST",
  //   data: cache,
  //   success: () => {
  //     setCache([]); // 清空缓存
  //   },
  //   fail: () => {
  //     // 保留缓存，下次重试
  //   }
  // });
}

// 定时上报
setInterval(flushEvents, UPLOAD_INTERVAL);

// 简单 UUID 生成器
function genUUID() {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, c => {
    const r = Math.random() * 16 | 0;
    const v = c === 'x' ? r : (r & 0x3 | 0x8);
    return v.toString(16);
  });
}

// 刷新sessionId
function flushSession() {
	const sessionId = genUUID();
	uni.setStorageSync(SESSION_KEY, sessionId);
	return sessionId;
}

// 基础信息
function getBaseInfo() {
  const systemInfo = uni.getSystemInfoSync();
  return {
    openId: uni.getStorageSync("openId") || null,
    appVersion: systemInfo.appVersion,
    SDKVersion: systemInfo.SDKVersion,
    brand: systemInfo.brand,
    deviceBrand: systemInfo.deviceBrand,
    deviceModel: systemInfo.deviceModel,
    osName: systemInfo.osName,
    osVersion: systemInfo.osVersion
  };
}

// 手动埋点
function track(event, params = {}) {
	let sessionId = uni.getStorageSync(SESSION_KEY);
	if(!sessionId){
		sessionId = flushSession();
	}
	
  const eventData = {
		sessionId: sessionId, 
    eventType: event,
    ...params,
    ...getBaseInfo(),
    timestamp: Date.now()
  };
	// console.log('tarck', eventData)
  addEvent(eventData);
}

function getUserId() {
    return uni.getStorageSync('openId') || '';
}

function getSessionId() {
    return uni.getStorageSync('gdt_vid') || '';
}

function getDeviceId() {
    return '';
}

function getPlatform() {
    return uni.getStorageSync('platform') || '';
}



// 埋点请求串行队列
let trackRequestQueue = [];
let trackRequestInFlight = false;

function processTrackQueue() {
    if (trackRequestInFlight) return;
    const next = trackRequestQueue.shift();
    if (!next) return;

    trackRequestInFlight = true;
    uni.request({
        url: `${config.APIHOST}/app/event/track`,
        method: 'POST',
        data: next,
        complete: () => {
            trackRequestInFlight = false;
            processTrackQueue();
        }
    });
}

function enqueueTrackRequest(data) {
    trackRequestQueue.push(data);
    processTrackQueue();
}

function handleTrack(eventId, properties = {}, options = {}) {
    try {
        const baseData = {
            eventId,
            userId: getUserId(),
            sessionId: getSessionId(),
            deviceId: getDeviceId(),
            platform: getPlatform(),
            appVersion: '1.10.14',
            channel: 'wechat',
            properties
        };

        enqueueTrackRequest(baseData);
    } catch (e) {

    }

}

export default {
  track,
  flushEvents,
	flushSession,
    handleTrack,
};
