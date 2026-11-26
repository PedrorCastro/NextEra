package com.nextera.domain.service;

import java.time.LocalDate;
import com.nextera.domain.model.GravidadeProblema;
import com.nextera.domain.model.ObrigacaoFiscal;
import com.nextera.domain.model.TicketPrioridade;

public class PriorizacaoTicketService {

    public TicketPrioridade calcularPrioridade(
        GravidadeProblema gravidade,
        ObrigacaoFiscal obrigacaoFiscal
    ) {
        if (gravidade == null) {
            throw new IllegalArgumentException("A gravidade do problema não pode ser nula.");
        }

        TicketPrioridade prioridade = converterGravidade(gravidade);
        if (prazoProximo(obrigacaoFiscal)) {
            return elevarPrioridade(prioridade);
        }

        return prioridade;
    }

    private TicketPrioridade converterGravidade(GravidadeProblema gravidade) {
        return switch (gravidade) {
            case BAIXA -> TicketPrioridade.BAIXA;
            case MEDIA -> TicketPrioridade.MEDIA;
            case ALTA -> TicketPrioridade.ALTA;
            case CRITICA -> TicketPrioridade.CRITICA;
        };
    }

    private TicketPrioridade elevarPrioridade(TicketPrioridade prioridade) {
        return switch (prioridade) {
            case BAIXA -> TicketPrioridade.MEDIA;
            case MEDIA -> TicketPrioridade.ALTA;
            case ALTA, CRITICA -> TicketPrioridade.CRITICA;
        };
    }

    private boolean prazoProximo(ObrigacaoFiscal obrigacaoFiscal) {
        return obrigacaoFiscal != null
            && obrigacaoFiscal.getDataVencimento() != null
            && !obrigacaoFiscal.getDataVencimento().isAfter(LocalDate.now().plusDays(3));
    }
}