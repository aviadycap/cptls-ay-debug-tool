package com.capitolis.websocket.ay.debug.tool.client;

import com.capitolis.eq.eq_common_notes_service.api.ProgramControllerApi;
import com.capitolis.eq.eq_common_notes_service.api.SeriesControllerApi;
import com.capitolis.eq.eq_common_notes_service.model.ProgramDto;
import com.capitolis.eq.eq_common_notes_service.model.SeriesCriteria;
import com.capitolis.eq.eq_common_notes_service.model.SeriesResponseDto;
import com.capitolis.websocket.ay.debug.tool.model.EnvName;
import com.capitolis_generated.eq.eq_common_notes_service.invoker.ApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.capitolis.commons.utils.HttpClientUtils.translateClientExceptions;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Component
@RequiredArgsConstructor
public class SeriesServiceClient {

    @Value("${server.url.common-eq.notes.service}")
    String baseUrl;

    private final SeriesControllerApi seriesControllerApi;
    private final ProgramControllerApi programControllerApi;
    private final RestTemplate restTemplate;


    public SeriesResponseDto getById(UUID seriesId, EnvName env) {
        ApiClient apiClient = setApiClientEnv(env);
        seriesControllerApi.setApiClient(apiClient);

        return translateClientExceptions(()-> seriesControllerApi.seriesGetById(seriesId));
    }

    public SeriesResponseDto getByIssuerIdAndName(Long issuerId, String name, boolean testSeries) throws RestClientException {
        try {
            return translateClientExceptions(() -> seriesControllerApi.getByIssuerIdAndName(issuerId, name, testSeries));
        } catch (ResponseStatusException responseStatusException) {
            if (Objects.equals(responseStatusException.getStatusCode(), NOT_FOUND)) {
                return null;
            }
            throw responseStatusException;
        }
    }

    public List<ProgramDto> getPrograms(ProgramDto dto) throws RestClientException {
        return translateClientExceptions(() -> programControllerApi.getByParams(dto));
    }

    public List<SeriesResponseDto> getSeriesByCriteria(SeriesCriteria seriesCriteria) {
        return translateClientExceptions(() -> seriesControllerApi.getAllByCriteria(seriesCriteria));
    }

    private ApiClient setApiClientEnv(EnvName env) {
        var apiClient = new ApiClient(restTemplate);
        apiClient.setBasePath(String.format(baseUrl, env.getName()));
        apiClient.addDefaultHeader("x-user-id", "101");
        return apiClient;
    }

    public SeriesResponseDto getSeriesByIssuerIdAndRiskOriginators(Long issuerId, List<Long> riskOriginatorsIds, Boolean testSeries) {
        try {
            return translateClientExceptions(() -> seriesControllerApi.getByIssuerIdAndRiskOriginators(issuerId, riskOriginatorsIds, testSeries));
        } catch (ResponseStatusException responseStatusException) {
            if (Objects.equals(responseStatusException.getStatusCode(), NOT_FOUND)) {
                return null;
            }
            throw responseStatusException;
        }
    }

    @Configuration
    public static class Conf {

        @Bean
        public com.capitolis_generated.eq.eq_common_notes_service.invoker.ApiClient noteServiceClientBean(
                RestTemplate restTemplate,
                @Value("${server.url.common-eq.notes.service}") String url) {
            com.capitolis_generated.eq.eq_common_notes_service.invoker.ApiClient apiClient = new com.capitolis_generated.eq.eq_common_notes_service.invoker.ApiClient(restTemplate);
            apiClient.setBasePath(url);
            return apiClient;
        }
    }

}
