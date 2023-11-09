package may.yuntian.commission.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public class ComissionMathUntils {

    /**
     *
     * @param targetOutput  人员目标额
     * @param cumulativeOutput 人员产出额
     * @param output 单个项目的产出额/净值
     */
    public static Map<String,BigDecimal> calculation(BigDecimal targetOutput,BigDecimal cumulativeOutput,BigDecimal output,BigDecimal netvalue,String type){
        Map<String,BigDecimal> map = new HashMap<>();
        BigDecimal commissionMoney = BigDecimal.ZERO;//提成金额
        BigDecimal yearDeep = BigDecimal.ZERO;//年底提成
        Map<String,BigDecimal> proportionMap = proportion(type,targetOutput);
        //percent1 percent2 percent3 percent4  目标额比例从大到小
        //ratio1 ratio2 ratio3 ratio4 ratio5  提成比例从大到小
        BigDecimal money;
        if (output.compareTo(BigDecimal.ZERO)==0){
            money = netvalue;
        }else {
            money = output;
        }
        if ((cumulativeOutput.add(output)).compareTo(proportionMap.get("percent1"))<1){
            commissionMoney = proportionMap.get("ratio1").multiply(money).multiply(proportionMap.get("bili"));
            yearDeep = proportionMap.get("ratio1").multiply(money).multiply(new BigDecimal("0.2"));
        }else if ((cumulativeOutput.add(output)).compareTo(proportionMap.get("percent1"))==1&&(cumulativeOutput.add(output)).compareTo(proportionMap.get("percent2"))<1){
            if (cumulativeOutput.compareTo(proportionMap.get("percent1"))<1){
                BigDecimal chazhi = proportionMap.get("percent1").subtract(cumulativeOutput);//小于这一梯度的差值
                BigDecimal jinjie = output.subtract(chazhi);//大于这一梯度的差值
                commissionMoney = proportionMap.get("ratio1").multiply(chazhi).multiply(proportionMap.get("bili")).add(proportionMap.get("ratio2").multiply(jinjie).multiply(proportionMap.get("bili")));
                yearDeep = proportionMap.get("ratio1").multiply(chazhi).multiply(new BigDecimal("0.2")).add(proportionMap.get("ratio2").multiply(jinjie).multiply(new BigDecimal("0.2")));
            }else {
                commissionMoney = proportionMap.get("ratio2").multiply(money).multiply(proportionMap.get("bili"));
                yearDeep = proportionMap.get("ratio2").multiply(money).multiply(new BigDecimal("0.2"));
            }
        }else if ((cumulativeOutput.add(output)).compareTo(proportionMap.get("percent2"))==1&&(cumulativeOutput.add(output)).compareTo(proportionMap.get("percent3"))<1){
            if (cumulativeOutput.compareTo(proportionMap.get("percent2"))<1){
                BigDecimal chazhi = proportionMap.get("percent2").subtract(cumulativeOutput);//小于这一梯度的差值
                BigDecimal jinjie = output.subtract(chazhi);//大于这一梯度的差值
                commissionMoney = proportionMap.get("ratio2").multiply(chazhi).multiply(proportionMap.get("bili")).add(proportionMap.get("ratio3").multiply(jinjie).multiply(proportionMap.get("bili")));
                yearDeep = proportionMap.get("ratio2").multiply(chazhi).multiply(new BigDecimal("0.2")).add(proportionMap.get("ratio3").multiply(jinjie).multiply(new BigDecimal("0.2")));
            }else {
                commissionMoney = proportionMap.get("ratio3").multiply(money).multiply(proportionMap.get("bili"));
                yearDeep = proportionMap.get("ratio3").multiply(money).multiply(new BigDecimal("0.2"));
            }
        }else if ((cumulativeOutput.add(output)).compareTo(proportionMap.get("percent3"))==1&&(cumulativeOutput.add(output)).compareTo(proportionMap.get("percent4"))==-1){
            if (cumulativeOutput.compareTo(proportionMap.get("percent3"))<1){
                BigDecimal chazhi = proportionMap.get("percent3").subtract(cumulativeOutput);//小于这一梯度的差值
                BigDecimal jinjie = output.subtract(chazhi);//大于这一梯度的差值
                commissionMoney = proportionMap.get("ratio3").multiply(chazhi).multiply(proportionMap.get("bili")).add(proportionMap.get("ratio4").multiply(jinjie).multiply(proportionMap.get("bili")));
                yearDeep = proportionMap.get("ratio3").multiply(chazhi).multiply(new BigDecimal("0.2")).add(proportionMap.get("ratio4").multiply(jinjie).multiply(new BigDecimal("0.2")));
            }else {
                commissionMoney = proportionMap.get("ratio4").multiply(money).multiply(proportionMap.get("bili"));
                yearDeep = proportionMap.get("ratio4").multiply(money).multiply(new BigDecimal("0.2"));
            }
        }else if ((cumulativeOutput.add(output)).compareTo(proportionMap.get("percent4"))>-1){
            if (cumulativeOutput.compareTo(proportionMap.get("percent4"))==-1){
                BigDecimal chazhi = proportionMap.get("percent4").subtract(cumulativeOutput);//小于这一梯度的差值
                BigDecimal jinjie = output.subtract(chazhi);//大于这一梯度的差值
                commissionMoney = proportionMap.get("ratio4").multiply(chazhi).multiply(proportionMap.get("bili")).add(proportionMap.get("ratio5").multiply(jinjie).multiply(proportionMap.get("bili")));
                yearDeep = proportionMap.get("ratio4").multiply(chazhi).multiply(new BigDecimal("0.2")).add(proportionMap.get("ratio5").multiply(jinjie).multiply(new BigDecimal("0.2")));
            }else {
                commissionMoney = proportionMap.get("ratio5").multiply(money).multiply(proportionMap.get("bili"));
                yearDeep = proportionMap.get("ratio5").multiply(money).multiply(new BigDecimal("0.2"));
            }
        }
        map.put("commissionMoney",commissionMoney);
        map.put("yearDeep",yearDeep);
        return map;
    }


    /**
     * 不同提成类型的不同产出比
     * @param type  提成类型  检评采样/评价采样/签发/报告
     * @param targetOutput 人员目标
     */
    public static Map<String,BigDecimal> proportion(String type,BigDecimal targetOutput){
        Map<String,BigDecimal> map = new HashMap<>();
        BigDecimal percent1 = BigDecimal.ZERO;
        BigDecimal percent2 = BigDecimal.ZERO;
        BigDecimal percent3 = BigDecimal.ZERO;
        BigDecimal percent4 = BigDecimal.ZERO;
        BigDecimal ratio1 = BigDecimal.ZERO;
        BigDecimal ratio2 = BigDecimal.ZERO;
        BigDecimal ratio3 = BigDecimal.ZERO;
        BigDecimal ratio4 = BigDecimal.ZERO;
        BigDecimal ratio5 = BigDecimal.ZERO;
        BigDecimal bili = BigDecimal.ZERO;
        switch (type){
            case "检评采样":
                percent1 = targetOutput.multiply(new BigDecimal("0.30"));//产出的30%
                percent2 = targetOutput.multiply(new BigDecimal("0.60"));//产出的60%
                percent3 = targetOutput.multiply(new BigDecimal("0.80"));//产出的80%
                percent4 = targetOutput.multiply(new BigDecimal("1.00"));//产出的100%
                map.put("percent1",percent1);
                map.put("percent2",percent2);
                map.put("percent3",percent3);
                map.put("percent4",percent4);
                map.put("ratio1",new BigDecimal("0.03"));
                map.put("ratio2",new BigDecimal("0.04"));
                map.put("ratio3",new BigDecimal("0.05"));
                map.put("ratio4",new BigDecimal("0.07"));
                map.put("ratio5",new BigDecimal("0.08"));
                map.put("bili",new BigDecimal("0.80"));
                break;
            case "评价采样":
                percent1 = targetOutput.multiply(new BigDecimal("0.30"));//产出的30%
                percent2 = targetOutput.multiply(new BigDecimal("0.50"));//产出的50%
                percent3 = targetOutput.multiply(new BigDecimal("0.80"));//产出的80%
                percent4 = targetOutput.multiply(new BigDecimal("1.00"));//产出的100%
                map.put("percent1",percent1);
                map.put("percent2",percent2);
                map.put("percent3",percent3);
                map.put("percent4",percent4);
                map.put("ratio1",new BigDecimal("0.015"));
                map.put("ratio2",new BigDecimal("0.03"));
                map.put("ratio3",new BigDecimal("0.045"));
                map.put("ratio4",new BigDecimal("0.06"));
                map.put("ratio5",new BigDecimal("0.08"));
                map.put("bili",new BigDecimal("0.80"));
                break;
            case "签发":
                percent1 = targetOutput.multiply(new BigDecimal("0.30"));//产出的30%
                percent2 = targetOutput.multiply(new BigDecimal("0.60"));//产出的60%
                percent3 = targetOutput.multiply(new BigDecimal("0.80"));//产出的80%
                percent4 = targetOutput.multiply(new BigDecimal("1.00"));//产出的100%
                map.put("percent1",percent1);
                map.put("percent2",percent2);
                map.put("percent3",percent3);
                map.put("percent4",percent4);
                map.put("ratio1",new BigDecimal("0.03"));
                map.put("ratio2",new BigDecimal("0.06"));
                map.put("ratio3",new BigDecimal("0.08"));
                map.put("ratio4",new BigDecimal("0.10"));
                map.put("ratio5",new BigDecimal("0.14"));
                map.put("bili",new BigDecimal("0.50"));
                break;
            case "报告":
                percent1 = targetOutput.multiply(new BigDecimal("0.30"));//产出的30%
                percent2 = targetOutput.multiply(new BigDecimal("0.60"));//产出的60%
                percent3 = targetOutput.multiply(new BigDecimal("0.80"));//产出的80%
                percent4 = targetOutput.multiply(new BigDecimal("1.00"));//产出的100%
                map.put("percent1",percent1);
                map.put("percent2",percent2);
                map.put("percent3",percent3);
                map.put("percent4",percent4);
                map.put("ratio1",new BigDecimal("0.03"));
                map.put("ratio2",new BigDecimal("0.06"));
                map.put("ratio3",new BigDecimal("0.08"));
                map.put("ratio4",new BigDecimal("0.10"));
                map.put("ratio5",new BigDecimal("0.14"));
                map.put("bili",new BigDecimal("0.30"));
                break;
            default:
                map.put("percent1",percent1);
                map.put("percent2",percent2);
                map.put("percent3",percent3);
                map.put("percent4",percent4);
                map.put("ratio1",ratio1);
                map.put("ratio2",ratio2);
                map.put("ratio3",ratio3);
                map.put("ratio4",ratio4);
                map.put("ratio5",ratio5);
                map.put("bili",bili);
                break;
        }
        return map;
    }
}
