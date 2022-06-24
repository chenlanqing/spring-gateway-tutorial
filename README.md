Spring Cloud Gateway 示例程序
--

[官方文档](https://spring.io/projects/spring-cloud-gateway)

- 基础示例程序：[gateway-demo](gateway-demo)
- 动态路由（Nacos配置路由）：[gateway-dynamic-route](gateway-dynamic-route)
- 全局异常处理：[gateway-exception](gateway-exception)
- 网关过滤器：[gateway-filter](gateway-filter)
- 负载均衡：
  - 使用Nacos作为注册中心负载均衡实现：[nacos-discovery](gateway-loadbalance/nacos-discovery)
  - 使用Eureka作为注册中心负载均衡实现：[eureka-discovery](gateway-loadbalance/eureka-discovery)
  - 不使用任何注册中心负载均衡实现：[simple-discovery](gateway-loadbalance/simple-discovery)
- 网关限流：[gateway-ratelimit](gateway-ratelimit)
- 统一返回值：[gateway-response](gateway-response)