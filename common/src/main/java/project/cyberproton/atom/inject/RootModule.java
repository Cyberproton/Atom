package project.cyberproton.atom.inject;

import com.google.inject.AbstractModule;
import project.cyberproton.atom.Atom;
import project.cyberproton.atom.Platform;

public class RootModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Atom.class).toInstance(Platform.getAtom());
    }
}
