package may.yuntian.anlian.service.impl;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import may.yuntian.anlian.mapper.AgreementMapper;
import may.yuntian.anlian.entity.AgreementEntity;
import may.yuntian.anlian.service.AgreementService;

/**
 * 项目对应的委托协议列表
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @email ''
 * @date 2023-06-27 14:25:05
 */
@Service("agreementService")
public class AgreementServiceImpl extends ServiceImpl<AgreementMapper, AgreementEntity> implements AgreementService {

    @Override
    public Integer selectListByProjectId(Long projectId){
        List<AgreementEntity> agreementEntities = baseMapper.selectList(new QueryWrapper<AgreementEntity>().eq("project_id",projectId));
        if (agreementEntities!=null&&agreementEntities.size()>0){
            return 1;
        }else {
            return 2;
        }
    }

}