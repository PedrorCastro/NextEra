package com.nextera.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class ObrigacaoFiscalTest {

	@Test
	public void deveCriarObrigacaoFiscalComDadosValidos() {
		String descricao = "Pagamento de impostos";
		LocalDate dataVencimento = LocalDate.of(2026, 12, 31);
		String tipoObrigacao = "Imposto";

		ObrigacaoFiscal obrigacao = new ObrigacaoFiscal(descricao, dataVencimento, tipoObrigacao);
		assertAll(
			() -> assertEquals(descricao, obrigacao.getDescricao()),
			() -> assertEquals(dataVencimento, obrigacao.getDataVencimento()),
			() -> assertEquals(tipoObrigacao, obrigacao.getTipoObrigacao())
		);
	}

	@Test
	public void deveRejeitarDescricaoNula() {
		assertThrows(IllegalArgumentException.class, () -> {
			new ObrigacaoFiscal(null, LocalDate.now(), "Imposto");
		});
	}

	@Test
	public void deveRejeitarDescricaoVaziaOuComEspacos() {
		assertThrows(IllegalArgumentException.class, () -> {
			new ObrigacaoFiscal("", LocalDate.now(), "Imposto");
		});
		assertThrows(IllegalArgumentException.class, () -> {
			new ObrigacaoFiscal("   ", LocalDate.now(), "Imposto");
		});
	}

	@Test
	public void deveRejeitarDataDeVencimentoNula() {
		assertThrows(IllegalArgumentException.class, () -> {
			new ObrigacaoFiscal("Pagamento de impostos", null, "Imposto");
		});
	}

	@Test
	public void deveRejeitarTipoObrigacaoNulo() {
		assertThrows(IllegalArgumentException.class, () -> {
			new ObrigacaoFiscal("Pagamento de impostos", LocalDate.now(), null);
		});
	}

	@Test
	public void deveRejeitarTipoObrigacaoVazioOuComEspacos() {
		assertThrows(IllegalArgumentException.class, () -> {
			new ObrigacaoFiscal("Pagamento de impostos", LocalDate.now(), "");
		});
		assertThrows(IllegalArgumentException.class, () -> {
			new ObrigacaoFiscal("Pagamento de impostos", LocalDate.now(), "   ");
		});
	}
}
