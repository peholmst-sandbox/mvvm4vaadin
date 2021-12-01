package net.pkhapps.mvvm4vaadin.model;

import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.shared.Registration;

import java.io.Serializable;

public interface VetoableObservable<EVENT> extends Observable<EVENT> {

    Registration addVetoableListener(SerializableConsumer<VetoableEvent<EVENT>> listener);

    void addWeakVetoableListener(SerializableConsumer<VetoableEvent<EVENT>> listener);

    interface VetoableEvent<EVENT> extends Serializable {

        EVENT getEvent();

        void veto();

        PostponedVetoableEvent<EVENT> postpone();
    }

    interface PostponedVetoableEvent<EVENT> extends Serializable {

        EVENT getEvent();

        void proceed();
    }
}
