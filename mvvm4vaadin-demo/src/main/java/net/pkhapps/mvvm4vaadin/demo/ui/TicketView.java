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

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import net.pkhapps.mvvm4vaadin.demo.data.TicketState;
import net.pkhapps.mvvm4vaadin.demo.data.TicketType;
import net.pkhapps.mvvm4vaadin.demo.service.TicketService;
import net.pkhapps.mvvm4vaadin.demo.ui.model.TicketListModel;
import net.pkhapps.mvvm4vaadin.demo.ui.model.TicketModel;

import static net.pkhapps.mvvm4vaadin.binder.BindingFactory.bindFieldValueOnAttach;
import static net.pkhapps.mvvm4vaadin.binder.BindingFactory.reverseBindFieldValueOnAttach;

public class TicketView extends VerticalLayout {

    public TicketView(TicketListModel parentModel, TicketService ticketService) {
        TicketModel ticketModel = new TicketModel(parentModel, ticketService);
        setSizeFull();

        add(new TicketHeader(ticketModel));

        var type = new Select<TicketType>();
        type.setItems(TicketType.values());
        type.setLabel("Type");
        type.setWidthFull();

        var summary = new TextField();
        summary.setLabel("Summary");
        summary.setWidthFull();
        summary.setValueChangeMode(ValueChangeMode.LAZY);

        var description = new TextArea();
        description.setLabel("Description");
        description.setSizeFull();
        description.setValueChangeMode(ValueChangeMode.LAZY);

        var state = new Select<TicketState>();
        state.setItems(TicketState.values());
        state.setLabel("State");
        state.setWidthFull();

        var created = new TextField();
        created.setLabel("Created on");
        created.setWidthFull();

        var lastModified = new TextField();
        lastModified.setLabel("Last modified on");
        lastModified.setWidthFull();

        add(type, summary, description, state, created, lastModified);

        bindFieldValueOnAttach(ticketModel.type(), type);
        reverseBindFieldValueOnAttach(type, ticketModel::updateType);
        bindFieldValueOnAttach(ticketModel.summary(), summary);
        reverseBindFieldValueOnAttach(summary, ticketModel::updateSummary);
        bindFieldValueOnAttach(ticketModel.description(), description);
        reverseBindFieldValueOnAttach(description, ticketModel::updateDescription);
        bindFieldValueOnAttach(ticketModel.state(), state);
        reverseBindFieldValueOnAttach(state, ticketModel::updateState);
        bindFieldValueOnAttach(ticketModel.createdOn().map(DateFormatters::formatDateTime), created);
        created.setReadOnly(true);
        bindFieldValueOnAttach(ticketModel.lastModifiedOn().map(DateFormatters::formatDateTime), lastModified);
        lastModified.setReadOnly(true);
    }
}
