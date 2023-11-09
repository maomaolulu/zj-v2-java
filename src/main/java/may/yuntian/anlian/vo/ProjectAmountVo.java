package may.yuntian.anlian.vo;

import com.baomidou.mybatisplus.annotation.TableId;


public class ProjectAmountVo {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 项目ID
     */
    private Long projectId;
    /**
     * 项目金额(元)大于
     */
    private String totalMoneyMin;
    /**
     * 项目金额(元)小于
     */
    private String totalMoneyMax;
    /**
     * 已收款金额(元)大于
     */
    private String receiptMoneyMin;
    /**
     * 已收款金额(元)小于
     */
    private String receiptMoneyMax;
    /**
     * 未结算金额大于
     */
    private String nosettlementMoneyMin;
    /**
     * 未结算金额小于
     */
    private String nosettlementMoneyMax;
    /**
     * 佣金金额(元)大于
     */
    private String commissionMin;
    /**
     * 佣金金额(元)小于
     */
    private String commissionMax;
    /**
     * 佣金比例,佣金/总金额
     */
    private String commissionRatio;
    /**
     * 佣金未结算金额
     */
    private String commissionOutstanding;
    /**
     * 评审费(元)大于
     */
    private String evaluationFeeMin;
    /**
     * 评审费(元)小于
     */
    private String evaluationFeeMax;
    /**
     * 未结算评审费(元)
     */
    private String evaluationOutstanding;
    /**
     * 分包费(元)大于
     */
    private String subprojectFeeMin;
    /**
     * 分包费(元)小于
     */
    private String subprojectFeeMax;
    /**
     * 未结算分包费(元)
     */
    private String subprojectOutstanding;
    /**
     * 服务费用(元)大于
     */
    private String serviceChargeMin;
    /**
     * 服务费用(元)小于
     */
    private String serviceChargeMax;
    /**
     * 未结算服务费用(元)
     */
    private String serviceChargeOutstanding;
    /**
     * 其他支出(元)大于
     */
    private String otherExpensesMin;
    /**
     * 其他支出(元)小于
     */
    private String otherExpensesMax;
    /**
     * 未结算的其他支出(元)
     */
    private String otherExpensesOutstanding;
    /**
     * 已开票金额(元)大于
     */
    private String invoiceMoneyMin;
    /**
     * 已开票金额(元)小于
     */
    private String invoiceMoneyMax;
    /**
     * 项目净值(元)大于
     */
    private String netvalueMin;
    /**
     * 项目净值(元)小于
     */
    private String netvalueMax;
    /**
     * 虚拟税费(元)大于
     */
    private String virtualTaxMin;
    /**
     * 虚拟税费(元)小于
     */
    private String virtualTaxMax;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getTotalMoneyMin() {
        return totalMoneyMin;
    }

    public void setTotalMoneyMin(String totalMoneyMin) {
        this.totalMoneyMin = totalMoneyMin;
    }

    public String getTotalMoneyMax() {
        return totalMoneyMax;
    }

    public void setTotalMoneyMax(String totalMoneyMax) {
        this.totalMoneyMax = totalMoneyMax;
    }

    public String getReceiptMoneyMin() {
        return receiptMoneyMin;
    }

    public void setReceiptMoneyMin(String receiptMoneyMin) {
        this.receiptMoneyMin = receiptMoneyMin;
    }

    public String getReceiptMoneyMax() {
        return receiptMoneyMax;
    }

    public void setReceiptMoneyMax(String receiptMoneyMax) {
        this.receiptMoneyMax = receiptMoneyMax;
    }

    public String getNosettlementMoneyMin() {
        return nosettlementMoneyMin;
    }

    public void setNosettlementMoneyMin(String nosettlementMoneyMin) {
        this.nosettlementMoneyMin = nosettlementMoneyMin;
    }

    public String getNosettlementMoneyMax() {
        return nosettlementMoneyMax;
    }

    public void setNosettlementMoneyMax(String nosettlementMoneyMax) {
        this.nosettlementMoneyMax = nosettlementMoneyMax;
    }

    public String getCommissionMin() {
        return commissionMin;
    }

    public void setCommissionMin(String commissionMin) {
        this.commissionMin = commissionMin;
    }

    public String getCommissionMax() {
        return commissionMax;
    }

