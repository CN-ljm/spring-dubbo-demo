package com.ljm.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author create by jiamingl on 下午9:36
 * @title
 * @desc
 */
@Data
@TableName("person")
public class Person implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String idCard;

    private String cellphone;

    private LocalDate birthDate;

    private String address;

}
