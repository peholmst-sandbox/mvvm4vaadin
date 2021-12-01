package net.pkhapps.mvvm4vaadin.model;

public abstract class AbstractObservable<EVENT> implements Observable<EVENT> {

    private ListenerCollection<EVENT> listeners;

    protected ListenerCollection<EVENT> getListeners() {
        if (listeners == null) {
            listeners = new ListenerCollection<>(this);
        }
        return listeners;
    }

    protected void fireEvent(EVENT event) {
        if (listeners != null) {
            listeners.fireEvent(event);
        }
    }
}
