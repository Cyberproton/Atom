package project.cyberproton.atom.util;

import project.cyberproton.atom.exception.DoubleRangeFormatException;
import project.cyberproton.atom.exception.InvalidDoubleRangeException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DoubleRange {
    private final double min;
    private final double max;
    private final boolean excludeMin;
    private final boolean excludeMax;

    private DoubleRange(double min, double max, boolean excludeMin, boolean excludeMax) {
        if (min == Double.POSITIVE_INFINITY) {
            throw new InvalidDoubleRangeException("min must not equals to +INF");
        }
        if (max == Double.NEGATIVE_INFINITY) {
            throw new InvalidDoubleRangeException("max must not equals to -INF");
        }
        if (min == Double.NEGATIVE_INFINITY && !excludeMin) {
            throw new InvalidDoubleRangeException("min is -INF but min bound is inclusive");
        }
        if (max == Double.POSITIVE_INFINITY && !excludeMax) {
            throw new InvalidDoubleRangeException("max is +INF but max bound is inclusive");
        }
        if (min == max && (excludeMin || excludeMax)) {
            throw new InvalidDoubleRangeException("Must not exclude min or max while min bound and max bound are equals");
        }
        this.min = min;
        this.max = max;
        this.excludeMin = excludeMin;
        this.excludeMax = excludeMax;
    }

    private DoubleRange(double value) {
        this(value, value, true, true);
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public boolean isExcludeMin() {
        return excludeMin;
    }

    public boolean isExcludeMax() {
        return excludeMax;
    }

    public static Builder of() {
        return new Builder();
    }

    @NotNull
    public static DoubleRange parse(@NotNull String string) {
        String s = string.trim();
        if (s.length() < 3) {
            Double d = Utils.parseDoubleOrNull(s);
            if (d == null) {
                throw new DoubleRangeFormatException(s);
            }
            return new DoubleRange(d);
        }
        char first = s.charAt(0);
        char last = s.charAt(string.length() - 1);
        if (first == '[' || first == '(') {
            if (last != ']' && last != ')') {
                throw new DoubleRangeFormatException(s);
            }
            boolean isExcludeMin = first == '(';
            boolean isExcludeMax = last == ')';
            String inner = string.substring(1, string.length() - 1);
            int comma = s.indexOf(',');
            if (comma == -1) {
                throw new DoubleRangeFormatException(s);
            }
            String firstPart = inner.substring(0, comma);
            Double left;
            if (Utils.isBlank(firstPart)) {
                left = Double.NEGATIVE_INFINITY;
            } else {
                left = Utils.parseDoubleOrNull(firstPart);
            }
            if (left == null) {
                throw new DoubleRangeFormatException(s);
            }
            if (left == Double.NEGATIVE_INFINITY && !isExcludeMin) {
                throw new InvalidDoubleRangeException("min is -INF but min bound is inclusive");
            }
            Double right;
            if (comma == inner.length() - 1) {
                right = Double.POSITIVE_INFINITY;
            } else {
                String secondPart = inner.substring(comma + 1);
                if (Utils.isBlank(secondPart)) {
                    right = Double.POSITIVE_INFINITY;
                } else {
                    right = Utils.parseDoubleOrNull(secondPart);
                }
            }
            if (right == null) {
                throw new DoubleRangeFormatException(s);
            }
            if (right == Double.POSITIVE_INFINITY && !isExcludeMax) {
                throw new InvalidDoubleRangeException("max is +INF but max bound is inclusive");
            }
            return new DoubleRange(left, right, isExcludeMin, isExcludeMax);
        }
        Double d = Utils.parseDoubleOrNull(s);
        if (d == null) {
            throw new DoubleRangeFormatException(s);
        }
        return new DoubleRange(d);
    }

    @Nullable
    public static DoubleRange parseOrNull(@Nullable String string) {
        if (string == null) {
            return null;
        }
        try {
            return parse(string);
        } catch (DoubleRangeFormatException | InvalidDoubleRangeException ex) {
            return null;
        }
    }

    public static final class Builder {
        private double min;
        private double max;
        private boolean excludeMin;
        private boolean excludeMax;

        private Builder() {
        }

        public Builder min(double min) {
            this.min = min;
            return this;
        }

        public Builder max(double max) {
            this.max = max;
            return this;
        }

        public Builder excludeMin(boolean excludeMin) {
            this.excludeMin = excludeMin;
            return this;
        }

        public Builder excludeMax(boolean excludeMax) {
            this.excludeMax = excludeMax;
            return this;
        }

        public DoubleRange build() {
            return new DoubleRange(min, max, excludeMin, excludeMax);
        }
    }
}
