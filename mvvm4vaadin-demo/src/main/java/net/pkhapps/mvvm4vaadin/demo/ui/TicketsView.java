package net.pkhapps.mvvm4vaadin.demo.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import net.pkhapps.mvvm4vaadin.demo.data.TicketType;
import net.pkhapps.mvvm4vaadin.demo.service.TicketService;
import net.pkhapps.mvvm4vaadin.demo.ui.model.TicketListItemModel;
import net.pkhapps.mvvm4vaadin.demo.ui.model.TicketListModel;
import net.pkhapps.mvvm4vaadin.model.support.MappingFunctions;

import java.util.Objects;

import static net.pkhapps.mvvm4vaadin.binder.BindingFactory.*;

@Route
@CssImport("./styles/tickets-view.css")
public class TicketsView extends HorizontalLayout {

    private final TicketListModel model;

    public TicketsView(TicketService ticketService) {
        addClassName("tickets-view");
        model = new TicketListModel(ticketService);
        setSizeFull();

        var noTicketsView = new NoTicketsView();
        add(noTicketsView);

        var ticketListView = new TicketListView();
        add(ticketListView);

        var ticketView = new TicketView(model, ticketService);
        add(ticketView);

        bindVisibleOnAttach(model.tickets().empty(), noTicketsView);
        bindVisibleOnAttach(model.tickets().empty().map(MappingFunctions::invert), ticketListView);
        bindVisibleOnAttach(model.selectedTicketId().map(Objects::nonNull), ticketView);
    }

    private void addTicket() {
        new AddTicketDialog(model).open();
    }

    public class NoTicketsView extends VerticalLayout {
        public NoTicketsView() {
            addClassName("no-tickets-view");
            add(new Span("There are no tickets. Please create one!"));
            add(new Button("Add Ticket", event -> addTicket()));
            setSizeFull();
            setAlignItems(Alignment.CENTER);
            setJustifyContentMode(JustifyContentMode.CENTER);
        }
    }

    public class TicketListView extends VerticalLayout {
        public TicketListView() {
            addClassName("ticket-list-view");
            setHeightFull();
            setWidth("300px");
            setSpacing(false);
            setMargin(false);
            setPadding(false);
            bindChildrenOnAttach(model.tickets().map(TicketPanel::new), this);
        }
    }

    public static class TicketPanel extends Div {
        public TicketPanel(TicketListItemModel model) {
            addClassName("ticket-panel");
            var iconContainer = new Div();
            iconContainer.addClassName("icon");
            var summary = new Div();
            summary.addClassName("summary");
            var delete = new Button(VaadinIcon.CLOSE_SMALL.create());
            delete.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL);
            add(iconContainer, summary, delete);

            bindContentOnAttach(model.type().map(this::typeToIcon), iconContainer);
            bindTextOnAttach(model.summary(), summary);
            bindActionMethodOnAttach(model::delete, delete);
            bindMethodOnAttach(model.lastModified().map(DateFormatters::formatDateTime), summary, summary::setTitle);
            bindClassNameOnAttach(model.selected().map(s -> s ? "selected" : null), this);
            bindActionMethodOnAttach(model::select, this);
        }

        private Icon typeToIcon(TicketType type) {
            switch (type) {
                case BUG:
                    return VaadinIcon.BUG.create();
                case STORY:
                    return VaadinIcon.OPEN_BOOK.create();
                default:
                    return VaadinIcon.TICKET.create();
            }
        }
    }
}
