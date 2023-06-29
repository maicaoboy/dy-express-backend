package com.neu.dy.server.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neu.dy.user.eneity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/**
 * 地址簿Mapper接口
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}