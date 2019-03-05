### 关于 es

es 是一个基于 Lucene 的搜索服务器。它提供一个分布式多用户能力的全文搜索引擎。

它用于全文搜索、结构化搜索、分析以及将这三者混合使用。


### 集群和节点

节点(node)是一个运行着的 Elasticsearch 实例。集群(cluster)是一组具有相同 cluster.name 的节点集合，他们协同工作，共享数据并提供故障转移和扩展功能，当然一个节点也可以组成一个集群。

### es Java API

**节点客户端（node client）**: 节点客户端以无数据节点(none data node)身份加入集群，换言之，它自己不存储任何数据，但是它知道数据在集群中的具体位置，并且能够直接转发请求到对应的节点上。

**传输客户端（Transport client）**: 这个更轻量的传输客户端能够发送请求到远程集群。它自己不加入集群，只是简单转发请求给集群中的节点。

### es 对比 db

```
Relational DB -> Databases -> Tables -> Rows -> Columns
Elasticsearch -> Indices   -> Types  -> Documents -> Fields
```

Elasticsearch集群可以包含多个索引(indices)（数据库），每一个索引可以包含多个类型(types)（表），每一个类型包含多个文档(documents)（行），然后每个文档包含多个字段(Fields)（列）。

