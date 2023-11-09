package may.yuntian.jianping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.jianping.entity.WarningBasicEntity;
import may.yuntian.jianping.mapper.WarningBasicMapper;
import may.yuntian.jianping.service.WarningBasicService;
import org.springframework.stereotype.Service;

import java.util.Map;


/**
 *职业危害警示标识设置基本信息
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @data 2021-03-18
 */
@Service("warningBasicService")
public class WarningBasicServiceImpl extends ServiceImpl<WarningBasicMapper, WarningBasicEntity> implements WarningBasicService {

	

	
	/**
	 * 查询条件
	 * @param params
	 * @return
	 */
	private QueryWrapper<WarningBasicEntity> queryWrapperByParmas(Map<String, Object> params){
		String warningLabels = (String)params.get("warningLabels");
		String instructLogo = (String)params.get("instructLogo");
		QueryWrapper<WarningBasicEntity> queryWrapper = new QueryWrapper<WarningBasicEntity>()
				.like(StringUtils.isNotBlank(warningLabels),"warning_labels", warningLabels)
				.like(StringUtils.isNotBlank(instructLogo),"instruct_logo", instructLogo);
				
		
		
		return queryWrapper;
	}
  
}
