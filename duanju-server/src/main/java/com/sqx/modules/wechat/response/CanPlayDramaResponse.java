package com.sqx.modules.wechat.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CanPlayDramaResponse implements Serializable {
    private static final long serialVersionUID = 6752596734061976273L;

    private String dramaId;
    private String encryptedData;
    private FreeList freeList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FreeList implements Serializable {
        private static final long serialVersionUID = 8534994007561782768L;
        private Integer startSerialNo;
        private Integer endSerialNo;
    }

}