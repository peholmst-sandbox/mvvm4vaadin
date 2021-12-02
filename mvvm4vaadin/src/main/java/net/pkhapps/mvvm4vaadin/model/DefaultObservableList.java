package net.pkhapps.mvvm4vaadin.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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

    @Override
    public void setItems(Collection<T> items) {
        requireNonNull(items, "items must not be null");
        this.items.clear();
        this.items.addAll(items);
        updateObservableValues();
        fireEvent(ItemChangeEvent.listChanged(this));
    }
}
