package com.ljm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ljm.pojo.Person;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author create by jiamingl on 下午9:41
 * @title
 * @desc
 */
@Mapper
public interface PersonMapper extends BaseMapper<Person> {
}
