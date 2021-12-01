package net.pkhapps.mvvm4vaadin.binder;

import com.vaadin.flow.component.*;
import com.vaadin.flow.data.binder.HasItems;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.shared.Registration;
import net.pkhapps.mvvm4vaadin.model.ObservableList;
import net.pkhapps.mvvm4vaadin.model.ObservableValue;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class Binder {

    private Binder() {
    }

    public static <V extends HasEnabled> Registration bindEnabled(ObservableValue<Boolean> model, V view) {
        return model.addListener(event -> {
            if (!Objects.equals(event.getValue(), view.isEnabled())) {
                view.setEnabled(event.getValue());
            }
        }, true);
    }

    public static <V extends Component & HasEnabled> void bindEnabledOnAttach(ObservableValue<Boolean> model, V view) {
        view.addAttachListener(attachEvent -> setRegistration(view, bindEnabled(model, view), "enabled"));
        view.addDetachListener(detachEvent -> getRegistration(view, "enabled").ifPresent(Registration::remove));
    }

    public static Registration bindVisible(ObservableValue<Boolean> model, Component view) {
        return model.addListener(event -> {
            if (!Objects.equals(event.getValue(), view.isVisible())) {
                view.setVisible(event.getValue());
            }
        }, true);
    }

    public static void bindVisibleOnAttach(ObservableValue<Boolean> model, Component view) {
        view.addAttachListener(attachEvent -> setRegistration(view, bindVisible(model, view), "visible"));
        view.addDetachListener(detachEvent -> getRegistration(view, "visible").ifPresent(Registration::remove));
    }

    public static <V extends HasText> Registration bindText(ObservableValue<String> model, V view) {
        return model.addListener(event -> {
            if (!Objects.equals(event.getValue(), view.getText())) {
                view.setText(event.getValue());
            }
        }, true);
    }

    public static <V extends Component & HasText> void bindTextOnAttach(ObservableValue<String> model, V view) {
        view.addAttachListener(attachEvent -> setRegistration(view, bindText(model, view), "text"));
        view.addDetachListener(detachEvent -> getRegistration(view, "text").ifPresent(Registration::remove));
    }

    public static <V extends HasTheme> Registration bindTheme(ObservableValue<String> model, V view) {
        return model.addListener(event -> {
            if (event.getOldValue() != null) {
                view.getThemeNames().remove(event.getOldValue());
            }
            if (event.getValue() != null) {
                view.getThemeNames().add(event.getValue());
            }
        }, true);
    }

    public static <V extends Component & HasTheme> void bindThemeOnAttach(ObservableValue<String> model, V view) {
        view.addAttachListener(attachEvent -> setRegistration(view, bindTheme(model, view), "theme"));
        view.addDetachListener(detachEvent -> getRegistration(view, "theme").ifPresent(Registration::remove));
    }

    public static <V extends HasStyle> Registration bindClassName(ObservableValue<String> model, V view) {
        return model.addListener(event -> {
            if (event.getOldValue() != null) {
                view.getClassNames().remove(event.getOldValue());
            }
            if (event.getValue() != null) {
                view.getClassNames().add(event.getValue());
            }
        }, true);
    }

    public static <V extends Component & HasStyle> void bindClassNameOnAttach(ObservableValue<String> model, V view) {
        view.addAttachListener(attachEvent -> setRegistration(view, bindClassName(model, view), "className"));
        view.addDetachListener(detachEvent -> getRegistration(view, "className").ifPresent(Registration::remove));
    }

    public static <V extends HasValue<?, T>, T> Registration bindFieldValue(ObservableValue<T> model, V view) {
        return model.addListener(event -> {
            if (!Objects.equals(event.getValue(), view.getValue())) {
                view.setValue(event.getValue());
            }
        }, true);
    }

    public static <V extends Component & HasValue<?, T>, T> void bindFieldValueOnAttach(ObservableValue<T> model, V view) {
        view.addAttachListener(attachEvent -> setRegistration(view, bindFieldValue(model, view), "fieldValue"));
        view.addDetachListener(detachEvent -> getRegistration(view, "fieldValue").ifPresent(Registration::remove));
    }

    public static <V extends HasValue<?, ?>> Registration bindReadOnly(ObservableValue<Boolean> model, V view) {
        return model.addListener(event -> {
            if (!Objects.equals(event.getValue(), view.isReadOnly())) {
                view.setReadOnly(event.getValue());
            }
        }, true);
    }

    public static <V extends Component & HasValue<?, ?>> void bindReadOnlyOnAttach(ObservableValue<Boolean> model, V view) {
        view.addAttachListener(attachEvent -> setRegistration(view, bindReadOnly(model, view), "readOnly"));
        view.addDetachListener(detachEvent -> getRegistration(view, "readOnly").ifPresent(Registration::remove));
    }

    public static <V extends HasItems<T>, T> Registration bindItems(ObservableList<T> model, V view) {
        return model.addListener(new SerializableConsumer<>() {

            private List<T> oldItems;

            @Override
            public void accept(ObservableList.ItemChangeEvent<T> event) {
                var newItems = event.getSender().getItems();
                if (!Objects.equals(oldItems, newItems)) {
                    view.setItems(newItems);
                    oldItems = newItems;
                }
            }
        }, true);
    }

    public static <V extends Component & HasItems<T>, T> void bindItemsOnAttach(ObservableList<T> model, V view) {
        view.addAttachListener(attachEvent -> setRegistration(view, bindItems(model, view), "items"));
        view.addDetachListener(detachEvent -> getRegistration(view, "items").ifPresent(Registration::remove));
    }

    private static void setRegistration(Component component, Registration registration, String key) {
        ComponentUtil.setData(component, "binding-" + key, registration);
    }

    private static Optional<Registration> getRegistration(Component component, String key) {
        return Optional.ofNullable(ComponentUtil.getData(component, "binding-" + key)).map(Registration.class::cast);
    }
}
