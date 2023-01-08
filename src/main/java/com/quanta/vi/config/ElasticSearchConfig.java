package com.quanta.vi.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.stereotype.Component;

@Component
@Configuration
//@ConfigurationProperties(prefix = "spring.elasticsearch.rest")
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {

    private String url = "";
    private String username = "";
    private String password = "";

    @Override
    @Bean(destroyMethod = "close")
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(url)
                .withBasicAuth(username, password)
                .build();
        return RestClients.create(clientConfiguration).rest();

    }
}

