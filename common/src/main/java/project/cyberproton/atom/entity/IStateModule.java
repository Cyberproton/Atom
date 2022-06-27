package project.cyberproton.atom.entity;

import project.cyberproton.atom.Keys;
import project.cyberproton.atom.state.NonExpandableStore;
import project.cyberproton.atom.world.Location;

public interface IStateModule<T extends IEntity> extends IEntityModule<T>, NonExpandableStore {
    default void setHealth(double value) {
        updateSync(Keys.HEALTH, value);
    }

    default double getHealth() {
        return getUnboxed(Keys.HEALTH);
    }

    default Location getLocation() {
        return getUnboxed(Keys.LOCATION);
    }

    default void setLocation(Location location) {
        updateSync(Keys.LOCATION, location);
    }
}
