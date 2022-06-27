package project.cyberproton.atom.stat;

import project.cyberproton.atom.modifier.Modifier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class DefaultCompositeStat implements CompositeStat {
    private final List<StatInstance<?, ?, ?>> instanceLst;

    DefaultCompositeStat(List<StatInstance<?, ?, ?>> instances) {
        instanceLst = instances;
    }

    @NotNull
    @Override
    public List<StatInstance<?, ?, ?>> all() {
        return instanceLst;
    }

    @Nullable
    @Override
    public <S extends Stat<M, V>, M extends Modifier<V>, V> List<StatInstance<S, M, V>> instanceOf(Stat<M, V> stat) {
        List<StatInstance<S, M, V>> res = new ArrayList<>();
        for (StatInstance<?, ?, ?> statInstance : instanceLst) {
            if (!statInstance.getStat().equals(stat)) {
                continue;
            }
            res.add(((StatInstance<S, M, V>) statInstance));
        }
        return res;
    }

    @Override
    public String toString() {
        return "DefaultCompositeStat{" +
               "instances=" + instanceLst +
               '}';
    }
}
