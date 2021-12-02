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
