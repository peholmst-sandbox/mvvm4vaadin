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

class ListenerCollection<EVENT> implements Serializable {

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final Serializable owner; // Needed to prevent premature garbage collection of the ListenerCollection
    private Set<SerializableConsumer<? super EVENT>> strongListeners;
    private Map<SerializableConsumer<? super EVENT>, Void> weakListeners;

    ListenerCollection(Serializable owner) {
        this.owner = owner;
    }

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

    Registration addListener(SerializableConsumer<? super EVENT> listener) {
        requireNonNull(listener, "listener must not be null");
        if (strongListeners == null) {
            strongListeners = new HashSet<>();
        }
        strongListeners.add(listener);
        return () -> strongListeners.remove(listener);
    }

    void addWeakListener(SerializableConsumer<? super EVENT> listener) {
        requireNonNull(listener, "listener must not be null");
        if (weakListeners == null) {
            weakListeners = new WeakHashMap<>();
        }
        weakListeners.put(listener, null);
    }

    boolean hasListeners() {
        return (strongListeners != null && strongListeners.size() > 0) || (weakListeners != null && weakListeners.size() > 0);
    }
}
