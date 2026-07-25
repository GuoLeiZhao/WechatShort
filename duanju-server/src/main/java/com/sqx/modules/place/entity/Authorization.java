package com.sqx.modules.place.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Authorization implements Serializable {
    private static final long serialVersionUID = -4559983186503819481L;

    private String token;

    public Authorization(String token) {
        this.token = token;
    }
}
