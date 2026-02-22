package com.example.eventHub.controller;

import com.example.eventHub.domain.Event;
import com.example.eventHub.service.EventService;
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
@RequestMapping("/api/events")
@Tag(name = "Eventos", description = "API para gerenciamento de eventos")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    @Operation(summary = "Listar todos os eventos", description = "Retorna uma lista com todos os eventos cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de eventos retornada com sucesso")
    })
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar evento por ID", description = "Retorna um evento específico através do seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento encontrado"),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    })
    public ResponseEntity<Event> getEventById(
            @Parameter(description = "ID do evento", required = true) @PathVariable Long id) {
        Event event = eventService.getEventById(id);
        if (event != null) {
            return ResponseEntity.ok(event);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Operation(summary = "Criar evento", description = "Cria um novo evento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Evento criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<Event> createEvent(
            @Parameter(description = "Dados do evento a ser criado", required = true)
            @Valid @RequestBody Event event) {
        Event createdEvent = eventService.createEvent(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar evento", description = "Atualiza os dados de um evento existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    })
    public ResponseEntity<Event> updateEvent(
            @Parameter(description = "ID do evento", required = true) @PathVariable Long id,
            @Parameter(description = "Dados atualizados do evento", required = true)
            @Valid @RequestBody Event event) {
        Event updatedEvent = eventService.updateEvent(id, event);
        if (updatedEvent != null) {
            return ResponseEntity.ok(updatedEvent);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar evento", description = "Remove um evento do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Evento deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado")
    })
    public ResponseEntity<Void> deleteEvent(
            @Parameter(description = "ID do evento", required = true) @PathVariable Long id) {
        boolean deleted = eventService.deleteEvent(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}

