package net.pkhapps.mvvm4vaadin.demo.ui.model;

import net.pkhapps.mvvm4vaadin.demo.data.Ticket;
import net.pkhapps.mvvm4vaadin.demo.data.TicketType;
import net.pkhapps.mvvm4vaadin.demo.service.TicketService;
import net.pkhapps.mvvm4vaadin.model.DefaultObservableList;
import net.pkhapps.mvvm4vaadin.model.DefaultObservableValue;
import net.pkhapps.mvvm4vaadin.model.ObservableList;
import net.pkhapps.mvvm4vaadin.model.ObservableValue;

import java.io.Serializable;

import static net.pkhapps.mvvm4vaadin.model.ModelFactory.observableList;
import static net.pkhapps.mvvm4vaadin.model.ModelFactory.observableValue;

public class TicketListModel implements Serializable {

    private final TicketService ticketService;
    private final DefaultObservableList<TicketListItemModel> tickets = observableList();
    private final DefaultObservableValue<Long> selectedTicketId = observableValue();

    public TicketListModel(TicketService ticketService) {
        this.ticketService = ticketService;
        refresh();
    }

    public ObservableList<TicketListItemModel> tickets() {
        return tickets;
    }

    public ObservableValue<Long> selectedTicketId() {
        return selectedTicketId;
    }

    void ticketRefreshed(Ticket ticket) {
        tickets.stream().filter(model -> model.getTicketId().equals(ticket.getId())).forEach(model -> model.populate(ticket));
    }

    public void refresh() {
        tickets.setItems(ticketService.getTickets().stream()
                .map(t -> new TicketListItemModel(this, t)));
    }

    public void addTicket(TicketType type, String summary, String description) {
        var ticket = ticketService.addTicket(type, summary, description);
        tickets.add(new TicketListItemModel(this, ticket));
    }

    public void deleteTicket(Long ticketId) {
        ticketService.deleteTicket(ticketId);
        tickets.removeIf(model -> model.getTicketId().equals(ticketId));
        if (selectedTicketId.isEqualTo(ticketId)) {
            selectedTicketId.setValue(null);
        }
    }

    public void selectTicket(Long ticketId) {
        selectedTicketId.setValue(ticketId);
    }
}
