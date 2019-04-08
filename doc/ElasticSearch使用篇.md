> GET http://192.168.199.139:9200/
```json
{
  "name": "node-1",
  "cluster_name": "elasticsearch",
  "cluster_uuid": "5KGWIR_7SYKVGdZAD7BAaw",
  "version": {
    "number": "7.0.0-rc1",
    "build_flavor": "default",
    "build_type": "zip",
    "build_hash": "486505d",
    "build_date": "2019-03-22T20:26:57.194601Z",
    "build_snapshot": false,
    "lucene_version": "8.0.0",
    "minimum_wire_compatibility_version": "6.7.0",
    "minimum_index_compatibility_version": "6.0.0-beta1"
  },
  "tagline": "You Know, for Search"
}
```


# es 服务起不来

报错信息在 `logs` 文件夹下

# 外部网络访问

配置 `config\elasticsearch.yml`
```yml
# 节点名称
node.name: node-1
# 服务绑定地址，不配置则只能 localhost 访问
network.host: 0.0.0.0
# 初始化主节点
cluster.inital_master_nodes: ["node-1"]
```

# 浏览器跨域访问
配置 `config\elasticsearch.yml`
```yml
http.cors.enabled: true
http.cors.allow-origin: "*"
```
