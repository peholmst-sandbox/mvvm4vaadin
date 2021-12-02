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

public abstract class AbstractComputedValue<T> extends AbstractObservableValue<T> {

    private T cachedValue;

    protected void updateCachedValue() {
        var old = cachedValue;
        var newValue = computeValue();
        if (!Objects.equals(old, newValue)) {
            this.cachedValue = newValue;
            fireValueChangeEvent(old, cachedValue);
        }
    }

    protected abstract T computeValue();

    @Override
    public T getValue() {
        return cachedValue;
    }
}
