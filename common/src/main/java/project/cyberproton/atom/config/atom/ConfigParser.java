package project.cyberproton.atom.config.atom;

import project.cyberproton.atom.exception.ConfigParseException;
import project.cyberproton.atom.exception.Exceptions;
import project.cyberproton.atom.util.Utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.*;
import java.util.function.Function;

public class ConfigParser {
    @NotNull
    public static ConfigNode parse(String string) {
        return new DefaultConfigNode(null, null, string);
    }

    @NotNull
    public static List<ConfigNode> parseList(String string) {
        List<ConfigNode> nodes = new ArrayList<>();
        String trimmed = string.trim();
        if (trimmed.charAt(0) == '[') {
            if (trimmed.charAt(trimmed.length() - 1) != ']') {
                throw new ConfigParseException("unbalanced brackets detected at map " + trimmed);
            }
            trimmed = trimmed.substring(1, trimmed.length() - 1);
        }
        if (Utils.isBlank(trimmed)) {
            return new ArrayList<>();
        }
        String[] entries = trimmed.split(",");
        for (String entry : entries) {
            if (Utils.isBlank(entry)) {
                continue;
            }
            nodes.add(new DefaultConfigNode(null, null, entry));
        }
        return nodes;
    }

    public static Map<String, ConfigNode> parseMap(ConfigNode node) {
        Map<String, ConfigNode> nodes = new HashMap<>();
        if (node.getRaw() == null) {
            throw new ConfigParseException("This node is null node");
        }
        for (Map.Entry<String, ConfigNode> entry : parseMap(node.getRaw()).entrySet()) {
            nodes.put(entry.getKey(), new DefaultConfigNode(entry.getKey(), node, entry.getValue().getRaw()));
        }
        return nodes;
    }

    public static Map<String, ConfigNode> parseMap(String string) {
        Map<String, ConfigNode> nodes = new HashMap<>();
        String trimmed = string.trim();
        if (trimmed.isEmpty()) {
            throw new ConfigParseException("String is blank or empty");
        }
        if (trimmed.charAt(0) == '{') {
            if (trimmed.charAt(trimmed.length() - 1) != '}') {
                throw new ConfigParseException("unbalanced braces detected at map " + trimmed);
            }
            trimmed = trimmed.substring(1, trimmed.length() - 1);
        }
        if (Utils.isBlank(trimmed)) {
            return new HashMap<>();
        }
        String[] entries = trimmed.split(";");
        for (String entry : entries) {
            if (Utils.isBlank(entry)) {
                continue;
            }
            ConfigEntry configEntry = parseEntry(entry);
            nodes.put(configEntry.getKey(), configEntry.getValue());
        }
        return nodes;
    }

