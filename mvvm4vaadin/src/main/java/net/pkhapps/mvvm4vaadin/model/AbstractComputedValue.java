package net.pkhapps.mvvm4vaadin.model;

import java.util.Objects;

public abstract class AbstractComputedValue<T> extends AbstractObservableValue<T> {

    private T cachedValue;

    protected void updateCachedValue() {
        var old = cachedValue;
        cachedValue = computeValue();
        if (!Objects.equals(old, cachedValue)) {
            fireValueChangeEvent(old, cachedValue);
        }
    }

    protected abstract T computeValue();

    @Override
    public T getValue() {
        return cachedValue;
    }
}
