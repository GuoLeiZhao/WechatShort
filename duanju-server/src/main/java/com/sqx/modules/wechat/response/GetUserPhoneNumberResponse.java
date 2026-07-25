package com.sqx.modules.wechat.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class GetUserPhoneNumberResponse implements Serializable {
    private static final long serialVersionUID = -6073690895219779150L;

    // 错误码
    private String errcode;
    // 错误信息
    private String errmsg;
    // 用户手机号信息
    private PhoneInfo phoneInfo;

    @Data
    public static class PhoneInfo implements Serializable {
        private static final long serialVersionUID = -6073690895219779150L;

        // 用户绑定的手机号（国外手机号会有区号）
        private String phoneNumber;
        // 没有区号的手机号
        private String purePhoneNumber;
        // 区号
        private String countryCode;
        // 数据水印
        private Watermark watermark;

        @Data
        public static class Watermark implements Serializable {
            private static final long serialVersionUID = -6073690895219779150L;
            // 用户获取手机号操作的时间戳
            private String timestamp;
            // 小程序appid
            private String appid;

        }
    }
}
