package com.neu.dy.user.feign;
import com.neu.dy.base.R;
import com.neu.dy.user.feign.fallback.UserResolveApiFallback;
import com.neu.dy.user.model.SysUser;

import com.neu.dy.base.R;
import com.neu.dy.user.feign.fallback.UserResolveApiFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
/**
 * 用户操作API
 */
@FeignClient(name = "${dy.feign.authority-server:dy-auth-server}", fallbackFactory = UserResolveApiFallback.class)
public interface UserResolveApi {
    /**
     * 根据id 查询用户详情
     */
    @PostMapping(value = "/user/anno/id/{id}")
    R<SysUser> getById(@PathVariable("id") Long id, @RequestBody UserQuery userQuery);
}