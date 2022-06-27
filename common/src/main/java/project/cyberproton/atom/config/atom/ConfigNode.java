package project.cyberproton.atom.config.atom;

import project.cyberproton.atom.util.DoubleRange;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Map;

public interface ConfigNode {
    @Nullable
    String getKey();

    @Nullable
    String getRaw();

    @Nullable
    ConfigNode getParent();

    @Nullable
    ConfigNode node(String path);

    boolean isVirtual();

    int getInt();

    long getLong();

    float getFloat();

    double getDouble();

    @Nullable
    DoubleRange getDoubleRange();

    double getPercentage();

    boolean getBoolean();

    @Nullable
    String getString();

    @Nullable
    ConfigObject getConfigObject();

    @NotNull
    List<ConfigNode> getList();

    @NotNull
    Map<String, ConfigNode> getMap();

    boolean isBoolean();

    boolean isInt();

    boolean isLong();

    boolean isFloat();

    boolean isDouble();

    boolean isDoubleRange();

    boolean isPercentage();

    boolean getBooleanOrElse(boolean def);

    int getIntOrElse(int def);

    long getLongOrElse(long def);

    float getFloatOrElse(float def);

    double getDoubleOrElse(double def);

    @NotNull
    DoubleRange getDoubleRangeOrElse(@NotNull DoubleRange def);

    double getPercentageOrElse(double def);

    @NotNull
    String getStringOrElse(@NotNull String def);

    @NotNull
    ConfigObject getConfigObjectOrElse(@NotNull ConfigObject def);
}
