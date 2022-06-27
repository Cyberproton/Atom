package project.cyberproton.atom;

public interface Version {
    String getRaw();

    int getMajor();

    int getMinor();

    default boolean isHigherThan(int major, int minor) {
        return getMajor() > major || getMajor() == major && getMinor() > minor;
    }

    default boolean isHigherThan(Version version) {
        return isHigherThan(version.getMajor(), version.getMinor());
    }

    default boolean isHigherThanOrEqualsTo(int major, int minor) {
        return getMajor() > major || getMajor() == major && getMinor() >= minor;
    }

    default boolean isHigherThanOrEqualsTo(Version version) {
        return isHigherThanOrEqualsTo(version.getMajor(), version.getMinor());
    }

    default boolean isLowerThan(int major, int minor) {
        return getMajor() < major || getMajor() == major && getMinor() < minor;
    }

    default boolean isLowerThanOrEqualsTo(int major, int minor) {
        return getMajor() < major || getMajor() == major && getMinor() <= minor;
    }
}