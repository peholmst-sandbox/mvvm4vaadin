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

public class DefaultObservableValue<T> extends AbstractObservableValue<T> implements WritableObservableValue<T> {

    private T value;
    private boolean updatingValue = false;

    public DefaultObservableValue() {
    }

    public DefaultObservableValue(T initialValue) {
        this.value = initialValue;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public void setValue(T value) {
        if (!Objects.equals(this.value, value)) {
            doSetValue(value);
        }
    }

    protected void doSetValue(T value) {
        if (updatingValue) {
            // Prevent value change listeners from changing the value, which would trigger yet another event and so on
            throw new IllegalStateException("The value is being updated");
        }
        try {
            updatingValue = true;
            var old = this.value;
            this.value = value;
            fireValueChangeEvent(old, value);
        } finally {
            updatingValue = false;
        }
    }
}
