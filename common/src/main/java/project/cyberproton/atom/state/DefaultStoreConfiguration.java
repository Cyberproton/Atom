package project.cyberproton.atom.state;

import org.jetbrains.annotations.NotNull;
import java.util.*;

public class DefaultStoreConfiguration implements StoreConfiguration {
    private final List<KeyValue<?>> keyValues;
    private final List<Interceptor<?>> interceptors;

    private DefaultStoreConfiguration(List<KeyValue<?>> keyValues, List<Interceptor<?>> interceptors) {
        Objects.requireNonNull(keyValues, "keyValues");
        Objects.requireNonNull(interceptors, "interceptors");
        this.keyValues = keyValues;
        this.interceptors = interceptors;
    }

    @NotNull
    @Override
    public Collection<KeyValue<?>> getInitialKeyValues() {
        return keyValues;
    }

    @NotNull
    @Override
    public Collection<Interceptor<?>> getInterceptors() {
        return interceptors;
    }

    public static final class Builder implements StoreConfiguration.Builder {
        private List<KeyValue<?>> keyValues = new ArrayList<>();
        private List<Interceptor<?>> interceptors = new ArrayList<>();

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        @NotNull
        @Override
        public Builder withKeyValues(@NotNull List<KeyValue<?>> keyValues) {
            Objects.requireNonNull(keyValues, "keyValues");
            this.keyValues = keyValues;
            return this;
        }

        @NotNull
        @Override
        public Builder keyValues(@NotNull KeyValue<?>... keyValues) {
            for (KeyValue<?> keyValue : keyValues) {
                Objects.requireNonNull(keyValue);
            }
            this.keyValues.addAll(Arrays.asList(keyValues));
            return this;
        }

        @NotNull
        @Override
        public Builder withInterceptors(@NotNull List<Interceptor<?>> interceptors) {
            this.interceptors = interceptors;
            return this;
        }

        @NotNull
        @Override
        public Builder interceptors(@NotNull Interceptor<?>... interceptors) {
            for (Interceptor<?> interceptor : interceptors) {
                Objects.requireNonNull(interceptor);
            }
            this.interceptors.addAll(Arrays.asList(interceptors));
            return this;
        }

        @NotNull
        @Override
        public DefaultStoreConfiguration build() {
            return new DefaultStoreConfiguration(keyValues, interceptors);
        }
    }
}
