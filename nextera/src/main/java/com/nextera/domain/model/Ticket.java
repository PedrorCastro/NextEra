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
                TicketStatus status, TicketPrioridade prioridade,
                UUID clienteId) {
        this.id = UUID.randomUUID();
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = TicketStatus.ABERTO;
        this.prioridade = prioridade;
        this.clienteId = clienteId;
        this.criadoEm = Instant.now();
    }


    //Getters and Setters
    public UUID getId() {
        return id;
    }
    private void setId(UUID id) {
        this.id = id;
    }
    public String getTitulo() {
        return titulo;
    }
    private void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getDescricao() {
        return descricao;
    }
    private void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public TicketStatus getStatus() {
        return status;
    }
    private void setStatus(TicketStatus status) {
        this.status = status;
    }
    public TicketPrioridade getPrioridade() {
        return prioridade;
    }
    private void setPrioridade(TicketPrioridade prioridade) {
        this.prioridade = prioridade;
    }
    public UUID getClienteId() {
        return clienteId;
    }
    private void setClienteId(UUID clienteId) {
        this.clienteId = clienteId;
    }
    public UUID getAnalistaId() {
        return analistaId;
    }
    private void setAnalistaId(UUID analistaId) {
        this.analistaId = analistaId;
    }
    public Instant getCriadoEm() {
        return criadoEm;
    }
    private void setCriadoEm(Instant criadoEm) {
        this.criadoEm = criadoEm;
    }
    public Instant getAtualizadoEm() {
        return atualizadoEm;
    }
    private void setAtualizadoEm(Instant atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }
    public Instant getFechadoEm() {
        return fechadoEm;
    }
    private void setFechadoEm(Instant fechadoEm) {
        this.fechadoEm = fechadoEm;
    }
    public Instant getResolvidoEm() {
        return resolvidoEm;
    }
    private void setResolvidoEm(Instant resolvidoEm) {
        this.resolvidoEm = resolvidoEm;
    }
    public Instant getAguardandoClienteEm() {
        return aguardandoClienteEm;
    }
    private void setAguardandoClienteEm(Instant aguardandoClienteEm) {
        this.aguardandoClienteEm = aguardandoClienteEm;
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

        if(this.status == TicketStatus.FECHADO || this.status == TicketStatus.RESOLVIDO || this.status == TicketStatus.AUTO_RESOLVIDO) {
            throw new IllegalStateException("Não é possível iniciar o atendimento de um ticket fechado ou resolvido.");
        } 
            this.status = TicketStatus.EM_ANDAMENTO;
            this.atualizadoEm = Instant.now();

        
    }

    public void fecharTicket() {
        if(this.status != TicketStatus.RESOLVIDO && this.status != TicketStatus.AUTO_RESOLVIDO) {
            throw new IllegalStateException("Não é possível fechar um ticket que não foi resolvido.");
        }
        this.status = TicketStatus.FECHADO;
        this.fechadoEm = Instant.now();
        this.atualizadoEm = Instant.now();
    }
    public void atualizarTicket(String titulo, String descricao, TicketPrioridade prioridade) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.atualizadoEm = Instant.now();
    }

    public void aguardarCliente(){
        if(this.status != TicketStatus.EM_ANDAMENTO) {
            throw new IllegalStateException("Não é possível aguardar o cliente de um ticket que não está em andamento.");
        }
        this.status = TicketStatus.AGUARDANDO_CLIENTE;
        this.aguardandoClienteEm = Instant.now();
        this.atualizadoEm = Instant.now();
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

    
}