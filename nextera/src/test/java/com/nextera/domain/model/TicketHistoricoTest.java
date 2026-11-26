package com.nextera.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class TicketHistoricoTest {

    @Test
    void deveCriarHistoricoComAutorValido() {
        UUID autorId = UUID.randomUUID();

        TicketHistorico historico = new TicketHistorico(
            UUID.randomUUID(),
            TipoEventoTicket.TICKET_CRIADO,
            "Ticket criado pelo cliente",
            autorId,
            TipoAutor.CLIENTE
        );

        assertNotNull(historico.getId());
        assertEquals(autorId, historico.getAutorId());
        assertEquals(TipoAutor.CLIENTE, historico.getTipoAutor());
    }

    @Test
    void deveGerarIdsDiferentesParaHistoricos() {
        TicketHistorico primeiro = new TicketHistorico(
            UUID.randomUUID(),
            TipoEventoTicket.TICKET_CRIADO,
            "Primeiro evento",
            null,
            TipoAutor.SISTEMA
        );
        TicketHistorico segundo = new TicketHistorico(
            UUID.randomUUID(),
            TipoEventoTicket.TICKET_CRIADO,
            "Segundo evento",
            null,
            TipoAutor.SISTEMA
        );

        assertNotEquals(primeiro.getId(), segundo.getId());
    }

    @Test
    void deveArmazenarValoresAnteriorEAtual() {
        TicketHistorico historico = new TicketHistorico(
            UUID.randomUUID(),
            TipoEventoTicket.STATUS_ALTERADO,
            "Status alterado",
            "ABERTO",
            "EM_ANDAMENTO",
            UUID.randomUUID(),
            TipoAutor.ANALISTA
        );

        assertEquals("ABERTO", historico.getValorAnterior());
        assertEquals("EM_ANDAMENTO", historico.getValorAtual());
    }

    @Test
    void deveCriarHistoricoValidoComAutorSistemaSemAutorId() {
        TicketHistorico historico = new TicketHistorico(
            UUID.randomUUID(),
            TipoEventoTicket.TICKET_CRIADO,
            "Ticket criado pelo sistema",
            null,
            TipoAutor.SISTEMA
        );

        assertNull(historico.getAutorId());
        assertNotNull(historico.getCriadoEm());
    }

    @Test
    void devePermitirValoresAnteriorEAtualNulos() {
        TicketHistorico historico = new TicketHistorico(
            UUID.randomUUID(),
            TipoEventoTicket.STATUS_ALTERADO,
            "Status alterado",
            null,
            null,
            UUID.randomUUID(),
            TipoAutor.ANALISTA
        );

        assertNull(historico.getValorAnterior());
        assertNull(historico.getValorAtual());
    }

    @Test
    void deveRejeitarTicketIdNulo() {
        assertThrows(IllegalArgumentException.class, () -> new TicketHistorico(
            null, TipoEventoTicket.TICKET_CRIADO, "Ticket criado", null, TipoAutor.SISTEMA
        ));
    }

    @Test
    void deveRejeitarTipoEventoNulo() {
        assertThrows(IllegalArgumentException.class, () -> new TicketHistorico(
            UUID.randomUUID(), null, "Ticket criado", null, TipoAutor.SISTEMA
        ));
    }

    @Test
    void deveRejeitarDescricaoNulaOuVazia() {
        UUID ticketId = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class, () -> new TicketHistorico(
            ticketId, TipoEventoTicket.TICKET_CRIADO, null, null, TipoAutor.SISTEMA
        ));
        assertThrows(IllegalArgumentException.class, () -> new TicketHistorico(
            ticketId, TipoEventoTicket.TICKET_CRIADO, "  ", null, TipoAutor.SISTEMA
        ));
    }

    @Test
    void deveRejeitarTipoAutorNulo() {
        assertThrows(IllegalArgumentException.class, () -> new TicketHistorico(
            UUID.randomUUID(), TipoEventoTicket.TICKET_CRIADO, "Ticket criado", null, null
        ));
    }

    @Test
    void deveRejeitarAutorIdNuloQuandoAutorNaoForSistema() {
        assertThrows(IllegalArgumentException.class, () -> new TicketHistorico(
            UUID.randomUUID(),
            TipoEventoTicket.TICKET_CRIADO,
            "Ticket criado",
            null,
            TipoAutor.ANALISTA
        ));
    }

    @Test
    void deveRejeitarAutorIdNuloQuandoAutorForCliente() {
        assertThrows(IllegalArgumentException.class, () -> new TicketHistorico(
            UUID.randomUUID(),
            TipoEventoTicket.TICKET_CRIADO,
            "Ticket criado",
            null,
            TipoAutor.CLIENTE
        ));
    }

    @Test
    void deveRejeitarAutorIdNuloQuandoAutorForAdministrador() {
        assertThrows(IllegalArgumentException.class, () -> new TicketHistorico(
            UUID.randomUUID(),
            TipoEventoTicket.TICKET_CRIADO,
            "Ticket criado",
            null,
            TipoAutor.ADMINISTRADOR
        ));
    }
}
