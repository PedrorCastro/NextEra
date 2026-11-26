package com.nextera.domain.model;

import java.time.LocalDate;

public class ObrigacaoFiscal {

    private String descricao;
    private LocalDate dataVencimento;
    private String tipoObrigacao;

    public ObrigacaoFiscal(String descricao, LocalDate dataVencimento, String tipoObrigacao) {
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
}