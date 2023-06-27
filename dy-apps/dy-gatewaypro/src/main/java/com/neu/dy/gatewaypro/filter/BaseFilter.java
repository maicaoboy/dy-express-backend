package com.neu.dy.gatewaypro.filter;

import cn.hutool.core.util.StrUtil;
import com.neu.dy.base.R;
import com.neu.dy.common.adapter.IgnoreTokenConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

public abstract class BaseFilter {
    //前缀api
    @Value("${server.servlet.context-path}")
    protected String prefix;
    //判断当前请求的uri是否需要忽略
    protected boolean isIgnoreToken(ServerWebExchange exchange){
        ServerHttpRequest request = exchange.getRequest();
        String uri = request.getURI().toString();

        uri = StrUtil.subSuf(uri,prefix.length());
        uri = StrUtil.subSuf(uri,uri.indexOf("/",1));

        boolean ignoreToken = IgnoreTokenConfig.isIgnoreToken(uri);
        return ignoreToken;
    }

    //网关抛异常，不再进行路由，而是直接返回到前端
    protected Mono<Void> errorResponse(ServerWebExchange exchange, String errMsg, int errCode, int httpStatusCode){
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.valueOf(httpStatusCode)); //设置响应状态吗

        HttpHeaders headers = response.getHeaders();
        headers.add("Content-Type","application/json;charset=utf-8"); //设置响应信息

        String responseBody = R.fail(errCode,errMsg).toString();
        return response.writeWith(Mono.just(response.bufferFactory().wrap(responseBody.getBytes(StandardCharsets.UTF_8)))); //设置响应体信息.并直接相应到前端

    }
}
