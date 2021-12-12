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

/**
 * Base class for implementation of {@link Observable}. This class is not thread safe.
 *
 * @param <EVENT> the type of event fired by the observable object.
 */
public abstract class AbstractObservable<EVENT> implements Observable<EVENT> {

    private ListenerCollection<EVENT> listeners;

    @Override
    public Registration addListener(SerializableConsumer<? super EVENT> listener, boolean fireInitialEvent) {
        var registration = getListeners().addListener(listener);
        if (fireInitialEvent) {
            fireInitialEvent(listener);
        }
        return registration;
    }

    @Override
    public void addWeakListener(SerializableConsumer<? super EVENT> listener, boolean fireInitialEvent) {
        getListeners().addWeakListener(listener);
        if (fireInitialEvent) {
            fireInitialEvent(listener);
        }
    }

    /**
     * Fires an initial event to the given {@code listener} so that it can synchronize itself with the state of this
     * observable.
     *
     * @param listener the listener to fire the initial event to.
     */
    protected abstract void fireInitialEvent(SerializableConsumer<? super EVENT> listener);

    private ListenerCollection<EVENT> getListeners() {
        if (listeners == null) {
            listeners = new ListenerCollection<>(this);
        }
        return listeners;
    }

    /**
     * Fires the given {@code event} to all registered listeners.
     *
     * @param event the event to fire.
     */
    protected void fireEvent(EVENT event) {
        if (listeners != null) {
            listeners.fireEvent(event);
        }
    }
}
