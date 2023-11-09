package may.yuntian.commission.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.commission.entity.PerCommissionEntity;
import may.yuntian.commission.vo.PojectCommissionVo;

import java.util.List;
import java.util.Map;

/**
 * @program: ANLIAN-JAVA
 * @description:
 * @author: liyongqiang
 * @create: 2022-05-29 18:44
 */
@SuppressWarnings("all")
public interface PerCommissionService extends IService<PerCommissionEntity> {



    /**
     * 根据检评绩效分配ID获取列表信息
     * @param performanceAllocationId
     * @return
     */
    List<PerCommissionEntity> getListByPerformanceAllocationId(Long performanceAllocationId,Long projectId);


    /**
     * 根据检评绩效分配ID获取信息
     * @param performanceAllocationId
     * @return
     */
    PerCommissionEntity getByPerformanceAllocationId(Long performanceAllocationId,Long projectId,String name);



    /**
     * g根据提成人查询
     * @param personnel
     * @return
     */
    List<Long> getListByPersonnel(String personnel);

    /**
     * 检评绩效分配页面所需获取列表接口
     * @param performanceAllocationId
     * @param projectId
     * @param type
     * @return
     */
    List<PerCommissionEntity> getListByIdAndType(Long performanceAllocationId, Long projectId, String type);

}
