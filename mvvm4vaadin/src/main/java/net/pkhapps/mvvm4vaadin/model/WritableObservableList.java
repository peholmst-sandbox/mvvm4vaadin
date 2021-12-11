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

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An extended version of {@link ObservableList} that allows clients to also change the list and not only observe it.
 *
 * @param <T> the type of items contained inside the observable list.
 * @see WritableObservableValue
 */
public interface WritableObservableList<T> extends ObservableList<T> {

    /**
     * Adds the given {@code item} to the end of this list, notifying all observers of the change. Implementations may
     * decide whether they support {@code null} items or not.
     *
     * @param item the item to add.
     * @see #add(int, Object)
     */
    default void add(T item) {
        add(getSize(), item);
    }

    /**
     * Adds the given {@code item} to the list at the given {@code index}, notifying all observers of the change. If
     * there is an element at that particular position it is shifted to the right, as are any subsequent items. No
     * events are fired for these items even though they are technically moved. Implementations may decide whether they
     * support {@code null} items or not.
     *
     * @param index the 0-based index at which the item should be inserted.
     * @param item  the item to insert into the list.
     * @throws IndexOutOfBoundsException if the index is less than 0 or greater than the size of the list.
     * @see #add(Object)
     */
    void add(int index, T item);

    /**
     * Adds the given {@code items} to the end of the list, notifying all observers that the entire list has changed.
     *
     * @param items the items to add.
     * @see #addAll(Stream)
     */
    void addAll(Collection<T> items);

    /**
     * Adds the given {@code items} to the end of the list, notifying all observers that the entire list has changed.
     *
     * @param items the items to add.
     * @see #addAll(Collection)
     */
    default void addAll(Stream<T> items) {
        addAll(items.collect(Collectors.toList()));
    }

    /**
     * Removes the given {@code item} from this list, notifying all observers of the change. If the item does not exist
     * in the list, nothing happens and no observers are notified.
     *
     * @param item the item to remove.
     * @see #remove(int)
     * @see #removeIf(Predicate)
     */
    default void remove(T item) {
        var index = indexOf(item);
        if (index != -1) {
            remove(index);
        }
    }

    /**
     * Removes the item at the given {@code index}, notifying all observers of the change.
     *
     * @param index the 0-based index of the item to remove.
     * @throws IndexOutOfBoundsException if the index is less than 0 or greater than or equal to the size of the list.
     * @see #remove(Object)
     * @see #removeIf(Predicate)
     */
    void remove(int index);

    /**
     * Removes all items that match the given predicate, notifying all observers of each removed item. If no items match
     * the predicate, nothing happens and no observers are notified.
     *
     * @param predicate the predicate to evaluate on all the items in this observable list.
     * @see #remove(Object)
     * @see #remove(int)
     */
    void removeIf(Predicate<T> predicate);

    /**
     * Moves the first occurrence of the given {@code item} to the {@code newPosition}, notifying all observers of the
     * change. If there is an element at that particular position it is shifted to the right, as are any subsequent
     * items. No events are fired for these items even though they are technically moved. If the item does not exist in
     * the list or is already at the desired position, nothing happens and no observers are notified.
     *
     * @param item        the item to move.
     * @param newPosition the 0-based position to move the item to.
     * @throws IndexOutOfBoundsException if the new position is less than 0 or greater than the size of the list.
     * @see #move(int, int)
     */
    default void move(T item, int newPosition) {
        move(indexOf(item), newPosition);
    }

    /**
     * Moves the item at the given {@code index} to the {@code newPosition}, notifying all observers of the change. If
     * there is an element at that particular position it is shifted to the right, as are any subsequent items. No
     * events are fired for these items even though they are technically moved. If the index and the new position are
     * equal, nothing happens and no observers are notified.
     *
     * @param index       the 0-based index of the item to move.
     * @param newPosition the 0-based position to move the item to.
     * @throws IndexOutOfBoundsException if the index is less than 0 or greater than or equal to the size of the list,
     *                                   or the new position is less than 0 or greater than the size of the list.
     * @see #move(Object, int)
     */
    void move(int index, int newPosition);

    /**
     * Removes all the items from this list, notifying all observers that the entire list has changed.
     */
    void clear();

    /**
     * Replaces the contents of this list with the given {@code items}, notifying all observers that the entire list has
     * changed.
     *
     * @param items the items to replace the contents of this list with.
     * @see #setItems(Stream)
     */
    void setItems(Collection<T> items);

    /**
     * Replaces the contents of this list with the given {@code items}, notifying all observers that the entire list has
     * changed.
     *
     * @param items the items to replace the contents of this list with.
     * @see #setItems(Collection)
     */
    default void setItems(Stream<T> items) {
        setItems(items.collect(Collectors.toList()));
    }
}
