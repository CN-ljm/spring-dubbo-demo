package com.ljm.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ljm.mapper.PersonMapper;
import com.ljm.pojo.Person;
import org.springframework.stereotype.Service;

/**
 * @author create by jiamingl on 下午9:43
 * @title
 * @desc
 */
@Service
public class PersonService extends ServiceImpl<PersonMapper, Person> {
}
