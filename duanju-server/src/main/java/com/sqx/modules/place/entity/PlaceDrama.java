package com.sqx.modules.place.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sqx.common.base.BaseEntity;
import com.sqx.modules.place.response.sykt.SyktOrder;
import com.sqx.modules.utils.ID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@TableName("tb_place_drama")
@EqualsAndHashCode(callSuper = true)
public class PlaceDrama extends BaseEntity {
    private static final long serialVersionUID = 6963596752970572267L;

    @TableId(type = IdType.INPUT)
    private String id;
    private String name;
    /** 三方短剧 id */
    @TableField("open_id")
    private String openId;
    /** 平台 */
    private Context.Channel channel;

    public PlaceDrama(String name, String openId, Context.Channel channel) {
        this.id = ID.get();
        this.name = name;
        this.openId = openId;
        this.channel = channel;
    }

    public PlaceDrama(SyktOrder.OrderItem order) {
        this.id = ID.get();
        this.name = order.getMovieName();
        this.openId = order.getMovieId();
        this.channel = Context.Channel.YKMG;
    }
}
