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
