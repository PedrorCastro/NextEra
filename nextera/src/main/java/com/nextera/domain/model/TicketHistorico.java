package com.nextera.domain.model;

import java.time.Instant;
import java.util.UUID;

public class TicketHistorico {

    private final UUID id;
    private final UUID ticketId;
    private final TipoEventoTicket tipoEvento;
    private final String descricao;
    private final String valorAnterior;
    private final String valorAtual;
    private final UUID autorId;
    private final TipoAutor tipoAutor;
    private final Instant criadoEm;


    public TicketHistorico(
        UUID ticketId,
        TipoEventoTicket tipoEvento,
        String descricao,
        UUID autorId,
        TipoAutor tipoAutor
    ) {
        this(ticketId, tipoEvento, descricao, null, null, autorId, tipoAutor);
    }

    public TicketHistorico(
        UUID ticketId,
        TipoEventoTicket tipoEvento,
        String descricao,
        String valorAnterior,
        String valorAtual,
        UUID autorId,
        TipoAutor tipoAutor
    ) {
        validarObrigatorios(ticketId, tipoEvento, descricao, tipoAutor);
        if (autorId == null && tipoAutor != TipoAutor.SISTEMA) {
            throw new IllegalArgumentException("O autorId só pode ser nulo quando o autor for o sistema.");
        }

        this.id = UUID.randomUUID();
        this.ticketId = ticketId;
        this.tipoEvento = tipoEvento;
        this.descricao = descricao;
        this.valorAnterior = valorAnterior;
        this.valorAtual = valorAtual;
        this.autorId = autorId;
        this.tipoAutor = tipoAutor;
        this.criadoEm = Instant.now();
    }

    private void validarObrigatorios(
        UUID ticketId,
        TipoEventoTicket tipoEvento,
        String descricao,
        TipoAutor tipoAutor
    ) {
        if (ticketId == null) {
            throw new IllegalArgumentException("O ticketId não pode ser nulo.");
        }
        if (tipoEvento == null) {
            throw new IllegalArgumentException("O tipoEvento não pode ser nulo.");
        }
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("A descrição não pode ser nula ou vazia.");
        }
        if (tipoAutor == null) {
            throw new IllegalArgumentException("O tipoAutor não pode ser nulo.");
        }
    }

    public UUID getId() {
        return id;
    }


    public UUID getTicketId() {
        return ticketId;
    }


    public TipoEventoTicket getTipoEvento() {
        return tipoEvento;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getValorAnterior() {
        return valorAnterior;
    }


    public String getValorAtual() {
        return valorAtual;
    }


    public UUID getAutorId() {
        return autorId;
    }


    public TipoAutor getTipoAutor() {
        return tipoAutor;
    }


    public Instant getCriadoEm() {
        return criadoEm;
    }

}