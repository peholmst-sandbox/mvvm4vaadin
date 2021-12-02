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

public final class MappingFunctions {

    private MappingFunctions() {
    }

    public static Boolean invert(Boolean v) {
        return v == null ? null : !v;
    }

    public static boolean isEmptyString(String s) {
        return s == null || s.isEmpty();
    }

    public static boolean isNonEmptyString(String s) {
        return s != null && !s.isEmpty();
    }

    public static boolean isBlankString(String s) {
        return s == null || s.isBlank();
    }

    public static boolean isNonBlankString(String s) {
        return s != null && !s.isBlank();
    }
}
