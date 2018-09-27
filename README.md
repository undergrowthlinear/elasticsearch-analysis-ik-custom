# elasticsearch-analysis-ik-custom
- https://github.com/bells/elasticsearch-analysis-dynamic-synonym
- https://github.com/medcl/elasticsearch-analysis-ik
- ik-custom融合了两个开源项目,用于es自定义ik分词器加上es同义词过滤器
- 用于elasticsearch 5.2.2
# 使用示例
- 当将此插件安装在es中后,使用如下方式创建索引同时设置索引的配置信息
- postman中使用 put方法 http://localhost:9200/testindex
```
{
  "index": {
    "analysis": {
      "analyzer": {
        "by_synonym_smart": {
          "type": "custom",
          "tokenizer": "ik_smart",
          "filter": ["by_tfr","remote_synonym"],
          "char_filter": [
            "by_cfr"
          ]
        },
        "by_synonym_max_word": {
          "type": "custom",
          "tokenizer": "ik_max_word",
          "filter": ["by_tfr","remote_synonym"],
          "char_filter": [
            "by_cfr"
          ]
        }
      },
      "filter": {
        "by_tfr": {
          "type": "stop",
          "stopwords": [" "]
        },
        "remote_synonym": {
          "type" : "dynamic_synonym",
        "synonyms_path" : "http://可访问文件地址",
        "interval": 21600
        }
      },
      "char_filter": {
        "by_cfr": {
          "type": "mapping",
          "mappings": ["| => |","- => "]
        }
      }
    }
  }
}
```
- 查看索引信息 postman get http://localhost:9200/testindex/
