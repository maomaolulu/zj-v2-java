package may.yuntian.jianping.vo;

import lombok.Data;

/**
 * @author gy
 * @date 2023-07-31 14:40
 */
@Data
public class SubstanceMapVo {
    private Integer substanceId;
    private Integer totalDustId;
    private String name;
    private String nameEn;
    private String casNo;
    private String mac;
    private Double pcTwa;
    private Double pcStel;
    private String reaction;
    private String deduction;
    private String remarks;
    private String sType;
    private String computeMode;
    private String mark;
    private Boolean isSilica;
    private String remarksNote;
    private String sDept;
    private Boolean highlyToxic;
    private String mergeName;
    private String totalMergeName;
    private Integer mergeSort;
    private Integer indicatorId;
    private Integer sampleId;
    private String sampleTablename;
    private String standardSerialNum;
    private String standardName;
    private String equipment;
    private String flow;
    private String testTime;
    private String testTimeNote;
    private Boolean indSample;
    private Boolean indEquipment;
    private Boolean indFlow;
    private Boolean indTestTime;
    private Boolean indTestTimeNote;
    private String collector;
    private String preserveTraffic;
    private String preserveRequire;
    private String shelfLife;
    private String qualification;
    private Integer detectionMethodNum;
    private Integer markNum;
    private Integer mainDataId;
}