    private static ConfigEntry parseEntry(String string) {
        String trimmed = string.trim();
        if (trimmed.isEmpty()) {
            throw new ConfigParseException("String is blank or empty");
        }
        int sliceIdx = -1;
        Character separator = null;
        boolean isNormalSeparator = false;
        int outermostOpeningBraceIdx = -1;
        int outermostClosingBraceIdx = -1;
        int outermostOpeningBracketIdx = -1;
        int outermostClosingBracketIdx = -1;
        int braceBalance = 0;
        int bracketBalance = 0;

        for (int i = 0; i < trimmed.length(); i++) {
            char c = trimmed.charAt(i);
            // Highest priority separator
            if (c == '=' && !Objects.equals('=', separator)) {
                sliceIdx = i;
                separator = '=';
                isNormalSeparator = true;
            } else if (Character.isWhitespace(c)) {
                // Lowest priority separator
                if (sliceIdx < 0) {
                    sliceIdx = i;
                    separator = c;
                    isNormalSeparator = true;
                }
            } else if (c == '{') {
                if (outermostOpeningBraceIdx < 0) {
                    outermostOpeningBraceIdx = i;
                    if (!Objects.equals(separator, '=')) {
                        separator = '{';
                        sliceIdx = i;
                    }
                }
                braceBalance++;
            } else if (c == '}') {
                //if (braceBalance == 0) {
                //    throw new ConfigParseException("unbalanced braces detected at entry " + string);
                //}
                braceBalance--;
                if (braceBalance == 0) {
                    outermostClosingBraceIdx = i;
                }
            } else if (c == '[') {
                if (outermostOpeningBracketIdx < 0) {
                    outermostOpeningBracketIdx = i;
                    if (!Objects.equals(separator, '=')) {
                        separator = '[';
                        sliceIdx = i;
                    }
                }
                bracketBalance++;
            } else if (c == ']') {
                //if (bracketBalance == 0) {
                //    throw new ConfigParseException("unbalanced brackets detected at entry " + string);
                //}
                bracketBalance--;
                if (bracketBalance == 0) {
                    outermostClosingBracketIdx = i;
                }
            }
        }
        if (!Objects.equals(separator, '=') && braceBalance != 0) {
            throw new ConfigParseException("unbalanced braces detected at entry " + string);
        }
        if (!Objects.equals(separator, '=') && bracketBalance != 0) {
            throw new ConfigParseException("unbalanced brackets detected at entry " + string);
        }
        if (outermostOpeningBraceIdx > -1 &&
            outermostOpeningBraceIdx < outermostOpeningBracketIdx &&
            outermostClosingBraceIdx < outermostClosingBracketIdx
        ) {
            throw new ConfigParseException("syntax error at entry " + string);
        }
        if (outermostOpeningBracketIdx > -1 &&
            outermostOpeningBracketIdx < outermostOpeningBraceIdx &&
            outermostClosingBracketIdx < outermostClosingBraceIdx
        ) {
            throw new ConfigParseException("syntax error at entry " + string);
        }
        if (sliceIdx < 0) {
            throw new ConfigParseException("syntax error at entry " + string);
        }

        // Slice time!
        String first = trimmed.substring(0, sliceIdx).trim();
        if (!Constants.VALID_KEY_PATTERN.matcher(first).matches()) {
            throw new ConfigParseException("Invalid key at entry " + trimmed);
        }
        String second = null;
        if (sliceIdx != trimmed.length() - 1) {
            second = trimmed.substring(isNormalSeparator  ? sliceIdx + 1 : sliceIdx);
        }
        return ConfigEntry.of(first, new DefaultConfigNode(first, null, second));
    }

    @Nullable
    public static Map<String, ConfigNode> parseMapOrNull(@NotNull ConfigNode node) {
        try {
            return parseMap(node);
        } catch (Exception ex) {
            return null;
        }
    }

    @Nullable
    public static Map<String, ConfigNode> parseMapOrNull(String string) {
        try {
            return parseMap(string);
        } catch (Exception ex) {
            return null;
        }
    }

    @Nullable
    public static List<ConfigNode> parseListOrNull(String string) {
        try {
            return parseList(string);
        } catch (ConfigParseException ex) {
            return null;
        }
    }

    @NotNull
    public static List<ConfigNode> parseListOrElse(@NotNull String string, @NotNull List<ConfigNode> def) {
        try {
            return parseList(string);
        } catch (ConfigParseException ex) {
            return def;
        }
    }

    @Nullable
    public static List<ConfigNode> parseListOrElse(@NotNull String string, @NotNull Function<String, List<ConfigNode>> provider) {
        try {
            return parseList(string);
        } catch (ConfigParseException ex) {
            return provider.apply(string);
        }
    }

    @NotNull
    public static List<ConfigNode> parseList(@NotNull ConfigNode node) {
        if (node.getRaw() == null) return new ArrayList<>();
        List<ConfigNode> nodes = parseList(node.getRaw());
        List<ConfigNode> res = new ArrayList<>();
        for (ConfigNode configNode : nodes) {
            res.add(new DefaultConfigNode(null, node, configNode.getRaw()));
        }
        return res;
    }

    @Nullable
    public static List<ConfigNode> parseListOrNull(@NotNull ConfigNode node) {
        try {
            return parseList(node);
        } catch (ConfigParseException ex) {
            return null;
        }
    }

    @NotNull
    public static List<ConfigNode> parseListOrElse(@NotNull ConfigNode node, @NotNull List<ConfigNode> def) {
        try {
            return parseList(node);
        } catch (ConfigParseException ex) {
            return def;
        }
    }

    @Nullable
    public static List<ConfigNode> parseListOrElse(@NotNull ConfigNode node, @NotNull Function<ConfigNode, List<ConfigNode>> provider) {
        try {
            return parseList(node);
        } catch (ConfigParseException ex) {
            return provider.apply(node);
        }
    }
}
