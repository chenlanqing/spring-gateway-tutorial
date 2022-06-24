package com.qing.fan.filter.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author QingFan 2022/6/22
 * @version 1.0.0
 */
@Component
public class FirstGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> implements Ordered {

    @Override
    public GatewayFilter apply(Object config) {
        return new FirstGatewayFilter();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
