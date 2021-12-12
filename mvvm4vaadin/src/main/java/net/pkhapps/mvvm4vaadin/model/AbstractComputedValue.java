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

import java.util.Objects;

/**
 * Base class for computed {@link ObservableValue}s. Implementations should pay special attention to the {@link
 * #updateCachedValue()} method. This class is not thread safe.
 *
 * @param <T> the type of the value contained inside the observable value.
 */
public abstract class AbstractComputedValue<T> extends AbstractObservableValue<T> {

    private T cachedValue;

    /**
     * Re-computes the value and compares it with the last cached value for equality. If the new value is different from
     * the old value, the new value is cached and all the observers are notified. If the new value is equal to the old
     * value, it is discarded, the old value remains cached and no listeners are notified.
     * <p>
     * Implementations should remember to call this method in their constructors to initialize the cache.
     */
    protected void updateCachedValue() {
        var old = cachedValue;
        var newValue = computeValue();
        if (!Objects.equals(old, newValue)) {
            this.cachedValue = newValue;
            fireValueChangeEvent(old, cachedValue);
        }
    }

    /**
     * Computes the value that may or may not become the new value of this computed value (see {@link
     * #updateCachedValue()} for more information).
     *
     * @return the computed value, may be {@code null}.
     */
    protected abstract T computeValue();

    @Override
    public T getValue() {
        return cachedValue;
    }
}
