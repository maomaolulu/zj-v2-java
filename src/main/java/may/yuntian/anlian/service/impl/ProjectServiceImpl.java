package may.yuntian.anlian.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.constant.IntellectConstants;
import may.yuntian.anlian.entity.*;
import may.yuntian.anlian.mapper.ProjectMapper;
import may.yuntian.anlian.service.*;
import may.yuntian.anlian.utils.DateUtils;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.anlian.vo.ExportSchedulingVo;
import may.yuntian.anlian.vo.ProjectDateUserVo;
import may.yuntian.anlian.vo.ProjectTaskVo;
import may.yuntian.anlian.vo.ProjectTimeZoneVo;
import may.yuntian.commission.service.PerformanceAllocationService;
import may.yuntian.commission.vo.PerformanceNodeVo;
import may.yuntian.common.utils.SpringContextUtils;
import may.yuntian.jianping.dto.AbuSendNoteDTO;
import may.yuntian.jianping.entity.MessageEntity;
import may.yuntian.jianping.entity.ProjectUserEntity;
import may.yuntian.jianping.service.LabReportRecordService;
import may.yuntian.jianping.service.MessageService;
import may.yuntian.jianping.service.ProjectUserService;
import may.yuntian.modules.sys.entity.SysDictEntity;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.service.SysDictService;
import may.yuntian.modules.sys.service.SysRoleDeptService;
import may.yuntian.modules.sys.service.SysUserService;
import may.yuntian.sys.utils.PageUtil2;
import may.yuntian.sys.utils.ShiroUtils;
import may.yuntian.untils.ExportExcelUntil;
import org.apache.shiro.SecurityUtils;
import org.bson.codecs.IntegerCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 项目表(包含了原任务表的字段)
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @email ''
 * @date 2022-01-10 14:43:43
 */
