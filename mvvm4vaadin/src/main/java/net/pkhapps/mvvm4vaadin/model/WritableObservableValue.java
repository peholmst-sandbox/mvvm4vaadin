/*
 * Copyright (c) 2021-2023 Petter Holmström
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

import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.function.SerializableSupplier;

import java.util.Locale;

/**
 * An extended version of {@link ObservableValue} that allows clients to also set the value and not only observe it.
 *
 * @param <T> the type of the value contained inside the observable value.
 * @see WritableObservableList
 */
public interface WritableObservableValue<T> extends ObservableValue<T> {

    /**
     * Sets the value of this observable value, notifying all observers of the change. If the value is equal to the
     * {@linkplain #getValue() current value}, implementations may choose to either do nothing or notify the observers
     * anyway.
     *
     * @param value the value to set, may be {@code null} unless explicitly denied by the implementation.
     */
    void setValue(T value);

    /**
     * This is the bidirectional counterpart to {@link #map(SerializableFunction)}, where it is also possible to write
     * to the converted observable value and have the change propagate back to the original.
     *
     * @param converter      the converter to use when converting back and forth between the value types.
     * @param localeSupplier a function returning the locale to use when building the
     *                       {@link com.vaadin.flow.data.binder.ValueContext} that will be passed to the
     *                       {@code converter}.
     * @param <E>            the type of the converted value.
     * @return the converted observable value that can also be written to.
     */
    <E> ValidatableWritableObservableValue<E> convert(Converter<E, T> converter,
                                                      SerializableSupplier<Locale> localeSupplier);
}
