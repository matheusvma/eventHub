package com.example.eventHub.exception;

import org.springframework.http.HttpStatus;

/**
 * Exceção lançada quando um evento não é encontrado
 */
public class EventoNaoEncontradoException extends BusinessException {

    public EventoNaoEncontradoException(Long eventoId) {
        super(
                String.format("Evento com ID %d não foi encontrado", eventoId),
                "EVENTO_NAO_ENCONTRADO",
                HttpStatus.NOT_FOUND.value()
        );
    }

    public EventoNaoEncontradoException(String mensagem) {
        super(
                mensagem,
                "EVENTO_NAO_ENCONTRADO",
                HttpStatus.NOT_FOUND.value()
        );
    }
}

