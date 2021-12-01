package net.pkhapps.mvvm4vaadin.model;

import java.util.Collection;

public interface WritableObservableList<T> extends ObservableList<T> {

    default void add(T item) {
        add(getSize(), item);
    }

    void add(int index, T item);

    void addAll(Collection<T> items);

    default void remove(T item) {
        var index = indexOf(item);
        if (index != -1) {
            remove(index);
        }
    }

    void remove(int index);

    default void move(T item, int newPosition) {
        move(indexOf(item), newPosition);
    }

    void move(int index, int newPosition);

    void clear();

    void setItems(Collection<T> items);
}
