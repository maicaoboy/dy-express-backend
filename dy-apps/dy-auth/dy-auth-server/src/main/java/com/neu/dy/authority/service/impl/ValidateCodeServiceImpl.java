package com.neu.dy.authority.service.impl;

import com.neu.dy.authority.service.ValidateCodeService;
import io.netty.util.internal.StringUtil;
import net.oschina.j2cache.CacheChannel;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;

/**
 * @Classname ValidateCodeServiceImpl
 * @Description 验证码服务
 * @Version 1.0.0
 * @Date 2023/6/24 17:01
 * @Created by maicaoboy
 */
public class ValidateCodeServiceImpl implements ValidateCodeService {
    @Autowired
    private CacheChannel cache;

    @Override
    public void create(String key, HttpServletResponse response) {
        if(StringUtil.isNullOrEmpty(key)) {

        }
    }
}
