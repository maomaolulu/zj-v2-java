package may.yuntian.jianping.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.models.auth.In;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.jianping.dto.ReportDto;
import may.yuntian.jianping.mapper.ReportStatisticsMapper;
import may.yuntian.jianping.service.ReportStatisticsService;
import may.yuntian.jianping.vo.IssuanceCountVo;
import may.yuntian.jianping.vo.ProjectStatusVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author gy
 * @date 2023-08-08 9:45
 */
@Service
public class ReportStatisticsServiceImpl implements ReportStatisticsService {

    @Autowired
    private ReportStatisticsMapper reportStatisticsMapper;
    private final List<String> fitProjectType = Arrays.asList("ZJ", "ZW");

    @Override
    public Map<String, Object> everyStageProject(ReportDto dto) {
        Map<String, Object> returnMap = new HashMap<>(6);
        QueryWrapper<Object> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(dto.getCompanyOrder()),"ap.company_order", dto.getCompanyOrder());
        queryWrapper.in("tc.name_en", fitProjectType);
        List<ProjectStatusVo> list = reportStatisticsMapper.selectProjectStatus(queryWrapper);
        // 现场调查
        returnMap.put("fieldInvestigation",list.stream().filter(fp -> fp.getStatus() == 4).count());
        // 采样
        returnMap.put("sampling",list.stream().filter(fp -> fp.getStatus() == 5).count());
        // 检测报告
        returnMap.put("detection",list.stream().filter(fp -> fp.getStatus() == 20).count());
        // 报告签发
        returnMap.put("toBeIssued",list.stream().filter(fp -> fp.getStatus() == 40).count());
        // 报告寄送
        returnMap.put("toBeArchived",list.stream().filter(fp -> fp.getStatus() == 50 && fp.getReportFiling() == null).count());
        // 归档
        returnMap.put("file",list.stream().filter(fp -> fp.getReportFiling() != null).count());
        return returnMap;
    }

    @Override
    public Object issuanceOverview(ReportDto dto) {
        Date startTime = dto.getStartTime(), endTime = dto.getEndTime(), now = new Date();
        if (startTime == null || endTime == null){
            switch (dto.getTimeLevel()){
                case "day":
                    startTime = DateUtil.beginOfDay(new Date());
                    endTime = DateUtil.offsetDay(startTime,1);
                    break;
                case "week":
                    startTime = DateUtil.beginOfWeek(now);
                    endTime = DateUtil.offsetDay(startTime,7);
                    break;
                case "month":
                    startTime = DateUtil.beginOfMonth(now);
                    endTime = DateUtil.offsetMonth(startTime,1);
                    break;
                case "year":
                    startTime = DateUtil.beginOfYear(now);
                    endTime = DateUtil.offsetMonth(startTime,12);
                    break;
                default:
                    break;
            }
        }else {
            //包含最后一天
            endTime = DateUtil.offsetDay(endTime,1);
        }
        List<String> names = reportStatisticsMapper.selectIssuanceOverview(getQuery(dto,startTime,endTime));
        return names.stream().collect(Collectors.groupingBy(s -> s, Collectors.summingInt(e -> 1)));
    }

    @Override
    public Map<String, Object> issuanceCount(ReportDto dto) {
        boolean ifGroupByMonth = false;
        String dateFormater = "MM-dd";
        if ("year".equals(dto.getTimeLevel())){
            ifGroupByMonth = true;
            dateFormater = "yyyy-MM";
        }
        Map<String, Object> returnMap = new HashMap<>(2);
        Map<String, Integer> mapCount = new LinkedHashMap<>();
        Date startTime = dto.getStartTime(), endTime = dto.getEndTime(), now = new Date();
        if (startTime == null || endTime == null){
            switch (dto.getTimeLevel()){
                case "day":
                    startTime = DateUtil.beginOfDay(new Date());
                    endTime = DateUtil.offsetDay(startTime,1);
                    break;
                case "week":
                    startTime = DateUtil.beginOfWeek(now);
                    endTime = DateUtil.offsetDay(startTime,7);
                    break;
                case "month":
                    startTime = DateUtil.beginOfMonth(now);
                    endTime = DateUtil.offsetMonth(startTime,1);
                    break;
                case "year":
                    startTime = DateUtil.beginOfYear(now);
                    endTime = DateUtil.offsetMonth(startTime,12);
                    break;
                default:
                    break;
            }
        }else {
            //包含最后一天
            endTime = DateUtil.offsetDay(endTime,1);
        }
        List<IssuanceCountVo> issuanceCountVos = reportStatisticsMapper.selectIssuanceCount(getQuery(dto, startTime, endTime));
        if (issuanceCountVos.size() == 0){
            Map<String, Integer> nullMapCount = new HashMap<>();
            Map<String, Integer> nullMapCricle = new HashMap<>(2);
            nullMapCricle.put("ZW",0);
            nullMapCricle.put("ZJ",0);
            returnMap.put("mapCount", nullMapCount);
            returnMap.put("mapCricle", nullMapCricle);
            return returnMap;
        }
        // 1.注入左表数据
        Map<String, List<IssuanceCountVo>> map;
        if (ifGroupByMonth){
            map = issuanceCountVos.stream().collect(Collectors.groupingBy(IssuanceCountVo::getMonth));
        }else {
            map = issuanceCountVos.stream().collect(Collectors.groupingBy(IssuanceCountVo::getDay));
        }
        long runningTime = startTime.getTime();
        while (runningTime < endTime.getTime()){
            Date key = new Date(runningTime);
            String stringKey = DateUtil.format(key, "yyyy-MM-dd");
            if (map.containsKey(stringKey)){
                mapCount.put(DateUtil.format(key,dateFormater), map.get(stringKey).size());
            }else {
                mapCount.put(DateUtil.format(key,dateFormater), 0);
            }
            Date nextStep;
            if (ifGroupByMonth){
                nextStep = DateUtil.offsetMonth(key, 1);
            }else {
                nextStep = DateUtil.offsetDay(key, 1);
            }
            runningTime = runningTime + nextStep.getTime() - key.getTime();
        }
        // 2.使用Stream的groupingBy和counting方法对List<IssuanceCountVo>进行分组并统计数量
        Map<String, Integer> mapCricle = issuanceCountVos.stream().collect(Collectors.groupingBy(IssuanceCountVo::getNameEn, Collectors.summingInt(e -> 1)));
        returnMap.put("mapCount", mapCount);
        returnMap.put("mapCricle", mapCricle);
        return returnMap;
    }

    /**
     * 构建通用查询条件
     */
    private QueryWrapper<Object> getQuery(ReportDto dto, Date startTime, Date endTime){
        QueryWrapper<Object> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(dto.getCompanyOrder()),"ap.company_order", dto.getCompanyOrder());
        wrapper.ge("tpp.createtime", startTime);
        wrapper.lt("tpp.createtime", endTime);
        wrapper.in("tc.name_en", fitProjectType);
        wrapper.eq("tpp.`status`", 40);
        return wrapper;
    }
}
