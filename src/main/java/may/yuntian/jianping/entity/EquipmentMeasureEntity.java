package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备布局测点布置图调查
 * 
 * @author LiXin
 * @email ''
 * @date 2022-09-18 18:50:58
 */
@Data
@TableName("zj_equipment_measure")
public class EquipmentMeasureEntity implements Serializable {
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
	 * 布局图名称
	 */
	private String name;
	/**
	 * 布局图路径
	 */
	private String path;
	/**
	 * 备注
	 */
	private String remarks;
	/**
	 * 数据入库时间
	 */
	private Date createtime;
	/**
	 * 修改时间
	 */
	private Date updatetime;

    /**
     * minio路径
     */
    private String minioPath;

}
