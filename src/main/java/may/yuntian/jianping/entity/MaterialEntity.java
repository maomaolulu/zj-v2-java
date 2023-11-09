package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName("zj_material")
@Data
public class MaterialEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 自增主键ID
     */
    @TableId
    private Long id;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 物料(原料、辅料、中间品、产品)
     */
    private String name;

    /**
     * 年消耗量/用量
     */
    private String consumption;

    /**
     * 最大储存量
     */
    private String maxStorage;

    /**
     * 状态(固态、液体、粉末等)
     */
    private String state;

    /**
     * 规格指标/包装规格
     */
    private String specs;

    /**
     * 主要成分
     */
    private String component;

    /**
     * 使用岗位(或场所)
     */
    private String whereUse;

    /**
     * 运输方式
     */
    private String transport;

    /**
     * 存储方式
     */
    private String storageMode;

    /**
     * 对应产品/对应工艺
     */
    private String correspondingProduct;

    /**
     * 储存场所
     */
    private String storageFacilities;

    /**
     * 储存周期
     */
    private String storageCycle;

    /**
     * 厂外运输方式
     */
    private String externalTransportation;

    /**
     *数据入库时间
     */
    private Date createtime;

    /**
     *修改时间
     */
    private Date updatetime;

    /**
     * 年产量
     */
    private String annualProduction;

    /**
     * 类型(1原辅料,2主要产品)
     */
    private Integer type;
}
