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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

/**
 * Default implementation of both {@link ObservableList} and {@link WritableObservableList}. Models typically never
 * expose objects of this class directly to the outside world. Rather, they expose them through any of the
 * aforementioned interfaces depending on whether they want clients to be able to write directly to the observcable list
 * or not. This class is not thread safe.
 *
 * @param <T> the type of items contained inside the observable list.
 * @see ModelFactory#observableList()
 * @see ModelFactory#observableList(Class)
 * @see ModelFactory#observableList(Object[])
 * @see ModelFactory#observableList(Collection)
 */
public class DefaultObservableList<T> extends AbstractObservableList<T> implements WritableObservableList<T> {

    private final List<T> items = new ArrayList<>();
    private final List<T> readOnlyView = Collections.unmodifiableList(items);

    /**
     * Creates a new, empty {@code DefaultObservableList}.
     */
    public DefaultObservableList() {
    }

    /**
     * Creates a new {@code DefaultObservableList} with the given {@code initialItems}.
     *
     * @param initialItems the initial items to add to the list, may be {@code null} or empty.
     */
    public DefaultObservableList(Collection<T> initialItems) {
        this();
        if (initialItems != null) {
            addAll(initialItems);
        }
    }

    @Override
    public List<T> getItems() {
        return readOnlyView;
    }

    @Override
    public void setItems(Collection<T> items) {
        requireNonNull(items, "items must not be null");
        this.items.clear();
        this.items.addAll(items);
        updateObservableValues();
        fireEvent(ItemChangeEvent.listChanged(this));
    }

    @Override
    public void add(int index, T item) {
        items.add(index, item);
        updateObservableValues();
        fireEvent(ItemChangeEvent.itemAdded(this, item, index));
    }

    @Override
    public void addAll(Collection<T> items) {
        requireNonNull(items, "items must not be null");
        if (this.items.addAll(items)) {
            updateObservableValues();
            fireEvent(ItemChangeEvent.listChanged(this));
        }
    }

    @Override
    public void remove(int index) {
        var removedItem = items.remove(index);
        updateObservableValues();
        fireEvent(ItemChangeEvent.itemRemoved(this, removedItem, index));
    }

    @Override
    public void removeIf(Predicate<T> predicate) {
        requireNonNull(predicate, "predicate must not be null");
        for (int i = items.size() - 1; i >= 0; --i) {
            var item = items.get(i);
            if (predicate.test(item)) {
                items.remove(i);
                updateObservableValues();
                fireEvent(ItemChangeEvent.itemRemoved(this, item, i));
            }
        }
    }

    @Override
    public void move(int index, int newPosition) {
        if (index != newPosition) {
            var item = items.remove(index);
            items.add(newPosition, item);
            fireEvent(ItemChangeEvent.itemMoved(this, item, index, newPosition));
        }
    }

    @Override
    public void clear() {
        items.clear();
        updateObservableValues();
        fireEvent(ItemChangeEvent.listChanged(this));
    }
}
