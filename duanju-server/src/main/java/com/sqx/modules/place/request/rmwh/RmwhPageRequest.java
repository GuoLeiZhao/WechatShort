package com.sqx.modules.place.request.rmwh;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class RmwhPageRequest implements Serializable {
    private static final long serialVersionUID = 6723624232937225583L;

    /**
     * token
     */
    private String token;
    /**
     * 页码
     */
    private int page = 1;
    /**
     * 每页数量，最多 100
     */
    private int pageSize = 100;
    /**
     * 开始时间，格式：yyyy-MM-dd HH:mm:ss，仅限 3 天之内
     */
    private String startTime;
    /**
     * 结束时间，格式：yyyy-MM-dd HH:mm:ss，因账单状态实时变动，订单暂时只支持获取 5 分钟之前订单
     */
    private String endTime;

    public RmwhPageRequest(String token) {
        this.token = token;
    }
}
