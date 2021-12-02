package net.pkhapps.mvvm4vaadin.model;

import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableSupplier;

import java.util.Collection;

import static java.util.Objects.requireNonNull;

public class ComputedValue<T> extends AbstractComputedValue<T> {

    private final SerializableSupplier<T> valueSupplier;

    private final SerializableConsumer<Object> dependencyValueChangeListener = (event) -> updateCachedValue();

    public ComputedValue(SerializableSupplier<T> valueSupplier, Collection<? extends Observable<?>> dependencies) {
        requireNonNull(valueSupplier, "valueSupplier must not be null");
        requireNonNull(dependencies, "dependencies must not be null");
        this.valueSupplier = valueSupplier;
        dependencies.forEach(dependency -> dependency.addWeakListener(dependencyValueChangeListener));
        updateCachedValue();
    }

    @Override
    protected T computeValue() {
        return valueSupplier.get();
    }
}
