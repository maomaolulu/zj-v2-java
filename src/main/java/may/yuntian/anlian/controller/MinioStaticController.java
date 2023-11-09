package may.yuntian.anlian.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import may.yuntian.anlian.service.LinkTestService;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.jianping.entity.CraftProcessEntity;
import may.yuntian.jianping.entity.EquipmentMeasureEntity;
import may.yuntian.jianping.entity.SampleImgEntity;
import may.yuntian.jianping.entity.SignatureEntity;
import may.yuntian.jianping.service.CraftProcessService;
import may.yuntian.jianping.service.EquipmentMeasureService;
import may.yuntian.jianping.service.SampleImgService;
import may.yuntian.jianping.service.SignatureService;
import may.yuntian.minio.utils.MinioUtil;
import may.yuntian.report.entity.ReportProofreadEntity;
import may.yuntian.report.entity.ReportReviewEntity;
import may.yuntian.report.service.ReportProofreadService;
import may.yuntian.report.service.ReportReviewService;
import may.yuntian.sys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;


/**
 * 管理
 * WEB请求处理层
 *
 * @author LiXin
 * @email ''
 * @date 2023-05-08 09:09:26
 */
@Slf4j
@RestController
@Api(tags="Minio脚本控制层")
public class MinioStaticController {
    @Value("${anlian.path.staticPath}")
    private String anlian;

    @Autowired
    private EquipmentMeasureService equipmentMeasureService;
    @Autowired
    private CraftProcessService craftProcessService;
    @Autowired
    private SignatureService signatureService;
    @Autowired
    private SampleImgService sampleImgService;
    @Autowired
    private ReportReviewService reportReviewService;
    @Autowired
    private ReportProofreadService reportProofreadService;

    @PostMapping("/equipmentMeasure")
    public Result equipmentMeasure(){
        List<EquipmentMeasureEntity> equipmentMeasureEntities = equipmentMeasureService.list();
        for (EquipmentMeasureEntity entity:equipmentMeasureEntities){
            if (StringUtils.isNotBlank(entity.getPath())&&entity.getPath().contains("uploadFile/")){
                System.out.println("entity.getPath() = " + entity.getPath());
                MultipartFile file = MinioUtil.createMfileByPath(anlian+entity.getPath());
                String type = "project/"+entity.getProjectId()+"/equipmentMeasure";
                if (null != file){
                    String url = MinioUtil.upload(file,type);
                    if (null==url){
                        return Result.error("上传文件失败");
                    }
                    entity.setMinioPath(url);
                }else {
                    entity.setMinioPath("----");
                }
                equipmentMeasureService.updateById(entity);
            }
        }

        return Result.ok();
    }

    @PostMapping("/craftProcess")
    public Result craftProcess(){
        List<CraftProcessEntity> equipmentMeasureEntities = craftProcessService.list();
        for (CraftProcessEntity entity:equipmentMeasureEntities){
            if (StringUtils.isNotBlank(entity.getPath())&&entity.getPath().contains("uploadFile/")){
                System.out.println("entity.getPath() = " + entity.getPath());
                List<String> paths = new ArrayList<>(Arrays.asList(entity.getPath().split(",")));
                String b = "";
                for (String s:paths){
                    log.error("s = " + s);
                    System.out.println("s = " + s);
                    if (StringUtils.isNotBlank(s)){
                        MultipartFile file = MinioUtil.createMfileByPath(anlian+s);
                        String type = "project/"+entity.getProjectId()+"/craftProcess";
                        if (null != file){
                            String url = MinioUtil.upload(file,type);
                            if (null==url){
                                return Result.error("上传文件失败");
                            }
                            b = b+url+",";
                        }else {
                            b=b+"---"+",";
                        }
                    }
                }
                entity.setMinioPath(b);
                craftProcessService.updateById(entity);
            }
        }

        return Result.ok();
    }

    @PostMapping("/signature")
    public Result signature(){
        List<SignatureEntity> equipmentMeasureEntities = signatureService.list();
        for (SignatureEntity entity:equipmentMeasureEntities){
            if (StringUtils.isNotBlank(entity.getPath())&&entity.getPath().contains("uploadFile/")){
                System.out.println("entity.getPath() = " + entity.getPath());
                MultipartFile file = MinioUtil.createMfileByPath(anlian+entity.getPath());
                String type = "project/"+entity.getProjectId()+"/signature";
                if (null != file){
                    String url = MinioUtil.upload(file,type);
                    if (null==url){
                        return Result.error("上传文件失败");
                    }
                    entity.setMinioPath(url);
                }else {
                    entity.setMinioPath("----");
                }
                signatureService.updateById(entity);
            }
        }

        return Result.ok();
    }

    @PostMapping("/sampleImg")
    public Result sampleImg(){
        List<SampleImgEntity> equipmentMeasureEntities = sampleImgService.list();
        for (SampleImgEntity entity:equipmentMeasureEntities){
            if (StringUtils.isNotBlank(entity.getUrl())&&entity.getUrl().contains("uploadFile/")){
                System.out.println("entity.getUrl() = " + entity.getUrl());
                MultipartFile file = MinioUtil.createMfileByPath(anlian+entity.getUrl());
                String type = "project/"+entity.getProjectId()+"/sampleImg";
                if (null != file){
                    String url = MinioUtil.upload(file,type);
                    if (null==url){
                        return Result.error("上传文件失败");
                    }
                    entity.setMinioPath(url);
                }else {
                    entity.setMinioPath("----");
                }
                sampleImgService.updateById(entity);
            }
        }

        return Result.ok();
    }

    @PostMapping("/reportProofread")
    public Result reportProofread(){
        List<ReportProofreadEntity> equipmentMeasureEntities = reportProofreadService.list();
        for (ReportProofreadEntity entity:equipmentMeasureEntities){
            if (StringUtils.isNotBlank(entity.getPath())){
                MultipartFile file = MinioUtil.createMfileByPath(anlian+entity.getPath());
                String type = "project/"+entity.getProjectId()+"/reportProofread";
                if (null != file){
                    String url = MinioUtil.upload(file,type);
                    if (null==url){
                        return Result.error("上传文件失败");
                    }
                    entity.setMinioPath(url);
                }else {
                    entity.setMinioPath("----");
                }
                reportProofreadService.updateById(entity);
            }
        }

        return Result.ok();
    }

    /**
     * 报告审核
     * @return
     */
    @PostMapping("/reportReview")
    public Result reportReview(){
        List<ReportReviewEntity> equipmentMeasureEntities = reportReviewService.list();
        for (ReportReviewEntity entity:equipmentMeasureEntities){
            if (StringUtils.isNotBlank(entity.getPath())){
                MultipartFile file = MinioUtil.createMfileByPath(anlian+entity.getPath());
                String type = "project/"+entity.getProjectId()+"/reportReview";
                if (null != file){
                    String url = MinioUtil.upload(file,type);
                    if (null==url){
                        return Result.error("上传文件失败");
                    }
                    entity.setMinioPath(url);
                }else {
                    entity.setMinioPath("----");
                }
                reportReviewService.updateById(entity);
            }
        }

        return Result.ok();
    }
}
