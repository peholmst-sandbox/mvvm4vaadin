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
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.function.SerializablePredicate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.Objects.requireNonNull;

public abstract class AbstractObservableList<T> extends AbstractObservable<ObservableList.ItemChangeEvent<T>> implements ObservableList<T> {

    private final DefaultObservableValue<Boolean> empty = new DefaultObservableValue<>(true);
    private final DefaultObservableValue<Integer> size = new DefaultObservableValue<>(0);

    protected AbstractObservableList() {
    }

    protected void updateObservableValues() {
        var items = getItems();
        empty.setValue(items.isEmpty());
        size.setValue(items.size());
    }

    @Override
    protected void fireInitialEvent(SerializableConsumer<? super ItemChangeEvent<T>> listener) {
        listener.accept(ItemChangeEvent.listChanged(this));
    }

    @Override
    public ObservableValue<Boolean> empty() {
        return empty;
    }

    @Override
    public ObservableValue<Integer> size() {
        return size;
    }

    @Override
    public <E> ObservableList<E> map(SerializableFunction<T, E> mappingFunction) {
        return new MappedObservableList<>(this, mappingFunction);
    }

    @Override
    public ObservableList<T> filter(SerializablePredicate<T> predicate) {
        throw new UnsupportedOperationException("Not implemented yet"); // TODO Implement me
    }

    @Override
    public ObservableList<T> sorted(Comparator<T> comparator) {
        throw new UnsupportedOperationException("Not implemented yet"); // TODO Implement me
    }

    private static class MappedObservableList<E, T> extends AbstractObservableList<E> {

        private final ObservableList<T> source;
        private final SerializableFunction<T, E> mappingFunction;
        private final List<E> mappedItems = new ArrayList<>();
        @SuppressWarnings("FieldCanBeLocal") // Needed to prevent premature GC
        private final SerializableConsumer<ItemChangeEvent<T>> sourceItemListener = this::onSourceItemChangeEvent;

        private MappedObservableList(ObservableList<T> source, SerializableFunction<T, E> mappingFunction) {
            this.source = requireNonNull(source, "source must not be null");
            this.mappingFunction = requireNonNull(mappingFunction, "mappingFunction must not be null");
            source.addWeakListener(sourceItemListener, true);
        }

        private void onSourceItemChangeEvent(ItemChangeEvent<T> event) {
            if (event.isItemAdded()) {
                var newItem = mappingFunction.apply(event.getItem());
                mappedItems.add(event.getNewPosition(), newItem);
                updateObservableValues();
                fireEvent(ItemChangeEvent.itemAdded(this, newItem, event.getNewPosition()));
            } else if (event.isItemRemoved()) {
                var oldItem = mappedItems.remove(event.getOldPosition());
                updateObservableValues();
                fireEvent(ItemChangeEvent.itemRemoved(this, oldItem, event.getOldPosition()));
            } else if (event.isItemMoved()) {
                var item = mappedItems.remove(event.getOldPosition());
                mappedItems.add(event.getNewPosition(), item);
                fireEvent(ItemChangeEvent.itemMoved(this, item, event.getOldPosition(), event.getNewPosition()));
            } else {
                mappedItems.clear();
                source.stream().map(mappingFunction).forEach(mappedItems::add);
                updateObservableValues();
                fireEvent(ItemChangeEvent.listChanged(this));
            }
        }

        @Override
        public List<E> getItems() {
            return mappedItems;
        }
    }
}
