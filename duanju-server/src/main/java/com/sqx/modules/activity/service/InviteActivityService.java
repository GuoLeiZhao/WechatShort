package com.sqx.modules.activity.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.activity.entity.InviteActivity;
import com.sqx.modules.activity.request.InviteActivityQueryReq;

public interface InviteActivityService extends IService<InviteActivity> {


    Result insert(InviteActivity inviteActivity);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    Result delete(Long id);

    /**
     * 分页查询
     * @param req
     * @return
     */
    IPage<InviteActivity> page(InviteActivityQueryReq req);

    Result receiveByHash(String hash, Long userId);

    Result getByHash(String hash);
}
