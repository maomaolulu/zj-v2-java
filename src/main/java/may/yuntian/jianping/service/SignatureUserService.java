package may.yuntian.jianping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.jianping.dto.SignatureInformationDTO;
import may.yuntian.jianping.entity.SignatureUserEntity;

import java.util.List;
import java.util.Map;


/**
 * 业务逻辑层接口
 * @author : lixin
 * @date : 2022-3-15
 * @desc : 电子签名对应人员表
 */
public interface SignatureUserService extends IService<SignatureUserEntity> {

    /**
     * 获取报告采样人员调查人员列表
     * @param name
     * @return
     */
    List<SignatureUserEntity> getListByName(String name);


    /**
     * 获取人员信息列表
     * @return
     */
    List<SignatureUserEntity> getPersonnelInformation();

    /**
     * 保存人员签名信息
     * @param dto
     */
    void updateSave(SignatureInformationDTO dto);

    /**
     * 已审核签名列表
     * @param params
     * @return
     */
    List<SignatureUserEntity> queryPageByStatus(Map<String, Object> params);

    /**
     * 待审核签名列表
     * @param params
     * @return
     */
    List<SignatureUserEntity> listInfo(Map<String, Object> params);

    /**
     * 驳回签名信息
     * @param user
     */
    void reject(SignatureUserEntity user);

    /**
     * 通过签名信息
     * @param user
     */
    void pass(SignatureUserEntity user);

    /**
     * 禁用或启用按钮
     * @param user
     */
    void enableOrDisable(SignatureUserEntity user);

    public SignatureUserEntity getOneInfo(Integer userId);
}
