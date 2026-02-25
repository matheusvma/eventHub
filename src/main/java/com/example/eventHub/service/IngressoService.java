package com.example.eventHub.service;

import com.example.eventHub.domain.Evento;
import com.example.eventHub.domain.Ingresso;
import com.example.eventHub.domain.Participante;
import com.example.eventHub.repository.IngressoRepository;
import com.example.eventHub.validator.IngressoValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngressoService {

    private final IngressoRepository ingressoRepository;

    private final EventoService eventoService;

    private final ParticipanteService participanteService;

    private final IngressoValidator ingressoValidator;

    public IngressoService(IngressoRepository ingressoRepository,
                          EventoService eventoService,
                          ParticipanteService participanteService,
                          IngressoValidator ingressoValidator) {
        this.ingressoRepository = ingressoRepository;
        this.eventoService = eventoService;
        this.participanteService = participanteService;
        this.ingressoValidator = ingressoValidator;
    }

    /**
     * Simula a compra de um ingresso por um participante em um evento
     *
     * @param eventoId ID do evento
     * @param participanteId ID do participante
     * @return Ingresso criado ou null se falhar
     */
    public Ingresso comprarIngresso(Long eventoId, Long participanteId) {

        Evento evento = eventoService.getEventoById(eventoId);
        Participante participante = participanteService.getParticipanteById(participanteId);

        ingressoValidator.validaCompraIngresso(evento, participante, eventoId, participanteId);

        // Criar inscrição
        Ingresso ingresso = new Ingresso();
        ingresso.setEvento(evento);
        ingresso.setParticipante(participante);

        Ingresso ingressoResultado = ingressoRepository.save(ingresso);

        //Decrementa a capacidade do evento
        evento.setCapacidade(evento.getCapacidade() - 1);
        eventoService.updateEvento(eventoId, evento);

        return ingressoResultado;
    }

    /**
     * Lista todos os ingressos comprados por um participante
     *
     * @param participanteId ID do participante
     * @return Lista de ingressos do participante
     */
    public List<Ingresso> listarIngressosPorParticipante(Long participanteId) {
        return ingressoRepository.findByParticipanteId(participanteId);
    }

    /**
     * Lista todos os ingressos de um evento
     *
     * @param eventoId ID do evento
     * @return Lista de ingressos do evento
     */
    public List<Ingresso> listarIngressosPorEvento(Long eventoId) {
        return ingressoRepository.findByEventoId(eventoId);
    }
}

