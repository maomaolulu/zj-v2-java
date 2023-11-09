package may.yuntian.jianping.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;


/**
 *个人防护用品
 * 
 * @author zhanghao
 * @data 2022-03-10
 */
@Data
public class ProtectionDto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 *自增主键ID
	 */
	@Field(name = "protection_id")
	private Long protectionId;
	/**
	 *类型
	 */
	@Field
	private String type;
	/**
	 *个人防护用品
	 */
	@Field
	private String name;
    /**
     * 个人防护用品使用情况
     */
    @Field
    private String protection;
	/**
	 *说明
	 */
	@Field
	private String illustrate;
	/**
	 *snr
	 */
	@Field
	private Integer snr;
	/**
	 *nrr
	 */
	@Field
	private Integer nrr;

    /** 个人防护佩戴情况 */
    @Field(name = "ppe_wear")
    private String ppeWear;
}
