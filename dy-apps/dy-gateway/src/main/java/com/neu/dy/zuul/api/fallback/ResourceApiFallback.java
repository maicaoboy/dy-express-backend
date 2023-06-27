package com.neu.dy.zuul.api.fallback;

import com.neu.dy.authority.dto.auth.ResourceQueryDTO;
import com.neu.dy.authority.entity.auth.Resource;
import com.neu.dy.base.R;
import com.neu.dy.zuul.api.ResourceApi;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Classname ResourceApiFallback
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/6/25 21:29
 * @Created by maicaoboy
 */
@Component
public class ResourceApiFallback implements ResourceApi {
    public R<List> list() {
        return null;
    }

    public R<List<Resource>> visible(ResourceQueryDTO resource) {
        return null;
    }
}