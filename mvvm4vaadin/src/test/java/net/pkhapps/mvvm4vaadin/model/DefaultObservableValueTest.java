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

    @Test
    void getValueOrDefault_null_defaultValueReturned() {
        var value = new DefaultObservableValue<String>();
        assertEquals("hello", value.getValueOrDefault("hello"));
    }

    @Test
    void getValueOrDefault_nonNull_realValueReturned() {
        var value = new DefaultObservableValue<>("world");
        assertEquals("world", value.getValueOrDefault("hello"));
    }

    @Test
    void convertValue_null_handledByConverter() {
        var value = new DefaultObservableValue<Integer>();
        assertEquals("null", value.convertValue(String::valueOf));
    }

    @Test
    void convertValue_nonNull_handledByConverter() {
        var value = new DefaultObservableValue<>(123);
        assertEquals("123", value.convertValue(String::valueOf));
    }

    @Test
    void convertValueOrDefault_null_defaultValueReturned() {
        var value = new DefaultObservableValue<Integer>();
        assertEquals("hello", value.convertValueOrDefault(String::valueOf, "hello"));
    }

    @Test
    void convertValueOrDefault_nonNull_handledByConverter() {
        var value = new DefaultObservableValue<>(123);
        assertEquals("123", value.convertValueOrDefault(String::valueOf, "hello"));
    }

    @Test
    void isEqualTo_nullValue() {
        var value = new DefaultObservableValue<Integer>();
        assertTrue(value.isEqualTo((Integer) null));
        assertFalse(value.isEqualTo(123));
    }

    @Test
    void isEqualTo_nonNullValue() {
        var value = new DefaultObservableValue<>(123);
        assertTrue(value.isEqualTo(123));
        assertFalse(value.isEqualTo((Integer) null));
    }

    @Test
    void isEqualTo_observableValue() {
        var v1 = new DefaultObservableValue<Integer>();
        var v2 = new DefaultObservableValue<Integer>();
        assertTrue(v1.isEqualTo(v2));
        assertTrue(v2.isEqualTo(v1));

        v1.setValue(123);
        assertFalse(v1.isEqualTo(v2));
        assertFalse(v2.isEqualTo(v1));

        v2.setValue(123);
        assertTrue(v1.isEqualTo(v2));
        assertTrue(v2.isEqualTo(v1));
    }
}
