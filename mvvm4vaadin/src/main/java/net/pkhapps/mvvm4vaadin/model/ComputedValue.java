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

public class ComputedValue<T> extends AbstractComputedValue<T> {

    private final SerializableSupplier<T> valueSupplier;

    private final SerializableConsumer<Object> dependencyValueChangeListener = (event) -> updateCachedValue();

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
