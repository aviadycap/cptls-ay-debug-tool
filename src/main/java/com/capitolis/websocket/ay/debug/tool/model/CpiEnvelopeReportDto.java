package com.capitolis.websocket.ay.debug.tool.model;

import com.capitolis.eq.eq_common_advance_service.model.AdvanceDto;
import com.capitolis.eq.eq_common_notes_service.model.NoteDto;
import com.capitolis.eq.eq_common_notes_service.model.SeriesResponseDto;
import com.capitolis.eq_common_financing_terms_service.model.FinancingTerms;
import com.capitolis.eq_common_payment_schedule_service.model.PaymentTable;
import com.capitolis.eq_common_payment_schedule_service.model.PaymentTableWithAccrualPeriodsDto;
import com.capitolis.eq_cpi_envelope_service.model.CpiEnvelopeRes;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CpiEnvelopeReportDto {

    private CpiEnvelopeRes cpiEnvelopeRes;

    private SeriesResponseDto series;

    private NoteDto                           note;
    private PaymentTable                      notePaymentTable;
    private PaymentTableWithAccrualPeriodsDto notePaymentTableWithAccrualPeriodsDto;
    private FinancingTerms                    noteFinancingTerms;

    private AdvanceDto                        advanceDto;
    private PaymentTable                      advancePaymentTable;
    private PaymentTableWithAccrualPeriodsDto advancePaymentTableWithAccrualPeriodsDto;
    private FinancingTerms                    advanceFinancingTerms;

}
