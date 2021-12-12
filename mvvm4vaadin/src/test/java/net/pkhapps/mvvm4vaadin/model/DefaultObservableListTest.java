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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultObservableListTest {

    // TODO Test IndexOutOfBoundsExceptions

    @Test
    void create_empty() {
        var list = new DefaultObservableList<String>();
        assertTrue(list.isEmpty());
        assertTrue(list.empty().getValue());
        assertEquals(0, list.getSize());
        assertEquals(0, list.size().getValue());
        assertEquals(Collections.emptyList(), list.getItems());
    }

    @Test
    void create_initialValue() {
        var list = new DefaultObservableList<>(List.of("hello", "world"));
        assertFalse(list.isEmpty());
        assertFalse(list.empty().getValue());
        assertEquals(2, list.getSize());
        assertEquals(2, list.size().getValue());
        assertEquals(List.of("hello", "world"), list.getItems());
    }

    @Test
    void add() {
        var list = new DefaultObservableList<String>();
        var lastEvent = new AtomicReference<ObservableList.ItemChangeEvent<String>>();
        list.addListener(lastEvent::set, false);

        list.add("hello");

        assertEquals(1, list.getSize());
        assertEquals(1, list.size().getValue());
        assertFalse(list.isEmpty());
        assertFalse(list.empty().getValue());
        assertEquals(list.get(0), "hello");
        assertEquals(List.of("hello"), list.getItems());

        assertEquals(list, lastEvent.get().getSender());
        assertTrue(lastEvent.get().isItemAdded());
        assertEquals("hello", lastEvent.get().getItem());
        assertEquals(0, lastEvent.get().getNewPosition());
    }

    @Test
    void add_specificPosition() {
        var list = new DefaultObservableList<String>(List.of("farewell", "world"));
        var lastEvent = new AtomicReference<ObservableList.ItemChangeEvent<String>>();
        list.addListener(lastEvent::set, false);

        list.add(1, "cruel");

        assertEquals(3, list.getSize());
        assertEquals(3, list.size().getValue());
        assertFalse(list.isEmpty());
        assertFalse(list.empty().getValue());
        assertEquals(list.get(1), "cruel");
        assertEquals(List.of("farewell", "cruel", "world"), list.getItems());

        assertEquals(list, lastEvent.get().getSender());
        assertTrue(lastEvent.get().isItemAdded());
        assertEquals("cruel", lastEvent.get().getItem());
        assertEquals(1, lastEvent.get().getNewPosition());
    }

    @Test
    void remove() {
        var list = new DefaultObservableList<>(List.of("hello", "awesome", "world"));
        var lastEvent = new AtomicReference<ObservableList.ItemChangeEvent<String>>();
        list.addListener(lastEvent::set, false);

        list.remove("awesome");

        assertEquals(2, list.getSize());
        assertEquals(2, list.size().getValue());
        assertFalse(list.isEmpty());
        assertFalse(list.empty().getValue());
        assertEquals(List.of("hello", "world"), list.getItems());

        assertEquals(list, lastEvent.get().getSender());
        assertTrue(lastEvent.get().isItemRemoved());
        assertEquals("awesome", lastEvent.get().getItem());
        assertEquals(1, lastEvent.get().getOldPosition());
    }

    @Test
    void removeIf() {
        var list = new DefaultObservableList<>(List.of("an item", "another item", "third", "fourth", "a fifth"));
        var events = new ArrayList<ObservableList.ItemChangeEvent<String>>();
        list.addListener(events::add, false);
        list.removeIf(s -> s.startsWith("a"));

        assertEquals(2, list.getSize());
        assertEquals(2, list.size().getValue());
        assertFalse(list.isEmpty());
        assertFalse(list.empty().getValue());
        assertEquals(List.of("third", "fourth"), list.getItems());

        assertEquals(3, events.size());
        assertEquals("a fifth", events.get(0).getItem());
        assertEquals("another item", events.get(1).getItem());
        assertEquals("an item", events.get(2).getItem());
        assertTrue(events.stream().allMatch(ObservableList.ItemChangeEvent::isItemRemoved));
    }

    @Test
    void move_backward() {
        var list = new DefaultObservableList<>(List.of("hello", "awesome", "world"));
        var lastEvent = new AtomicReference<ObservableList.ItemChangeEvent<String>>();
        list.addListener(lastEvent::set, false);

        list.move("awesome", 0);

        assertEquals(3, list.getSize());
        assertEquals(3, list.size().getValue());
        assertFalse(list.isEmpty());
        assertFalse(list.empty().getValue());
        assertEquals(List.of("awesome", "hello", "world"), list.getItems());

        assertEquals(list, lastEvent.get().getSender());
        assertTrue(lastEvent.get().isItemMoved());
        assertEquals("awesome", lastEvent.get().getItem());
        assertEquals(1, lastEvent.get().getOldPosition());
        assertEquals(0, lastEvent.get().getNewPosition());
    }

    @Test
    void move_forward() {
        var list = new DefaultObservableList<>(List.of("hello", "awesome", "world"));
        var lastEvent = new AtomicReference<ObservableList.ItemChangeEvent<String>>();
        list.addListener(lastEvent::set, false);

        list.move("awesome", 2);

        assertEquals(3, list.getSize());
        assertEquals(3, list.size().getValue());
        assertFalse(list.isEmpty());
        assertFalse(list.empty().getValue());
        assertEquals(List.of("hello", "world", "awesome"), list.getItems());

        assertEquals(list, lastEvent.get().getSender());
        assertTrue(lastEvent.get().isItemMoved());
        assertEquals("awesome", lastEvent.get().getItem());
        assertEquals(1, lastEvent.get().getOldPosition());
        assertEquals(2, lastEvent.get().getNewPosition());
    }

    @Test
    void clear() {
        var list = new DefaultObservableList<>(List.of("hello", "world"));
        var lastEvent = new AtomicReference<ObservableList.ItemChangeEvent<String>>();
        list.addListener(lastEvent::set, false);

        list.clear();

        assertEquals(0, list.getSize());
        assertEquals(0, list.size().getValue());
        assertTrue(list.isEmpty());
        assertTrue(list.empty().getValue());

        assertEquals(list, lastEvent.get().getSender());
        assertTrue(lastEvent.get().isListChanged());
    }

    @Test
    void setItems() {
        var list = new DefaultObservableList<>(List.of("hello", "world"));
        var lastEvent = new AtomicReference<ObservableList.ItemChangeEvent<String>>();
        list.addListener(lastEvent::set, false);

        list.setItems(List.of("foo", "bar"));

        assertEquals(2, list.getSize());
        assertEquals(2, list.size().getValue());
        assertFalse(list.isEmpty());
        assertFalse(list.empty().getValue());
        assertEquals(List.of("foo", "bar"), list.getItems());

        assertEquals(list, lastEvent.get().getSender());
        assertTrue(lastEvent.get().isListChanged());
    }

    @Test
    void addAll() {
        var list = new DefaultObservableList<>(List.of("hello", "world"));
        var lastEvent = new AtomicReference<ObservableList.ItemChangeEvent<String>>();
        list.addListener(lastEvent::set, false);

        list.addAll(List.of("foo", "bar"));

        assertEquals(4, list.getSize());
        assertEquals(4, list.size().getValue());
        assertFalse(list.isEmpty());
        assertFalse(list.empty().getValue());
        assertEquals(List.of("hello", "world", "foo", "bar"), list.getItems());

        assertEquals(list, lastEvent.get().getSender());
        assertTrue(lastEvent.get().isListChanged());
    }

    @Test
    void mappedList_mapperFunction_valuesConvertedCorrectly() {
        var list = new DefaultObservableList<>(List.of(0, 1, 2, 3));
        var mapped = list.map(String::valueOf);

        assertEquals(List.of("0", "1", "2", "3"), mapped.getItems());
        assertEquals(list.size().getValue(), mapped.size().getValue());
        assertEquals(list.empty().getValue(), mapped.empty().getValue());

        list.add(4);
        assertEquals(List.of("0", "1", "2", "3", "4"), mapped.getItems());
        assertEquals(list.size().getValue(), mapped.size().getValue());
        assertEquals(list.empty().getValue(), mapped.empty().getValue());

        list.remove(3);
        assertEquals(List.of("0", "1", "2", "4"), mapped.getItems());
        assertEquals(list.size().getValue(), mapped.size().getValue());
        assertEquals(list.empty().getValue(), mapped.empty().getValue());

        list.move(1, 0);
        assertEquals(List.of("1", "0", "2", "4"), mapped.getItems());
        assertEquals(list.size().getValue(), mapped.size().getValue());
        assertEquals(list.empty().getValue(), mapped.empty().getValue());

        list.move(2, 3);
        assertEquals(List.of("1", "0", "4", "2"), mapped.getItems());
        assertEquals(list.size().getValue(), mapped.size().getValue());
        assertEquals(list.empty().getValue(), mapped.empty().getValue());
    }

    @Test
    void mappedList_addListener_initialEventFired() {
        var list = new DefaultObservableList<>(List.of(0, 1, 2, 3));
        var mapped = list.map(String::valueOf);
        var lastEvent = new AtomicReference<ObservableList.ItemChangeEvent<String>>();
        mapped.addListener(lastEvent::set);

        assertNotNull(lastEvent.get());
        assertSame(mapped, lastEvent.get().getSender());
        assertTrue(lastEvent.get().isListChanged());
    }

    @Test
    void mappedList_addListener_itemAdded_eventFired() {
        var list = new DefaultObservableList<>(List.of(0, 1, 2, 3));
        var mapped = list.map(String::valueOf);
        var lastEvent = new AtomicReference<ObservableList.ItemChangeEvent<String>>();
        mapped.addListener(lastEvent::set);

        list.add(4);

        assertSame(mapped, lastEvent.get().getSender());
        assertEquals("4", lastEvent.get().getItem());
        assertTrue(lastEvent.get().isItemAdded());
        assertEquals(4, lastEvent.get().getNewPosition());
    }

    @Test
    void mappedList_addListener_itemRemoved_eventFired() {
        var list = new DefaultObservableList<>(List.of(0, 1, 2, 3));
        var mapped = list.map(String::valueOf);
        var lastEvent = new AtomicReference<ObservableList.ItemChangeEvent<String>>();
        mapped.addListener(lastEvent::set);

        list.remove(3);

        assertSame(mapped, lastEvent.get().getSender());
        assertEquals("3", lastEvent.get().getItem());
        assertTrue(lastEvent.get().isItemRemoved());
        assertEquals(3, lastEvent.get().getOldPosition());
    }

    @Test
    void mappedList_addListener_itemMoved_eventFired() {
        var list = new DefaultObservableList<>(List.of(0, 1, 2, 3));
        var mapped = list.map(String::valueOf);
        var lastEvent = new AtomicReference<ObservableList.ItemChangeEvent<String>>();
        mapped.addListener(lastEvent::set);

        list.move(1, 2);

        assertSame(mapped, lastEvent.get().getSender());
        assertEquals("1", lastEvent.get().getItem());
        assertTrue(lastEvent.get().isItemMoved());
        assertEquals(1, lastEvent.get().getOldPosition());
        assertEquals(2, lastEvent.get().getNewPosition());
    }

    @Test
    void mappedList_addListener_listChanged_eventFired() {
        var list = new DefaultObservableList<>(List.of(0, 1, 2, 3));
        var mapped = list.map(String::valueOf);
        var lastEvent = new AtomicReference<ObservableList.ItemChangeEvent<String>>();
        mapped.addListener(lastEvent::set);

        list.setItems(List.of(4, 5, 6, 7));

        assertSame(mapped, lastEvent.get().getSender());
        assertTrue(lastEvent.get().isListChanged());
    }
}
