package com.sqx.modules.wechat.request;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SignRequest implements Serializable {
    private static final long serialVersionUID = -9072781370353072450L;

    private String sessionKey;
    private SignData signData;
    private Long userId;
    private BigDecimal money;
    private Long courseId;
    private Long courseDetailsId;
    private Long vipDetailsId;

    @Data
    public static class SignData implements Serializable{

        private static final long serialVersionUID = 5494553378927813074L;

        private String offerId;
        private int buyQuantity;
        private int env;
        private String currencyType;
        private String outTradeNo;
        private String attach;
    }
}
