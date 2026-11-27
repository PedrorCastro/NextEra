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
        this.clienteId = clienteId;
        this.descricaoProblema = descricaoProblema;
        this.moduloSistema = moduloSistema;
        this.codigoErro = codigoErro;
        this.status = DiagnosticoStatus.RECEBIDO;
        this.resultado = null;
        this.detalhes = null;
        this.ticketId = null;
        this.criadoEm = Instant.now();
        this.concluidoEm = null;
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
        this.status = DiagnosticoStatus.FALHA;
        this.detalhes = detalhes;
        this.concluidoEm = Instant.now();
    }

    public void vincularTicket(UUID ticketId){
        if(this.status != DiagnosticoStatus.CONCLUIDO) {
            throw new IllegalStateException("O diagnóstico só pode vincular um ticket se estiver no status CONCLUIDO.");
        }
        if(ticketId == null) {
            throw new IllegalArgumentException("O ticketId não pode ser nulo.");
        }
        if(this.resultado == ResultadoDiagnostico.RESOLVIDO_AUTOMATICAMENTE) {
            throw new IllegalStateException("O diagnóstico não pode vincular um ticket se o resultado for RESOLVIDO_AUTOMATICAMENTE.");
        }
        
        if(this.resultado == null){
            throw new IllegalStateException("O diagnóstico não pode vincular um ticket se o resultado for nulo.");
        }

        this.ticketId = ticketId;
    }

    public DiagnosticoStatus getStatus() {
        return status;
    }

    public Instant getConcluidoEm() {
        return concluidoEm;
    }

}