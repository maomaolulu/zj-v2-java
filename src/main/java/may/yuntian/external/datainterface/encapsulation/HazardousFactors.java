package may.yuntian.external.datainterface.encapsulation;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 危害因素封装
 *
 * @author cwt
 * @Create 2023-4-15 17:03:30
 */
@Data
public class HazardousFactors implements Serializable {

    private static final long serialVersionUID = 42L;

    /**
     * 检测日期
     */
    private Date date;

    /**
     * 结果
     */
    private String result;

    /**
     * 单项结论
     */
    private String conclusion;

    /**
     * mac
     */
    private String mac;

    /**
     * pcTwa
     */
    private String pcTwa;

    /**
     * pcStel
     */
    private String pcStel;

    /**
     * pe
     */
    private String pe;

    /**
     * c_m
     */
    private String cM;

    /**
     * c_twa
     */
    private String cTwa;

    /**
     * c_ste
     */
    private String cSte;

    /**
     * c_pe
     */
    private String cPe;

}
