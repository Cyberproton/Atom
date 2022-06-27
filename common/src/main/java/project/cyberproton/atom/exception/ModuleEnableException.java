package project.cyberproton.atom.exception;

import project.cyberproton.atom.module.Module;

public class ModuleEnableException extends AtomException {
    public ModuleEnableException(Module module, Throwable cause) {
        super(" enabling module " + module.getClass().getName(), cause);
    }
}
