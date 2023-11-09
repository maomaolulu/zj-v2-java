package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 送样收样记录表
 * </p>
 *
 * @author cwt
 * @since 2023-03-16
 */
@Data
@ApiModel(value="AlDeliverReceived对象", description="送样收样记录表")
public class AlDeliverReceived implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "自增主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "关联采样计划ID")
    private Long gatherPlanId;

    @ApiModelProperty(value = "项目ID")
    private Long projectId;

    @ApiModelProperty(value = "采样任务id(环境公卫)")
    private Long taskId;

    @ApiModelProperty(value = "项目编号")
    private String identifier;

    @ApiModelProperty(value = "受检单位名称")
    private String company;

    @ApiModelProperty(value = "受检单位地址")
    private String officeAddress;

    @ApiModelProperty(value = "送样人名称")
    private String deliverName;

    @ApiModelProperty(value = "样品名称")
    private String sampleName;

    @ApiModelProperty(value = "总收样人")
    private String received;

    @ApiModelProperty(value = "采样日期")
    private Date gatherDate;

    @ApiModelProperty(value = "送样日期")
    private Date deliverDate;

    @ApiModelProperty(value = "收样日期")
    private Date receivedDate;

    @ApiModelProperty(value = "采样地点")
    private String place;

    @ApiModelProperty(value = "送样收样状态（1未送样，2送样，3部分收样，4全部收样）")
    private Integer status;

    @ApiModelProperty(value = "检测物质")
    private String substance;

    @ApiModelProperty(value = "检测依据")
    private String testBasis;

    @ApiModelProperty(value = "收集器")
    private String collector;

    @ApiModelProperty(value = "样品状态")
    private String preserveTraffic;

    @ApiModelProperty(value = "样品编号")
    private String sampleCode;

    @ApiModelProperty(value = "空白样品编号")
    private String sampleCodeKb;

    @ApiModelProperty(value = "样品数量")
    private Integer sampleNum;

    @ApiModelProperty(value = "职卫：空白样(0否、1是)；环境公卫：样品类型（1:普通样，2：空白样，3：平行样）")
    private Integer sampleBlank;

    @ApiModelProperty(value = "大气压、温度")
    private String temperaturePressure;

    @ApiModelProperty(value = "采样体积")
    private String sampleVolume;

    @ApiModelProperty(value = "保存方式及期限")
    private String preserveRequire;

    @ApiModelProperty(value = "接收人")
    private String receivedName;

    @ApiModelProperty(value = "接收日期")
    private Date receiveDate;

    @ApiModelProperty(value = "数据入库时间")
    private Date createtime;

    @ApiModelProperty(value = "修改时间")
    private Date updatetime;

    @ApiModelProperty(value = "采样物质ID")
    private Long substanceId;

    @ApiModelProperty(value = "多物质IDs")
    private String substanceIds;

    @ApiModelProperty(value = "可废除-检测人员ID")
    private Long userId;

    @ApiModelProperty(value = "可废除-物质原始记录文件编号")
    private String fileNumber;

    @ApiModelProperty(value = "可废除-物质原始记录文件名称")
    private String fileName;

    @ApiModelProperty(value = "可废除-物资原始记录类型")
    private String originalRecordType;

    @ApiModelProperty(value = "可废除-复核人ID")
    private Long reviewerId;

    @ApiModelProperty(value = "是否勾选: 0未选择(未确认收样) 、1选择（确认收样）")
    private Boolean isChoose;

    @ApiModelProperty(value = "收样数量")
    private Integer receivedNumber;

    @ApiModelProperty(value = "送样单批次(采样日期批次)")
    private String sampleDeliveryBatch;

    @ApiModelProperty(value = "检测项目对照数据id")
    private Long indicatorId;

    @ApiModelProperty(value = "采样方案/记录ID (mongo库)")
    private String planRecordId;

    @ApiModelProperty(value = "采样方案/记录ID (mongo库)唯一标识")
    private String mId;

    @ApiModelProperty(value = "合并采样物质名称")
    private String mergeName;

    @ApiModelProperty(value = "合并采样的所有物质中第一个物质的记录id")
    private String mergeFirstId;

    @ApiModelProperty(value = "lab_main_data表id")
    private Long mainDataId;

    @ApiModelProperty(value = "已打印的标签数量")
    private Integer printBarCodeNum;

    @ApiModelProperty(value = "已打印的样品编号")
    private String printSampleCodes;

    @ApiModelProperty(value = "已选中的样品编号")
    private String selectedSampleCodes;

    @ApiModelProperty(value = "0:没有勾选，1:人为勾选，2:已生成条形码标签（默认勾选）")
    private Integer isFlag;

    @ApiModelProperty(value = "岗位id")
    private String postId;


}
