package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.jianping.entity.ArtisanEntity;

import java.util.List;
import java.util.Map;

/**
 * 技术人员信息
 * 业务逻辑层接口
 *
 * @author LiXin
 * @date 2020-11-17
 */
public interface ArtisanService extends IService<ArtisanEntity> {

	/**
	 *不分页查询技术人员信息全部列表
	 */
	List<ArtisanEntity> queryList(Map<String, Object> params);
	/**
	 *分页查询 现在不用
	 */
	PageUtils queryPage(Map<String, Object> params);


    /**
     * 根据名字获取调查/采样人员列表
     * @param
     * @return
     */
    List<ArtisanEntity> getDioChaList(String name);


}