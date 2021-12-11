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

import com.vaadin.flow.function.SerializablePredicate;

import java.util.*;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

public class DefaultObservableList<T> extends AbstractObservableList<T> implements WritableObservableList<T> {

    private final List<T> items = new ArrayList<>();
    private final List<T> readOnlyView = Collections.unmodifiableList(items);

    public DefaultObservableList() {
    }

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
    public ObservableList<T> filter(SerializablePredicate<T> predicate) {
        throw new UnsupportedOperationException("Not implemented yet"); // TODO Implement me
    }

    @Override
    public ObservableList<T> sorted(Comparator<T> comparator) {
        throw new UnsupportedOperationException("Not implemented yet"); // TODO Implement me
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
