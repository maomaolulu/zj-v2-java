package may.yuntian.anlian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.anlian.entity.AgreementEntity;

import java.util.Map;

/**
 * 项目对应的委托协议列表
 * 业务逻辑层接口
 *
 * @author LiXin
 * @email ''
 * @date 2023-06-27 14:25:05
 */
public interface AgreementService extends IService<AgreementEntity> {
    public Integer selectListByProjectId(Long projectId);
}

