# 官方播放器用法参考

本目录是**微信官方短剧播放器插件**（provider: `wx94a6522b1d640c3b`）的接入用法参考 Demo，
一个原生微信小程序，演示如何调起官方播放器、拉取剧集配置、登录与广告归因上报等。

## 用途
- 参考 `pages/theater/` 了解如何用 `plugin-private://wx94a6522b1d640c3b/...` 调起官方播放器
- 参考 `common/config.js` / `lib/dn-api-miniprogram/` 了解后端配置与归因上报对接

## 部署前需替换的占位符（都在 common/config.js 及 project.config.json）
- `__WX_APPID__`：你自己的小程序 appid（project.config.json 的 appid、common/config.js 的 SRC_APPID）
- `SERVER_BASE_URL`：改成你自己的后端地址
- `__AD_UNIT_ID__`：你的激励广告位 id
- `__GDT_ACCESS_TOKEN__` / `__GDT_ACTION_SET_ID__` / `__GDT_ACCOUNT_ID__`：腾讯广告（GDT）转化上报凭证
- `__SYKT_APP_ID__` / `__SYKT_APP_SECRET__` 等第三方平台凭证（如使用）

> 注：`wx94a6522b1d640c3b` 是微信官方短剧播放器插件的固定 provider id，保持不变。
