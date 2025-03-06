package com.capitolis.websocket.ay.debug.tool.service;

import com.capitolis.websocket.ay.debug.tool.model.CpiEnvelopeReportDao;
import com.capitolis.websocket.ay.debug.tool.model.EnvName;
import com.capitolis.websocket.ay.debug.tool.utils.JsonPrettyPrinter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class EnvelopeReportService {

    private final EnvelopeReportDataCollector envelopeReportDataCollector;

    public CpiEnvelopeReportDao extractEnvelopeData(String id, boolean withAccruals, EnvName envName) throws IOException {
        CpiEnvelopeReportDao cpiEnvelopeReportDao = envelopeReportDataCollector.collect(id, withAccruals, envName);

        log.info("\n" + JsonPrettyPrinter.printJsonAsTable(cpiEnvelopeReportDao));

        return cpiEnvelopeReportDao;
    }

}
