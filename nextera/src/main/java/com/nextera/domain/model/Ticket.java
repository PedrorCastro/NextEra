package com.nextera.domain.model;
import java.time.Instant;
import java.util.UUID;

public class Ticket {
    private UUID id;
    private String titulo;
    private String descricao;
    private TicketStatus status;
    private TicketPrioridade prioridade;
    private UUID clienteId;
    private UUID analistaId;
    private Instant criadoEm;
    private Instant atualizadoEm;
    private Instant fechadoEm;
    private Instant resolvidoEm;
    private Instant aguardandoClienteEm;

    public Ticket(String titulo, String descricao,
                TicketPrioridade prioridade,
                UUID clienteId) {
        this.id = UUID.randomUUID();
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = TicketStatus.ABERTO;
        this.prioridade = prioridade;
        this.clienteId = clienteId;
        validarDadosObrigatorios();
        Instant agora = Instant.now();
        this.criadoEm = agora;
        this.atualizadoEm = agora;
    }


    public UUID getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public TicketPrioridade getPrioridade() {
        return prioridade;
    }

    public UUID getClienteId() {
        return clienteId;
    }

    public UUID getAnalistaId() {
        return analistaId;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }

    public Instant getAtualizadoEm() {
        return atualizadoEm;
    }

    public Instant getFechadoEm() {
        return fechadoEm;
    }

    public Instant getResolvidoEm() {
        return resolvidoEm;
    }

    public Instant getAguardandoClienteEm() {
        return aguardandoClienteEm;
    }

    public void atribuirAnalista(UUID analistaId) {
        if (analistaId == null) {
            throw new IllegalArgumentException("O ID do analista não pode ser nulo.");
        }

        if (this.analistaId != null) {
            throw new IllegalStateException("O ticket já possui um analista atribuído.");
        }

        if (this.status == TicketStatus.FECHADO ||
            this.status == TicketStatus.RESOLVIDO ||
            this.status == TicketStatus.AUTO_RESOLVIDO) {
            throw new IllegalStateException("Não é possível atribuir um analista a um ticket encerrado ou resolvido.");
        }

        this.analistaId = analistaId;
        this.atualizadoEm = Instant.now();
    }

    public void iniciarAtendimento() {
        if (this.status != TicketStatus.ABERTO) {
            throw new IllegalStateException("Só é possível iniciar o atendimento de um ticket aberto.");
        }
        this.status = TicketStatus.EM_ANDAMENTO;
        this.atualizadoEm = Instant.now();
    }

    public void fecharTicket() {
        if(this.status != TicketStatus.RESOLVIDO && this.status != TicketStatus.AUTO_RESOLVIDO) {
            throw new IllegalStateException("Não é possível fechar um ticket que não foi resolvido.");
        }
        Instant agora = Instant.now();
        this.status = TicketStatus.FECHADO;
        this.fechadoEm = agora;
        this.atualizadoEm = agora;
    }

    public void aguardarCliente(){
        if(this.status != TicketStatus.EM_ANDAMENTO) {
            throw new IllegalStateException("Não é possível aguardar o cliente de um ticket que não está em andamento.");
        }
        Instant agora = Instant.now();
        this.status = TicketStatus.AGUARDANDO_CLIENTE;
        this.aguardandoClienteEm = agora;
        this.atualizadoEm = agora;
    }

    public void aguardarAnalista(){
        if(this.status != TicketStatus.EM_ANDAMENTO) {
            throw new IllegalStateException("Não é possível aguardar o analista de um ticket que não está em andamento.");
        }
        this.status = TicketStatus.AGUARDANDO_ANALISTA;
        this.atualizadoEm = Instant.now();
    }
    public void resolver(){
        Instant agora = Instant.now();

        if (this.status != TicketStatus.EM_ANDAMENTO) {
            throw new IllegalStateException("Não é possível resolver um ticket que não está em andamento.");
        }
        this.status = TicketStatus.RESOLVIDO;
        this.atualizadoEm = agora;
        this.resolvidoEm = agora;
        
    }
    public void alterarPrioridade(TicketPrioridade prioridade) {
        if(this.status == TicketStatus.FECHADO || this.status == TicketStatus.RESOLVIDO || this.status == TicketStatus.AUTO_RESOLVIDO) {
            throw new IllegalStateException("Não é possível atualizar a prioridade de um ticket fechado ou resolvido.");
        }
        if(prioridade == null) {
            throw new IllegalArgumentException("A prioridade não pode ser nula.");
        }
        if(this.prioridade == prioridade) {
            throw new IllegalArgumentException("A prioridade do ticket já é " + prioridade + ".");
        }
        
        this.prioridade = prioridade;
        this.atualizadoEm = Instant.now();
    }

    public void retomarAtendimento(){
        if(this.status != TicketStatus.AGUARDANDO_CLIENTE && this.status != TicketStatus.AGUARDANDO_ANALISTA) {
            throw new IllegalStateException("Não é possível retomar o atendimento de um ticket que não está aguardando cliente ou analista.");
        }
        this.status = TicketStatus.EM_ANDAMENTO;
        this.atualizadoEm = Instant.now();
        
    }

    public void removerAnalista() {
        if (this.analistaId == null) {
            throw new IllegalStateException("Não é possível remover um analista de um ticket que não possui um analista atribuído.");
        }
        if(this.status == TicketStatus.FECHADO || this.status == TicketStatus.RESOLVIDO || this.status == TicketStatus.AUTO_RESOLVIDO) {
            throw new IllegalStateException("Não é possível remover um analista de um ticket fechado ou resolvido.");
        }
        this.analistaId = null;
        this.atualizadoEm = Instant.now();
    }

    public void alterarDescricao(String descricao) {
        if(this.status == TicketStatus.FECHADO || this.status == TicketStatus.RESOLVIDO || this.status == TicketStatus.AUTO_RESOLVIDO) {
            throw new IllegalStateException("Não é possível atualizar a descrição de um ticket fechado ou resolvido.");
        }
        if(descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("A descrição não pode ser nula ou vazia.");
        }

        if(this.descricao.equals(descricao)) {
            throw new IllegalArgumentException("A descrição do ticket já é a mesma.");
        }
        this.descricao = descricao;
        this.atualizadoEm = Instant.now();
    }

    
    public void alterarTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("O título não pode ser nulo ou vazio.");
        }

        if (this.status == TicketStatus.FECHADO ||
            this.status == TicketStatus.RESOLVIDO ||
            this.status == TicketStatus.AUTO_RESOLVIDO) {
            throw new IllegalStateException("Não é possível atualizar o título de um ticket fechado ou resolvido.");
        }

        if (this.titulo.equals(titulo)) {
            throw new IllegalArgumentException("O título do ticket já é o mesmo.");
        }

        this.titulo = titulo;
        this.atualizadoEm = Instant.now();
    }


    private void validarDadosObrigatorios(){
        if(this.titulo == null || this.titulo.isBlank()) {
            throw new IllegalArgumentException("O título do ticket é obrigatório.");
        }
        if(this.descricao == null || this.descricao.isBlank()) {
            throw new IllegalArgumentException("A descrição do ticket é obrigatória.");
        }
        if(this.prioridade == null) {
            throw new IllegalArgumentException("A prioridade do ticket é obrigatória.");
        }
        if(this.clienteId == null) {
            throw new IllegalArgumentException("O ID do cliente é obrigatório.");
        }
    }

}