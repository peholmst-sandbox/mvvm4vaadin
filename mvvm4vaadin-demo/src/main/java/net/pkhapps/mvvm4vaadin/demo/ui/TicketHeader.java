package net.pkhapps.mvvm4vaadin.demo.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import net.pkhapps.mvvm4vaadin.demo.ui.model.TicketModel;

import static net.pkhapps.mvvm4vaadin.binder.BindingFactory.bindTextOnAttach;
import static net.pkhapps.mvvm4vaadin.model.ModelFactory.computedValue;

public class TicketHeader extends HorizontalLayout {

    public TicketHeader(TicketModel model) {
        setWidthFull();
        var title = new Span();
        title.addClassName("ticket-title");
        var addButton = new Button("New Ticket", event -> new AddTicketDialog(model.getParentModel()).open());
        add(title, addButton);

        bindTextOnAttach(computedValue(() -> String.format("%s: %s (%s)",
                        model.type().getValue(), model.summary().getValue(), model.state().getValue()),
                model.type(), model.summary(), model.state()), title);
    }
}
