package may.yuntian.jianping.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.service.CategoryService;
import may.yuntian.anlian.service.OrderSourceService;
import may.yuntian.anlian.service.ProjectService;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.common.utils.SpringContextUtils;
import may.yuntian.jianping.entity.CompanySurveyEntity;
import may.yuntian.jianping.entity.ProjectUserEntity;
import may.yuntian.jianping.entity.SampleImgEntity;
import may.yuntian.jianping.mapper.CompanySurveyMapper;
import may.yuntian.jianping.service.CompanySurveyService;
import may.yuntian.jianping.service.ProjectUserService;
import may.yuntian.jianping.service.SampleImgService;
import may.yuntian.jianping.vo.CompanySurveyVo;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.service.SysRoleDeptService;
import may.yuntian.sys.utils.PageUtil2;
import may.yuntian.sys.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
/**
 * @author mi
 */
@Service("companyService")
public class CompanySurveyServiceImpl extends ServiceImpl<CompanySurveyMapper, CompanySurveyEntity> implements CompanySurveyService {

    @Autowired
    private SampleImgService sampleImgService;
    @Autowired
    private ProjectUserService projectUserService;
    @Autowired
    private SysRoleDeptService sysRoleDeptService;
    @Autowired
    private OrderSourceService orderSourceService;
    @Autowired
    private CategoryService categoryService;

    @Override
    public CompanySurveyEntity getOne(Long projectId) {
        CompanySurveyEntity companySurveyEntity = this.getOne(new QueryWrapper<CompanySurveyEntity>()
                .eq(StringUtils.isNotBlank(String.valueOf(projectId)), "project_id", projectId)
        );
        return companySurveyEntity;
    }

    /**
     * 检评需要公示列表 // 项目是检评的  加删除
     *
     * @param params
     * @return
     */
    @Override
    public List<CompanySurveyVo> pageListJp(Map<String, Object> params) {
        String projectId = (String) params.get("projectId");
        String company = (String) params.get("company");
        String identifier = (String) params.get("identifier");
        String charge = (String) params.get("charge");
        String applyPublicityStatus = (String) params.get("applyPublicityStatus");

        QueryWrapper<ProjectEntity> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.eq(StringUtils.isNotBlank(projectId), "cs.project_id", projectId);
        //需要公示的项目
        objectQueryWrapper.lt("ap.status", 98);
        //报告接收日期大于22年五月二十一号
        objectQueryWrapper.gt("apd.report_accept", "2022-05-21");
        // objectQueryWrapper.isNotNull("apd.report_issue");
        objectQueryWrapper.isNotNull("apd.report_cover_date");
        objectQueryWrapper.like(StringUtils.isNotBlank(company), "cs.company", company);
        objectQueryWrapper.like(StringUtils.isNotBlank(identifier), "ap.identifier", identifier);
        objectQueryWrapper.like(StrUtil.isNotBlank(charge), "ap.charge", charge);
        objectQueryWrapper.like(StringUtils.isNotBlank(applyPublicityStatus), "ap.apply_publicity_status", applyPublicityStatus);
        //查询 项目还在个人没申请到主管的项目与  既 公示状态为 0或3 的项目
        objectQueryWrapper.in("ap.apply_publicity_status", 0, 3);
        objectQueryWrapper.orderByAsc("ap.hide_status", "apd.report_cover_date");
        //只查询检评的
        objectQueryWrapper.eq("ap.type", "检评");
        //权限控制
        QueryWrapper<ProjectEntity> projectEntityQueryWrapper = queryWrapperByParamsAuth(objectQueryWrapper);
        PageUtil2.startPage();
        List<CompanySurveyVo> companySurveyVos = baseMapper.publicityList(projectEntityQueryWrapper);
        if (companySurveyVos != null && companySurveyVos.size() > 0) {
            for (CompanySurveyVo companySurveyVo : companySurveyVos
            ) {
                //检测图
                List<SampleImgEntity> sampleImgEntityList = sampleImgService.list(new QueryWrapper<SampleImgEntity>().eq("project_id", companySurveyVo.getProjectId()));
                if (sampleImgEntityList != null && sampleImgEntityList.size() > 0) {
                    companySurveyVo.setImgs(sampleImgEntityList);
                } else {
                    companySurveyVo.setImgs(new ArrayList<SampleImgEntity>());
                }
                //技术服务项目组人员
                Set<String> strings1 = new HashSet<>();
                String[] stringss = new String[]{"姜翠霞", "孙春花", "赵鑫"};
                strings1.addAll(Arrays.asList(stringss));
                //项目负责人
                String charge1 = companySurveyVo.getCharge();
                if (StrUtil.isNotBlank(charge1)) {
                    if (charge1.equals("张纯")) {
                        strings1.add("戴文雅");
                    } else if (charge1.equals("欧阳婷")) {
                        strings1.add("王玲玲");
                    } else {
                        strings1.add(charge1);
                    }
                }
                //现场调查人员
                List<ProjectUserEntity> list = projectUserService.list(new QueryWrapper<ProjectUserEntity>().eq("project_id", companySurveyVo.getProjectId()).eq("types", 110));
                if (list != null && list.size() > 0) {
                    List<String> strings = new ArrayList<>();
                    for (ProjectUserEntity projectUserEntity : list
                    ) {
                        strings.add(projectUserEntity.getUsername());
                        strings1.add(projectUserEntity.getUsername());
                    }
                    String join = String.join("、", strings);
                    companySurveyVo.setFieldInvestigators(join);
                } else {
                    companySurveyVo.setFieldInvestigators("");
                }

                //现场采样人员
                List<ProjectUserEntity> list2 = projectUserService.list(new QueryWrapper<ProjectUserEntity>().eq("project_id", companySurveyVo.getProjectId()).eq("types", 120));
                if (list2 != null && list2.size() > 0) {
                    List<String> strings = new ArrayList<>();
                    for (ProjectUserEntity projectUserEntity : list2
                    ) {
                        strings.add(projectUserEntity.getUsername());
                        strings1.add(projectUserEntity.getUsername());
                    }
                    String join = String.join("、", strings);
                    companySurveyVo.setFieldSampling(join);
                } else {
                    companySurveyVo.setFieldSampling("");
                }
                //技术服务项目组人员
                companySurveyVo.setTechnicalPersons(String.join("、", strings1));
                //项目采样陪同人
                if (companySurveyVo.getSamplingCompany() == null) {
                    companySurveyVo.setSamplingCompany(companySurveyVo.getAccompany());
                }
            }
        }
        return companySurveyVos;
    }





