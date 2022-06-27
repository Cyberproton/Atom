package project.cyberproton.atom.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Enums {
    @Nullable
    public static <T extends Enum<T>> T valueOfOrNull(@NotNull Class<T> enumCls, @NotNull String name) {
        try {
            return Enum.valueOf(enumCls, name);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
