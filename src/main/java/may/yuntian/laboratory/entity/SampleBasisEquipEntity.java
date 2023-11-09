package may.yuntian.laboratory.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 *检测依据及设备实体类
 * 
 * @author LiXin
 * @data 2020-12-17
 */
@Data
@TableName("zj_sample_basis_equip")
public class SampleBasisEquipEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 *自增主键ID
	 */
	@TableId
	private Long id;
	/**
	 *项目ID
	 */
	private Long projectId;
	/**
	 *检测物质ID
	 */
	private Long substanceId;
	/**
	 *检测物质
	 */
	private String testItem;
	/**
	 *物质类型(0:无,1定点,2个体)
	 */
	private Integer itemCate;
	/**
	 *检测依据
	 */
	private String testBasis;
	/**
	 * 检测依据名称
	 */
	private String basisName;
	/**
	 *检测方法
	 */
	private String testMethod;
	/**
	 *检测仪器及编号
	 */
	private String equip;

    /**
     *检测仪器及编号
     */
    private String recordIds;

	/**
	 *数据入库时间
	 */
	private Date createtime;
	/**
	 *修改时间
	 */
	private Date updatetime;
	

}
