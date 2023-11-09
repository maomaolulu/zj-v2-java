package may.yuntian.jianping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;
import may.yuntian.jianping.entity.SignatureEntity;
import may.yuntian.jianping.mapper.SignatureMapper;
import may.yuntian.jianping.service.SignatureService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 电子签名路径表
 * 
 * @author LiXin
 * @date 2021-01-29
 */
@Service("signatureService")
public class SignatureServiceImpl extends ServiceImpl<SignatureMapper, SignatureEntity> implements SignatureService {


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String projectId = (String)params.get("projectId");//项目ID

        IPage<SignatureEntity> page = this.page(
                new Query<SignatureEntity>().getPage(params),
                new QueryWrapper<SignatureEntity>()
                        .eq(StringUtils.isNotBlank(projectId),"project_id", projectId)
        );
        page.getRecords().forEach(action->{
            String useList = action.getUseList();
            if (StringUtils.isNotBlank(useList)) {
                String[] uStrings = useList.split(",");
                List<String> useSpitList = Arrays.asList(uStrings);
                action.setUseSpitList(useSpitList);
            }else {
                action.setUseSpitList(new ArrayList<>());
            }

        });
        return new PageUtils(page);
    }


    /**
     * 根据项目ID获取签名列表--陪同人
     * @param projectId
     * @return
     */
    public List<SignatureEntity> getListByProjectId(Long projectId){
        List<SignatureEntity> list = baseMapper.selectList(new QueryWrapper<SignatureEntity>().eq("project_id",projectId).eq("people_type",1));
        for (SignatureEntity signatureEntity :list){
            String useList = signatureEntity.getUseList();
            if (StringUtils.isNotBlank(useList)) {
                String[] uStrings = useList.split(",");
                List<String> useSpitList = Arrays.asList(uStrings);
                signatureEntity.setUseSpitList(useSpitList);
            }else {
                signatureEntity.setUseSpitList(new ArrayList<>());
            }
        }
        return list;
    }

    /**
     * 根据项目ID获取签名列表--内部人员
     * @param projectId
     * @return
     */
    public List<SignatureEntity> getListNeiBuByProjectId(Long projectId){
        List<SignatureEntity> list = baseMapper.selectList(new QueryWrapper<SignatureEntity>().eq("project_id",projectId).eq("people_type",2));
        for (SignatureEntity signatureEntity :list){
            String useList = signatureEntity.getUseList();
            if (StringUtils.isNotBlank(useList)) {
                String[] uStrings = useList.split(",");
                List<String> useSpitList = Arrays.asList(uStrings);
                signatureEntity.setUseSpitList(useSpitList);
            }else {
                signatureEntity.setUseSpitList(new ArrayList<>());
            }
        }
        return list;
    }

}
