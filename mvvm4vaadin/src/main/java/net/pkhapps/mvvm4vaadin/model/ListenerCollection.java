package net.pkhapps.mvvm4vaadin.model;

import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.shared.Registration;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

class ListenerCollection<EVENT> implements Serializable {

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final Serializable owner; // Needed to prevent premature garbage collection of the ListenerCollection
    private Set<SerializableConsumer<? super EVENT>> strongListeners;
    private Map<SerializableConsumer<? super EVENT>, Void> weakListeners;

    ListenerCollection(Serializable owner) {
        this.owner = owner;
    }

    void fireEvent(EVENT event) {
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
        if (strongListeners == null) {
            strongListeners = new HashSet<>();
        }
        strongListeners.add(listener);
        return () -> strongListeners.remove(listener);
    }

    void addWeakListener(SerializableConsumer<? super EVENT> listener) {
        if (weakListeners == null) {
            weakListeners = new WeakHashMap<>();
        }
        weakListeners.put(listener, null);
    }
}
