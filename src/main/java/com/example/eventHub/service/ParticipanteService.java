package com.example.eventHub.service;

import com.example.eventHub.domain.Participante;
import com.example.eventHub.repository.ParticipanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipanteService {

    @Autowired
    private ParticipanteRepository participanteRepository;

    /**
     * Obtém todos os participantes
     * @return Lista de participantes
     */
    public List<Participante> getAllParticipantes() {
        return participanteRepository.findAll();
    }

    /**
     * Obtém um participante por ID
     * @param id ID do participante
     * @return Participante ou null se não encontrado
     */
    public Participante getParticipanteById(Long id) {
        return participanteRepository.findById(id).orElse(null);
    }

    /**
     * Obtém um participante por email
     * @param email Email do participante
     * @return Participante ou null se não encontrado
     */
    public Participante getParticipanteByEmail(String email) {
        return participanteRepository.findByEmail(email).orElse(null);
    }

    /**
     * Cria um novo participante
     * @param participante Participante a ser criado
     * @return Participante criado
     */
    public Participante createParticipante(Participante participante) {
        return participanteRepository.save(participante);
    }

    /**
     * Atualiza um participante existente
     * @param id ID do participante
     * @param participante Dados atualizados
     * @return Participante atualizado ou null se não encontrado
     */
    public Participante updateParticipante(Long id, Participante participante) {
        if (participanteRepository.existsById(id)) {
            participante.setId(id);
            return participanteRepository.save(participante);
        }
        return null;
    }

    /**
     * Deleta um participante
     * @param id ID do participante
     * @return true se deletado, false se não encontrado
     */
    public boolean deleteParticipante(Long id) {
        if (participanteRepository.existsById(id)) {
            participanteRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

