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

public interface WritableObservableList<T> extends ObservableList<T> {

    default void add(T item) {
        add(getSize(), item);
    }

    void add(int index, T item);

    void addAll(Collection<T> items);

    default void addAll(Stream<T> items) {
        addAll(items.collect(Collectors.toList()));
    }

    default void remove(T item) {
        var index = indexOf(item);
        if (index != -1) {
            remove(index);
        }
    }

    void remove(int index);

    void removeIf(Predicate<T> predicate);

    default void move(T item, int newPosition) {
        move(indexOf(item), newPosition);
    }

    void move(int index, int newPosition);

    void clear();

    void setItems(Collection<T> items);

    default void setItems(Stream<T> items) {
        setItems(items.collect(Collectors.toList()));
    }
}
