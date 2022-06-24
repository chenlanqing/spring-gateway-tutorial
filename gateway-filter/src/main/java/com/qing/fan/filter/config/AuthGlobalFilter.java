package com.qing.fan.filter.config;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author QingFan 2022/6/22
 * @version 1.0.0
 */
@Component
public class AuthGlobalFilter implements GlobalFilter{

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.err.println("全局过滤器~~~~~~AuthGlobalFilter");
        return chain.filter(exchange);
    }
}
