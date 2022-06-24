package com.qing.fan.dynamic.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author QingFan 2022/6/24 14:20
 * @version 1.0.0
 */
@Configuration
public class RouteHandlerConfiguration {

    @Bean
    public RouteConfigHandler routeOperator(ObjectMapper objectMapper,
                                            RouteDefinitionWriter routeDefinitionWriter,
                                            ApplicationEventPublisher applicationEventPublisher) {
        return new RouteConfigHandler(objectMapper, routeDefinitionWriter, applicationEventPublisher);
    }
}
