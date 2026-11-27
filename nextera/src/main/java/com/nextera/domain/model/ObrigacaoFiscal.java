package com.nextera.domain.model;

import java.time.LocalDate;

public class ObrigacaoFiscal {

    private final String descricao;
    private final LocalDate dataVencimento;
    private final String tipoObrigacao;

    public ObrigacaoFiscal(String descricao, LocalDate dataVencimento, String tipoObrigacao) {
        validarObrigacao(descricao, dataVencimento, tipoObrigacao);
        this.descricao = descricao;
        this.dataVencimento = dataVencimento;
        this.tipoObrigacao = tipoObrigacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public String getTipoObrigacao() {
        return tipoObrigacao;
    }

    private void validarObrigacao(String descricao, LocalDate dataVencimento, String tipoObrigacao) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Descrição da obrigação fiscal não pode ser nula ou vazia.");
        }
        if (dataVencimento == null) {
            throw new IllegalArgumentException("Data de vencimento da obrigação fiscal não pode ser nula.");
        }
        if (tipoObrigacao == null || tipoObrigacao.isBlank()) {
            throw new IllegalArgumentException("Tipo da obrigação fiscal não pode ser nulo ou vazio.");
        }
    }
}