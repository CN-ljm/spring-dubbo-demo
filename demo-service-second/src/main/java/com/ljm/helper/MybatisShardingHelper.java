package com.ljm.helper;

/**
 * @author create by jiamingl on 下午9:22
 * @title
 * @desc
 */
public class MybatisShardingHelper {

    // 存储分表序号
    private static final ThreadLocal<Integer> tableNOHelper = new ThreadLocal<>();

    /**
     * 设置线程上下文表编号
     * @param tableNO
     */
    public static void setTableNO(Integer tableNO) {
        tableNOHelper.set(tableNO);
    }

    /**
     * 获取表编号
     * @return
     */
    public static Integer getTableNO() {
        return tableNOHelper.get();
    }

    /**
     * 移除上下文表编号缓存
     */
    public static void removeTableNO() {
        tableNOHelper.remove();
    }

}
