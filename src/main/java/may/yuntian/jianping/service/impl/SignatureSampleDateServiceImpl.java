package may.yuntian.jianping.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.jianping.entity.SignatureSampleDateEntity;
import may.yuntian.jianping.mapper.SignatureSampleDateMapper;
import may.yuntian.jianping.service.SignatureSampleDateService;
import org.springframework.stereotype.Service;

/**
 * @author gy
 * @date 2023-07-19 16:01
 */
@Service("SignatureSampleDateServiceImpl")
public class SignatureSampleDateServiceImpl extends ServiceImpl<SignatureSampleDateMapper, SignatureSampleDateEntity> implements SignatureSampleDateService {
}
