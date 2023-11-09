package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.jianping.entity.SignatureEntity;

import java.util.List;
import java.util.Map;


/**
 * 电子签名路径表
 * 
 * @author LiXin
 * @date 2021-01-29
 */
public interface SignatureService extends IService<SignatureEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据项目ID获取签名列表
     * @param projectId
     * @return
     */
    List<SignatureEntity> getListByProjectId(Long projectId);

    /**
     * 根据项目ID获取签名列表--内部人员
     * @param projectId
     * @return
     */
    List<SignatureEntity> getListNeiBuByProjectId(Long projectId);
}

