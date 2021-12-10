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
import com.vaadin.flow.shared.Registration;

import java.io.Serializable;

/**
 * Base interface for any object that can be observed by other objects.
 *
 * @param <EVENT> the type of event fired by the observable object.
 */
public interface Observable<EVENT> extends Serializable {

    /**
     * Registers the given {@code listener} with this observable object using a strong reference. The listener will
     * receive events until explicitly de-registered using the returned {@link Registration} handle. The observable will
     * fire an initial event to the listener upon registration so that it can initialize itself. This is the same as
     * calling {@link #addListener(SerializableConsumer, boolean) addListener(listener, true)}.
     *
     * @param listener the listener to register.
     * @return a {@link Registration} handle for de-registering the listener when no longer needed.
     */
    default Registration addListener(SerializableConsumer<? super EVENT> listener) {
        return addListener(listener, true);
    }

    /**
     * Registers the given {@code listener} with this observable object using a strong reference. The listener will
     * receive events until explicitly de-registered using the returned {@link Registration} handle.
     *
     * @param listener         the listener to register.
     * @param fireInitialEvent true to fire an initial event to the listener upon registration, false to wait for the
     *                         first real event before notifying the listener.
     * @return a {@link Registration} handle for de-registering the listener when no longer needed.
     */
    Registration addListener(SerializableConsumer<? super EVENT> listener, boolean fireInitialEvent);

    /**
     * Registers the given {@code listener} with this observable object using a weak reference. The listener will
     * receive events until it has been garbage collected. It is up to the caller to make sure the listener is not
     * garbage collected too early. The observable will fire an initial event to the listener upon registration so that
     * it can initialize itself. This is the same as calling {@link #addWeakListener(SerializableConsumer, boolean)
     * addWeakListener(listener, true)}.
     *
     * @param listener the listener to register.
     */
    default void addWeakListener(SerializableConsumer<? super EVENT> listener) {
        addWeakListener(listener, true);
    }

    /**
     * Registers the given {@code listener} with this observable object using a weak reference. The listener will
     * receive events until it has been garbage collected. It is up to the caller to make sure the listener is not
     * garbage collected too early.
     *
     * @param listener         the listener to register.
     * @param fireInitialEvent true to fire an initial event to the listener upon registration, false to wait for the
     *                         first real event before notifying the listener.
     */
    void addWeakListener(SerializableConsumer<? super EVENT> listener, boolean fireInitialEvent);
}
