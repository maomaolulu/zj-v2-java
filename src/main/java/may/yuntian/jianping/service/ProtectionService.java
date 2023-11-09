package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.jianping.entity.ProtectionEntity;

import java.util.List;

/**
 * 个人防护用品信息
 * 业务逻辑层接口
 *
 * @author zhanghao
 * @date 2022-03-10
 */
public interface ProtectionService extends IService<ProtectionEntity> {
    /**
     * 根据名称获取个人防护用品
     * @param name
     * @return
     */
    List<ProtectionEntity> getProtectionList(String name);

}