package com.ljm.dto.wx;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @author create by jiamingl on 下午5:34
 * @title 微信api数据代理对象
 * @desc
 */
@Data
public class WxApiDataProxyDTO implements Serializable {

    /** 地址 **/
    private String url;
    /** 接口数据 **/
    private JSONObject data;

}
