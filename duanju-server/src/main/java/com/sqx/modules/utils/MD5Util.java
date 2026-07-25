package com.sqx.modules.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class MD5Util {
    private static final Logger logger = LoggerFactory.getLogger(MD5Util.class);
    static MessageDigest messageDigest = null;

    /**
     * 判断新密码和旧密码是否正确  返回true 和 false
     *
     * @param newStr
     * @param oldMD5Str
     * @return
     */
    public final static boolean checkMD5(String newStr, String oldMD5Str) {
        String temp = encoderByMd5(newStr);
        return temp != null && temp.equals(oldMD5Str);
    }

    /**
     * 对给定的字符串进行加密
     *
     * @param source
     * @return 加密后的16进制的字符串
     */
    public final static String encoderByMd5(String source) {
        String tmp = source.substring(0, 1)
                + source.subSequence(source.length() - 1, source.length());
        tmp = md5(tmp);
        return md5(source + tmp);
    }

    private static String md5(String source) {

        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
                'e', 'f'};
        try {

            byte[] strTemp = source.getBytes();
            // 使用MD5创建MessageDigest对象
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte b : md) {
                str[k++] = hexDigits[b >> 4 & 0xf];
                str[k++] = hexDigits[b & 0xf];
            }

            if (logger.isDebugEnabled()) {
                logger.debug("加密后的字符串：" + new String(str));
            }
            return new String(str);
        } catch (Exception e) {
            logger.error("md5加密出错：" + source, e);
            return null;
        }

    }


    public static String encodeByMD5(String str) {
        try {
            if (messageDigest == null)
                messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            logger.error("NoSuchAlgorithmException caught!", e);

        }
        if (messageDigest == null)
            return "";
        byte[] byteArray = messageDigest.digest();

        StringBuilder md5StrBuff = new StringBuilder();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }

        return md5StrBuff.toString();
    }

    /**
     * MD5加密字符串（32位大写）
     *
     * @param string 需要进行MD5加密的字符串
     * @return 加密后的字符串（大写）
     */
    public static String md5Encrypt32Upper(String string) {
        byte[] hash;
        try {
            //创建一个MD5算法对象，并获得MD5字节数组,16*8=128位
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        }
        //转换为十六进制字符串
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString().toUpperCase();
    }


    public static String encryption(String plain) {
        String re_md5 = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plain.getBytes(StandardCharsets.UTF_8));
            byte[] b = md.digest();

            int i;

            StringBuilder buf = new StringBuilder();
            for (byte value : b) {
                i = value;
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            log.error("MD5Util.encryption error", e);
        }
        return re_md5;
    }


    public static String getMD5Str(Map<String, Object> map, String keyMD5) {
        try {
            List<Map.Entry<String, Object>> infoIds = new ArrayList<>(map.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            infoIds.sort(Map.Entry.comparingByKey());

            // 构造签名键值对的格式
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Object> item : infoIds) {
                String key = item.getKey();
                Object val = item.getValue();
                if (val == null) {
                    continue;
                }
                sb.append(key).append("=").append(val).append("&");
            }
             sb.append(keyMD5);
            if (StrUtil.isBlank(sb)) {
                return null;
            }
            String result = sb.toString();
            System.out.println("result = " + result);
            // 进行MD5加密
            return SecureUtil.md5(result).toUpperCase();
        } catch (Exception e) {
            log.error("MD5加密失败", e);
        }
        return null;
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<String, Object>() {

            {
                put("endTime", 1682061867);
                put("page", 1);
                put("pageSize", 10);
                put("startTime", 0);
                put("timestamp", 1682061867);
                put("token", "__TEST_TOKEN__");
            }
        };

        System.out.println("getMD5Str(map, \"123123\") = " + getMD5Str(map, "123123"));
    }

}
