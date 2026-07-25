package com.sqx.common.base;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseDropList implements Serializable {
    private static final long serialVersionUID = 2890616841845656690L;

    private String label;
    private String value;
}
