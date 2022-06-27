package project.cyberproton.atom.stat;

import com.google.common.reflect.TypeToken;
import project.cyberproton.atom.stat.loader.DoubleStatLoader;
import project.cyberproton.atom.stat.combiner.DoubleStatCombiner;
import project.cyberproton.atom.modifier.DoubleModifier;
import project.cyberproton.atom.state.Key;

import org.jetbrains.annotations.NotNull;

public class DoubleStat extends AbstractStat<DoubleModifier, Double> {
    public DoubleStat(@NotNull Key key) {
        super(key, new TypeToken<DoubleModifier>() {}, new TypeToken<Double>() {});
    }

    @NotNull
    @Override
    public DoubleStatCombiner getCombiner() {
        return new DoubleStatCombiner(this);
    }

    @NotNull
    @Override
    public DoubleStatLoader getLoader() {
        return new DoubleStatLoader(this);
    }
}
