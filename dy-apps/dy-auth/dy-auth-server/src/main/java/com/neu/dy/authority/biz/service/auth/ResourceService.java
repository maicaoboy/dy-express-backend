package com.neu.dy.authority.biz.service.auth;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neu.dy.authority.dto.auth.ResourceQueryDTO;
import com.neu.dy.authority.entity.auth.Resource;

import java.util.List;

/**
 * @Classname ResourceService
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/6/25 11:19
 * @Created by maicaoboy
 */
public interface ResourceService extends IService<Resource> {
    /**
     * 查询 用户拥有的资源权限
     */
    List<Resource> findVisibleResource(ResourceQueryDTO resource);
}
