package com.capitolis.websocket.ay.debug.tool.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ComponentScan(basePackages = "com.capitolis")
public class ApiConfig {
//    @Bean
//    public PageableOpenAPIConverter pageableOpenAPIConverter() {
//        return new PageableOpenAPIConverter(null);
//    }




//    @Bean
//    public com.capitolis_generated.eq.eq_common_pledge_collateral_service.invoker.ApiClient pledgeApiClient(
//            RestTemplate restTemplate,
//            @Value("${server.url.common-eq.pledge-collateral}") String url) {
//        var apiClient = new com.capitolis_generated.eq.eq_common_pledge_collateral_service.invoker.ApiClient(restTemplate);
//        apiClient.setBasePath(url);
//        return apiClient;
//    }

//    @Bean
//    public com.capitolis_generated.eq.eq_common_document_generation_service.invoker.ApiClient docGenServiceApiClientBean(
//            RestTemplate restTemplate,
//            @Value("${server.url.eq.document-generation-service}") String url) {
//        var apiClient = new com.capitolis_generated.eq.eq_common_document_generation_service.invoker.ApiClient(restTemplate);
//        apiClient.setBasePath(url);
//        return apiClient;
//    }
}
