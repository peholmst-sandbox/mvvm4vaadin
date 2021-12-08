package net.pkhapps.mvvm4vaadin.demo.ui.model;

import com.vaadin.flow.function.SerializableConsumer;
import net.pkhapps.mvvm4vaadin.demo.data.Ticket;
import net.pkhapps.mvvm4vaadin.demo.data.TicketState;
import net.pkhapps.mvvm4vaadin.demo.data.TicketType;
import net.pkhapps.mvvm4vaadin.demo.service.TicketService;
import net.pkhapps.mvvm4vaadin.model.DefaultObservableValue;
import net.pkhapps.mvvm4vaadin.model.ObservableValue;

import java.io.Serializable;
import java.time.Instant;
import java.util.Optional;

import static net.pkhapps.mvvm4vaadin.model.ModelFactory.observableValue;

public class TicketModel implements Serializable {

    private final TicketListModel parentModel;
    private final TicketService ticketService;
    private final DefaultObservableValue<TicketType> type = observableValue();
    private final DefaultObservableValue<String> summary = observableValue();
    private final DefaultObservableValue<String> description = observableValue();
    private final DefaultObservableValue<Instant> createdOn = observableValue();
    private final DefaultObservableValue<Instant> lastModifiedOn = observableValue();
    private final DefaultObservableValue<TicketState> state = observableValue();
    private final SerializableConsumer<ObservableValue.ValueChangeEvent<Long>> onSelectedTicketIdChanged
            = this::setOnSelectedTicketIdChanged;
    private Long ticketId;

    public TicketModel(TicketListModel parentModel, TicketService ticketService) {
        this.parentModel = parentModel;
        this.ticketService = ticketService;
        parentModel.selectedTicketId().addWeakListener(onSelectedTicketIdChanged);
    }

    private void setOnSelectedTicketIdChanged(ObservableValue.ValueChangeEvent<Long> event) {
        populate(Optional.ofNullable(event.getValue()).flatMap(ticketService::getTicket).orElse(null));
    }

    private void populate(Ticket ticket) {
        if (ticket == null) {
            type.setValue(null);
            summary.setValue(null);
            description.setValue(null);
            createdOn.setValue(null);
            lastModifiedOn.setValue(null);
            state.setValue(null);
            ticketId = null;
        } else {
            type.setValue(ticket.getType());
            summary.setValue(ticket.getSummary());
            description.setValue(ticket.getDescription());
            createdOn.setValue(ticket.getCreatedOn());
            lastModifiedOn.setValue(ticket.getLastModifiedOn());
            state.setValue(ticket.getState());
            ticketId = ticket.getId();
            parentModel.ticketRefreshed(ticket);
        }
    }

    public ObservableValue<TicketType> type() {
        return type;
    }

    public ObservableValue<String> summary() {
        return summary;
    }

    public ObservableValue<String> description() {
        return description;
    }

    public ObservableValue<Instant> createdOn() {
        return createdOn;
    }

    public ObservableValue<Instant> lastModifiedOn() {
        return lastModifiedOn;
    }

    public ObservableValue<TicketState> state() {
        return state;
    }

    public TicketListModel getParentModel() {
        return parentModel;
    }

    public void updateSummary(String summary) {
        if (ticketId != null) {
            populate(ticketService.updateSummary(ticketId, summary));
        }
    }

    public void updateDescription(String description) {
        if (ticketId != null) {
            populate(ticketService.updateDescription(ticketId, description));
        }
    }

    public void updateType(TicketType type) {
        if (ticketId != null) {
            populate(ticketService.updateType(ticketId, type));
        }
    }

    public void updateState(TicketState state) {
        if (ticketId != null) {
            populate(ticketService.updateState(ticketId, state));
        }
    }
}
