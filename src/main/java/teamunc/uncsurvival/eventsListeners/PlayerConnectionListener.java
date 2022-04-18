package teamunc.uncsurvival.eventsListeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.utils.scoreboards.InGameInfoScoreboard;

import java.time.LocalDateTime;

public class PlayerConnectionListener extends AbstractEventsListener
{
    public PlayerConnectionListener(UNCSurvival plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerConnection(PlayerJoinEvent playerJoinEvent) {
        if (this.plugin.getGameManager().getGameStats().isGameStarted())
            this.plugin.getGameManager().appliqueStartConstraints(playerJoinEvent.getPlayer());

        plugin.getGameManager().getScoreboardManager().addScoreboard(new InGameInfoScoreboard(playerJoinEvent.getPlayer()));
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"advancement grant @a only uncsurvival:root");
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent playerQuitEvent) {
        if(this.plugin.getGameManager().getGameStats().isGameStarted()) {
            if (this.plugin.getGameManager().getCombatDisconnectManager().isPlayerStillInCombat(LocalDateTime.now(),
                    playerQuitEvent.getPlayer())) {
                this.plugin.getGameManager().getCombatDisconnectManager().triggerDisconnectCombat(playerQuitEvent.getPlayer());
            }
        }
    }
}
