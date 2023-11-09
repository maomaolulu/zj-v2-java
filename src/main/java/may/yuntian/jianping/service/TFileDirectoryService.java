package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.jianping.entity.TFileDirectoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 文件目录表
 * 业务逻辑层接口
 *
 * @author LiXin
 * @email ''
 * @date 2022-04-14 20:45:49
 */
public interface TFileDirectoryService extends IService<TFileDirectoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 通过项目ID 获取文件目录列表
     * @param projectId
     * @return
     */
    List<TFileDirectoryEntity> getListByProjectId(Long projectId);
    
}

