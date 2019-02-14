# Spring Cloud Ribbon

Spring Cloud Ribbon 是一个基于 Http 和 TCP 的客服端负载均衡工具，它是基于 Netflix Ribbon 实现的。它不像服务注册中心、
配置中心、API网关那样独立部署，但是它几乎存在于每个微服务的基础设施中。包括前面的提供的声明式服务调用也是基于该Ribbon实现的。
理解Ribbon对于我们使用Spring Cloud来讲非常的重要，因为负载均衡是对系统的高可用、网络压力的缓解和处理能力扩容的重要手段之一。