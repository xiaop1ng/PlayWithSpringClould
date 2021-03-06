# PlayWithSpringClould

🍰 体验 SpringCloud 的一个 repo

### eureka-server

run with module **eureka-server** -> com.xiaoping.EurekaServerApp

> http://localhost:8761/

### service-hi

run with module **service-hi** -> com.xiaoping.ServerHiApp

> http://localhost:8762/hi?name=Spring

Out：

`hi, Spring from port: 8762`

开启第二个 service-hi 的实例，达到集群的目的，修改 `application.yml` 的 `server.port` 为 8763，再启动一次 com.xiaoping.ServerHiApp

> http://localhost:8763/hi?name=Spring

Out：

`hi, Spring from port: 8763`

### service-ribbon

run with module **service-ribbon** -> com.xiaoping.ServerRibbonApp

> http://localhost:8764/hi?name=Spring

Out：

`hi, Spring from port: 8762` or `hi, Spring from port: 8763`

![结构](http://upload-images.jianshu.io/upload_images/2279594-9f10b702188a129d.png)


### service-feign

run with module **service-feign** -> com.xiaoping.ServerFeignApp

> http://localhost:8765/hi?name=Spring

Out：

`hi, Spring from port: 8762` or `hi, Spring from port: 8763`

### service-zuul

run with module **service-zuul** -> com.xiaoping.ServerZuulApp

> http://localhost:8769/api-a/hi?name=Spring

> http://localhost:8769/api-b/hi?name=Spring

Out：

`hi, Spring from port: 8762` or `hi, Spring from port: 8763`

### config-server

run with module **config-server** -> com.xiaoping.ConfigServerApp

> http://localhost:8770/version/dev

Out:

```
{
  "name": "version",
  "profiles": [
    "dev"
  ],
  "label": null,
  "version": "b21bb51a2609b599de933804bcc0de8a06f4d7cd",
  "state": null,
  "propertySources": [

  ]
}
```

### config-client

run with module **config-client** -> com.xiaoping.ConfigClientApp

> http://localhost:8771/version

Out:

`1.0.0`

更新配置后 POST 请求配置好的 /actuator/bus-refresh，可以读到最新的配置信息

> [POST] http://localhost:8771/actuator/bus-refresh

> http://localhost:8771/version

Out:

`1.0.1`

### server-zipkin

java -jar zipkin-server-2.12.1-exec.jar

> http://localhost:9411