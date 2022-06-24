package com.qing.fan.dynamic.config;

import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.AbstractListener;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author QingFan 2022/6/24 14:01
 * @version 1.0.0
 */
@Component
public class RouteConfigListener {

    @Value("${dynamic.config.dataId}")
    private String dataId;

    @Resource
    private NacosConfigProperties nacosConfigProperties;

    @Resource
    private RouteConfigHandler routeConfigHandler;

    @PostConstruct
    public void dynamicRouteByNacosListener() throws NacosException {
        String group = nacosConfigProperties.getGroup();
        String serverAddr = nacosConfigProperties.getServerAddr();
        ConfigService configService = NacosFactory.createConfigService(serverAddr);
        // 添加监听，nacos上的配置变更后会执行
        configService.addListener(dataId, group, new AbstractListener() {
            @Override
            public void receiveConfigInfo(String configInfo) {
                routeConfigHandler.refreshAll(configInfo);
            }
        });
        // 获取当前的配置
        String initConfig = configService.getConfig(dataId, group, 5000);
        // 立即更新
        routeConfigHandler.refreshAll(initConfig);
    }
}
