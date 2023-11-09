package may.yuntian.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.modules.sys.entity.SysDictEntity;

import java.util.List;
import java.util.Map;

/**
 * 数据字典
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2018-02-16
 */
public interface SysDictService extends IService<SysDictEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    /**
     * 根据字典类型显示列表
     * @param type
     * @return
     */
    List<SysDictEntity> listByType(String type);
    
    /**
     * 根据字典类型与字典码查询字典类型对象
     * @param type 字典类型
     * @param code 字典码
     * @return
     */
    SysDictEntity queryByTypeAndCode(String type, String code);
}

