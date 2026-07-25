package com.sqx.modules.wechat.request;

import io.swagger.models.auth.In;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ListMediaRequest implements Serializable {
    private static final long serialVersionUID = -2722343279728643084L;

    // 根据剧目ID获取剧集信息，非必填
    private Long dramaId;

    // 媒资文件名，支持模糊匹配，非必填
    private String mediaName;

    // 媒资上传时间开始范围，非必填
    private Long startTime;

    // 媒资上传时间结束范围，非必填
    private Long endTime;

    // 分页拉取的最大返回结果数，默认值为100，最大值也为100，非必填
    private Integer limit;

    // 分页拉取的起始偏移量，默认值为0，非必填
    private Integer offset;

    public ListMediaRequest(Long dramaId, Integer limit, Integer offset) {
        this.dramaId = dramaId;
        this.limit = limit;
        this.offset = offset;
    }
}
