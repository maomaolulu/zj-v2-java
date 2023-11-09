package may.yuntian.external.strategy.template;


import may.yuntian.external.datainterface.entity.EvalResultEntity;
import may.yuntian.external.datainterface.pojo.dto.InBatchGatherDTO;
import may.yuntian.external.datainterface.pojo.vo.ProResultItemVO;
import may.yuntian.external.strategy.template.base.Strategy;
import may.yuntian.jianping.dto.BatchSampleDto;
import may.yuntian.jianping.mongoentity.ResultEntity;

/**
 * 环境角色
 *
 * @author cwt
 * @date 2023-4-19 17:42:44
 */
public class Context {

    /**
     * 策略对象
     */
    private Strategy strategy;

    /**
     * 具体策略对象
     *
     * @param strategy
     */
    public Context(Strategy strategy) {
        this.strategy = strategy;
    }

    /**
     * 执行策略方法,检评结果项策略
     *
     * @date 2018年1月14日 下午8:43:31
     */
    public void contextInterfaceEv(ProResultItemVO proResultItemVO, BatchSampleDto batchSampleDto, ResultEntity resultEntity) {
        strategy.strategyInterfaceEv(proResultItemVO, batchSampleDto, resultEntity);
    }

    /**
     * 执行策略方法,评价结果项策略
     *
     * @date 2018年1月14日 下午8:43:31
     */
    public void contextInterfaceIn(ProResultItemVO proResultItemVO, EvalResultEntity eREntity, InBatchGatherDTO inBatchGatherDTO) {
        strategy.strategyInterfaceIn(proResultItemVO, eREntity, inBatchGatherDTO);
    }

    /**
     * 检评结果项策略
     *
     * @description 使用策略模式:针对一组算法，将每一个算法封装到具有共同接口的独立的类
     * @date 2023-4-20 08:50:25
     */
    public static void useStrategyEV(ProResultItemVO proResultItemVO, BatchSampleDto batchSampleDto, ResultEntity resultEntity) {
        // 具体使用策略
        Strategy noise = new Noise();
        Strategy field = new ElectricField();
        Strategy highFrequencyElectromagneticField = new HighFrequencyElectromagneticField();
        Strategy highTemperature = new HighTemperature();
        Strategy laserRadiation = new LaserRadiation();
        Strategy manualVibration = new ManualVibration();
        Strategy ultraHighFrequencyRadiation = new UltraHighFrequencyRadiation();
        Strategy ultravioletRadiation = new UltravioletRadiation();
        Strategy windSpeed = new WindSpeed();
        // 将策略放入环境中并执行策略
        new Context(noise).contextInterfaceEv(proResultItemVO, batchSampleDto, resultEntity);
        new Context(field).contextInterfaceEv(proResultItemVO, batchSampleDto, resultEntity);
        new Context(highFrequencyElectromagneticField).contextInterfaceEv(proResultItemVO, batchSampleDto, resultEntity);
        new Context(highTemperature).contextInterfaceEv(proResultItemVO, batchSampleDto, resultEntity);
        new Context(laserRadiation).contextInterfaceEv(proResultItemVO, batchSampleDto, resultEntity);
        new Context(manualVibration).contextInterfaceEv(proResultItemVO, batchSampleDto, resultEntity);
        new Context(ultraHighFrequencyRadiation).contextInterfaceEv(proResultItemVO, batchSampleDto, resultEntity);
        new Context(ultravioletRadiation).contextInterfaceEv(proResultItemVO, batchSampleDto, resultEntity);
        new Context(windSpeed).contextInterfaceEv(proResultItemVO, batchSampleDto, resultEntity);

    }


    /**
     * 评价结果项策略
     *
     * @description 使用策略模式:针对一组算法，将每一个算法封装到具有共同接口的独立的类
     * @date 2023-4-20 08:50:25
     */
    public static void useStrategyIn(ProResultItemVO proResultItemVO, EvalResultEntity eREntity, InBatchGatherDTO inBatchGatherDTO) {
        // 具体使用策略
        Strategy noise = new Noise();
        Strategy field = new ElectricField();
        Strategy highFrequencyElectromagneticField = new HighFrequencyElectromagneticField();
        Strategy highTemperature = new HighTemperature();
        Strategy laserRadiation = new LaserRadiation();
        Strategy manualVibration = new ManualVibration();
        Strategy ultraHighFrequencyRadiation = new UltraHighFrequencyRadiation();
        Strategy ultravioletRadiation = new UltravioletRadiation();
        Strategy windSpeed = new WindSpeed();
        // 将策略放入环境中并执行策略
        new Context(noise).contextInterfaceIn(proResultItemVO, eREntity, inBatchGatherDTO);
        new Context(field).contextInterfaceIn(proResultItemVO, eREntity, inBatchGatherDTO);
        new Context(highFrequencyElectromagneticField).contextInterfaceIn(proResultItemVO, eREntity, inBatchGatherDTO);
        new Context(highTemperature).contextInterfaceIn(proResultItemVO, eREntity, inBatchGatherDTO);
        new Context(laserRadiation).contextInterfaceIn(proResultItemVO, eREntity, inBatchGatherDTO);
        new Context(manualVibration).contextInterfaceIn(proResultItemVO, eREntity, inBatchGatherDTO);
        new Context(ultraHighFrequencyRadiation).contextInterfaceIn(proResultItemVO, eREntity, inBatchGatherDTO);
        new Context(ultravioletRadiation).contextInterfaceIn(proResultItemVO, eREntity, inBatchGatherDTO);
        new Context(windSpeed).contextInterfaceIn(proResultItemVO, eREntity, inBatchGatherDTO);

    }


}
