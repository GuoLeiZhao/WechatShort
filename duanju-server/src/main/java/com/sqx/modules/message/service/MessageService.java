package com.sqx.modules.message.service;


import com.sqx.common.utils.PageUtils;
import com.sqx.modules.message.entity.MessageInfo;

import java.util.Map;

public interface MessageService {

    PageUtils selectMessageList(Map<String,Object> params);

    int saveBody(MessageInfo messageInfo);

    int update(MessageInfo messageInfo);

    int delete(Long id);

    MessageInfo selectMessageById(Long id);


}
