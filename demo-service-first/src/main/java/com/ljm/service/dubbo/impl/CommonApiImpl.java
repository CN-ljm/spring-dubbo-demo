package com.ljm.service.dubbo.impl;

import com.ljm.api.CommonApi;
import org.apache.dubbo.config.annotation.Service;

@Service()
public class CommonApiImpl implements CommonApi {

    @Override
    public long getCustomerNoByNames(String name) {
        System.out.println("Name is " + name);
        return 1007L;
    }
}
