package com.neu.dy.authority.service;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

/**
 * @Classname ValidateCodeService
 * @Description 验证码验证服务接口
 * @Version 1.0.0
 * @Date 2023/6/23 22:11
 * @Created by maicaoboy
 */
@Service
public interface ValidateCodeService {
    public void create(String key, HttpServletResponse response);
}
