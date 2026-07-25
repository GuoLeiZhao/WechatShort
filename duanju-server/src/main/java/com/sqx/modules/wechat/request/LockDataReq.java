package com.sqx.modules.wechat.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
public class LockDataReq implements Serializable {
    private static final long serialVersionUID = 2410288625994343011L;

    /** 当前观看用户的openid */
    String openid;
    /** 提审方小程序的appid */
    String srcAppid;
    /** 提审的剧目id */
    String dramaId;
    /** SerialItem数组，SerialItem定义见下 */
    List<SerialItem> serialList;
    /** 数据有效期，时间戳(秒)。到时间过期不可用，建议当前时 */
    Long dataExpireAt;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SerialItem implements Serializable {
        private static final long serialVersionUID = 7819049950249105729L;

        /** 剧集编号，表示起始剧集编号，从1开始 */
        Integer startSerialNo;
        /** 结束剧集编号，包含此集 */
        Integer endSerialNo;
        /** 上面的起始和结束区间内剧集是否可播放。// 1-可播放,2-不可播放 */
        Integer status;
    }
}
