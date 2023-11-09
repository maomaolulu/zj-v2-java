package may.yuntian.external.datainterface.script.service;

/**
 * 脚本操作 Service层
 *
 * @author cwt
 * @Create 2023-4-13 16:12:27
 */
public interface ScriptService {

    /**
     * pro_detection_substance  表数据处理
     */
    void tableDataProcessing();


    /**
     * al_substance_copy_to_pro  表数据处理
     */
    void tableDataProcessing1();


    /**
     * zj_workspace脏数据处理
     */
    void mongoTest();

    /**
     * zj_post_pfn脏数据处理
     */
    void mongoTest1();
}
