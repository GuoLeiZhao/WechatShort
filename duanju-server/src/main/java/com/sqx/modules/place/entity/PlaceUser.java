package com.sqx.modules.place.entity;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sqx.common.base.BaseEntity;
import com.sqx.modules.place.response.qsyy.QsyyUser;
import com.sqx.modules.place.response.rmwh.RmwhUser;
import com.sqx.modules.place.response.sykt.SyktUser;
import com.sqx.modules.utils.ID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@TableName("tb_place_user")
@EqualsAndHashCode(callSuper = true)
public class PlaceUser extends BaseEntity {
    private static final long serialVersionUID = -305277313214024747L;

    @TableId(type = IdType.INPUT)
    private String id;
    /** 用户名称 */
    private String name;
    /** 注册时间 */
    @TableField("reg_time")
    private Date regTime;
    /** 三方平台用户 id */
    @TableField("open_id")
    private String openId;
    /** 小程序名称 */
    @TableField("miniapp_name")
    private String miniappName;
    /** 小程序 appId */
    @TableField("miniapp_appid")
    private String miniappAppid;
    /** 推广链接 id */
    @TableField("link_id")
    private String linkId;
    /** 推广链接名称 */
    @TableField("link_name")
    private String linkName;
    /** 用户染色 ip */
    private String ip;
    /** 用户广告 id */
    @TableField("ad_id")
    private String adId;
    /** 代理商 id */
    @TableField("agent_id")
    private String agentId;
    /** 平台 */
    private Context.Channel channel;

    public PlaceUser(RmwhUser rmwhUser, PlaceLink placeLink){
        this.id = ID.get();
        this.name = rmwhUser.getUserName();
        this.regTime = rmwhUser.getUserRegtime();
        this.openId = rmwhUser.getUserId().toString();
        this.miniappName = rmwhUser.getMiniappName();
        this.miniappAppid = rmwhUser.getMiniappAppid();
        this.linkId = placeLink.getId();
        this.linkName = placeLink.getName();
        this.ip = rmwhUser.getIp();
        this.adId = rmwhUser.getClickid();
        this.agentId = rmwhUser.getAgentId().toString();
        this.channel = Context.Channel.LHDEER;
    }

    public PlaceUser(SyktUser.UserItem syktUser, PlaceLink placeLink){
        this.id = ID.get();
        this.name = "映客美光用户";
        this.regTime = DateUtil.parseDateTime(syktUser.getRegDate());
        this.openId = syktUser.getMaOpenid();
        this.miniappName = syktUser.getAppName();
        this.miniappAppid = null;
        this.linkId = placeLink.getId();
        this.linkName = placeLink.getName();
        this.ip = syktUser.getIp();
        this.adId = syktUser.getLinkId();
        this.agentId = syktUser.getAgentId();
        this.channel = Context.Channel.YKMG;
    }

    public PlaceUser(QsyyUser qsyyUser, PlaceLink placeLink){
        this.id = ID.get();
        this.name = "奇树有鱼用户";
        this.regTime = new Date(qsyyUser.getRegisterTime() * 1000L);
        this.openId = qsyyUser.getUid().toString();
        this.miniappName = qsyyUser.getAppName();
        this.miniappAppid = qsyyUser.getAppid();
        this.linkId = placeLink.getId();
        this.linkName = placeLink.getName();
        this.ip = qsyyUser.getIp();
        this.channel = Context.Channel.QSYY;
    }

}
