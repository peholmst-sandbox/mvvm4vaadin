/*
 * Copyright (c) 2023 Petter Holmström
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

        var type = new Select<TicketType>();
        type.setItems(TicketType.values());
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
