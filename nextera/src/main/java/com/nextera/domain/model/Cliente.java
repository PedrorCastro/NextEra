package com.nextera.domain.model;
import java.util.UUID;
import java.util.regex.Pattern;


public class Cliente {

	private UUID id;

	private String nome;

	private String email;

	private String telefone;

	private static final Pattern EMAIL_PATTERN = Pattern.compile(
		"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
	);


	public Cliente(String nome, String telefone, String email) {
		this.id = UUID.randomUUID();
        validarNome(nome);
        this.nome = nome;
		validarEmail(email);
		this.email = email;
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
        validarEmail(email);
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
	private void validarEmail(String email) {
		if (email == null || email.isBlank()) {
			throw new IllegalArgumentException("Email não pode ser nulo ou vazio.");
		}

		if (!EMAIL_PATTERN.matcher(email).matches()) {
			throw new IllegalArgumentException("Formato de email inválido.");
		}
	}
}
