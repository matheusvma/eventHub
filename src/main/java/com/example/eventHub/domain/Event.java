package com.example.eventHub.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "events")
@Schema(description = "Entidade que representa um evento")
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do evento", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "O nome do evento não pode ser vazio")
    @Column(nullable = false)
    @Schema(description = "Nome do evento", example = "Festival de Música 2026")
    private String nome;

    @NotNull(message = "A data do evento é obrigatória")
    @FutureOrPresent(message = "A data do evento não pode ser no passado")
    @Column(nullable = false)
    @Schema(description = "Data e hora do evento", example = "2026-12-31T20:00:00")
    private LocalDateTime data;

    @NotBlank(message = "O local do evento não pode ser vazio")
    @Column(nullable = false)
    @Schema(description = "Local onde o evento será realizado", example = "Estádio Municipal")
    private String local;

    @NotNull(message = "A capacidade do evento é obrigatória")
    @Positive(message = "A capacidade deve ser um número positivo")
    @Column(nullable = false)
    @Schema(description = "Capacidade máxima de participantes", example = "5000")
    private Integer capacidade;
}

