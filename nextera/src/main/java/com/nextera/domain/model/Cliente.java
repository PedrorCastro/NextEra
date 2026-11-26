package com.nextera.domain.model;
import java.util.UUID;

public class Cliente {

	private UUID id;

	private String nome;

	private String email;

	private String telefone;


	public Cliente(String nome, String telefone) {
		this.id = UUID.randomUUID();
        validarNome(nome);
        this.nome = nome;
		this.email = null;
		this.telefone = telefone;
	}

	public UUID getId() {
		return id;
	}


	public String getNome() {
		return nome;
	}


	public String getEmail() {
		return email;
	}


	public String getTelefone() {
		return telefone;
	}


    public void alterarNome(String nome) {
        validarNome(nome);
        this.nome = nome;
    }

    public void alterarEmail(String email) {
        if(email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email não pode ser nulo ou vazio");
        }
        this.email = email;
    }

    public void alterarTelefone(String telefone) {
        this.telefone = telefone;
    }

    private void validarNome(String nome) {
    if (nome == null || nome.isBlank()) {
        throw new IllegalArgumentException("Nome não pode ser nulo ou vazio.");
    }
}
}
