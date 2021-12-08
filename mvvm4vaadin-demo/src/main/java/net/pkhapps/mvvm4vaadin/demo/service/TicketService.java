package net.pkhapps.mvvm4vaadin.demo.service;

import net.pkhapps.mvvm4vaadin.demo.data.Ticket;
import net.pkhapps.mvvm4vaadin.demo.data.TicketRepository;
import net.pkhapps.mvvm4vaadin.demo.data.TicketState;
import net.pkhapps.mvvm4vaadin.demo.data.TicketType;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TicketService {

    private final TicketRepository ticketRepository;

    TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Ticket addTicket(TicketType type, String summary, String description) {
        return ticketRepository.saveAndFlush(new Ticket(type, summary, description));
    }

    public void deleteTicket(Long ticketId) {
        ticketRepository.deleteById(ticketId);
    }

    public Optional<Ticket> getTicket(Long ticketId) {
        return ticketRepository.findById(ticketId);
    }

    public List<Ticket> getTickets() {
        return ticketRepository.findAll();
    }

    public Ticket updateSummary(Long ticketId, String summary) {
        var ticket = requireTicket(ticketId);
        ticket.setSummary(summary);
        return ticketRepository.saveAndFlush(ticket);
    }

    public Ticket updateDescription(Long ticketId, String description) {
        var ticket = requireTicket(ticketId);
        ticket.setDescription(description);
        return ticketRepository.saveAndFlush(ticket);
    }

    public Ticket updateType(Long ticketId, TicketType type) {
        var ticket = requireTicket(ticketId);
        ticket.setType(type);
        return ticketRepository.saveAndFlush(ticket);
    }

    public Ticket updateState(Long ticketId, TicketState state) {
        var ticket = requireTicket(ticketId);
        ticket.setState(state);
        return ticketRepository.saveAndFlush(ticket);
    }

    private Ticket requireTicket(Long ticketId) {
        return ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IncorrectResultSizeDataAccessException(1, 0));
    }
}
