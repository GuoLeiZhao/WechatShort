package com.sqx.modules.enterpriseWechat.request;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class GetGroupmsgListRequest implements Serializable {
    private static final long serialVersionUID = -6647554120203123950L;

    private String chatType = "single";
    private Long startTime = DateUtil.offsetDay(new Date(), -30).getTime() / 1000;
    private Long endTime = new Date().getTime() / 1000;
    private String creator;
    /**  0：企业发表 1：个人发表 2：所有，包括个人创建以及企业创建，默认情况下为所有类型 */
    private Integer filterType;
    private Integer limit;
    private String cursor;
}
