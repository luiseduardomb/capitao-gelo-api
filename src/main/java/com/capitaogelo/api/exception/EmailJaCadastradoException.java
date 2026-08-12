package com.capitaogelo.api.exception;

public class EmailJaCadastradoException extends RuntimeException {

    public EmailJaCadastradoException(String mensagem) {
        super(mensagem);
    }
}