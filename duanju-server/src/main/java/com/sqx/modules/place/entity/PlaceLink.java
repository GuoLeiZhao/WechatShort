package com.sqx.modules.place.entity;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sqx.common.base.BaseEntity;
import com.sqx.modules.place.response.rmwh.RmwhChannel;
import com.sqx.modules.place.response.sykt.SyktOrder;
import com.sqx.modules.place.response.sykt.SyktUser;
import com.sqx.modules.utils.ID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@TableName("tb_place_link")
@EqualsAndHashCode(callSuper = true)
public class PlaceLink extends BaseEntity {
    private static final long serialVersionUID = -6421899742097892951L;

    @TableId(type = IdType.INPUT)
    private String id;
    /** 推广链接名称 */
    private String name;
    /** 三方推广链接 id */
    @TableField("open_id")
    private String openId;
    /** 小程序路径 */
    @TableField("boot_address")
    private String bootAddress;
    /** 小程序 appid */
    @TableField("miniapp_appid")
    private String miniappAppid;
    /** 小程序名称 */
    @TableField("miniapp_name")
    private String miniappName;
    /** 代理商id */
    @TableField("agent_id")
    private String agentId;
    /** 三方推广链接创建时间 */
    @TableField("open_create_time")
    private Date openCreateTime;
    /** 渠道 */
    private Context.Channel channel;

    public PlaceLink(RmwhChannel rmwhChannel) {
        this.id = ID.get();
        this.name = rmwhChannel.getChannelName();
        this.openId = rmwhChannel.getChannelId().toString();
        this.bootAddress = rmwhChannel.getBootAddress();
        this.miniappAppid = rmwhChannel.getMiniappAppid();
        this.miniappName = rmwhChannel.getMiniappName();
        this.agentId = rmwhChannel.getAgentId().toString();
        this.openCreateTime = DateUtil.parseDateTime(rmwhChannel.getChannelCreatedtime());
    }

    public PlaceLink(SyktUser.UserItem user) {
        this.id = ID.get();
        this.name = user.getLinkName();
        this.openId = user.getLinkId();
        this.miniappName = user.getAppName();
        this.agentId = user.getAgentId();
        this.channel = Context.Channel.YKMG;
    }

}
