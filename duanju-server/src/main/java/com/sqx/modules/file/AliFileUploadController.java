package com.sqx.modules.file;



import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.sqx.common.utils.Result;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.file.utils.FileUploadUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;



/**
 * 阿里云文件上传
 */
@RestController
@Api(value = "阿里云文件上传", tags = {"阿里云文件上传"})
@RequestMapping(value = "/alioss")
@Slf4j
public class AliFileUploadController {


    /**
     * 图片上传大小上限。封面在手机上显示才 300x400 左右，2MB 的原图会让首页加载十几秒
     * （实测 2.3MB 封面在 1Mbps 带宽下要 18 秒），建议压到 600x800、200KB 以内。
     */
    private static final long MAX_IMAGE_SIZE = 2 * 1024 * 1024L;

    /**
     * 视为图片的扩展名。视频也走这个接口（后台批量导入剧集），不能一刀切限制大小，
     * 所以只对图片生效，其余类型放行。
     */
    private static final List<String> IMAGE_EXTENSIONS =
            Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "webp");

    private final CommonInfoService commonRepository;

    @Autowired
    public AliFileUploadController(CommonInfoService commonRepository) {
        this.commonRepository = commonRepository;
    }

    /**
     * 超限返回错误信息，未超限或不是图片返回 null
     */
    private String checkImageSize(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return null;
        }
        int dotIndex = originalFilename.lastIndexOf(".");
        if (dotIndex < 0) {
            return null;
        }
        String extension = originalFilename.substring(dotIndex + 1).toLowerCase();
        if (!IMAGE_EXTENSIONS.contains(extension)) {
            // 视频等其他类型不受此限制
            return null;
        }
        if (file.getSize() <= MAX_IMAGE_SIZE) {
            return null;
        }
        return String.format("图片不能超过 %dMB，当前 %.1fMB。请压缩后再传，封面建议 600x800、200KB 以内",
                MAX_IMAGE_SIZE / 1024 / 1024, file.getSize() / 1024.0 / 1024.0);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ApiOperation("文件上传")
    @ResponseBody
    public Result upload(@RequestParam("file") MultipartFile file){
        String sizeError = checkImageSize(file);
        if (sizeError != null) {
            return Result.error(-100, sizeError);
        }
        String value = commonRepository.findOne(234).getValue();
        if("1".equals(value)){
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(commonRepository.findOne(68).getValue(), commonRepository.findOne(69).getValue(), commonRepository.findOne(70).getValue());
            String suffix = file.getOriginalFilename().substring(Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf("."));
            // 上传文件流。
            InputStream inputStream = null;
            try {
                inputStream =new ByteArrayInputStream(file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String completePath=getPath(suffix);
            ossClient.putObject(commonRepository.findOne(71).getValue(), completePath, inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();
            //        String src = commonRepository.findOne(72).getValue()+"/"+completePath;
            String src = commonRepository.findOne(19).getValue()+"/img/"+completePath;
            return Result.success().put("data",src).put("fileName", file.getOriginalFilename().replaceAll(suffix, ""));
        }else if("2".equals(value)){
            String accessKey=commonRepository.findOne(800).getValue();
            String secretKey=commonRepository.findOne(801).getValue();
            String bucket=commonRepository.findOne(802).getValue();
            // bucket的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
            String path=commonRepository.findOne(804).getValue();
            String bucketName=commonRepository.findOne(803).getValue();
            String oldFileName = file.getOriginalFilename();
            String eName = oldFileName.substring(oldFileName.lastIndexOf("."));
            String newFileName = UUID.randomUUID()+eName;
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month=cal.get(Calendar.MONTH);
            int day=cal.get(Calendar.DATE);
            // 1 初始化用户身份信息(secretId, secretKey)
            COSCredentials cred = new BasicCOSCredentials(accessKey, secretKey);
            // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
            ClientConfig clientConfig = new ClientConfig(new Region(bucket));
            // 3 生成cos客户端
            COSClient cosclient = new COSClient(cred, clientConfig);


            // 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20 M 以下的文件使用该接口
            // 大文件上传请参照 API 文档高级 API 上传
            File localFile = null;
            try {
                localFile = File.createTempFile("temp",null);
                file.transferTo(localFile);
                // 指定要上传到 COS 上的路径
                String key = "/duanju/"+year+"/"+month+"/"+day+"/"+newFileName;
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
                PutObjectResult putObjectResult = cosclient.putObject(putObjectRequest);
                return Result.success().put("data",path + putObjectRequest.getKey());
            } catch (IOException e) {
                return Result.error(-100,"文件上传失败！");
            }finally {
                // 关闭客户端(关闭后台线程)
                cosclient.shutdown();
            }
        }else{
            try
            {
                String http = commonRepository.findOne(19).getValue();
                String[] split = http.split("://");
                // 上传文件路径
                String filePath ="/www/wwwroot/"+split[1]+"/file/uploadPath";
                // 上传并返回新文件名称
                String fileName = FileUploadUtils.upload(filePath, file);
                String url = http +fileName;
                return Result.success().put("data",url);
            }
            catch (Exception e)
            {
                log.error("本地上传失败："+e.getMessage(),e);
                return Result.error(-100,"文件上传失败！");
            }
        }

    }

    @RequestMapping(value = "/uploadUniApp", method = RequestMethod.POST)
    @ApiOperation("文件上传")
    @ResponseBody
    public String uploadUniApp(@RequestParam("file") MultipartFile file){
        String value = commonRepository.findOne(234).getValue();
        if("1".equals(value)){
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(commonRepository.findOne(68).getValue(), commonRepository.findOne(69).getValue(), commonRepository.findOne(70).getValue());
            String suffix = file.getOriginalFilename().substring(Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf("."));
            // 上传文件流。
            InputStream inputStream = null;
            try {
                inputStream =new ByteArrayInputStream(file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String completePath=getPath(suffix);
            ossClient.putObject(commonRepository.findOne(71).getValue(), completePath, inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();
            return commonRepository.findOne(19).getValue()+"/img/"+completePath;
        }else if("2".equals(value)){
            String accessKey=commonRepository.findOne(800).getValue();
            String secretKey=commonRepository.findOne(801).getValue();
            String bucket=commonRepository.findOne(802).getValue();
            // bucket的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
            String path=commonRepository.findOne(804).getValue();
            String bucketName=commonRepository.findOne(803).getValue();
            String oldFileName = file.getOriginalFilename();
            String eName = oldFileName.substring(oldFileName.lastIndexOf("."));
            String newFileName = UUID.randomUUID()+eName;
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month=cal.get(Calendar.MONTH);
            int day=cal.get(Calendar.DATE);
            // 1 初始化用户身份信息(secretId, secretKey)
            COSCredentials cred = new BasicCOSCredentials(accessKey, secretKey);
            // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
            ClientConfig clientConfig = new ClientConfig(new Region(bucket));
            // 3 生成cos客户端
            COSClient cosclient = new COSClient(cred, clientConfig);


            // 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20 M 以下的文件使用该接口
            // 大文件上传请参照 API 文档高级 API 上传
            File localFile = null;
            try {
                localFile = File.createTempFile("temp",null);
                file.transferTo(localFile);
                // 指定要上传到 COS 上的路径
                String key = "/duanju/"+year+"/"+month+"/"+day+"/"+newFileName;
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
                PutObjectResult putObjectResult = cosclient.putObject(putObjectRequest);
                return path + putObjectRequest.getKey();
            } catch (IOException e) {
                return null;
            }finally {
                // 关闭客户端(关闭后台线程)
                cosclient.shutdown();
            }
        }else{
            try
            {
                String http = commonRepository.findOne(19).getValue();
                String[] split = http.split("://");
                // 上传文件路径
                String filePath ="/www/wwwroot/"+split[1]+"/file/uploadPath";
                // 上传并返回新文件名称
                String fileName = FileUploadUtils.upload(filePath, file);
                String url = http +fileName;
                return url;
            }
            catch (Exception e)
            {
                log.error("本地上传失败："+e.getMessage(),e);
                return null;
            }
        }

    }



    private String getPath(String suffix) {
        //生成uuid
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //文件路径
        String path =format(new Date()) + "/" + uuid;
        return path + suffix;
    }


    private String format(Date date) {
        if(date != null){
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            return df.format(date);
        }
        return null;
    }


}