package com.capitolis.websocket.ay.debug.tool.client;

import com.capitolis.eq.eq_common_advance_service.api.AdvanceControllerApi;
import com.capitolis.eq.eq_common_advance_service.api.AdvanceV2ControllerApi;
import com.capitolis.eq.eq_common_advance_service.model.AdvanceDto;
import com.capitolis.eq.eq_common_advance_service.model.PaymentTableWithAccrualPeriodsDto;
import com.capitolis.websocket.ay.debug.tool.model.EnvName;
import com.capitolis_generated.eq.eq_common_advance_service.invoker.ApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

import static com.capitolis.commons.utils.HttpClientUtils.translateClientExceptions;

@Component
@RequiredArgsConstructor
public class AdvanceServiceClient {

    @Value("${server.url.common-eq.advance.service}")
    String baseUrl;

    private final AdvanceControllerApi advanceControllerApi;
    private final AdvanceV2ControllerApi advanceV2ControllerApi;

    private final RestTemplate restTemplate;

    public AdvanceDto getAdvance(UUID id, EnvName envName) {
        ApiClient apiClient = setApiClientEnv(envName);

        advanceControllerApi.setApiClient(apiClient);
        return translateClientExceptions(() ->
                advanceControllerApi.getAdvance(id));
    }

    public PaymentTableWithAccrualPeriodsDto getAdvancePaymentWithAccruals(UUID advanceId) {
        return translateClientExceptions(() ->
                advanceV2ControllerApi.getPaymentTableWithAccrualsDto(advanceId));
    }

    public List<AdvanceDto> getAdvanceByIds(List<UUID> UUID) {
        return translateClientExceptions(() -> 
                advanceControllerApi.getAdvanceByIds(UUID));
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
        public ApiClient advanceApiClient(
                RestTemplate restTemplate,
                @Value("${server.url.common-eq.advance.service}") String url) {
            var apiClient = new ApiClient(restTemplate);
            apiClient.setBasePath(url);
            apiClient.addDefaultHeader("x-user-id", "101");

            return apiClient;
        }
    }
}
