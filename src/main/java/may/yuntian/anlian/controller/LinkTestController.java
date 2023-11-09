package may.yuntian.anlian.controller;

import may.yuntian.anlian.entity.LinkTestEntity;
import may.yuntian.sys.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

import may.yuntian.anlian.service.LinkTestService;

import java.util.HashMap;
import java.util.Map;


/**
 * 管理
 * WEB请求处理层
 *
 * @author LiXin
 * @email ''
 * @date 2023-05-08 09:09:26
 */
@RestController
@Api(tags="连接测试")
public class LinkTestController {
    @Autowired
    private LinkTestService linkTestService;


    @GetMapping("/ping")
    public Result ping(){
        Map<String,Object> map = new HashMap<>();
        try {
            int count = linkTestService.count();
            map.put("db_status","OK");
        }catch (Exception e){
            map.put("db_status","FAILD");
        }

        return Result.data(map);
    }

}
