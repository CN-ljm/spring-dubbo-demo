package com.ljm.service.impl.dubbo;

import com.ljm.api.CommonApi;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(interfaceName = "com.ljm.api.CommonApi", group = "Common", version = "0.0.2", weight = 100, timeout = 1000)
public class CommonApiImpl implements CommonApi {
    @Override
    public long getCustomerNoByNames(String name) {
        return 2222L;
    }
}
