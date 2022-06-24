package com.qing.fan.nacos.config;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Configuration;

/**
 * LoadBalancerClient 注解中的 value 为 路由策略中的 service
 *
 * @author QingFan 2022/6/24 11:16
 * @version 1.0.0
 */
@Configuration
@LoadBalancerClient(value = "gateway-app", configuration = CustomLoadBalanceConfig.class)
public class LoadBalanceConfig {

}
