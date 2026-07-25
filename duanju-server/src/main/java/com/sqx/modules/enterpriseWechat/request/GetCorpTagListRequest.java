package com.sqx.modules.enterpriseWechat.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GetCorpTagListRequest implements Serializable {
    private static final long serialVersionUID = -3704304096851638725L;

    // 要查询的标签id
    private List<String> tagId;

    // 要查询的标签组id，返回该标签组以及其下的所有标签信息
    private List<String> groupId;
}
