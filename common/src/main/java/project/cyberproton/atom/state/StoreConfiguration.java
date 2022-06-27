package project.cyberproton.atom.state;

import org.jetbrains.annotations.NotNull;
import java.util.*;

public interface StoreConfiguration {

    @NotNull
    static StoreConfiguration empty() {
        return builder().build();
    }

    @NotNull
    static Builder builder() {
        return Builder.create();
    }

    @NotNull
    Collection<KeyValue<?>> getInitialKeyValues();

    @NotNull
    Collection<Interceptor<?>> getInterceptors();

    interface Builder {

        @NotNull
        static Builder create() {
            return DefaultStoreConfiguration.Builder.create();
        }

        @NotNull
        Builder withKeyValues(@NotNull List<KeyValue<?>> keyValues);

        @NotNull
        Builder keyValues(@NotNull KeyValue<?>... keyValues);

        @NotNull
        default <T> Builder keyValues(@NotNull TypedKey<T> key, @NotNull Value<T> value) {
            return keyValues(KeyValue.of(key, value));
        }

        @NotNull
        Builder withInterceptors(@NotNull List<Interceptor<?>> interceptors);

        @NotNull
        Builder interceptors(@NotNull Interceptor<?>... interceptors);

        @NotNull
        DefaultStoreConfiguration build();

    }

}
