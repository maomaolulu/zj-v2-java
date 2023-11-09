package may.yuntian.external.datainterface.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 省报送-检测项目表（上游物质表需以此为对照）
 *
 * @author cwt
 * @Create 2023-4-14 10:40:45
 */
@Data
@TableName("pro_detection_substance")
public class ProDetectionSubstanceEntity implements Serializable {

    private static final long serialVersionUID = 42L;

    /**
     * id
     */
    private Long id;

    /**
     * 检测项目编码
     */
    private String checkItemCode;

    /**
     * 检测项目名称
     */
    private String itemName;

    /**
     * 物质表id   al_substance表的id
     */
    private Long subId;

    /**
     * 物质名称  al_substance表
     */
    private String subName;

    /**
     * 是否为省平台初始数据  1.是  2.自定义
     */
    private Integer isPro;

    /**
     * 有无资质  1: 有   2: 无
     */
    private Integer hasCredential;

}
