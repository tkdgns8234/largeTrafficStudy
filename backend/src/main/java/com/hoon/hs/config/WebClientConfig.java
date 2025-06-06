package com.hoon.hs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

@Configuration
class WebClientConfig {

    @Bean
    public WebClient elasticSearchWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:9200")
                .defaultHeaders(headers -> headers.setBasicAuth("elastic", "onion1!"))
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .build();
    }

    @Bean
    public WebClient genericWebClient()  {
        return WebClient.builder().build();
    }
}