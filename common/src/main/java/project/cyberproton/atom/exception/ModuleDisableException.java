package project.cyberproton.atom.exception;

import project.cyberproton.atom.module.Module;

public class ModuleDisableException extends AtomException {
    public ModuleDisableException(Module module, Throwable cause) {
        super(" disabling module " + module.getClass().getName(), cause);
    }
}
