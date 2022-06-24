package com.qing.fan.dynamic.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * @author QingFan 2022/6/24 14:19
 * @version 1.0.0
 */
@Slf4j
public class RouteConfigHandler {

    private final ObjectMapper objectMapper;
    private final RouteDefinitionWriter routeDefinitionWriter;
    private final ApplicationEventPublisher applicationEventPublisher;

    private static final List<RouteDefinition> routeList = new ArrayList<>();

    public RouteConfigHandler(ObjectMapper objectMapper, RouteDefinitionWriter routeDefinitionWriter, ApplicationEventPublisher applicationEventPublisher) {
        this.objectMapper = objectMapper;
        this.routeDefinitionWriter = routeDefinitionWriter;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    private void clear() {
        routeList.forEach(routeDefinition -> routeDefinitionWriter.delete(Mono.just(routeDefinition.getId())).subscribe());
        routeList.clear();
    }

    private void add(List<RouteDefinition> routeDefinitions) {
        routeDefinitions.forEach(routeDefinition -> {
            routeDefinitionWriter.save(Mono.just(routeDefinition)).subscribe();
            routeList.add(routeDefinition);
        });
    }

    /**
     * 发布进程内通知，更新路由
     */
    private void publish() {
        applicationEventPublisher.publishEvent(new RefreshRoutesEvent(routeDefinitionWriter));
    }

    public void refreshAll(String configStr) {
        log.info("start refreshAll : {}", configStr);
        // 无效字符串不处理
        if (!StringUtils.hasText(configStr)) {
            log.error("invalid string for route config");
            return;
        }
        // 用Jackson反序列化
        List<RouteDefinition> routeDefinitions = null;
        try {
            routeDefinitions = objectMapper.readValue(configStr, new TypeReference<List<RouteDefinition>>() {
            });
        } catch (JsonProcessingException e) {
            log.error("get route definition from nacos string error", e);
        }
        // 如果等于null，表示反序列化失败，立即返回
        if (null == routeDefinitions) {
            return;
        }
        // 清理掉当前所有路由
        clear();
        // 添加最新路由
        add(routeDefinitions);
        // 通过应用内消息的方式发布
        publish();
        log.info("finish refreshAll");
    }
}
