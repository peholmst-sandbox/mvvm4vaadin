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

import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableSupplier;

import java.util.Collection;

import static java.util.Objects.requireNonNull;

/**
 * An implementation of {@link ObservableValue} that computes its value on the fly whenever other {@link Observable}s
 * are updated. This class is not thread safe.
 *
 * @param <T> the type of the value contained inside the observable value.
 * @see ModelFactory#computedValue(SerializableSupplier, Observable[])
 * @see ModelFactory#computedValue(SerializableSupplier, Collection)
 */
public class ComputedValue<T> extends AbstractComputedValue<T> {

    private final SerializableSupplier<T> valueSupplier;

    private final SerializableConsumer<Object> dependencyValueChangeListener = (event) -> updateCachedValue();

    /**
     * Creates a new {@code ComputedValue} that uses the given {@code valueSupplier} to compute the value and updates
     * itself whenever any of the {@code dependencies} are updated.
     *
     * @param valueSupplier the function to use to compute the value.
     * @param dependencies  any dependencies that should trigger a re-computation of the value. If this is empty, this
     *                      computed value will never change once it has been created.
     */
    public ComputedValue(SerializableSupplier<T> valueSupplier, Collection<? extends Observable<?>> dependencies) {
        requireNonNull(valueSupplier, "valueSupplier must not be null");
        requireNonNull(dependencies, "dependencies must not be null");
        this.valueSupplier = valueSupplier;
        dependencies.forEach(dependency -> dependency.addWeakListener(dependencyValueChangeListener));
        updateCachedValue();
    }

    @Override
    protected T computeValue() {
        return valueSupplier.get();
    }
}
