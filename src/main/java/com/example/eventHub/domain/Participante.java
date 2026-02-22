package com.example.eventHub.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "participantes")
@Schema(description = "Entidade que representa um participante de um evento")
@AllArgsConstructor
@NoArgsConstructor
public class Participante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do participante", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "O nome do participante não pode ser vazio")
    @Column(nullable = false)
    @Schema(description = "Nome do participante", example = "João Silva", required = true)
    private String nome;

    @NotBlank(message = "O email do participante não pode ser vazio")
    @Email(message = "O email deve ser válido")
    @Column(nullable = false, unique = true)
    @Schema(description = "Email do participante", example = "joao@example.com", required = true)
    private String email;

    @OneToMany(mappedBy = "participante", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Schema(description = "Lista de eventos nos quais o participante está inscrito", accessMode = Schema.AccessMode.READ_ONLY)
    private List<Ingresso> eventos;
}

