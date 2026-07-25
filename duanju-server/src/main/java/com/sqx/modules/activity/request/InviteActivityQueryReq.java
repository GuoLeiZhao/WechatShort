package com.sqx.modules.activity.request;

import com.sqx.common.base.BasePagingRequest;
import com.sqx.modules.activity.entity.InviteActivity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class InviteActivityQueryReq extends BasePagingRequest implements Serializable {
    private static final long serialVersionUID = -6357061204211242655L;

    private Long id;
    /** 领取用户 */
    private Long userId;
    /** 看点领取状态 */
    private InviteActivity.Status status;
    /** 活动类型 */
    private InviteActivity.Type type;



}
