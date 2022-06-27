package project.cyberproton.atom.inject;

import project.cyberproton.atom.util.PackageFilter;

import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.function.Predicate;

public interface Injector {

    void inject(@NotNull Object obj);

    void inject(@NotNull Object obj, @NotNull Predicate<Field> predicate);

    interface Config {
        Config DEFAULT = null;

        Set<PackageFilter> getPackageFilters();
    }

}
