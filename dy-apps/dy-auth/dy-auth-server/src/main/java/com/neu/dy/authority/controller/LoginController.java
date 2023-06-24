package com.neu.dy.authority.controller;

import com.neu.dy.authority.service.ValidateCodeService;
import com.neu.dy.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @Classname LoginController
 * @Description 登陆控制器
 * @Version 1.0.0
 * @Date 2023/6/23 22:08
 * @Created by maicaoboy
 */
@RestController
@RequestMapping("anno")
@Api(value = "UserAuthController", tags = "登录")
@Slf4j
public class LoginController extends BaseController {
//    @Autowired
//    private ValidateCodeService validateCodeService;
//
//    @ApiOperation(value = "验证码", notes = "验证码")
//    @GetMapping(value = "captcha", produces = "image/png")
//    public void captcha(@RequestParam(value = "key") String key, HttpServletResponse response) {
//        this.validateCodeService.create(key, response);
//    }
}
