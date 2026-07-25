package com.sqx.modules.douyin.request;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 2、【选接】用户权益流水同步
 * https://bytedance.larkoffice.com/docx/OjordhwgMo3WdexEYIhcDHlWnFc
 */
@Data
public class UserAssetFlowRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String app_id;
    private String open_id;
    // 开发者自己账户系统的用户账户id，用于记录用户资产的归属账户，一般是开发者用于打通或者隔离用户的资产账户id
    private String dev_user_account_id;
    // 同步的资产信息类型1：会员，2：代币，3：抵用券，4：已获得的短剧剧集信息，本字段会决定flow_content的字段结构
    private Integer asset_type;
    /**
     * 流水方向，1：入账，2：出账
     * 入账包含：包含购买金币，购买会员，获得优惠券等
     * 出账包含：包含使用金币解锁剧集，会员过期，优惠券兑换消耗等
     */
    private Integer flow_direction;
    private String flow_type;
    private String out_change_id;
    private String flow_content;
    private ChangeContext change_context;
    private String version;
    private String idempotent_id;
    private String change_time;

    private String clickid;
    private String linkId;
    private String ttAlbumId;
    private String ttEpisodeId;

    @Data
    public static class Album implements Serializable {
        private static final long serialVersionUID = 1L;
        private String album_id;
        private List<String> episode_id_list;
    }

    @Data
    public static class ChangeContext implements Serializable {
        private static final long serialVersionUID = 1L;
        private String scene;
        private Album album;
    }

    @Data
    public static class AlbumFlowContent implements Serializable {
        private static final long serialVersionUID = 1L;
        private Album album;
        /**
         * 解锁的剧集权益是否会过期，过期后观看需要重新（付费）解锁，0：不会过期，1：会过期
         */
        private Integer can_expire;
        private String expire_time;
        private Integer total_episode_num;
        private Integer need_unlock_episode_num;
        private Integer unlocked_episode_num;
    }
}