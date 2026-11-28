package com.nextera.domain.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DiagnosticoTest {

    private UUID clienteId;
    private Diagnostico diagnostico;

    @BeforeEach
    void setup() {
        clienteId = UUID.randomUUID();
        diagnostico = new Diagnostico(
            clienteId,
            "Erro ao emitir nota fiscal",
            "FISCAL",
            "NF-500"
        );
    }

    @Test
    void deveCriarDiagnosticoComStatusRecebidoEValoresIniciais() {
        assertAll(
            () -> assertNotNull(diagnostico.getId()),
            () -> assertEquals(clienteId, diagnostico.getClienteId()),
            () -> assertEquals("Erro ao emitir nota fiscal", diagnostico.getDescricaoProblema()),
            () -> assertEquals("FISCAL", diagnostico.getModuloSistema()),
            () -> assertEquals("NF-500", diagnostico.getCodigoErro()),
            () -> assertEquals(DiagnosticoStatus.RECEBIDO, diagnostico.getStatus()),
            () -> assertNull(diagnostico.getResultado()),
            () -> assertNull(diagnostico.getDetalhes()),
            () -> assertNull(diagnostico.getTicketId()),
            () -> assertNotNull(diagnostico.getCriadoEm()),
            () -> assertNull(diagnostico.getConcluidoEm())
        );
    }

    @Test
    void deveGerarIdUnico() {
        Diagnostico outroDiagnostico = new Diagnostico(
            clienteId,
            "Erro ao emitir nota fiscal",
            "FISCAL",
            "NF-500"
        );

        assertNotEquals(diagnostico.getId(), outroDiagnostico.getId());
    }

    @Test
    void devePermitirCodigoErroNuloNaCriacao() {
        Diagnostico semCodigoErro = new Diagnostico(
            clienteId,
            "Lentidão no fechamento mensal",
            "CONTABIL",
            null
        );

        assertNull(semCodigoErro.getCodigoErro());
        assertEquals(DiagnosticoStatus.RECEBIDO, semCodigoErro.getStatus());
    }

    @Test
    void deveRejeitarClienteIdNulo() {
        assertThrows(IllegalArgumentException.class, () ->
            new Diagnostico(null, "Erro ao emitir nota", "FISCAL", "NF-500")
        );
    }

    @Test
    void deveRejeitarDescricaoProblemaNulaOuVazia() {
        assertAll(
            () -> assertThrows(IllegalArgumentException.class, () ->
                new Diagnostico(clienteId, null, "FISCAL", "NF-500")
            ),
            () -> assertThrows(IllegalArgumentException.class, () ->
                new Diagnostico(clienteId, "", "FISCAL", "NF-500")
            ),
            () -> assertThrows(IllegalArgumentException.class, () ->
                new Diagnostico(clienteId, "   ", "FISCAL", "NF-500")
            )
        );
    }

    @Test
    void deveRejeitarModuloSistemaNuloOuVazio() {
        assertAll(
            () -> assertThrows(IllegalArgumentException.class, () ->
                new Diagnostico(clienteId, "Erro ao emitir nota", null, "NF-500")
            ),
            () -> assertThrows(IllegalArgumentException.class, () ->
                new Diagnostico(clienteId, "Erro ao emitir nota", "", "NF-500")
            ),
            () -> assertThrows(IllegalArgumentException.class, () ->
                new Diagnostico(clienteId, "Erro ao emitir nota", "   ", "NF-500")
            )
        );
    }

    @Test
    void deveIniciarAnaliseQuandoEstiverRecebido() {
        diagnostico.iniciarAnalise();

        assertEquals(DiagnosticoStatus.EM_ANALISE, diagnostico.getStatus());
    }

    @Test
    void naoDeveIniciarAnaliseDuasVezes() {
        diagnostico.iniciarAnalise();

        assertThrows(IllegalStateException.class, () -> diagnostico.iniciarAnalise());
    }

    @Test
    void naoDeveIniciarAnaliseAposConclusao() {
        diagnostico.iniciarAnalise();
        diagnostico.concluirDiagnostico(ResultadoDiagnostico.RESOLVIDO_AUTOMATICAMENTE);

        assertThrows(IllegalStateException.class, () -> diagnostico.iniciarAnalise());
    }

    @Test
    void naoDeveIniciarAnaliseAposFalha() {
        diagnostico.iniciarAnalise();
        diagnostico.registrarFalha("Timeout ao consultar SEFAZ");

        assertThrows(IllegalStateException.class, () -> diagnostico.iniciarAnalise());
    }

    @Test
    void deveConcluirDiagnosticoQuandoEstiverEmAnalise() {
        Instant antes = Instant.now();

        diagnostico.iniciarAnalise();
        diagnostico.concluirDiagnostico(ResultadoDiagnostico.NECESSITA_ATENDIMENTO);

        Instant depois = Instant.now();

        assertAll(
            () -> assertEquals(DiagnosticoStatus.CONCLUIDO, diagnostico.getStatus()),
            () -> assertEquals(ResultadoDiagnostico.NECESSITA_ATENDIMENTO, diagnostico.getResultado()),
            () -> assertNotNull(diagnostico.getConcluidoEm()),
            () -> assertTrue(
                !diagnostico.getConcluidoEm().isBefore(antes)
                && !diagnostico.getConcluidoEm().isAfter(depois)
            )
        );
    }

    @Test
    void naoDeveConcluirDiagnosticoQuandoEstiverRecebido() {
        assertThrows(IllegalStateException.class, () ->
            diagnostico.concluirDiagnostico(ResultadoDiagnostico.NECESSITA_ATENDIMENTO)
        );
    }

    @Test
    void naoDeveConcluirDiagnosticoQuandoJaEstiverConcluido() {
        diagnostico.iniciarAnalise();
        diagnostico.concluirDiagnostico(ResultadoDiagnostico.NECESSITA_ATENDIMENTO);

        assertThrows(IllegalStateException.class, () ->
            diagnostico.concluirDiagnostico(ResultadoDiagnostico.ERRO_NAO_IDENTIFICADO)
        );
    }

    @Test
    void naoDeveConcluirDiagnosticoQuandoEstiverEmFalha() {
        diagnostico.iniciarAnalise();
        diagnostico.registrarFalha("Falha inesperada");

        assertThrows(IllegalStateException.class, () ->
            diagnostico.concluirDiagnostico(ResultadoDiagnostico.NECESSITA_ATENDIMENTO)
        );
    }

    @Test
    void deveRejeitarConcluirDiagnosticoComResultadoNulo() {
        diagnostico.iniciarAnalise();

        assertThrows(IllegalArgumentException.class, () ->
            diagnostico.concluirDiagnostico(null)
        );
        assertEquals(DiagnosticoStatus.EM_ANALISE, diagnostico.getStatus());
        assertNull(diagnostico.getResultado());
        assertNull(diagnostico.getConcluidoEm());
    }

    @Test
    void deveRegistrarFalhaQuandoEstiverEmAnalise() {
        Instant antes = Instant.now();

        diagnostico.iniciarAnalise();
        diagnostico.registrarFalha("Falha de comunicação com serviço externo");

        Instant depois = Instant.now();

        assertAll(
            () -> assertEquals(DiagnosticoStatus.FALHA, diagnostico.getStatus()),
            () -> assertEquals("Falha de comunicação com serviço externo", diagnostico.getDetalhes()),
            () -> assertNotNull(diagnostico.getConcluidoEm()),
            () -> assertTrue(
                !diagnostico.getConcluidoEm().isBefore(antes)
                && !diagnostico.getConcluidoEm().isAfter(depois)
            )
        );
    }

    @Test
    void naoDeveRegistrarFalhaQuandoEstiverRecebido() {
        assertThrows(IllegalStateException.class, () ->
            diagnostico.registrarFalha("Falha inesperada")
        );
    }

    @Test
    void naoDeveRegistrarFalhaQuandoJaEstiverEmFalha() {
        diagnostico.iniciarAnalise();
        diagnostico.registrarFalha("Falha inesperada");

        assertThrows(IllegalStateException.class, () ->
            diagnostico.registrarFalha("Nova falha")
        );
    }

    @Test
    void naoDeveRegistrarFalhaQuandoJaEstiverConcluido() {
        diagnostico.iniciarAnalise();
        diagnostico.concluirDiagnostico(ResultadoDiagnostico.RESOLVIDO_AUTOMATICAMENTE);

        assertThrows(IllegalStateException.class, () ->
            diagnostico.registrarFalha("Falha após conclusão")
        );
    }

    @Test
    void deveRejeitarRegistrarFalhaComDetalhesNulosOuVazios() {
        diagnostico.iniciarAnalise();

        assertAll(
            () -> assertThrows(IllegalArgumentException.class, () -> diagnostico.registrarFalha(null)),
            () -> assertThrows(IllegalArgumentException.class, () -> diagnostico.registrarFalha("")),
            () -> assertThrows(IllegalArgumentException.class, () -> diagnostico.registrarFalha("   "))
        );
        assertEquals(DiagnosticoStatus.EM_ANALISE, diagnostico.getStatus());
        assertNull(diagnostico.getDetalhes());
        assertNull(diagnostico.getConcluidoEm());
    }

    @Test
    void deveVincularTicketQuandoResultadoForNecessitaAtendimento() {
        UUID ticketId = UUID.randomUUID();
        diagnostico.iniciarAnalise();
        diagnostico.concluirDiagnostico(ResultadoDiagnostico.NECESSITA_ATENDIMENTO);

        diagnostico.vincularTicket(ticketId);

        assertEquals(ticketId, diagnostico.getTicketId());
    }

    @Test
    void deveVincularTicketQuandoResultadoForErroNaoIdentificado() {
        UUID ticketId = UUID.randomUUID();
        diagnostico.iniciarAnalise();
        diagnostico.concluirDiagnostico(ResultadoDiagnostico.ERRO_NAO_IDENTIFICADO);

        diagnostico.vincularTicket(ticketId);

        assertEquals(ticketId, diagnostico.getTicketId());
    }

    @Test
    void naoDeveVincularTicketQuandoEstiverRecebido() {
        assertThrows(IllegalStateException.class, () ->
            diagnostico.vincularTicket(UUID.randomUUID())
        );
    }

    @Test
    void naoDeveVincularTicketQuandoEstiverEmAnalise() {
        diagnostico.iniciarAnalise();

        assertThrows(IllegalStateException.class, () ->
            diagnostico.vincularTicket(UUID.randomUUID())
        );
    }

    @Test
    void naoDeveVincularTicketQuandoEstiverEmFalha() {
        diagnostico.iniciarAnalise();
        diagnostico.registrarFalha("Erro na análise");

        assertThrows(IllegalStateException.class, () ->
            diagnostico.vincularTicket(UUID.randomUUID())
        );
    }

    @Test
    void deveRejeitarVincularTicketQuandoJaPossuiTicketVinculado() {
        UUID primeiroTicket = UUID.randomUUID();
        UUID segundoTicket = UUID.randomUUID();

        diagnostico.iniciarAnalise();
        diagnostico.concluirDiagnostico(ResultadoDiagnostico.NECESSITA_ATENDIMENTO);
        diagnostico.vincularTicket(primeiroTicket);

        assertThrows(IllegalStateException.class, () -> diagnostico.vincularTicket(segundoTicket));
        assertEquals(primeiroTicket, diagnostico.getTicketId());
    }

    @Test
    void deveRejeitarVincularTicketComTicketIdNulo() {
        diagnostico.iniciarAnalise();
        diagnostico.concluirDiagnostico(ResultadoDiagnostico.NECESSITA_ATENDIMENTO);

        assertThrows(IllegalArgumentException.class, () -> diagnostico.vincularTicket(null));
        assertNull(diagnostico.getTicketId());
    }

    @Test
    void deveRejeitarVincularTicketQuandoResolvidoAutomaticamente() {
        diagnostico.iniciarAnalise();
        diagnostico.concluirDiagnostico(ResultadoDiagnostico.RESOLVIDO_AUTOMATICAMENTE);

        assertThrows(IllegalStateException.class, () ->
            diagnostico.vincularTicket(UUID.randomUUID())
        );
        assertNull(diagnostico.getTicketId());
    }
}
