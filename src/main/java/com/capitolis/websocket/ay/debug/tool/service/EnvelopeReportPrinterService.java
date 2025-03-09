package com.capitolis.websocket.ay.debug.tool.service;

import com.capitolis.eq.eq_common_notes_service.model.NoteDto;
import com.capitolis.eq.eq_common_notes_service.model.SeriesResponseDto;
import com.capitolis.eq_common_financing_terms_service.model.FinancingTerms;
import com.capitolis.eq_common_payment_schedule_service.model.PaymentTable;
import com.capitolis.eq_common_payment_schedule_service.model.PaymentTableWithAccrualPeriodsDto;
import com.capitolis.eq_cpi_envelope_service.model.CpiEnvelopeRes;
import com.capitolis.websocket.ay.debug.tool.model.CpiEnvelopeReportDto;
import com.capitolis.websocket.ay.debug.tool.utils.JsonPrinter;
import com.capitolis.websocket.ay.debug.tool.utils.JsonUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class EnvelopeReportPrinterService {

    ObjectMapper mapper;

    public EnvelopeReportPrinterService() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);
    }



    @SneakyThrows
    public void prettyPrint(CpiEnvelopeReportDto cpiEnvelopeReportDto) {
        SeriesResponseDto seriesDto = cpiEnvelopeReportDto.getSeries();
        NoteDto noteDto = cpiEnvelopeReportDto.getNote();
        PaymentTable notePaymentTable = cpiEnvelopeReportDto.getNotePaymentTable();
        PaymentTable advancePaymentTable = cpiEnvelopeReportDto.getAdvancePaymentTable();

        FinancingTerms noteFinancingTerms = cpiEnvelopeReportDto.getNoteFinancingTerms();


        //Pretty print each model.
        //For that, we would need to ignore some attributes.
        //Steps -
        // 1. convert to Json.
        // 2. Remove attribute.
        // 3. Pretty print Json.
        String cpiEnvelopeJson = mapper.writeValueAsString(seriesDto);
        String cpiEnvelopeString = JsonPrinter.formatJsonAsTable(cpiEnvelopeJson);

        String seriesJson = mapper.writeValueAsString(cpiEnvelopeReportDto.getSeries());
        String seriesString = JsonPrinter.formatJsonAsTable(seriesJson);

        String noteJson = mapper.writeValueAsString(noteDto);
        String noteWithoutFtJson = JsonUtil.formatJsonRemoveKeys(noteJson, "financingTerms");
        String noteString = JsonPrinter.formatJsonAsTable(noteWithoutFtJson);

        String noteFtJson = mapper.writeValueAsString(noteFinancingTerms);
        String noteFtString = JsonPrinter.formatJsonAsTable(noteFtJson);


        String notePaymentTableNoPeriods = formatJsonTableWithoutPeriods(notePaymentTable, mapper);
        String notePstPeriods = (notePaymentTable != null) ? formatJsonTableForPeriods(notePaymentTable.getPaymentPeriods(), mapper) : null;

        PaymentTableWithAccrualPeriodsDto notePaymentTableWithAccrualPeriodsDto = cpiEnvelopeReportDto.getNotePaymentTableWithAccrualPeriodsDto();
        if (notePaymentTableWithAccrualPeriodsDto != null) {
            notePaymentTableNoPeriods = formatJsonTableWithoutPeriods(notePaymentTableWithAccrualPeriodsDto, mapper);
            notePstPeriods = formatJsonTableForPeriods(notePaymentTableWithAccrualPeriodsDto.getPaymentPeriods(), mapper);
        }

        String advanceJson = mapper.writeValueAsString(cpiEnvelopeReportDto.getAdvanceDto());
        String advanceWithoutFtJson = JsonUtil.formatJsonRemoveKeys(advanceJson, "financingTerms");
        String advanceString = JsonPrinter.formatJsonAsTable(advanceWithoutFtJson);

        String advanceFtJson = mapper.writeValueAsString(cpiEnvelopeReportDto.getAdvanceFinancingTerms());
        String advanceFtString = JsonPrinter.formatJsonAsTable(advanceFtJson);


        String advancePaymentTableNoPeriods = formatJsonTableWithoutPeriods(advancePaymentTable, mapper);
        String advancePstPeriods = (notePaymentTable != null) ? formatJsonTableForPeriods(notePaymentTable.getPaymentPeriods(), mapper) : null;

        PaymentTableWithAccrualPeriodsDto advancePaymentTableWithAccrualPeriodsDto = cpiEnvelopeReportDto.getNotePaymentTableWithAccrualPeriodsDto();
        if (advancePaymentTableWithAccrualPeriodsDto != null) {
            advancePaymentTableNoPeriods = formatJsonTableWithoutPeriods(advancePaymentTableWithAccrualPeriodsDto, mapper);
            advancePstPeriods = formatJsonTableForPeriods(advancePaymentTableWithAccrualPeriodsDto.getPaymentPeriods(), mapper);
        }


        log.info("\n\n CpiEnvelope\n" + cpiEnvelopeString
                +"\n\n NoteSeries\n"  + seriesString
                +"\n\n Note\n"        + noteString
                +"\n\n NoteFT\n"      + noteFtString
                +"\n\n NotePaymentTable\n"+ notePaymentTableNoPeriods
                +"\n\n NotePaymentTablePeriods\n"+ notePstPeriods

                +"\n\n Advance\n"        + advanceString
                +"\n\n AdvanceFT\n"      + advanceFtString
                +"\n\n AdvancePaymentTable\n"+ advancePaymentTableNoPeriods
                +"\n\n AdvancePaymentTablePeriods\n"+ advancePstPeriods
        );

    }

    private String formatJsonTableWithoutPeriods(Object table, ObjectMapper mapper) throws JsonProcessingException {
        if (table == null) {
            return null;
        }
        String json = mapper.writeValueAsString(table);
        String jsonWithoutPeriods = JsonUtil.formatJsonRemoveKeys(json, "paymentPeriods");
        return JsonPrinter.formatJsonAsTable(jsonWithoutPeriods);
    }

    private String formatJsonTableForPeriods(List<?> periods, ObjectMapper mapper) throws JsonProcessingException {
        if (periods == null) {
            return null;
        }
        return JsonPrinter.formatJsonAsTable(mapper.writeValueAsString(periods));
    }
}