    public void setCommissionMax(String commissionMax) {
        this.commissionMax = commissionMax;
    }

    public String getCommissionRatio() {
        return commissionRatio;
    }

    public void setCommissionRatio(String commissionRatio) {
        this.commissionRatio = commissionRatio;
    }

    public String getCommissionOutstanding() {
        return commissionOutstanding;
    }

    public void setCommissionOutstanding(String commissionOutstanding) {
        this.commissionOutstanding = commissionOutstanding;
    }

    public String getEvaluationFeeMin() {
        return evaluationFeeMin;
    }

    public void setEvaluationFeeMin(String evaluationFeeMin) {
        this.evaluationFeeMin = evaluationFeeMin;
    }

    public String getEvaluationFeeMax() {
        return evaluationFeeMax;
    }

    public void setEvaluationFeeMax(String evaluationFeeMax) {
        this.evaluationFeeMax = evaluationFeeMax;
    }

    public String getEvaluationOutstanding() {
        return evaluationOutstanding;
    }

    public void setEvaluationOutstanding(String evaluationOutstanding) {
        this.evaluationOutstanding = evaluationOutstanding;
    }

    public String getSubprojectFeeMin() {
        return subprojectFeeMin;
    }

    public void setSubprojectFeeMin(String subprojectFeeMin) {
        this.subprojectFeeMin = subprojectFeeMin;
    }

    public String getSubprojectFeeMax() {
        return subprojectFeeMax;
    }

    public void setSubprojectFeeMax(String subprojectFeeMax) {
        this.subprojectFeeMax = subprojectFeeMax;
    }

    public String getSubprojectOutstanding() {
        return subprojectOutstanding;
    }

    public void setSubprojectOutstanding(String subprojectOutstanding) {
        this.subprojectOutstanding = subprojectOutstanding;
    }

    public String getServiceChargeMin() {
        return serviceChargeMin;
    }

    public void setServiceChargeMin(String serviceChargeMin) {
        this.serviceChargeMin = serviceChargeMin;
    }

    public String getServiceChargeMax() {
        return serviceChargeMax;
    }

    public void setServiceChargeMax(String serviceChargeMax) {
        this.serviceChargeMax = serviceChargeMax;
    }

    public String getServiceChargeOutstanding() {
        return serviceChargeOutstanding;
    }

    public void setServiceChargeOutstanding(String serviceChargeOutstanding) {
        this.serviceChargeOutstanding = serviceChargeOutstanding;
    }

    public String getOtherExpensesMin() {
        return otherExpensesMin;
    }

    public void setOtherExpensesMin(String otherExpensesMin) {
        this.otherExpensesMin = otherExpensesMin;
    }

    public String getOtherExpensesMax() {
        return otherExpensesMax;
    }

    public void setOtherExpensesMax(String otherExpensesMax) {
        this.otherExpensesMax = otherExpensesMax;
    }

    public String getOtherExpensesOutstanding() {
        return otherExpensesOutstanding;
    }

    public void setOtherExpensesOutstanding(String otherExpensesOutstanding) {
        this.otherExpensesOutstanding = otherExpensesOutstanding;
    }

    public String getInvoiceMoneyMin() {
        return invoiceMoneyMin;
    }

    public void setInvoiceMoneyMin(String invoiceMoneyMin) {
        this.invoiceMoneyMin = invoiceMoneyMin;
    }

    public String getInvoiceMoneyMax() {
        return invoiceMoneyMax;
    }

    public void setInvoiceMoneyMax(String invoiceMoneyMax) {
        this.invoiceMoneyMax = invoiceMoneyMax;
    }

    public String getNetvalueMin() {
        return netvalueMin;
    }

    public void setNetvalueMin(String netvalueMin) {
        this.netvalueMin = netvalueMin;
    }

    public String getNetvalueMax() {
        return netvalueMax;
    }

    public void setNetvalueMax(String netvalueMax) {
        this.netvalueMax = netvalueMax;
    }

    public String getVirtualTaxMin() {
        return virtualTaxMin;
    }

    public void setVirtualTaxMin(String virtualTaxMin) {
        this.virtualTaxMin = virtualTaxMin;
    }

    public String getVirtualTaxMax() {
        return virtualTaxMax;
    }

    public void setVirtualTaxMax(String virtualTaxMax) {
        this.virtualTaxMax = virtualTaxMax;
    }

}
