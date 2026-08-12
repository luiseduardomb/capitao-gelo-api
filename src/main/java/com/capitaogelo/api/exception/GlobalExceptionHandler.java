package com.capitaogelo.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResponse tratarValidacao(MethodArgumentNotValidException exception) {

        List<ErroCampoResponse> erros = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(erro -> new ErroCampoResponse(
                        erro.getField(),
                        erro.getDefaultMessage()
                ))
                .toList();

        return new ErroResponse(
                400,
                "Dados inválidos",
                erros
        );
    }

    public record ErroResponse(
            int status,
            String mensagem,
            List<ErroCampoResponse> erros
    ) {
    }

    public record ErroCampoResponse(
            String campo,
            String mensagem
    ) {
    }

    @ExceptionHandler(EmailJaCadastradoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErroResponse tratarEmailJaCadastrado(EmailJaCadastradoException exception) {
        return new ErroResponse(
                409,
                exception.getMessage(),
                List.of()
        );
    }

    @ExceptionHandler(NaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErroResponse tratarNaoEncontrado(NaoEncontradoException exception) {

        return new ErroResponse(
                404,
                exception.getMessage(),
                List.of()
        );
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErroResponse tratarCredenciaisInvalidas(
            CredenciaisInvalidasException exception) {

        return new ErroResponse(
                401,
                exception.getMessage(),
                List.of()
        );
    }

    @ExceptionHandler(RegraNegocioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResponse tratarRegraNegocio(
            RegraNegocioException exception) {

        return new ErroResponse(
                400,
                exception.getMessage(),
                List.of()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErroResponse tratarErroInterno(Exception exception) {

        return new ErroResponse(
                500,
                "Ocorreu um erro interno no servidor.",
                List.of()
        );
    }
}