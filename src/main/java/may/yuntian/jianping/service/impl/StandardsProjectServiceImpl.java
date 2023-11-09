package may.yuntian.jianping.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.jianping.entity.StandardsProjectEntity;
import may.yuntian.jianping.mapper.StandardsProjectMapper;
import may.yuntian.jianping.service.StandardsProjectService;
import org.springframework.stereotype.Service;

@Service("standardsProjectService")
public class StandardsProjectServiceImpl extends ServiceImpl<StandardsProjectMapper, StandardsProjectEntity> implements StandardsProjectService {
}
