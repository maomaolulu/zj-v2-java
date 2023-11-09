package may.yuntian;

import lombok.val;
import may.yuntian.anlian.utils.Number2Money;
import may.yuntian.jianping.dto.CodeSortDto;
import may.yuntian.jianping.entity.AlDeliverReceived;
import may.yuntian.jianping.service.AlDeliverReceivedService;
import may.yuntian.jianping.vo.PostPfnVo;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SpringBootTest
public class MongoTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AlDeliverReceivedService alDeliverReceivedService;

//    @Test
//    void find() {
//        Query query = new Query((Criteria.where("_id").is(1)));
//        PostPfnVo postPfnVo = mongoTemplate.findOne(query, PostPfnVo.class);
//        System.out.println("postPfnVo = " + postPfnVo);
//
//        List<AlDeliverReceived> list = alDeliverReceivedService.alDeliverReceived("33230");
//        System.out.println("list = " + list);
//        list.forEach(System.out::println);
//    }

    @Test
    void testTwoList(){
// 新个体点位信息
//        List<CodeSortDto> codeSort = new ArrayList<>();
//        CodeSortDto codeSortDto = new CodeSortDto();
//        codeSortDto.setPointId("1");
//        codeSortDto.setPoint("2");
//        CodeSortDto codeSortDto2 = new CodeSortDto();
//        codeSortDto2.setPointId("2");
//        codeSortDto2.setPoint("1");
//        codeSort.add(codeSortDto);
//        codeSort.add(codeSortDto2);
//        List<CodeSortDto> codeSort1 = new ArrayList<>();
//        CodeSortDto codeSortDto1 = new CodeSortDto();
//        codeSortDto1.setPointId("2");
//        codeSortDto1.setPoint("1");
//        CodeSortDto codeSortDto3 = new CodeSortDto();
//        codeSortDto3.setPointId("1");
//        codeSortDto3.setPoint("2");
//        codeSort1.add(codeSortDto1);
//        codeSort1.add(codeSortDto3);
//        boolean equalCollection = CollectionUtils.isEqualCollection(codeSort, codeSort1);
//        System.out.println("equalCollection = " + equalCollection);

        BigDecimal number = new BigDecimal("30120.11");

        // 创建一个NumberFormat对象，并设置为大写模式
        NumberFormat format = NumberFormat.getInstance(Locale.CHINA);

        // 设置最大小数位数为两位（即显示小数点后两位）
        format.setMaximumFractionDigits(2);

        // 将数值转换为大写字符串
        String result = format.format(number);

        System.out.println("转换结果: " + result);
        
        
        
        String a = "30100.01";
        String format1 = Number2Money.format(a);
        System.out.println("format1 = " + format1);

    }
}
