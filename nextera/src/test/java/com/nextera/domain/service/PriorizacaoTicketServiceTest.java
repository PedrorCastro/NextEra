package com.nextera.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import com.nextera.domain.model.GravidadeProblema;
import com.nextera.domain.model.ObrigacaoFiscal;
import com.nextera.domain.model.TicketPrioridade;
import org.junit.jupiter.api.Test;

class PriorizacaoTicketServiceTest {

    private final PriorizacaoTicketService service = new PriorizacaoTicketService();

    @Test
    void deveCombinarGravidadeComPrazoDeAteTresDias() {
        assertEquals(TicketPrioridade.MEDIA, calcular(GravidadeProblema.BAIXA, 3));
        assertEquals(TicketPrioridade.ALTA, calcular(GravidadeProblema.MEDIA, 3));
        assertEquals(TicketPrioridade.CRITICA, calcular(GravidadeProblema.ALTA, 3));
        assertEquals(TicketPrioridade.CRITICA, calcular(GravidadeProblema.CRITICA, 3));
    }

    @Test
    void deveManterPrioridadeDaGravidadeQuandoPrazoForDistante() {
        assertEquals(TicketPrioridade.BAIXA, calcular(GravidadeProblema.BAIXA, 4));
        assertEquals(TicketPrioridade.MEDIA, calcular(GravidadeProblema.MEDIA, 4));
        assertEquals(TicketPrioridade.ALTA, calcular(GravidadeProblema.ALTA, 4));
        assertEquals(TicketPrioridade.CRITICA, calcular(GravidadeProblema.CRITICA, 4));
    }

    @Test
    void deveManterPrioridadeDaGravidadeQuandoObrigacaoOuPrazoNaoExistir() {
        assertEquals(
            TicketPrioridade.MEDIA,
            service.calcularPrioridade(GravidadeProblema.MEDIA, null)
        );
        assertEquals(
            TicketPrioridade.MEDIA,
            service.calcularPrioridade(
                GravidadeProblema.MEDIA,
                new ObrigacaoFiscal("Obrigação", null, "Fiscal")
            )
        );
    }

    @Test
    void deveElevarPrioridadeParaObrigacaoVencida() {
        assertEquals(TicketPrioridade.MEDIA, calcular(GravidadeProblema.BAIXA, -1));
    }

    @Test
    void deveRejeitarGravidadeNula() {
        assertThrows(
            IllegalArgumentException.class,
            () -> service.calcularPrioridade(null, null)
        );
    }

    private TicketPrioridade calcular(GravidadeProblema gravidade, int diasAteVencimento) {
        ObrigacaoFiscal obrigacao = new ObrigacaoFiscal(
            "Obrigação fiscal",
            LocalDate.now().plusDays(diasAteVencimento),
            "Fiscal"
        );
        return service.calcularPrioridade(gravidade, obrigacao);
    }
}
