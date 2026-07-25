package com.sqx.modules.invite.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.invite.dao.InviteMoneyDao;
import com.sqx.modules.invite.entity.InviteMoney;
import com.sqx.modules.invite.service.InviteMoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("InviteMoneyService")
public class InviteMoneyServiceImpl extends ServiceImpl<InviteMoneyDao, InviteMoney> implements InviteMoneyService {

	@Autowired
	private InviteMoneyDao inviteMoneyDao;


	@Override
	public InviteMoney selectInviteMoneyByUserId(Long userId) {
		InviteMoney inviteMoney = inviteMoneyDao.selectInviteMoneyByUserId(userId);
		if(inviteMoney==null){
			inviteMoney=new InviteMoney();
			inviteMoney.setCashOut(0.00);
			inviteMoney.setUserId(userId);
			inviteMoney.setMoney(0.00);
			inviteMoney.setMoneySum(0.00);
			inviteMoneyDao.insert(inviteMoney);
		}
		return inviteMoney;
	}

	@Override
	public int updateInviteMoneySum(Double money, Long userId) {
		selectInviteMoneyByUserId(userId);
		return inviteMoneyDao.updateInviteMoneySum(money,userId);
	}

	@Override
	public int updateInviteMoneyCashOut(Double money, Long userId) {
		return inviteMoneyDao.updateInviteMoneySum(money,userId);
	}
}
