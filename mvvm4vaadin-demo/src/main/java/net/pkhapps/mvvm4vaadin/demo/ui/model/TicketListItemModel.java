package net.pkhapps.mvvm4vaadin.demo.ui.model;

import net.pkhapps.mvvm4vaadin.demo.data.Ticket;
import net.pkhapps.mvvm4vaadin.demo.data.TicketType;
import net.pkhapps.mvvm4vaadin.model.DefaultObservableValue;
import net.pkhapps.mvvm4vaadin.model.ObservableValue;

import java.io.Serializable;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

import static java.util.Objects.requireNonNull;
import static net.pkhapps.mvvm4vaadin.model.ModelFactory.observableValue;

public class TicketListItemModel implements Serializable {

    private final TicketListModel parentModel;
    private final DefaultObservableValue<String> summary = observableValue();
    private final DefaultObservableValue<TicketType> type = observableValue();
    private final DefaultObservableValue<ZonedDateTime> lastModified = observableValue();
    private final Long ticketId;
    private final ObservableValue<Boolean> selected;

    public TicketListItemModel(TicketListModel parentModel, Ticket ticket) {
        this.parentModel = parentModel;
        this.ticketId = requireNonNull(ticket.getId());
        this.selected = parentModel.selectedTicketId().map(ticketId::equals);
        populate(ticket);
    }

    void populate(Ticket ticket) {
        if (!Objects.equals(ticketId, ticket.getId())) {
            throw new IllegalStateException("Model cannot be populated with data from the wrong ticket");
        }
        summary.setValue(ticket.getSummary());
        type.setValue(ticket.getType());
        lastModified.setValue(ticket.getLastModifiedOn().atZone(ZoneId.systemDefault()));
    }

    public Long getTicketId() {
        return ticketId;
    }

    public ObservableValue<String> summary() {
        return summary;
    }

    public ObservableValue<TicketType> type() {
        return type;
    }

    public ObservableValue<ZonedDateTime> lastModified() {
        return lastModified;
    }

    public ObservableValue<Boolean> selected() {
        return selected;
    }

    public void delete() {
        parentModel.deleteTicket(ticketId);
    }

    public void select() {
        parentModel.selectTicket(ticketId);
    }
}
