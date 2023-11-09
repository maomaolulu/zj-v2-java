package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.jianping.entity.WarningSignsEntity;

import java.util.List;
import java.util.Map;

/**
 * 职业危害警示标识设置一览表
 * 业务逻辑层接口
 *
 * @author LiXin
 * @data 2021-03-17
 */
public interface WarningSignsService extends IService<WarningSignsEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    /**
     * 通过项目id查询职业危害警示标识设置一览表
     * @param projectId
     * @return
     */
    List<WarningSignsEntity> seleteByProjectId(Long projectId);
    
    /**
     * 通过项目ID初始化生成职业危害警示标识设置一览列表
     * @param projectId
     */
    void initializeWarningSigns(WarningSignsEntity warningSignsEntity);
	
}