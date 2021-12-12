package com.ljm.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.ljm.helper.MybatisShardingHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author create by jiamingl on 下午10:39
 * @title
 * @desc
 */
@Configuration
public class MybatisPlusConfig {

    private static String dynamicTableName(String sql, String tableName) {
        // 获取参数方法
        if ("person".equals(tableName)) {
            Integer tableNO = MybatisShardingHelper.getTableNO();
            return tableName + "_" + tableNO;
        }
        return tableName;
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        DynamicTableNameInnerInterceptor dynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
        dynamicTableNameInnerInterceptor.setTableNameHandler(MybatisPlusConfig::dynamicTableName);
        interceptor.addInnerInterceptor(dynamicTableNameInnerInterceptor);
        // 3.4.3.2 作废该方式
        // dynamicTableNameInnerInterceptor.setTableNameHandlerMap(map);
        return interceptor;
    }

}
