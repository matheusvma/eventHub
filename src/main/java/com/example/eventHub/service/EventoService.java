package com.example.eventHub.service;

import com.example.eventHub.domain.Evento;
import com.example.eventHub.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    /**
     * Obtém todos os eventos
     * @return Lista de eventos
     */
    public List<Evento> getAllEventos() {
        return eventoRepository.findAll();
    }

    /**
     * Obtém um evento por ID
     * @param id ID do evento
     * @return Evento ou null se não encontrado
     */
    public Evento getEventoById(Long id) {
        return eventoRepository.findById(id).orElse(null);
    }

    /**
     * Cria um novo evento
     * @param evento Evento a ser criado
     * @return Evento criado
     */
    public Evento createEvento(Evento evento) {
        return eventoRepository.save(evento);
    }

    /**
     * Atualiza um evento existente
     * @param id ID do evento
     * @param evento Dados atualizados
     * @return Evento atualizado ou null se não encontrado
     */
    public Evento updateEvento(Long id, Evento evento) {
        if (eventoRepository.existsById(id)) {
            evento.setId(id);
            return eventoRepository.save(evento);
        }
        return null;
    }

    /**
     * Deleta um evento
     * @param id ID do evento
     * @return true se deletado, false se não encontrado
     */
    public boolean deleteEvento(Long id) {
        if (eventoRepository.existsById(id)) {
            eventoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

