package may.yuntian.jianping.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.jianping.entity.LawsProjectEntity;
import may.yuntian.jianping.mapper.LawsProjectMapper;
import may.yuntian.jianping.service.LawsProjectService;
import org.springframework.stereotype.Service;

@Service("lawsProjectService")
public class LawsProjectServiceImpl extends ServiceImpl<LawsProjectMapper, LawsProjectEntity> implements LawsProjectService {
}
