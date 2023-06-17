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

import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableFunction;

import static java.util.Objects.requireNonNull;

/**
 * Base class for implementations of {@link ObservableValue}. This class is not thread safe.
 *
 * @param <T> the type of the value contained inside the observable value.
 */
public abstract class AbstractObservableValue<T> extends AbstractObservable<ObservableValue.ValueChangeEvent<T>>
        implements ObservableValue<T> {

    @Override
    protected void fireInitialEvent(SerializableConsumer<? super ValueChangeEvent<T>> listener) {
        var value = getValue();
        listener.accept(new ValueChangeEvent<>(this, value, value));
    }

    /**
     * Fires a {@link net.pkhapps.mvvm4vaadin.model.ObservableValue.ValueChangeEvent} to all registered listeners.
     *
     * @param old   the old value, may be {@code null}.
     * @param value the new value, may be {@code null}.
     */
    protected void fireValueChangeEvent(T old, T value) {
        fireEvent(new ValueChangeEvent<>(this, old, value));
    }

    @Override
    public <E> ObservableValue<E> map(SerializableFunction<T, E> mappingFunction) {
        return new MappedObservableValue<>(this, mappingFunction);
    }

    protected static class MappedObservableValue<E, T> extends AbstractComputedValue<E> {

        private final ObservableValue<T> source;
        private final SerializableFunction<T, E> mappingFunction;
        @SuppressWarnings("FieldCanBeLocal") // Needed to prevent premature GC
        private final SerializableConsumer<ValueChangeEvent<T>> sourceValueListener = (event) -> updateCachedValue();

        protected MappedObservableValue(ObservableValue<T> source, SerializableFunction<T, E> mappingFunction) {
            this.source = requireNonNull(source, "source must not be null");
            this.mappingFunction = requireNonNull(mappingFunction, "mappingFunction must not be null");
            source.addWeakListener(sourceValueListener);
        }

        @Override
        protected E computeValue() {
            return mappingFunction.apply(source.getValue());
        }
    }
}
