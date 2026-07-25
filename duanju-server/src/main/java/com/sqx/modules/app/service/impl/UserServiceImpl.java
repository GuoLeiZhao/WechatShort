package com.sqx.modules.app.service.impl;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.ClientException;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.getui.push.v2.sdk.ApiHelper;
import com.getui.push.v2.sdk.GtApiConfiguration;
import com.getui.push.v2.sdk.api.PushApi;
import com.getui.push.v2.sdk.common.ApiResult;
import com.getui.push.v2.sdk.dto.req.Audience;
import com.getui.push.v2.sdk.dto.req.message.PushDTO;
import com.getui.push.v2.sdk.dto.req.message.PushMessage;
import com.getui.push.v2.sdk.dto.req.message.android.GTNotification;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.sqx.common.base.BaseDropList;
import com.sqx.common.utils.ConfigConstant;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.MsgDao;
import com.sqx.modules.app.dao.UserDao;
import com.sqx.modules.app.entity.AppUserInfo;
import com.sqx.modules.app.entity.Msg;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserVip;
import com.sqx.modules.app.request.SmsHengXunSendReq;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.app.service.UserVipService;
import com.sqx.modules.app.utils.JwtUtils;
import com.sqx.modules.app.utils.UserConstantInterface;
import com.sqx.modules.bilibili.service.BilibiliService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.douyin.request.Code2SessionRequest;
import com.sqx.modules.douyin.response.Code2SessionResponse;
import com.sqx.modules.douyin.service.DouyinService;
import com.sqx.modules.file.utils.Md5Utils;
import com.sqx.modules.invite.service.InviteService;
import com.sqx.modules.utils.HttpClientUtil;
import com.sqx.modules.utils.InvitationCodeUtil;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weixin.popular.api.SnsAPI;
import weixin.popular.bean.sns.SnsToken;
import weixin.popular.util.JsonUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 用户
 *
 */

@Service("userService")
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