@SuppressWarnings("all")
@Service("projectService")
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, ProjectEntity> implements ProjectService {


    @Autowired
    private ProjectUserService projectUserService;
    @Autowired
    private ProjectDateService projectDateService;
    @Autowired
    private ProjectProceduresService projectProceduresService;
    @Autowired
    private OrderSourceService orderSourceService;		//项目隶属来源信息
    @Autowired
    private CategoryService categoryService;//项目类型管理
    @Autowired
    private SysRoleDeptService sysRoleDeptService;//角色与部门对应关系
    @Autowired
    private CommissionService commissionService;
    @Autowired
    private SysDictService sysDictService;//数据字典
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private LabReportRecordService labReportRecordService;
    @Autowired
    private MessageService messageService;

    private static String TYPE_NAME = "commissionRatio";//参数类型



    /**
     * ipad端 获取本人负责或参与得项目列表
     * @param params
     * @return
     */
    public List<ProjectTaskVo> getMyTaskList(Map<String,Object> params){
        String queryParam = (String)params.get("queryParam");
        //用户名
        SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
        String username = sysUserEntity.getUsername();
        List<Long> planIdList = projectUserService.getPlanIdListByUsername(username);
        if (!(planIdList.size()>0)){
            planIdList.add(Long.valueOf("0"));
        }
        QueryWrapper<ProjectEntity> queryWrapper = new QueryWrapper<>();
//        queryWrapper.in("p.type","检评","职卫监督");
        if (StringUtils.isNotBlank(queryParam)){
            queryWrapper.and(i->i.like("p.company",queryParam)
                            .or().like("p.identifier",queryParam)
//                .or().in(planIdList.size()>0,"p.id",planIdList).or().eq("p.charge", username)
            );
        }
        queryWrapper.and(wr->wr.in(planIdList.size()>0,"p.id",planIdList).or().eq("p.charge", username));
        queryWrapper.between("status",2,70);
        queryWrapper.orderByDesc("p.urgent");
        queryWrapper.orderByDesc("pd.task_release_date");
        queryWrapper.orderByAsc("pd.claim_end_date");
        PageUtil2.startPage();
        List<ProjectTaskVo> projectTaskVoList = baseMapper.getListByQueryWapper(queryWrapper);
        projectTaskVoList.forEach(action->{
            Long projectId = action.getId();
            String name = projectUserService.getListByTypeAndProjectId(projectId);
            action.setSamplePerson(name);
        });
        return projectTaskVoList;
    }


    /**
     * web端 项目排单列表页
     * @param params
     * @return
     */
    @Override
    public List<ProjectDateUserVo> getListProjectDateUser(Map<String, Object> params) {
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        List<Long> roleDeptIds = sysRoleDeptService.queryDeptIdListByUserId(sysUserEntity.getUserId());
        String charge = (String) params.get("charge");
        String company = (String)params.get("company");
        String identifier = (String) params.get("identifier");
        String status = (String) params.get("status");
        String reportCoverDateStart = (String) params.get("reportCoverDateStart");
        String reportCoverDateEnd = (String) params.get("reportCoverDateEnd");

        String subjection = (String)params.get("subjection");

        QueryWrapper<ProjectEntity> queryWrapper = new QueryWrapper<ProjectEntity>();
        queryWrapper.eq(StringUtils.isNotBlank(charge), "p.charge",charge);
        queryWrapper.like(StringUtils.isNotBlank(company), "p.company",company);
        queryWrapper.like(StringUtils.isNotBlank(identifier), "p.identifier",identifier);
        queryWrapper.in("p.type","检评","职卫监督","其他示范");
        queryWrapper.eq(StringUtils.isNotBlank(status),"p.status",status);
        queryWrapper.ge(StringUtils.isNotBlank(reportCoverDateStart),"pd.report_cover_date",reportCoverDateStart);
        queryWrapper.le(StringUtils.isNotBlank(reportCoverDateEnd),"pd.report_cover_date",reportCoverDateEnd);
        queryWrapper.between(StringUtils.isBlank(status),"p.status",2,70);
        queryWrapper.orderByAsc("p.status");
        queryWrapper.orderByDesc("p.urgent","pd.task_release_date");

//        PageUtil2.startPage();

//        queryWrapper.eq(StringUtils.isNotBlank(subjection),"p.company_order",subjection);
        if (StringUtils.isNotBlank(subjection)) {
            // 支持分流
//            queryWrapper.and(wrapper -> wrapper.eq(com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(subjection), "company_order", subjection)
//                    .or().eq(com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(subjection), "business_source", subjection));
            queryWrapper.and(wrapper ->
                    wrapper.like( "company_order", subjection).or().like( "business_source", subjection)
                            .or().in(roleDeptIds.size() > 0,"dept_id", roleDeptIds)
            );
        }

        PageUtil2.startPage();

        List<ProjectDateUserVo> list = baseMapper.getListProjectDateUser(queryWrapper);

        for (ProjectDateUserVo p : list) {
            Long id = p.getId();
            List<ProjectUserEntity> projectUserEntityList = projectUserService.getListByProjectId(id);
            p.setProjectUserList(projectUserEntityList);
            String listByTypeAndProjectId = projectUserService.getListByTypeAndProjectId(id);
            p.setUsername(listByTypeAndProjectId);
            Date labReportDate = labReportRecordService.getLabReportDate(id);
            p.setLabReportDate(labReportDate);
            Long day;
            if (p.getReportIssue()==null||labReportDate==null){
                day = Long.valueOf("0");
                p.setReportDay(day);
            }else {
                day = DateUtils.getDays(p.getReportIssue(),labReportDate);
                p.setReportDay(day);
            }

        }
        return list;

    }


    /**
     * 排单列表导出信息处理
     * @param params
     * @return
     */
    public List<ExportSchedulingVo> exportScheduling(Map<String,Object> params){
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        List<Long> roleDeptIds = sysRoleDeptService.queryDeptIdListByUserId(sysUserEntity.getUserId());
        String charge = (String) params.get("charge");
        String company = (String)params.get("company");
        String identifier = (String) params.get("identifier");
        String status = (String) params.get("status");
        String reportCoverDateStart = (String) params.get("reportCoverDateStart");
        String reportCoverDateEnd = (String) params.get("reportCoverDateEnd");

        String subjection = (String)params.get("subjection");

        QueryWrapper<ProjectEntity> queryWrapper = new QueryWrapper<ProjectEntity>();
        queryWrapper.eq(StringUtils.isNotBlank(charge), "p.charge",charge);
        queryWrapper.like(StringUtils.isNotBlank(company), "p.company",company);
        queryWrapper.like(StringUtils.isNotBlank(identifier), "p.identifier",identifier);
        queryWrapper.in("p.type","检评","职卫监督","其他示范");
        queryWrapper.eq(StringUtils.isNotBlank(status),"p.status",status);
        queryWrapper.ge(StringUtils.isNotBlank(reportCoverDateStart),"pd.report_cover_date",reportCoverDateStart);
        queryWrapper.le(StringUtils.isNotBlank(reportCoverDateEnd),"pd.report_cover_date",reportCoverDateEnd);
        queryWrapper.between(StringUtils.isBlank(status),"p.status",2,70);
        queryWrapper.orderByAsc("p.status");
        queryWrapper.orderByDesc("p.urgent","pd.task_release_date");

        if (StringUtils.isNotBlank(subjection)) {
            // 支持分流
            queryWrapper.and(wrapper ->
                    wrapper.like( "company_order", subjection).or().like( "business_source", subjection)
                            .or().in(roleDeptIds.size() > 0,"dept_id", roleDeptIds)
            );
        }
        List<ExportSchedulingVo> list = baseMapper.exportScheduling(queryWrapper);

        if (StringUtils.isNotEmpty(list)){
            List<Long> projectIdList = list.stream().map(ExportSchedulingVo::getId).distinct().collect(Collectors.toList());
            List<ProjectUserEntity> projectUserEntities = projectUserService.getListByProjectIdList(projectIdList);

            if (StringUtils.isNotEmpty(projectUserEntities)){
                Map<Long,List<ProjectUserEntity>> projectUserMap = projectUserEntities.stream().collect(Collectors.groupingBy(ProjectUserEntity::getProjectId));
                for (ExportSchedulingVo exportSchedulingVo:list){
                    Long id = exportSchedulingVo.getId();
                    List<ProjectUserEntity> userList = projectUserMap.get(id);
                    if (StringUtils.isNotEmpty(userList)){
                        String username1 = userList.stream().filter(i->i.getTypes()==4).map(ProjectUserEntity::getUsername).collect(Collectors.joining("、"));//采样人员-组长
                        String username2 = userList.stream().filter(i->i.getTypes()==1).map(ProjectUserEntity::getUsername).collect(Collectors.joining("、"));//采样人员-组员
                        String surveyName = userList.stream().filter(i->i.getTypes()==110).map(ProjectUserEntity::getUsername).collect(Collectors.joining("、"));//报告调查人员
                        String sampleName = userList.stream().filter(i->i.getTypes()==120).map(ProjectUserEntity::getUsername).collect(Collectors.joining("、"));//报告采样人员
                        String labPerson = userList.stream().filter(i->i.getTypes()==130).map(ProjectUserEntity::getUsername).collect(Collectors.joining("、"));//实验室人员
                        exportSchedulingVo.setUsername(username1+"、"+username2);
                        exportSchedulingVo.setSurveyName(surveyName);
                        exportSchedulingVo.setSampleName(sampleName);
                        exportSchedulingVo.setLabPerson(labPerson);
                    }
                }
            }
        }
        return list;
    }


    /**
     * 导出排单信息
     * @param all
     * @param response
     * @throws IOException
     */
    public void download(List<ExportSchedulingVo> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ExportSchedulingVo schedulingVo : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("项目编号",schedulingVo.getIdentifier());
            map.put("受检单位",schedulingVo.getCompany());
            map.put("项目净值",schedulingVo.getNetvalue());
            map.put("真实调查人员",schedulingVo.getCharge());
            map.put("采样人员",schedulingVo.getUsername());
            map.put("调查日期",schedulingVo.getPlanSurveyDate());
            map.put("采样日期开始",schedulingVo.getPlanStartDate());
            map.put("采样日期结束",schedulingVo.getPlanEndDate());
            map.put("报告调查人员",schedulingVo.getSurveyName());
            map.put("报告采样人员",schedulingVo.getSampleName());
            map.put("报告调查日期",schedulingVo.getSurveyDate());
            map.put("报告采样日期开始",schedulingVo.getStartDate());
            map.put("报告采样日期结束",schedulingVo.getEndDate());
            map.put("签订日期",schedulingVo.getSignDate());
            map.put("实验室检测人员",schedulingVo.getLabPerson());
            map.put("报告封面日期",schedulingVo.getReportCoverDate());
            list.add(map);
        }
        ExportExcelUntil.downloadExcel(list, response);
    }


    /**
     * 项目排单接口
     * @param projectDateUserVo
     */
    public void saveOrUpdateProjectUser(ProjectDateUserVo projectDateUserVo){
        ProjectEntity projectEntity = baseMapper.selectById(projectDateUserVo.getId());
        List<ProjectUserEntity> projectUserList = projectDateUserVo.getProjectUserList();

        List<ProjectUserEntity> oldProjectUserList = projectUserService.getListByProjectId(projectEntity.getId());
        Integer[] types = new Integer[]{1,4,110,120};
        List<Long> projectUsetIdList = oldProjectUserList.stream().filter(i->Arrays.asList(types).contains(i.getTypes())).map(ProjectUserEntity::getId).distinct().collect(Collectors.toList());
        projectUserService.removeByIds(projectUsetIdList);

        if (projectUserList!=null&&projectUserList.size()>0){
            projectUserService.saveBatch(projectUserList);
        }
        ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(projectEntity.getId());
        projectDateEntity.setSurveyDate(projectDateUserVo.getSurveyDate());
        projectDateEntity.setStartDate(projectDateUserVo.getStartDate());
        projectDateEntity.setEndDate(projectDateUserVo.getEndDate());
        projectDateEntity.setPlanSurveyDate(projectDateUserVo.getPlanSurveyDate());
        projectDateEntity.setPlanStartDate(projectDateUserVo.getPlanStartDate());
        projectDateEntity.setPlanEndDate(projectDateUserVo.getPlanEndDate());
        projectDateService.updateById(projectDateEntity);

        projectEntity.setCharge(projectDateUserVo.getCharge());
        projectEntity.setChargeId(projectDateUserVo.getChargeId());
        projectEntity.setDeptId(projectDateUserVo.getDeptId());
        projectEntity.setRemarks(projectDateUserVo.getRemarks());
        if (projectEntity.getStatus()<=4){
            projectEntity.setStatus(4);
            ProjectProceduresEntity proceduresEntity = projectProceduresService.getProceduresEntity(projectEntity.getId(),4);
            if (proceduresEntity!=null){
                proceduresEntity.setStatus(4);
                proceduresEntity.setCreatetime(new Date());
                projectProceduresService.updateById(proceduresEntity);
            }else {
                ProjectProceduresEntity procedures = new ProjectProceduresEntity();
                procedures.setProjectId(projectEntity.getId());
                procedures.setStatus(4);
                projectProceduresService.save(procedures);
            }
        }

        this.updateById(projectEntity);

        //2023/08/04 项目排单给项目负责人、采样组长发送消息
        if ("检评".equals(projectEntity.getType()) || "职卫监督".equals(projectEntity.getType())){
            MessageEntity entity = new MessageEntity();
            List<Long> receivers = new ArrayList<>();
            List<ProjectUserEntity> collect = projectUserList.stream().filter(projectUserEntity -> projectUserEntity.getTypes() == 4).collect(Collectors.toList());
            if (collect.size() > 0){
                for (ProjectUserEntity projectUser : collect){
                    receivers.add(projectUser.getUserId());
                }
            }
            entity.setTitle("新项目提醒");
            entity.setContent("您有新的项目（【"+ projectEntity.getIdentifier() + " " + projectEntity.getCompany() +"】）已排单，请及时处理");
            entity.setBusinessType(0);
            entity.setSenderType(0);
            SysUserEntity nowUser = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
            entity.setSenderId(nowUser.getUserId());
            entity.setSenderName(nowUser.getUsername());
            receivers.add(projectEntity.getChargeId());
            messageService.newMessage(entity, receivers);
        }
    }



    /**
     * web端 实验室数据录入列表
     * @param params
     * @return
     */
    @Override
    public List<ProjectDateUserVo> getListLab(Map<String, Object> params) {
        String charge = (String) params.get("charge");
        String company = (String)params.get("company");
        String identifier = (String) params.get("identifier");
        String status = (String) params.get("status");
        String planDateStart = (String)params.get("planDateStart");
        String planDateEnd = (String)params.get("planDateEnd");

        String subjection = (String)params.get("subjection");

        QueryWrapper<ProjectEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(charge), "p.charge",charge);
        queryWrapper.eq(StringUtils.isNotBlank(status), "p.status",status);
        queryWrapper.ge(StringUtils.isNotBlank(planDateStart), "pd.plan_start_date",planDateStart);
        queryWrapper.le(StringUtils.isNotBlank(planDateEnd), "pd.plan_end_date",planDateEnd);
        queryWrapper.like(StringUtils.isNotBlank(company), "p.company",company);
        queryWrapper.like(StringUtils.isNotBlank(identifier), "p.identifier",identifier);
        queryWrapper.in("p.type","检评","职卫监督","其他示范");
        queryWrapper.between("p.status",2,70);
        queryWrapper.orderByAsc("p.status");
        queryWrapper.orderByDesc("p.urgent","pd.task_release_date");

        if (StringUtils.isNotBlank(subjection)) {
            // 支持分流
            queryWrapper.and(wrapper -> wrapper.eq(com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(subjection), "company_order", subjection)
                    .or().eq(com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(subjection), "business_source", subjection));
        }
