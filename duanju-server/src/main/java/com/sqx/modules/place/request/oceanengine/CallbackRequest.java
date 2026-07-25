package com.sqx.modules.place.request.oceanengine;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class CallbackRequest implements Serializable  {

    private static final long serialVersionUID = -9034835912490300840L;

    private String auth_code;
    private String state;

}
