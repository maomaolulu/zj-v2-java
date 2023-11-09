package may.yuntian.jianping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;
import may.yuntian.jianping.entity.SampleImgEntity;
import may.yuntian.jianping.mapper.SampleImgMapper;
import may.yuntian.jianping.service.SampleImgService;
import may.yuntian.modules.sys.entity.SysUserEntity;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * 采样影像记录
 * 业务逻辑层实现类
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-09-25 00:31:49
 */
@Service("sampleImgService")
public class SampleImgServiceImpl extends ServiceImpl<SampleImgMapper, SampleImgEntity> implements SampleImgService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
    	String projectId = (String)params.get("projectId");//项目ID
        IPage<SampleImgEntity> page = this.page(
                new Query<SampleImgEntity>().getPage(params),
                new QueryWrapper<SampleImgEntity>()
                .eq(StringUtils.isNotBlank(projectId),"project_id", projectId)
        );
        return new PageUtils(page);
    }


    /**
     * 根据项目ID获取采集影像列表
     * @param projectId
     * @return
     */
    public List<SampleImgEntity> getListByProjectId(Long projectId){
        List<SampleImgEntity> list = baseMapper.selectList(new QueryWrapper<SampleImgEntity>().eq("project_id",projectId));
        return list;
    }

    /**
     * 先删除老数据 重新保存新数据
     * @param list
     */
    public void saveSampleImg(List<SampleImgEntity> list){
        Long projectId = list.get(0).getProjectId();
//        List<SampleImgEntity> sampleImgList = getListByProjectId(projectId);
        this.remove(new QueryWrapper<SampleImgEntity>().eq("project_id",projectId));
        SysUserEntity sysUserEntity = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal());
        for (SampleImgEntity sampleImg:list){
            sampleImg.setEditor(sysUserEntity.getUsername());
            this.save(sampleImg);
        }
    }

}
