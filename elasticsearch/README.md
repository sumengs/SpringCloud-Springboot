#### ElasticSearch

 > 都是基于倒排索引实现的，倒排索引内容词条和内容唯一标识的关联索引表
 - lucene是java语言中最流行的全文搜索引擎
 - ES是基于lucene实现的分布式全文搜索引擎
 
 #### 什么是ElasticSearch
 
  - 定义：是一个基于Lucene实现的分布式全文搜索引擎
  - 原理：
    - 倒排索引（词条和内容）
  - 优点：
    - 性能高
    - 搜索数据量大
    - 线性扩容
  - 缺点
    - 不支持事务，存储的数据安全性偏低
  - 组成：
    - index：索引库（类似mysql的database）
    - type：索引类型（类似mysql的table）
    - document：文档（类是mysql的一行数据）
    - field：文档字段（类似mysql的列）