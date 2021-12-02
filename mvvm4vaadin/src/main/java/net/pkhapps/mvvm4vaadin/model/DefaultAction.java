package net.pkhapps.mvvm4vaadin.model;

import com.vaadin.flow.function.SerializableRunnable;

import static java.util.Objects.requireNonNull;
import static net.pkhapps.mvvm4vaadin.model.ModelFactory.observableValue;

public class DefaultAction implements Action {

    private final SerializableRunnable action;
    private final DefaultObservableValue<Boolean> runnable = observableValue(true);

    public DefaultAction() {
        action = null;
    }

    public DefaultAction(SerializableRunnable action) {
        this.action = requireNonNull(action, "action must not be null");
    }

    @Override
    public void run() {
        if (!runnable.getValue()) {
            throw new IllegalStateException("Action is not runnable at the moment");
        }
        doRun();
    }

    protected void doRun() {
        if (action == null) {
            throw new UnsupportedOperationException("Please override the run() method");
        } else {
            action.run();
        }
    }

    @Override
    public ObservableValue<Boolean> runnable() {
        return runnable;
    }

    public void setRunnable(boolean runnable) {
        this.runnable.setValue(runnable);
    }
}
