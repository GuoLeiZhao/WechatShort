package com.sqx.modules.place.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

public class Context implements Serializable {
    private static final long serialVersionUID = 6891751961841554965L;

    public enum Platform {
        LHDeerPlatformSyncData,
        QsyyPlatformSyncData,
        YkmgPlatformSyncData,
        ;
    }

    @Getter
    @AllArgsConstructor
    public enum Channel {
        LHDEER("长角鹿"),
        YKMG("映客美光"),
        QSYY("奇树有鱼"),
        ;
        private final String desc;
    }

}
