package com.sqx.modules.enterpriseWechat.service;

import com.sqx.common.utils.Result;
import com.sqx.modules.enterpriseWechat.entity.UploadMediaType;
import com.sqx.modules.enterpriseWechat.request.AddMsgTemplateRequest;
import com.sqx.modules.enterpriseWechat.request.GetCorpTagListRequest;
import com.sqx.modules.enterpriseWechat.request.GetGroupmsgListRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public interface EnterpriseService {
    String getAccessToken();

    Result getCorpTagList(GetCorpTagListRequest getCorpTagListRequest);

    Result addMsgTemplate(AddMsgTemplateRequest addMsgTemplateRequest);

    Result getGroupMsgListV2(GetGroupmsgListRequest getGroupmsgListRequest);

    Result tempMediaUpload(MultipartFile file, UploadMediaType uploadMediaType) throws IOException;

    void getTempMedia(OutputStream outputStream, String mediaId);
}
