package com.capitolis.websocket.ay.debug.tool.service;

import com.capitolis.eq.eq_common_advance_service.model.AdvanceDto;
import com.capitolis.eq.eq_common_notes_service.model.NoteDto;
import com.capitolis.eq.eq_common_notes_service.model.SeriesResponseDto;
import com.capitolis.eq_common_financing_terms_service.model.FinancingTerms;
import com.capitolis.eq_common_payment_schedule_service.model.PaymentTable;
import com.capitolis.eq_common_payment_schedule_service.model.PaymentTableWithAccrualPeriodsDto;
import com.capitolis.eq_cpi_envelope_service.model.CpiEnvelopeRes;
import com.capitolis.websocket.ay.debug.tool.client.*;
import com.capitolis.websocket.ay.debug.tool.model.CpiEnvelopeReportDao;
import com.capitolis.websocket.ay.debug.tool.model.EnvName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Data
@Slf4j
public class EnvelopeReportDataCollector {

    private final CpiEnvelopeService envelopeService;
    private final SeriesServiceClient seriesServiceClient;
    private final NoteServiceClient noteServiceClient;
    private final AdvanceServiceClient advanceServiceClient;
    private final PaymentScheduleClient paymentScheduleClient;
    private final FinancingTermsServiceClient financingTermsServiceClient;


    public CpiEnvelopeReportDao collect(String id, boolean withAccruals, EnvName envName) {
        CpiEnvelopeRes cpiEnvelope = envelopeService.extractCpiEnvelope(id, envName);

        SeriesResponseDto series = seriesServiceClient.getById(cpiEnvelope.getSeriesId(), envName);

        NoteDto note = noteServiceClient.getById(cpiEnvelope.getNoteId(), envName);
        FinancingTerms noteFinancingTerms = financingTermsServiceClient.getFinancingTermsById(note.getFinancingTerms().getId(), envName);

        PaymentTableWithAccrualPeriodsDto notePaymentTableWithAccruals = null;
        PaymentTable notePaymentTable = null;
        if (withAccruals) {
            notePaymentTableWithAccruals = paymentScheduleClient.getPaymentTableWithAccrualPeriodsDto(note.getPaymentTableId(), envName);
        } else {
            notePaymentTable = paymentScheduleClient.getPaymentTable(note.getPaymentTableId(), envName);
        }


        AdvanceDto advance = advanceServiceClient.getAdvance(cpiEnvelope.getAdvanceId(), envName);
        FinancingTerms advanceFinancingTerms = financingTermsServiceClient.getFinancingTermsById(advance.getFinancingTerms().getId(), envName);

        PaymentTableWithAccrualPeriodsDto advancePaymentTableWithAccruals = null;
        PaymentTable advancePaymentTable = null;
        if (withAccruals) {
            advancePaymentTableWithAccruals = paymentScheduleClient.getPaymentTableWithAccrualPeriodsDto(advance.getPaymentTableId(), envName);
        } else {
            advancePaymentTable = paymentScheduleClient.getPaymentTable(advance.getPaymentTableId(), envName);
        }

        String detailedPrettyString = String.format(
                " Cpi: %s\n" +
                " Series: %s\n" +

                " Note: %s\n" +
                " Note-PT: %s\n" +
                " Note-FT: %s\n" +

                " Advance: %s\n" +
                " Advance-PT: %s\n" +
                " Advance-FT: %s\n"

                , cpiEnvelope, series

                , note   ,    notePaymentTableWithAccruals,    noteFinancingTerms
                , advance, advancePaymentTableWithAccruals, advanceFinancingTerms
        );
        log.info("\n" + detailedPrettyString);

        return CpiEnvelopeReportDao.builder()
                .cpiEnvelopeRes(cpiEnvelope)
                .series(series)

                .note(note)
                .noteFinancingTerms(noteFinancingTerms)
                .notePaymentTable(notePaymentTable)
                .notePaymentTableWithAccrualPeriodsDto(notePaymentTableWithAccruals)

                .advanceDto(advance)
                .advanceFinancingTerms(advanceFinancingTerms)
                .advancePaymentTable(advancePaymentTable)
                .advancePaymentTableWithAccrualPeriodsDto(advancePaymentTableWithAccruals)

                .build();
    }

}
