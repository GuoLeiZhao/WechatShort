package com.sqx.modules.kuaishou.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sqx.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kuai_shou_watch")
public class KuaiShouWatchEntity extends BaseEntity {
    private static final long serialVersionUID = -5984736458042529341L;
    /** 主键id */
    @TableId(type = IdType.INPUT)
    private String id;
    /** 渠道来源 */
    private String channel;
    /** 安卓设备id的md5摘要，对15位数字的 IMEI （比如860576038225452）进行 MD5，32位 */
    private String imei2;
    /** 安卓设备ID，imei 的sha1 */
    private String imei3;
    /** ios 设备id, idfa 明文直接 md5（大写）比如：32ED3EE5-9968-4F25-A015-DE3CFF569568 */
    private String idfa;
    /** IOS 获取不到idfa后的设备id，将填充URLEncode后的JSON数组，支持多版本的中广协CAID下发，MD5是32位[小]写加密 */
    @TableField("kenyid_caa")
    private String kenyidCaa;
    /** 默认md5，可跟联盟运营申请替换明文 */
    private String mac;
    /** 默认md5，可跟联盟运营申请替换明文 */
    private String androidid;
    /** 操作系统 */
    private String os;
    /** 机型 */
    private String model;
    /** 用户代理(User Agent) */
    private String ua;
    /** 设备时间戳 */
    private String ts;
    /** Android设备标识，Android Q版本 以上使用，原值(不通手机品牌格式会有区别) */
    private String oaid;
    /** Android设备标识，Android Q版本 以上使用，md5加密后替换 */
    private String oaid2;
    /** 加密后的广告位id，可选择账号id、产品id、rtaid 维度加密 */
    private String pos;
    /** 广告主账户ID，原值 */
    private String accountid;
    /** 计划名称，原值 */
    private String dname;
    /** 计划ID，原值 */
    private String did;
    /** 广告创意ID，原值 */
    private String cid;
    /** 广告组ID，原值 */
    private String aid;
    /** 用户IP地址 */
    private String ip;
    /** 回调信息，编码一次的URL，长度小于10k */
    private String callback;
    /** callback值，非url */
    @TableField("callback_param")
    private String callbackParam;
    /** 设备宽 */
    @TableField("device_width")
    private String deviceWidth;
    /** 设备高 */
    @TableField("device_height")
    private String deviceHeight;
    /** 屏幕宽 */
    @TableField("screen_width")
    private String screenWidth;
    /** 屏幕高 */
    @TableField("screen_height")
    private String screenHeight;
    /** 投放媒体的一级行业，md5摘要，需跟联盟运营申请加白之后替换 */
    private String industryname;
    /** 本次曝光扣费金额，单位厘，需跟联盟运营申请之后替换 */
    @TableField("cost_price")
    private String costPrice;

}





























