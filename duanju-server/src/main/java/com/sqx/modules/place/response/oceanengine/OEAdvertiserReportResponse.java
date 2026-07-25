package com.sqx.modules.place.response.oceanengine;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class OEAdvertiserReportResponse implements Serializable {
    private static final long serialVersionUID = -5661815513281690707L;

    private List<Report> list;
    private OEPageInfo page_info;

    @Data
    public static class Report implements Serializable {
        private static final long serialVersionUID = -3491351686629827211L;

        private Long advertiser_id; // 广告主ID
        private Date stat_datetime; // 数据起始时间，分组条件包含 STAT_GROUP_BY_FIELD_STAT_TIME 时返回
        private String inventory; // 投放广告位，分组条件包含STAT_GROUP_BY_INVENTORY时返回
        private String creative_material_mode; // 创意类型（STATIC_ASSEMBLE：程序化创意、INTERVENE：自定义创意）
        private String landing_type; // 推广目的类型
        private String pricing; // 出价类型
        private String image_mode; // 素材类型，分组条件STAT_GROUP_BY_IMAGE_MODE返回
        private String province_name; // 省份。如果分组条件中包括 STAT_GROUP_BY_PROVINCE_NAME 时返回
        private String city_name; // 城市。如果分组条件中包括 STAT_GROUP_BY_CITY_NAME 时返回
        private String gender; // 性别。如果分组条件中包括 STAT_GROUP_BY_GENDER 时返回
        private String age; // 年龄。如果分组条件中包括 STAT_GROUP_BY_AGE 时返回
        private String platform; // 平台。如果分组条件中包括 STAT_GROUP_BY_PLATFORM 时返回
        private String ac; // 网络类型。如果分组条件中包括 STAT_GROUP_BY_AC 时返回
        private Float cost; // 展现数据-总花费
        private Long show; // 展现数据-展示数
        private Float avg_show_cost; // 展现数据-平均千次展现费用
        private Long click; // 展现数据-点击数
        private Float avg_click_cost; // 展现数据-平均点击单价
        private Float ctr; // 展现数据-点击率
        private Long convert; // 转化数据-转化数
        private Float convert_cost; // 转化数据-转化成本
        private Float convert_rate; // 转化数据-转化率
        private Long deep_convert; // 转化数据-深度转化数
        private Float deep_convert_cost; // 转化数据-深度转化成本
        private Float deep_convert_rate; // 转化数据-深度转化率
        private Long attribution_convert; // 转化数据（计费时间）-转化数（计费时间）
        private Float attribution_convert_cost; // 转化数据（计费时间）-转化成本（计费时间）
        private Long attribution_deep_convert; // 转化数据（计费时间）-深度转化数（计费时间）
        private Float attribution_deep_convert_cost; // 转化数据（计费时间）-深度转化成本（计费时间）
        private Long download_start; // 应用下载广告数据-安卓下载开始数
        private Float download_start_cost; // 应用下载广告数据-安卓下载开始成本
        private Float download_start_rate; // 应用下载广告数据-安卓下载开始率
        private Long download_finish; // 应用下载广告数据-安卓下载完成数
        private Float download_finish_cost; // 应用下载广告数据-安卓下载完成成本
        private Float download_finish_rate; // 应用下载广告数据-安卓下载完成率
        private Long click_install; // 应用下载广告数据-点击安装数
        private Long install_finish; // 应用下载广告数据-安卓安装完成数
        private Float install_finish_cost; // 应用下载广告数据-安卓安装完成成本
        private Float install_finish_rate; // 应用下载广告数据-安卓安装完成率
        private Long active; // 应用下载广告数据-激活数
        private Float active_cost; // 应用下载广告数据-激活成本
        private Float active_rate; // 应用下载广告数据-激活率
        private Long register; // 应用下载广告数据-注册数
        private Float active_register_cost; // 应用下载广告数据-注册成本
        private Float active_register_rate; // 应用下载广告数据-注册率
        private Long attribution_next_day_open_cnt; // 应用下载广告数据-次留数
        private Float attribution_next_day_open_cost; // 应用下载广告数据-次留成本
        private Float attribution_next_day_open_rate; // 应用下载广告数据-次留率
        private Long attribution_retention_2d_cnt; // 应用下载广告数据-2日留存数
        private Float attribution_retention_2d_cost; // 应用下载广告数据-2日留存成本
        private Float attribution_retention_2d_rate; // 应用下载广告数据-2日留存率
        private Long attribution_retention_3d_cnt; // 应用下载广告数据-3日留存数
        private Float attribution_retention_3d_cost; // 应用下载广告数据-3日留存成本
        private Float attribution_retention_3d_rate; // 应用下载广告数据-3日留存率
        private Long attribution_retention_4d_cnt; // 应用下载广告数据-4日留存数
        private Float attribution_retention_4d_cost; // 应用下载广告数据-4日留存成本
        private Float attribution_retention_4d_rate; // 应用下载广告数据-4日留存率
        private Long attribution_retention_5d_cnt; // 应用下载广告数据-5日留存数
        private Float attribution_retention_5d_cost; // 应用下载广告数据-5日留存成本
        private Float attribution_retention_5d_rate; // 应用下载广告数据-5日留存率
        private Long attribution_retention_6d_cnt; // 应用下载广告数据-6日留存数
        private Float attribution_retention_6d_cost; // 应用下载广告数据-6日留存成本
        private Float attribution_retention_6d_rate; // 应用下载广告数据-6日留存率
        private Long attribution_retention_7d_cnt; // 应用下载广告数据-7日留存数
        private Float attribution_retention_7d_cost; // 应用下载广告数据-7日留存成本
        private Float attribution_retention_7d_rate; // 应用下载广告数据-7日留存率
        private Long game_addiction; // 应用下载广告数据-关键行为数
        private Float game_addiction_cost; // 应用下载广告数据-关键行为成本
        private Float game_addiction_rate; // 应用下载广告数据-关键行为率
        private Long pay_count; // 应用下载广告数据-首次付费次数
        private Float active_pay_cost; // 应用下载广告数据-首次付费成本
        private Float active_pay_rate; // 应用下载广告数据-首次付费率
        private Long loan_completion; // 应用下载广告数据-完件数
        private Float loan_completion_cost; // 应用下载广告数据-完件成本
        private Float loan_completion_rate; // 应用下载广告数据-完件率
        private Long pre_loan_credit; // 应用下载广告数据-预授信数
        private Float pre_loan_credit_cost; // 应用下载广告数据-预授信成本
        private Long loan_credit; // 应用下载广告数据-授信数
        private Float loan_credit_cost; // 应用下载广告数据-授信成本
        private Float loan_credit_rate; // 应用下载广告数据-授信率
        private Long in_app_uv; // 应用下载广告数据-APP内访问
        private Long in_app_detail_uv; // 应用下载广告数据-APP内访问详情页
        private Long in_app_cart; // 应用下载广告数据-APP内加入购物车
        private Long in_app_pay; // 应用下载广告数据-APP内付费
        private Long in_app_order; // 应用下载广告数据-APP内下单
        private Long attribution_game_pay_7d_count; // 应用下载广告数据-7日付费次数（激活时间）
        private Float attribution_game_pay_7d_cost; // 应用下载广告数据-7日付费成本（激活时间）
        private Long attribution_active_pay_7d_per_count; // 应用下载广告数据-7日人均付费次数（激活时间）
        private Long game_pay_count; // 应用下载广告数据-付费次数
        private Long phone; // 落地页转化数据-点击电话按钮
        private Long form; // 落地页转化数据-表单提交
        private Long map_search; // 落地页转化数据-地图搜索
        private Long button; // 落地页转化数据-按钮button
        private Long view; // 落地页转化数据-关键页面浏览
        private Long download; // 落地页转化数据-下载开始
        private Long qq; // 落地页转化数据-QQ咨询
        private Long lottery; // 落地页转化数据-抽奖
        private Long vote; // 落地页转化数据-投票
        private Long message; // 落地页转化数据-短信咨询
        private Long redirect; // 落地页转化数据-页面跳转
        private Long shopping; // 落地页转化数据-商品购买
        private Long consult; // 落地页转化数据-在线咨询
        private Long wechat; // 落地页转化数据-微信复制
        private Long phone_confirm; // 落地页转化数据-智能电话-确认拨打
        private Long phone_connect; // 落地页转化数据-智能电话-确认接通
        private Long consult_effective; // 落地页转化数据-有效咨询
        private Long coupon; // 落地页转化数据-建站卡券领取
        private Long coupon_single_page; // 落地页转化数据-卡券页领取
        private Long redirect_to_shop; // 落地页及门店数据-调起店铺
        private Long poi_collect; // 落地页及门店数据-店铺收藏
        private Long poi_address_click; // 落地页及门店数据-查看店铺地址
        private Long luban_order_cnt; // 落地页及门店数据-鲁班订单量
        private Float luban_order_stat_amount; // 落地页及门店数据-鲁班订单金额
        private Float luban_order_roi; // 落地页及门店数据-鲁班ROI
        private Long luban_live_enter_cnt; // 落地页及门店数据-直播间观看数
        private Long live_watch_one_minute_count; // 落地页及门店数据-直播间超过1分钟观看数
        private Long luban_live_follow_cnt; // 落地页及门店数据-直播间关注数
        private Long live_fans_club_join_cnt; // 落地页及门店数据-直播间加入粉丝团
        private Long luban_live_comment_cnt; // 落地页及门店数据-直播间评论数
        private Long luban_live_share_cnt; // 落地页及门店数据-直播间分享数
        private Long luban_live_gift_cnt; // 落地页及门店数据-直播间打赏次数
        private Float luban_live_gift_amount; // 落地页及门店数据-直播间礼物总金额
        private Long luban_live_slidecart_click_cnt; // 落地页及门店数据-直播间查看购物车数
        private Long luban_live_click_product_cnt; // 落地页及门店数据-直播间点击商品数
        private Long luban_live_pay_order_count; // 落地页及门店数据-直播间订单量
        private Float luban_live_pay_order_stat_cost; // 落地页及门店数据-直播间订单金额
        private Long live_component_click_count; // 落地页及门店数据-直播间组件点击数
        private Float live_component_click_cost; // 落地页及门店数据-直播间组件点击成本
        private Float live_component_click_rate; // 落地页及门店数据-直播间组件点击率
        private Long wechat_login_count; // 落地页及门店数据-微信内注册数
        private Long attribution_wechat_login_30d_count; // 落地页及门店数据-微信内注册数(计费时间)
        private Float wechat_login_cost; // 落地页及门店数据-微信内注册成本
        private Float attribution_wechat_login_30d_cost; // 落地页及门店数据-微信内注册成本(计费时间)
        private Long wechat_first_pay_count; // 落地页及门店数据-微信内首次付费数
        private Long attribution_wechat_first_pay_30d_count; // 落地页及门店数据-微信内首次付费数(计费时间)
        private Float wechat_first_pay_cost; // 落地页及门店数据-微信内首次付费成本
        private Float attribution_wechat_first_pay_30d_cost; // 落地页及门店数据-微信内首次付费成本(计费时间)
        private Float wechat_first_pay_rate; // 落地页及门店数据-微信内首次付费率
        private Float attribution_wechat_first_pay_30d_rate; // 落地页及门店数据-微信内首次付费率(计费时间)
        private Float wechat_first_pay_amount; // 落地页及门店数据-微信内首次付费金额
        private Float attribution_wechat_pay_30d_amount; // 落地页及门店数据-微信内首次付费金额(计费时间)
        private Float attribution_wechat_pay_30d_roi; // 落地页及门店数据-微信内首次付费ROI
        private Long phone_effective; // 落地页及门店数据-智能电话-有效接通
        private Long total_play; // 视频数据-播放数
        private Long valid_play; // 视频数据-有效播放数
        private Float valid_play_cost; // 视频数据-有效播放成本
        private Float valid_play_rate; // 视频数据-有效播放率
        private Long play_25_feed_break; // 视频数据-25%进度播放数
        private Long play_50_feed_break; // 视频数据-50%进度播放数
        private Long play_75_feed_break; // 视频数据-75%进度播放数
        private Long play_100_feed_break; // 视频数据-99%进度播放数
        private Float average_play_time_per_play; // 视频数据-平均单次播放时长，单位：秒
        private Float play_over_rate; // 视频数据-播完率
        private Float wifi_play_rate; // 视频数据-WiFi播放占比
        private Long wifi_play; // 视频数据-WiFi播放量
        private Long play_duration_sum; // 视频数据-播放时长，单位ms
        private Long advanced_creative_phone_click; // 附加创意-附加创意电话按钮点击
        private Long advanced_creative_counsel_click; // 附加创意-附加创意在线咨询点击
        private Long advanced_creative_form_click; // 附加创意-附加创意表单按钮点击
        private Long advanced_creative_coupon_addition; // 附加创意-附加创意卡券领取
        private Long advanced_creative_form_submit; // 附加创意-附加创意表单提交
        private Long card_show; // 视频数据3秒卡片展现
        private Long share; // 互动数据-分享数
        private Long comment; // 互动数据-评论数
        private Long like; // 互动数据-点赞数
        private Long follow; // 互动数据-新增关注数
        private Long home_visited; // 互动数据-主页访问量
        private Long ies_challenge_click; // 互动数据-挑战赛查看数
        private Long ies_music_click; // 互动数据-音乐查看数
        private Long location_click; // 互动数据-POI点击数
        private Long message_action; // 互动数据-私信数
        private Long click_landing_page; // 互动数据-推广页访问量
        private Long click_shopwindow; // 互动数据-主页商品橱窗访问量
        private Long click_website; // 互动数据-主页内落地页访问量（主页官网访问量）
        private Long click_download; // 互动数据-主页下载链接点击量
        private Long click_call_dy; // 互动数据-主页内电话拨打点击量
        private Long submit_certification_count; // 提交身份认证数-出行行业司机在应用内提交身份认证的次数
        private Long approval_count; // 通过身份认证数-出行行业司机在应用内成功通过身份认证的次数
        private Long first_rental_order_count; // 乘客首次下单数-出行行业乘客在应用内首次成功下单的数量
        private Long first_order_count; // 司机首次完单数-出行行业司机在应用内首次成功完成订单的数量
        private Long commute_first_pay_count; // 乘客首次支付数-出行行业乘客在应用内首次成功支付订单的数量
        private Long attribution_active_pay_intra_one_day_count; // 游戏行业-激活后24h首次付费数
        private Float attribution_active_pay_intra_one_day_cost; // 游戏行业-激活后24h首次付费成本
        private Float attribution_active_pay_intra_one_day_rate; // 游戏行业-激活后24h首次付费率
        private Float attribution_active_pay_intra_one_day_amount; // 游戏行业-激活后24h付费金额
        private Float attribution_active_pay_intra_one_day_roi; // 游戏行业-激活后24小时付费roi
        private Float attribution_micro_game_0d_ltv; // 小游戏当日LTV-所选时间范围内的激活用户在激活当日的变现金额
        private Float attribution_micro_game_3d_ltv; // 小游戏激活后三日LTV-所选时间范围内的激活用户在激活后三日内的变现金额
        private Float attribution_micro_game_7d_ltv; // 小游戏激活后七日LTV-所选时间范围内的激活用户在激活后七日内的变现金额
        private Float attribution_micro_game_0d_roi; // 小游戏当日广告变现ROI-所选时间范围内的激活用户在激活当日的广告变现ROI
        private Float attribution_micro_game_3d_roi; // 小游戏激活后三日广告变现ROI-所选时间范围内的激活用户在激活后三日内的广告变现ROI
        private Float attribution_micro_game_7d_roi; // 小游戏激活后七日广告变现ROI-所选时间范围内的激活用户在激活后七日内的广告变现ROI
        private Float attribution_game_in_app_ltv_1day; // 当日付费金额-所选时间范围内的激活用户，激活当日在APP内的付费金额
        private Float attribution_game_in_app_ltv_7days; // 激活后六日付费金额
        private Float attribution_game_in_app_roi_1day; // 当日付费ROI
        private Float attribution_game_in_app_roi_3days; // 激活后二日付费ROI
        private Float attribution_game_in_app_roi_5days; // 激活后四日付费ROI
        private Float attribution_game_in_app_roi_7days; // 激活后六日付费ROI
        private Float attribution_active_pay_7d_count; // 激活后七日首次付费数
        private Float attribution_active_pay_7d_rate; // 激活后七日首次付费率
        private Float attribution_game_in_app_ltv_2days; // 激活后一日付费金额
        private Float attribution_game_in_app_ltv_4days; // 激活后三日付费金额
        private Float attribution_game_in_app_ltv_6days; // 激活后五日付费金额
        private Float attribution_game_in_app_ltv_8days; // 激活后七日付费金额
        private Float attribution_game_in_app_roi_2days; // 激活后一日付费ROI
        private Float attribution_game_in_app_roi_4days; // 激活后三日付费ROI
        private Float attribution_game_in_app_roi_6days; // 激活后五日付费ROI
        private Float attribution_game_in_app_roi_8days; // 激活后七日付费ROI
        private Float attribution_active_pay_7d_cost; // 激活后七日首次付费成本
        private Long stat_pay_amount; // 付费金额（回传时间）
        private Float pay_amount_roi; // 付费ROI（回传时间）
        private Long customer_effective; // 有效获客
        private Float stat_union_ltv_0; // 当日LTV
        private Float stat_union_ltv_3; // 激活后三日LTV
        private Float stat_union_ltv_7; // 激活后七日LTV
        private Float union_roi_0; // 当日广告变现ROI
        private Float union_roi_3; // 激活后三日广告变现ROI
        private Float union_roi_7; // 激活后七日广告变现ROI

    }

}
