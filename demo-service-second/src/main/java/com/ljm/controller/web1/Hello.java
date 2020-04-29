package com.ljm.controller.web1;

import com.alibaba.fastjson.JSONObject;
import com.ljm.service.CustomerCommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 视图控制器
 */
@Api("hello视图控制器")
@RestController
@RequestMapping("/hello")
public class Hello {

    @Autowired
    private CustomerCommonService service;

    @ApiOperation("测试swagger")
    @GetMapping("/sayHello")
    public String sayHello(){
        long customerNo = service.getCustomerNoByNames("world!");
        return String.valueOf(customerNo);
    }

    @ApiOperation("测试swagger")
    @GetMapping("/getInfo")
    public String getInfo(@RequestParam String name){
        System.out.println("输入：" + name);
        return "你好啊！";
    }

    @ApiOperation("测试swagger")
    @PostMapping("/getMapInfo")
    public String getMapInfo(@RequestBody Map<String, Object> reqMap){
        System.out.println("输入：" + JSONObject.toJSONString(reqMap));
        Map<String, Object> map = new HashMap<>();
        map.put("name", "张三");
        map.put("age", 22);
        return JSONObject.toJSONString(map);
    }

}
