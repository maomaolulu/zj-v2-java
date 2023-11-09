package may.yuntian.minio.controller;

import io.minio.messages.Bucket;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import may.yuntian.minio.config.MinioConfig;
import may.yuntian.minio.utils.DateUtils;
import may.yuntian.minio.utils.MinioUtil;
import may.yuntian.untils.AlRedisUntil;
import may.yuntian.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@RestController
@Api(tags="Minio")
@RequestMapping(value = "/minio/file")
public class MinioController {

    @Autowired
    private MinioConfig prop;
    @Autowired
    private AlRedisUntil alRedisUntil;

    @ApiOperation(value = "查看存储bucket是否存在")
    @GetMapping("/bucketExists")
    public Result bucketExists(@RequestParam("bucketName") String bucketName) {
        return Result.ok().put("bucketName", MinioUtil.bucketExists(bucketName));
    }

    @ApiOperation(value = "创建存储bucket")
    @GetMapping("/makeBucket")
    public Result makeBucket(String bucketName) {
        return Result.ok().put("bucketName", MinioUtil.makeBucket(bucketName));
    }

    @ApiOperation(value = "删除存储bucket")
    @GetMapping("/removeBucket")
    public Result removeBucket(String bucketName) {
        return Result.ok().put("bucketName", MinioUtil.removeBucket(bucketName));
    }

    @ApiOperation(value = "获取全部bucket")
    @GetMapping("/getAllBuckets")
    public Result getAllBuckets() {
        List<Bucket> allBuckets = MinioUtil.getAllBuckets();
        return Result.ok().put("allBuckets", allBuckets);
    }

    @ApiOperation(value = "文件上传返回url")
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file,@RequestParam("type") String type) {
        String objectName = MinioUtil.upload(file,type);
        if (null != objectName) {
            alRedisUntil.hset("anlian-java",objectName, DateUtils.getDate());
            return Result.ok().put("path", objectName);
        }
        return Result.error("文件上传失败");
    }

    @ApiOperation(value = "图片/视频预览")
    @GetMapping("/preview")
    public Result preview(@RequestParam("fileName") String fileName) {
        String objName = fileName.substring(fileName.lastIndexOf(prop.getBucketName() + "/") + prop.getBucketName().length() + 1);
        return Result.ok().put("filleName", MinioUtil.preview(objName));
    }

    @ApiOperation(value = "文件下载")
    @GetMapping("/download")
    public Result download(@RequestParam("fileName") String fileName, HttpServletResponse res) {
        if (fileName.contains("uploadFile/")){
            return Result.error("图片加载失败");
        }
        String objName = fileName.substring(fileName.lastIndexOf(prop.getBucketName() + "/") + prop.getBucketName().length() + 1);
        MinioUtil.download(objName, res);
        return Result.ok();
    }

    @ApiOperation(value = "删除文件", notes = "根据url地址删除文件")
    @PostMapping("/delete")
    public Result remove(String url) {
        String objName = url.substring(url.lastIndexOf(prop.getBucketName() + "/") + prop.getBucketName().length() + 1);
        MinioUtil.remove(objName);
        return Result.ok().put("objName", objName);
    }

}