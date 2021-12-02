package net.pkhapps.mvvm4vaadin.model;

import java.io.Serializable;

public interface Action extends Serializable, Runnable {

    ObservableValue<Boolean> runnable();
}
