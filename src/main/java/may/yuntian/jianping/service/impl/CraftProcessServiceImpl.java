package may.yuntian.jianping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.jianping.entity.CraftProcessEntity;
import may.yuntian.jianping.mapper.CraftProcessMapper;
import may.yuntian.jianping.service.CraftProcessService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("craftProcessService")
public class CraftProcessServiceImpl extends ServiceImpl<CraftProcessMapper, CraftProcessEntity> implements CraftProcessService {


    @Override
    public List<CraftProcessEntity> craftList(Long projectId) {
        List<CraftProcessEntity> craftProcessEntityList = this.list(new QueryWrapper<CraftProcessEntity>()
                .eq("project_id", projectId)
        );
        if (craftProcessEntityList.size()!=0&&craftProcessEntityList!=null){
            ArrayList<String> list = new ArrayList<>();
            for (CraftProcessEntity processEntity: craftProcessEntityList){
                if (processEntity!=null){
                    String path = processEntity.getPath();
                    if (StringUtils.isNotBlank(path)){
                        String[] ss = path.split(",");
                        for (int i = 0; i < ss.length; i++) {
                            list.add(ss[i]);
                        }
                    }
                    processEntity.setPathAllList(list);
                }
            }
        }
        return craftProcessEntityList;
    }
}
