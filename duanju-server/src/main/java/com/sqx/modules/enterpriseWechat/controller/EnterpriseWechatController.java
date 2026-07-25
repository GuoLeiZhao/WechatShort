package com.sqx.modules.enterpriseWechat.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.enterpriseWechat.entity.UploadMediaType;
import com.sqx.modules.enterpriseWechat.request.AddMsgTemplateRequest;
import com.sqx.modules.enterpriseWechat.request.GetCorpTagListRequest;
import com.sqx.modules.enterpriseWechat.request.GetGroupmsgListRequest;
import com.sqx.modules.enterpriseWechat.service.EnterpriseService;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Api(value = "企微相关", tags = {"企微相关"})
@RequestMapping(value = "/enterpriseWechat")
@RequiredArgsConstructor
public class EnterpriseWechatController extends AbstractController {

    private final EnterpriseService enterpriseService;

    @PostMapping("/getCorpTagList")
    @ApiOperation("获取企业标签")
    @ResponseBody
    public Result getCorpTagList(@RequestBody GetCorpTagListRequest getCorpTagListRequest){
        return enterpriseService.getCorpTagList(getCorpTagListRequest);
    }

    @PostMapping("/addMsgTemplate")
    @ApiOperation("创建企业群发")
    @ResponseBody
    public Result addMsgTemplate(@RequestBody AddMsgTemplateRequest addMsgTemplateRequest){
        return enterpriseService.addMsgTemplate(addMsgTemplateRequest);
    }

    @PostMapping("/getGroupMsgListV2")
    @ApiOperation("获取群发记录列表")
    @ResponseBody
    public Result getGroupMsgListV2(@RequestBody GetGroupmsgListRequest getGroupmsgListRequest){
        return enterpriseService.getGroupMsgListV2(getGroupmsgListRequest);
    }

    @PostMapping("/tempMediaUpload/{uploadMediaType}")
    @ApiOperation("上传临时素材")
    @ResponseBody
    public Result tempMediaUpload(MultipartFile file, @PathVariable UploadMediaType uploadMediaType) throws IOException {
        return enterpriseService.tempMediaUpload(file, uploadMediaType);
    }

    @GetMapping("/getTempMedia/{mediaId}")
    @ApiOperation("获取临时素材")
    public void getTempMedia(HttpServletResponse response, @PathVariable String mediaId) throws IOException {
        ServletOutputStream outputStream = response.getOutputStream();
        enterpriseService.getTempMedia(outputStream, mediaId);
        outputStream.flush();
    }

}
