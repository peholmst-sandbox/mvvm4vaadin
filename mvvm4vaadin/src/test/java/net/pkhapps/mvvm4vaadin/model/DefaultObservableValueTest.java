package net.pkhapps.mvvm4vaadin.model;

import com.vaadin.flow.function.SerializableConsumer;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultObservableValueTest {

    @Test
    void create_empty() {
        var value = new DefaultObservableValue<String>();
        assertNull(value.getValue());
    }

    @Test
    void create_initialValue() {
        var value = new DefaultObservableValue<>("");
        assertEquals("", value.getValue());
    }

    @Test
    void setValue_different_eventFired() {
        var value = new DefaultObservableValue<>("");
        var lastEvent = new AtomicReference<ObservableValue.ValueChangeEvent<String>>();
        value.addListener(lastEvent::set, false);
        value.setValue("foo");
        assertEquals("foo", value.getValue());
        assertNotNull(lastEvent.get());
        assertSame(value, lastEvent.get().getSender());
        assertEquals("", lastEvent.get().getOldValue());
        assertEquals("foo", lastEvent.get().getValue());
    }

    @Test
    void setValue_same_noEventFired() {
        var value = new DefaultObservableValue<>("foo");
        var lastEvent = new AtomicReference<ObservableValue.ValueChangeEvent<String>>();
        value.addListener(lastEvent::set, false);
        value.setValue("foo");
        assertEquals("foo", value.getValue());
        assertNull(lastEvent.get());
    }

    @Test
    void setValue_insideListener_exceptionThrown() {
        var value = new DefaultObservableValue<>("");
        value.addListener(event -> value.setValue("bar"));
        assertThrows(IllegalStateException.class, () -> value.setValue("foo"));
    }

    @Test
    void addListener_initialEventFired() {
        var value = new DefaultObservableValue<>("");
        var lastEvent = new AtomicReference<ObservableValue.ValueChangeEvent<String>>();
        value.addListener(lastEvent::set);
        assertNotNull(lastEvent.get());
        assertSame(value, lastEvent.get().getSender());
        assertEquals("", lastEvent.get().getOldValue());
        assertEquals("", lastEvent.get().getValue());
    }

    @Test
    void removeListener_noMoreEventsFired() {
        var value = new DefaultObservableValue<>("");
        var lastEvent = new AtomicReference<ObservableValue.ValueChangeEvent<String>>();
        var registration = value.addListener(lastEvent::set);
        assertNotNull(lastEvent.get());

        lastEvent.set(null);
        registration.remove();

        value.setValue("foo");
        assertNull(lastEvent.get());
    }

    @SuppressWarnings("UnusedAssignment")
    @Test
    void addWeakListener_noMoreEventsFiredAfterGC() {
        var value = new DefaultObservableValue<>("");
        var lastEvent = new AtomicReference<ObservableValue.ValueChangeEvent<String>>();
        var listener = (SerializableConsumer<ObservableValue.ValueChangeEvent<String>>) lastEvent::set;
        value.addWeakListener(listener);
        assertNotNull(lastEvent.get());

        listener = null;
        System.gc();
        lastEvent.set(null);

        value.setValue("foo");
        assertNull(lastEvent.get());
    }

    @Test
    void mappedValue_addListener_initialEventFired() {
        var value = new DefaultObservableValue<>(0);
        var mapped = value.map(String::valueOf);
        var lastEvent = new AtomicReference<ObservableValue.ValueChangeEvent<String>>();
        mapped.addListener(lastEvent::set);

        assertNotNull(lastEvent.get());
        assertSame(mapped, lastEvent.get().getSender());
        assertEquals("0", lastEvent.get().getOldValue());
        assertEquals("0", lastEvent.get().getValue());
    }

    @Test
    void mappedValue_setValue_same_noEventFired() {
        var value = new DefaultObservableValue<>(0);
        var mapped = value.map(String::valueOf);
        var lastEvent = new AtomicReference<ObservableValue.ValueChangeEvent<String>>();
        mapped.addListener(lastEvent::set, false);

        value.setValue(0);

        assertNull(lastEvent.get());
    }

    @Test
    void mappedValue_setValue_different_eventFired() {
        var value = new DefaultObservableValue<>(0);
        var mapped = value.map(String::valueOf);
        var lastEvent = new AtomicReference<ObservableValue.ValueChangeEvent<String>>();
        mapped.addListener(lastEvent::set, false);

        value.setValue(1);

        assertNotNull(lastEvent.get());
        assertSame(mapped, lastEvent.get().getSender());
        assertEquals("0", lastEvent.get().getOldValue());
        assertEquals("1", lastEvent.get().getValue());
    }

    @Test
    void mappedValue_mapperFunction_valueConvertedCorrectly() {
        var value = new DefaultObservableValue<>(0);
        var mapped = value.map(String::valueOf);

        assertEquals("0", mapped.getValue());
        value.setValue(1);
        assertEquals("1", mapped.getValue());
        value.setValue(null);
        assertEquals("null", mapped.getValue());
    }

    @Test
    void mappedValue_mapperFunction_valueIsCached() {
        var value = new DefaultObservableValue<>(0);
        var mapped = value.map(String::valueOf);

        var originalMappedValue = mapped.getValue();
        value.setValue(0);
        assertSame(originalMappedValue, mapped.getValue());
    }
}
