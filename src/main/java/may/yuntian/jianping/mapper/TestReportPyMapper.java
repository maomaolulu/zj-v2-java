package may.yuntian.jianping.mapper;

import may.yuntian.jianping.entity.ConclusionPyEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 检测报告
 *
 * @author hjy
 * @date 2023/7/12 15:02
 */
@Mapper
@Repository
public interface TestReportPyMapper {
    /**
     * 结论列表
     *
     * @param projectId 项目id
     * @return 结论
     */
    List<ConclusionPyEntity> getConclusionList(Long projectId);

    /**
     * 修改结论
     *
     * @param conclusion 结论信息
     */
    void updateConclusion(ConclusionPyEntity conclusion);

    /**
     * 结论信息删除
     *
     * @param projectId     项目id
     * @param conclusionIds 结论id集合
     */
    void deleteConclusions(@Param("projectId") Long projectId, @Param("ids") List<Long> conclusionIds);
}
