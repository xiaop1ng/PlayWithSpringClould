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

run with module **service-ribbon** -> com.xiaoping.ServiceRibbonApp

> http://localhost:8764/hi?name=Spring

Out：

`hi, Spring from port: 8762` or `hi, Spring from port: 8763`

![结构](http://upload-images.jianshu.io/upload_images/2279594-9f10b702188a129d.png)
