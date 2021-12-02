package net.pkhapps.mvvm4vaadin.model;

import com.vaadin.flow.function.SerializableSupplier;

import java.util.Collection;
import java.util.List;

public final class ModelFactory {

    private ModelFactory() {
    }

    public static <T> DefaultObservableValue<T> observableValue() {
        return new DefaultObservableValue<>();
    }

    public static <T> DefaultObservableValue<T> observableValue(T initialValue) {
        return new DefaultObservableValue<>(initialValue);
    }

    // To make it possible to use with the var keyword, a static import and still get the right type
    @SuppressWarnings("unused")
    public static <T> DefaultObservableValue<T> observableValue(Class<T> type) {
        return observableValue();
    }

    public static <T> DefaultObservableList<T> observableList() {
        return new DefaultObservableList<>();
    }

    // To make it possible to use with the var keyword, a static import and still get the right type
    @SuppressWarnings("unused")
    public static <T> DefaultObservableList<T> observableList(Class<T> type) {
        return observableList();
    }

    public static <T> DefaultObservableList<T> observableList(Collection<T> initialValue) {
        return new DefaultObservableList<>(initialValue);
    }

    @SafeVarargs
    public static <T> DefaultObservableList<T> observableList(T... initialValue) {
        return observableList(List.of(initialValue));
    }

    public static <T> ComputedValue<T> computedValue(SerializableSupplier<T> valueSupplier, Observable<?>... dependencies) {
        return new ComputedValue<>(valueSupplier, List.of(dependencies));
    }

    public static <T> ComputedValue<T> computedValue(SerializableSupplier<T> valueSupplier, Collection<Observable<?>> dependencies) {
        return new ComputedValue<>(valueSupplier, dependencies);
    }

    public static <T> DefaultVetoableObservableValue<T> vetoableValue() {
        return new DefaultVetoableObservableValue<>();
    }

    public static <T> DefaultVetoableObservableValue<T> vetoableValue(T initialValue) {
        return new DefaultVetoableObservableValue<>(initialValue);
    }

    // To make it possible to use with the var keyword, a static import and still get the right type
    @SuppressWarnings("unused")
    public static <T> DefaultVetoableObservableValue<T> vetoableValue(Class<T> type) {
        return vetoableValue();
    }
}
