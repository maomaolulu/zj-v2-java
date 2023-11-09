package may.yuntian.laboratory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.laboratory.entity.InstrumentEntity;

import java.util.List;

public interface InstrumentService extends IService<InstrumentEntity> {
    List<InstrumentEntity> selectAllAndSplicing(String name);
}
