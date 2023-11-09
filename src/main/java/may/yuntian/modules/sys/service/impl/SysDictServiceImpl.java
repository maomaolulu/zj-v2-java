package may.yuntian.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import may.yuntian.common.utils.Query;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.modules.sys.dao.SysDictDao;
import may.yuntian.modules.sys.entity.SysDictEntity;
import may.yuntian.modules.sys.service.SysDictService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("sysDictService")
public class SysDictServiceImpl extends ServiceImpl<SysDictDao, SysDictEntity> implements SysDictService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String name = (String)params.get("name");

        IPage<SysDictEntity> page = this.page(
            new Query<SysDictEntity>().getPage(params),
            new QueryWrapper<SysDictEntity>()
                .like(StringUtils.isNotBlank(name),"name", name)
        );

        return new PageUtils(page);
    }
    
    /**
     * 根据字典类型显示列表
     * @param type
     * @return
     */
    public List<SysDictEntity> listByType(String type){
    	List<SysDictEntity> list = this.list(
    			new QueryWrapper<SysDictEntity>()
    			.eq("del_flag", 0)
    			.like(StringUtils.isNotBlank(type),"type", type)
    			.orderByAsc("order_num")
    			);
    	
    	return list;
    }
    
    /**
     * 根据字典类型与字典码查询字典类型对象
     * @param type 字典类型
     * @param code 字典码
     * @return
     */
    public SysDictEntity queryByTypeAndCode(String type,String code){
    	SysDictEntity sysDictEntity = baseMapper.selectOne(
	    			new QueryWrapper<SysDictEntity>()
	    			.eq("del_flag", 0)
	    			.eq("type", type)
	    			.eq("code", code)
    			);
    	
    	return sysDictEntity;
    }

}
