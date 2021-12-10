/*
 * Copyright (c) 2021 Petter Holmström
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.pkhapps.mvvm4vaadin.binder;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.textfield.TextField;
import net.pkhapps.mvvm4vaadin.model.ObservableList;
import net.pkhapps.mvvm4vaadin.model.ObservableValue;
import org.junit.jupiter.api.Test;

import static net.pkhapps.mvvm4vaadin.binder.BindingFactory.*;
import static net.pkhapps.mvvm4vaadin.model.ModelFactory.observableList;
import static net.pkhapps.mvvm4vaadin.model.ModelFactory.observableValue;
import static org.junit.jupiter.api.Assertions.*;

public class BindingFactoryTest {

    @Test
    void testBindEnabledOnAttach() {
        var model = observableValue(false);
        var view = new TextField();
        bindEnabledOnAttach(model, view);
        assertTrue(view.isEnabled());

        simulateAttach(view);
        assertFalse(view.isEnabled());
        model.setValue(true);
        assertTrue(view.isEnabled());

        simulateDetach(view);
        model.setValue(false);
        assertTrue(view.isEnabled());
    }

    @Test
    void testBindVisibleOnAttach() {
        var model = observableValue(false);
        var view = new TextField();
        bindVisibleOnAttach(model, view);
        assertTrue(view.isVisible());

        simulateAttach(view);
        assertFalse(view.isVisible());
        model.setValue(true);
        assertTrue(view.isVisible());

        simulateDetach(view);
        model.setValue(false);
        assertTrue(view.isVisible());
    }

    @Test
    void testBindTextOnAttach() {
        var model = observableValue("hello");
        var view = new Span();
        bindTextOnAttach(model, view);
        assertEquals("", view.getText());

        simulateAttach(view);
        assertEquals("hello", view.getText());
        model.setValue("world");
        assertEquals("world", view.getText());

        simulateDetach(view);
        model.setValue("");
        assertEquals("world", view.getText());
    }

    @Test
    void testBindThemeOnAttach() {
        var model = observableValue("my-theme");
        var view = new TextField();
        bindThemeOnAttach(model, view);
        assertTrue(view.getThemeNames().isEmpty());

        simulateAttach(view);
        assertTrue(view.getThemeNames().contains("my-theme"));
        model.setValue("my-other-theme");
        assertFalse(view.getThemeNames().contains("my-theme"));
        assertTrue(view.getThemeNames().contains("my-other-theme"));
        model.setValue(null);
        assertTrue(view.getThemeNames().isEmpty());

        simulateDetach(view);
        model.setValue("my-theme");
        assertTrue(view.getThemeNames().isEmpty());
    }

    @Test
    void testBindClassNameOnAttach() {
        var model = observableValue("my-class");
        var view = new TextField();
        bindClassNameOnAttach(model, view);
        assertTrue(view.getClassNames().isEmpty());

        simulateAttach(view);
        assertTrue(view.getClassNames().contains("my-class"));
        model.setValue("my-other-class");
        assertFalse(view.getClassNames().contains("my-class"));
        assertTrue(view.getClassNames().contains("my-other-class"));
        model.setValue(null);
        assertTrue(view.getClassNames().isEmpty());

        simulateDetach(view);
        model.setValue("my-class");
        assertTrue(view.getClassNames().isEmpty());
    }

    @Test
    void testBindErrorMessageOnAttach() {
        var model = observableValue("error");
        var view = new TextField();
        bindErrorMessageOnAttach(model, view);
        assertNull(view.getErrorMessage());

        simulateAttach(view);
        assertEquals("error", view.getErrorMessage());
        model.setValue(null);
        assertTrue(view.getErrorMessage().isEmpty());

        simulateDetach(view);
        model.setValue("error");
        assertTrue(view.getErrorMessage().isEmpty());
    }

    @Test
    void testBindInvalidOnAttach() {
        var model = observableValue(true);
        var view = new TextField();
        bindInvalidOnAttach(model, view);
        assertFalse(view.isInvalid());

        simulateAttach(view);
        assertTrue(view.isInvalid());
        model.setValue(false);
        assertFalse(view.isInvalid());

        simulateDetach(view);
        model.setValue(true);
        assertFalse(view.isInvalid());
    }

    @Test
    void testBindFieldValue() {
        var model = observableValue("foo");
        var view = new TextField();
        bindFieldValueOnAttach(model, view);
        assertTrue(view.isEmpty());

        simulateAttach(view);
        assertEquals("foo", view.getValue());
        model.setValue(null);
        assertTrue(view.isEmpty());
        model.setValue("bar");
        assertEquals("bar", view.getValue());

        simulateDetach(view);
        model.setValue("foo");
        assertTrue(view.isEmpty()); // Should have been cleared when detached
    }

    @Test
    void testReadOnlyOnAttach() {
        var model = observableValue(true);
        var view = new TextField();
        bindReadOnlyOnAttach(model, view);
        assertFalse(view.isReadOnly());

        simulateAttach(view);
        assertTrue(view.isReadOnly());
        model.setValue(false);
        assertFalse(view.isReadOnly());

        simulateDetach(view);
        model.setValue(true);
        assertFalse(view.isReadOnly());
    }

    @Test
    void testBindRequiredOnAttach() {
        var model = observableValue(true);
        var view = new TextField();
        bindRequiredOnAttach(model, view);
        assertFalse(view.isRequiredIndicatorVisible());

        simulateAttach(view);
        assertTrue(view.isRequiredIndicatorVisible());
        model.setValue(false);
        assertFalse(view.isRequiredIndicatorVisible());

        simulateDetach(view);
        model.setValue(true);
        assertFalse(view.isRequiredIndicatorVisible());
    }

    @Test
    void testBindListDataProviderOnAttach() {
        var model = observableList("hello", "world");
        var view = new ComboBox<String>();
        bindListDataProviderOnAttach(model, view);
        assertEquals(0, view.getListDataView().getItemCount());

        simulateAttach(view);
        assertTrue(view.getListDataView().contains("hello"));
        assertTrue(view.getListDataView().contains("world"));
        model.add("foo");
        assertTrue(view.getListDataView().contains("foo"));
        assertEquals(3, view.getListDataView().getItemCount());

        simulateDetach(view);
        model.add("bar");
        assertEquals(0, view.getListDataView().getItemCount()); // Should have been cleared when detached
    }

    @Test
    void testBindChildrenOnAttach() {
        var model = observableList(new Span("hello"), new Span("world"));
        var view = new Div();
        bindChildrenOnAttach(model, view);
        assertEquals(0, view.getComponentCount());

        simulateAttach(view);
        assertSameContent(model, view);
        model.add(new Span("foo"));
        assertSameContent(model, view);
        model.move(2, 0);
        assertSameContent(model, view);
        model.move(0, 2);
        assertSameContent(model, view);
        model.remove(0);
        assertSameContent(model, view);

        simulateDetach(view);
        model.add(new Span("bar"));
        assertEquals(0, view.getComponentCount()); // Should have been cleared when detached
    }

    @Test
    void testBindContentOnAttach() {
        var model = observableValue(new Span("hello"));
        var view = new Div();
        bindContentOnAttach(model, view);
        assertEquals(0, view.getComponentCount());

        simulateAttach(view);
        assertSameContent(model, view);
        model.setValue(null);
        assertSameContent(model, view);
        model.setValue(new Span("world"));
        assertSameContent(model, view);

        simulateDetach(view);
        model.setValue(new Span("bar"));
        assertEquals(0, view.getComponentCount()); // Should have been cleared when detached
    }

    @Test
    void testBindMethodOnAttach() {
        var model = observableValue("hello");
        var view = new TextField();
        bindMethodOnAttach(model, view, view::setPlaceholder);
        assertNull(view.getPlaceholder());

        simulateAttach(view);
        assertEquals("hello", view.getPlaceholder());
        model.setValue(null);
        assertTrue(view.getPlaceholder().isEmpty());
        model.setValue("world");
        assertEquals("world", view.getPlaceholder());

        simulateDetach(view);
        model.setValue("bar");
        assertEquals("world", view.getPlaceholder());
    }

    private void assertSameContent(ObservableList<? extends Component> model, HasOrderedComponents view) {
        assertEquals(model.getSize(), view.getComponentCount());
        for (int i = 0; i < model.getSize(); ++i) {
            assertEquals(model.get(i), view.getComponentAt(i));
        }
    }

    private void assertSameContent(ObservableValue<? extends Component> model, HasComponents view) {
        if (model.getValue() == null) {
            assertEquals(0, view.getElement().getChildCount());
        } else {
            assertEquals(1, view.getElement().getChildCount());
            assertEquals(model.getValue().getElement(), view.getElement().getChild(0));
        }
    }

    private static void simulateAttach(Component component) {
        ComponentUtil.fireEvent(component, new AttachEvent(component, true));
    }

    private static void simulateDetach(Component component) {
        ComponentUtil.fireEvent(component, new DetachEvent(component));
    }
}
