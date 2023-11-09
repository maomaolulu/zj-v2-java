package may.yuntian.jianping.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.common.exception.RRException;
import may.yuntian.external.datainterface.constant.NumConstants;
import may.yuntian.jianping.dto.SignatureInformationDTO;
import may.yuntian.jianping.entity.SignatureUserEntity;
import may.yuntian.jianping.mapper.SignatureUserMapper;
import may.yuntian.jianping.service.SignatureUserService;
import may.yuntian.sys.utils.PageUtil2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 业务逻辑层实现类
 *
 * @author : lixin
 * @date : 2022-3-15
 * @desc : 电子签名对应人员表
 */
@Service("signatureUserService")
public class SignatureUserServiceImpl extends ServiceImpl<SignatureUserMapper, SignatureUserEntity> implements SignatureUserService {


    /**
     * 获取报告采样人员调查人员列表
     *
     * @param name
     * @return
     */
    @Override
    public List<SignatureUserEntity> getListByName(String name) {
        List<SignatureUserEntity> list = baseMapper.selectList(new QueryWrapper<SignatureUserEntity>()
                .like(StringUtils.isNotBlank(name), "username", name)
        );

        return list;
    }

    /**
     * 获取人员信息列表
     *
     * @return
     */
    @Override
    public List<SignatureUserEntity> getPersonnelInformation() {
        List<SignatureUserEntity> list = baseMapper.selectList(new QueryWrapper<>());
        return list;
    }

    /**
     * 保存人员签名信息
     *
     * @param dto
     */
    @Override
    public void updateSave(SignatureInformationDTO dto) {
        UpdateWrapper<SignatureUserEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("userid", dto.getUserId());
        if (StrUtil.isBlank(dto.getPath())) {
            throw new RRException("未拿到Path");
        }
        updateWrapper.set("path", dto.getPath());
        updateWrapper.set("status", 1);
        SignatureUserEntity signatureUserEntity = new SignatureUserEntity();
        baseMapper.update(signatureUserEntity, updateWrapper);
    }

    /**
     * 已审核签名列表
     *
     * @param params
     * @return
     */
    @Override
    public List<SignatureUserEntity> queryPageByStatus(Map<String, Object> params) {
        String username = (String) params.get("username");
        QueryWrapper<SignatureUserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 3);
        queryWrapper.like(StringUtils.isNotBlank(username), "username", username);
        PageUtil2.startPage();
        List<SignatureUserEntity> list = baseMapper.selectList(queryWrapper);
        return list;
    }

    /**
     * 待审核签名列表
     *
     * @param params
     * @return
     */
    @Override
    public List<SignatureUserEntity> listInfo(Map<String, Object> params) {
        String username = (String) params.get("username");
        QueryWrapper<SignatureUserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        queryWrapper.like(StringUtils.isNotBlank(username), "username", username);
        PageUtil2.startPage();
        List<SignatureUserEntity> list = baseMapper.selectList(queryWrapper);
        return list;
    }

    /**
     * 驳回签名信息
     *
     * @param user
     */
    @Override
    public void reject(SignatureUserEntity user) {
        UpdateWrapper<SignatureUserEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("userid", user.getUserid());
        updateWrapper.set("status", 0);
        baseMapper.update(user, updateWrapper);
    }

    /**
     * 通过签名信息
     *
     * @param user
     */
    @Override
    public void pass(SignatureUserEntity user) {
        UpdateWrapper<SignatureUserEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("userid", user.getUserid());
        updateWrapper.set("status", 3);
        baseMapper.update(user, updateWrapper);
    }

    /**
     * 禁用或启用按钮
     *
     * @param user
     */
    @Override
    public void enableOrDisable(SignatureUserEntity user) {
        UpdateWrapper<SignatureUserEntity> updateWrapper = new UpdateWrapper<>();
        if (NumConstants.NUMBER_ONE.equals(user.getSignStatus())) {
            updateWrapper.set("sign_status", 2);
        } else if (NumConstants.NUMBER_TWO.equals(user.getSignStatus())) {
            updateWrapper.set("sign_status", 1);
        }
        updateWrapper.set("enable_time", user.getEnableTime());
        updateWrapper.set("disable_time", user.getDisableTime());
        updateWrapper.eq("userid", user.getUserid());

        baseMapper.update(user, updateWrapper);
    }


    @Override
    public SignatureUserEntity getOneInfo(Integer userId){
        QueryWrapper<SignatureUserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        SignatureUserEntity signatureUserEntity = baseMapper.selectOne(queryWrapper);
        return signatureUserEntity;
    }

}
