package com.capitolis.websocket.ay.debug.tool.client;

import com.capitolis.commons.utils.HttpClientUtils;
import com.capitolis.eq_cpi_envelope_service.api.CpiEnvelopeControllerApi;
import com.capitolis.eq_cpi_envelope_service.model.CpiEnvelopeRes;
import com.capitolis.websocket.ay.debug.tool.model.EnvName;
import com.capitolis_generated.eq_cpi_envelope_service.invoker.ApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CpiEnvelopeClient {

    @Value("${server.url.common-eq.cpi-envelope.service}")
    String baseUrl;

    private final CpiEnvelopeControllerApi cpiEnvelopeControllerApi;
    private final RestTemplate restTemplate;

    public CpiEnvelopeRes fetchCpiEnvelopeBy(UUID id, EnvName env) {
        var apiClient = setApiClientEnv(env);

        cpiEnvelopeControllerApi.setApiClient(apiClient);
        return HttpClientUtils.translateClientExceptions(() -> cpiEnvelopeControllerApi.getById(id));
    }

    public CpiEnvelopeRes fetchCpiEnvelopeBy(int id, EnvName env) {
        var apiClient = setApiClientEnv(env);

        cpiEnvelopeControllerApi.setApiClient(apiClient);
        return HttpClientUtils.translateClientExceptions(() -> cpiEnvelopeControllerApi.getByFriendlyId(id));
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
        public com.capitolis_generated.eq_cpi_envelope_service.invoker.ApiClient cpiEnvelopeApiClient(
                @Value("${server.url.common-eq.cpi-envelope.service}") String baseUrl,
                RestTemplate restTemplate) {
            String format = String.format(baseUrl, EnvName.EDGE2.getName());
            var apiClient = new ApiClient(restTemplate);
            apiClient.setBasePath(baseUrl);
            return apiClient;
        }
    }

}
