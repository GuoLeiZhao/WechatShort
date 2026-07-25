import tracker from "./tracker";

export default {
  onShow() {
    const pages = getCurrentPages();
    const currentPage = pages[pages.length - 1];
    const route = currentPage ? currentPage.route : "unknown";

    tracker.track("page_show", { route });
  },
  methods: {
    $trackEvent(eventName, params = {}) {
      tracker.track(eventName, params);
    }
  }
};
