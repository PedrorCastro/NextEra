package com.nextera.domain.model;

import java.time.Instant;
import java.util.UUID;


public class Diagnostico {
    private final UUID id;
    private final UUID clienteId;
    private final String descricaoProblema;
    private final String moduloSistema;
    private final String codigoErro;
    private DiagnosticoStatus status;
    private ResultadoDiagnostico resultado;
    private String detalhes;
    private UUID ticketId;
    private final Instant criadoEm;
    private Instant concluidoEm;


    public Diagnostico(
        UUID clienteId,
        String descricaoProblema,
        String moduloSistema,
        String codigoErro
    ) {
        this.id = UUID.randomUUID();
        this.clienteId = validaClienteId(clienteId);
        this.descricaoProblema = validarTextoObrigatorio(
            descricaoProblema, "A descricaoProblema do diagnóstico não pode ser nula ou vazia."
        );
        this.moduloSistema = validarTextoObrigatorio(
            moduloSistema, "O moduloSistema do diagnóstico não pode ser nulo ou vazio."
        );
        this.codigoErro = codigoErro;
        this.status = DiagnosticoStatus.RECEBIDO;
        this.criadoEm = Instant.now();
    }

    public void iniciarAnalise(){
        if(this.status != DiagnosticoStatus.RECEBIDO) {
            throw new IllegalStateException("O diagnóstico só pode ser iniciado se estiver no status RECEBIDO.");
        }
        this.status = DiagnosticoStatus.EM_ANALISE;
    }

    public void concluirDiagnostico(ResultadoDiagnostico resultado){
        if(this.status != DiagnosticoStatus.EM_ANALISE) {
            throw new IllegalStateException("O diagnóstico só pode ser concluído se estiver no status EM_ANALISE.");
        }
        if (resultado == null) {
            throw new IllegalArgumentException("O resultado do diagnóstico não pode ser nulo.");
        }
        this.resultado = resultado;
        this.status = DiagnosticoStatus.CONCLUIDO;
        this.concluidoEm = Instant.now();
    }

    public void registrarFalha(String detalhes){
        if(this.status != DiagnosticoStatus.EM_ANALISE) {
            throw new IllegalStateException("O diagnóstico só pode registrar falha se estiver no status EM_ANALISE.");
        }
        validarTextoObrigatorio(detalhes, "Os detalhes da falha não podem ser nulos ou vazios.");
        this.status = DiagnosticoStatus.FALHA;
        this.detalhes = detalhes;
        this.concluidoEm = Instant.now();
    }

    public void vincularTicket(UUID ticketId) {
        if (this.status != DiagnosticoStatus.CONCLUIDO) {
            throw new IllegalStateException("O diagnóstico só pode vincular um ticket se estiver no status CONCLUIDO.");
        }
        if (this.ticketId != null) {
            throw new IllegalStateException("O diagnóstico já possui um ticket vinculado.");
        }
        if (ticketId == null) {
            throw new IllegalArgumentException("O ticketId não pode ser nulo.");
        }
        if (this.resultado == null) {
            throw new IllegalStateException("O diagnóstico não pode vincular um ticket se o resultado for nulo.");
        }
        if (this.resultado == ResultadoDiagnostico.RESOLVIDO_AUTOMATICAMENTE) {
            throw new IllegalStateException("O diagnóstico não pode vincular um ticket se o resultado for RESOLVIDO_AUTOMATICAMENTE.");
        }

        this.ticketId = ticketId;
    }

    public DiagnosticoStatus getStatus() {
        return status;
    }

    public Instant getConcluidoEm() {
        return concluidoEm;
    }
    public UUID getId() {
        return id;
    }
    
    public UUID getClienteId() {
        return clienteId;
    }

    public String getDescricaoProblema() {
        return descricaoProblema;
    }

    public String getModuloSistema() {
        return moduloSistema;
    }

    public String getCodigoErro() {
        return codigoErro;
    }

    public ResultadoDiagnostico getResultado() {
        return resultado;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public UUID getTicketId() {
        return ticketId;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }


    private UUID validaClienteId(UUID clienteId) {
        if (clienteId == null) {
            throw new IllegalArgumentException("O clienteId do diagnóstico não pode ser nulo.");
        }
        return clienteId;
    }

    private String validarTextoObrigatorio(String valor, String mensagem) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensagem);
        }
        return valor;
    }

  


}