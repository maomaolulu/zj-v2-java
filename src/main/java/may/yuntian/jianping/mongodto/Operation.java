package may.yuntian.jianping.mongodto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ZhuYiCheng
 * @date 2023/9/6 16:44
 */
@Data
public class Operation implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 总人数
     */
    private Integer people_num = 0;
    /**
     * 设备名称 修改前
     */
    private String equip_name = "";
    /**
     * 设备列表 修改后  2023-05-29于企业微信--检评系统对接群讨论  2023-06-01 现场调查Java接口修改  2023-06-07 采样方案/记录Python端开始调整
     * 修改后 在送样单提交后，现场调查的部分字段将不能修改，这些字段会在方案生成时代入方案和记录信息； 作业内容/设备/人员/工程防护个人防护 这些信息现场调查时填写配置情况同步到方案/记录信息里  采用记录填写运行状况
     */
    private List<Map<String, String>> equip_lis = new ArrayList<>();
    /**
     * 作业内容
     */
    private String work_content = "";
}
