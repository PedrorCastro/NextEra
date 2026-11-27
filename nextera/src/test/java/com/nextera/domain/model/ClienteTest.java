package com.nextera.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ClienteTest {

	@Test
	public void deveCriarClienteComDadosValidos() {
		String nome = "João da Silva";
		String telefone = "123456789";
		String email = "teste@teste.com";

		Cliente cliente = new Cliente(nome, telefone, email);

		assertNotNull(cliente.getId());
		assertEquals(nome, cliente.getNome());
		assertEquals(telefone, cliente.getTelefone());
		assertEquals(email, cliente.getEmail());
	}

	@Test
	public void deveLancarExcecaoQuandoNomeForInvalido() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Cliente("", "123456789", "teste@teste.com");
		});
	}

	@Test
	public void deveRejeitarNomeNuloOuComEspacos() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Cliente(null, "123456789", "teste@teste.com");
		});
		assertThrows(IllegalArgumentException.class, () -> {
			new Cliente("   ", "123456789", "teste@teste.com");
		});
	}

	@Test
	public void deveLancarExcecaoQuandoEmailForInvalido() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Cliente("João da Silva", "123456789", "email-invalido.com");
		});
	}

	@Test
	public void deveRejeitarEmailNuloOuVazio() {
		assertThrows(IllegalArgumentException.class, () -> {
			new Cliente("João da Silva", "123456789", null);
		});
		assertThrows(IllegalArgumentException.class, () -> {
			new Cliente("João da Silva", "123456789", "");
		});
		assertThrows(IllegalArgumentException.class, () -> {
			new Cliente("João da Silva", "123456789", "   ");
		});
	}

	@Test
	public void deveAlterarDadosDoClienteComSucesso() {
		Cliente cliente = new Cliente("João da Silva", "123456789", "teste@teste.com");

		cliente.alterarNome("Maria Souza");
		cliente.alterarTelefone("987654321");
		cliente.alterarEmail("maria@teste.com");

		assertEquals("Maria Souza", cliente.getNome());
		assertEquals("987654321", cliente.getTelefone());
		assertEquals("maria@teste.com", cliente.getEmail());
	}

	@Test
	public void deveManterEmailAnteriorQuandoNovaAlteracaoForInvalida() {
		Cliente cliente = new Cliente("João da Silva", "123456789", "teste@teste.com");

		assertThrows(IllegalArgumentException.class, () -> cliente.alterarEmail("email-invalido.com"));

		assertEquals("teste@teste.com", cliente.getEmail());
	}
}