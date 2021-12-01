package net.pkhapps.mvvm4vaadin.model;

import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.shared.Registration;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public abstract class AbstractObservableList<T> extends AbstractObservable<ObservableList.ItemChangeEvent<T>> implements ObservableList<T> {

    private final DefaultObservableValue<Boolean> empty = new DefaultObservableValue<>(true);
    private final DefaultObservableValue<Integer> size = new DefaultObservableValue<>(0);

    protected AbstractObservableList() {
        addListener(event -> updateObservableValues());
    }

    protected void updateObservableValues() {
        var items = getItems();
        empty.setValue(items.isEmpty());
        size.setValue(items.size());
    }

    @Override
    public Registration addListener(SerializableConsumer<? super ItemChangeEvent<T>> listener, boolean fireInitialEvent) {
        var registration = getListeners().addListener(listener);
        if (fireInitialEvent) {
            listener.accept(ItemChangeEvent.listChanged(this));
        }
        return registration;
    }

    @Override
    public void addWeakListener(SerializableConsumer<? super ItemChangeEvent<T>> listener, boolean fireInitialEvent) {
        getListeners().addWeakListener(listener);
        if (fireInitialEvent) {
            listener.accept(ItemChangeEvent.listChanged(this));
        }
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

    private static class MappedObservableList<E, T> extends AbstractObservableList<E> {

        private final ObservableList<T> source;
        private final SerializableFunction<T, E> mappingFunction;
        @SuppressWarnings("FieldCanBeLocal") // Needed to prevent premature GC
        private final SerializableConsumer<ItemChangeEvent<T>> sourceItemListener = this::onSourceItemChangeEvent;
        private final List<E> mappedItems = new ArrayList<>();

        private MappedObservableList(ObservableList<T> source, SerializableFunction<T, E> mappingFunction) {
            this.source = source;
            this.mappingFunction = requireNonNull(mappingFunction);
            source.addWeakListener(sourceItemListener, true);
        }

        private void onSourceItemChangeEvent(ItemChangeEvent<T> event) {
            if (event.isItemAdded()) {
                var newItem = mappingFunction.apply(event.getItem());
                mappedItems.add(event.getNewPosition(), newItem);
                fireEvent(ItemChangeEvent.itemAdded(this, newItem, event.getNewPosition()));
            } else if (event.isItemRemoved()) {
                var oldItem = mappedItems.remove(event.getOldPosition());
                fireEvent(ItemChangeEvent.itemRemoved(this, oldItem, event.getOldPosition()));
            } else if (event.isItemMoved()) {
                var item = mappedItems.remove(event.getOldPosition());
                var offsetNewPosition = event.getNewPosition() > event.getOldPosition() ? event.getNewPosition() - 1 : event.getNewPosition();
                mappedItems.add(offsetNewPosition, item);
                fireEvent(ItemChangeEvent.itemMoved(this, item, event.getOldPosition(), event.getNewPosition()));
            } else {
                mappedItems.clear();
                source.stream().map(mappingFunction).forEach(mappedItems::add);
                fireEvent(ItemChangeEvent.listChanged(this));
            }
        }

        @Override
        public List<E> getItems() {
            return mappedItems;
        }
    }
}
