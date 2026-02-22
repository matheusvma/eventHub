package com.example.eventHub.exception;

import org.springframework.http.HttpStatus;

/**
 * Exceção lançada quando um participante não é encontrado
 */
public class ParticipanteNaoEncontradoException extends BusinessException {

    public ParticipanteNaoEncontradoException(Long participanteId) {
        super(
                String.format("Participante com ID %d não foi encontrado", participanteId),
                "PARTICIPANTE_NAO_ENCONTRADO",
                HttpStatus.NOT_FOUND.value()
        );
    }

    public ParticipanteNaoEncontradoException(String mensagem) {
        super(
                mensagem,
                "PARTICIPANTE_NAO_ENCONTRADO",
                HttpStatus.NOT_FOUND.value()
        );
    }
}

