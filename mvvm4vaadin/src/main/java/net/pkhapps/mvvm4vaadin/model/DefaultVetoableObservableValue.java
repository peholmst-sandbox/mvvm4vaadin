package net.pkhapps.mvvm4vaadin.model;

import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.shared.Registration;

import java.util.Objects;

public class DefaultVetoableObservableValue<T> extends DefaultObservableValue<T> implements VetoableObservable<ObservableValue.ValueChangeEvent<T>> {

    private ListenerCollection<VetoableEvent<ValueChangeEvent<T>>> vetoableListeners;

    public DefaultVetoableObservableValue() {
    }

    public DefaultVetoableObservableValue(T initialValue) {
        super(initialValue);
    }

    @Override
    protected void doSetValue(T value) {
        if (vetoableListeners == null || !vetoableListeners.hasListeners()) {
            super.doSetValue(value);
        } else {
            var vetoableEvent = new VetoableEvent<ValueChangeEvent<T>>() {

                boolean vetoed = false;
                boolean postponed = false;
                final ValueChangeEvent<T> event = new ValueChangeEvent<>(DefaultVetoableObservableValue.this, getValue(), value);

                @Override
                public ValueChangeEvent<T> getEvent() {
                    return event;
                }

                @Override
                public void veto() {
                    vetoed = true;
                }

                @Override
                public PostponedVetoableEvent<ValueChangeEvent<T>> postpone() {
                    if (postponed) {
                        throw new IllegalStateException("The operation has already been postponed by another listener");
                    }
                    postponed = true;
                    return new PostponedVetoableEvent<>() {
                        @Override
                        public ValueChangeEvent<T> getEvent() {
                            return event;
                        }

                        @Override
                        public void proceed() {
                            if (!Objects.equals(event.getOldValue(), DefaultVetoableObservableValue.this.getValue())) {
                                throw new IllegalStateException("The value has changed since this change was postponed");
                            }
                            DefaultVetoableObservableValue.super.doSetValue(value);
                        }
                    };
                }
            };
            getVetoableListeners().fireEvent(vetoableEvent);
            if (!vetoableEvent.vetoed && !vetoableEvent.postponed) {
                super.doSetValue(value);
            }
        }
    }

    @Override
    public Registration addVetoableListener(SerializableConsumer<VetoableEvent<ValueChangeEvent<T>>> listener) {
        return getVetoableListeners().addListener(listener);
    }

    @Override
    public void addWeakVetoableListener(SerializableConsumer<VetoableEvent<ValueChangeEvent<T>>> listener) {
        getVetoableListeners().addWeakListener(listener);
    }

    private ListenerCollection<VetoableEvent<ValueChangeEvent<T>>> getVetoableListeners() {
        if (vetoableListeners == null) {
            vetoableListeners = new ListenerCollection<>(this);
        }
        return vetoableListeners;
    }
}
