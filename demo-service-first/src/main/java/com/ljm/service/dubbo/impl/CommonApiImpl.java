package com.ljm.service.dubbo.impl;

import com.ljm.api.CommonApi;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Service;

@DubboService(interfaceName = "CommonApi", group = "Common", version = "0.0.1", weight = 100, timeout = 1000)
public class CommonApiImpl implements CommonApi {

    @Override
    public long getCustomerNoByNames(String name) {
        System.out.println("Name is " + name);
        return 1007L;
    }
}
