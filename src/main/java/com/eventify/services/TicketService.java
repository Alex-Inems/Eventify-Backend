package com.eventify.services;

import com.eventify.models.Event;
import com.eventify.models.Ticket;
import com.eventify.repositories.EventRepository;
import com.eventify.repositories.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;

    public TicketService(TicketRepository ticketRepository, EventRepository eventRepository) {
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    public Ticket createTicket(Ticket ticket) {
        if (ticket.getEvent() == null || ticket.getEvent().getId() == null) {
            throw new RuntimeException("Event ID is required!");
        }

        // Fetch the event from the database before saving the ticket
        Optional<Event> eventOptional = eventRepository.findById(ticket.getEvent().getId());
        if (eventOptional.isEmpty()) {
            throw new RuntimeException("Event not found!");
        }

        ticket.setEvent(eventOptional.get());
        return ticketRepository.save(ticket);
    }

    public Ticket updateTicket(Long id, Ticket ticketDetails) {
        return ticketRepository.findById(id).map(ticket -> {
            ticket.setTicketType(ticketDetails.getTicketType());
            ticket.setPrice(ticketDetails.getPrice());
            ticket.setQuantity(ticketDetails.getQuantity());

            if (ticketDetails.getEvent() != null && ticketDetails.getEvent().getId() != null) {
                Optional<Event> event = eventRepository.findById(ticketDetails.getEvent().getId());
                event.ifPresent(ticket::setEvent);
            }

            return ticketRepository.save(ticket);
        }).orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }
}
