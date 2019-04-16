# Mycat

Mycat 是一个强大的数据库中间件，介入数据库与应用之间，不仅仅可以用作读写分离、以及分表分库、容灾备份，而且可以用于多
租户应用开发、云平台基础设施、让你的架构具备很强的适应性和灵活性。

Mycat的架构其实很好理解，Mycat是代理，Mycat后面就是物理数据库。和Web服务器的Nginx类似。对于使用者来说，访问的都是Mycat，不会接触到后端的数据库。

# Mycat 应用场景

- 单纯的读写分离，此时配置最为简单，支持读写分离，主从切换；
- 分表分库，对于超过 1000 万的表进行分片，最大支持 1000 亿的单表分片；
- 多租户应用，每个应用一个库，但应用程序只连接 Mycat，从而不改造程序本身，实现多租户化；
- 报表系统，借助于 Mycat 的分表能力，处理大规模报表的统计；
- 替代 Hbase，分析大数据；
- 作为海量数据实时查询的一种简单有效方案，比如 100 亿条频繁查询的记录需要在 3 秒内查询出来结果，
除了基于主键的查询，还可能存在范围查询或其他属性查询，此时 Mycat 可能是最简单有效的选择。

# 逻辑库（Schema）

通常对于实际应用而言，业务开发人员并不需要知道中间件的存在，而只需要知道数据库的概念，所以数据库中间件
可以被看做是一个或多个数据库集群构成的逻辑库。


# 逻辑表（Table）

既然有逻辑库，那么就会有逻辑表，分布式数据库中，对应用来说，读写数据的表就是逻辑表。逻辑表，可
以是数据切分后，分布在一个或多个分片库中，也可以不做数据切分，不分片，只有一个表构成。

# 分片表

分片表，是指那些原有的很大数据的表，需要切分到多个数据库的表，这样，每个分片都有一部分数据，所
有分片构成了完整的数据。

# 非分片表

一个数据库中并不是所有的表都很大，某些表是可以不用进行切分的，非分片是相对分片表来说的，就是那
些不需要进行数据切分的表。

# ER 表

关系型数据库是基于实体关系模型（Entity-Relationship Model)之上，通过其描述了真实世界中事物与关
系，Mycat 中的 ER 表即是来源于此。根据这一思路，提出了基于 E-R 关系的数据分片策略，子表的记录与所关
联的父表记录存放在同一个数据分片上，即子表依赖于父表，通过表分组（Table Group）保证数据 Join 不会跨
库操作。

# 全局表

一个真实的业务系统中，往往存在大量的类似字典表的表，这些表基本上很少变动，字典表具有以下几个特
性：
- 变动不频繁；
- 数据量总体变化不大；
- 数据规模不大，很少有超过数十万条记录。
对于这类的表，在分片的情况下，当业务表因为规模而进行分片以后，业务表与这些附属的字典表之间的关
联，就成了比较棘手的问题，所以 Mycat 中通过数据冗余来解决这类表的 join，即所有的分片都有一份数据的拷
贝，所有将字典表或者符合字典表特性的一些表定义为全局表。

数据冗余是解决跨分片数据 join 的一种很好的思路，也是数据切分规划的另外一条重要规则。

# 分片节点（dataNode）

数据切分后，一个大表被分到不同的分片数据库上面，每个表分片所在的数据库就是分片节点。

# 节点主机（dataHost）

数据切分后，每个分片节点（dataNode）不一定都会独占一台机器，同一机器上面可以有多个分片数据库，
这样一个或多个分片节点（dataNode）所在的机器就是节点主机（dataHost）,为了规避单节点主机并发数限
制，尽量将读写压力高的分片节点（dataNode）均衡的放在不同的节点主机（dataHost）。

# 分片规则（rule）

一个大表被分成若干个分片表，就需要一定的规则，这样按照某种业务规则把数据分到
某个分片的规则就是分片规则，数据切分选择合适的分片规则非常重要，将极大的避免后续数据处理的难度。

# 全局序列号（sequence）

