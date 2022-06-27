package project.cyberproton.atom.stat;

import project.cyberproton.atom.attribute.AttributeType;
import project.cyberproton.atom.state.Key;

import org.jetbrains.annotations.NotNull;
import java.util.Objects;

public class VanillaStat extends DoubleStat {
    private final AttributeType attributeType;

    public VanillaStat(@NotNull Key key, AttributeType attributeType) {
        super(key);
        Objects.requireNonNull(attributeType, "attributeType");
        this.attributeType = attributeType;
    }

    @NotNull
    public AttributeType getAttributeType() {
        return attributeType;
    }
}
