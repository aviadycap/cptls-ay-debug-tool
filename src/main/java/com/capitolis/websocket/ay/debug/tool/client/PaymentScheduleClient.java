package com.capitolis.websocket.ay.debug.tool.client;

import com.capitolis.eq_common_payment_schedule_service.api.*;
import com.capitolis.eq_common_payment_schedule_service.model.*;
import com.capitolis.websocket.ay.debug.tool.model.EnvName;
import com.capitolis_generated.eq_common_payment_schedule_service.invoker.ApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

import static com.capitolis.commons.utils.HttpClientUtils.translateClientExceptions;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentScheduleClient {

    @Value("${server.url.common-eq.payment-schedule-service}")
    String baseUrl;

    private final PaymentTableControllerApi paymentTableControllerApi;
    private final PaymentTableV2ControllerApi paymentTableV2ControllerApi;
    private final PaymentTableV3ControllerApi paymentTableV3ControllerApi;
    private final ChangeEventControllerApi changeEventControllerApi;

    private final PaymentsControllerApi paymentsControllerApi;

    private final RestTemplate restTemplate;


    public List<PaymentDto> fetchPaymentsByPaymentTableId(UUID paymentTableId) {
        return translateClientExceptions(() -> paymentsControllerApi.findPaymentByPaymentTableId(paymentTableId));
    }

    public PaymentTable getPaymentTable(UUID paymentTableId, EnvName envName) {
        ApiClient apiClient = setApiClientEnv(envName);
        paymentTableControllerApi.setApiClient(apiClient);

        return translateClientExceptions(() -> paymentTableControllerApi.findById(paymentTableId));
    }

    public PaymentTableDto getPaymentTableDto(UUID paymentTableId) {
        return translateClientExceptions(() -> paymentTableV2ControllerApi.findByIdV2(paymentTableId));
    }

    public PaymentTableWithAccrualPeriods getPaymentTableWithAccrualPeriods(UUID paymentTableId) {
        return translateClientExceptions(() -> paymentTableControllerApi.findByIdWithAccrualPeriods(paymentTableId));
    }

    public PaymentTableWithAccrualPeriodsDto getPaymentTableWithAccrualPeriodsDto(UUID paymentTableId, EnvName envName) {
        ApiClient apiClient = setApiClientEnv(envName);
        paymentTableV2ControllerApi.setApiClient(apiClient);

        return translateClientExceptions(() -> paymentTableV2ControllerApi.findByIdWithAccrualPeriodsV2(paymentTableId));
    }


    public List<PaymentTableDto> findAllPaymentTablesByIds(List<UUID> paymentTableIds) {
        return translateClientExceptions(() -> paymentTableV3ControllerApi.findAllByIds(paymentTableIds));
    }

    public FirstCouponDatePreview previewFirstCouponDates(FirstCouponDatePreviewRequest firstCouponDatePreviewRequest) {
        return translateClientExceptions(() -> paymentTableV2ControllerApi.previewFirstCouponDate(firstCouponDatePreviewRequest));
    }


    public List<PaymentTableNotionalValueHistory> getNotionalHistory(UUID paymentTableId) {
        return translateClientExceptions(() -> paymentTableControllerApi.getNotionalHistory(paymentTableId));
    }

    public List<ChangeEventDto> getChangeEvents(UUID paymentTableId, ChangeEventCriteria changeEventCriteria) {
        return translateClientExceptions(() ->
                changeEventControllerApi.getChangeEvents(paymentTableId, changeEventCriteria));
    }

    public List<TotalInterestResponse> getTotalInterestByTableIds(List<UUID> paymentTableIds) {
        return translateClientExceptions(() -> paymentTableControllerApi.getTotalInterestByTableIds(paymentTableIds));
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
        public ApiClient paymentScheduleApiClient(
                @Value("${server.url.common-eq.payment-schedule-service}") String baseUrl,
                RestTemplate restTemplate) {

            var apiClient = new ApiClient(restTemplate);
            apiClient.setBasePath(baseUrl);
            return apiClient;
        }
    }

}
