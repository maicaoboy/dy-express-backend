package com.neu.dy.authority.biz.service.auth.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neu.dy.authority.biz.dao.auth.UserMapper;
import com.neu.dy.authority.biz.service.auth.UserService;
import com.neu.dy.authority.entity.auth.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Classname UserServiceImpl
 * @Description 业务处理
 * @Version 1.0.0
 * @Date 2023/6/25 11:10
 * @Created by maicaoboy
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
}
