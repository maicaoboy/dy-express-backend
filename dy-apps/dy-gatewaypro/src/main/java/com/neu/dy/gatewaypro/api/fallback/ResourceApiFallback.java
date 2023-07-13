package com.neu.dy.gatewaypro.api.fallback;

import com.neu.dy.authority.dto.auth.ResourceQueryDTO;
import com.neu.dy.authority.entity.auth.Resource;
import com.neu.dy.base.R;
import com.neu.dy.gatewaypro.api.ResourceApi;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Classname ResourceApiFallback
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/6/25 21:29
 * @Created by wangxing
 */
@Component
public class ResourceApiFallback implements ResourceApi {
    public List<String> list() {
        System.out.println("熔断降级");
        return null;
    }

    public List<Resource> visible(ResourceQueryDTO resource) {
        return null;
    }
}