    /**
     * 用于项目信息分页的查询条件的处理
     * 限制部门数据权限及项目类型权限
     *
     * @param
     * @return
     */
    public QueryWrapper<ProjectEntity> queryWrapperByParamsAuth(QueryWrapper<ProjectEntity> wappr) {
        //数据权限控制
        SysUserEntity sysUserEntity = ShiroUtils.getUserEntity();
        //登录用户部门ID
        Long deptId = sysUserEntity.getDeptId();
        List<Long> roleDeptIds = sysRoleDeptService.queryDeptIdListByUserId(sysUserEntity.getUserId());
        List<Long> projectTypes = sysRoleDeptService.queryProjectTypeListByUserId(sysUserEntity.getUserId());
        List<Long> orderIds = sysRoleDeptService.queryOrderListByUserId(sysUserEntity.getUserId());
        List<Long> sourceIds = sysRoleDeptService.querySourceListByUserId(sysUserEntity.getUserId());

        //根据ID列表查询类型信息名称列表
        List<String> projectTypeNames = new ArrayList<>();
        if (projectTypes != null && projectTypes.size() > 0) {
            projectTypeNames = categoryService.getCategoryNameByIds(projectTypes);
        } else {
            //项目类型权限控制,无任何权限时故意赋值0查不到任何数据
            projectTypeNames.add("无项目类型权限");
            log.error("当前用户" + sysUserEntity.getUsername() + ",部门ID：" + deptId + ",无任何项目类型权限!");
        }

        //数据权限控制
        QueryWrapper<ProjectEntity> queryWrapper = wappr
                //部门权限控制,只根据数据权限显示数据，不根据归属部门
                .in("dept_id", roleDeptIds)
                //项目类型权限控制,>0判断逻辑上稍有漏洞
                .in((projectTypeNames != null && projectTypeNames.size() > 0), "type", projectTypeNames);

        queryWrapper.and(wr -> {
            //根据ID获取项目隶属名称列表
            List<String> companyOrderList = new ArrayList<>();
            if (orderIds != null && orderIds.size() > 0) {
                companyOrderList = orderSourceService.getOrderSourceByIds(orderIds);
            } else {
                //权限控制,无任何权限时故意赋值0查不到任何数据
                companyOrderList.add("无此权限");
            }
            //根据ID获取业务来源名称列表
            List<String> businessSourcesList = new ArrayList<String>();
            if (sourceIds != null && sourceIds.size() > 0) {
                businessSourcesList = orderSourceService.getOrderSourceByIds(sourceIds);
            } else {
                //权限控制,无任何权限时故意赋值0查不到任何数据
                businessSourcesList.add("无此权限");
            }
            wr.in("company_order", companyOrderList);
            wr.or();
            wr.in("business_source", businessSourcesList);
        });

        return queryWrapper;
    }
}
