package net.pkhapps.mvvm4vaadin.model;

import com.vaadin.flow.function.SerializableFunction;

import java.io.Serializable;

import static java.util.Objects.requireNonNull;

public interface ObservableValue<T> extends Observable<ObservableValue.ValueChangeEvent<T>> {

    T getValue();

    <E> ObservableValue<E> map(SerializableFunction<T, E> mappingFunction);

    class ValueChangeEvent<T> implements Serializable {
        private final ObservableValue<T> sender;
        private final T oldValue;
        private final T value;

        public ValueChangeEvent(ObservableValue<T> sender, T oldValue, T value) {
            this.sender = requireNonNull(sender);
            this.oldValue = oldValue;
            this.value = value;
        }

        public ObservableValue<T> getSender() {
            return sender;
        }

        public T getOldValue() {
            return oldValue;
        }

        public T getValue() {
            return value;
        }
    }
}
