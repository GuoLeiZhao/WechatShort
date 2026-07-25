package com.sqx.modules.place.request.oceanengine;

import com.sqx.modules.ad.enums.TimeGranularityType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
public class ReportRequest implements Serializable  {

    private static final long serialVersionUID = 7142498167645826103L;

    private Long advertiserId;
    private String date;
    private TimeGranularityType timeGranularity;
    private Integer page;
    private Integer pageSize;
    /**
     * 排序字段，所有的统计指标均可参与排序
     * */
    private String orderField;
    /**
     * 排序方式；默认值: DESC；允许值: ASC, DESC
     * */
    private String orderType;

}
