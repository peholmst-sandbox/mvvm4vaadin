/*
 * Copyright (c) 2021 Petter Holmström
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

package net.pkhapps.mvvm4vaadin.model;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComputedValueTest {

    @Test
    public void create_initialValueCalculated() {
        var dependency = ModelFactory.observableValue("Joe");
        var computed = new ComputedValue<>(() -> String.format("Hello %s!", dependency.getValue()), List.of(dependency));
        assertEquals("Hello Joe!", computed.getValue());
    }

    @Test
    public void getValue_changedWhenDependencyChanges() {
        var dependency1 = ModelFactory.observableValue("Joe");
        var dependency2 = ModelFactory.observableValue(123);
        var computed = new ComputedValue<>(() -> String.format("Hello %s! You have %d euros.", dependency1.getValue(), dependency2.getValue()), List.of(dependency1, dependency2));

        dependency1.setValue("Max");
        assertEquals("Hello Max! You have 123 euros.", computed.getValue());

        dependency2.setValue(456);
        assertEquals("Hello Max! You have 456 euros.", computed.getValue());
    }

    @Test
    public void addListener_dependencyChanges_eventFires() {
        var dependency = ModelFactory.observableValue("Joe");
        var computed = new ComputedValue<>(() -> String.format("Hello %s!", dependency.getValue()), List.of(dependency));
        var lastEvent = new AtomicReference<ObservableValue.ValueChangeEvent<String>>();
        computed.addListener(lastEvent::set, false);

        dependency.setValue("Max");
        assertEquals(computed, lastEvent.get().getSender());
        assertEquals("Hello Joe!", lastEvent.get().getOldValue());
        assertEquals("Hello Max!", lastEvent.get().getValue());
    }
}
