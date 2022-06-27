package project.cyberproton.atom.bukkit.event;

import org.bukkit.event.Event;
import project.cyberproton.atom.event.EventManager;

public class BukkitEventManager implements EventManager {
    @Override
    public void dispatch(Object event) {
        if (!(event instanceof Event)) {
            throw new IllegalArgumentException("Event must be Bukkit or Atom event");
        }
    }
}
