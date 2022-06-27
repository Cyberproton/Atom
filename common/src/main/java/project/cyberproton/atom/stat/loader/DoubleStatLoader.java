package project.cyberproton.atom.stat.loader;

import org.spongepowered.configurate.ConfigurationNode;
import project.cyberproton.atom.stat.Stat;
import project.cyberproton.atom.modifier.DoubleModifier;
import project.cyberproton.atom.util.Utils;

import org.jetbrains.annotations.NotNull;

public class DoubleStatLoader extends GenericStatLoader<DoubleModifier, Double> {
    public DoubleStatLoader(Stat<DoubleModifier, Double> stat) {
        super(stat);
    }

    @Override
    public Double load(@NotNull ConfigurationNode node) {
        return Utils.parseDoubleOrNull(node.getString());
    }
}
