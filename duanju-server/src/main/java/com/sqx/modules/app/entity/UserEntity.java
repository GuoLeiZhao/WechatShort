package com.sqx.modules.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 用户
 *
 */
@Data
@ApiModel("用户")
@TableName("tb_user")
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@ApiModelProperty("用户id")
	@TableId(type = IdType.AUTO,value = "user_id")
	private Long userId;
	/**
	 * 用户名
	 */
	@ApiModelProperty("用户名")
	@TableField("user_name")
	private String userName;

	/**
	 * 手机号
	 */
	@ApiModelProperty("手机号")
	private String phone;

	/**
	 * 头像
	 */
	@ApiModelProperty("头像")
	private String avatar;

	/**
	 * 性别 1男 2女
	 */
	@ApiModelProperty("性别 1男 2女")
	private Integer sex;

	/**
	 * 微信小程序openid
	 */
	@ApiModelProperty("微信小程序openid")
	@TableField("open_id")
	private String openId;

	/**
	 * 微信小程序openid
	 */
	@ApiModelProperty("微信公众号openid")
	@TableField("wx_id")
	private String wxId;

	/**
	 * 微信app openid
	 */
	@ApiModelProperty("微信app openid")
	@TableField("wx_open_id")
	private String wxOpenId;

	/**
	 * 微信app openid
	 */
	@ApiModelProperty("微信 unionId")
	@TableField("wx_union_id")
	private String wxUnionId;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 创建时间
	 */
	@TableField("create_time")
	private String createTime;

	/**
	 * 更新时间
	 */
	@TableField("update_time")
	private String updateTime;

	/**
	 * 苹果id
	 */
	@TableField("apple_id")
	private String appleId;

	/**
	 * 手机类型 1安卓 2ios
	 */
	@TableField("sys_phone")
	private Integer sysPhone;

	/**
	 * 状态 1正常 2禁用
	 */
	private Integer status;

	/**
	 * 来源 app 小程序 公众号
	 */
	private String platform;

	/**
	 * 积分
	 */
	private Integer jifen;

	/**
	 * 邀请码
	 */
	@TableField("invitation_code")
	private String invitationCode;

	/**
	 * 邀请人邀请码
	 */
	@TableField("inviter_code")
	private String inviterCode;

	/**
	 * 链接参数
	 */
	@TableField("tt_options")
	private String ttOptions;

	/**
	 * 链接点击参数
	 */
	@TableField("tt_click_id")
	private String ttClickId;

	/**
	 * 链接id
	 */
	@TableField("tt_link_id")
	private String ttLinkId;

	private String clientid;

	private String zhiFuBao;

	private String zhiFuBaoName;

	private BigDecimal rate;

	/**
	 * 最后一次在线时间
	 */
	private String onLineTime;

	@TableField(exist = false)
	private Integer member;

	@TableField(exist = false)
	private Integer counts;

	@TableField(exist = false)
	private BigDecimal money;

	@TableField(exist = false)
	private String endTime;


}
