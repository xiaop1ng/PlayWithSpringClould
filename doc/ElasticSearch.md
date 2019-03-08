## 关于 es

es 是一个基于 Lucene 的搜索服务器。它提供一个分布式多用户能力的全文搜索引擎。

它用于全文搜索、结构化搜索、分析以及将这三者混合使用。


## 集群和节点

节点(node)是一个运行着的 Elasticsearch 实例。集群(cluster)是一组具有相同 cluster.name 的节点集合，他们协同工作，共享数据并提供故障转移和扩展功能，当然一个节点也可以组成一个集群。

## es Java API

**节点客户端（node client）**: 节点客户端以无数据节点(none data node)身份加入集群，换言之，它自己不存储任何数据，但是它知道数据在集群中的具体位置，并且能够直接转发请求到对应的节点上。

**传输客户端（Transport client）**: 这个更轻量的传输客户端能够发送请求到远程集群。它自己不加入集群，只是简单转发请求给集群中的节点。

## es 对比 db

```
Relational DB -> Databases -> Tables -> Rows -> Columns
Elasticsearch -> Indices   -> Types  -> Documents -> Fields
```

Elasticsearch 集群可以包含多个索引(indices)（数据库），每一个索引可以包含多个类型(types)（表），每一个类型包含多个文档(documents)（行），然后每个文档包含多个字段(Fields)（列）。

## RESTful

为了创建员工目录，我们将进行如下操作：
- 为每个员工的文档(document)建立索引，每个文档包含了相应员工的所有信息。
- 每个文档的类型为employee。
- employee类型归属于索引megacorp。
- megacorp索引存储在Elasticsearch集群中

```
PUT http://localhost:9200/megacorp/employee/1

req:
{
    "first_name" : "John",
    "last_name" :  "Smith",
    "age" :        25,
    "about" :      "I love to go rock climbing",
    "interests": [ "sports", "music" ]
}

res:
{
    "_index": "megacorp",
    "_type": "employee",
    "_id": "1",
    "_version": 1,
    "result": "created",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    },
    "_seq_no": 0,
    "_primary_term": 1
}
```


**检索文档**
```
GET http://localhost:9200/megacorp/employee/1

res:
{
    "_index": "megacorp",
    "_type": "employee",
    "_id": "1",
    "_version": 1,
    "_seq_no": 0,
    "_primary_term": 1,
    "found": true,
    "_source": {
        "first_name": "John",
        "last_name": "Smith",
        "age": 25,
        "about": "I love to go rock climbing",
        "interests": [
            "sports",
            "music"
        ]
    }
}
```

**删除文档**
```
DELETE http://localhost:9200/megacorp/employee/2

res:
{
    "_index": "megacorp",
    "_type": "employee",
    "_id": "2",
    "_version": 2,
    "result": "deleted",
    "_shards": {
        "total": 2,
        "successful": 1,
        "failed": 0
    },
    "_seq_no": 1,
    "_primary_term": 1
}
```

**简单搜索**

使用megacorp索引和employee类型，但是我们在结尾使用关键字_search来取代原来的文档ID。响应内容的hits数组中包含了我们所有的三个文档。默认情况下搜索会返回前10个结果。

```
GET http://localhost:9200/megacorp/employee/_search

res:
{
    "took": 15,
    "timed_out": false,
    "_shards": {
        "total": 5,
        "successful": 5,
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        "total": 2,
        "max_score": 1,
        "hits": [
            {
                "_index": "megacorp",
                "_type": "employee",
                "_id": "1",
                "_score": 1,
                "_source": {
                    "first_name": "John",
                    "last_name": "Smith",
                    "age": 25,
                    "about": "I love to go rock climbing",
                    "interests": [
                        "sports",
                        "music"
                    ]
                }
            },
            {
                "_index": "megacorp",
                "_type": "employee",
                "_id": "3",
                "_score": 1,
                "_source": {
                    "first_name": "Douglas",
                    "last_name": "Fir",
                    "age": 35,
                    "about": "I like to build cabinets",
                    "interests": [
                        "forestry"
                    ]
                }
            }
        ]
    }
}
```

简单带条件搜索

> http://localhost:9200/megacorp/employee/_search?q=last_name:Smith

短语搜索

```
GET http://localhost:9200/megacorp/employee/_search

req:
{
    "query" : {
        "match_phrase" : {
            "about" : "rock climbing"
        }
    }
}

res:
{
    "took": 6,
    "timed_out": false,
    "_shards": {
        "total": 5,
        "successful": 5,
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        "total": 1,
        "max_score": 0.5753642,
        "hits": [
            {
                "_index": "megacorp",
                "_type": "employee",
                "_id": "1",
                "_score": 0.5753642,
                "_source": {
                    "first_name": "John",
                    "last_name": "Smith",
                    "age": 25,
                    "about": "I love to go rock climbing",
                    "interests": [
                        "sports",
                        "music"
                    ]
                }
            }
        ]
    }
}
```

