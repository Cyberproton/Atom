package project.cyberproton.atom.state;

import org.jetbrains.annotations.NotNull;
import java.util.Objects;

public class DefaultKey implements Key {
    protected final String namespace;
    protected final String value;

    public DefaultKey(@NotNull String namespace, @NotNull String value) {
        this.namespace = namespace;
        this.value = value;
    }

    @NotNull
    @Override
    public String getNamespace() {
        return namespace;
    }

    @NotNull
    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultKey that = (DefaultKey) o;
        return namespace.equals(that.namespace) && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, value);
    }

    @Override
    public String toString() {
        return "Key{" +
               "namespace='" + namespace + '\'' +
               ", value='" + value + '\'' +
               '}';
    }
}
