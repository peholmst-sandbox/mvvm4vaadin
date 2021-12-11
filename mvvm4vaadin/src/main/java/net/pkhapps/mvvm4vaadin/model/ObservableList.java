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

import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.function.SerializablePredicate;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * An observable list is an ordered list of items that can be observed by other objects. Whenever an item is added,
 * removed or moved, an {@link ItemChangeEvent} is fired to all registered observers. The list does not keep track of
 * what happens to each individual item in case they are mutable.
 *
 * @param <T> the type of items contained inside the observable list.
 * @see ObservableValue
 */
public interface ObservableList<T> extends Iterable<T>, Observable<ObservableList.ItemChangeEvent<T>> {

    /**
     * Returns the items currently in the list.
     *
     * @return an unmodifiable list.
     * @see #iterator()
     * @see #stream()
     */
    List<T> getItems();

    /**
     * An observable value containing the value of {@link #isEmpty()}. Whenever the list becomes empty or non-empty,
     * this value is updated.
     *
     * @return an observable value containing true if the list is empty and false if it contains at least one item.
     */
    ObservableValue<Boolean> empty();

    /**
     * An observable value containing the value of {@link #getSize()}. Whenever items are added to or removed from the
     * list, this value is updated.
     *
     * @return an observable value containing the number of items currently in the list.
     */
    ObservableValue<Integer> size();

    /**
     * Checks whether the list is currently empty or not.
     *
     * @return true if the list is empty, false if it contains at least one item.
     * @see #empty()
     */
    default boolean isEmpty() {
        return getItems().isEmpty();
    }

    /**
     * Returns the current size of the list.
     *
     * @return the number of items in the list.
     * @see #size()
     */
    default int getSize() {
        return getItems().size();
    }

    /**
     * Returns the 0-based index of the first occurrence of the given {@code item} in the list, or -1 if the item does
     * not exist in the list at all.
     *
     * @param item the item to search for.
     * @return the index of the first occurrence of the item if found, -1 otherwise.
     */
    default int indexOf(T item) {
        return getItems().indexOf(item);
    }

    /**
     * Returns the item at the given {@code index}.
     *
     * @param index the 0-based index of the item to locate.
     * @return the item at the given position.
     * @throws IndexOutOfBoundsException if the index is less than 0 or greater than or equal to the size of the list.
     */
    default T get(int index) {
        return getItems().get(index);
    }

    /**
     * {@inheritDoc} The iterator cannot be used to remove items from the list. If the list is changed (items added,
     * removed or moved) while using this iterator, the behavior is undefined.
     */
    @Override
    default Iterator<T> iterator() {
        return getItems().iterator();
    }

    /**
     * Returns a sequential stream with this observable list as its source. If the list is changed (items added, removed
     * or moved) while using this stream, the behavior is undefined.
     *
     * @return a sequential stream over the items in this list.
     */
    default Stream<T> stream() {
        return getItems().stream();
    }

    /**
     * Maps this observable list to another observable list using the given {@code mappingFunction} and returns it. The
     * returned observable list changes whenever this observable value changes. The mapping function is invoked each
     * time an item is added to the list.
     *
     * @param mappingFunction the mapping function to apply to the items of this observable list.
     * @param <E>             the type of the items in the mapped list.
     * @return the mapped observable list.
     * @see #filter(SerializablePredicate)
     */
    <E> ObservableList<E> map(SerializableFunction<T, E> mappingFunction);

    /**
     * Maps this observable list to another observable list that will contain all the items of this observable list that
     * match the given {@code predicate}.
     *
     * @param predicate the predicate to evaluate on the items of this observable list.
     * @return the mapped observable list.
     * @see #map(SerializableFunction)
     */
    ObservableList<T> filter(SerializablePredicate<T> predicate);

    /**
     * Maps this observable list to another observable list that will contain all the items of this observable list
     * sorted using the given {@code comparator}.
     *
     * @param comparator the comparator to use.
     * @return the mapped observable list.
     * @see #map(SerializableFunction)
     */
    ObservableList<T> sorted(Comparator<T> comparator);