高亮搜索
```
GET http://localhost:9200/megacorp/employee/_search

req:
{
    "query" : {
        "match_phrase" : {
            "about" : "rock climbing"
        }
    },
    "highlight": {
        "fields" : {
            "about" : {}
        }
    }
}

res:
{
    "took": 166,
    "timed_out": false,
    "_shards": {
        "total": 5,
        "successful": 5,
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        "total": 1,
        "max_score": 0.5753642,
        "hits": [
            {
                "_index": "megacorp",
                "_type": "employee",
                "_id": "1",
                "_score": 0.5753642,
                "_source": {
                    "first_name": "John",
                    "last_name": "Smith",
                    "age": 25,
                    "about": "I love to go rock climbing",
                    "interests": [
                        "sports",
                        "music"
                    ]
                },
                "highlight": {
                    "about": [
                        "I love to go <em>rock</em> <em>climbing</em>"
                    ]
                }
            }
        ]
    }
}
```

## 聚合、推荐、定位、渗透、模糊以及部分匹配等

## 分布式特性
Elasticsearch致力于隐藏分布式系统的复杂性。以下这些操作都是在底层自动完成的：

- 将你的文档分区到不同的容器或者分片(shards)中，它们可以存在于一个或多个节点中。
- 将分片均匀的分配到各个节点，对索引和搜索做负载均衡。
- 冗余每一个分片，防止硬件故障造成的数据丢失。
- 将集群中任意一个节点上的请求路由到相应数据所在的节点。
- 无论是增加节点，还是移除节点，分片都可以做到无缝的扩展和迁移。

## 分布式集群

一个节点(node)就是一个Elasticsearch实例，而一个集群(cluster)由一个或多个节点组成，它们具有相同的cluster.name，它们协同工作，分享数据和负载。当加入新的节点或者删除一个节点时，集群就会感知到并平衡数据。

**集群健康**：
```
GET http://localhost:9200/_cluster/health

res:
{
    "cluster_name": "elasticsearch",
    "status": "yellow",
    "timed_out": false,
    "number_of_nodes": 1,
    "number_of_data_nodes": 1,
    "active_primary_shards": 6,
    "active_shards": 6,
    "relocating_shards": 0,
    "initializing_shards": 0,
    "unassigned_shards": 5,
    "delayed_unassigned_shards": 0,
    "number_of_pending_tasks": 0,
    "number_of_in_flight_fetch": 0,
    "task_max_waiting_in_queue_millis": 0,
    "active_shards_percent_as_number": 54.54545454545454
}
```

这里的 `status`:
- green 表意所有主要分片和复制分片都可用
- yellow 所有主要分片可用，但不是所有复制分片都可用
- red 不是所有的主要分片都可用

## 文档

**文档元数据**
```
GET http://localhost:9200/megacorp/employee/1

res:
{
    "_index": "megacorp",   <文档存储的地方，索引（index）类似于关系型数据库里的“数据库”>
    "_type": "employee",    <文档代表对象的类，类型（type）类似数据库里的“表”>
    "_id": "1",             <文档的唯一标识>
    "_version": 1,
    "_seq_no": 0,
    "_primary_term": 1,
    "found": true,
    "_source": {
        "first_name": "John",
        "last_name": "Smith",
        "age": 25,
        "about": "I love to go rock climbing",
        "interests": [
            "sports",
            "music"
        ]
    }
}
```

提供自己的 _id
> PUT /{index}/{type}/{id}

由 es 生成 _id，这时由 es 提供一个 uuid
> POST /{index}/{type}

获取想要得部分数据
> GET /{index}/{type}/{id}/_source

判断是否存在数据，`HEAD`请求不会返回响应体，只有`HTTP`头
> HEAD /{index}/{type}/{id}

更新全文文档，响应中 _version 会增加，在内部，Elasticsearch已经标记旧文档为删除并添加了一个完整的新文档。旧版本文档不会立即消失，但你也不能去访问它。Elasticsearch会在你继续索引更多数据时清理被删除的文档。
> PUT /{index}/{type}/{id}

删除文档
> DELETE /{index}/{type}/{id}

