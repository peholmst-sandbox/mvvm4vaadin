package net.pkhapps.mvvm4vaadin.binder;

import com.vaadin.flow.component.*;
import com.vaadin.flow.data.provider.HasListDataView;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.function.SerializableBiFunction;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableRunnable;
import com.vaadin.flow.function.SerializableSupplier;
import com.vaadin.flow.shared.Registration;
import net.pkhapps.mvvm4vaadin.model.ObservableList;
import net.pkhapps.mvvm4vaadin.model.ObservableValue;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Objects.requireNonNull;

public final class BindingFactory {

    private BindingFactory() {
    }

    public static <V extends HasEnabled> Registration bindEnabled(ObservableValue<Boolean> model, V view) {
        requireNonNull(model, "model must not be null");
        requireNonNull(view, "view must not be null");
        return model.addListener(event -> {
            if (event.getValue() != null && !Objects.equals(event.getValue(), view.isEnabled())) {
                view.setEnabled(event.getValue());
            }
        }, true);
    }

    public static <V extends Component & HasEnabled> void bindEnabledOnAttach(ObservableValue<Boolean> model, V view) {
        bindOnAttach(model, view, BindingFactory::bindEnabled);
    }

    public static Registration bindVisible(ObservableValue<Boolean> model, Component view) {
        requireNonNull(model, "model must not be null");
        requireNonNull(view, "view must not be null");
        return model.addListener(event -> {
            if (event.getValue() != null && !Objects.equals(event.getValue(), view.isVisible())) {
                view.setVisible(event.getValue());
            }
        }, true);
    }

    public static void bindVisibleOnAttach(ObservableValue<Boolean> model, Component view) {
        bindOnAttach(model, view, BindingFactory::bindVisible);
    }

    public static <V extends HasText> Registration bindText(ObservableValue<String> model, V view) {
        requireNonNull(model, "model must not be null");
        requireNonNull(view, "view must not be null");
        return model.addListener(event -> {
            if (!Objects.equals(event.getValue(), view.getText())) {
                view.setText(event.getValue());
            }
        }, true);
    }

    public static <V extends Component & HasText> void bindTextOnAttach(ObservableValue<String> model, V view) {
        bindOnAttach(model, view, BindingFactory::bindText);
    }

    public static <V extends HasTheme> Registration bindTheme(ObservableValue<String> model, V view) {
        requireNonNull(model, "model must not be null");
        requireNonNull(view, "view must not be null");
        return model.addListener(event -> {
            if (event.getOldValue() != null && !event.getOldValue().isBlank()) {
                view.getThemeNames().remove(event.getOldValue());
            }
            if (event.getValue() != null && !event.getValue().isBlank()) {
                view.getThemeNames().add(event.getValue());
            }
        }, true);
    }

    public static <V extends Component & HasTheme> void bindThemeOnAttach(ObservableValue<String> model, V view) {
        bindOnAttach(model, view, BindingFactory::bindTheme);
    }

    public static <V extends HasStyle> Registration bindClassName(ObservableValue<String> model, V view) {
        requireNonNull(model, "model must not be null");
        requireNonNull(view, "view must not be null");
        return model.addListener(event -> {
            if (event.getOldValue() != null && !event.getOldValue().isBlank()) {
                view.getClassNames().remove(event.getOldValue());
            }
            if (event.getValue() != null && !event.getValue().isBlank()) {
                view.getClassNames().add(event.getValue());
            }
        }, true);
    }

    public static <V extends Component & HasStyle> void bindClassNameOnAttach(ObservableValue<String> model, V view) {
        bindOnAttach(model, view, BindingFactory::bindClassName);
    }

    public static <V extends HasValidation> Registration bindErrorMessage(ObservableValue<String> model, V view) {
        requireNonNull(model, "model must not be null");
        requireNonNull(view, "view must not be null");
        return model.addListener(event -> {
            if (!Objects.equals(model.getValue(), view.getErrorMessage())) {
                view.setErrorMessage(model.getValue());
            }
        }, true);
    }

    public static <V extends Component & HasValidation> void bindErrorMessageOnAttach(ObservableValue<String> model, V view) {
        bindOnAttach(model, view, BindingFactory::bindErrorMessage);
    }

    public static <V extends HasValidation> Registration bindInvalid(ObservableValue<Boolean> model, V view) {
        requireNonNull(model, "model must not be null");
        requireNonNull(view, "view must not be null");
        return model.addListener(event -> {
            if (event.getValue() != null && !Objects.equals(model.getValue(), view.isInvalid())) {
                view.setInvalid(event.getValue());
            }
        }, true);
    }

    public static <V extends Component & HasValidation> void bindInvalidOnAttach(ObservableValue<Boolean> model, V view) {
        bindOnAttach(model, view, BindingFactory::bindInvalid);
    }

    public static <V extends HasValue<?, T>, T> Registration bindFieldValue(ObservableValue<T> model, V view) {
        requireNonNull(model, "model must not be null");
        requireNonNull(view, "view must not be null");
        return decorateWithRemoveAction(model.addListener(event -> {
            if (event.getValue() == null) {
                view.clear();
            } else if (!Objects.equals(event.getValue(), view.getValue())) {
                view.setValue(event.getValue());
            }
        }, true), view::clear);
    }

    public static <V extends Component & HasValue<?, T>, T> void bindFieldValueOnAttach(ObservableValue<T> model, V view) {
        bindOnAttach(model, view, BindingFactory::bindFieldValue);
    }

