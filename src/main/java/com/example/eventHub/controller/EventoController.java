package com.example.eventHub.controller;

import com.example.eventHub.domain.Evento;
import com.example.eventHub.service.EventoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventos")
@Tag(name = "Eventos", description = "API para gerenciamento de eventos")
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @GetMapping
    @Operation(summary = "Listar todos os eventos", description = "Retorna uma lista com todos os eventos cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de eventos retornada com sucesso")
    })
    public ResponseEntity<List<Evento>> getAllEventos() {
        List<Evento> eventos = eventoService.getAllEventos();
        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar evento por ID", description = "Retorna um evento específico através do seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento encontrado"),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    })
    public ResponseEntity<Evento> getEventoById(
            @Parameter(description = "ID do evento", required = true) @PathVariable Long id) {
        Evento evento = eventoService.getEventoById(id);
        if (evento != null) {
            return ResponseEntity.ok(evento);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Criar evento", description = "Cria um novo evento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Evento criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<Evento> createEvento(
            @Parameter(description = "Dados do evento a ser criado", required = true)
            @Valid @RequestBody Evento evento) {
        Evento createdEvento = eventoService.createEvento(evento);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvento);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar evento", description = "Atualiza os dados de um evento existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    })
    public ResponseEntity<Evento> updateEvento(
            @Parameter(description = "ID do evento", required = true) @PathVariable Long id,
            @Parameter(description = "Dados atualizados do evento", required = true)
            @Valid @RequestBody Evento evento) {
        Evento updatedEvento = eventoService.updateEvento(id, evento);
        if (updatedEvento != null) {
            return ResponseEntity.ok(updatedEvento);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar evento", description = "Remove um evento do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Evento deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    })
    public ResponseEntity<Void> deleteEvento(
            @Parameter(description = "ID do evento", required = true) @PathVariable Long id) {
        boolean deleted = eventoService.deleteEvento(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

