package project.cyberproton.atom.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Utils {
    @Nullable
    public static Integer parseIntOrNull(@Nullable String string) {
        if (string == null) return null;
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    @Nullable
    public static Double parseDoubleOrNull(@Nullable String string) {
        if (string == null) return null;
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    @Nullable
    public static Float parseFloatOrNull(@Nullable String string) {
        if (string == null) return null;
        try {
            return Float.parseFloat(string);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    @Nullable
    public static Boolean parseBooleanOrNull(@Nullable String string) {
        if (string == null) return null;
        try {
            return Boolean.parseBoolean(string);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    @Nullable
    public static Long parseLongOrNull(@Nullable String string) {
        if (string == null) return null;
        try {
            return Long.parseLong(string);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public static boolean isBlank(@NotNull String string) {
        if (string.isEmpty()) {
            return true;
        }
        boolean allMatches = true;
        for (int i = 0; i < string.length(); i++) {
            if (Character.isWhitespace(string.charAt(i)) || Character.isSpaceChar(string.charAt(i))) {
                continue;
            }
            allMatches = false;
            break;
        }
        return allMatches;
    }
}
