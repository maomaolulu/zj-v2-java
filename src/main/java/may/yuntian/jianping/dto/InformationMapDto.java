package may.yuntian.jianping.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;


/**
 * 申报索引基本信息
 * 
 * @author lixin
 * @data 2022-11-11
 */
@Data
public class InformationMapDto implements Serializable {
	private static final long serialVersionUID = 1L;

    /** 用人单位名称 */
    @Field
    private String company;

    /** 统一社会信用代码 */
    @Field
    private String code;

    /** 单位注册地址 */
    @Field(name = "registered_address")
    private String registeredAddress;

    /** 作业场所地址 */
    @Field(name = "work_address")
    private String workAddress;

    /** 行业分类 */
    @Field
    private String industry;

    /** 经济类型 */
    @Field
    private String economy;

    /** 企业规模 */
    @Field
    private String scale;

    /** 在册职工总数 */
    @Field(name = "incumbency_nums")
    private Integer incumbencyNums;

    /** 接害总人数(含外委) */
    @Field(name = "total_num")
    private Integer totalNum;

    /** 职业卫生管理人员 */
    @Field
    private String management;

    /** 联系电话 */
    @Field
    private String phone;
}
