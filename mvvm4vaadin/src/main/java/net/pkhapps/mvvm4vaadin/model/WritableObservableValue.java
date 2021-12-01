package net.pkhapps.mvvm4vaadin.model;

public interface WritableObservableValue<T> extends ObservableValue<T> {

    void setValue(T value);
}
