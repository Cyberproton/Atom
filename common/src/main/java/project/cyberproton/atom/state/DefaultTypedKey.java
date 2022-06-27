package project.cyberproton.atom.state;

import com.google.common.reflect.TypeToken;
import project.cyberproton.atom.Atom;
import project.cyberproton.atom.util.ReflectionUtils;

import org.jetbrains.annotations.NotNull;
import java.util.Objects;

@SuppressWarnings("unchecked")
public final class DefaultTypedKey<V> implements TypedKey<V> {
    private final Key key;
    private final TypeToken<V> valueType;

    public DefaultTypedKey(@NotNull Key key, @NotNull TypeToken<V> valueType) {
        this.key = key;
        if (valueType.getRawType().isPrimitive()) {
            this.valueType = (TypeToken<V>) TypeToken.of(ReflectionUtils.getWrapperClass(valueType.getRawType()));
        } else {
            this.valueType = valueType;
        }
    }

    @NotNull
    @Override
    public Key getKey() {
        return key;
    }

    @NotNull
    @Override
    public TypeToken<V> getValueType() {
        return valueType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultTypedKey<?> that = (DefaultTypedKey<?>) o;
        return key.equals(that.key) && valueType.equals(that.valueType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, valueType);
    }

    @Override
    public String toString() {
        return "TypedKey{" +
               "key=" + key +
               ", valueType=" + valueType +
               '}';
    }
}
