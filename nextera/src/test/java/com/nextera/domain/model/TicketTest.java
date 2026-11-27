package com.nextera.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TicketTest {

    private UUID clienteId;
    private UUID analistaId;
    private Ticket ticket;

    @BeforeEach
    public void setup() {
        clienteId = UUID.randomUUID();
        analistaId = UUID.randomUUID();
        ticket = new Ticket(
            "Erro no login",
            "Não consigo acessar minha conta",
            TicketPrioridade.ALTA,
            clienteId
        );
    }

    @Test
    public void deveCriarTicketComStatusAbertoEDataCriacao() {
        assertNotNull(ticket.getId());
        assertEquals("Erro no login", ticket.getTitulo());
        assertEquals("Não consigo acessar minha conta", ticket.getDescricao());
        assertEquals(TicketStatus.ABERTO, ticket.getStatus());
        assertEquals(TicketPrioridade.ALTA, ticket.getPrioridade());
        assertEquals(clienteId, ticket.getClienteId());
        assertNotNull(ticket.getCriadoEm());
        assertEquals(ticket.getCriadoEm(), ticket.getAtualizadoEm());
        assertNull(ticket.getAnalistaId());
    }

    @Test
    public void deveRejeitarDadosObrigatoriosInvalidosNaCriacao() {
        assertThrows(IllegalArgumentException.class, () ->
            new Ticket(" ", "Descrição válida", TicketPrioridade.ALTA, clienteId));
        assertThrows(IllegalArgumentException.class, () ->
            new Ticket("Título válido", null, TicketPrioridade.ALTA, clienteId));
        assertThrows(IllegalArgumentException.class, () ->
            new Ticket("Título válido", "Descrição válida", null, clienteId));
        assertThrows(IllegalArgumentException.class, () ->
            new Ticket("Título válido", "Descrição válida", TicketPrioridade.ALTA, null));
    }

    @Test
    public void deveAtribuirERemoverAnalistaComSucesso() {
        ticket.atribuirAnalista(analistaId);
        assertEquals(analistaId, ticket.getAnalistaId());
        assertNotNull(ticket.getAtualizadoEm());

        ticket.removerAnalista();
        assertNull(ticket.getAnalistaId());
    }

    @Test
    public void deveLancarExcecaoAoAtribuirAnalistaQuandoJaPossuiUm() {
        ticket.atribuirAnalista(analistaId);

        assertThrows(IllegalStateException.class, () -> {
            ticket.atribuirAnalista(UUID.randomUUID());
        });
    }

    @Test
    public void deveLancarExcecaoAoAtribuirAnalistaNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            ticket.atribuirAnalista(null);
        });
    }

    @Test
    public void deveSeguirFluxoCompletoDeAtendimentoAteFechamento() {
        ticket.atribuirAnalista(analistaId);
        ticket.iniciarAtendimento();
        assertEquals(TicketStatus.EM_ANDAMENTO, ticket.getStatus());

        ticket.aguardarCliente();
        assertEquals(TicketStatus.AGUARDANDO_CLIENTE, ticket.getStatus());
        assertNotNull(ticket.getAguardandoClienteEm());
        assertEquals(ticket.getAguardandoClienteEm(), ticket.getAtualizadoEm());

        ticket.retomarAtendimento();
        assertEquals(TicketStatus.EM_ANDAMENTO, ticket.getStatus());

        ticket.resolver();
        assertEquals(TicketStatus.RESOLVIDO, ticket.getStatus());
        assertNotNull(ticket.getResolvidoEm());

        ticket.fecharTicket();
        assertEquals(TicketStatus.FECHADO, ticket.getStatus());
        assertNotNull(ticket.getFechadoEm());
        assertEquals(ticket.getFechadoEm(), ticket.getAtualizadoEm());
    }

    @Test
    public void deveIniciarAtendimentoSomenteQuandoTicketEstaAberto() {
        ticket.iniciarAtendimento();
        assertThrows(IllegalStateException.class, () -> ticket.iniciarAtendimento());
        assertEquals(TicketStatus.EM_ANDAMENTO, ticket.getStatus());
    }

    @Test
    public void deveLancarExcecaoAoFecharTicketNaoResolvido() {
        assertThrows(IllegalStateException.class, () -> {
            ticket.fecharTicket();
        });
    }

    @Test
    public void deveLancarExcecaoAoResolverTicketQueNaoEstaEmAndamento() {
        assertThrows(IllegalStateException.class, () -> {
            ticket.resolver();
        });
    }

    @Test
    public void deveAlterarTituloDescricaoEPrioridadeComSucesso() {
        ticket.alterarTitulo("Novo título");
        ticket.alterarDescricao("Nova descrição detalhada");
        ticket.alterarPrioridade(TicketPrioridade.CRITICA);

        assertEquals("Novo título", ticket.getTitulo());
        assertEquals("Nova descrição detalhada", ticket.getDescricao());
        assertEquals(TicketPrioridade.CRITICA, ticket.getPrioridade());
    }

    @Test
    public void deveLancarExcecaoAoAlterarDadosDeTicketFechado() {
        ticket.iniciarAtendimento();
        ticket.resolver();
        ticket.fecharTicket();

        assertThrows(IllegalStateException.class, () -> ticket.alterarTitulo("Outro título"));
        assertThrows(IllegalStateException.class, () -> ticket.alterarDescricao("Outra descrição"));
        assertThrows(IllegalStateException.class, () -> ticket.alterarPrioridade(TicketPrioridade.BAIXA));
        assertThrows(IllegalStateException.class, () -> ticket.atribuirAnalista(analistaId));
    }

    @Test
    public void deveLancarExcecaoAoAlterarParaMesmoValor() {
        assertThrows(IllegalArgumentException.class, () -> ticket.alterarTitulo("Erro no login"));
        assertThrows(IllegalArgumentException.class, () -> ticket.alterarDescricao("Não consigo acessar minha conta"));
        assertThrows(IllegalArgumentException.class, () -> ticket.alterarPrioridade(TicketPrioridade.ALTA));
    }

    @Test
    public void deveManterStatusAbertoQuandoTentarFecharTicketNaoResolvido(){
        assertThrows(IllegalStateException.class, () -> {
            ticket.fecharTicket();
        });
        assertEquals(TicketStatus.ABERTO, ticket.getStatus());
    }
    @Test
    public void deveAguardarAnalistaERetomarAtendimentoComSucesso(){
        ticket.atribuirAnalista(analistaId);
        ticket.iniciarAtendimento();
        ticket.aguardarAnalista();
        assertEquals(TicketStatus.AGUARDANDO_ANALISTA, ticket.getStatus());

        ticket.retomarAtendimento();
        assertEquals(TicketStatus.EM_ANDAMENTO, ticket.getStatus());
    }
    @Test
    public void deveManterPrioridadeAnteriorQuandoNovaPrioridadeForNula(){
        assertThrows(IllegalArgumentException.class, () -> {
            ticket.alterarPrioridade(null);
        });
        assertEquals(TicketPrioridade.ALTA, ticket.getPrioridade());

    }

    @Test
    public void deveLancarExcecaoAoRemoverAnalistaNaoAtribuido() {
        assertThrows(IllegalStateException.class, () -> ticket.removerAnalista());
    }

    @Test
    public void deveImpedirInicioDeAtendimentoEnquantoAguardaCliente() {
        ticket.iniciarAtendimento();
        ticket.aguardarCliente();

        assertThrows(IllegalStateException.class, () -> ticket.iniciarAtendimento());
        assertEquals(TicketStatus.AGUARDANDO_CLIENTE, ticket.getStatus());
    }
}