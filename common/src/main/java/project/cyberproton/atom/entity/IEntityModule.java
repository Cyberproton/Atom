package project.cyberproton.atom.entity;

import project.cyberproton.atom.module.Module;
import project.cyberproton.atom.plugin.AtomPlugin;

import org.jetbrains.annotations.NotNull;

public interface IEntityModule<T extends IEntity> extends Module {

    @NotNull
    AtomPlugin getPlugin();

    @NotNull
    T getOwner();

}
