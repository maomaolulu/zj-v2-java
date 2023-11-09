package may.yuntian.jianping.service;

import may.yuntian.jianping.entity.ConclusionPyEntity;

import java.util.List;
import java.util.Map;

/**
 * 结论-接口层
 *
 * @author hjy
 * @description python代码迁移，保持原python风格
 * @date 2023/7/12 14:50
 */
public interface TestReportPyService {
    /**
     * 结论列表
     *
     * @param projectId 项目id
     * @return 结论
     */
    List<ConclusionPyEntity> getConclusionList(Long projectId);

    /**
     * 批量修改 结论
     *
     * @param conclusions 结论信息集合
     */
    void updateConclusions(List<ConclusionPyEntity> conclusions);

    /**
     * 结论信息删除
     *
     * @param projectId     项目id
     * @param conclusionIds 结论id集合
     */
    void deleteConclusions(Long projectId, List<Long> conclusionIds);

    /**
     * 结果计算列表
     *
     * @param projectId 项目id
     * @param sType     类型：1 ：化学；3  噪声；4 高温；
     * @return 计算列表
     */
    Map<String, Object> getResultList(Long projectId, Integer sType);
}
