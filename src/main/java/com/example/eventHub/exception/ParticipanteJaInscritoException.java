package com.example.eventHub.exception;

import org.springframework.http.HttpStatus;

/**
 * Exceção lançada quando um participante já está inscrito em um evento
 */
public class ParticipanteJaInscritoException extends BusinessException {

    public ParticipanteJaInscritoException(Long participanteId, Long eventoId) {
        super(
                String.format("Participante com ID %d já está inscrito no evento com ID %d", participanteId, eventoId),
                "PARTICIPANTE_JA_INSCRITO",
                HttpStatus.CONFLICT.value()
        );
    }

    public ParticipanteJaInscritoException(String nomeParticipante, String nomeEvento) {
        super(
                String.format("Participante '%s' já está inscrito no evento '%s'", nomeParticipante, nomeEvento),
                "PARTICIPANTE_JA_INSCRITO",
                HttpStatus.CONFLICT.value()
        );
    }
}

