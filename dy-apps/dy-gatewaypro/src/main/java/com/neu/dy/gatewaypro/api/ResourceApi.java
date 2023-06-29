package com.neu.dy.gatewaypro.api;

import com.neu.dy.authority.dto.auth.ResourceQueryDTO;
import com.neu.dy.authority.entity.auth.Resource;
import com.neu.dy.base.R;
import com.neu.dy.gatewaypro.api.fallback.ResourceApiFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @Classname ResourceApi
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/6/25 21:27
 * @Created by maicaoboy
 */
@FeignClient(name = "${dy.feign.authority-server:dy-auth-server}",
        fallback = ResourceApiFallback.class)
public interface ResourceApi {
    //获取所有需要鉴权的资源
    @GetMapping("/resource/list")
    public R<List> list();

    //查询当前登录用户拥有的资源权限
    @GetMapping("/resource")
    public R<List<Resource>> visible(ResourceQueryDTO resource);
}
