import tracker from "./tracker";

// App 自动埋点
// 小程序场景值 https://developers.weixin.qq.com/miniprogram/dev/reference/scene-list.html
export function wrapApp(appOptions) {
  const originalOnLaunch = appOptions.onLaunch;
  appOptions.onLaunch = function (options) {
    tracker.track("app_launch", { scene: options?.scene });
    if (originalOnLaunch) originalOnLaunch.call(this, options);
  };

  const originalOnShow = appOptions.onShow;
  appOptions.onShow = function (options) {
    tracker.track("app_show", { scene: options?.scene });
    if (originalOnShow) originalOnShow.call(this, options);
  };

  return appOptions;
}

// Page 自动埋点
export function wrapPage(pageOptions) {
  const originalOnShow = pageOptions.onShow;

  pageOptions.onShow = function () {
    // 获取当前页面路径
    const pages = getCurrentPages();
    const currentPage = pages[pages.length - 1];
    const route = currentPage ? currentPage.route : "unknown";

    tracker.track("page_show", { route });

    if (originalOnShow) originalOnShow.call(this);
  };

  return pageOptions;
}