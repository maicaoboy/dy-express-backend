package com.neu.dy.authority.biz.service.auth.impl;

import com.neu.dy.authority.biz.service.auth.ValidateCodeService;
import com.neu.dy.common.constant.CacheKey;
import com.neu.dy.exception.BizException;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.base.Captcha;
import io.netty.util.internal.StringUtil;
import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.CacheObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Service;


/**
 * @Classname ValidateCodeServiceImpl
 * @Description 验证码服务
 * @Version 1.0.0
 * @Date 2023/6/24 17:01
 * @Created by maicaoboy
 */
@Service
public class ValidateCodeServiceImpl implements ValidateCodeService {
    @Autowired
    private CacheChannel cache;

    @Override
    public void create(String key, HttpServletResponse response) throws IOException {
        if(StringUtil.isNullOrEmpty(key)) {
            throw BizException.validFail("验证码key不能为空");
        }

        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        response.setHeader(HttpHeaders.PRAGMA, "No-cache");
        response.setHeader(HttpHeaders.CACHE_CONTROL, "No-cache");
        response.setDateHeader(HttpHeaders.EXPIRES, 0L);

        ArithmeticCaptcha captcha = new ArithmeticCaptcha(115, 42);
        captcha.setCharType(Captcha.TYPE_ONLY_NUMBER);

        cache.set(CacheKey.CAPTCHA, key, StringUtils.lowerCase(captcha.text()));
        captcha.out(response.getOutputStream());
    }

    //校验验证码
    @Override
    public boolean check(String key, String value) {
        if (StringUtils.isBlank(value)) {
            throw BizException.validFail("请输入验证码");
        }
        //根据key从缓存中获取验证码
        CacheObject cacheObject = cache.get(CacheKey.CAPTCHA, key);
        if (cacheObject.getValue() == null) {
            throw BizException.validFail("验证码已过期");
        }
        //比对验证码
        if (!StringUtils.equalsIgnoreCase(value,
                String.valueOf(cacheObject.getValue()))) {
            throw BizException.validFail("验证码不正确");
        }
        //验证通过，立即从缓存中删除验证码
        cache.evict(CacheKey.CAPTCHA, key);
        return true;
    }
}
