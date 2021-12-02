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

import java.io.Serializable;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public interface ObservableValue<T> extends Observable<ObservableValue.ValueChangeEvent<T>> {

    T getValue();

    <E> ObservableValue<E> map(SerializableFunction<T, E> mappingFunction);

    class ValueChangeEvent<T> implements Serializable {
        private final ObservableValue<T> sender;
        private final T oldValue;
        private final T value;

        public ValueChangeEvent(ObservableValue<T> sender, T oldValue, T value) {
            this.sender = requireNonNull(sender);
            this.oldValue = oldValue;
            this.value = value;
        }

        public ObservableValue<T> getSender() {
            return sender;
        }

        public T getOldValue() {
            return oldValue;
        }

        public T getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ValueChangeEvent<?> that = (ValueChangeEvent<?>) o;
            return sender.equals(that.sender) && Objects.equals(oldValue, that.oldValue) && Objects.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(sender, oldValue, value);
        }
    }
}
