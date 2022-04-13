package com.dpwgc.ringlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@EnableElasticsearchRepositories(basePackages = "com.dpwgc.ringlog.dao")
@SpringBootApplication
@ServletComponentScan
public class RinglogApplication {

    /**
     * 让@Document(indexName = "xxx")能读取到配置文件中的字段
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {

        PropertySourcesPlaceholderConfigurer c = new PropertySourcesPlaceholderConfigurer();
        c.setIgnoreUnresolvablePlaceholders(true);
        return c;
    }

    public static void main(String[] args) {
        SpringApplication.run(RinglogApplication.class, args);
    }

}
