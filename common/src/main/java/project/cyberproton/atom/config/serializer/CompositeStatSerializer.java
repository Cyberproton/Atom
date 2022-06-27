package project.cyberproton.atom.config.serializer;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;
import project.cyberproton.atom.Atom;
import project.cyberproton.atom.stat.CompositeStat;
import project.cyberproton.atom.stat.Stat;
import project.cyberproton.atom.stat.StatInstance;
import project.cyberproton.atom.stat.loader.StatLoader;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
public class CompositeStatSerializer implements TypeSerializer<CompositeStat> {
    @Override
    public CompositeStat deserialize(Type type, ConfigurationNode node) {
        Atom.getInstance().getLog().dt(getClass().getSimpleName(), "Deserialize stat " + node.key());
        List<StatInstance<?, ?, ?>> instances = new ArrayList<>();
        for (Map.Entry<Object, ? extends ConfigurationNode> entry : node.childrenMap().entrySet()) {
            Atom.getInstance().getLog().dt(getClass().getSimpleName(), "Checking " + entry.getKey());
            Stat stat = Atom.getInstance().getStatManager().getStat(entry.getKey().toString());
            if (stat == null) continue;
            Atom.getInstance().getLog().dt(getClass().getSimpleName(), "Stat: " + stat.getClass().getSimpleName());
            StatLoader<?> loader = stat.getLoader();
            if (loader == null) continue;
            Atom.getInstance().getLog().dt(getClass().getSimpleName(), "Loader pass");
            Object value = loader.load(entry.getValue());
            if (value == null) {
                continue;
            }
            Atom.getInstance().getLog().dt(getClass().getSimpleName(), "Value pass: " + value);
            instances.add(new StatInstance<>(stat, value));
        }
        return CompositeStat.of(instances);
    }

    @Override
    public void serialize(Type type, @Nullable CompositeStat obj, ConfigurationNode node) throws SerializationException {

    }
}
