package may.yuntian.commission.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.entity.ProjectDateEntity;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.service.ProjectDateService;
import may.yuntian.anlian.service.ProjectService;
import may.yuntian.anlian.utils.DateUtils;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.commission.entity.PerCommissionEntity;
import may.yuntian.commission.entity.PerformanceAllocationEntity;
import may.yuntian.commission.entity.PerformanceEntity;
import may.yuntian.commission.mapper.PerformanceAllocationMapper;
import may.yuntian.commission.service.PerCommissionService;
import may.yuntian.commission.service.PerformanceAllocationService;
import may.yuntian.commission.service.PerformanceService;
import may.yuntian.commission.utils.ComissionMathUntils;
import may.yuntian.commission.vo.PerformanceNodeVo;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.jianping.entity.ProjectUserEntity;
import may.yuntian.jianping.service.ProjectUserService;
import may.yuntian.modules.sys.entity.SysUserEntity;
import may.yuntian.modules.sys.service.SysRoleDeptService;
import may.yuntian.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 绩效分配表
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @email ''
 * @date 2022-06-17 09:54:17
 */
@SuppressWarnings("all")
@Service("performanceAllocationService")
public class PerformanceAllocationServiceImpl extends ServiceImpl<PerformanceAllocationMapper, PerformanceAllocationEntity> implements PerformanceAllocationService {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectDateService projectDateService;
    @Autowired
    private PerformanceService performanceService;
    @Autowired
    private PerCommissionService perCommissionService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleDeptService sysRoleDeptService;//角色与部门对应关系
    @Autowired
    private ProjectUserService projectUserService;






    /**
     * 根据项目ID和提成类型获取信息
     * @param projectId
     * @param types
     * @return
     */
    public PerformanceAllocationEntity getByProjectIdAndTypes(Long projectId, String types){
        PerformanceAllocationEntity performanceAllocationEntity = baseMapper.selectOne(new QueryWrapper<PerformanceAllocationEntity>()
            .eq("types",types)
            .eq("project_id",projectId)
        );
        return performanceAllocationEntity;
    }

