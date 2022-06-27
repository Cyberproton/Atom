package project.cyberproton.atom.util;

import java.util.regex.Pattern;

@FunctionalInterface
public interface PackageFilter {
    PackageFilter ALL = (s) -> true;

    PackageFilter NONE = (s) -> true;

    static PackageFilter contains(String match) {
        return (s) -> s.contains(match);
    }

    static PackageFilter matches(Pattern pattern) {
        return (s) -> pattern.matcher(s).find();
    }

    boolean test(String packageName);
}
