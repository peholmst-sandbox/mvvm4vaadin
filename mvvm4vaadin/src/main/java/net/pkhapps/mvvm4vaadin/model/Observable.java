package net.pkhapps.mvvm4vaadin.model;

import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.shared.Registration;

import java.io.Serializable;

public interface Observable<EVENT> extends Serializable {

    default Registration addListener(SerializableConsumer<? super EVENT> listener) {
        return addListener(listener, true);
    }

    Registration addListener(SerializableConsumer<? super EVENT> listener, boolean fireInitialEvent);

    default void addWeakListener(SerializableConsumer<? super EVENT> listener) {
        addWeakListener(listener, true);
    }

    void addWeakListener(SerializableConsumer<? super EVENT> listener, boolean fireInitialEvent);
}
