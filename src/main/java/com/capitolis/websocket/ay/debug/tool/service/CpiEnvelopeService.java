package com.capitolis.websocket.ay.debug.tool.service;

import com.capitolis.eq_cpi_envelope_service.model.CpiEnvelopeRes;
import com.capitolis.websocket.ay.debug.tool.client.CpiEnvelopeClient;
import com.capitolis.websocket.ay.debug.tool.model.EnvName;
import com.capitolis.websocket.ay.debug.tool.utils.InterpolationIdUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CpiEnvelopeService {

    private final CpiEnvelopeClient cpiEnvelopeClient;

    public CpiEnvelopeRes extractCpiEnvelope(String id, EnvName envName) {
        CpiEnvelopeRes cpiEnvelope;

        UUID cpiEnvelopeId = InterpolationIdUtil.tryUuid(id);
        if (cpiEnvelopeId != null) {
            cpiEnvelope = cpiEnvelopeClient.fetchCpiEnvelopeBy(cpiEnvelopeId, envName);
        } else {
            try {
                int friendlyId = Integer.parseInt(id);
                cpiEnvelope = cpiEnvelopeClient.fetchCpiEnvelopeBy(friendlyId, envName);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Failed to parse ID, should be int or UUID", e);
            }
        }

        return cpiEnvelope;
    }

}
