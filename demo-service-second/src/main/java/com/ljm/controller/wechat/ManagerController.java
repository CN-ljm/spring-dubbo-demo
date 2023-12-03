package com.ljm.controller.wechat;

import com.ljm.dto.wx.WxApiDataProxyDTO;
import com.ljm.service.WxManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author create by jiamingl on 下午5:04
 * @title 微信后段控制器，对接微信API进行资公众号源管理
 * @desc
 */
@RestController
@RequestMapping("/wx/manager")
@Slf4j
public class ManagerController {

    @Autowired
    private WxManagerService managerService;

    /**
     * 获取微信AccessToken
     * @return
     */
    @GetMapping("/getAccessToken")
    public String getAccessToken() {
        return managerService.getAccessToken();
    }

    /**
     * 请求微信接口
     * @param dataProxy
     * @return
     */
    @PostMapping("/invokeWxApi")
    public String invokeWxApi(@RequestBody WxApiDataProxyDTO dataProxy) {
        return managerService.invokeWxApi(dataProxy);
    }

}
