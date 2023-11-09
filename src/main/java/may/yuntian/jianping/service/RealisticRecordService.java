package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.jianping.entity.RealisticRecordEntity;

import java.util.List;

public interface RealisticRecordService extends IService<RealisticRecordEntity> {

    List<RealisticRecordEntity> getListByRealisticId(Long id);

    void deleteRealisticId(Long realisticId);
}
