package com.sqx.modules.douyin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.sqx.common.utils.RedisUtils;
import com.sqx.modules.douyin.request.thirdparty.ComponentTicketReq;
import com.sqx.modules.douyin.service.DouyinThirdPartyService;
import com.sqx.modules.douyin.utils.douyinThirdParty.MsgDecrypt;
import com.sqx.modules.douyin.utils.douyinThirdParty.ServerVerification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinThirdPartyServiceImpl implements DouyinThirdPartyService {

    static final String COMPONENT_TICKET = "douyin:thirdparty:component_ticket";
    private static final String tpToken = "__DOUYIN_TP_TOKEN__";
    private static final String encodingAesKey = "__DOUYIN_TP_AESKEY__";

    private final RedisUtils redisUtils;

    @Override
    public void getComponentTicket(ComponentTicketReq req) {

        try {
            ServerVerification serverVerification = new ServerVerification();
            String clientMsgSignature = req.getMsgSignature();
            String timestamp = req.getTimestamp();
            String nonce = req.getNonce();
            String encrypt = req.getEncrypt();

            // 服务端生成新签名
            String newMsgSignature = serverVerification.getMsgSignature(tpToken, timestamp, nonce, encrypt);

            // 验证签名是否一致
            boolean equals = clientMsgSignature.equals(newMsgSignature);

            if (equals) {
                MsgDecrypt msgDecrypt = new MsgDecrypt(encodingAesKey);
                String msg = msgDecrypt.decrypt(encrypt);
                JSONObject jsonObject = JSONObject.parseObject(msg);
                String ticket = jsonObject.getString("Ticket");
                log.info("getComponentTicket ticket:{}", ticket);
                log.info("getComponentTicket msgDecrypt decrypt:{}", msg);
                if (StrUtil.isNotBlank(ticket)) {
                    redisUtils.set(COMPONENT_TICKET, ticket, 3 * 60 * 60);
                }
            } else {
                log.info("getComponentTicket clientMsgSignature equals: false");
            }

        } catch (Exception e) {
            log.error("getComponentTicket", e);
        }

    }

}
