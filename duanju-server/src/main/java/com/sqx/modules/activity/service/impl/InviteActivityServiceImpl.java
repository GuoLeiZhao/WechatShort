package com.sqx.modules.activity.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.base.BasePagingRequest;
import com.sqx.common.utils.Query;
import com.sqx.common.utils.Result;
import com.sqx.modules.activity.dao.InviteActivityDao;
import com.sqx.modules.activity.entity.InviteActivity;
import com.sqx.modules.activity.request.InviteActivityQueryReq;
import com.sqx.modules.activity.service.InviteActivityService;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.integral.entity.UserIntegralDetails;
import com.sqx.modules.integral.service.UserIntegralDetailsService;
import com.sqx.modules.integral.service.UserIntegralService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InviteActivityServiceImpl extends ServiceImpl<InviteActivityDao, InviteActivity> implements InviteActivityService {

    private final UserIntegralService userIntegralService;
    private final UserIntegralDetailsService userIntegralDetailsService;
    private final UserMoneyService userMoneyService;
    private final UserService userService;

    @Override
    public Result insert(InviteActivity inviteActivity) {
        if (null == inviteActivity) {
            return Result.error("添加失败，请联系管理员");
        }
        if (null == inviteActivity.getNum() || 0d == inviteActivity.getNum()) {
            return Result.error("添加失败，数量不能为空");
        }
        inviteActivity.setHash(RandomUtil.randomString(6).toUpperCase());
        inviteActivity.setType(null == inviteActivity.getType() ? InviteActivity.Type.POINT : inviteActivity.getType());
        inviteActivity.setStatus(InviteActivity.Status.WAITING);

        return Result.success().setData(this.save(inviteActivity));
    }

    @Override
    public Result delete(Long id) {
        if (null == id || 0L == id) {
            return Result.error("删除失败，请联系管理员");
        }
        InviteActivity inviteActivity = new InviteActivity();
        inviteActivity.setId(id);
        inviteActivity.setDeleted(Boolean.TRUE);
        int rows = super.baseMapper.updateById(inviteActivity);
        if (0 < rows) {
            return Result.success();
        }
        return Result.error("删除失败，请确认活动是否存在");
    }

    @Override
    public IPage<InviteActivity> page(InviteActivityQueryReq req) {
        req.setOrders(Collections.singletonList(new BasePagingRequest.Order("id", false)));
        IPage<InviteActivity> activityIPage = baseMapper.selectPage(
                new Query<InviteActivity>().getPage(req),
                new QueryWrapper<InviteActivity>().lambda()
                        .eq(null != req.getId() && 0L != req.getId(), InviteActivity::getId, req.getId())
                        .eq(null != req.getUserId() && 0L != req.getUserId(), InviteActivity::getUserId, req.getUserId())
                        .eq(null != req.getStatus(), InviteActivity::getStatus, req.getStatus())
                        .eq(null != req.getType(), InviteActivity::getType, req.getType())
                        .eq(InviteActivity::getDeleted, Boolean.FALSE)
        );

        return fillAdditionalInfo(activityIPage);
    }

    private IPage<InviteActivity> fillAdditionalInfo(IPage<InviteActivity> activityIPage) {
        // 翻译 status 和 type
        activityIPage.getRecords().forEach(inviteActivity -> {
            inviteActivity.setStatusDesc(inviteActivity.getStatus().getValue());
            inviteActivity.setTypeDesc(inviteActivity.getType().getValue());
        });

        if (0L != activityIPage.getTotal() && CollUtil.isNotEmpty(activityIPage.getRecords())) {
            List<Long> userIds = activityIPage.getRecords().stream().map(InviteActivity::getUserId).filter(Objects::nonNull).collect(Collectors.toList());
            if (CollUtil.isEmpty(userIds)) {
                return activityIPage;
            }
            // 设置用户名称
            List<UserEntity> userList = userService.queryUserList(userIds);
            if (CollUtil.isEmpty(userList)) {
                return activityIPage;
            }
            Map<Long, UserEntity> idUserMap = userList.stream().collect(Collectors.toMap(UserEntity::getUserId, userEntity -> userEntity, (o1, o2) -> o1));
            activityIPage.getRecords().stream().filter(inviteActivity -> null != inviteActivity.getUserId() && null != idUserMap.get(inviteActivity.getUserId()))
                    .forEach(inviteActivity -> inviteActivity.setUserName(idUserMap.get(inviteActivity.getUserId()).getUserName()));
        }
        return activityIPage;
    }

    @Override
    public Result receiveByHash(String hash, Long userId) {
        InviteActivity inviteActivity = baseMapper.selectOne(new QueryWrapper<InviteActivity>().lambda()
                .eq(InviteActivity::getHash, hash)
                .eq(InviteActivity::getDeleted, Boolean.FALSE)
                .eq(InviteActivity::getStatus, InviteActivity.Status.WAITING)
        );

        if (null == inviteActivity) {
            return Result.error("活动不存在或已被领取");
        }

        inviteActivity.setStatus(InviteActivity.Status.FINISHED);
        inviteActivity.setReceiveTime(DateUtil.now());
        inviteActivity.setUserId(userId);

        if (InviteActivity.Type.INTEGRAL == inviteActivity.getType()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            int num = inviteActivity.getNum().intValue();
            userIntegralService.updateIntegral(UserIntegralDetails.Constants.TYPE.INCREASE, userId, num);
            UserIntegralDetails userIntegralDetails = new UserIntegralDetails();
            userIntegralDetails.setClassify(UserIntegralDetails.Constants.CLASSIFY.ACTIVITY);
            userIntegralDetails.setContent(inviteActivity.getTitle() + " 活动获得:" + num + "积分");
            userIntegralDetails.setCreateTime(sdf.format(new Date()));
            userIntegralDetails.setNum(num);
            userIntegralDetails.setType(UserIntegralDetails.Constants.TYPE.INCREASE);
            userIntegralDetails.setUserId(userId);
            userIntegralDetailsService.save(userIntegralDetails);
            return Result.success().setData(this.updateById(inviteActivity));
        }

        if (InviteActivity.Type.POINT == inviteActivity.getType()) {
            // 用户看点增加逻辑
            Boolean updateRes = userMoneyService.updateMoneyByUserId(userId, UserMoneyDetails.Constant.Type.RECHARGE, inviteActivity.getNum(),
                    inviteActivity.getTitle() + "活动领取",
                    inviteActivity.getTitle() + " 活动获得 " + inviteActivity.getNum() + " 看点");
            return Result.success().setData(this.updateById(inviteActivity));
        }

        return Result.error("活动类型错误，请联系管理员");
    }

    @Override
    public Result getByHash(String hash) {
        InviteActivity inviteActivity = baseMapper.selectOne(new QueryWrapper<InviteActivity>().lambda()
                .eq(InviteActivity::getDeleted, Boolean.FALSE)
                .eq(InviteActivity::getStatus, InviteActivity.Status.WAITING)
                .eq(InviteActivity::getHash, hash)
        );

        if (null == inviteActivity) {
            return Result.error("活动不存在或已被领取");
        }

        return Result.success().setData(inviteActivity);
    }
}
