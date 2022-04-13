package com.dpwgc.ringlog.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

/**
 * ElasticSearch 客户端配置
 */
@Configuration
public class EsConfig extends AbstractElasticsearchConfiguration {

    //Elasticsearch连接地址
    @Value("${spring.data.elasticsearch.client.reactive.endpoints}")
    private String endpoints;

    //Elasticsearch账户
    @Value("${spring.data.elasticsearch.client.reactive.username}")
    private String username;

    //Elasticsearch密码
    @Value("${spring.data.elasticsearch.client.reactive.password}")
    private String password;

    //要使用的elasticsearch索引名称
    @Value("${elasticsearch.indexName}")
    private String indexName;
    @Bean
    public String indexName(){
        return indexName;
    }

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(endpoints).withBasicAuth(username,password)
                .build();

        return RestClients.create(clientConfiguration).rest();
    }
}
