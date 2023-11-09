package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.jianping.entity.AlDeliverReceived;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 送样收样记录表 服务类
 * </p>
 *
 * @author cwt
 * @since 2023-03-16
 */
public interface AlDeliverReceivedService extends IService<AlDeliverReceived> {


    /**
     * 获取项目的送样状态
     * @param projectId
     * @return
     */
    List<AlDeliverReceived> alDeliverReceived (String projectId);

    /**
     * 获取是否送样
     * @param projectId
     * @return
     */
    public boolean isDeliver(Long projectId);

}
