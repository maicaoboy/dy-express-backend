package com.neu.dy.gatewaypro.filter;

import cn.hutool.core.util.StrUtil;
import com.neu.dy.auth.client.properties.AuthClientProperties;
import com.neu.dy.auth.client.utils.JwtTokenClientUtils;
import com.neu.dy.auth.utils.JwtUserInfo;
import com.neu.dy.base.R;
import com.neu.dy.common.adapter.IgnoreTokenConfig;
import com.neu.dy.context.BaseContextConstants;
import com.neu.dy.exception.BizException;
import com.neu.dy.utils.StrHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;


//认证用户令牌过滤器
@Slf4j
@Component
@Order(99)
public class TokenContextFilter extends BaseFilter implements GlobalFilter{
    @Autowired
    private AuthClientProperties authClientProperties;

    @Autowired
    private JwtTokenClientUtils jwtTokenClientUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if(isIgnoreToken(exchange)){
            return chain.filter(exchange); //直接放行
        }
        //进行jwt解析
        ServerHttpRequest request = exchange.getRequest();//获取请求
        //从请求头中获取前端提交的jwt令牌
        String userToken = request.getHeaders().getFirst(authClientProperties.getUser().getHeaderName());
        //解析jwt令牌
        JwtUserInfo userInfo = null;
        try {
            userInfo = jwtTokenClientUtils.getUserInfo(userToken);
        }catch (BizException e){
            return errorResponse(exchange,e.getMessage(),e.getCode(),200);
        }catch (Exception e){
            return errorResponse(exchange,"解析jwt令牌出错",R.FAIL_CODE,200);
        }
        ServerWebExchange modifiedExchange = null;
        //将解析出的用户信息放到header中
        if(userInfo != null){
            ServerHttpRequest modifiedRequest = request.mutate()
                    .header(BaseContextConstants.JWT_KEY_NAME,StrHelper.encode(userInfo.getName().toString()))
                    .header(BaseContextConstants.JWT_KEY_ACCOUNT,StrHelper.encode(userInfo.getAccount().toString()))
                    .header(BaseContextConstants.JWT_KEY_ORG_ID,StrHelper.encode(userInfo.getOrgId().toString()))
                    .header(BaseContextConstants.JWT_KEY_STATION_ID,StrHelper.encode(userInfo.getStationId().toString()))
                    .header(BaseContextConstants.JWT_KEY_USER_ID,StrHelper.encode(userInfo.getUserId().toString()))
                    .build();
            modifiedExchange = exchange.mutate().request(modifiedRequest).build();
        }
        return chain.filter(modifiedExchange); //放行
    }

    //单纯抽取一个添加的方法
    private void addHeader(ServerHttpRequest request, String name, Object value) {
        if (StringUtils.isEmpty(value)) {
            return;
        }
        request.getHeaders().add(name, StrHelper.encode(value.toString()));
    }

}
