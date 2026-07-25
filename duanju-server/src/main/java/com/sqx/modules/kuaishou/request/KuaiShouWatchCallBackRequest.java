package com.sqx.modules.kuaishou.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class KuaiShouWatchCallBackRequest implements Serializable {

    private static final long serialVersionUID = 2067663169516201989L;

    /** 安卓设备id的md5摘要，对15位数字的 IMEI （比如860576038225452）进行 MD5，32位 */
    private String channel;
    /** 安卓设备ID，imei 的sha1 */
    private String imei2;
    /** ios 设备id, idfa 明文直接 md5（大写）比如：32ED3EE5-9968-4F25-A015-DE3CFF569568 */
    private String imei3;
    /** IOS 获取不到idfa后的设备id， */
    private String idfa;
    /** 将填充URLEncode后的JSON数组，支持多版本的中广协CAID下发 */
    private String kenyidCaa;
    /** MD5是32位[小]写加密 */
    private String mac;
    /** 默认md5，可跟联盟运营申请替换明文 */
    private String androidid;
    /** 默认md5，可跟联盟运营申请替换明文 */
    private String os;
    /** 操作系统 */
    private String model;
    /** 机型 */
    private String ua;
    /** 用户代理(User Agent) */
    private String ts;
    /** 设备时间戳 */
    private String oaid;
    /** Android设备标识，Android Q版本 以上使用，原值(不通手机品牌格式会有区别) */
    private String oaid2;
    /** Android设备标识，Android Q版本 以上使用，md5加密后替换 */
    private String pos;
    /** 加密后的广告位id，可选择账号id、产品id、rtaid 维度加密 */
    private String accountid;
    /** 广告主账户ID，原值 */
    private String dname;
    /** 计划名称，原值 */
    private String did;
    /** 计划ID，原值 */
    private String cid;
    /** 广告创意ID，原值 */
    private String aid;
    /** 广告组ID，原值 */
    private String ip;
    /** 用户IP地址 */
    private String callback;
    /** 回调信息，编码一次的URL，长度小于10k */
    private String callbackParam;
    /** callback值，非url */
    private String deviceWidth;
    /** 设备宽 */
    private String deviceHeight;
    /** 设备高 */
    private String screenWidth;
    /** 屏幕宽 */
    private String screenHeight;
    /** 屏幕高 */
    private String industryname;
    /** 投放媒体的一级行业，md5摘要，需跟联盟运营申请加白之后替换 */
    private String costPrice;
}
