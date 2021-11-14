package com.ljm.service;

import com.ljm.api.CommonApi;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerCommonService {

    /*@Reference(group = "Common", version = "0.0.2")
    private CommonApi commonApi;*/

    @DubboReference
    private CommonApi commonApi;

    /**
     * 获取客户号
     * @param name
     * @return
     */
    public long getCustomerNoByNames(String name){
        return commonApi.getCustomerNoByNames(name);
    }

}
