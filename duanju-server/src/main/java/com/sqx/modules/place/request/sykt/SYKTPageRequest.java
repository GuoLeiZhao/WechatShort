package com.sqx.modules.place.request.sykt;

import cn.hutool.crypto.SecureUtil;
import lombok.Data;

import java.io.Serializable;

@Data
public class SYKTPageRequest implements Serializable {
    private static final long serialVersionUID = 8113464272254853657L;
    private static final String appSecret = "__SYKT_APP_SECRET__";

    /** 唯-appld，请联系商务提供 */
    private String appId = "__SYKT_APP_ID__";
    /**
     * 验签字段，sign，算法:
     * sha256(appSecret:startTime:endTime),其中appSecret，请联系商务提供，请勿泄露，其它参数即当前请求中的参数
     */
    private String sign;
    /** 用户点击推广链接开始时间 精确到小时,格式yyyyMMddHH或yyyyMMddHHmmss(查询范围不能超7天) */
    private String startTime;
    /** 用户点击推广链接结束时间 精确到小时,格式yyyyMMddHH或yyyyMMddHHmmss(查询范围不能超7天) */
    private String endTime;
    /** 页码 */
    private Integer page;
    /** 每页记录数（最大 500） */
    private Integer size;

    public SYKTPageRequest(String startTime, String endTime, Integer page, Integer size) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.page = page;
        this.size = size;
        this.sign = sign(startTime, endTime);
    }

    /**
     * @param startTime
     * @param endTime
     * @return
     */
    private String sign(String startTime, String endTime) {
        String signOrigin = appSecret + ":" + startTime + ":" + endTime;
        return SecureUtil.sha256(signOrigin);
    }
}