//        queryWrapper.eq(StringUtils.isNotBlank(subjection),"p.company_order",subjection);

        PageUtil2.startPage();
        List<ProjectDateUserVo> list = baseMapper.getListProjectDateUser(queryWrapper);

        for (ProjectDateUserVo p : list) {
            Long id = p.getId();
            List<ProjectUserEntity> projectUserEntityList = projectUserService.getListByProjectId(id);
            p.setProjectUserList(projectUserEntityList);
            String listByTypeAndProjectId = projectUserService.getListByTypeAndProjectId(id);
            p.setUsername(listByTypeAndProjectId);
        }
        return list;

    }




    /**
     * web端我的任务列表
     * @param params
     * @return
     */
    @Override
    public List<ProjectDateUserVo> getMytaskList(Map<String, Object> params) {

        String charge = (String) params.get("charge");
        String company = (String)params.get("company");
        String identifier = (String) params.get("identifier");
        //用户名
        SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
        String username = sysUserEntity.getUsername();
        List<Long> planIdList = projectUserService.getPlanIdListByUsername(username);
        if (!(planIdList.size()>0)){
            planIdList.add(Long.valueOf("0"));
        }

        QueryWrapper<ProjectEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(charge), "charge",charge);
        queryWrapper.like(StringUtils.isNotBlank(company), "company",company);
        queryWrapper.like(StringUtils.isNotBlank(identifier), "identifier",identifier);
        queryWrapper.in("p.type","检评","职卫监督","其他示范");
        queryWrapper.and(wr->wr.in(planIdList.size()>0,"p.id",planIdList).or().eq("p.charge", username));
        queryWrapper.between("status",2,70);
        queryWrapper.orderByAsc("p.status");
        queryWrapper.orderByDesc("p.urgent","pd.task_release_date");


        PageUtil2.startPage();
        List<ProjectDateUserVo> list = baseMapper.getListProjectDateUser(queryWrapper);

        List<Long> idList = list.stream().map(ProjectDateUserVo::getId).distinct().collect(Collectors.toList());
        if (StringUtils.isNotEmpty(idList)){
            List<ProjectUserEntity> projectUserEntityList = projectUserService.getListByProjectIdList(idList);
            Map<Long,List<ProjectUserEntity>> listMap = projectUserEntityList.stream().collect(Collectors.groupingBy(ProjectUserEntity::getProjectId));


            for (ProjectDateUserVo p : list) {
                Long id = p.getId();
                List<ProjectUserEntity> userEntityList = listMap.get(id);
                if (userEntityList!=null&&userEntityList.size()>0){
                    List<String> usernameList = new ArrayList<>();//实际采样人员
                    List<String> diaochaList = new ArrayList<>();//报告调查人员
                    List<String> caiyangList = new ArrayList<>();//报告采样人员
                    for (ProjectUserEntity pUser:userEntityList){
                        if (pUser.getTypes()==1||pUser.getTypes()==4){
                            usernameList.add(pUser.getUsername());
                            continue;
                        }
                        if (pUser.getTypes()==110){
                            diaochaList.add(pUser.getUsername());
                            continue;
                        }
                        if (pUser.getTypes()==120){
                            caiyangList.add(pUser.getUsername());
                            continue;
                        }
                    }
                    if (usernameList.size()>0){
                        String userName = usernameList.stream().collect(Collectors.joining(","));
                        p.setUsername(userName);
                    }else {
                        p.setUsername("");
                    }
                    if (diaochaList.size()>0){
                        String diaocha = diaochaList.stream().collect(Collectors.joining(","));
                        p.setSurveyName(diaocha);
                    }else {
                        p.setSurveyName("");
                    }
                    if (caiyangList.size()>0){
                        String caiyang = caiyangList.stream().collect(Collectors.joining(","));
                        p.setSampleName(caiyang);
                    }else {
                        p.setSampleName("");
                    }
                }else {
                    p.setUsername("");
                    p.setSurveyName("");
                    p.setSampleName("");
                }

//            String listByTypeAndProjectId = projectUserService.getListByTypeAndProjectId(id);
//            p.setUsername(listByTypeAndProjectId);
//            String diaocha = projectUserService.getTypeName(110,id);
//            p.setSurveyName(diaocha);
            }
        }

        return list;

    }

    /**
     * web任务列表部门权限列表
     * @param params
     * @return
     */
    public List<ProjectDateUserVo> planDeptLit(Map<String, Object> params) {
        String charge = (String) params.get("charge");
        String company = (String)params.get("company");
        String identifier = (String) params.get("identifier");
        //用户名
        SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
//        String username = sysUserEntity.getUsername();
        List<Long> roleDeptIds = sysRoleDeptService.queryDeptIdListByUserId(sysUserEntity.getUserId());
//        List<Long> planIdList = projectUserService.getPlanIdListByUsername(username);
//        if (!(planIdList.size()>0)){
//            planIdList.add(Long.valueOf("0"));
//        }

        QueryWrapper<ProjectEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(charge), "charge",charge);
        queryWrapper.like(StringUtils.isNotBlank(company), "company",company);
        queryWrapper.like(StringUtils.isNotBlank(identifier), "identifier",identifier);
        queryWrapper.in("p.type","检评","职卫监督","其他示范");
        queryWrapper.in("dept_id", roleDeptIds);//部门权限控制,只根据数据权限显示数据，不根据归属部门
