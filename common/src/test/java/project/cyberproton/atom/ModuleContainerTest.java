package project.cyberproton.atom;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import project.cyberproton.atom.module.Module;
import project.cyberproton.atom.module.ModuleContainer;
import project.cyberproton.atom.module.ModuleManager;
import project.cyberproton.atom.module.SimpleModuleManager;
import project.cyberproton.atom.stat.DoubleStat;
import project.cyberproton.atom.stat.Stat;
import project.cyberproton.atom.modifier.DoubleModifier;
import project.cyberproton.atom.modifier.Modifier;
import project.cyberproton.atom.state.Key;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModuleContainerTest {
    @Test
    public void registerModules() {
        ModuleManager<?> moduleManager = getExampleModule();
        assertEquals(5, moduleManager.getModules().size());
    }

    @Test
    public void sortedModuleByDeps() {
        SimpleModuleManager<?> moduleManager = getExampleModule();
        List<? extends ModuleContainer<?>> moduleDatumByDependencies = moduleManager.getSortedModuleDatumByDependencies(false);
        assertEquals(5, moduleDatumByDependencies.size());
    }

    @Test
    public void sortedModuleByDeps2() {
        SimpleModuleManager<?> moduleManager = getExampleModule();
        List<? extends ModuleContainer<?>> moduleDatumByDependencies = moduleManager.getSortedModuleDatumByDependencies(false);
        assertEquals(
            Lists.newArrayList(DummyModule1.class, DummyModule2.class, DummyModule2.class, ModuleHasDepends.class, ModuleHasDepends2.class),
            moduleDatumByDependencies
                .stream()
                .map(mc -> mc.getModule().getClass())
                .collect(Collectors.toList())
        );
    }

    public void requireNotNull(@NotNull Integer notNull) {
        int i = 1;
        int j = 2;
        int k = notNull + 3;
    }

    public static SimpleModuleManager<?> getExampleModule() {
        SimpleModuleManager<Module> moduleManager = SimpleModuleManager.create();
        moduleManager.registerModule(new ModuleHasDepends2());
        moduleManager.registerModule(new DummyModule1());
        moduleManager.registerModule(new ModuleHasDepends());
        moduleManager.registerModule(new DummyModule2());
        moduleManager.registerModule(new DummyModule2());
        return moduleManager;
    }
}
