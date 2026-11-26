package com.nextera.domain.model;
import java.time.Instant;
import java.util.UUID;

@Model
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

    public Ticket(UUID id, String titulo, String descricao, TicketStatus status, TicketPrioridade prioridade, UUID clienteId, UUID analistaId, Instant criadoEm, Instant atualizadoEm, Instant fechadoEm) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = status;
        this.prioridade = prioridade;
        this.clienteId = clienteId;
        this.analistaId = analistaId;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
        this.fechadoEm = fechadoEm;
    }


    //Getters and Setters
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
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
    public void setPrioridade(TicketPrioridade prioridade) {
        this.prioridade = prioridade;
    }
    public UUID getClienteId() {
        return clienteId;
    }
    public void setClienteId(UUID clienteId) {
        this.clienteId = clienteId;
    }
    public UUID getAnalistaId() {
        return analistaId;
    }
    public void setAnalistaId(UUID analistaId) {
        this.analistaId = analistaId;
    }
    public Instant getCriadoEm() {
        return criadoEm;
    }
    public void setCriadoEm(Instant criadoEm) {
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


    

    public UUID atribuirAnalista(UUID analistaId) {
        this.analistaId = analistaId;
        return this.analistaId;
    }

    public Instant iniciarAtendimento() {
        this.status = TicketStatus.EM_ANDAMENTO;
        this.atualizadoEm = Instant.now();
        return this.atualizadoEm;
    }

    public Instant fecharTicket() {
        this.status = TicketStatus.FECHADO;
        this.fechadoEm = Instant.now();
        return this.fechadoEm;
    }
    public void atualizarTicket(String titulo, String descricao, TicketPrioridade prioridade) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.atualizadoEm = Instant.now();
        return this.atualizadoEm;
    }

    public Instant aguardarCliente(){
        this.status = TicketStatus.AGUARDANDO_CLIENTE;
        this.atualizadoEm = Instant.now();
        return this.atualizadoEm;
    }

    public Instant aguardarAnalista(){
        this.status = TicketStatus.AGUARDANDO_ANALISTA;
        this.atualizadoEm = Instant.now();
        return this.atualizadoEm;
    }
    public Instant resolver(){
        this.status = TicketStatus.RESOLVIDO;
        this.atualizadoEm = Instant.now();
        return this.atualizadoEm;
    }
    public TicketPrioridade atualizarPrioridade(TicketPrioridade prioridade) {
        this.prioridade = prioridade;
        this.atualizadoEm = Instant.now();
        return this.prioridade;
    }
    
}