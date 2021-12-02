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
