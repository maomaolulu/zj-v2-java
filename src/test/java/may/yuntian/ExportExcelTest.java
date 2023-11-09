package may.yuntian;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ExportExcelTest {

    /**
     *	测试For循环
     */
    public void testFor(List<String> list){
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i);
        }
    }

    /**
     *	测试Foreach循环
     */
    public void testForeach(List<String> list){
        for (String o : list) {
            System.out.println(o);
        }
    }

    /**
     *	测试Stream.foreach循环
     */
    public void testStream(List<String> list){
        list.stream().forEach(System.out::println);
    }

    @Test
    void test(){

        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 100; i++) {
            list.add(i+"");
        }

        Long startTime = System.currentTimeMillis();
        testFor(list);
        Long endTime = System.currentTimeMillis();
        System.out.println("testForresult:" + (endTime - startTime));

        Long startTime1 = System.currentTimeMillis();
        testForeach(list);
        Long endTime1 = System.currentTimeMillis();
        System.out.println("testForeachresult:" + (endTime1 - startTime1));

        Long startTime2 = System.currentTimeMillis();
        testStream(list);
        Long endTime2 = System.currentTimeMillis();
        System.out.println("testStreamresult:" + (endTime2 - startTime2));


        System.out.println(Integer.parseInt("0.2",10));

    }
}