//        queryWrapper.and(wr->wr.in(planIdList.size()>0,"p.id",planIdList).or().eq("p.charge", username));
        queryWrapper.between("status",2,70);
        queryWrapper.orderByAsc("p.status");
        queryWrapper.orderByDesc("p.urgent","pd.task_release_date");

        PageUtil2.startPage();
        List<ProjectDateUserVo> list = baseMapper.getListProjectDateUser(queryWrapper);

        for (ProjectDateUserVo p : list) {
            Long id = p.getId();
            String listByTypeAndProjectId = projectUserService.getListByTypeAndProjectId(id);
            p.setUsername(listByTypeAndProjectId);
        }
        return list;

    }

    /**
     * web任务列表无权限控制
     * @param params
     * @return
     */
    public List<ProjectDateUserVo> planLit(Map<String, Object> params) {
        String charge = (String) params.get("charge");
        String company = (String)params.get("company");
        String identifier = (String) params.get("identifier");

        QueryWrapper<ProjectEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(charge), "charge",charge);
        queryWrapper.like(StringUtils.isNotBlank(company), "company",company);
        queryWrapper.like(StringUtils.isNotBlank(identifier), "identifier",identifier);
        queryWrapper.in("p.type","检评","职卫监督","其他示范");
        queryWrapper.between("status",2,70);
        queryWrapper.orderByAsc("p.status");
        queryWrapper.orderByDesc("p.urgent","pd.task_release_date");

        PageUtil2.startPage();
        List<ProjectDateUserVo> list = baseMapper.getListProjectDateUser(queryWrapper);

        List<Long> idList = list.stream().map(ProjectDateUserVo::getId).distinct().collect(Collectors.toList());
        if (StringUtils.isNotEmpty(idList)){
            List<ProjectUserEntity> projectUserEntityList = projectUserService.getListByProjectIdList(idList);
            Map<Long,List<ProjectUserEntity>> listMap = projectUserEntityList.stream().collect(Collectors.groupingBy(ProjectUserEntity::getProjectId));



            for (ProjectDateUserVo p : list) {
                Long id = p.getId();
                List<ProjectUserEntity> userEntityList = listMap.get(id);
                if (userEntityList!=null&&userEntityList.size()>0){
                    List<String> usernameList = new ArrayList<>();//实际采样人员
                    List<String> diaochaList = new ArrayList<>();//报告调查人员
                    List<String> caiyangList = new ArrayList<>();//报告采样人员
                    for (ProjectUserEntity pUser:userEntityList){

                        if (pUser.getTypes()==1||pUser.getTypes()==4){
                            usernameList.add(pUser.getUsername());
                            continue;
                        }
                        if (pUser.getTypes()==110){
                            diaochaList.add(pUser.getUsername());
                            continue;
                        }
                        if (pUser.getTypes()==120){
                            caiyangList.add(pUser.getUsername());
                            continue;
                        }
                    }
                    if (usernameList.size()>0){
                        String userName = usernameList.stream().collect(Collectors.joining(","));
                        p.setUsername(userName);
                    }else {
                        p.setUsername("");
                    }
                    if (diaochaList.size()>0){
                        String diaocha = diaochaList.stream().collect(Collectors.joining(","));
                        p.setSurveyName(diaocha);
                    }else {
                        p.setSurveyName("");
                    }
                    if (caiyangList.size()>0){
                        String caiyang = caiyangList.stream().collect(Collectors.joining(","));
                        p.setSampleName(caiyang);
                    }else {
                        p.setSampleName("");
                    }
                }else {
                    p.setUsername("");
                    p.setSurveyName("");
                    p.setSampleName("");
                }

//            String listByTypeAndProjectId = projectUserService.getListByTypeAndProjectId(id);
//            p.setUsername(listByTypeAndProjectId);
            }
        }

        return list;

    }


    /**
     * 节点填写列表页
     * @param params
     * @return
     */
    public List<ProjectTimeZoneVo> getTimeList(Map<String,Object> params){
        String charge = (String) params.get("charge");
        String company = (String)params.get("company");
        String identifier = (String) params.get("identifier");

        String subjection = (String)params.get("subjection");

        QueryWrapper<ProjectEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(charge), "p.charge",charge);
        queryWrapper.like(StringUtils.isNotBlank(company), "p.company",company);
        queryWrapper.like(StringUtils.isNotBlank(identifier), "p.identifier",identifier);
