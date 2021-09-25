package com.ljm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author create by jiamingl on 下午9:49
 * @title
 * @desc
 */
@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @GetMapping("/getText")
    public String getText(@RequestParam(name = "username") String username, @RequestParam(name = "password") String password) {
        log.info("request params, username:{}, password:{}", username, password);
        return "get ok!";
    }

    @PostMapping("/postText")
    public String postText(@RequestBody String context) {
        log.info("request body context:{}", context);
        return "post ok!";
    }


}
