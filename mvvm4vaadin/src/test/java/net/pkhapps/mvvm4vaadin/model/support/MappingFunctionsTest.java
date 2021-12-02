package net.pkhapps.mvvm4vaadin.model.support;

import org.junit.jupiter.api.Test;

import static net.pkhapps.mvvm4vaadin.model.ModelFactory.observableValue;
import static org.junit.jupiter.api.Assertions.*;

public class MappingFunctionsTest {

    @Test
    void testInvert() {
        var model = observableValue(false);
        var mapped = model.map(MappingFunctions::invert);
        assertTrue(mapped.getValue());
        model.setValue(true);
        assertFalse(mapped.getValue());
        model.setValue(null);
        assertNull(mapped.getValue());
    }

    @Test
    void testIsEmptyString() {
        var model = observableValue(String.class);
        var mapped = model.map(MappingFunctions::isEmptyString);
        assertTrue(mapped.getValue());
        model.setValue("foo");
        assertFalse(mapped.getValue());
        model.setValue("");
        assertTrue(mapped.getValue());
    }

    @Test
    void testIsNonEmptyString() {
        var model = observableValue(String.class);
        var mapped = model.map(MappingFunctions::isNonEmptyString);
        assertFalse(mapped.getValue());
        model.setValue("foo");
        assertTrue(mapped.getValue());
        model.setValue("");
        assertFalse(mapped.getValue());
    }

    @Test
    void testIsBlankString() {
        var model = observableValue(String.class);
        var mapped = model.map(MappingFunctions::isBlankString);
        assertTrue(mapped.getValue());
        model.setValue("");
        assertTrue(mapped.getValue());
        model.setValue(" ");
        assertTrue(mapped.getValue());
        model.setValue("foo");
        assertFalse(mapped.getValue());
    }

    @Test
    void testIsNonBlankString() {
        var model = observableValue(String.class);
        var mapped = model.map(MappingFunctions::isNonBlankString);
        assertFalse(mapped.getValue());
        model.setValue("");
        assertFalse(mapped.getValue());
        model.setValue(" ");
        assertFalse(mapped.getValue());
        model.setValue("foo");
        assertTrue(mapped.getValue());
    }
}
