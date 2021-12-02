package net.pkhapps.mvvm4vaadin.model;

import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.shared.Registration;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public abstract class AbstractObservableValue<T> extends AbstractObservable<ObservableValue.ValueChangeEvent<T>> implements ObservableValue<T> {

    @Override
    public Registration addListener(SerializableConsumer<? super ValueChangeEvent<T>> listener, boolean fireInitialEvent) {
        var registration = getListeners().addListener(listener);
        if (fireInitialEvent) {
            var value = getValue();
            listener.accept(new ValueChangeEvent<>(this, value, value));
        }
        return registration;
    }

    @Override
    public void addWeakListener(SerializableConsumer<? super ValueChangeEvent<T>> listener, boolean fireInitialEvent) {
        getListeners().addWeakListener(listener);
        if (fireInitialEvent) {
            var value = getValue();
            listener.accept(new ValueChangeEvent<>(this, value, value));
        }
    }

    protected void fireValueChangeEvent(T old, T value) {
        fireEvent(new ValueChangeEvent<>(this, old, value));
    }

    @Override
    public <E> ObservableValue<E> map(SerializableFunction<T, E> mappingFunction) {
        return new MappedObservableValue<>(this, mappingFunction);
    }

    private static class MappedObservableValue<E, T> extends AbstractObservableValue<E> {

        private final ObservableValue<T> source;
        private final SerializableFunction<T, E> mappingFunction;
        @SuppressWarnings("FieldCanBeLocal") // Needed to prevent premature GC
        private final SerializableConsumer<ValueChangeEvent<T>> sourceValueListener = this::onSourceValueChangeEvent;

        private MappedObservableValue(ObservableValue<T> source, SerializableFunction<T, E> mappingFunction) {
            this.source = requireNonNull(source, "source must not be null");
            this.mappingFunction = requireNonNull(mappingFunction, "mappingFunction must not be null");
            source.addWeakListener(sourceValueListener);
        }

        private void onSourceValueChangeEvent(ValueChangeEvent<T> event) {
            var oldValue = mappingFunction.apply(event.getOldValue());
            var newValue = mappingFunction.apply(event.getValue());
            if (!Objects.equals(oldValue, newValue)) {
                fireValueChangeEvent(oldValue, newValue);
            }
        }

        @Override
        public E getValue() {
            return mappingFunction.apply(source.getValue());
        }
    }
}
