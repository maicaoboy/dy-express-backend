package com.neu.dy.authority.biz.service.auth.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neu.dy.authority.biz.dao.auth.ResourceMapper;
import com.neu.dy.authority.biz.service.auth.ResourceService;
import com.neu.dy.authority.dto.auth.ResourceQueryDTO;
import com.neu.dy.authority.entity.auth.Resource;
import com.neu.dy.common.constant.CacheKey;
import lombok.extern.slf4j.Slf4j;
import net.oschina.j2cache.CacheChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Classname ResourceServiceImpl
 * @Description TODO
 * @Version 1.0.0
 * @Date 2023/6/25 11:20
 * @Created by maicaoboy
 */
@Slf4j
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {
    @Autowired
    private CacheChannel cache;

    /**
     * 查询用户的可用资源权限
     */
    @Override
    public List<Resource> findVisibleResource(ResourceQueryDTO resourceQueryDTO) {
        //查询当前用户可访问的资源
        List<Resource> visibleResource =
                baseMapper.findVisibleResource(resourceQueryDTO);
        if(visibleResource != null && visibleResource.size() > 0){
            List<String> userResource = visibleResource.
                    stream().
                    map((Resource r) -> {
                        return r.getMethod() + r.getUrl();
                    }).collect(Collectors.toList());
            //将当前用户可访问的资源载入缓存，形式为：GET/user/page
            cache.set(CacheKey.USER_RESOURCE,
                    resourceQueryDTO.getUserId().toString(),
                    userResource);
        }
        return visibleResource;
    }
}
