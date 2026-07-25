package com.sqx.modules.invite.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.invite.dao.InviteDao;
import com.sqx.modules.invite.entity.Invite;
import com.sqx.modules.invite.service.InviteMoneyService;
import com.sqx.modules.invite.service.InviteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 邀请记录
 */
@Service
public class InviteServiceImpl extends ServiceImpl<InviteDao, Invite> implements InviteService {


    @Autowired
    private InviteDao inviteDao;
    @Autowired
    private UserService userService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private InviteMoneyService inviteMoneyService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;

    @Override
    public PageUtils selectInviteList(int page,int limit,Integer state,Long userId){
        Page<Map<String,Object>> pages=new Page<>(page,limit);
        if(state==null || state==-1){
            state=null;
        }
        return new PageUtils(inviteDao.selectInviteList(pages,state,userId));
    }


    @Override
    public PageUtils selectInviteUser(int page,int limit,Long userId,Integer state){
        Page<Map<String,Object>> pages=new Page<>(page,limit);
        return new PageUtils(inviteDao.selectInviteUser(pages,userId,state));
    }

    @Override
    public Integer selectInviteByUserIdCountNotTime(Long userId) {
        return inviteDao.selectInviteByUserIdCountNotTime(userId);
    }

    @Override
    public Integer selectInviteByUserIdCount(Long userId, Date startTime, Date endTime) {
        return inviteDao.selectInviteByUserIdCount(userId,startTime,endTime);
    }

    @Override
    public Double selectInviteByUserIdSum(Long userId, Date startTime, Date endTime) {
        return inviteDao.selectInviteByUserIdSum(userId,startTime,endTime);
    }

    @Override
    public Double sumInviteMoney(String time, Integer flag) {
        return inviteDao.sumInviteMoney(time,flag);
    }

    @Override
    public PageUtils inviteAnalysis(int page,int limit, String time, Integer flag) {
        Page<Map<String,Object>> pages=new Page<>(page,limit);
        return new PageUtils(inviteDao.inviteAnalysis(pages,time,flag));
    }

    @Override
    public Integer selectInviteCount(Integer state,Long userId){
        if(state==null || state==-1){
            state=null;
        }
        return inviteDao.selectInviteCount(state,userId);
    }

    @Override
    public Double selectInviteSum(Integer state, Long userId) {
        if(state==null || state==-1){
            state=null;
        }
        return inviteDao.selectInviteSum(state,userId);
    }


    @Transactional
    @Override
    public int saveBody(Long userId, UserEntity userEntity){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(new Date());
        Invite invite=new Invite();
        invite.setState(0);
        invite.setMoney(0.00);
        invite.setUserId(userEntity.getUserId());
        invite.setInviteeUserId(userId);
        invite.setCreateTime(format);
        inviteDao.insert(invite);
        //给被邀请者增加上级id
        UserEntity user=new UserEntity();
        user.setUserId(userId);
        user.setInviterCode(userEntity.getInvitationCode());
        userService.updateById(user);
        return 1;
    }


    @Override
    public void updateInvite(UserEntity userEntity,String format,Long userId,BigDecimal price){
        if(userEntity!=null && userId!=null && price!=null){
            Invite invite1 = inviteDao.selectInviteByUser(userEntity.getUserId(), userId);
            if(invite1==null){
                Invite invite=new Invite();
                invite.setState(0);
                invite.setMoney(0.00);
                invite.setUserId(userEntity.getUserId());
                invite.setInviteeUserId(userId);
                invite.setCreateTime(format);
                inviteDao.insert(invite);
                invite1 = inviteDao.selectInviteByUser(userEntity.getUserId(), userId);
            }
            if(userEntity.getRate()!=null && userEntity.getRate().doubleValue()>0){
                BigDecimal rateMoney = price.multiply(userEntity.getRate()).setScale(2,BigDecimal.ROUND_HALF_UP);
                Double money=rateMoney.doubleValue();
                invite1.setState(1);
                invite1.setMoney(money);
                inviteDao.updateById(invite1);
                inviteMoneyService.updateInviteMoneySum(money,userEntity.getUserId());
//            userMoneyService.selectUserMoneyByUserId(userEntity.getUserId());
//            userMoneyService.updateMoney(1,userEntity.getUserId(),money);
                UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
                userMoneyDetails.setUserId(userEntity.getUserId());
                UserEntity userEntity1 = userService.selectUserById(userId);
                userMoneyDetails.setTitle("[邀请好友]好友名称："+userEntity1.getUserName());
                userMoneyDetails.setContent("增加金额:"+money);
                userMoneyDetails.setType(1);
                userMoneyDetails.setMoney(new BigDecimal(money));
                userMoneyDetails.setCreateTime(format);
                userMoneyDetailsService.save(userMoneyDetails);
//            InviteMoney inviteMoney = inviteMoneyService.selectInviteMoneyByUserId(userEntity.getUserId());
//            inviteMoneyService.updateInviteMoneySum(money,userEntity.getUserId());
            }
        }

    }



}
