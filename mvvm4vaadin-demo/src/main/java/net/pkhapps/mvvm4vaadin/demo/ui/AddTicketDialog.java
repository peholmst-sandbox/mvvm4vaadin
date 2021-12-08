package net.pkhapps.mvvm4vaadin.demo.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import net.pkhapps.mvvm4vaadin.demo.data.TicketType;
import net.pkhapps.mvvm4vaadin.demo.ui.model.TicketListModel;

public class AddTicketDialog extends Dialog {

    public AddTicketDialog(TicketListModel model) {
        add(new H4("Add Ticket"));

        var type = new Select<>(TicketType.values());
        type.setValue(TicketType.BUG);
        type.setLabel("Type");
        type.setWidthFull();

        var summary = new TextField();
        summary.setLabel("Summary");
        summary.setWidthFull();

        var description = new TextArea();
        description.setLabel("Description");
        description.setWidthFull();
        description.setHeight("100px");

        var addButton = new Button("Add", event -> {
            model.addTicket(type.getValue(), summary.getValue(), description.getValue());
            close();
        });
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        add(type, summary, description, addButton);
    }
}