    /**
     * Event fired by an {@link ObservableList} when items are added, removed or moved and when the entire list contents
     * changes.
     *
     * @param <T> the type of items in the list.
     */
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

        /**
         * Creates a new {@code ItemChangeEvent} for the case when an item has been added to the list.
         *
         * @param sender      the list to which the item was added.
         * @param item        the added item.
         * @param newPosition the position of the added item.
         * @param <T>         the type of the added item.
         * @return the new event.
         */
        public static <T> ItemChangeEvent<T> itemAdded(ObservableList<T> sender, T item, int newPosition) {
            return new ItemChangeEvent<>(sender, item, -1, newPosition);
        }

        /**
         * Creates a new {@code ItemChangeEvent} for the case when an item has been removed from the list.
         *
         * @param sender      the list from which the item was removed.
         * @param item        the removed item.
         * @param oldPosition the position of the item prior to its removal.
         * @param <T>         the type of the removed item.
         * @return the new event.
         */
        public static <T> ItemChangeEvent<T> itemRemoved(ObservableList<T> sender, T item, int oldPosition) {
            return new ItemChangeEvent<>(sender, item, oldPosition, -1);
        }

        /**
         * Creates a new {@code ItemChangeEvent} for the case when the entire list has changed.
         *
         * @param sender the list that has changed.
         * @param <T>    the type of the items in the list.
         * @return the new event.
         */
        public static <T> ItemChangeEvent<T> listChanged(ObservableList<T> sender) {
            return new ItemChangeEvent<>(sender, null, -1, -1);
        }

        /**
         * Creates a new {@code ItemChangeEvent} for the case when an item has moved from one place to another in the
         * list.
         *
         * @param sender      the list in which the item has moved.
         * @param item        the moved item.
         * @param oldPosition the position of the item before it was moved.
         * @param newPosition the current position of the item.
         * @param <T>         the type of the moved item
         * @return the new event.
         */
        public static <T> ItemChangeEvent<T> itemMoved(ObservableList<T> sender, T item, int oldPosition, int newPosition) {
            return new ItemChangeEvent<>(sender, item, oldPosition, newPosition);
        }

        /**
         * Returns whether this event was fired in response to an added item.
         */
        public boolean isItemAdded() {
            return oldPosition == -1 && newPosition > -1;
        }

        /**
         * Returns whether this event was fired in response to a removed item.
         */
        public boolean isItemRemoved() {
            return newPosition == -1 && oldPosition > -1;
        }

        /**
         * Returns whether this event was fired in response to a changed list.
         */
        public boolean isListChanged() {
            return oldPosition == -1 && newPosition == -1;
        }

        /**
         * Returns whether this event was fired in response to a moved item.
         */
        public boolean isItemMoved() {
            return oldPosition > -1 && newPosition > -1;
        }

        /**
         * Returns the observable list that fired this event.
         *
         * @return the observable list.
         */
        public ObservableList<T> getSender() {
            return sender;
        }

        /**
         * Returns the item that the event concerned, if applicable.
         *
         * @return the event, may be {@code null}.
         */
        public T getItem() {
            return item;
        }

        /**
         * Returns the old position of the item in case it was removed or moved, or -1 if not applicable.
         *
         * @return the old position of the item or -1 if not applicable.
         */
        public int getOldPosition() {
            return oldPosition;
        }

        /**
         * Returns the new (current) position of the item in case it was moved or added, or -1 if not applicable.
         *
         * @return the new position of the item or -1 if not applicable.
         */
        public int getNewPosition() {
            return newPosition;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ItemChangeEvent<?> that = (ItemChangeEvent<?>) o;
            return oldPosition == that.oldPosition && newPosition == that.newPosition && sender.equals(that.sender) && Objects.equals(item, that.item);
        }

        @Override
        public int hashCode() {
            return Objects.hash(sender, item, oldPosition, newPosition);
        }
    }
}
