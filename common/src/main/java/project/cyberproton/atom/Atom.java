package project.cyberproton.atom;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.AudienceProvider;
import project.cyberproton.atom.ability.AbilityManager;
import project.cyberproton.atom.gui.GuiManager;
import project.cyberproton.atom.plugin.AtomPlugin;
import project.cyberproton.atom.stat.StatManager;
import project.cyberproton.atom.world.IEntityManager;
import project.cyberproton.atom.logging.Logger;
import project.cyberproton.atom.world.WorldManager;

import org.jetbrains.annotations.NotNull;

public interface Atom {

    @NotNull
    Logger getLog();

    @NotNull
    AudienceProvider audiences();

    @NotNull
    IEntityManager<? extends AtomPlugin> getEntityManager();

    @NotNull
    AbilityManager getAbilityManager();

    @NotNull
    StatManager getStatManager();

    @NotNull
    WorldManager getWorldManager();

    @NotNull
    GuiManager<? extends AtomPlugin> getGuiManager();

    @NotNull
    GlobalConfig globalConfig();

    @NotNull
    static Atom getInstance() {
        return Platform.getAtom();
    }

}
