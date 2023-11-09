package may.yuntian.jianping.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : lixin
 * @date : 2022-3-15
 * @desc : 电子签名对应人员表
 */
@TableName("t_signature_user")
public class SignatureUserEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 自增主键ID
     */
    @TableId
    private Long id;
    /** 用户ID */
    private Long userid ;
    /** 用户名 */
    private String username ;
    /** 签名路径 */
    @TableField(fill = FieldFill.UPDATE)
    private String path ;
    /** 状态(0.未签字,1.刚签字,3.审核通过) */
    private Integer status ;
    /** 签字状态(1启用,2停用) */
    private Integer signStatus ;
    /** 备注 */
    private String remarks ;
    /** 性别 */
    private String sex ;
    /** 学历 */
    private String education ;
    /** 所学专业 */
    private String speciality ;
    /** 职称 */
    private String title ;
    /** 方向 */
    private String direction ;
    /** 培训合格证书编号 */
    private String certificateNumber ;
    /** 发证日期 */
    private Date certificationDate ;
    /** 公司 */
    private String company ;
    /** 部门 */
    private String department ;
    /** 上岗证所属(1.内,2.外) */
    private Integer workLicense ;
    /** 启用时间 **/
    @TableField(fill = FieldFill.UPDATE)
    private Date enableTime;
    /** 禁用时间 **/
    @TableField(fill = FieldFill.UPDATE)
    private Date disableTime;
    /** 自增主键ID */
    public Long getId(){
        return this.id;
    }
    /** 自增主键ID */
    public void setId(Long id){
        this.id=id;
    }
    /** 用户ID */
    public Long getUserid(){
        return this.userid;
    }
    /** 用户ID */
    public void setUserid(Long userid){
        this.userid=userid;
    }
    /** 用户名 */
    public String getUsername(){
        return this.username;
    }
    /** 用户名 */
    public void setUsername(String username){
        this.username=username;
    }
    /** 签名路径 */
    public String getPath(){
        return this.path;
    }
    /** 签名路径 */
    public void setPath(String path){
        this.path=path;
    }
    /** 状态(1,刚签字,2驳回,3.审核通过) */
    public Integer getStatus(){
        return this.status;
    }
    /** 状态(1,刚签字,2驳回,3.审核通过) */
    public void setStatus(Integer status){
        this.status=status;
    }
    /** 签字状态(1启用,2停用) */
    public Integer getSignStatus(){
        return this.signStatus;
    }
    /** 签字状态(1启用,2停用) */
    public void setSignStatus(Integer signStatus){
        this.signStatus=signStatus;
    }
    /** 备注 */
    public String getRemarks(){
        return this.remarks;
    }
    /** 备注 */
    public void setRemarks(String remarks){
        this.remarks=remarks;
    }
    /** 性别 */
    public String getSex(){
        return this.sex;
    }
    /** 性别 */
    public void setSex(String sex){
        this.sex=sex;
    }
    /** 学历 */
    public String getEducation(){
        return this.education;
    }
    /** 学历 */
    public void setEducation(String education){
        this.education=education;
    }
    /** 所学专业 */
    public String getSpeciality(){
        return this.speciality;
    }
    /** 所学专业 */
    public void setSpeciality(String speciality){
        this.speciality=speciality;
    }
    /** 职称 */
    public String getTitle(){
        return this.title;
    }
    /** 职称 */
    public void setTitle(String title){
        this.title=title;
    }
    /** 方向 */
    public String getDirection(){
        return this.direction;
    }
    /** 方向 */
    public void setDirection(String direction){
        this.direction=direction;
    }
    /** 培训合格证书编号 */
    public String getCertificateNumber(){
        return this.certificateNumber;
    }
    /** 培训合格证书编号 */
    public void setCertificateNumber(String certificateNumber){
        this.certificateNumber=certificateNumber;
    }
    /** 发证日期 */
    public Date getCertificationDate(){
        return this.certificationDate;
    }
    /** 发证日期 */
    public void setCertificationDate(Date certificationDate){
        this.certificationDate=certificationDate;
    }
    /** 公司 */
    public String getCompany(){
        return this.company;
    }
    /** 公司 */
    public void setCompany(String company){
        this.company=company;
    }
    /** 部门 */
    public String getDepartment(){
        return this.department;
    }
    /** 部门 */
    public void setDepartment(String department){
        this.department=department;
    }
    /** 上岗证所属(1.内,2.外) */
    public Integer getWorkLicense(){
        return this.workLicense;
    }
    /** 上岗证所属(1.内,2.外) */
    public void setWorkLicense(Integer workLicense){
        this.workLicense=workLicense;
    }

    public Date getEnableTime() {
        return enableTime;
    }

    public void setEnableTime(Date enableTime) {
        this.enableTime = enableTime;
    }

    public Date getDisableTime() {
        return disableTime;
    }

    public void setDisableTime(Date disableTime) {
        this.disableTime = disableTime;
    }
}
