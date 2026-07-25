package com.sqx.modules.place.response.rmwh;

import lombok.Data;

import java.io.Serializable;

@Data
public class RmwhToken implements Serializable {
    private static final long serialVersionUID = -1156765502319023405L;

    private String token;
}
