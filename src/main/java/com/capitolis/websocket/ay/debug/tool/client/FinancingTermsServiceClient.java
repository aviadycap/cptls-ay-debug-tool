package com.capitolis.websocket.ay.debug.tool.client;


import com.capitolis.eq_common_financing_terms_service.api.FinancingTermsControllerApi;
import com.capitolis.eq_common_financing_terms_service.model.CreateFinancingTermsRequest;
import com.capitolis.eq_common_financing_terms_service.model.FinancingTerms;
import com.capitolis.eq_common_financing_terms_service.model.FinancingTermsFilter;
import com.capitolis.eq_common_financing_terms_service.model.GetFinancingTermsByIdsRequestDto;
import com.capitolis.websocket.ay.debug.tool.model.EnvName;
import com.capitolis_generated.eq_common_financing_terms_service.invoker.ApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

import static com.capitolis.commons.utils.HttpClientUtils.translateClientExceptions;

@Component
@RequiredArgsConstructor
public class FinancingTermsServiceClient {

    @Value("${server.url.common-eq.financing-terms-service}")
    String baseUrl;

    private final FinancingTermsControllerApi financingTermsControllerApi;
    private final RestTemplate restTemplate;

    public FinancingTerms createFinancingTerms(CreateFinancingTermsRequest createFinancingTermsRequest, boolean persist) {
        return translateClientExceptions(() ->
                financingTermsControllerApi.createFinancingTerms(createFinancingTermsRequest, persist));
    }

    public FinancingTerms getFinancingTermsById(UUID financingTermsId, EnvName env) {
        ApiClient apiClient = setApiClientEnv(env);
        financingTermsControllerApi.setApiClient(apiClient);

        return translateClientExceptions(() ->
                financingTermsControllerApi.getFinancingTermsById(financingTermsId));
    }

    public List<FinancingTerms> getFinancingTermsByIds(List<UUID> financingTermsIds) {
        GetFinancingTermsByIdsRequestDto requestDto = new GetFinancingTermsByIdsRequestDto()
                .ids(financingTermsIds);
        return translateClientExceptions(() ->
                financingTermsControllerApi.getFinancingTermsByIds(requestDto));
    }

    public List<FinancingTerms> getFinancingTerms(FinancingTermsFilter filter) {
        return translateClientExceptions(() ->
                financingTermsControllerApi.getFinancingTerms(filter));
    }


    private ApiClient setApiClientEnv(EnvName env) {
        var apiClient = new ApiClient(restTemplate);
        apiClient.setBasePath(String.format(baseUrl, env.getName()));
        apiClient.addDefaultHeader("x-user-id", "101");
        return apiClient;
    }

    @Configuration
    public static class Conf {

        @Bean
        @Primary
        public com.capitolis_generated.eq_common_financing_terms_service.invoker.ApiClient financingTermsServiceApiClient(
                RestTemplate restTemplate,
                @Value("${self.eq-common-financing-terms-service.url}") String url) {
            var apiClient = new com.capitolis_generated.eq_common_financing_terms_service.invoker.ApiClient(restTemplate);
            apiClient.setBasePath(url);
            return apiClient;
        }
    }
}
