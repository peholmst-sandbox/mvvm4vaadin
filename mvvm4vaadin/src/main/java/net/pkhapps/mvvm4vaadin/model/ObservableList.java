package net.pkhapps.mvvm4vaadin.model;

import com.vaadin.flow.function.SerializableFunction;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public interface ObservableList<T> extends Iterable<T>, Observable<ObservableList.ItemChangeEvent<T>> {

    List<T> getItems();

    ObservableValue<Boolean> empty();

    ObservableValue<Integer> size();

    default boolean isEmpty() {
        return getItems().isEmpty();
    }

    default int getSize() {
        return getItems().size();
    }

    default int indexOf(T item) {
        return getItems().indexOf(item);
    }

    default T get(int index) {
        return getItems().get(index);
    }

    @Override
    default Iterator<T> iterator() {
        return getItems().iterator();
    }

    default Stream<T> stream() {
        return getItems().stream();
    }

    <E> ObservableList<E> map(SerializableFunction<T, E> mappingFunction);

    class ItemChangeEvent<T> implements Serializable {
        private final ObservableList<T> sender;
        private final T item;
        private final int oldPosition;
        private final int newPosition;

        private ItemChangeEvent(ObservableList<T> sender, T item, int oldPosition, int newPosition) {
            this.sender = requireNonNull(sender);
            this.item = item;
            this.oldPosition = oldPosition;
            this.newPosition = newPosition;
        }

        public boolean isItemAdded() {
            return oldPosition == -1 && newPosition > -1;
        }

        public boolean isItemRemoved() {
            return newPosition == -1 && oldPosition > -1;
        }

        public boolean isListChanged() {
            return oldPosition == -1 && newPosition == -1;
        }

        public boolean isItemMoved() {
            return oldPosition > -1 && newPosition > -1;
        }

        public ObservableList<T> getSender() {
            return sender;
        }

        public T getItem() {
            return item;
        }

        public int getOldPosition() {
            return oldPosition;
        }

        public int getNewPosition() {
            return newPosition;
        }

        public static <T> ItemChangeEvent<T> itemAdded(ObservableList<T> sender, T item, int newPosition) {
            return new ItemChangeEvent<>(sender, item, -1, newPosition);
        }

        public static <T> ItemChangeEvent<T> itemRemoved(ObservableList<T> sender, T item, int oldPosition) {
            return new ItemChangeEvent<>(sender, item, oldPosition, -1);
        }

        public static <T> ItemChangeEvent<T> listChanged(ObservableList<T> sender) {
            return new ItemChangeEvent<>(sender, null, -1, -1);
        }

        public static <T> ItemChangeEvent<T> itemMoved(ObservableList<T> sender, T item, int oldPosition, int newPosition) {
            return new ItemChangeEvent<>(sender, item, oldPosition, newPosition);
        }
    }
}
