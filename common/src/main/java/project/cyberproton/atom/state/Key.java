package project.cyberproton.atom.state;

import project.cyberproton.atom.Namespaces;

import org.jetbrains.annotations.NotNull;

public interface Key {
    static Key of(String namespace, String value) {
        return new DefaultKey(namespace, value);
    }

    static Key minecraft(String value) {
        return of(Namespaces.MINECRAFT, value);
    }

    static Key atom(String value) {
        return of(Namespaces.ATOM, value);
    }

    @NotNull
    String getNamespace();

    @NotNull
    String getValue();
}