//    private final CommonInfoService commonInfoService;
//    private final MsgDao msgDao;
//    private final JwtUtils jwtUtils;
//	private final InviteService inviteService;
//    private final UserMoneyService userMoneyService;
//    private final UserVipService userVipService;
//    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
////    private final WechatOfficeAccountService wechatOfficeAccountService;


    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private MsgDao msgDao;
    @Autowired
    private JwtUtils jwtUtils;
    private int number = 1;
    @Autowired
    private InviteService inviteService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private UserVipService userVipService;
    @Autowired
    private UserDao userDao;
    @Autowired
    private DouyinService douyinService;
    @Autowired
    private BilibiliService bilibiliService;


    @Override
    public UserEntity queryByPhone(String phone) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("phone", phone).orderByAsc("user_id").last("limit 1"));
    }

    @Override
    public UserEntity queryByOpenId(String openId) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("open_id", openId).orderByAsc("user_id").last("limit 1"));
    }

    @Override
    public UserEntity queryByWxId(String wxId) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("wx_id", wxId).orderByAsc("user_id").last("limit 1"));
    }

    @Override
    public UserEntity queryByWxUnionId(String wxUnionId) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("wx_union_id", wxUnionId).orderByAsc("user_id").last("limit 1"));
    }

    @Override
    public UserEntity queryByWxOpenId(String openId) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("wx_open_id", openId).orderByAsc("user_id").last("limit 1"));
    }

    @Override
    public UserEntity queryByAppleId(String appleId) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("apple_id", appleId).orderByAsc("user_id").last("limit 1"));
    }

    @Override
    public UserEntity queryByUserId(Long userId) {
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("user_id", userId));
    }

    @Override
    public UserEntity queryByInvitationCode(String invitationCode){
        return baseMapper.selectOne(new QueryWrapper<UserEntity>().eq("invitation_code", invitationCode));
    }

    @Override
    public Result updatePhone(String phone, String msg, Long userId) {
        Msg msg1 = msgDao.findByPhoneAndCode(phone, msg);
        //校验短信验证码
        if (msg1 != null) {
            msgDao.deleteById(msg1.getId());
            UserEntity userInfo = queryByPhone(phone);
            if (userInfo != null) {
                return Result.error("手机号已经被其他账号绑定");
            } else {
                UserEntity one = baseMapper.selectById(userId);
                one.setPhone(phone);
                baseMapper.updateById(one);
                return Result.success();
            }
        }
        return Result.error("验证码不正确");
    }

    @Override
    public Result iosRegister(String appleId) {
        if (StringUtils.isEmpty(appleId)) {
            return Result.error("账号信息获取失败，请退出重试！");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        // 根据返回的user实体类，判断用户是否是新用户，不是的话，更新最新登录时间，是的话，将用户信息存到数据库
        UserEntity userInfo = queryByAppleId(appleId);
        if (userInfo != null) {
            if (userInfo.getStatus().equals(2)) {
                return Result.error("账号已被封禁，请联系客服处理！");
            }
            userInfo.setUpdateTime(date);
            baseMapper.updateById(userInfo);
            //返回用户信息
            UserEntity user = queryByAppleId(appleId);
            return getResult(user);
        } else {
            return Result.error(-200, "请先绑定手机号账号！");
        }
    }

    @Override
    public Result wxLogin(String code) {
        try {
            String appid = ConfigConstant.App1.APP_ID;
            String secret = ConfigConstant.App1.SECRET;
            // 配置请求参数
            Map<String, String> param = new HashMap<>();
            param.put("appid", appid);
            param.put("secret", secret);
            param.put("js_code", code);
            param.put("grant_type", UserConstantInterface.WX_LOGIN_GRANT_TYPE);
            param.put("scope", "snsapi_userinfo");
            // 发送请求
            String wxResult = HttpClientUtil.doGet(UserConstantInterface.WX_LOGIN_URL, param);
            log.info(wxResult);
            JSONObject jsonObject = JSONObject.parseObject(wxResult);
            // 获取参数返回的
            String session_key = jsonObject.get("session_key").toString();
            String open_id = jsonObject.get("openid").toString();
            UserEntity userEntity = queryByOpenId(open_id);
            Map<String, String> map = new HashMap<>();
            // 封装返回小程序
            map.put("session_key", session_key);
            map.put("open_id", open_id);
            if (jsonObject.get("unionid") != null) {
                String unionid = jsonObject.get("unionid").toString();
                map.put("unionid", unionid);
            } else {
                map.put("unionid", "-1");
            }
            String value = commonInfoService.findOne(237).getValue();
            if("是".equals(value)){
                if (userEntity == null || StringUtils.isEmpty(userEntity.getPhone())) {
                    map.put("flag", "1");
                } else {
                    map.put("flag", "2");
                }
            }else{
                map.put("flag", "2");
            }
            return Result.success("登陆成功").put("data", map);
        } catch (Exception e) {
            System.err.println(e.toString());
            return Result.success("登录失败！");
        }
    }

    @Override
    public Result dyLogin(String app, Code2SessionRequest request) {
        Code2SessionResponse response = douyinService.code2Session(app, request);
        log.info("dyLoginRequest:{}", request);
        log.info("dyLoginResponse:{}", response);
        String unionid = response.getUnionid();
        String openId = response.getOpenid();
        String anonymousOpenid = response.getAnonymous_openid();
        if (StrUtil.isNotBlank(anonymousOpenid)) {
            log.info("dyLoginAnonymousOpenid:{}", anonymousOpenid);
        }
        //校验 openId 是否存在
        UserEntity userInfo = queryByOpenId(openId);
        String now = DateUtil.now();
        if (userInfo != null) {
            if (userInfo.getStatus().equals(2)) {
                return Result.error("账号已被封禁，请联系客服处理！");
            }
            UserEntity userInfoUpdate = new UserEntity();
            userInfoUpdate.setUserId(userInfo.getUserId());
            userInfoUpdate.setUpdateTime(now);
            baseMapper.updateById(userInfoUpdate);
            return getResult(userInfo);
        } else {
            //没有则生成新账号
            userInfo = new UserEntity();
            userInfo.setUserName("抖音用户" + RandomUtil.randomString(6));
            userInfo.setOpenId(openId);
            userInfo.setInviterCode(commonInfoService.findOne(88).getValue());
            userInfo.setCreateTime(now);
            userInfo.setPlatform("抖音小程序");
            userInfo.setStatus(1);
            userInfo.setRate(new BigDecimal(commonInfoService.findOne(80).getValue()));
            userInfo.setTtOptions(request.getTtOptions());
            userInfo.setTtClickId(request.getTtClickId());
            userInfo.setTtLinkId(request.getTtLinkId());
            baseMapper.insert(userInfo);
//            userInfo.setInvitationCode(InvitationCodeUtil.toSerialCode(userInfo.getUserId()));
//            baseMapper.updateById(userInfo);
            return getResult(userInfo);
        }
    }

    @Override
    public Result bilibiliLogin(String app, com.sqx.modules.bilibili.request.Code2SessionRequest request) {
        com.sqx.modules.bilibili.response.Code2SessionResponse response = bilibiliService.code2Session(app, request);
        log.info("blLoginRequest:{}", request);
        log.info("blLoginResponse:{}", response);
        String openId = response.getOpenid();

        //校验 openId 是否存在
        UserEntity userInfo = queryByOpenId(openId);
        String now = DateUtil.now();
        if (userInfo != null) {
            if (userInfo.getStatus().equals(2)) {
                return Result.error("账号已被封禁，请联系客服处理！");
            }
            UserEntity userInfoUpdate = new UserEntity();
            userInfoUpdate.setUserId(userInfo.getUserId());
            userInfoUpdate.setUpdateTime(now);
            baseMapper.updateById(userInfoUpdate);
            return getResult(userInfo);
        } else {
            //没有则生成新账号
            userInfo = new UserEntity();
            userInfo.setUserName("B站用户" + RandomUtil.randomString(6));
            userInfo.setOpenId(openId);
            userInfo.setInviterCode(commonInfoService.findOne(88).getValue());
            userInfo.setCreateTime(now);
            userInfo.setPlatform("B站小程序");
            userInfo.setStatus(1);
            userInfo.setRate(new BigDecimal(commonInfoService.findOne(80).getValue()));
            baseMapper.insert(userInfo);
            return getResult(userInfo);
        }
    }

    @Override
    public Result wxRegister(UserEntity userInfo1) {
        if (StringUtils.isEmpty(userInfo1.getOpenId())) {
            return Result.error("账号信息获取失败，请退出重试！");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        // 根据返回的user实体类，判断用户是否是新用户，不是的话，更新最新登录时间，是的话，将用户信息存到数据库
        UserEntity userInfo = queryByOpenId(userInfo1.getOpenId());
        if (userInfo != null) {
            if (userInfo.getStatus().equals(2)) {
                return Result.error("账号已被封禁，请联系客服处理！");
            }
            /*if (StringUtils.isNotEmpty(userInfo1.getUserName())) {
                userInfo.setUserName(userInfo1.getUserName());
            }
            if (StringUtils.isNotEmpty(userInfo1.getAvatar())) {
                userInfo.setAvatar(userInfo1.getAvatar());
            }
            if (StringUtils.isNotEmpty(userInfo1.getPhone())) {
                userInfo.setPhone(userInfo1.getPhone());
            }*/
            userInfo.setUpdateTime(date);
            baseMapper.updateById(userInfo);
        } else {
            //判断是否在app登陆过  手机号是否有账号
            UserEntity userByMobile = queryByPhone(userInfo1.getPhone());
            if (userByMobile != null) {
                //有账号则绑定账号
                userByMobile.setOpenId(userInfo1.getOpenId());
                baseMapper.updateById(userByMobile);
                if (userByMobile.getStatus().equals(2)) {
                    return Result.error("账号已被封禁，请联系客服处理！");
                }
            } else {
                if(StringUtils.isEmpty(userInfo1.getInviterCode())){
                    userInfo1.setInviterCode(commonInfoService.findOne(88).getValue());
                }
                UserEntity userEntity = queryByInvitationCode(userInfo1.getInviterCode());
                //没有则生成新账号
                userInfo1.setCreateTime(date);
                userInfo1.setPlatform("小程序");
                userInfo1.setStatus(1);
                // userInfo1.setPassword(DigestUtils.sha256Hex(userInfo1.getPhone()));
                userInfo1.setRate(new BigDecimal(commonInfoService.findOne(80).getValue()));
                baseMapper.insert(userInfo1);
                userInfo1.setInvitationCode(InvitationCodeUtil.toSerialCode(userInfo1.getUserId()));
                baseMapper.updateById(userInfo1);
                if(userEntity!=null){
                    inviteService.saveBody(userInfo1.getUserId(),userEntity);
                }
//                userMoneyService.selectUserMoneyByUserId(userInfo1.getUserId());
            }
        }
        //返回用户信息
        UserEntity user = queryByOpenId(userInfo1.getOpenId());
        return getResult(user);
    }


    @Override
    public Result wxBindMobile(String phone, String code, String wxOpenId, String token, String platform, Integer sysPhone) {
        Msg byPhoneAndCode = msgDao.findByPhoneAndCode(phone, code);
        if (byPhoneAndCode == null) {
            return Result.error("验证码错误");
        }
        msgDao.deleteById(byPhoneAndCode.getId());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(new Date());
        UserEntity userInfo = queryByPhone(phone);
        if (userInfo != null) {
            if (StringUtils.isNotEmpty(userInfo.getWxOpenId())) {
                return Result.error("当前手机号已经被其他微信绑定");
            }
            //小程序登陆过
            userInfo.setWxOpenId(wxOpenId);
            String s = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + wxOpenId);
            AppUserInfo user = JsonUtil.parseObject(s, AppUserInfo.class);
            if (user != null && user.getNickname() != null) {
                if (user.getHeadimgurl() != null) {
                    userInfo.setAvatar(user.getHeadimgurl());
                }
                userInfo.setSex(user.getSex());
                if (user.getNickname() != null) {
                    userInfo.setUserName(user.getNickname().replaceAll("(\\d{3})\\d*([0-9a-zA-Z]{4})", "$1****$2"));
                }
            }
            baseMapper.updateById(userInfo);
        } else {
            //小程序没有登陆过
            userInfo = new UserEntity();
            String s = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + wxOpenId);
            AppUserInfo user = JsonUtil.parseObject(s, AppUserInfo.class);
            if (user != null && user.getNickname() != null) {
                if (user.getHeadimgurl() != null) {
                    userInfo.setAvatar(user.getHeadimgurl());
                }
                userInfo.setSex(user.getSex());
                if (user.getNickname() != null) {
                    userInfo.setUserName(user.getNickname().replaceAll("(\\d{3})\\d*([0-9a-zA-Z]{4})", "$1****$2"));
                }
            }
            userInfo.setWxOpenId(wxOpenId);
            userInfo.setPhone(phone);
            userInfo.setPlatform(platform);
            userInfo.setCreateTime(time);
            userInfo.setSysPhone(sysPhone);
            userInfo.setStatus(1);
            userInfo.setUpdateTime(time);
            userInfo.setRate(new BigDecimal(commonInfoService.findOne(80).getValue()));
            baseMapper.insert(userInfo);
        }
        UserEntity userEntity = queryByWxOpenId(userInfo.getWxOpenId());
        return getResult(userEntity);
    }

    @Override
    public Result iosBindMobile(String phone, String code, String appleId, String platform, Integer sysPhone) {
        Msg byPhoneAndCode = msgDao.findByPhoneAndCode(phone, code);
        if (byPhoneAndCode == null) {
            return Result.error("验证码错误");
        }
        msgDao.deleteById(byPhoneAndCode.getId());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(new Date());
        UserEntity userInfo = queryByPhone(phone);
        if (userInfo != null) {
            if (StringUtils.isNotEmpty(userInfo.getAppleId())) {
                return Result.error("当前手机号已经被其他苹果绑定");
            }
            userInfo.setAppleId(appleId);
            userInfo.setUpdateTime(simpleDateFormat.format(new Date()));
            baseMapper.updateById(userInfo);
        } else {
            userInfo = new UserEntity();
            userInfo.setSex(0);
            userInfo.setUserName(phone.replaceAll("(\\d{3})\\d*([0-9a-zA-Z]{4})", "$1****$2"));
            userInfo.setPhone(phone);
            userInfo.setPlatform(platform);
            userInfo.setCreateTime(time);
            userInfo.setSysPhone(sysPhone);
            userInfo.setStatus(1);
            userInfo.setUpdateTime(time);
            userInfo.setRate(new BigDecimal(commonInfoService.findOne(80).getValue()));
            baseMapper.insert(userInfo);
        }
        UserEntity userEntity = queryByAppleId(userInfo.getAppleId());
        return getResult(userEntity);
    }


    @Override
    public Result wxAppLogin(String wxOpenId, String token) {
        UserEntity userEntity = queryByWxOpenId(wxOpenId);
        if (userEntity != null) {
            if (userEntity.getStatus().equals(2)) {
                return Result.error("账号已被禁用，请联系客服处理！");
            }
            String s = HttpClientUtil.doGet("https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + wxOpenId);
            AppUserInfo user = JsonUtil.parseObject(s, AppUserInfo.class);
            if (user != null && user.getNickname() != null) {
                if (user.getHeadimgurl() != null) {
                    userEntity.setAvatar(user.getHeadimgurl());
                }
                userEntity.setSex(user.getSex());
                if (user.getNickname() != null) {
                    userEntity.setUserName(user.getNickname().replaceAll("(\\d{3})\\d*([0-9a-zA-Z]{4})", "$1****$2"));
                }
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userEntity.setUpdateTime(sdf.format(new Date()));
            baseMapper.updateById(userEntity);
            return getResult(userEntity);
        } else {
            return Result.error(-200, "请先绑定手机号账号！");
        }
    }


    @Override
    public Result registerCode(String phone, String msg, String platform, Integer sysPhone,String password,String inviterCode,String wxId) {
        //校验手机号是否存在
        UserEntity userInfo = queryByPhone(phone);
        if (userInfo != null) {
            if(StringUtils.isNotEmpty(password)){
                //密码登录
                if(!userInfo.getPassword().equals(DigestUtils.sha256Hex(password))){
                    return Result.error("账号或密码不正确！");
                }
            }else if(StringUtils.isNotEmpty(msg)){
                //验证码登录
                Msg msg1 = msgDao.findByPhoneAndCode(phone, msg);
                if(msg1==null){
                    return Result.error("验证码不正确！");
                }
                msgDao.deleteById(msg1.getId());
            }else{
                return Result.error("登录失败，请刷新页面重试！");
            }
            if (userInfo.getStatus().equals(2)) {
                return Result.error("账号已被禁用，请联系客服处理！");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if(StringUtils.isNotEmpty(wxId) && StringUtils.isNotEmpty(userInfo.getWxId()) && !wxId.equals(userInfo.getWxId())){
                return Result.error("当前手机号已经绑定过了，请更换其他手机号！");
            }
            if(StringUtils.isNotEmpty(wxId)){
                userInfo.setWxId(wxId);
            }
            userInfo.setUpdateTime(sdf.format(new Date()));
            baseMapper.updateById(userInfo);
            return getResult(userInfo);
        } else {
            if(StringUtils.isEmpty(msg)){
                return Result.error("手机号未注册！");
            }
            Msg msg1 = msgDao.findByPhoneAndCode(phone, msg);
            if(msg1==null){
                return Result.error("验证码不正确！");
            }
            userInfo = new UserEntity();
            UserEntity userEntity=null;
            if(StringUtils.isNotEmpty(inviterCode)){
                userEntity = queryByInvitationCode(inviterCode);
                if(userEntity==null){
                    return Result.error("邀请码不正确！");
                }
            }else{
                userInfo.setInviterCode(commonInfoService.findOne(88).getValue());
                userEntity = queryByInvitationCode(userInfo.getInviterCode());
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = simpleDateFormat.format(new Date());
            if(StringUtils.isNotEmpty(wxId)){
                userInfo.setWxId(wxId);
            }
            userInfo.setPhone(phone);
            userInfo.setUserName(phone.replaceAll("(\\d{3})\\d*([0-9a-zA-Z]{4})", "$1****$2"));
            userInfo.setPlatform(platform);
            userInfo.setCreateTime(time);
            userInfo.setSysPhone(sysPhone);
            userInfo.setPassword(DigestUtils.sha256Hex(phone));
            userInfo.setStatus(1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userInfo.setUpdateTime(sdf.format(new Date()));
            userInfo.setRate(new BigDecimal(commonInfoService.findOne(80).getValue()));
            baseMapper.insert(userInfo);
            userInfo.setInvitationCode(InvitationCodeUtil.toSerialCode(userInfo.getUserId()));
            baseMapper.updateById(userInfo);
            msgDao.deleteById(msg1.getId());
            if(userEntity!=null){
                inviteService.saveBody(userInfo.getUserId(),userEntity);
            }
            return getResult(userInfo);
        }
    }


    @Override
    public Result bindWxOpenPhone(Long userId, String phone, String msg) {
        Msg byPhoneAndCode = msgDao.findByPhoneAndCode(phone, msg);
        if (byPhoneAndCode == null) {
            return Result.error("验证码错误");
        }
        msgDao.deleteById(byPhoneAndCode.getId());
        UserEntity userEntity = queryByPhone(phone);
        if (userEntity != null) {
            return Result.error("当前手机号已经绑定过了，请直接登录！");
        }
        UserEntity byId = baseMapper.selectById(userId);
        byId.setPhone(phone);
        baseMapper.updateById(byId);
        return Result.success();
    }

    @Override
    public Result forgetPwd(String pwd, String phone, String msg) {
        try {
            Msg byPhoneAndCode = msgDao.findByPhoneAndCode(phone, msg);
            //校验短信验证码
            if (byPhoneAndCode == null) {
                return Result.error("验证码不正确");
            }
            UserEntity userByPhone = queryByPhone(phone);
            userByPhone.setPassword(DigestUtils.sha256Hex(pwd));
            msgDao.deleteById(byPhoneAndCode.getId());
            baseMapper.updateById(userByPhone);
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("服务器内部错误");
        }
    }




    @Override
    public Result login(String phone, String pwd) {
        UserEntity userEntity = queryByPhone(phone);
        if (userEntity == null) {
            return Result.error("手机号未注册！");
        }
        if (!userEntity.getPassword().equals(DigestUtils.sha256Hex(pwd))) {
            return Result.error("密码不正确！");
        }
        if (userEntity.getStatus().equals(2)) {
            return Result.error("账号已被禁用，请联系客服处理！");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        userEntity.setUpdateTime(sdf.format(new Date()));
        baseMapper.updateById(userEntity);
        return getResult(userEntity);
    }


    @Override
    public Result getResult(UserEntity user) {
        //生成token
        String token = jwtUtils.generateToken(user.getUserId());
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("expire", jwtUtils.getExpire());
        map.put("user", user);
        return Result.success(map);
    }


    @Override
    public Result sendMsg(String phone, String state) {
        int code = (int) ((Math.random() * 9 + 1) * 100000);
        System.out.println("sendMsg code is " + code);
        SmsSingleSenderResult result = null;
        if ("bindWx".equals(state)) {
            UserEntity userByPhone = queryByPhone(phone);
            if (userByPhone != null && StringUtils.isNotEmpty(userByPhone.getWxOpenId())) {
                return Result.error("当前手机号已被其他微信账号绑定");
            }
        } else if ("bindIos".equals(state)) {
            UserEntity userByPhone = queryByPhone(phone);
            if (userByPhone != null && StringUtils.isNotEmpty(userByPhone.getAppleId())) {
                return Result.error("当前手机号已被其他苹果账号绑定");
            }
        }else if("login".equals(state)){
            UserEntity userByPhone = queryByPhone(phone);
            if(userByPhone!=null){
                return Result.error("当前手机号已注册！");
            }
        }else if("forget".equals(state)){
            UserEntity userByPhone = queryByPhone(phone);
            if(userByPhone==null){
                return Result.error("手机号未注册！");
            }
        }
        CommonInfo three = commonInfoService.findOne(79);
        //默认使用腾讯云
        if (three == null || "1".equals(three.getValue())) {
            //腾讯云短信发送
            return sendMsgTencent(phone, state, code);
        } else if("2".equals(three.getValue())){
            //阿里云短信发送
            return sendMsgAlibaba(phone, state,code);
        } else if ("3".equals(three.getValue())) {
            return sendMsgDXB(phone, state, code);
        } else {
            return sendMsgHengXun(phone, state, code);
        }
    }

    private Result sendMsgHengXun(String phone, String state, int code) {

//        ------HTTP短信接口------
//        网关地址：http://120.79.52.238:8001/sms
//        帐号：210072
//        密码：ro7BfeoQQrGv
//        绑定IP：

        String uri = "http://120.79.52.238:8001/sms/api/sendMessage";
        long timeMillis = System.currentTimeMillis();

        // 短信签名
        CommonInfo name = commonInfoService.findOne(81);
        String content;
        switch (state) {
            case "register":
                content = "【" + name.getValue() + "】验证码: " + code + "，此验证码可用于登录或注册，10分钟内有效，如非您本人操作，可忽略本条消息。";
                break;
            case "forget":
                content = "【" + name.getValue() + "】验证码: " + code + "，您正在执行找回密码操作，10分钟内有效，如非您本人操作，可忽略本条消息。";
                break;
            case "bind":
                content = "【" + name.getValue() + "】验证码: " + code + "，您正在执行绑定手机号操作，10分钟内有效，如非您本人操作，可忽略本条消息。";
                break;
            default:
                content = "【" + name.getValue() + "】验证码: " + code + "，此验证码可用于登录或注册，10分钟内有效，如非您本人操作，可忽略本条消息。";
                break;
        }

        String userName = "210072";
        String password = "ro7BfeoQQrGv";

        String sign =  SecureUtil.md5(userName + content + timeMillis + SecureUtil.md5(password));

        SmsHengXunSendReq smsHengXunSendReq = new SmsHengXunSendReq();
        smsHengXunSendReq.setUserName(userName);
        smsHengXunSendReq.setContent(content);
        smsHengXunSendReq.setPhoneList(Collections.singletonList(phone));
        smsHengXunSendReq.setTimestamp(timeMillis+"");
        smsHengXunSendReq.setSign(sign);

        HttpRequest post = HttpUtil.createPost(uri);
        post.contentType(ContentType.JSON.getValue());
        post.body(JSON.toJSONString(smsHengXunSendReq));
        try (HttpResponse execute = post.execute()) {
            String body = execute.body();
            if (StrUtil.isBlank(body)) {
                log.info(post.toString());
                log.info(execute.toString());
            }

            JSONObject jsonObject = JSON.parseObject(body);
            Integer resCode = jsonObject.getInteger("code");
            if (0 == resCode) {
                Msg byPhone = msgDao.findByPhone(phone);
                if (byPhone != null) {
                    byPhone.setCode(String.valueOf(code));
                    byPhone.setPhone(phone);
                    msgDao.updateById(byPhone);
                } else {
                    Msg msg = new Msg();
                    msg.setCode(String.valueOf(code));
                    msg.setPhone(phone);
                    msgDao.insert(msg);
                }
                log.info("sendMsgHengXun success!");
            } else {
                log.info(post.toString());
                log.info(execute.toString());
            }
        } catch (Exception e) {
            log.info(post.toString());
            log.error("sendMsgCustom error!", e);
        }
        return Result.success();
    }

    private Result sendMsgAlibaba(String phone,String state, int code) {

        CommonInfo three = commonInfoService.findOne(85);
        String accessKeyId = three.getValue();
        CommonInfo four = commonInfoService.findOne(86);
        String accessSecret = four.getValue();
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonInfo name = commonInfoService.findOne(81);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", name.getValue());
        String value;
        switch (state) {
            case "login":
                value=commonInfoService.findOne(82).getValue();
                break;
            case "forget":
                value=commonInfoService.findOne(83).getValue();
                break;
            case "bindWx":
                value=commonInfoService.findOne(84).getValue();
                break;
            case "bindIos":
                value=commonInfoService.findOne(84).getValue();
                break;
            default:
                value=commonInfoService.findOne(82).getValue();
                break;
        }
        request.putQueryParameter("TemplateCode", value);
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            String data = response.getData();
            JSONObject jsonObject = JSON.parseObject(data);
            if ("OK".equals(jsonObject.get("Code"))) {
                Msg byPhone = msgDao.findByPhone(phone);
                if (byPhone != null) {
                    byPhone.setCode(String.valueOf(code));
                    byPhone.setPhone(phone);
                    msgDao.updateById(byPhone);
                } else {
                    Msg msg = new Msg();
                    msg.setCode(String.valueOf(code));
                    msg.setPhone(phone);
                    msgDao.insert(msg);
                }
                UserEntity userByPhone = queryByPhone(phone);
                if (userByPhone != null) {
                    return Result.success("login");
                } else {
                    return Result.success("register");
                }
            } else {
                if (jsonObject.get("Message").toString().contains("分钟")) {
                    return Result.error("短信发送过于频繁，请一分钟后再试！");
                } else if (jsonObject.get("Message").toString().contains("小时")) {
                    return Result.error("短信发送过于频繁，请一小时后再试！");
                } else if (jsonObject.get("Message").toString().contains("天")) {
                    return Result.error("短信发送过于频繁，请明天再试！");
                }
                log.info(jsonObject.get("Message").toString());
                return Result.error("短信发送失败！");
            }
        } catch (ClientException | com.aliyuncs.exceptions.ClientException e) {
            e.printStackTrace();
        }
        return Result.error("验证码发送失败");
    }


    private Result sendMsgTencent(String phone, String state, int code) {
        SmsSingleSenderResult result = null;

        CommonInfo three = commonInfoService.findOne(31);
        String clientId = three.getValue();
        CommonInfo four = commonInfoService.findOne(32);
        String clientSecret = four.getValue();
        try {
            String secretId = commonInfoService.findOne(240).getValue();
            String secretKey = commonInfoService.findOne(241).getValue();
            // 实例化一个认证对象，入参需要传入腾讯云账户 SecretId 和 SecretKey，此处还需注意密钥对的保密
            // 代码泄露可能会导致 SecretId 和 SecretKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考，建议采用更安全的方式来使用密钥，请参见：https://cloud.tencent.com/document/product/1278/85305
            // 密钥可前往官网控制台 https://console.cloud.tencent.com/cam/capi 进行获取
            Credential cred = new Credential(secretId, secretKey);
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("sms.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            SmsClient client = new SmsClient(cred, "ap-beijing", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            SendSmsRequest req = new SendSmsRequest();
            String[] phoneNumberSet1 = {phone};
            req.setPhoneNumberSet(phoneNumberSet1);

            req.setSmsSdkAppId(clientId);
            CommonInfo one = commonInfoService.findOne(81);
            req.setSignName(one.getValue());
            switch (state) {
                case "register":
                    req.setTemplateId(commonInfoService.findOne(242).getValue());
                    break;
                case "forget":
                    req.setTemplateId(commonInfoService.findOne(243).getValue());
                    break;
                case "bind":
                    req.setTemplateId(commonInfoService.findOne(244).getValue());
                    break;
                default:
                    req.setTemplateId(commonInfoService.findOne(242).getValue());
                    break;
            }
            String[] templateParamSet1 = {String.valueOf(code)};
            req.setTemplateParamSet(templateParamSet1);

            // 返回的resp是一个SendSmsResponse的实例，与请求对象对应
            SendSmsResponse resp = client.SendSms(req);
            // 输出json格式的字符串回包
            JSONObject jsonObject = JSONObject.parseObject(SendSmsResponse.toJsonString(resp));
            JSONArray sendStatusSet = jsonObject.getJSONArray("SendStatusSet");
            JSONObject jsonObject1 = sendStatusSet.getJSONObject(0);
            if ("Ok".equals(jsonObject1.getString("Code"))) {
                Msg byPhone = msgDao.findByPhone(phone);
                if (byPhone != null) {
                    byPhone.setCode(String.valueOf(code));
                    byPhone.setPhone(phone);
                    msgDao.updateById(byPhone);
                } else {
                    Msg msg = new Msg();
                    msg.setCode(String.valueOf(code));
                    msg.setPhone(phone);
                    msgDao.insert(msg);
                }
                UserEntity userByPhone = queryByPhone(phone);
                if (userByPhone != null) {
                    return Result.success("login");
                } else {
                    return Result.success("register");
                }
            }
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
        return Result.error("验证码发送失败");
    }


    private Result sendMsgDXB(String phone, String state, int code) {
        CommonInfo three = commonInfoService.findOne(238);
        CommonInfo four = commonInfoService.findOne(239);
        CommonInfo name = commonInfoService.findOne(81);
        String testUsername = three.getValue(); //在短信宝注册的用户名
        String testPassword = four.getValue(); //在短信宝注册的密码
        String value="";
        switch (state) {
            case "register":
                value = "【" + name.getValue() + "】验证码: " + code + "，此验证码可用于登录或注册，10分钟内有效，如非您本人操作，可忽略本条消息";
                break;
            case "forget":
                value = "【" + name.getValue() + "】验证码: " + code + "，您正在执行找回密码操作，10分钟内有效，如非您本人操作，可忽略本条消息";
                break;
            case "bind":
                value = "【" + name.getValue() + "】验证码: " + code + "，您正在执行绑定手机号操作，10分钟内有效，如非您本人操作，可忽略本条消息";
                break;
            default:
                value = "【" + name.getValue() + "】验证码: " + code + "，此验证码可用于登录或注册，10分钟内有效，如非您本人操作，可忽略本条消息";
                break;
        }
        StringBuilder httpArg = new StringBuilder();
        httpArg.append("u=").append(testUsername).append("&");
        httpArg.append("p=").append(Md5Utils.md5s(testPassword)).append("&");
        httpArg.append("m=").append(phone).append("&");
        httpArg.append("c=").append(Md5Utils.encodeUrlString(value, "UTF-8"));
        String result = Md5Utils.request("https://api.smsbao.com/sms", httpArg.toString());
        log.error("短信包返回值："+result);
        if ("0".equals(result)) {
            Msg byPhone = msgDao.findByPhone(phone);
            if (byPhone != null) {
                byPhone.setCode(String.valueOf(code));
                byPhone.setPhone(phone);
                msgDao.updateById(byPhone);
            } else {
                Msg msg = new Msg();
                msg.setCode(String.valueOf(code));
                msg.setPhone(phone);
                msgDao.insert(msg);
            }
            UserEntity userByPhone = queryByPhone(phone);
            if (userByPhone != null) {
                return Result.success("login");
            } else {
                return Result.success("register");
            }
        } else {
//            return ResultUtil.error(6, result.errMsg);
            if("30".equals(result)){
                return Result.error( "错误密码");
            }else if("40".equals(result)){
                return Result.error( "账号不存在");
            }else if("41".equals(result)){
                return Result.error( "余额不足");
            }else if("43".equals(result)){
                return Result.error( "IP地址限制");
            }else if("50".equals(result)){
                return Result.error( "内容含有敏感词");
            }else if("51".equals(result)){
                return Result.error( "手机号码不正确");
            }
        }

        return Result.error("验证码发送失败");
    }


    @Override
    public Result getOpenId(String code, Long userId) {
        try {
            SnsToken value = updateUserOpenUnionId(code, userId);
            return Result.success().put("data", value.getOpenid());
        } catch (Exception exception) {
            log.error("GET_OPENID_FAIL");
            return Result.error("获取失败,出错了！");
        }
    }

    @Override
    public Result getOpenIdV2(String code, Long userId) {
        try {
            return Result.success().put("data", updateUserOpenUnionId(code, userId));
        } catch (Exception exception) {
            log.error("GET_OPENID_FAIL");
            return Result.error("获取失败,出错了！");
        }
    }

    private SnsToken updateUserOpenUnionId(String code, Long userId) {
        try {
            //微信appid
            CommonInfo one = commonInfoService.findOne(5);
            //微信秘钥
            CommonInfo two = commonInfoService.findOne(21);
            SnsToken snsToken = SnsAPI.oauth2AccessToken(one.getValue(), two.getValue(), code);
            String openid = snsToken.getOpenid();
            String unionid = snsToken.getUnionid();
            if (StringUtils.isNotEmpty(openid)) {
                UserEntity userEntity = new UserEntity();
                userEntity.setUserId(userId);
                userEntity.setWxId(openid);
                userEntity.setWxUnionId(unionid);
                baseMapper.updateById(userEntity);
                return snsToken;
            }
            throw new RuntimeException("获取失败");
        } catch (Exception e) {
            log.error("GET_OPENID_FAIL");
            throw new RuntimeException("获取失败,出错了！");
        }
    }

    @Override
    public UserEntity selectUserById(Long userId) {
        UserEntity userEntity = baseMapper.selectById(userId);
        if(userEntity!=null){
            UserVip userVip = userVipService.selectUserVipByUserId(userId);
            if(userVip!=null){
                userEntity.setMember(userVip.getIsVip());
                userEntity.setEndTime(userVip.getEndTime());
            }
        }
        return userEntity;
    }


    @Override
    public PageUtils selectUserPage(Integer page, Integer limit, String search, Integer sex, String platform,
                                    String sysPhone, Integer status,Integer member,String inviterCode,String userName) {
        Page<UserEntity> pages = new Page<>(page, limit);
        return new PageUtils(baseMapper.selectUserPage(pages, search, sex, platform, sysPhone, status,member,inviterCode,userName));
    }

    @Override
    public int queryInviterCount(String inviterCode) {
        return baseMapper.queryInviterCount(inviterCode);
    }

    @Override
    public int queryUserCount(int type,String date,String platform) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        if(date==null||date==""){
            date = simpleDateFormat.format(new Date());
        }
        return baseMapper.queryUserCount(type, date,platform);
    }

    @Override
    public Double queryPayMoney(int type) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        String date = simpleDateFormat.format(new Date());
        return baseMapper.queryPayMoney(type, date);
    }

    @Override
    public IPage<Map<String, Object>> queryCourseOrder(Page<Map<String, Object>> iPage,int type, String date) {
        return baseMapper.queryCourseOrder(iPage,type, date);
    }

    @Override
    public int userMessage( String date, int type) {
        return baseMapper.userMessage(date, type);
    }


    @Override
    public void pushToSingle(String title, String content, String clientId) {
        GtApiConfiguration apiConfiguration = new GtApiConfiguration();
        //填写应用配置
        apiConfiguration.setAppId(commonInfoService.findOne(61).getValue());
        apiConfiguration.setAppKey(commonInfoService.findOne(60).getValue());
        apiConfiguration.setMasterSecret(commonInfoService.findOne(62).getValue());
        // 接口调用前缀，请查看文档: 接口调用规范 -> 接口前缀, 可不填写appId
        apiConfiguration.setDomain("https://restapi.getui.com/v2/");
        // 实例化ApiHelper对象，用于创建接口对象
        ApiHelper apiHelper = ApiHelper.build(apiConfiguration);
        // 创建对象，建议复用。目前有PushApi、StatisticApi、UserApi
        PushApi pushApi = apiHelper.creatApi(PushApi.class);
        //根据cid进行单推
        PushDTO<Audience> pushDTO = new PushDTO<Audience>();
        // 设置推送参数
        pushDTO.setRequestId(System.currentTimeMillis() + "");
        PushMessage pushMessage = new PushMessage();
        GTNotification notification = new GTNotification();
        pushDTO.setPushMessage(pushMessage);
        // 配置通知栏图标
        notification.setLogo("icon.png"); //配置通知栏图标，需要在客户端开发时嵌入，默认为push.png
        // 配置通知栏网络图标
        notification.setLogoUrl(commonInfoService.findOne(19).getValue() + "/logo.png");
        notification.setTitle(title);
        notification.setBody(content);
        notification.setClickType("url");
        notification.setUrl(commonInfoService.findOne(19).getValue());
        pushMessage.setNotification(notification);
        // 设置接收人信息
        Audience audience = new Audience();
        audience.addCid(clientId);
        pushDTO.setAudience(audience);
        // 进行cid单推
        ApiResult<Map<String, Map<String, String>>> apiResult = pushApi.pushToSingleByCid(pushDTO);
        if (apiResult.isSuccess()) {
            // success
            log.error("消息推送成功：" + apiResult.getData());
        } else {
            // failed
            log.error("消息推送成功失败：code:" + apiResult.getCode() + ", msg: " + apiResult.getMsg());
        }
    }

    @Override
    public Result selectInviteUserList(Integer page,Integer limit,String userName,String phone){
        return Result.success().put("data",baseMapper.selectInviteUserList(new Page<>(page,limit),userName,phone));
    }

    @Override
    public Result loginByOpenId(String openId, String unionId) {
        if (StrUtil.isBlank(openId) && StrUtil.isBlank(unionId)) {
            return Result.error("登陆凭证为空，请联系管理员");
        }
        UserEntity userInfo = queryByWxId(openId);
        if (userInfo == null && StrUtil.isNotBlank(unionId)) {
            userInfo =  queryByWxUnionId(unionId);
        }
        if (userInfo == null) {
            userInfo = new UserEntity();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = simpleDateFormat.format(new Date());
            userInfo.setWxId(openId);
            userInfo.setWxUnionId(StrUtil.isNotBlank(unionId) ? unionId : null);
            userInfo.setUserName("公众号用户"+ RandomUtil.randomString(6));
            userInfo.setPlatform("公众号");
            userInfo.setCreateTime(time);
            userInfo.setStatus(1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userInfo.setUpdateTime(sdf.format(new Date()));
            userInfo.setRate(new BigDecimal(commonInfoService.findOne(80).getValue()));
            baseMapper.insert(userInfo);
            userInfo.setInvitationCode(InvitationCodeUtil.toSerialCode(userInfo.getUserId()));
            baseMapper.updateById(userInfo);
        }

        if (StrUtil.isNotBlank(userInfo.getWxUnionId()) && StrUtil.isNotBlank(unionId)) {
            userInfo.setWxUnionId(unionId);
            baseMapper.updateById(userInfo);
        }

        String token = jwtUtils.generateToken(userInfo.getUserId());
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("expire", jwtUtils.getExpire());
        map.put("user", userInfo);

        return Result.success(map);
    }

    @Override
    public Result selectUserOnLineCount(){
        return Result.success().put("data",baseMapper.selectUserOnLineCount());
    }

    @Override
    public Result uniappLoginByPhone(String phone) {
        //校验手机号是否存在
        UserEntity userInfo = queryByPhone(phone);
        if (userInfo != null) {
            if (userInfo.getStatus().equals(2)) {
                return Result.error("账号已被禁用，请联系客服处理！");
            }
            return getResult(userInfo);
        } else {
            userInfo = new UserEntity();
            UserEntity userEntity = queryByInvitationCode(commonInfoService.findOne(88).getValue());;
            userInfo.setInviterCode(commonInfoService.findOne(88).getValue());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = simpleDateFormat.format(new Date());
            userInfo.setPhone(phone);
            userInfo.setUserName(phone.replaceAll("(\\d{3})\\d*([0-9a-zA-Z]{4})", "$1****$2"));
            // app / 小程序 / ios
            userInfo.setPlatform("app");
            userInfo.setCreateTime(time);
            // 手机类型 1安卓 2ios
            userInfo.setSysPhone(1);
            userInfo.setPassword(DigestUtils.sha256Hex(phone));
            // 状态 1正常 2禁用
            userInfo.setStatus(1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userInfo.setUpdateTime(sdf.format(new Date()));
            userInfo.setRate(new BigDecimal(commonInfoService.findOne(80).getValue()));
            baseMapper.insert(userInfo);
            userInfo.setInvitationCode(InvitationCodeUtil.toSerialCode(userInfo.getUserId()));
            baseMapper.updateById(userInfo);
            if(userEntity!=null){
                inviteService.saveBody(userInfo.getUserId(),userEntity);
            }
            return getResult(userInfo);
        }
    }

    @Override
    public List<BaseDropList> queryUserDropList() {
        return userDao.selectUserDropList();
    }

    @Override
    public List<UserEntity> queryUserList(List<Long> ids) {
        return userDao.selectUserList(ids);
    }
}
