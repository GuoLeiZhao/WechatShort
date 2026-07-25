package com.sqx.modules.place.request.qsyy;

import com.sqx.common.utils.BeanUtil;
import com.sqx.modules.utils.MD5Util;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QsyyPageRequest implements Serializable {
    private static final long serialVersionUID = -8531191509713126676L;

    /** 分销商加密身份 */
    private String token;
    /** 10 位时间戳 */
    private Long timestamp;
    /** 页码(1 开始) */
    private Long page;
    /** 每页数据数量 */
    private Long pageSize;
    /** 开始时间(10 位时间戳) */
    private Long startTime;
    /** 结束时间(10 位时间戳) */
    private Long endTime;
    /** 签名(详见签名规则) */
    private String sign;
    /** 订单状态(-1:已退款, 0:未支付, 1:已支付) */
    private Long status;

    public QsyyPageRequest(String token, Long status, Date timestamp, Date startTime, Date endTime, String keyMd5) {
        this.token = token;
        this.status = status;
        this.timestamp = timestamp.getTime() / 1000;
        this.page = 1L;
        this.pageSize = 100L;
        this.startTime = startTime.getTime() / 1000;
        this.endTime = endTime.getTime() / 1000;
        this.sign = MD5Util.getMD5Str(BeanUtil.toMap(this), keyMd5);
    }
}
