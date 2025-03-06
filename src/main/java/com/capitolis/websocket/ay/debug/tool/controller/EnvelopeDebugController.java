package com.capitolis.websocket.ay.debug.tool.controller;

import com.capitolis.websocket.ay.debug.tool.model.CpiEnvelopeReportDao;
import com.capitolis.websocket.ay.debug.tool.model.EnvName;
import com.capitolis.websocket.ay.debug.tool.service.EnvelopeReportService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController()
@RequiredArgsConstructor
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/envelope-debug")
public class EnvelopeDebugController {

    private final EnvelopeReportService envelopeReportService;


    @GetMapping(path = "/data2")
    public CpiEnvelopeReportDao extractEnvelopeData2(
             @RequestParam String id
            ,@Schema(type = "boolean", allowableValues = {"false", "true"})
             @RequestParam(value = "withAccruals") boolean withAccruals
            ,@Schema(type = "string", allowableValues = { "edge2", "qa-euw1","qa3", "eq-uat"})
             @RequestParam String env
    ) throws IOException {

        return envelopeReportService.extractEnvelopeData(id, withAccruals, EnvName.fromName(env));

    }


}

