package com.qing.fan.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 基本使用
 *
 * @author QingFan 2022/6/22
 * @version 1.0.0
 */
@Configuration
public class BaseRouteConfig {

    @Bean
    public RouteLocator route(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("app2", r -> r.path("/app/**").uri("http://localhost:9001/"))
                .build();
    }
}
