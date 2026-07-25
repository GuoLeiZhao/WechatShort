package com.sqx.modules.applog.entity;

public class UserEvent {
    private String sessionId;

    private String eventType;

    private String link; // from ad的链接

    private String openId;

    private String appVersion;

    private String SDKVersion;

    private String brand;

    private String deviceBrand;

    private String deviceModel;

    private String osName;

    private String osVersion;

    private String route; // page_show 事件的页面路径

    private String scene; // app_show, app_launch 的场景值

    private String timestamp;

    private String extra;
}
