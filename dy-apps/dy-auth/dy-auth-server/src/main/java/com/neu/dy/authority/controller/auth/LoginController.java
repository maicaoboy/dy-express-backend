package com.neu.dy.authority.controller.auth;

import com.neu.dy.authority.biz.service.auth.impl.AuthManager;
import com.neu.dy.authority.dto.auth.LoginDTO;
import com.neu.dy.authority.dto.auth.LoginParamDTO;
import com.neu.dy.authority.biz.service.auth.ValidateCodeService;
import com.neu.dy.base.BaseController;
import com.neu.dy.base.R;
import com.neu.dy.exception.BizException;
import com.neu.dy.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
    @Autowired
    private ValidateCodeService validateCodeService;

    @Autowired
    private AuthManager authManager;//认证管理器对象

    @SysLog("验证码")
    @ApiOperation(value = "验证码", notes = "验证码")
    @GetMapping(value = "captcha", produces = "image/png")
    public void captcha(@RequestParam(value = "key") String key, HttpServletResponse response) throws IOException {
        this.validateCodeService.create(key, response);
    }

    /**
     * 登录认证
     */
    @ApiOperation(value = "登录", notes = "登录")
    @PostMapping(value = "/login")
    public R<LoginDTO> login(@Validated @RequestBody LoginParamDTO login)
            throws BizException {
        log.info("account={}", login.getAccount());
        if (this.validateCodeService.check(login.getKey(), login.getCode())) {
            return this.authManager.login(login.getAccount(), login.getPassword());
        }
        return this.success(null);
    }
}
