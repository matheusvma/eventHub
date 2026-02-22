package com.example.eventHub.exception;

import org.springframework.http.HttpStatus;

/**
 * Exceção lançada quando um evento atinge a capacidade máxima
 */
public class EventoLotadoException extends BusinessException {

    public EventoLotadoException(Long eventoId, int capacidade) {
        super(
                String.format("Evento com ID %d está lotado. Capacidade máxima: %d participantes", eventoId, capacidade),
                "EVENTO_LOTADO",
                HttpStatus.CONFLICT.value()
        );
    }

    public EventoLotadoException(String nomeEvento, int capacidade) {
        super(
                String.format("Evento '%s' está lotado. Capacidade máxima: %d participantes", nomeEvento, capacidade),
                "EVENTO_LOTADO",
                HttpStatus.CONFLICT.value()
        );
    }
}