    /**
     * 检评采样提成
     * @param performanceNodeVo
     */
    public void caiyangZjCommission(PerformanceNodeVo performanceNodeVo){
        Long projectId = performanceNodeVo.getProjectId();
        ProjectEntity projectEntity = projectService.getById(projectId);

        BigDecimal netvalue = projectEntity.getNetvalue();
        BigDecimal commissionMoney = BigDecimal.ZERO;//提成金额
        BigDecimal yearDeep = BigDecimal.ZERO;//年底提成
        BigDecimal zongticheng = BigDecimal.ZERO;
        List<PerformanceEntity> performanceEntityList = new ArrayList<>();
        List<PerCommissionEntity> perCommissionEntityList = new ArrayList<>();
        List<ProjectUserEntity> headmanList = projectUserService.getListByType(4,projectId);//获取项目采样组长
        List<ProjectUserEntity> zuyuanList = projectUserService.getListByType(1,projectId);//获取项目采样组员

        SysUserEntity sysUserEntity2 = new SysUserEntity();
        if (headmanList.size()>0){
            sysUserEntity2 = sysUserService.getById(headmanList.get(0).getUserId());
        }else if(zuyuanList.size()>0){
            sysUserEntity2 = sysUserService.getById(zuyuanList.get(0).getUserId());
        }
        if (sysUserEntity2!=null&&"杭州安联".equals(sysUserEntity2.getSubjection())){
            //比较日期大小 靠后的为提成日期
            Date commissionDate ;
            if(performanceNodeVo.getIsTime()!=null&&performanceNodeVo.getIsTime()==2){
                commissionDate = performanceNodeVo.getReceivedDate();
                if (commissionDate.before(performanceNodeVo.getGatherAcceptDate())){
                    commissionDate = performanceNodeVo.getGatherAcceptDate();
                }
            }else if (performanceNodeVo.getIsTime()!=null&&performanceNodeVo.getIsTime()==3){
                commissionDate = performanceNodeVo.getPhysicalAcceptDate();
            }else {
                commissionDate = performanceNodeVo.getReceivedDate();
                if (commissionDate.before(performanceNodeVo.getGatherAcceptDate())){
                    commissionDate = performanceNodeVo.getGatherAcceptDate();
                }
                if (commissionDate.before(performanceNodeVo.getPhysicalAcceptDate())){
                    commissionDate = performanceNodeVo.getPhysicalAcceptDate();
                }
            }
            //查询是否已生成一条绩效分配记录
            PerformanceAllocationEntity performanceAllocationEntity = this.getByProjectIdAndTypes(projectId,"采样提成");
//("职卫监督").equals(projectEntity.getType())
            if(1 == projectEntity.getIncludedOutput()){
                if (headmanList.size()>0){
                    PerformanceEntity performanceEntity = performanceService.getByUserid(headmanList.get(0).getUserId());//采样组长组长

                    SysUserEntity sysUserEntity = sysUserService.getById(headmanList.get(0).getUserId());

                    BigDecimal targetOutput = performanceEntity.getTargetOutput();//采样组长人员产出目标金额
                    BigDecimal cumulativeOutput = performanceEntity.getCumulativeOutput();//采样组长人员目前累计金额

                    if (zuyuanList.size()>0){
                        BigDecimal outPut = netvalue.multiply(new BigDecimal("0.60"));
                        BigDecimal headmanLeiji = cumulativeOutput.add(outPut);//采样组长人员产出累计金额

                        performanceEntity.setCumulativeOutput(headmanLeiji);
                        performanceEntityList.add(performanceEntity);

                        Map<String,BigDecimal> map = ComissionMathUntils.calculation(targetOutput,cumulativeOutput,outPut,netvalue,"检评采样");
                        commissionMoney = map.get("commissionMoney");
                        yearDeep = map.get("yearDeep");

                        zongticheng = zongticheng.add(commissionMoney);

                        perCommissionEntityList = newPerCommissionEntity(performanceAllocationEntity,projectEntity,sysUserEntity,commissionMoney,yearDeep,commissionDate,perCommissionEntityList,2);

                    }else {
                        BigDecimal headmanLeiji =cumulativeOutput.add(netvalue);//采样组长人员产出累计金额 .multiply(new BigDecimal("0.60"))
                        BigDecimal outPut = netvalue;
                        performanceEntity.setCumulativeOutput(headmanLeiji);
                        performanceEntityList.add(performanceEntity);

                        Map<String,BigDecimal> map = ComissionMathUntils.calculation(targetOutput,cumulativeOutput,outPut,netvalue,"检评采样");
                        commissionMoney = map.get("commissionMoney");
                        yearDeep = map.get("yearDeep");
                        zongticheng = zongticheng.add(commissionMoney);

                        perCommissionEntityList = newPerCommissionEntity(performanceAllocationEntity,projectEntity,sysUserEntity,commissionMoney,yearDeep,commissionDate,perCommissionEntityList,2);

                    }

                }
                if (zuyuanList.size()>0){
                    if (headmanList.size()>0){
                        for (int i=0;i<zuyuanList.size();i++){//处理采样组员产出金额
                            PerformanceEntity performanceEntity = performanceService.getByUserid(zuyuanList.get(i).getUserId());//采样组员
                            SysUserEntity sysUserEntity = sysUserService.getById(zuyuanList.get(i).getUserId());

                            BigDecimal targetOutput = performanceEntity.getTargetOutput();//采样组员人员产出目标金额
                            BigDecimal cumulativeOutput = performanceEntity.getCumulativeOutput();//采样组员人员目前产出额
                            BigDecimal outPut = netvalue.multiply(new BigDecimal("0.40")).divide(new BigDecimal(zuyuanList.size()),3,BigDecimal.ROUND_HALF_UP);
                            BigDecimal headmanLeiji = cumulativeOutput.add(outPut);//采样组员人员产出累计金额

                            performanceEntity.setCumulativeOutput(headmanLeiji);
                            performanceEntityList.add(performanceEntity);

                            Map<String,BigDecimal> map = ComissionMathUntils.calculation(targetOutput,cumulativeOutput,outPut,netvalue,"检评采样");
                            commissionMoney = map.get("commissionMoney");
                            yearDeep = map.get("yearDeep");
                            zongticheng = zongticheng.add(commissionMoney);

                            perCommissionEntityList = newPerCommissionEntity(performanceAllocationEntity,projectEntity,sysUserEntity,commissionMoney,yearDeep,commissionDate,perCommissionEntityList,3);

                        }
                    }else {
                        for (int i=0;i<zuyuanList.size();i++){//处理采样组员产出金额
                            PerformanceEntity performanceEntity = performanceService.getByUserid(zuyuanList.get(i).getUserId());//采样组员
                            SysUserEntity sysUserEntity = sysUserService.getById(zuyuanList.get(i).getUserId());

                            BigDecimal targetOutput = performanceEntity.getTargetOutput();//采样组员人员产出目标金额
                            BigDecimal cumulativeOutput = performanceEntity.getCumulativeOutput();//采样组员人员目前产出额
                            BigDecimal outPut = netvalue.divide(new BigDecimal(zuyuanList.size()));
                            BigDecimal headmanLeiji = cumulativeOutput.add(outPut);//采样组员人员产出累计金额

                            performanceEntity.setCumulativeOutput(headmanLeiji);
                            performanceEntityList.add(performanceEntity);

                            Map<String,BigDecimal> map = ComissionMathUntils.calculation(targetOutput,cumulativeOutput,outPut,netvalue,"检评采样");
                            commissionMoney = map.get("commissionMoney");
                            yearDeep = map.get("yearDeep");
                            zongticheng = zongticheng.add(commissionMoney);

                            perCommissionEntityList = newPerCommissionEntity(performanceAllocationEntity,projectEntity,sysUserEntity,commissionMoney,yearDeep,commissionDate,perCommissionEntityList,3);

                        }
                    }

                }
                if (performanceAllocationEntity==null){
                    performanceService.updateBatchById(performanceEntityList);
                    PerformanceAllocationEntity performanceAllocation = new PerformanceAllocationEntity();
                    performanceAllocation.setPerformanceMoney(zongticheng);
                    performanceAllocation.setProjectId(projectId);
                    performanceAllocation.setPerformanceDate(commissionDate);
                    performanceAllocation.setTypes("采样提成");
                    this.save(performanceAllocation);
                    for (PerCommissionEntity perCommissionEntity:perCommissionEntityList){
                        perCommissionEntity.setPerformanceAllocationId(performanceAllocation.getId());
                    }
                    perCommissionService.saveBatch(perCommissionEntityList);
                }

            }else {
                if (headmanList.size()>0){
                    PerformanceEntity performanceEntity = performanceService.getByUserid(headmanList.get(0).getUserId());//采样组长组长
                    SysUserEntity sysUserEntity = sysUserService.getById(headmanList.get(0).getUserId());
                    BigDecimal targetOutput = performanceEntity.getTargetOutput();//采样组长人员产出目标金额
                    BigDecimal cumulativeOutput = performanceEntity.getCumulativeOutput();//采样组长人员产出累计金额


                    if(zuyuanList.size()>0){
                        BigDecimal mathNetvalue = netvalue.multiply(new BigDecimal("0.60"));
                        Map<String,BigDecimal> map = ComissionMathUntils.calculation(targetOutput,cumulativeOutput,BigDecimal.ZERO,mathNetvalue,"检评采样");
                        commissionMoney = map.get("commissionMoney");
                        yearDeep = map.get("yearDeep");
                        zongticheng = zongticheng.add(commissionMoney);

                        perCommissionEntityList = newPerCommissionEntity(performanceAllocationEntity,projectEntity,sysUserEntity,commissionMoney,yearDeep,commissionDate,perCommissionEntityList,2);

                    }else {
                        Map<String,BigDecimal> map = ComissionMathUntils.calculation(targetOutput,cumulativeOutput,BigDecimal.ZERO,netvalue,"检评采样");
                        commissionMoney = map.get("commissionMoney");
                        yearDeep = map.get("yearDeep");
                        zongticheng = zongticheng.add(commissionMoney);

                        perCommissionEntityList = newPerCommissionEntity(performanceAllocationEntity,projectEntity,sysUserEntity,commissionMoney,yearDeep,commissionDate,perCommissionEntityList,2);

                    }

                }
                if (zuyuanList.size()>0){
                    if (headmanList.size()>0){
                        for (int i=0;i<zuyuanList.size();i++){//处理采样组员产出金额
                            PerformanceEntity performanceEntity = performanceService.getByUserid(zuyuanList.get(i).getUserId());//采样组员
                            SysUserEntity sysUserEntity = sysUserService.getById(zuyuanList.get(i).getUserId());
                            BigDecimal targetOutput = performanceEntity.getTargetOutput();//采样组员人员产出目标金额
                            BigDecimal cumulativeOutput = performanceEntity.getCumulativeOutput();//采样组员人员产出累计金额
                            BigDecimal mathNetvalue = netvalue.multiply(new BigDecimal("0.40")).divide(new BigDecimal(zuyuanList.size()),3,BigDecimal.ROUND_HALF_UP);

                            Map<String,BigDecimal> map = ComissionMathUntils.calculation(targetOutput,cumulativeOutput,BigDecimal.ZERO,mathNetvalue,"检评采样");
                            commissionMoney = map.get("commissionMoney");
                            yearDeep = map.get("yearDeep");
                            zongticheng = zongticheng.add(commissionMoney);

                            perCommissionEntityList = newPerCommissionEntity(performanceAllocationEntity,projectEntity,sysUserEntity,commissionMoney,yearDeep,commissionDate,perCommissionEntityList,3);

                        }
                    }else {
                        for (int i=0;i<zuyuanList.size();i++){//处理采样组员产出金额
                            PerformanceEntity performanceEntity = performanceService.getByUserid(zuyuanList.get(i).getUserId());//采样组员
                            SysUserEntity sysUserEntity = sysUserService.getById(zuyuanList.get(i).getUserId());
                            BigDecimal targetOutput = performanceEntity.getTargetOutput();//采样组员人员产出目标金额
                            BigDecimal cumulativeOutput = performanceEntity.getCumulativeOutput();//采样组员人员产出累计金额
                            BigDecimal mathNetvalue = netvalue.divide(new BigDecimal(zuyuanList.size()));

                            Map<String,BigDecimal> map = ComissionMathUntils.calculation(targetOutput,cumulativeOutput,BigDecimal.ZERO,mathNetvalue,"检评采样");
                            commissionMoney = map.get("commissionMoney");
                            yearDeep = map.get("yearDeep");
                            zongticheng = zongticheng.add(commissionMoney);

                            perCommissionEntityList = newPerCommissionEntity(performanceAllocationEntity,projectEntity,sysUserEntity,commissionMoney,yearDeep,commissionDate,perCommissionEntityList,3);

                        }
                    }

                }
                if (performanceAllocationEntity==null){
                    PerformanceAllocationEntity performanceAllocation = new PerformanceAllocationEntity();
                    performanceAllocation.setPerformanceMoney(zongticheng);
                    performanceAllocation.setProjectId(projectId);
                    performanceAllocation.setPerformanceDate(commissionDate);
                    performanceAllocation.setTypes("采样提成");
                    this.save(performanceAllocation);
                    for (PerCommissionEntity perCommissionEntity:perCommissionEntityList){
                        perCommissionEntity.setPerformanceAllocationId(performanceAllocation.getId());
                    }
                    perCommissionService.saveBatch(perCommissionEntityList);
                }
            }
        }


    }