//        queryWrapper.eq("p.type","职卫监督");
        queryWrapper.between("status",2,70);

//        PageUtil2.startPage();


        queryWrapper.eq(StringUtils.isNotBlank(subjection),"p.company_order",subjection);

        PageUtil2.startPage();

        List<ProjectTimeZoneVo> list = baseMapper.getTimeZoneList(queryWrapper);
        for (ProjectTimeZoneVo pr : list){
            Long projectId = pr.getId();
            CommissionEntity commissionEntity = commissionService.getCommissionByProjectIdAndType(projectId,"采样提成");
            if (commissionEntity!=null){
                pr.setCommissionDate(commissionEntity.getCommissionDate());
            }
            String listByTypeAndProjectId = projectUserService.getListByTypeAndProjectId(projectId);
            pr.setSamplePerson(listByTypeAndProjectId);
        }

        return list;
    }

    /**
     * 时间节点获取接口
     * @param id
     */
    public ProjectEntity getTimeZoneById(Long id){
        ProjectEntity projectEntity = baseMapper.selectOne(new QueryWrapper<ProjectEntity>().eq("id",id));
        ProjectDateEntity projectDateEntity = projectDateService.getOneByProjetId(id);
        List<ProjectUserEntity> projectUserList = projectUserService.getListByProjectId(id);
        projectEntity.setProjectDateEntity(projectDateEntity);
        projectEntity.setProjectUserList(projectUserList);
        return projectEntity;
    }


    /**
     * 修改时间节点
     */
    public void updateTimeZone(HttpServletRequest httpRequest, ProjectEntity project){
        ProjectDateEntity projectDateEntity = project.getProjectDateEntity();
        if (StringUtils.checkValNotNull(projectDateEntity.getDeliverDate())){
            projectDateEntity.setReceivedDate(projectDateEntity.getDeliverDate());
        }
        if (StringUtils.checkValNotNull(projectDateEntity.getPhysicalSendDate())){
            projectDateEntity.setPhysicalAcceptDate(projectDateEntity.getPhysicalSendDate());
        }

        projectDateService.updateById(projectDateEntity);
//        //用户名
//        SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
//        String username = sysUserEntity.getUsername();
        if (projectDateEntity.getReceivedDate()!=null&&projectDateEntity.getPhysicalAcceptDate()!=null&&project.getStatus()<20){
            if (project != null && project.getStatus() < 20) {
                project.setStatus(20);
                this.updateById(project);
                if ("集团发展".equals(project.getBusinessSource())){
                    if (null!=project.getSalesmenid()){
                        AbuSendNoteDTO abuSendNoteDTO = new AbuSendNoteDTO();
                        abuSendNoteDTO.setProjectId(project.getId());
                        abuSendNoteDTO.setIdentifier(project.getIdentifier());
                        abuSendNoteDTO.setCompany(project.getCompany());
                        abuSendNoteDTO.setEntrustCompany(project.getEntrustCompany());
                        abuSendNoteDTO.setSalesmanId(project.getSalesmenid());
                        abuSendNoteDTO.setSalesman(project.getSalesmen());
                        abuSendNoteDTO.setStatus(project.getStatus());
                        Map<String,Object> msgMap = BeanUtil.beanToMap(abuSendNoteDTO);
                        JSONObject josmmap = JSONUtil.parseObj(msgMap);
                        try {
                            String object = HttpRequest.post(IntellectConstants.SEND_MESSAGE_URL)
                                    .header("Content-Type", "application/json")
                                    .header("token",httpRequest.getHeader("token"))
                                    .body(josmmap.toString())
                                    .execute().body();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                ProjectProceduresEntity proceduresEntity = new ProjectProceduresEntity();
                proceduresEntity.setProjectId(project.getId());
                proceduresEntity.setStatus(project.getStatus());
                projectProceduresService.save(proceduresEntity);
            }
        }

        //Todo 检评采样提成--开始
        PerformanceAllocationService performanceAllocationService = SpringContextUtils.getBean("performanceAllocationService", PerformanceAllocationService.class);
        PerformanceNodeVo performanceNodeVo = new PerformanceNodeVo();
        performanceNodeVo.setProjectId(project.getId());
        performanceNodeVo.setIsTime(project.getIsTime());
        performanceNodeVo.setGatherAcceptDate(projectDateEntity.getGatherAcceptDate());
        performanceNodeVo.setPhysicalAcceptDate(projectDateEntity.getPhysicalAcceptDate());
        performanceNodeVo.setReceivedDate(projectDateEntity.getReceivedDate());

        // TODO 检评采样提成--结束

        if(project.getIsTime()==null||project.getIsTime()==1) {
            if(projectDateEntity.getReceivedDate()!=null && projectDateEntity.getPhysicalAcceptDate()!=null && projectDateEntity.getGatherAcceptDate()!=null) {
                gatherCommission(project);
                performanceAllocationService.caiyangZjCommission(performanceNodeVo);
            }
        }else if (project.getIsTime()==2) {
            if(projectDateEntity.getReceivedDate()!=null && projectDateEntity.getGatherAcceptDate()!=null) {
                gatherCommission(project);
                performanceAllocationService.caiyangZjCommission(performanceNodeVo);
            }
        }else if (project.getIsTime()==3) {
            if(projectDateEntity.getPhysicalAcceptDate()!=null) {
                gatherCommission(project);
                performanceAllocationService.caiyangZjCommission(performanceNodeVo);
            }
        }

    }



    /**
     * 检评/评价采样提成计算
     * @param project
     */
    private void gatherCommission(ProjectEntity project) {
        CommissionService commissionService = SpringContextUtils.getBean("commissionService", CommissionService.class);
        Long projectId = project.getId();
        BigDecimal netvalue = project.getNetvalue();
        ProjectDateEntity projectDateEntity = project.getProjectDateEntity();
        SysDictEntity sysDict = sysDictService.queryByTypeAndCode(TYPE_NAME, "2");
        Double commissionRatioDouble = Double.valueOf(sysDict.getValue());
        BigDecimal commissionRatio = BigDecimal.valueOf(commissionRatioDouble);//提成比例
        BigDecimal cmsAmount = netvalue.multiply(commissionRatio);//项目净值*提成比例


        Long chargeId = project.getChargeId();
        SysUserEntity sysUserEntity = sysUserService.getById(chargeId);
        String subjection = sysUserEntity.getSubjection();

        List<ProjectUserEntity> planUserList = projectUserService.list(new QueryWrapper<ProjectUserEntity>()
                .eq("project_id", projectId)
                .and(i ->i.eq("types", 1)//组员
                        .or()
                        .eq("types", 4)));//组长

        String name ="";
        if(planUserList!=null&&planUserList.size()>0) {


            for (int i = 0; i < planUserList.size(); i++) {
                if(i==planUserList.size()-1){
                    name+=planUserList.get(i).getUsername();
                    break;
                }
                name+=planUserList.get(i).getUsername()+",";

            }
        }

        CommissionEntity commissionEntity = commissionService.getCommissionByProjectIdAndType(projectId, "采样提成");
        if(commissionEntity == null ) {
            CommissionEntity commission = new CommissionEntity();
            commission.setProjectId(projectId);
            commission.setCommissionDate(projectDateEntity.getGatherAcceptDate());
            commission.setPersonnel(name);
            commission.setType("采样提成");
            commission.setState(1);
            commission.setSubjection(subjection);
            commission.setCmsAmount(cmsAmount);
            commissionService.save(commission);
        }else if (!commissionEntity.getCmsAmount().equals(cmsAmount) || !commissionEntity.getPersonnel().equals(project.getCharge())) {
            commissionEntity.setCmsAmount(cmsAmount);
            commissionEntity.setPersonnel(name);
            commissionEntity.setSubjection(subjection);
            commissionEntity.setCommissionDate(projectDateEntity.getGatherAcceptDate());
            commissionService.updateById(commissionEntity);
        }

    }

    /**
     * 项目信息 单条
     * @param projectId
     * @return
     */
    public ProjectDateUserVo getOneByProjectId(Long projectId){
        ProjectDateUserVo projectDateUserVo = new ProjectDateUserVo();
        QueryWrapper<ProjectEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("p.id",projectId);
        List<ProjectDateUserVo> projectDateUserVoList = baseMapper.getListProjectDateUser(queryWrapper);
        if (projectDateUserVoList.size()>0){
            projectDateUserVo = projectDateUserVoList.get(0);
            List<ProjectUserEntity> projectUserEntityList = projectUserService.getListByProjectId(projectId);
            List<ProjectProceduresEntity> projectProceduresEntityList = projectProceduresService.listProceduresByProjectId(projectId);
            projectDateUserVo.setProjectUserList(projectUserEntityList);
            projectDateUserVo.setProjectProceduresEntityList(projectProceduresEntityList);
        }

        return projectDateUserVo;
    }



    /**
     * 用于项目信息分页的查询条件的处理
     * 限制部门数据权限及项目类型权限
     * @param
     * @return
     */
    private QueryWrapper<ProjectEntity> queryWrapperByParamsAuth(){
        //数据权限控制
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        Long deptId = sysUserEntity.getDeptId();//登录用户部门ID
        List<Long> roleDeptIds = sysRoleDeptService.queryDeptIdListByUserId(sysUserEntity.getUserId());
        List<Long> projectTypes = sysRoleDeptService.queryProjectTypeListByUserId(sysUserEntity.getUserId());
        List<Long> orderIds = sysRoleDeptService.queryOrderListByUserId(sysUserEntity.getUserId());
        List<Long> sourceIds = sysRoleDeptService.querySourceListByUserId(sysUserEntity.getUserId());

        //根据ID列表查询类型信息名称列表
        List<String> projectTypeNames = new ArrayList<>();
        if(projectTypes!=null && projectTypes.size()>0) {
            projectTypeNames = categoryService.getCategoryNameByIds(projectTypes);
        }else {
            projectTypeNames.add("无项目类型权限");//项目类型权限控制,无任何权限时故意赋值0查不到任何数据
            log.error("当前用户"+sysUserEntity.getUsername()+",部门ID："+deptId+",无任何项目类型权限!");
        }

        //数据权限控制
        QueryWrapper<ProjectEntity> queryWrapper = new QueryWrapper<ProjectEntity>();
        queryWrapper.and(wr->{
            wr.and(wr2->{
                //根据ID获取项目隶属名称列表
                List<String> companyOrderList = new ArrayList<>();
                if (orderIds!=null && orderIds.size()>0) {
                    companyOrderList = orderSourceService.getOrderSourceByIds(orderIds);
                }else {
                    companyOrderList.add("无此权限");//权限控制,无任何权限时故意赋值0查不到任何数据
                }

                //根据ID获取业务来源名称列表
                List<String> businessSourcesList  = new ArrayList<String>();
                if (sourceIds!=null && sourceIds.size()>0) {
                    businessSourcesList = orderSourceService.getOrderSourceByIds(sourceIds);
                }else {
                    businessSourcesList.add("无此权限");//权限控制,无任何权限时故意赋值0查不到任何数据
                }
                wr2.in("p.company_order",companyOrderList).or().in("p.business_source",businessSourcesList);
            }).or().in("dept_id", roleDeptIds);//部门权限控制,只根据数据权限显示数据，不根据归属部门);
        });
        queryWrapper.in((projectTypeNames!=null && projectTypeNames.size()>0), "type", projectTypeNames);//项目类型权限控制,>0判断逻辑上稍有漏洞
        return queryWrapper;
    }



}
