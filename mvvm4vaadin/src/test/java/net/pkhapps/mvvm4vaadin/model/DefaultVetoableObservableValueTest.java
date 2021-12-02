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

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DefaultVetoableObservableValueTest {

    @Test
    void noVetoListeners_valueChanged() {
        var model = new DefaultVetoableObservableValue<>("");
        model.setValue("hello");
        assertEquals("hello", model.getValue());
    }

    @Test
    void noVetoingListeners_valueChanged() {
        var model = new DefaultVetoableObservableValue<>("");
        model.addVetoableListener(event -> {
        });
        model.setValue("hello");
        assertEquals("hello", model.getValue());
    }

    @Test
    void veto_valueNotChanged() {
        var model = new DefaultVetoableObservableValue<>("");
        model.addVetoableListener(VetoableObservable.VetoableEvent::veto);
        model.setValue("hello");
        assertEquals("", model.getValue());
    }

    @Test
    void postpone_valueChangedAfterProceed() {
        var model = new DefaultVetoableObservableValue<>("");
        var postponedEvent = new AtomicReference<VetoableObservable.PostponedVetoableEvent<ObservableValue.ValueChangeEvent<String>>>();
        model.addVetoableListener(event -> postponedEvent.set(event.postpone()));
        model.setValue("hello");
        assertEquals("", model.getValue());
        postponedEvent.get().proceed();
        assertEquals("hello", model.getValue());
    }

    @Test
    void postponeTwice_exceptionThrown() {
        var model = new DefaultVetoableObservableValue<>("");
        model.addWeakVetoableListener(VetoableObservable.VetoableEvent::postpone);
        model.addWeakVetoableListener(VetoableObservable.VetoableEvent::postpone);
        assertThrows(IllegalStateException.class, () -> model.setValue("hello"));
        assertEquals("", model.getValue());
    }

    @Test
    void postpone_valueChangedBeforeProceed_exceptionThrown() {
        var model = new DefaultVetoableObservableValue<>("");
        var postponedEvent = new AtomicReference<VetoableObservable.PostponedVetoableEvent<ObservableValue.ValueChangeEvent<String>>>();
        model.addVetoableListener(event -> {
            if (event.getEvent().getValue().equals("hello")) {
                postponedEvent.set(event.postpone());
            }
        });
        model.setValue("hello");
        model.setValue("world");
        assertEquals("world", model.getValue());
        assertThrows(IllegalStateException.class, () -> postponedEvent.get().proceed());
        assertEquals("world", model.getValue());
    }
}
