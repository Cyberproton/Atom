package project.cyberproton.atom.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ReflectionUtils {
    public final static Map<Class<?>, Class<?>> primitivesToWrappers = new HashMap<>();
    private final static Map<Class<?>, Object> primitivesDefaultValues = new HashMap<>();

    static {
        primitivesToWrappers.put(boolean.class, Boolean.class);
        primitivesToWrappers.put(byte.class, Byte.class);
        primitivesToWrappers.put(short.class, Short.class);
        primitivesToWrappers.put(char.class, Character.class);
        primitivesToWrappers.put(int.class, Integer.class);
        primitivesToWrappers.put(long.class, Long.class);
        primitivesToWrappers.put(float.class, Float.class);
        primitivesToWrappers.put(double.class, Double.class);

        primitivesDefaultValues.put(boolean.class, false);
        primitivesDefaultValues.put(byte.class, (byte) 0);
        primitivesDefaultValues.put(short.class, (short) 0);
        primitivesDefaultValues.put(char.class, '\u0000');
        primitivesDefaultValues.put(int.class, 0);
        primitivesDefaultValues.put(long.class, 0L);
        primitivesDefaultValues.put(float.class, 0F);
        primitivesDefaultValues.put(double.class, 0D);

    }

    @NotNull
    public static Class<?> getWrapperClass(Class<?> primitiveClass) {
        if (!primitiveClass.isPrimitive()) {
            throw new IllegalArgumentException("Passed class is not a primitive class");
        }
        return primitivesToWrappers.get(primitiveClass);
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public static <T> T getPrimitiveInitialValue(Class<T> primitiveClass) {
        if (!primitivesDefaultValues.containsKey(primitiveClass)) {
            throw new IllegalArgumentException("Passed class is not a primitive class");
        }
        return (T) primitivesDefaultValues.get(primitiveClass);
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> T getInitialValue(Class<T> clazz) {
        if (!primitivesDefaultValues.containsKey(clazz)) {
            return null;
        }
        return (T) primitivesDefaultValues.get(clazz);
    }
}
