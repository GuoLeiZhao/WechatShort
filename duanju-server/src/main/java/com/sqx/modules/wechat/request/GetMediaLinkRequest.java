package com.sqx.modules.wechat.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 媒体资源访问链接生成器类
 * 该类用于构建带有特定参数的媒资文件播放链接，包括访问凭证、媒资ID、链接有效期等。
 */
@Data
@NoArgsConstructor
public class GetMediaLinkRequest implements Serializable {

    private static final long serialVersionUID = -3709726063689270013L;

    // 媒资文件ID，必填项
    private long mediaId;
    // 播放地址的过期时间戳，单位为秒，必填项，最长有效期不超过2小时
    private long t;
    // 链接标识，默认由平台生成，可选用户自定义部分，增强链接唯一性
    private String us;
    // 试看时长，单位：秒，选填项，最大值不能超过视频长度
    private Integer exper;
    // 最多允许不同IP终端播放次数，十进制表示，选填项，最大9，不填则不限制
    private Integer rlimit;
    // 允许访问的域名列表，支持1-10条，半角逗号分隔，选填项，支持前缀匹配与通配符
    private String whref;
    // 禁止访问的域名列表，支持1-10条，半角逗号分隔，选填项，支持前缀匹配与通配符
    private String bkref;

    public GetMediaLinkRequest(long mediaId, long t) {
        this.mediaId = mediaId;
        this.t = t;
    }
}