    public static <V extends HasValue<?, ?>> Registration bindReadOnly(ObservableValue<Boolean> model, V view) {
        requireNonNull(model, "model must not be null");
        requireNonNull(view, "view must not be null");
        return model.addListener(event -> {
            if (event.getValue() != null && !Objects.equals(event.getValue(), view.isReadOnly())) {
                view.setReadOnly(event.getValue());
            }
        }, true);
    }

    public static <V extends Component & HasValue<?, ?>> void bindReadOnlyOnAttach(ObservableValue<Boolean> model, V view) {
        bindOnAttach(model, view, BindingFactory::bindReadOnly);
    }

    public static <V extends HasValue<?, ?>> Registration bindRequired(ObservableValue<Boolean> model, V view) {
        requireNonNull(model, "model must not be null");
        requireNonNull(view, "view must not be null");
        return model.addListener(event -> {
            if (event.getValue() != null && !Objects.equals(event.getValue(), view.isRequiredIndicatorVisible())) {
                view.setRequiredIndicatorVisible(event.getValue());
            }
        }, true);
    }

    public static <V extends Component & HasValue<?, ?>> void bindRequiredOnAttach(ObservableValue<Boolean> model, V view) {
        bindOnAttach(model, view, BindingFactory::bindRequired);
    }

    public static <V extends HasListDataView<T, ?>, T> Registration bindListDataProvider(ObservableList<T> model, V view) {
        requireNonNull(model, "model must not be null");
        requireNonNull(view, "view must not be null");
        return decorateWithRemoveAction(model.addListener(new SerializableConsumer<>() {
            private final ListDataProvider<T> dataProvider = new ListDataProvider<>(model.getItems());

            {
                view.setItems(dataProvider);
            }

            @Override
            public void accept(ObservableList.ItemChangeEvent<T> event) {
                dataProvider.refreshAll();
            }
        }, true), () -> view.setItems(Collections.emptyList()));
    }

    public static <V extends Component & HasListDataView<T, ?>, T> void bindListDataProviderOnAttach(ObservableList<T> model, V view) {
        bindOnAttach(model, view, BindingFactory::bindListDataProvider);
    }

    public static <V extends HasOrderedComponents, T extends Component> Registration bindChildren(ObservableList<T> model, V view) {
        requireNonNull(model, "model must not be null");
        requireNonNull(view, "view must not be null");
        return decorateWithRemoveAction(model.addListener(event -> {
            if (event.isItemAdded()) {
                view.addComponentAtIndex(event.getNewPosition(), event.getItem());
            } else if (event.isItemRemoved()) {
                var component = view.getComponentAt(event.getOldPosition());
                view.remove(component);
            } else if (event.isItemMoved()) {
                var component = view.getComponentAt(event.getOldPosition());
                view.remove(component);
                var newIndex = event.getNewPosition() > event.getOldPosition() ? event.getNewPosition() - 1 : event.getNewPosition();
                view.addComponentAtIndex(newIndex, component);
            } else if (event.isListChanged()) {
                view.removeAll();
                event.getSender().forEach(view::add);
            }
        }, true), view::removeAll);
    }

    public static <V extends Component & HasOrderedComponents, T extends Component> void bindChildrenOnAttach(ObservableList<T> model, V view) {
        bindOnAttach(model, view, BindingFactory::bindChildren);
    }

    public static <V extends HasComponents, T extends Component> Registration bindContent(ObservableValue<T> model, V view) {
        requireNonNull(model, "model must not be null");
        requireNonNull(view, "view must not be null");
        return decorateWithRemoveAction(model.addListener(event -> {
            view.removeAll();
            if (event.getValue() != null) {
                view.add(event.getValue());
            }
        }, true), view::removeAll);
    }

    public static <V extends Component & HasComponents, T extends Component> void bindContentOnAttach(ObservableValue<T> model, V view) {
        bindOnAttach(model, view, BindingFactory::bindContent);
    }

    public static <T> Registration bindMethod(ObservableValue<T> model, SerializableConsumer<T> method) {
        requireNonNull(model, "model must not be null");
        requireNonNull(method, "method must not be null");
        return model.addListener(event -> method.accept(event.getValue()), true);
    }

    public static <T> void bindMethodOnAttach(ObservableValue<T> model, Component view, SerializableConsumer<T> method) {
        bindOnAttach(view, () -> bindMethod(model, method));
    }

    private static <V extends Component, M> void bindOnAttach(M model, V view, SerializableBiFunction<M, V, Registration> bindingMethod) {
        bindOnAttach(view, () -> bindingMethod.apply(model, view));
    }

    private static final AtomicLong nextBindingId = new AtomicLong(System.currentTimeMillis());

    private static <V extends Component> void bindOnAttach(V view, SerializableSupplier<Registration> registrationSupplier) {
        var key = "binding" + nextBindingId.incrementAndGet();
        view.addAttachListener(attachEvent -> setRegistration(view, registrationSupplier.get(), key));
        view.addDetachListener(detachEvent -> getRegistration(view, key).ifPresent(Registration::remove));
    }

    private static void setRegistration(Component component, Registration registration, String key) {
        ComponentUtil.setData(component, key, registration);
    }

    private static Optional<Registration> getRegistration(Component component, String key) {
        return Optional.ofNullable(ComponentUtil.getData(component, key)).map(Registration.class::cast);
    }

    private static Registration decorateWithRemoveAction(Registration registration, SerializableRunnable removeAction) {
        return () -> {
            try {
                registration.remove();
            } finally {
                removeAction.run();
            }
        };
    }
}