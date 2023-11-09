package may.yuntian.jianping.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.anlian.mapper.ProjectMapper;
import may.yuntian.anlian.service.ProjectService;
import may.yuntian.anlian.utils.Number2Money;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;
import may.yuntian.jianping.entity.SubstanceEntity;
import may.yuntian.jianping.mapper.SubstanceMapper;
import may.yuntian.jianping.service.SubstanceService;
import may.yuntian.jianping.vo.SubstanceNewVo;
import may.yuntian.jianping.vo.SubstanceVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 检测物质数据
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @email ''
 * @date 2022-03-07 09:39:33
 */
@Service("substanceService")
public class SubstanceServiceImpl extends ServiceImpl<SubstanceMapper, SubstanceEntity> implements SubstanceService {


    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        params = Number2Money.getPageInfo(params);
        IPage<SubstanceEntity> page = this.page(
                new Query<SubstanceEntity>().getPage(params),
                new QueryWrapper<SubstanceEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 获取全部数据
     *
     * @return
     */
    public List<SubstanceVo> getListAll() {
        List<SubstanceVo> list = baseMapper.getListAll();
        return list;
    }

    /**
     * 获取物质信息
     *
     * @param name
     * @return
     */
    public SubstanceEntity getByName(String name) {
        SubstanceEntity substanceEntity = baseMapper.selectOne(new QueryWrapper<SubstanceEntity>().eq("name", name).last("limit 1"));

        return substanceEntity;
    }


    /**
     * 获取固定岗位物质
     *
     * @param name
     * @return
     */
    @Override
    public List<SubstanceVo> getListByShortcutKey(String name) {
        List<SubstanceVo> substanceVoList = new ArrayList<>();
        QueryWrapper<SubstanceEntity> queryWrapper = new QueryWrapper<SubstanceEntity>();
        queryWrapper.eq("sa.sample_mode", 1);

        String[] ids = new String[]{};
        if (name.equals("噪声")) {
            ids = new String[]{"645"};//噪声
            queryWrapper.in("s.id", Arrays.asList(ids));
            substanceVoList = baseMapper.getListByShortcutKey(queryWrapper);
        } else if (name.equals("焊接")) {
            ids = new String[]{"645", "556", "218", "95", "35", "643"};//噪声、电焊烟尘、锰及其无机化合物、氮氧化物、臭氧、电焊弧光
            queryWrapper.in("s.id", Arrays.asList(ids));
            substanceVoList = baseMapper.getListByShortcutKey(queryWrapper);
        } else if (name.equals("二保焊")) {
            ids = new String[]{"645", "556", "218", "95", "35", "643", "327"};//噪声、电焊烟尘、锰及其无机化合物、氮氧化物、臭氧、电焊弧光、一氧化碳
            queryWrapper.in("s.id", Arrays.asList(ids));
            substanceVoList = baseMapper.getListByShortcutKey(queryWrapper);
        } else if (name.equals("刮腻子")) {
            ids = new String[]{"624", "17"};//其他粉尘（腻子粉）、苯乙烯
            queryWrapper.in("s.id", Arrays.asList(ids));
            substanceVoList = baseMapper.getListByShortcutKey(queryWrapper);
        } else if (name.equals("加油站")) {
            ids = new String[]{"12", "139", "69", "246", "375"};//苯、甲苯、二甲苯、溶剂汽油、正己烷
            queryWrapper.in("s.id", Arrays.asList(ids));
            substanceVoList = baseMapper.getListByShortcutKey(queryWrapper);
        } else if (name.equals("四个验证性")) {
            ids = new String[]{"12", "375", "87", "255"};//苯、正己烷、1,2-二氯乙烷、三氯甲烷
            queryWrapper.in("s.id", Arrays.asList(ids));
            substanceVoList = baseMapper.getListByShortcutKey(queryWrapper);
        } else if (name.equals("木工岗位")) {
            ids = new String[]{"645", "582"};//木粉尘、噪声
            queryWrapper.in("s.id", Arrays.asList(ids));
            substanceVoList = baseMapper.getListByShortcutKey(queryWrapper);
        } else if (name.equals("三苯两酯")) {
            ids = new String[]{"12", "139", "69", "350", "346"};//苯、甲苯、二甲苯、乙酸乙酯、乙酸丁酯
            queryWrapper.in("s.id", Arrays.asList(ids));
            substanceVoList = baseMapper.getListByShortcutKey(queryWrapper);
        }
        return substanceVoList;
    }

    /**
     * 分流xin查询物质信息
     *
     * @param companyName
     * @return
     */
    @Override
    public List<SubstanceNewVo> getListNewAll(String companyName) {
        List<SubstanceNewVo> list = baseMapper.getListNewAll(companyName);
        // 修改:ind_sample  ->  has_solo   (0，1 -> 1，2)
        // is_qualification -> authentication  (1,2->是，否)
//        for (SubstanceNewVo substanceNewVo : list) {
//            switch (substanceNewVo.getHasSolo()) {
//                case 0:
//                    substanceNewVo.setHasSolo(2);
//                    break;
//                case 1:
//                    substanceNewVo.setHasSolo(1);
//                    break;
//                default:
//                    break;
//            }
//            String authentication = substanceNewVo.getAuthentication();
//            Integer integer = Integer.valueOf(authentication);
//            switch (integer) {
//                case 1:
//                    substanceNewVo.setAuthentication("是");
//                    break;
//                case 2:
        if (list!=null&&list.size()>0){
            for (SubstanceNewVo substanceNewVo : list) {
                switch (substanceNewVo.getHasSolo()) {
                    case 0:
                        substanceNewVo.setHasSolo(2);
                        break;
                    case 1:
                        substanceNewVo.setHasSolo(1);
                        break;
                    default:
                        break;
                }
                String authentication = substanceNewVo.getAuthentication();
                if (StringUtils.isNotBlank(authentication)){
                    Integer integer = Integer.valueOf(authentication);
                    switch (integer) {
                        case 1:
                            substanceNewVo.setAuthentication("是");
                            break;
                        case 2:
                            substanceNewVo.setAuthentication("否");
                            break;
                        default:
                            break;
                    }
                }else {
                    substanceNewVo.setAuthentication("否");
                }

            }
        }

        return list;
    }

    /**
     * 项目对应的公司
     *
     * @param projectId
     * @return
     */
    @Override
    public String getProjectCompany(Long projectId) {
        String companyName = projectMapper.selectById(projectId).getCompanyOrder();
        return companyName;
    }


}
