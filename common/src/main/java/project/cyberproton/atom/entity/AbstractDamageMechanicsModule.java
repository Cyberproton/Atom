package project.cyberproton.atom.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import project.cyberproton.atom.mechanics.MechanicsExtractContext;
import project.cyberproton.atom.mechanics.MechanicsModifierExtractor;
import project.cyberproton.atom.mechanics.MechanicsType;
import project.cyberproton.atom.modifier.Modifier;
import project.cyberproton.atom.plugin.AtomPlugin;
import project.cyberproton.atom.state.Key;
import project.cyberproton.atom.util.ReflectionUtils;

import java.util.*;

@SuppressWarnings({"unchecked", "UnstableApiUsage"})
public abstract class AbstractDamageMechanicsModule<T extends IEntity> implements IDamageMechanicsModule<T> {
    private final AtomPlugin plugin;
    private final T owner;
    protected final Map<Key, List<Modifier<?>>> typedModifiers = new HashMap<>();
    private final Map<Key, Object> combinedModifierValueCache = new HashMap<>();

    public AbstractDamageMechanicsModule(@NotNull AtomPlugin plugin, @NotNull T owner) {
        Objects.requireNonNull(plugin, "plugin");
        Objects.requireNonNull(owner, "owner");
        this.plugin = plugin;
        this.owner = owner;
    }

    @Override
    public @NotNull UUID getUUID() {
        return EntityModules.ModuleId.MECHANICS;
    }

    @NotNull
    @Override
    public AtomPlugin getPlugin() {
        return plugin;
    }

    @Override
    public @NotNull T getOwner() {
        return owner;
    }

    @Override
    public int getTickRate() {
        return 1;
    }

    @Override
    public void onTick() {

    }

    public <M extends Modifier<V>, V, O> void addModifier(MechanicsType<M, V, O> mechanicsType, M modifier) {
        List<M> list = ((List<M>) typedModifiers.get(mechanicsType.getKey()));
        if (list == null) list = new ArrayList<>();
        list.add(modifier);
        combinedModifierValueCache.remove(mechanicsType.getKey());
    }

    @Nullable
    public <M extends Modifier<V>, V, O> O getCombinedModifierValue(MechanicsType<M, V, O> mechanicsType) {
        if (combinedModifierValueCache.containsKey(mechanicsType.getKey())) {
            return (O) combinedModifierValueCache.get(mechanicsType.getKey());
        }
        List<M> list = ((List<M>) typedModifiers.getOrDefault(mechanicsType.getKey(), new ArrayList<>()));
        MechanicsModifierExtractor<M, O> extractor = mechanicsType.getExtractor();
        O res = null;
        if (extractor != null) {
            res = extractor.extract(list, new MechanicsExtractContext(owner));
        }
        if (res != null) {
            combinedModifierValueCache.put(mechanicsType.getKey(), res);
            return res;
        }
        return (O) ReflectionUtils.getInitialValue(mechanicsType.getDataType().getRawType());
    }

    @NotNull
    public <M extends Modifier<V>, V, O> Collection<M> getModifiers(MechanicsType<M, V, O> mechanicsType) {
        return ((List<M>) typedModifiers.getOrDefault(mechanicsType.getKey(), new ArrayList<>()));
    }
}
