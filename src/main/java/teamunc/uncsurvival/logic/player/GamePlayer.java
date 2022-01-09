package teamunc.uncsurvival.logic.player;

import java.util.UUID;

public class GamePlayer {
    protected final UUID uuid;

    public GamePlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return uuid;
    }
}
