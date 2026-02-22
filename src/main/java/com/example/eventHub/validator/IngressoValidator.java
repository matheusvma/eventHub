package com.example.eventHub.validator;

import com.example.eventHub.domain.Evento;
import com.example.eventHub.domain.Participante;
import com.example.eventHub.exception.EventoLotadoException;
import com.example.eventHub.exception.EventoNaoEncontradoException;
import com.example.eventHub.exception.ParticipanteJaInscritoException;
import com.example.eventHub.exception.ParticipanteNaoEncontradoException;
import com.example.eventHub.repository.IngressoRepository;
import org.springframework.stereotype.Component;

@Component
public class IngressoValidator {

    private IngressoRepository ingressoRepository;

    public IngressoValidator(IngressoRepository ingressoRepository) {
        this.ingressoRepository = ingressoRepository;
    }

    /**
     * Valida a compra de um ingresso
     * Validações:
     * - Evento deve existir
     * - Participante deve existir
     * - Participante não pode estar já inscrito
     * - Capacidade do evento não pode estar completa
     *
     * @param evento Evento para inscrição
     * @param participante Participante para inscrição
     * @param eventoId ID do evento
     * @param participanteId ID do participante
     * @throws EventoNaoEncontradoException se evento não existir
     * @throws ParticipanteNaoEncontradoException se participante não existir
     * @throws ParticipanteJaInscritoException se participante já está inscrito
     * @throws EventoLotadoException se evento está lotado
     */
    public void validaCompraIngresso(Evento evento, Participante participante, Long eventoId, Long participanteId) {

        if (evento == null) {
            throw new EventoNaoEncontradoException(eventoId);
        }

        if (participante == null) {
            throw new ParticipanteNaoEncontradoException(participanteId);
        }

        if (ingressoRepository.existsByEventoIdAndParticipanteId(eventoId, participanteId)) {
            throw new ParticipanteJaInscritoException(participanteId, eventoId);
        }

        long currentParticipantes = ingressoRepository.countByEventoId(eventoId);
        if (currentParticipantes >= evento.getCapacidade()) {
            throw new EventoLotadoException(eventoId, evento.getCapacidade());
        }
    }
}

