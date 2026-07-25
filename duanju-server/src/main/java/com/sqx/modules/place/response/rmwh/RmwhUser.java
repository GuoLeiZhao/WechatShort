package com.sqx.modules.place.response.rmwh;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RmwhUser implements Serializable {
    private static final long serialVersionUID = 5776739083643757140L;

    /**
     * 注册时间
     */
    private Date userRegtime;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 用户昵称
     */
    private String userName;
    /**
     * openid
     */
    private String openId;
    /**
     * 小程序名称
     */
    private String miniappName;
    /**
     * 小程序appid
     */
    private String miniappAppid;
    /**
     * 推广链接id
     */
    private Long channelId;
    /**
     * 推广链接名称
     */
    private String channelName;
    /**
     * 分销商id
     */
    private Long agentId;
    /**
     * 分销商自定义字段json
     */
    private String miJson;
    /**
     * 广告id
     */
    private String clickid;
    /**
     * 用户ip
     */
    private String ip;
}
