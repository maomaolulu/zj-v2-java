package may.yuntian.jianping.vo;

import lombok.Data;

import java.util.List;

@Data
public class MathResultVo {

    private String id;

    private Long projectId;

    private Integer isFixed;

    private String postId;

    private String testPlace;

    private String pointId;

    private Long substanceId;

    private String substanceName;

    private List<SubstanceSampleCodeVo> substanceSampleCodeList;

}
