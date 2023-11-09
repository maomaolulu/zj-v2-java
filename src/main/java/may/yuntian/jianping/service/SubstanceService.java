package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.jianping.entity.SubstanceEntity;
import may.yuntian.jianping.vo.SubstanceNewVo;
import may.yuntian.jianping.vo.SubstanceVo;

import java.util.List;
import java.util.Map;

/**
 * 检测物质数据
 * 业务逻辑层接口
 *
 * @author LiXin
 * @email ''
 * @date 2022-03-07 09:39:33
 */
public interface SubstanceService extends IService<SubstanceEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取全部数据
     *
     * @return
     */
    List<SubstanceVo> getListAll();

    /**
     * 获取物质信息
     *
     * @param name
     * @return
     */
    SubstanceEntity getByName(String name);


    /**
     * 获取固定岗位物质
     *
     * @param name
     * @return
     */
    List<SubstanceVo> getListByShortcutKey(String name);

    /**
     * 分流xin查询物质信息
     * @param companyName
     * @return
     */
    List<SubstanceNewVo> getListNewAll(String companyName);

    /**
     * 项目对应的公司
     * @param projectId
     * @return
     */
    String getProjectCompany(Long projectId);
}

