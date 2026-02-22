package com.example.eventHub.service;

import com.example.eventHub.domain.Event;
import com.example.eventHub.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        Optional<Event> event = eventRepository.findById(id);
        return event.orElse(null);
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public Event updateEvent(Long id, Event eventDetails) {
        Optional<Event> existingEvent = eventRepository.findById(id);
        if (existingEvent.isPresent()) {
            Event event = existingEvent.get();

            if (eventDetails.getNome() != null) {
                event.setNome(eventDetails.getNome());
            }
            if (eventDetails.getData() != null) {
                event.setData(eventDetails.getData());
            }
            if (eventDetails.getLocal() != null) {
                event.setLocal(eventDetails.getLocal());
            }
            if (eventDetails.getCapacidade() != null) {
                event.setCapacidade(eventDetails.getCapacidade());
            }

            return eventRepository.save(event);
        }
        return null;
    }

    public boolean deleteEvent(Long id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

