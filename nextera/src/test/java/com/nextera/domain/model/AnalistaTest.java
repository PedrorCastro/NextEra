package com.nextera.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class AnalistaTest {

    @Test
    void deveCriarAnalistaComDadosValidos() {
        Analista analista = new Analista("Ana Souza", "11987654321", "ana@nextera.com");

        assertNotNull(analista.getId());
        assertEquals("Ana Souza", analista.getNome());
        assertEquals("ana@nextera.com", analista.getEmail());
        assertEquals("11987654321", analista.getTelefone());
        assertTrue(analista.isAtivo());
    }

    @Test
    void deveGerarUuidDiferenteParaCadaAnalista() {
        Analista primeiro = new Analista("Ana Souza", null, "ana@nextera.com");
        Analista segundo = new Analista("Ana Souza", null, "ana@nextera.com");

        assertNotEquals(primeiro.getId(), segundo.getId());
    }

    @Test
    void deveRejeitarNomeNuloOuVazio() {
        assertThrows(IllegalArgumentException.class,
            () -> new Analista(null, null, "ana@nextera.com"));
        assertThrows(IllegalArgumentException.class,
            () -> new Analista("", null, "ana@nextera.com"));
    }

    @Test
    void deveRejeitarEmailNuloVazioOuInvalido() {
        assertThrows(IllegalArgumentException.class,
            () -> new Analista("Ana Souza", null, null));
        assertThrows(IllegalArgumentException.class,
            () -> new Analista("Ana Souza", null, ""));
        assertThrows(IllegalArgumentException.class,
            () -> new Analista("Ana Souza", null, "email-invalido"));
    }

    @Test
    void devePermitirTelefoneNulo() {
        Analista analista = new Analista("Ana Souza", null, "ana@nextera.com");

        assertNull(analista.getTelefone());
    }

    @Test
    void deveDesativarAnalistaAtivo() {
        Analista analista = new Analista("Ana Souza", null, "ana@nextera.com");

        analista.desativar();

        assertFalse(analista.isAtivo());
    }

    @Test
    void deveAtivarAnalistaInativo() {
        Analista analista = new Analista("Ana Souza", null, "ana@nextera.com");
        analista.desativar();

        analista.ativar();

        assertTrue(analista.isAtivo());
    }

    @Test
    void deveRejeitarAtivacaoDuplicada() {
        Analista analista = new Analista("Ana Souza", null, "ana@nextera.com");

        assertThrows(IllegalStateException.class, analista::ativar);
    }

    @Test
    void deveRejeitarDesativacaoDuplicada() {
        Analista analista = new Analista("Ana Souza", null, "ana@nextera.com");
        analista.desativar();

        assertThrows(IllegalStateException.class, analista::desativar);
    }

    @Test
    void deveAlterarNomeEEmail() {
        Analista analista = new Analista("Ana Souza", null, "ana@nextera.com");

        analista.alterarNome("Maria Lima");
        analista.alterarEmail("maria@nextera.com");

        assertEquals("Maria Lima", analista.getNome());
        assertEquals("maria@nextera.com", analista.getEmail());
    }

    @Test
    void devePreservarDadosOriginaisQuandoAlteracaoForInvalida() {
        Analista analista = new Analista("Ana Souza", null, "ana@nextera.com");

        assertThrows(IllegalArgumentException.class, () -> analista.alterarNome(" "));
        assertThrows(IllegalArgumentException.class, () -> analista.alterarEmail("email-invalido"));

        assertEquals("Ana Souza", analista.getNome());
        assertEquals("ana@nextera.com", analista.getEmail());
    }
}