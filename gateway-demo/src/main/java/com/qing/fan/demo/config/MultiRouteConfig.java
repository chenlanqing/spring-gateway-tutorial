package com.qing.fan.demo.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author QingFan 2022/6/22
 * @version 1.0.0
 */
@Configuration
public class MultiRouteConfig {

    /**
     * 虽然这两个 route 的 path 是一样的，但是 gateway 在使用的时候匹配到第一个路由就结束了
     */
//    @Bean
//    public RouteLocator route(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route("app1", r -> r.path("/app/**").uri("http://localhost:9000/"))
//                .route("app2", r -> r.path("/app/**").uri("http://localhost:9001/"))
//                .build();
//    }
}
