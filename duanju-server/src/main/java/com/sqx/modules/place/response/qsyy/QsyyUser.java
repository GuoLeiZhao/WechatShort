package com.sqx.modules.place.response.qsyy;

import lombok.Data;

import java.io.Serializable;

@Data
public class QsyyUser implements Serializable {

    private static final long serialVersionUID = 1924033541950368939L;

    /**
     * 用户 uid
     */
    private Long uid;
    /**
     * 用户 openid
     */
    private String openid;
    /**
     * 推广 ID
     */
    private String spreadId;
    /**
     * 推广名
     */
    private String spreadName;
    /**
     * 小程序 APPID
     */
    private String appid;
    /**
     * 小程序名
     */
    private String appName;
    /**
     * 用户最新 IP
     */
    private String ip;
    /**
     * 用户最新 UA
     */
    private String ua;
    /**
     * 用户操作系统(下单时)<IOS/Android>
     */
    private String os;
    /**
     * 染色时间(13 位时间戳)
     */
    private Long createTime;
    /**
     * 注册时间(13 位时间戳)
     */
    private Long registerTime;
    /**
     * 推广员账号
     */
    private String promoter;
    /**
     * 推广员名
     */
    private String promoterName;
}