局部更新文档
> POST /{index/{type}/{id}/_update

```
POST /website/blog/1/_update

req:
{
   "doc" : {
      "tags" : [ "testing" ], <doc 字段里的即是需要局部更新的部分>
      "views": 0
   }
}
```

## 搜索

```
GET http://localhost:9200/_search

res:
{
    "took": 27,         <搜索耗费毫秒数>
    "timed_out": false, <查询超时与否>
    "_shards": {
        "total": 6,     <参与查询的分片数>
        "successful": 6,<查询成功的分片数>
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        "total": 7,     <匹配到的文档总数>
        "max_score": 1, <最高的相关性得分>
        "hits": [
            {
                "_index": "megacorp",
                "_type": "employee",
                "_id": "3",
                "_score": 1,    <相关性得分(relevance score)>
                "_source": {
                    "first_name": "Douglas",
                    "last_name": "Fir",
                    "age": 35,
                    "about": "I like to build cabinets",
                    "interests": [
                        "forestry"
                    ]
                }
            },
            ... <6 object more here.>
        ]
    }
}
```

**指定索引、类型的查询**

> GET /{index}/_search

> GET /{index}/{type}/_search

> GET /{index1},{index2}/{type1},{type2}/_search

**分页查询**

> GET /{index}/_search?size=7&from=7

`size` : 结果数，默认10

`from` : 跳过开始的结果数，默认0

## 映射

查看文档结构（schema definition）

> GET /{index}/_mapping/{type}

```
GET http://localhost:9200/megacorp/_mapping/employee

res:
{
    "megacorp": {
        "mappings": {
            "employee": {
                "properties": {
                    "about": {
                        "type": "text",
                        "fields": {
                            "keyword": {
                                "type": "keyword",
                                "ignore_above": 256
                            }
                        }
                    },
                    "age": {
                        "type": "long"
                    },
                    "first_name": {
                        "type": "text",
                        "fields": {
                            "keyword": {
                                "type": "keyword",
                                "ignore_above": 256
                            }
                        }
                    },
                    "interests": {
                        "type": "text",
                        "fields": {
                            "keyword": {
                                "type": "keyword",
                                "ignore_above": 256
                            }
                        }
                    },
                    "last_name": {
                        "type": "text",
                        "fields": {
                            "keyword": {
                                "type": "keyword",
                                "ignore_above": 256
                            }
                        }
                    },
                    "sex": {
                        "type": "text",
                        "fields": {
                            "keyword": {
                                "type": "keyword",
                                "ignore_above": 256
                            }
                        }
                    }
                }
            }
        }
    }
}
```

## 倒排索引

ElasticSearch 使用一种叫做倒序索引（inverted index）的结构来做快速的全文索引。倒序索引由在文档中出现的唯一的单词列表，以及对于每个单词在文档中的位置组成。

为了创建倒排索引，我们首先切分文档的字段为单独的单词（词（terms）或者表征（tokens）），这个标记化和标准化的过程叫做分词（analysis）。
然后这个工作是分析器（analyzer）完成的。一个分析器（analyzer）是一个包装了三个功能的工具：

1. 字符过滤器（character filter）
2. 分词器（tokenizer）
3. 标记过滤（token filters）

**创建包含 mapping 的索引**
```
PUT http://localhost:9200/gb

req:
{
  "mappings": {
    "tweet" : {
      "properties" : {
        "tweet" : {
          "type" :    "text",
          "analyzer": "english"
        },
        "date" : {
          "type" :   "date"
        },
        "name" : {
          "type" :   "text"
        },
        "user_id" : {
          "type" :   "long"
        }
      }
    }
  }
}

res:
{
    "acknowledged": true,
    "shards_acknowledged": true,
    "index": "gb"
}
```

## Query DSL(Query Domain Specific Language) & Filter DSL(Filter Domain Specific Language)


事实上我们可以使用两种结构化语句： 结构化查询（Query DSL）和结构化过滤（Filter DSL）。 查询与过滤语句非常相似，但是它们由于使用目的不同而稍有差异。

一条过滤语句会询问每个文档的字段值是否包含着特定值：
- created 的日期范围是否在 2013 到 2014 ?
- status 字段中是否包含单词 "published" ?
- lat_lon 字段中的地理位置与目标点相距是否不超过10km ?

一条查询语句与过滤语句相似，但问法不同：

查询语句会询问每个文档的字段值与特定值的匹配程度如何？

查询语句的典型用法是为了找到文档：
- 查找与 full text search 这个词语最佳匹配的文档
- 查找包含单词 run ，但是也包含runs, running, jog 或 sprint的文档
- 同时包含着 quick, brown 和 fox --- 单词间离得越近，该文档的相关性越高
- 标识着 lucene, search 或 java --- 标识词越多，该文档的相关性越高

原则上来说，使用查询语句做全文本搜索或其他需要进行相关性评分的时候，剩下的全部用过滤语句

**过滤**


**查询**
