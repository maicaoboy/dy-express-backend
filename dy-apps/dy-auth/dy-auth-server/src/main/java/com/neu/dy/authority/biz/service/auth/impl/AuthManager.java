package com.neu.dy.authority.biz.service.auth.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.neu.dy.auth.server.utils.JwtTokenServerUtils;
import com.neu.dy.auth.utils.JwtUserInfo;
import com.neu.dy.auth.utils.Token;
import com.neu.dy.authority.biz.service.auth.ResourceService;
import com.neu.dy.authority.dto.auth.LoginDTO;
import com.neu.dy.authority.dto.auth.ResourceQueryDTO;
import com.neu.dy.authority.dto.auth.UserDTO;
import com.neu.dy.authority.entity.auth.Resource;
import com.neu.dy.authority.entity.auth.User;
import com.neu.dy.base.R;
import com.neu.dy.dozer.DozerUtils;
import com.neu.dy.exception.code.ExceptionCode;
import com.neu.dy.authority.biz.service.auth.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Classname AuthManager
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/6/25 11:02
 * @Created by maicaoboy
 */

/**
 *认证管理器
 */
@Service
@Slf4j
public class AuthManager {
    @Autowired
    private JwtTokenServerUtils jwtTokenServerUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private DozerUtils dozer;
    /**
     * 账号登录
     * @param account
     * @param password
     */
    public R<LoginDTO> login(String account, String password) {
        // 登录验证
        R<User> result = checkUser(account, password);
        if (result.getIsError()) {
            return R.fail(result.getCode(), result.getMsg());
        }
        User user = result.getData();

        // 生成jwt token
        Token token = this.generateUserToken(user);

        List<Resource> resourceList =this.resourceService.
                findVisibleResource(ResourceQueryDTO.builder().
                        userId(user.getId()).build());
        List<String> permissionsList = null;
        if(resourceList != null && resourceList.size() > 0){
            permissionsList = resourceList.stream().
                    map(Resource::getCode).
                    collect(Collectors.toList());
        }
        //封装数据
        LoginDTO loginDTO = LoginDTO.builder()
                .user(this.dozer.map(user, UserDTO.class))
                .token(token)
                .permissionsList(permissionsList)
                .build();
        return R.success(loginDTO);
    }

    //生成jwt token
    private Token generateUserToken(User user) {
        JwtUserInfo userInfo = new JwtUserInfo(user.getId(),
                user.getAccount(),
                user.getName(),
                user.getOrgId(),
                user.getStationId());

        Token token = this.jwtTokenServerUtils.generateUserToken(userInfo, null);
        log.info("token={}", token.getToken());
        return token;
    }

    // 登录验证
    private R<User> checkUser(String account, String password) {
        User user = this.userService.getOne(Wrappers.<User>lambdaQuery()
                .eq(User::getAccount, account));

        // 密码加密
        String passwordMd5 = DigestUtils.md5Hex(password);

        if (user == null || !user.getPassword().equals(passwordMd5)) {
            return R.fail(ExceptionCode.JWT_USER_INVALID);
        }

        return R.success(user);
    }
}