数据切分后，原有的关系数据库中的主键约束在分布式条件下将无法使用，因此需要引入外部机制保证数据
唯一性标识，这种保证全局性的数据唯一标识的机制就是全局序列号（sequence）。

# 工程结构

```
│  version.txt  版本说明
│
├─bin 可执行脚本
│      dataMigrate.bat
│      init_zk_data.bat
│      mycat.bat
│      startup_nowrap.bat  win版启动脚本（jdk 版本必须是 1.7+）
│      wrapper-windows-x86-32.exe
│      wrapper-windows-x86-64.exe
│
├─catlet catlet为Mycat的一个扩展功能
├─conf 配置文件
│  │  auto-sharding-long.txt
│  │  auto-sharding-rang-mod.txt
│  │  autopartition-long.txt
│  │  cacheservice.properties
│  │  ehcache.xml
│  │  index_to_charset.properties
│  │  log4j2.xml
│  │  migrateTables.properties
│  │  myid.properties
│  │  partition-hash-int.txt
│  │  partition-range-mod.txt
│  │  rule.xml          定义分片规则
│  │  schema.xml        定义逻辑库，表、分片节点等内容
│  │  sequence_conf.properties
│  │  sequence_db_conf.properties
│  │  sequence_distributed_conf.properties
│  │  sequence_time_conf.properties
│  │  server.xml        定义用户以及系统相关变量，如端口等
│  │  sharding-by-enum.txt
│  │  wrapper.conf
│  │
│  ├─zkconf
│  │      auto-sharding-long.txt
│  │      auto-sharding-rang-mod.txt
│  │      autopartition-long.txt
│  │      cacheservice.properties
│  │      ehcache.xml
│  │      index_to_charset.properties
│  │      partition-hash-int.txt
│  │      partition-range-mod.txt
│  │      rule.xml
│  │      schema.xml
│  │      sequence_conf.properties
│  │      sequence_db_conf.properties
│  │      sequence_distributed_conf-mycat_fz_01.properties
│  │      sequence_distributed_conf.properties
│  │      sequence_time_conf-mycat_fz_01.properties
│  │      sequence_time_conf.properties
│  │      server-mycat_fz_01.xml
│  │      server.xml
│  │      sharding-by-enum.txt
│  │
│  └─zkdownload
│          auto-sharding-long.txt
│
├─lib       依赖
│      asm-4.0.jar
│      commons-collections-3.2.1.jar
│      commons-lang-2.6.jar
│      curator-client-2.11.0.jar
│      curator-framework-2.11.0.jar
│      curator-recipes-2.11.0.jar
│      disruptor-3.3.4.jar
│      dom4j-1.6.1.jar
│      druid-1.0.26.jar
│      ehcache-core-2.6.11.jar
│      fastjson-1.2.12.jar
│      guava-19.0.jar
│      hamcrest-core-1.3.jar
│      hamcrest-library-1.3.jar
│      jline-0.9.94.jar
│      joda-time-2.9.3.jar
│      jsr305-2.0.3.jar
│      kryo-2.10.jar
│      leveldb-0.7.jar
│      leveldb-api-0.7.jar
│      log4j-1.2-api-2.5.jar
│      log4j-1.2.17.jar
│      log4j-api-2.5.jar
│      log4j-core-2.5.jar
│      log4j-slf4j-impl-2.5.jar
│      mapdb-1.0.7.jar
│      minlog-1.2.jar
│      mongo-java-driver-2.11.4.jar
│      Mycat-server-1.6-RELEASE.jar
│      mysql-binlog-connector-java-0.4.1.jar
│      netty-3.7.0.Final.jar
│      objenesis-1.2.jar
│      reflectasm-1.03.jar
│      sequoiadb-driver-1.12.jar
│      slf4j-api-1.6.1.jar
│      univocity-parsers-2.2.1.jar
│      velocity-1.7.jar
│      wrapper-windows-x86-32.dll
│      wrapper-windows-x86-64.dll
│      wrapper.jar
│      zookeeper-3.4.6.jar
│
└─logs
        mycat.log 日志文件
```

