package com.neu.dy.authority.biz.service.auth;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Classname ValidateCodeService
 * @Description 验证码验证服务接口
 * @Version 1.0.0
 * @Date 2023/6/23 22:11
 * @Created by maicaoboy
 */
@Service
public interface ValidateCodeService {
    public void create(String key, HttpServletResponse response) throws IOException;

    /**
     * 校验验证码
     * @param key   前端上送 key
     * @param code 前端上送待校验值
     */
    boolean check(String key, String code);
}
