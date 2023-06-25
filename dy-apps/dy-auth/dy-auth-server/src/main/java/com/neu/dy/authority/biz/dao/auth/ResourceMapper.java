package com.neu.dy.authority.biz.dao.auth;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neu.dy.authority.dto.auth.ResourceQueryDTO;
import com.neu.dy.authority.entity.auth.Resource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Classname ResourceMapper
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/6/25 11:21
 * @Created by maicaoboy
 */
@Repository
public interface ResourceMapper extends BaseMapper<Resource> {
    /**
     * 查询用户拥有的资源权限
     */
    List<Resource> findVisibleResource(ResourceQueryDTO resource);
}
