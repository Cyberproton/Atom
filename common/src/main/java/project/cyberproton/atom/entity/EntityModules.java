package project.cyberproton.atom.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EntityModules {
    public static class ModuleId {
        public static final UUID ABILITY = UUID.fromString("9704ecb8-04f6-4c91-9bc2-db097b103cb6");
        public static final UUID STAT = UUID.fromString("f1ea3475-eff4-4f34-9129-41b93e4ef5bd");
        public static final UUID STATE = UUID.fromString("e9e144f7-8840-491e-9488-4709327adb74");
        public static final UUID MECHANICS = UUID.fromString("aab0d09f-d88a-436e-b06f-57672237348a");

        public static List<UUID> all() {
            List<UUID> lst = new ArrayList<>();
            lst.add(ABILITY);
            lst.add(STAT);
            lst.add(STATE);
            return lst;
        }
    }
}
