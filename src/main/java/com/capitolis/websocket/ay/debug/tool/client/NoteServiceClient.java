package com.capitolis.websocket.ay.debug.tool.client;

import com.capitolis.eq.eq_common_notes_service.api.NoteControllerApi;
import com.capitolis.eq.eq_common_notes_service.model.*;
import com.capitolis.websocket.ay.debug.tool.model.EnvName;
import com.capitolis_generated.eq.eq_common_notes_service.invoker.ApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

import static com.capitolis.commons.utils.HttpClientUtils.translateClientExceptions;

@Component
@RequiredArgsConstructor
public class NoteServiceClient {

    @Value("${server.url.common-eq.notes.service}")
    String baseUrl;

    private final NoteControllerApi noteControllerApi;

    private final RestTemplate restTemplate;


    public NoteDto getById(UUID id, EnvName env) {
        ApiClient apiClient = setApiClientEnv(env);
        noteControllerApi.setApiClient(apiClient);

        return translateClientExceptions(() -> noteControllerApi.getNoteById(id));
    }

    public List<NoteDto> getNotesBySeriesId(UUID seriesId) {
        return translateClientExceptions(() -> noteControllerApi.getNotesBySeriesId(seriesId));
    }

    public List<ChangeEventDto> getChangeEventsByType(UUID noteId, ChangeEventType changeEventType) {
        return translateClientExceptions(() -> noteControllerApi.getChangeEvents(noteId, changeEventType));
    }

    public BookingReferenceDto getBookingReferenceId(UUID noteId) {
        return translateClientExceptions(() -> noteControllerApi.getBookingReferenceId(noteId));
    }

    public List<PlacementAgentAllocationDto> getAllocations(UUID noteId) {
        return translateClientExceptions(() -> noteControllerApi.getAllocations(noteId));
    }

    public List<EndInvestorDto> getEndInvestors(UUID allocationId) {
        return translateClientExceptions(() -> noteControllerApi.getEndInvestors(allocationId));
    }


    private ApiClient setApiClientEnv(EnvName env) {
        var apiClient = new ApiClient(restTemplate);
        apiClient.setBasePath(String.format(baseUrl, env.getName()));
        apiClient.addDefaultHeader("x-user-id", "101");
        return apiClient;
    }

}
