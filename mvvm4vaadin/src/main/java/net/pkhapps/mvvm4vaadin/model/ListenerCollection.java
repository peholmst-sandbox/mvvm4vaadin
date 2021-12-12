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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import static java.util.Objects.requireNonNull;

/**
 * A collection of listeners that can be added using both strong and weak references. This class is intended for
 * internal use only and is not thread safe.
 *
 * @param <EVENT> the type of event that can be sent to the listeners.
 */
class ListenerCollection<EVENT> implements Serializable {


    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    // Storing this reference is needed to prevent premature garbage collection of the owner of the ListenerCollection.
    // This can happen when e.g. mapping an ObservableValue to another ObservableValue and no reference is stored to the
    // mapped value itself, even though observers have been added to it.
    private final Serializable owner;
    private Set<SerializableConsumer<? super EVENT>> strongListeners;
    private Map<SerializableConsumer<? super EVENT>, Void> weakListeners;

    /**
     * Creates a new {@code ListenerCollection}.
     *
     * @param owner the object that owns the collection.
     */
    ListenerCollection(Serializable owner) {
        this.owner = owner;
    }

    /**
     * Fires the given event to all registered listeners.
     *
     * @param event the event to fire.
     */
    void fireEvent(EVENT event) {
        requireNonNull(event, "event must not be null");
        Set<SerializableConsumer<? super EVENT>> listeners = new HashSet<>();
        if (strongListeners != null) {
            listeners.addAll(strongListeners);
        }
        if (weakListeners != null) {
            listeners.addAll(weakListeners.keySet());
        }
        listeners.forEach(listener -> listener.accept(event));
    }

    /**
     * Adds the given {@code listener} to this collection using a strong reference.
     *
     * @param listener the listener to add.
     * @return a registration handle for removing the listener when no longer needed.
     */
    Registration addListener(SerializableConsumer<? super EVENT> listener) {
        requireNonNull(listener, "listener must not be null");
        if (strongListeners == null) {
            strongListeners = new HashSet<>();
        }
        strongListeners.add(listener);
        return () -> strongListeners.remove(listener);
    }

    /**
     * Adds the given {@code listener} to this collection using a weak reference. The listener will become unregistered
     * when it is garbage collected.
     *
     * @param listener the listener to add.
     */
    void addWeakListener(SerializableConsumer<? super EVENT> listener) {
        requireNonNull(listener, "listener must not be null");
        if (weakListeners == null) {
            weakListeners = new WeakHashMap<>();
        }
        weakListeners.put(listener, null);
    }

    /**
     * Returns whether the collection currently contains any listeners (registered using strong or weak references).
     *
     * @return true if there is at least one listener, false otherwise.
     */
    boolean hasListeners() {
        return (strongListeners != null && strongListeners.size() > 0) || (weakListeners != null && weakListeners.size() > 0);
    }
}
