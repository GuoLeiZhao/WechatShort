package com.sqx.modules.ad.constant;

/**
 * 小程序生命周期事件常量
 */
public class MiniProgramEventConstant {

    /**
     * 事件ID常量
     */
    public static class EventId {
        /** 小程序初始化事件 (onLaunch) */
        public static final String APP_LAUNCH = "AppLaunch";
        
        /** 小程序显示事件 (onShow) */
        public static final String APP_SHOW = "AppShow";
        
        /** 小程序隐藏事件 (onHide) */
        public static final String APP_HIDE = "AppHide";
        
        /** 小程序错误事件 (onError) */
        public static final String APP_ERROR = "AppError";
        
        /** 用户进入小程序事件 (兼容原有格式) */
        public static final String APP_ENTER = "AppEnter";
        
        /** 用户退出小程序事件 (兼容原有格式) */
        public static final String APP_EXIT = "AppExit";
    }

    /**
     * 属性键名常量
     */
    public static class PropertyKey {
        // 通用属性
        /** 当前页面 */
        public static final String CURRENT_PAGE = "CurrentPage";
        
        /** 页面栈 */
        public static final String PAGE_STACK = "PageStack";
        
        /** 引用信息 */
        public static final String REFERRER_INFO = "ReferrerInfo";
        
        /** 系统信息 */
        public static final String SYSTEM_INFO = "SystemInfo";
        
        /** 场景值 */
        public static final String SCENE = "Scene";
        
        // 启动相关属性
        /** 启动类型 */
        public static final String LAUNCH_TYPE = "LaunchType";
        
        /** 进入来源 */
        public static final String ENTRY_SOURCE = "EntrySource";
        
        // 显示/隐藏相关属性
        /** 显示类型 */
        public static final String SHOW_TYPE = "ShowType";
        
        /** 隐藏类型 */
        public static final String HIDE_TYPE = "HideType";
        
        /** 后台时间 */
        public static final String BACKGROUND_TIME = "BackgroundTime";
        
        /** 停留时长 */
        public static final String STAY_DURATION = "StayDuration";
        
        /** 页面数量 */
        public static final String PAGE_COUNT = "PageCount";
        
        /** 用户操作 */
        public static final String USER_ACTION = "UserAction";
        
        /** 恢复原因 */
        public static final String RESUME_REASON = "ResumeReason";
        
        // 错误相关属性
        /** 错误类型 */
        public static final String ERROR_TYPE = "ErrorType";
        
        /** 错误信息 */
        public static final String ERROR_MESSAGE = "ErrorMessage";
        
        /** 错误堆栈 */
        public static final String ERROR_STACK = "ErrorStack";
        
        /** 错误时间 */
        public static final String ERROR_TIME = "ErrorTime";
        
        /** API URL */
        public static final String API_URL = "ApiUrl";
        
        /** 错误代码 */
        public static final String ERROR_CODE = "ErrorCode";
        
        // 退出相关属性
        /** 退出原因 */
        public static final String EXIT_REASON = "ExitReason";
    }

    /**
     * 属性值常量
     */
    public static class PropertyValue {
        // 启动类型
        /** 冷启动 */
        public static final String LAUNCH_TYPE_COLD = "冷启动";
        
        /** 热启动 */
        public static final String LAUNCH_TYPE_HOT = "热启动";
        
        // 显示类型
        /** 前台显示 */
        public static final String SHOW_TYPE_FOREGROUND = "前台显示";
        
        /** 后台恢复 */
        public static final String SHOW_TYPE_BACKGROUND_RESUME = "后台恢复";
        
        // 隐藏类型
        /** 后台隐藏 */
        public static final String HIDE_TYPE_BACKGROUND = "后台隐藏";
        
        // 错误类型
        /** 脚本错误 */
        public static final String ERROR_TYPE_SCRIPT = "脚本错误";
        
        /** API调用失败 */
        public static final String ERROR_TYPE_API = "API调用失败";
        
        // 退出原因
        /** 主动退出 */
        public static final String EXIT_REASON_ACTIVE = "主动退出";
        
        /** 系统退出 */
        public static final String EXIT_REASON_SYSTEM = "系统退出";
        
        // 用户操作
        /** 主动切换 */
        public static final String USER_ACTION_SWITCH = "主动切换";
        
        /** 返回桌面 */
        public static final String USER_ACTION_HOME = "返回桌面";
    }
}
