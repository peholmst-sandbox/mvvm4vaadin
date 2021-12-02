package net.pkhapps.mvvm4vaadin.model;

import java.util.Objects;

public class DefaultObservableValue<T> extends AbstractObservableValue<T> implements WritableObservableValue<T> {

    private T value;
    private boolean updatingValue = false;

    public DefaultObservableValue() {
    }

    public DefaultObservableValue(T initialValue) {
        this.value = initialValue;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public void setValue(T value) {
        if (!Objects.equals(this.value, value)) {
            doSetValue(value);
        }
    }

    protected void doSetValue(T value) {
        if (updatingValue) {
            // Prevent value change listeners from changing the value, which would trigger yet another event and so on
            throw new IllegalStateException("The value is being updated");
        }
        try {
            updatingValue = true;
            var old = this.value;
            this.value = value;
            fireValueChangeEvent(old, value);
        } finally {
            updatingValue = false;
        }
    }
}
