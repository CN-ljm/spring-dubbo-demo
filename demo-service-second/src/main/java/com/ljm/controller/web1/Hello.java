package com.ljm.controller.web1;

import com.alibaba.fastjson.JSONObject;
import com.ljm.pojo.Person;
import com.ljm.service.CustomerCommonService;
import com.ljm.service.MsgProducer;
import com.ljm.service.PersonService;
import com.ljm.service.RabbitMQMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @Autowired
    private MsgProducer producer;

    @Autowired
    private RabbitMQMessageService messageService;

    @Autowired
    private PersonService personService;

    @ApiOperation("测试swagger")
    @GetMapping("/sayHello")
    public String sayHello(String content){
        long customerNo = service.getCustomerNoByNames(content);
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

    @ApiOperation("测试数据库插入")
    @PostMapping("/testDBInsert")
    public String testDBInsert() {
        Person person = new Person();
        person.setName("李四");
        person.setIdCard("450481199102131567");
        person.setCellphone("18820211211");
        person.setBirthDate(LocalDate.now());
        person.setAddress("深圳市宝安区西乡社区");
        personService.save(person);
        return "success";
    }

    /*@ApiOperation("发送消息")
    @GetMapping("/sendMsg")
    public void sendMsg() throws UnsupportedEncodingException {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS"));
        producer.sendMsg(time);
    }

    @ApiOperation("死信队列消息")
    @GetMapping("/consumerDL")
    public void consumerDL() throws IOException {
        messageService.getDealLetterQueue();
    }

    @ApiOperation("死信队列消息")
    @GetMapping("/reQueue")
    public void reQueue() throws IOException {
        messageService.reQueue();
    }*/

}
