package may.yuntian.jianping.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;


/**
 *职业病危害因素检测情况
 *
 * @author lixin
 * @data 2022-11-11
 */
@Data
public class DetectionDto implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 本年度检测情况 */
	@Field
    private String detection;

    /** 检测机构名称 */
    @Field(name = "company_name")
    private String companyName;

    /** 检测报告编号 */
    @Field(name = "report_number")
    private String reportNumber;

}
