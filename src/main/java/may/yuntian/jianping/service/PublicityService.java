package may.yuntian.jianping.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;

import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.jianping.mapper.PublicityMapper;
import may.yuntian.jianping.vo.PublicityPageVo;
import may.yuntian.untils.PageUtil2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Slf4j
@Service
@SuppressWarnings("all")
public class PublicityService {

    @Autowired
    private PublicityMapper publicityMapper;

    /**
     * 胶装开发页面
     *
     * @param params
     * @return
     */
    public List<PublicityPageVo> getBindingPageList(Map<String, Object> params) {
        String company = (String) params.get("company");
        String identifier = (String) params.get("identifier");
        String charge = (String) params.get("charge");
        String statusStr = (String) params.get("status");
        String protocol = (String) params.get("protocol");
        // 报告封面时间  report_cover_date 前段传递过来的 开始时间+结束时间
        String reportIssueStart = (String) params.get("reportIssueStart");
        String reportIssueEnd = (String) params.get("reportIssueEnd");
        Integer status = null;
        if (StringUtils.isNotBlank(statusStr)) {
            status = Integer.valueOf(statusStr);
        }


        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.between("p.status", 35, 80);
        queryWrapper.eq("p.type", "检评");
//        queryWrapper.ge("pd.task_release_date","2022-12-01");
//        queryWrapper.eq("cs.detection_type","定期检测");
//        queryWrapper.isNotNull("pd.report_cover_date");
        queryWrapper.between("p.binding_status", 1, 2);
//        queryWrapper.eq("p.binding_status",6);
//        queryWrapper.orderByAsc("pd.report_cover_date");
        queryWrapper.like(StringUtils.isNotBlank(company), "p.company", company);
        queryWrapper.like(StringUtils.isNotBlank(identifier), "p.identifier", identifier);
        queryWrapper.like(StringUtils.isNotBlank(charge), "p.charge", charge);
        queryWrapper.eq(StringUtils.checkValNotNull(status), "p.binding_status", status);
        queryWrapper.eq(StringUtils.isNotBlank(protocol), "p.protocol", protocol);
        queryWrapper.ge(StringUtils.isNotBlank(reportIssueStart), "pd.report_cover_date", reportIssueStart);
        queryWrapper.le(StringUtils.isNotBlank(reportIssueEnd), "pd.report_cover_date", reportIssueEnd);
        queryWrapper.orderByDesc("p.publicity_last_time");

//        QueryWrapper queryWrapper1 = queryWrapperByParamsAuth(queryWrapper);
        PageUtil2.startPage();
        List<PublicityPageVo> list = publicityMapper.getPublicityList(queryWrapper);
        return list;
    }


    /**
     * 项目负责人专属
     *
     * @param params
     * @return
     */
    public List<PublicityPageVo> getBindingPageListForLeader(Map<String, Object> params) {
        String company = (String) params.get("company");
        String identifier = (String) params.get("identifier");
        String charge = (String) params.get("charge");
        String statusStr = (String) params.get("status");
        String protocol = (String) params.get("protocol");
        String username = (String) params.get("username");
        // 报告封面时间  report_cover_date 前段传递过来的 开始时间+结束时间
        String reportIssueStart = (String) params.get("reportIssueStart");
        String reportIssueEnd = (String) params.get("reportIssueEnd");
        Integer status = null;
        if (StringUtils.isNotBlank(statusStr)) {
            status = Integer.valueOf(statusStr);
        }


        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.between("p.status", 35, 80);
        queryWrapper.eq("p.type", "检评");
//        queryWrapper.ge("pd.task_release_date","2022-12-01");
//        queryWrapper.eq("cs.detection_type","定期检测");
//        queryWrapper.isNotNull("pd.report_cover_date");
        queryWrapper.between("p.binding_status", 1, 2);
//        queryWrapper.eq("p.binding_status",6);
//        queryWrapper.orderByAsc("pd.report_cover_date");
        queryWrapper.like(StringUtils.isNotBlank(company), "p.company", company);
        queryWrapper.like(StringUtils.isNotBlank(identifier), "p.identifier", identifier);
        queryWrapper.like(StringUtils.isNotBlank(charge), "p.charge", charge);
        // 负责人 只看本人所属的
        queryWrapper.eq(StringUtils.isNotBlank(username), "p.charge", username);
        queryWrapper.eq(StringUtils.checkValNotNull(status), "p.binding_status", status);
        queryWrapper.eq(StringUtils.isNotBlank(protocol), "p.protocol", protocol);
        queryWrapper.ge(StringUtils.isNotBlank(reportIssueStart), "pd.report_cover_date", reportIssueStart);
        queryWrapper.le(StringUtils.isNotBlank(reportIssueEnd), "pd.report_cover_date", reportIssueEnd);

        queryWrapper.orderByDesc("p.publicity_last_time");


//        QueryWrapper queryWrapper1 = queryWrapperByParamsAuth(queryWrapper);
        PageUtil2.startPage();
        List<PublicityPageVo> list = publicityMapper.getPublicityList(queryWrapper);
        return list;
    }
}
