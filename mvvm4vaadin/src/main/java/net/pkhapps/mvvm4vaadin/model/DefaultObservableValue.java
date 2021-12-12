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
 * Default implementation of both {@link ObservableValue} and {@link WritableObservableValue}. Models typically never
 * expose objects of this class directly to the outside world. Rather, they expose them through any of the
 * aforementioned interfaces depending on whether they want clients to be able to write directly to the observable value
 * or not. This class is not thread safe.
 *
 * @param <T> the type of the value contained inside the observable value.
 * @see ModelFactory#observableValue()
 * @see ModelFactory#observableValue(Class)
 * @see ModelFactory#observableValue(Object)
 */
public class DefaultObservableValue<T> extends AbstractObservableValue<T> implements WritableObservableValue<T> {

    private T value;
    private boolean updatingValue = false;

    /**
     * Creates a new {@code DefaultObservableValue} with an initial value of {@code null}.
     */
    public DefaultObservableValue() {
    }

    /**
     * Creates a new {@code DefaultObservableValue} with the given {@code initialValue}.
     *
     * @param initialValue the initial value, may be {@code null}.
     */
    public DefaultObservableValue(T initialValue) {
        this.value = initialValue;
    }

    @Override
    public T getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     * <p>
     * In this implementation, nothing will happen if the given {@code value} is equal to the {@linkplain #getValue()
     * current value}.
     *
     * @throws IllegalStateException if a listener or any other object is trying to call this method while the listeners
     *                               are being notified of a change. This is forbidden to prevent an eternal loop where
     *                               a listener is notified, changes the value, is notified again, changes the value, is
     *                               notified again, etc.
     */
    @Override
    public void setValue(T value) {
        if (!Objects.equals(this.value, value)) {
            doSetValue(value);
        }
    }

    /**
     * Sets the value of this observable value and fires an event, regardless of whether the given {@code value} is
     * equal to the {@linkplain #getValue() current value} or not.
     *
     * @param value the value to set, may be {@code null}.
     * @throws IllegalStateException if a listener or any other object is trying to call this method while the listeners
     *                               are being notified of a change. This is forbidden to prevent an eternal loop where
     *                               a listener is notified, changes the value, is notified again, changes the value, is
     *                               notified again, etc.
     */
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
