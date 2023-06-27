package com.neu.dy.gatewaypro.filter;

import cn.hutool.core.util.StrUtil;
import com.neu.dy.authority.dto.auth.ResourceQueryDTO;
import com.neu.dy.authority.entity.auth.Resource;
import com.neu.dy.base.R;
import com.neu.dy.common.constant.CacheKey;
import com.neu.dy.context.BaseContextConstants;
import com.neu.dy.exception.code.ExceptionCode;
import com.neu.dy.gatewaypro.api.ResourceApi;
import lombok.extern.slf4j.Slf4j;
import net.oschina.j2cache.CacheChannel;
import net.oschina.j2cache.CacheObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@Order(1)
public class AccessFilter extends BaseFilter implements GlobalFilter {

    @Qualifier("com.neu.dy.gatewaypro.api.ResourceApi")
    private ResourceApi resourceApi;

    @Autowired
    private CacheChannel cacheChannel;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //1.判断当前请求是否需要忽略
        if(isIgnoreToken(exchange)){
            return chain.filter(exchange);
        }
        //2.获取当前请求的请求方式和uri，拼接成GET/user/page(即method+uri)形式，称为权限标识符
        ServerHttpRequest request = exchange.getRequest();
        String method = String.valueOf(request.getMethod());
        String uri = String.valueOf(exchange.getRequest().getURI());
        uri = StrUtil.subSuf(uri,prefix.length());
        uri = StrUtil.subSuf(uri,uri.indexOf("/",1));

        String permission = method + uri;

        //3.从缓存中获取所有需要鉴权的资源
        CacheObject cacheObject = cacheChannel.get(CacheKey.RESOURCE, CacheKey.RESOURCE_NEED_TO_CHECK);
        List<String> list  = (List<String>) cacheObject.getValue();
        if(list == null){
            list = resourceApi.list().getData();
            if(list != null && list.size() > 0){
                cacheChannel.set(CacheKey.RESOURCE, CacheKey.RESOURCE_NEED_TO_CHECK,list); //从数据库中查出的数据放入缓存中
            }
        }

        //4判断这些资源是否包含当前请求的标识符，如果不包含说明当前请求是个非法请求
        long count = list.stream().filter((resource) -> {
            return resource.startsWith(permission);
        }).count();
        if(count  == 0){
            //当前请求是一个未知请求,直接返回
            errorResponse(exchange, ExceptionCode.UNAUTHORIZED.getMsg(), ExceptionCode.UNAUTHORIZED.getCode(), 200);
        }

        //5.请求头中获取用户id,根据用户id取出缓存中用户拥有的权限，如果没有取到通过feign调用数据库取
        String userId = request.getHeaders().getFirst(BaseContextConstants.JWT_KEY_USER_ID);
        List<String> visibleResource = (List<String>) cacheChannel.get(CacheKey.USER_RESOURCE,userId).getValue();
        if(visibleResource == null){
            //缓存中不存在
            List<Resource> resourceList = resourceApi.visible(ResourceQueryDTO.builder().userId(new Long(userId)).build()).getData();
            if(resourceList != null && resourceList.size() > 0){
                visibleResource = resourceList.stream().map((resource -> {
                    return resource.getMethod() + resource.getUrl();
                })).collect(Collectors.toList());
            }
            //将当前用户拥有的权限再放入缓存
            cacheChannel.set(CacheKey.USER_RESOURCE, userId, visibleResource);
        }
        count = visibleResource.stream().filter((resource) -> {
            return resource.startsWith(permission);
        }).count();
        if(count > 0){
            //当前用户拥有访问权限，直接放行
            return chain.filter(exchange);
        }else{
            //没有权限
            errorResponse(exchange, ExceptionCode.UNAUTHORIZED.getMsg(), ExceptionCode.UNAUTHORIZED.getCode(), 200);
        }
        return null;
    }
}
