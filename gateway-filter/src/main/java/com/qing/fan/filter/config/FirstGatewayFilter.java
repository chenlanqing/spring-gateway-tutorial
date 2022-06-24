package com.qing.fan.filter.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author QingFan 2022/6/22
 * @version 1.0.0
 */
public class FirstGatewayFilter implements GatewayFilter{
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.err.println("------FirstGatewayFilterFactory");
        return chain.filter(exchange);
    }

}
