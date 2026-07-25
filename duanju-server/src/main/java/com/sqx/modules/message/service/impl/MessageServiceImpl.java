package com.sqx.modules.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Query;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.message.dao.MessageInfoDao;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 消息
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageInfoDao, MessageInfo> implements MessageService {

    @Autowired
    private MessageInfoDao messageInfoDao;
    @Autowired
    private UserService userService;

    @Override
    public PageUtils selectMessageList(Map<String,Object> params){
        Long userId = (Long)params.get("userId");
        Integer state = (Integer)params.get("state");
        Integer type = (Integer)params.get("type");
        IPage<MessageInfo> page = this.page(
                new Query<MessageInfo>().getPage(params),
                new QueryWrapper<MessageInfo>()
                        .eq(userId!=null,"user_id", userId)
                        .eq(state!=null,"state", state)
                        .eq(type!=null,"type", type).orderByDesc("create_at")
        );
        List<MessageInfo> records = page.getRecords();
        for (MessageInfo messageInfo:records){
            if(messageInfo.getUserId()!=null){
                messageInfo.setUserEntity(userService.selectUserById(Long.parseLong(messageInfo.getUserId())));
            }
        }
        return new PageUtils(page);
    }

    @Override
    public int saveBody(MessageInfo messageInfo){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        messageInfo.setCreateAt(sdf.format(now));
        return messageInfoDao.insert(messageInfo);
    }

    @Override
    public int update(MessageInfo messageInfo){
        return messageInfoDao.updateById(messageInfo);
    }

    @Override
    public int delete(Long id){
        return messageInfoDao.deleteById(id);
    }

    @Override
    public MessageInfo selectMessageById(Long id){
        return messageInfoDao.selectById(id);
    }




}
