package com.sqx.modules.place.response.rmwh;

import lombok.Data;

import java.io.Serializable;

@Data
public class RmwhChannel implements Serializable {
    private static final long serialVersionUID = -1990702283087777932L;

    /**
     * 推广链接id
     */
    private Integer channelId;
    /**
     * 推广链接名称
     */
    private String channelName;
    /**
     * 小程序路径
     */
    private String bootAddress;
    /**
     * 创建时间
     */
    private String channelCreatedtime;
    /**
     * 短剧id
     */
    private Integer videoId;
    /**
     * 短剧名称
     */
    private String videoName;
    /**
     * 小程序名称
     */
    private String miniappName;
    /**
     * 小程序appid
     */
    private String miniappAppid;
    /**
     * 代理商ID
     */
    private Integer agentId;
}
