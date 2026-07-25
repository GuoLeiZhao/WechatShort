package com.sqx.modules.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.sqx.modules.shortUrl.response.IPAddressInfoResponse;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * @description:
 **/
@Slf4j
public class IPUtils {
    /**
     * 对传入ip进行解析
     * 输出所在区域
     *
     * @param ip
     * @return 解析后区域
     */
    public static IPAddressInfoResponse getIpInfo(String ip) {
        try {
            String result = HttpUtil.get("http://ip-api.com/json/" + ip + "?lang=zh-CN", 2000);

            IPAddressInfoResponse info = JSONObject.parseObject(result, IPAddressInfoResponse.class);
            if ("success".equals(info.getStatus())) {
                return info;
            }
            return null;

        } catch (Exception e) {
            log.error("getIpinfo error.", e);
        }
        return null;
    }

    /**
     * 获取客户端ip
     *
     * @param request
     * @return
     */
    public static String getIPAddress(HttpServletRequest request) {
        String ip = "";
        String ipAddresses = request.getHeader("X-Forwarded-For");
        if (StrUtil.isBlank(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }
        if (StrUtil.isBlank(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StrUtil.isBlank(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StrUtil.isBlank(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getHeader("X-Real-IP");
        }
        if (StrUtil.isNotBlank(ipAddresses)) {
            ip = ipAddresses.split(",")[0];
        }
        if (StrUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
