package project.cyberproton.atom.entity;

import project.cyberproton.atom.entity.player.IPlayer;
import project.cyberproton.atom.state.Key;

import org.jetbrains.annotations.NotNull;

public class EntityTypes {
    public static final EntityType<IPlayer> PLAYER = new EntityType<IPlayer>() {
        @NotNull
        @Override
        public Key getKey() {
            return null;
        }

        @NotNull
        @Override
        public String getName() {
            return "PLAYER";
        }
    };
}
