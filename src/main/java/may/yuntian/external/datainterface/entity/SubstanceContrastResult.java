package may.yuntian.external.datainterface.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 省报送-物质结果项编码表
 * @author: liyongqiang
 * @create: 2023-06-19 15:03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@TableName("pro_substance_contrast_result")
public class SubstanceContrastResult implements Serializable {
    private static final long serialVersionUID = 1L;

    /** id **/
    @TableId
    private Long id;
    /** （省）检测项目编码 **/
    private String checkItemCode;
    /** （省）检测项目名称 **/
    private String itemName;
    /** 结果项编码 **/
    private String resultItemCode;
    /** 结果项名称 **/
    private String resultItemName;
    /** （安联）物质表id   al_substance表的id **/
    private Long subId;
    /** （安联）物质名称  al_substance表 **/
    private String subName;
    /** 是否为省平台初始数据  1.是  2.自定义 **/
    private Integer isPro;
    /** 有无资质  1: 有   2: 无 **/
    private Integer hasCredential;
    /** 备注 **/
    private String remark;
    /** 计量单位编码 **/
    private String unit;

}
