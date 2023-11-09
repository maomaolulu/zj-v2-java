package may.yuntian.jianping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.anlian.utils.Number2Money;
import may.yuntian.common.utils.PageUtils;
import may.yuntian.common.utils.Query;
import may.yuntian.jianping.entity.ArtisanEntity;
import may.yuntian.jianping.mapper.ArtisanMapper;
import may.yuntian.jianping.service.ArtisanService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 技术人员信息
 * 业务逻辑层实现类
 *
 * @author LiXin
 * @date 2020-11-17
 */
@Service("artisanService")
public class ArtisanServiceImpl extends ServiceImpl<ArtisanMapper, ArtisanEntity> implements ArtisanService {


	
	/**
	 *不分页查询技术人员信息全部列表
	 */
	
	public List<ArtisanEntity> queryList(Map<String, Object> params) {
        params = Number2Money.getPageInfo(params);
		String userid = (String)params.get("userid");//用户ID
		String jobNum = (String)params.get("jobNum");
    	String username = (String)params.get("username");
    	String type = (String)params.get("type");
    	String startDate = (String)params.get("startDate");	//排单日期
    	String endDate = (String)params.get("endDate");		//排单日期
    	
    	List<ArtisanEntity> list = this.list(
					new QueryWrapper<ArtisanEntity>()
					.like(StringUtils.isNotBlank(username),"username", username)
					.eq(StringUtils.isNotBlank(userid),"userid", userid)
	                .like(StringUtils.checkValNotNull(jobNum), "job_num", jobNum)
	                .eq(StringUtils.checkValNotNull(type), "type", type)
				);
    	
//		//根据日期查询技术人员排单记录
//		// 根据日期查询 放在循环外面做一次查询 不用在循环里面做多次查询
//    	if(StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {//如果前端没传日期时将不查询时间段内人员行程
//
//			List<ArtisanTaskEntity> artisanTaskLists = artisanTaskService.list(new QueryWrapper<ArtisanTaskEntity>()
//					.between(StringUtils.isNotBlank(endDate), "task_date", startDate, endDate)//排单日期taskDate
//					);
//
//			List<ArtisanTaskEntity> artisanTaskList;//技术人员排单记录
//
//
//			for (ArtisanEntity artisanEntity : list) {
//				artisanTaskList = new ArrayList<ArtisanTaskEntity>();
//				Long artisanId = artisanEntity.getId();
//				for (ArtisanTaskEntity artisanTaskEntity : artisanTaskLists) {
//					if(artisanId == artisanTaskEntity.getArtisanId()) {
//						artisanTaskList.add(artisanTaskEntity);
//					}
//				}
//				artisanEntity.setArtisanTaskList(artisanTaskList);
//			}
//
//			List<PlanUserEntity> planUserList;
//
//			for (ArtisanEntity artisanEntity : list) {
//				planUserList = new ArrayList<PlanUserEntity>();
//				Long artisanId = artisanEntity.getId();
//				for (PlanUserEntity PlanUserEntity : planUserList) {
//					if(artisanId == PlanUserEntity.getArtisanId()) {
//						planUserList.add(PlanUserEntity);
//					}
//				}
//				artisanEntity.setPlanUserList(planUserList);
//			}
//
//    	}

		return list;
		
	}

    /**
     * 根据名字获取调查/采样人员列表
     * @param
     * @return
     */
    public List<ArtisanEntity> getDioChaList(String name) {
        List<ArtisanEntity> list = baseMapper.selectList(new QueryWrapper<ArtisanEntity>()
            .like(StringUtils.isNotBlank(name),"username",name)
        );
        return list;
    }

    /**
	 *分页查询 现在不用
	 */
	
	public PageUtils queryPage(Map<String, Object> params) {
		String userid = (String)params.get("userid");//用户ID
		String jobNum = (String)params.get("jobNum");
		String username = (String)params.get("username");
//		String startDate = (String)params.get("startDate");	//排单日期
//		String endDate = (String)params.get("endDate");		//排单日期
		
		IPage<ArtisanEntity> page = this.page(
				new Query<ArtisanEntity>().getPage(params),
				new QueryWrapper<ArtisanEntity>()
				.like(StringUtils.isNotBlank(username),"username", username)
				.eq(StringUtils.isNotBlank(userid),"userid", userid)
				.like(StringUtils.checkValNotNull(jobNum), "job_num", jobNum)
				//.orderByDesc("id")
				);
		
		return new PageUtils(page);
		
	}
	

}
