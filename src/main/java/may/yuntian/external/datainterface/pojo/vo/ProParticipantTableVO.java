package may.yuntian.external.datainterface.pojo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 参与人员信息
 *
 * @author cwt
 * @Create 2023-4-12 16:50:26
 */
@Data
public class ProParticipantTableVO implements Serializable {

    private static final long serialVersionUID = 42L;

    /**
     * 项目id
     */
    private Long projectId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 服务事项编码  1：现场调查；2：现场采样/检测；3：实验室检测；4：评价
     */
    private String itemCode;

    /**
     * 服务事项名称  1：现场调查；2：现场采样/检测；3：实验室检测；4：评价
     */
    private String itemName;

}
