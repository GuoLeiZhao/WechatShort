package com.sqx.modules.place.response.sykt;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SyktUser implements Serializable {
    private static final long serialVersionUID = -6007115482527124450L;

    private List<UserItem> userRegList;
    private int totalCount;

    @Data
    public static class UserItem implements Serializable {
        private static final long serialVersionUID = -669588862245692580L;

        /** 小程序名称 */
        private String appName;
        /** 用户点击推广链接时间戳（也可当作回流注册的时间） */
        private String createDate;
        /** IP地址 */
        private String ip;
        /** 用户id */
        private String memberId;
        /** 分销商子账户id */
        private String agentId;
        /** userAgent */
        private String ua;
        /** 推广链接id */
        private String linkId;
        /** 推广链接名称 */
        private String linkName;
        /** 用户首次注册日期时间戳（用户只有首次注册时，regDate与createDate一致） */
        private String regDate;
        /** 用户所属小程序的openid */
        private String maOpenid;
    }

}
