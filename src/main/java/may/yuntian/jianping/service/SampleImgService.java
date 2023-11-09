package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.jianping.entity.SampleImgEntity;

import java.util.List;
import java.util.Map;

/**
 * 采样影像记录
 * 业务逻辑层接口
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-09-14 22:13:15
 */
public interface SampleImgService extends IService<SampleImgEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据项目ID获取采集影像列表
     * @param projectId
     * @return
     */
    List<SampleImgEntity> getListByProjectId(Long projectId);

    /**
     * 先删除老数据 重新保存新数据
     * @param list
     */
    void saveSampleImg(List<SampleImgEntity> list);
}

