package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.jianping.entity.IndustryEntity;

import java.util.List;

public interface IndustryService extends IService<IndustryEntity> {
    /**
     * 根据joint查询项目类别
     * @param joint
     * @return
     */
    List<IndustryEntity> listJoint(String joint);
}
