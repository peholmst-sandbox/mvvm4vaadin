package net.pkhapps.mvvm4vaadin.model.support;

import com.vaadin.flow.function.SerializableFunction;

import java.util.Objects;

public final class MappingFunctions {

    private MappingFunctions() {
    }

    public static SerializableFunction<Boolean, Boolean> invert() {
        return v -> v == null ? null : !v;
    }

    public static SerializableFunction<?, Boolean> isNull() {
        return Objects::isNull;
    }

    public static SerializableFunction<?, Boolean> isNonNull() {
        return Objects::nonNull;
    }

    public static SerializableFunction<String, Boolean> isEmptyString() {
        return s -> s == null || s.isEmpty();
    }

    public static SerializableFunction<String, Boolean> isNonEmptyString() {
        return s -> s != null && !s.isEmpty();
    }

    public static SerializableFunction<String, Boolean> isBlankString() {
        return s -> s == null || s.isBlank();
    }

    public static SerializableFunction<String, Boolean> isNonBlankString() {
        return s -> s != null && !s.isBlank();
    }
}
