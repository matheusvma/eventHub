package com.example.eventHub.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "DTO para resposta de ingresso")
public class IngressoResponse {

    @Schema(description = "ID do ingresso", example = "1")
    private Long id;

    @Schema(description = "Dados do evento")
    private EventoDTO evento;

    @Schema(description = "Dados do participante")
    private ParticipanteDTO participante;

    public IngressoResponse() {
    }

    public IngressoResponse(Long id, EventoDTO evento, ParticipanteDTO participante) {
        this.id = id;
        this.evento = evento;
        this.participante = participante;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EventoDTO getEvento() {
        return evento;
    }

    public void setEvento(EventoDTO evento) {
        this.evento = evento;
    }

    public ParticipanteDTO getParticipante() {
        return participante;
    }

    public void setParticipante(ParticipanteDTO participante) {
        this.participante = participante;
    }

    @Schema(description = "DTO simplificado do evento")
    public static class EventoDTO {
        @Schema(description = "ID do evento", example = "1")
        private Long id;

        @Schema(description = "Nome do evento", example = "Tech Conference 2026")
        private String nome;

        @Schema(description = "Data e hora do evento", example = "2026-12-31T20:00:00")
        private LocalDateTime data;

        @Schema(description = "Local do evento", example = "São Paulo")
        private String local;

        @Schema(description = "Capacidade restante do evento", example = "99")
        private Integer capacidade;

        public EventoDTO() {
        }

        public EventoDTO(Long id, String nome, LocalDateTime data, String local, Integer capacidade) {
            this.id = id;
            this.nome = nome;
            this.data = data;
            this.local = local;
            this.capacidade = capacidade;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public LocalDateTime getData() {
            return data;
        }

        public void setData(LocalDateTime data) {
            this.data = data;
        }

        public String getLocal() {
            return local;
        }

        public void setLocal(String local) {
            this.local = local;
        }

        public Integer getCapacidade() {
            return capacidade;
        }

        public void setCapacidade(Integer capacidade) {
            this.capacidade = capacidade;
        }
    }

    @Schema(description = "DTO simplificado do participante")
    public static class ParticipanteDTO {
        @Schema(description = "ID do participante", example = "1")
        private Long id;

        @Schema(description = "Nome do participante", example = "João Silva")
        private String nome;

        @Schema(description = "Email do participante", example = "joao@example.com")
        private String email;

        public ParticipanteDTO() {
        }

        public ParticipanteDTO(Long id, String nome, String email) {
            this.id = id;
            this.nome = nome;
            this.email = email;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}