    /**
     * 保存提成信息的处理
     * @param performanceAllocationEntity
     * @param projectEntity
     * @param sysUserEntity
     * @param commissionMoney
     * @param yearDeep
     * @param commissionDate
     * @param perCommissionEntityList
     * @param humanType
     * @return
     */
    private List<PerCommissionEntity> newPerCommissionEntity(PerformanceAllocationEntity performanceAllocationEntity,ProjectEntity projectEntity,SysUserEntity sysUserEntity,BigDecimal commissionMoney,BigDecimal yearDeep,Date commissionDate,List<PerCommissionEntity> perCommissionEntityList,Integer humanType ){
        PerCommissionEntity perCommissionEntity = new PerCommissionEntity();
        perCommissionEntity.setPerformanceAllocationId(performanceAllocationEntity==null?null:performanceAllocationEntity.getId());
        perCommissionEntity.setProjectId(projectEntity.getId());
        perCommissionEntity.setPType(projectEntity.getType());
        perCommissionEntity.setPersonnel(sysUserEntity.getUsername());
        perCommissionEntity.setSubjection(sysUserEntity.getSubjection());
        perCommissionEntity.setDeptId(sysUserEntity.getDeptId());
        perCommissionEntity.setDeptName("检评");
        perCommissionEntity.setCmsAmount(commissionMoney);
        perCommissionEntity.setHumenType(humanType);
        perCommissionEntity.setCommissionDate(commissionDate);
        perCommissionEntity.setType("采样提成");
        perCommissionEntityList.add(perCommissionEntity);
        PerCommissionEntity perCommissionEntity1 = new PerCommissionEntity();
        perCommissionEntity1.setPerformanceAllocationId(performanceAllocationEntity==null?null:performanceAllocationEntity.getId());
        perCommissionEntity1.setProjectId(projectEntity.getId());
        perCommissionEntity1.setPType(projectEntity.getType());
        perCommissionEntity1.setPersonnel(sysUserEntity.getUsername());
        perCommissionEntity1.setSubjection(sysUserEntity.getSubjection());
        perCommissionEntity1.setDeptId(sysUserEntity.getDeptId());
        perCommissionEntity1.setDeptName("检评");
        perCommissionEntity1.setCmsAmount(yearDeep);
        perCommissionEntity1.setCommissionDate(commissionDate);
        perCommissionEntity1.setType("采样年底提成");
        perCommissionEntityList.add(perCommissionEntity1);

        return perCommissionEntityList;
    }


    /**
     * 根据ID获取详情信息
     * @param id
     * @return
     */
    public PerformanceAllocationEntity getInfo(Long id){
        PerformanceAllocationEntity performanceAllocationEntity = baseMapper.selectById(id);
        List<PerCommissionEntity> commissionEntityList = perCommissionService.getListByIdAndType(performanceAllocationEntity.getId(),performanceAllocationEntity.getProjectId(),performanceAllocationEntity.getTypes());
        performanceAllocationEntity.setPerCommissionEntityList(commissionEntityList);

        return performanceAllocationEntity;
    }


}
