package project.cyberproton.atom.config.atom;

import project.cyberproton.atom.util.DoubleRange;
import project.cyberproton.atom.util.Utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DefaultConfigNode implements ConfigNode {
    private final String key;
    private final ConfigNode parent;
    private final String raw;
    private Integer asInt;
    private Long asLong;
    private Float asFloat;
    private Double asDouble;
    private DoubleRange asDoubleRange;
    private Double asPercentage;
    private Boolean asBoolean;

    DefaultConfigNode(@Nullable String key, @Nullable ConfigNode parent, @Nullable String raw) {
        this.key = key;
        this.parent = parent;
        this.raw = raw == null ? null : raw.trim();
    }

    @Nullable
    @Override
    public String getKey() {
        return key;
    }

    @Nullable
    @Override
    public String getRaw() {
        return raw;
    }

    @Nullable
    @Override
    public ConfigNode getParent() {
        return parent;
    }

    @Nullable
    @Override
    public ConfigNode node(String path) {
        return null;
    }

    @Override
    public boolean isVirtual() {
        return raw == null;
    }

    @Override
    public int getInt() {
        return getIntOrElse(0);
    }

    @Override
    public long getLong() {
        return getLongOrElse(0L);
    }

    @Override
    public float getFloat() {
        return getFloatOrElse(0F);
    }

    @Override
    public double getDouble() {
        return getDoubleOrElse(0.0);
    }

    @Nullable
    @Override
    public DoubleRange getDoubleRange() {
        if (asDoubleRange != null) return asDoubleRange;
        DoubleRange range = DoubleRange.parseOrNull(raw);
        asDoubleRange = range;
        return range;
    }

    @Override
    public double getPercentage() {
        return getPercentageOrElse(0.0);
    }

    @Override
    public boolean getBoolean() {
        return getBooleanOrElse(false);
    }

    @Nullable
    @Override
    public String getString() {
        return raw;
    }

    @Nullable
    @Override
    public ConfigObject getConfigObject() {
        return null;
    }

    @NotNull
    @Override
    public List<ConfigNode> getList() {
        return ConfigParser.parseListOrElse(this, (node) -> new ArrayList<>());
    }

    @NotNull
    @Override
    public Map<String, ConfigNode> getMap() {
        return null;
    }

    @Override
    public boolean isBoolean() {
        return getRaw() != null && (getRaw().equals("true") || getRaw().equals("false"));
    }

    @Override
    public boolean isInt() {
        return Utils.parseIntOrNull(raw) != null;
    }

    @Override
    public boolean isLong() {
        return Utils.parseLongOrNull(raw) != null;
    }

    @Override
    public boolean isFloat() {
        return Utils.parseFloatOrNull(raw) != null;
    }

    @Override
    public boolean isDouble() {
        return Utils.parseDoubleOrNull(raw) != null;
    }

    @Override
    public boolean isDoubleRange() {
        return getDoubleRange() != null;
    }

    @Override
    public boolean isPercentage() {
        if (raw.length() > 1 && raw.charAt(raw.length() - 1) == '%') {
            return Utils.parseDoubleOrNull(raw.substring(0, raw.length() - 1)) != null;
        }
        return Utils.parseDoubleOrNull(raw) != null;
    }

    @Override
    public int getIntOrElse(int def) {
        if (asInt != null) return asInt;
        if (raw == null) {
            return def;
        }
        Integer res = Utils.parseIntOrNull(raw);
        asInt = res;
        return res == null ? def : res;
    }

    @Override
    public long getLongOrElse(long def) {
        if (asLong != null) return asLong;
        if (raw == null) {
            return def;
        }
        Long res = Utils.parseLongOrNull(raw);
        return res == null ? def : res;
    }

    @Override
    public float getFloatOrElse(float def) {
        if (asFloat != null) return asFloat;
        if (raw == null) {
            return def;
        }
        Float res = Utils.parseFloatOrNull(raw);
        asFloat = res;
        return res == null ? def : res;
    }

    @Override
    public double getDoubleOrElse(double def) {
        if (asDouble != null) return asDouble;
        if (raw == null) {
            return def;
        }
        Double res = Utils.parseDoubleOrNull(raw);
        asDouble = res;
        return res == null ? def : res;
    }

    @NotNull
    @Override
    public DoubleRange getDoubleRangeOrElse(@NotNull DoubleRange def) {
        return getDoubleRange() == null ? def : getDoubleRange();
    }

    @Override
    public double getPercentageOrElse(double def) {
        if (asPercentage != null) return asPercentage;
        if (raw == null) return def;
        if (raw.length() > 1 && raw.charAt(raw.length() - 1) == '%') {
            Double d = Utils.parseDoubleOrNull(raw.substring(0, raw.length() - 1));
            if (d == null) return def;
            asPercentage = d;
            return d;
        }
        Double d = Utils.parseDoubleOrNull(raw);
        asPercentage = d;
        return d == null ? def : d;
    }

    @Override
    public boolean getBooleanOrElse(boolean def) {
        if (asBoolean != null) return asBoolean;
        if (raw == null) {
            return def;
        }
        Boolean res = Utils.parseBooleanOrNull(raw);
        asBoolean = res;
        return res == null ? def : res;
    }

    @NotNull
    @Override
    public String getStringOrElse(@NotNull String def) {
        return raw == null ? def : raw;
    }

    @NotNull
    @Override
    public ConfigObject getConfigObjectOrElse(@NotNull ConfigObject def) {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultConfigNode that = (DefaultConfigNode) o;
        return Objects.equals(key, that.key) && Objects.equals(parent, that.parent) && Objects.equals(raw, that.raw);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, parent, raw);
    }

    @Override
    public String toString() {
        return "DefaultConfigNode{" +
               "key='" + key + '\'' +
               ", hasParent=" + (parent != null) +
               ", raw='" + raw + '\'' +
               '}';
    }
}
