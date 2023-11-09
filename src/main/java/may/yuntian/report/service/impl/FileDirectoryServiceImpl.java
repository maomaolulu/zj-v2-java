package may.yuntian.report.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.entity.ProjectDateEntity;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.service.ProjectDateService;
import may.yuntian.anlian.service.ProjectService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;
import may.yuntian.jianping.entity.CompanySurveyEntity;
import may.yuntian.jianping.entity.ProjectUserEntity;
import may.yuntian.jianping.service.CompanySurveyService;
import may.yuntian.jianping.service.ProjectUserService;
import may.yuntian.laboratory.entity.DetectionEntity;
import may.yuntian.laboratory.service.DetectionService;
import may.yuntian.report.entity.FileDirectoryEntity;
import may.yuntian.report.mapper.FileDirectoryMapper;
import may.yuntian.report.service.FileDirectoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 文件目录表
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @email ''
 * @date 2022-04-14 20:45:49
 */
@Service("fileDirectoryService")
public class FileDirectoryServiceImpl extends ServiceImpl<FileDirectoryMapper, FileDirectoryEntity> implements FileDirectoryService {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectDateService projectDateService;
    @Autowired
    private ProjectUserService projectUserService;
    @Autowired
    private DetectionService detectionService;
    @Autowired
    private CompanySurveyService companySurveyService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<FileDirectoryEntity> page = this.page(
                new Query<FileDirectoryEntity>().getPage(params),
                new QueryWrapper<FileDirectoryEntity>()
        );

        return new PageUtils(page);
    }


    /**
     * 通过项目ID 获取文件目录列表
     * @param projectId
     * @return
     */
    public List<FileDirectoryEntity> getListByProjectId(Long projectId){
        if (notExistByProject(projectId)){
            this.shengchengMuLu(projectId);
        }
        List<FileDirectoryEntity> list = baseMapper.selectList(new QueryWrapper<FileDirectoryEntity>()
            .eq("project_id",projectId)
        );
        return list;
    }



    /**
     * 初始化文件目录表信息
     * @param projectId
     */
    public void shengchengMuLu(Long projectId){
        ProjectEntity projectEntity = projectService.getById(projectId);
        ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(projectId);
        List<ProjectUserEntity> diaoChaList = projectUserService.getListByType(110,projectId);//报告调查人
        String diaoCha = "";
        String caiYang = "";
        if (diaoChaList.size()>0){
            diaoCha =  diaoChaList.stream().map(ProjectUserEntity::getUsername).collect(Collectors.joining(","));
        }
        List<ProjectUserEntity> caiYangList = projectUserService.getListByType(120,projectId);//报告采样人
        if (caiYangList.size()>0){
            caiYang = caiYangList.stream().map(ProjectUserEntity::getUsername).collect(Collectors.joining(","));
        }
        DetectionEntity detectionEntity = detectionService.getByProjectId(projectId);
        CompanySurveyEntity companySurveyEntity = companySurveyService.getOne(projectId);
        List<FileDirectoryEntity> list  = new ArrayList<>();
        for (int i = 1;i<10;i++){
            FileDirectoryEntity tFileDirectoryEntity = new FileDirectoryEntity();
            switch (i){
                case 1:
                    tFileDirectoryEntity.setProjectId(projectId);
                    tFileDirectoryEntity.setSedgewick("委托单位");
                    tFileDirectoryEntity.setTitle("营业执照及资料");
                    tFileDirectoryEntity.setFileName(projectEntity.getCompany());
                    list.add(tFileDirectoryEntity);
                    continue;
                case 2:
                    tFileDirectoryEntity.setProjectId(projectId);
                    tFileDirectoryEntity.setSedgewick(diaoCha);
                    tFileDirectoryEntity.setTitle("工作场所现场调查记录");
                    tFileDirectoryEntity.setFileName(projectEntity.getCompany());
                    tFileDirectoryEntity.setDateStr(projectDateEntity.getSurveyDate());
                    tFileDirectoryEntity.setBackfillType(1);
                    list.add(tFileDirectoryEntity);
                    continue;
                case 3:
                    tFileDirectoryEntity.setProjectId(projectId);
                    tFileDirectoryEntity.setTitle("工作场所现场采样和检测计划");
                    tFileDirectoryEntity.setFileName(projectEntity.getCompany());
                    list.add(tFileDirectoryEntity);
                    continue;
                case 4:
                    tFileDirectoryEntity.setProjectId(projectId);
                    tFileDirectoryEntity.setSedgewick(caiYang);
                    tFileDirectoryEntity.setTitle("工作场所采样、检测原始记录");
                    tFileDirectoryEntity.setFileName(projectEntity.getCompany());
                    tFileDirectoryEntity.setDateStr(projectDateEntity.getEndDate());
                    tFileDirectoryEntity.setBackfillType(2);
                    list.add(tFileDirectoryEntity);
                    continue;
                case 5:
                    tFileDirectoryEntity.setProjectId(projectId);
                    tFileDirectoryEntity.setSedgewick(detectionEntity.getCompileName());
                    tFileDirectoryEntity.setDocumentNum(detectionEntity.getReportNumber());
                    tFileDirectoryEntity.setTitle("检测报告");
                    tFileDirectoryEntity.setFileName(projectEntity.getCompany());
                    tFileDirectoryEntity.setDateStr(detectionEntity.getReportDate());
                    tFileDirectoryEntity.setBackfillType(3);
                    list.add(tFileDirectoryEntity);
                    continue;
                case 6:
                    tFileDirectoryEntity.setProjectId(projectId);
                    tFileDirectoryEntity.setTitle("检测结果处理过程记录");
                    tFileDirectoryEntity.setFileName(projectEntity.getCompany());
                    list.add(tFileDirectoryEntity);
                    continue;
                case 7:
                    tFileDirectoryEntity.setProjectId(projectId);
                    tFileDirectoryEntity.setTitle("职业病危害因素检测报告技术审核记录");
                    tFileDirectoryEntity.setFileName(projectEntity.getCompany());
                    list.add(tFileDirectoryEntity);
                    continue;
                case 8:
                    tFileDirectoryEntity.setProjectId(projectId);
                    tFileDirectoryEntity.setTitle("职业病危害因素检测报告技术校核记录");
                    tFileDirectoryEntity.setFileName(projectEntity.getCompany());
                    list.add(tFileDirectoryEntity);
                    continue;
                case 9:
                    tFileDirectoryEntity.setProjectId(projectId);
                    tFileDirectoryEntity.setSedgewick(projectEntity.getCharge());
                    tFileDirectoryEntity.setDocumentNum(projectEntity.getIdentifier());
                    tFileDirectoryEntity.setTitle("职业病危害因素检测报告(正式稿)");
                    tFileDirectoryEntity.setFileName(projectEntity.getCompany());
                    tFileDirectoryEntity.setDateStr(companySurveyEntity.getReportCoverTime());
                    tFileDirectoryEntity.setBackfillType(4);
                    list.add(tFileDirectoryEntity);
                    continue;
                default:
                    break;
            }
        }
        this.saveBatch(list);
    }






    /**
     * 根据项目ID查询是否已经存在
     * @param projectId
     * @return boolean
     */
    public Boolean notExistByProject(Long projectId) {
        Integer count = baseMapper.selectCount(new QueryWrapper<FileDirectoryEntity>().eq("project_id", projectId));
        if(count>0)
            return false;
        else
            return true;
    }

}
