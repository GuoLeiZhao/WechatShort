package com.sqx.modules.enterpriseWechat.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.sqx.common.exception.SqxException;
import com.sqx.common.utils.RedisUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.enterpriseWechat.entity.UploadMediaType;
import com.sqx.modules.enterpriseWechat.request.AddMsgTemplateRequest;
import com.sqx.modules.enterpriseWechat.request.GetCorpTagListRequest;
import com.sqx.modules.enterpriseWechat.request.GetGroupmsgListRequest;
import com.sqx.modules.enterpriseWechat.response.GetCorpTagListResponse;
import com.sqx.modules.enterpriseWechat.response.GetGroupmsgListResponse;
import com.sqx.modules.enterpriseWechat.response.UploadTempMediaResponse;
import com.sqx.modules.enterpriseWechat.service.EnterpriseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnterpriseServiceImpl implements EnterpriseService {

    private final RedisUtils redisUtils;

    private static final String BASE_URL = "https://qyapi.weixin.qq.com/cgi-bin";
    private static final String WX_AUTH_TOKEN_KEY = "WX_AUTH_TOKEN_KEY";

    private static final String AUTH_TOKEN_URL = "/gettoken?corpid={}&corpsecret={}";
    private static final String ADD_MSG_TEMPLATE_URL = "/externalcontact/add_msg_template?access_token={}";
    private static final String GET_CORP_TAG_LIST_URL = "/externalcontact/get_corp_tag_list?access_token={}";
    private static final String GET_GROUPMSG_LIST_V2_URL = "/externalcontact/get_groupmsg_list_v2?access_token={}";

    private static final String UPLOAD_TEMP_MEDIA_URL = "/media/upload?type={}&access_token={}";
    private static final String GET_TEMP_MEDIA_URL = "/media/get?media_id={}&access_token={}";

    // 小程序4
    private static final String CORP_ID = "ww34032a12a9fbd456";
    private static final String SECRET = "wmBMsOKkjJShDGuA4axHl0xx7AQy2WdJTBEHSvc365k";


    @Override
    public String getAccessToken() {
        String token = redisUtils.get(WX_AUTH_TOKEN_KEY);
        if (StrUtil.isNotBlank(token)) {
            return token;
        }

        synchronized (CORP_ID) {
            token = redisUtils.get(WX_AUTH_TOKEN_KEY);
            if (StrUtil.isNotBlank(token)) {
                return token;
            }
            return accessTokenExecute();
        }
    }

    @Override
    public Result getCorpTagList(GetCorpTagListRequest getCorpTagListRequest){
        return Result.success().setData(executeEnterpriseWechatApi(Method.POST, GET_CORP_TAG_LIST_URL, getCorpTagListRequest, "企业获取标签列表", httpResponse -> {
            ParserConfig parserConfig = new ParserConfig();
            parserConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
            return JSONObject.parseObject(httpResponse.body(), GetCorpTagListResponse.class, parserConfig);
        }));
    }

    @Override
    public Result addMsgTemplate(AddMsgTemplateRequest addMsgTemplateRequest) {
        return Result.success().setData(executeEnterpriseWechatApi(Method.POST, ADD_MSG_TEMPLATE_URL, addMsgTemplateRequest, "企业群发消息", httpResponse -> {
            ParserConfig parserConfig = new ParserConfig();
            parserConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
            return JSONObject.parseObject(httpResponse.body(), GetCorpTagListResponse.class, parserConfig);
        }));
    }

    @Override
    public Result getGroupMsgListV2(GetGroupmsgListRequest getGroupmsgListRequest) {
        return Result.success().setData(executeEnterpriseWechatApi(Method.POST, GET_GROUPMSG_LIST_V2_URL, getGroupmsgListRequest, "获取群发记录列表", httpResponse -> {
            ParserConfig parserConfig = new ParserConfig();
            parserConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
            return JSONObject.parseObject(httpResponse.body(), GetGroupmsgListResponse.class, parserConfig);
        }));
    }

    @Override
    public Result tempMediaUpload(MultipartFile multipartFile, UploadMediaType uploadMediaType) throws IOException {
        String url = StrUtil.format(UPLOAD_TEMP_MEDIA_URL, uploadMediaType.name(), getAccessToken());

        String originalFilename = multipartFile.getOriginalFilename();
        String[] filename = originalFilename.split("\\.");
        File file =File.createTempFile(filename[0] + ".", filename[1]);
        multipartFile.transferTo(file);

        return Result.success().setData(executeEnterpriseWechatApi(Method.POST, url, file, "上传临时素材", httpResponse -> {
            ParserConfig parserConfig = new ParserConfig();
            parserConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
            file.delete();
            file.deleteOnExit();
            return JSONObject.parseObject(httpResponse.body(), UploadTempMediaResponse.class, parserConfig);
        }));
    }

    @Override
    public void getTempMedia(OutputStream outputStream, String mediaId) {
        String url = StrUtil.format(GET_TEMP_MEDIA_URL, mediaId, accessTokenExecute());
        Result.success().setData(executeEnterpriseWechatApi(Method.POST, url, null, "上传临时素材", httpResponse -> {
            IoUtil.copy(httpResponse.bodyStream(),  outputStream);
            return null;
        }));
    }

    /**
     * 执行公共方法
     *
     * @param method
     * @param url
     * @param reqParam
     * @param apiName
     * @param mapper
     * @return
     */
    private <R> R executeEnterpriseWechatApi(Method method, String url, Object reqParam, String apiName, Function<HttpResponse, ? extends R> mapper){
        HttpRequest request;
        if (url.contains("gettoken")) {
            request = HttpUtil.createRequest(method, BASE_URL + url);
        } else {
            request = HttpUtil.createRequest(method, BASE_URL + StrUtil.format(url, getAccessToken()));
        }
        request.contentType(ContentType.JSON.toString());

        if (null != reqParam) {
            if (reqParam instanceof File) {
                // file
                request.contentType(ContentType.MULTIPART.toString());
                request.form("file", reqParam);
                request.contentLength(Long.valueOf(((File) reqParam).length()).intValue());
            } else if (reqParam instanceof String && StrUtil.isNotBlank(reqParam.toString())) {
                // 是 string 直接塞进去
                request.body(reqParam.toString());
            } else {
                // others
                SerializeConfig serializeConfig = new SerializeConfig();
                // 驼峰转下划线
                serializeConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
                request.body(JSON.toJSONString(reqParam, serializeConfig));
            }
        }

        try (HttpResponse httpResponse = request.execute()){
            if (200 == httpResponse.getStatus()) {
                if (url.contains("/media/get")) {
                    return mapper.apply(httpResponse);
                }
                // 200 成功
                JSONObject responseJson = JSONObject.parseObject(httpResponse.body());
                if (0 == responseJson.getInteger("errcode")) {
//                    // 0 成功
                        return mapper.apply(httpResponse);
                } else {
                    logReqAndRes(request, httpResponse);
                    throw new SqxException(apiName + " 接口错误，请查看日志并检查配置！");
                }
            }else {
                logReqAndRes(request, httpResponse);
                throw new SqxException(apiName + " 接口错误，请查看日志并检查配置！");
            }
        }
    }

    /**
     * 发起获取 access_token 的请求
     *
     * @return
     */
    private String accessTokenExecute() {
        String url = StrUtil.format(AUTH_TOKEN_URL, CORP_ID, SECRET);
        return executeEnterpriseWechatApi(Method.GET, url, null, "企业获取 access_token", httpResponse -> {
            JSONObject responseJson = JSONObject.parseObject(httpResponse.body());
            redisUtils.set(WX_AUTH_TOKEN_KEY, responseJson.getString("access_token"), responseJson.getLong("expires_in"));
            return responseJson.getString("access_token");
        });
    }

    private void logReqAndRes(HttpRequest request, HttpResponse response) {
        log.info("request: " + request);
        log.info("response: "+ response);
    }
}
