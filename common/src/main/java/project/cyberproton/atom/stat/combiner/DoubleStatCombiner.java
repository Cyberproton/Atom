package project.cyberproton.atom.stat.combiner;

import project.cyberproton.atom.stat.Stat;
import project.cyberproton.atom.modifier.DoubleModifier;

import org.jetbrains.annotations.NotNull;
import java.util.Collection;

public class DoubleStatCombiner extends StatCombiner<DoubleModifier, Double> {
    public DoubleStatCombiner(@NotNull Stat<DoubleModifier, Double> stat) {
        super(stat);
    }

    @Override
    public Double combine(@NotNull Collection<? extends DoubleModifier> values) {
        double total = 0;
        for (DoubleModifier value : values) {
            total += value.getValue();
        }
        return total;
    }
}
