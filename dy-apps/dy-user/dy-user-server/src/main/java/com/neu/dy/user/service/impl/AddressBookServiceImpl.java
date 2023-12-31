package com.neu.dy.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neu.dy.user.mapper.AddressBookMapper;
import com.neu.dy.user.service.IAddressBookService;
import com.neu.dy.user.eneity.AddressBook;
import org.springframework.stereotype.Service;

/**
 * 地址簿服务类实现
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements IAddressBookService {

}