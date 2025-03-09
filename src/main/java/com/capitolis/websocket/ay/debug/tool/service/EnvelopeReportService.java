package com.capitolis.websocket.ay.debug.tool.service;

import com.capitolis.websocket.ay.debug.tool.model.CpiEnvelopeReportDto;
import com.capitolis.websocket.ay.debug.tool.model.EnvName;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class EnvelopeReportService {

    private final EnvelopeReportDataCollector envelopeReportDataCollector;
    private final EnvelopeReportPrinterService envelopeReportPrinterService;

    public CpiEnvelopeReportDto extractEnvelopeData(String id, boolean withAccruals, EnvName envName) throws IOException {
        CpiEnvelopeReportDto cpiEnvelopeReportDto = envelopeReportDataCollector.collect(id, withAccruals, envName);

        envelopeReportPrinterService.prettyPrint(cpiEnvelopeReportDto);

        return cpiEnvelopeReportDto;
    }

}
