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
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

/**
 * An observable value is a container of a single value that can be observed by other objects. Whenever the value is
 * changed, a {@link ValueChangeEvent} is fired to all registered observers. Observable values can contain {@code null}
 * unless their implementations explicitly prevent it.
 *
 * @param <T> the type of the value contained inside the observable value.
 * @see ObservableList
 */
public interface ObservableValue<T> extends Observable<ObservableValue.ValueChangeEvent<T>> {

    /**
     * Gets the current value and returns it.
     *
     * @return the value, may be {@code null}.
     * @see #getValueOrDefault(Object)
     * @see #optional()
     */
    T getValue();

    /**
     * Gets the {@linkplain #getValue() current value} and returns it wrapped inside an {@link Optional}.
     *
     * @return the value wrapped inside an {@code Optional} or an empty {@code Optional} if {@code null}.
     * @see #getValueOrDefault(Object)
     * @see #getValue()
     */
    default Optional<T> optional() {
        return Optional.ofNullable(getValue());
    }

    /**
     * Gets the {@linkplain #getValue() current value} and returns it if non-{@code null}. If the value is {@code null},
     * {@code defaultValue} is returned instead.
     *
     * @param defaultValue the default value, may be {@code null}.
     * @return the value or {@code defaultValue}; {@code null} when both the current value and {@code defaultValue} are
     * {@code null}.
     * @see #getValue()
     * @see #optional()
     */
    default T getValueOrDefault(T defaultValue) {
        return optional().orElse(defaultValue);
    }

    /**
     * Converts the {@linkplain #getValue() current value} using the given {@code converter} and returns the result. The
     * converter must be able to deal with {@code null} values, or special care must be taken to ensure this observable
     * value can never contain {@code null}.
     *
     * @param converter the converter to use.
     * @param <E>       the type of the converted value.
     * @return the converted value; {@code null} when returned by the converter.
     * @see #convertValueOrDefault(SerializableFunction, Object)
     */
    default <E> E convertValue(SerializableFunction<T, E> converter) {
        return converter.apply(getValue());
    }

    /**
     * Converts the {@linkplain #getValue() current value} using the given {@code converter} if non-{@code null} and
     * returns the result. If the value is {@code null}, {@code defaultValue} is returned instead.
     *
     * @param converter    the converter to use.
     * @param defaultValue the default value, may be {@code null}.
     * @param <E>          the type of the converted value.
     * @return the converted value or {@code defaultValue}; {@code null} when returned by the converter or when both the
     * current value and {@code defaultValue} are {@code null}.
     * @see #convertValue(SerializableFunction)
     */
    default <E> E convertValueOrDefault(SerializableFunction<T, E> converter, E defaultValue) {
        return optional().map(converter).orElse(defaultValue);
    }

    /**
     * Checks if the {@linkplain #getValue() current value} is equal to the given {@code value}.
     *
     * @param value the value to compare with for equality, may be {@code null}.
     * @return true if both values are either equal or {@code null}, false otherwise.
     * @see #isEqualTo(ObservableValue)
     */
    default boolean isEqualTo(T value) {
        return Objects.equals(getValue(), value);
    }

    /**
     * Checks if the {@linkplain #getValue() current value} is equal to the current value of the given {@code
     * observableValue}.
     *
     * @param observableValue the  observable value to compare with for equality, must not be {@code null} but can
     *                        contain a {@code null} value.
     * @return true if this and the other observable value both contain either equal values or {@code null}, false
     * otherwise.
     * @see #isEqualTo(Object)
     */
    default boolean isEqualTo(ObservableValue<T> observableValue) {
        return isEqualTo(observableValue.getValue());
    }

    /**
     * Maps this observable value to another observable value using the given {@code mappingFunction} and returns it.
     * The returned observable value changes whenever this observable value changes and the mapping function is invoked
     * each time to compute the new mapped value. The mapping function must be able to deal with {@code null} values, or
     * special care must be taken to ensure this observable value can never contain {@code null}.
     *
     * @param mappingFunction the mapping function to apply to the value of this observable value.
     * @param <E>             the type of the mapped value.
     * @return the mapped observable value.
     * @see #map(SerializableFunction, Object)
     * @see #filter(SerializablePredicate)
     */
    <E> ObservableValue<E> map(SerializableFunction<T, E> mappingFunction);

    /**
     * Maps this observable value to another observable value using the given {@code mappingFunction} and returns it.
     * However, if the {@linkplain #getValue() current value} is {@code null}, the {@code defaultValue} will be used as
     * the mapped value without invoking the mapping function at all. In other words, the mapping function will never be
     * invoked with a {@code null}-argument.
     *
     * @param mappingFunction the mapping function to apply to the value of this observable value.
     * @param defaultValue    the default mapped value to use whenever the value of this observable value is {@code
     *                        null}, can be {@code null}.
     * @param <E>             the type of the mapped value.
     * @return the mapped observable value.
     * @see #map(SerializableFunction, Object)
     */
    default <E> ObservableValue<E> map(SerializableFunction<T, E> mappingFunction, E defaultValue) {
        return map(v -> v != null ? mappingFunction.apply(v) : defaultValue);
    }

    /**
     * Maps this observable value to another observable value that will contain the {@linkplain #getValue() current
     * value} when it matches the given {@code predicate} and {@code null} when it does not.
     *
     * @param predicate the predicate to evaluate on the value of this observable value.
     * @return the mapped observable value.
     * @see #map(SerializableFunction)
     */
    default ObservableValue<T> filter(SerializablePredicate<T> predicate) {
        return map(v -> predicate.test(v) ? v : null);
    }

    /**
     * Event fired by an {@link ObservableValue} when its {@linkplain  ObservableValue#getValue() value} changes.
     *
     * @param <T> the type of the value.
     */
    class ValueChangeEvent<T> implements Serializable {
        private final ObservableValue<T> sender;
        private final T oldValue;
        private final T value;

        /**
         * Creates a new {@code ValueChangeEvent}.
         *
         * @param sender   the observable value that fired the event.
         * @param oldValue the old value, may be {@code null}.
         * @param value    the new value, may be {@code null}.
         */
        public ValueChangeEvent(ObservableValue<T> sender, T oldValue, T value) {
            this.sender = requireNonNull(sender);
            this.oldValue = oldValue;
            this.value = value;
        }

        /**
         * Gets the observable value that fired this event.
         *
         * @return the observable value.
         */
        public ObservableValue<T> getSender() {
            return sender;
        }

        /**
         * Gets the old value of the observable value.
         *
         * @return the old value, may be {@code null}.
         */
        public T getOldValue() {
            return oldValue;
        }

        /**
         * Gets the new value of the observable value. This is typically the {@linkplain ObservableValue#getValue()
         * current value} of the observable value, but this is not an absolute requirement and depends on the
         * implementation of the observable value.
         *
         * @return the new value, may be {@code null}.
         */
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
