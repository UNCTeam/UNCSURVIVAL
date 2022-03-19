package teamunc.uncsurvival.eventsListeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.utils.scoreboards.InGameInfoScoreboard;

public class PlayerConnectionListener extends AbstractEventsListener
{
    public PlayerConnectionListener(UNCSurvival plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerConnection(PlayerJoinEvent playerJoinEvent) {
        plugin.getGameManager().getScoreboardManager().addScoreboard(new InGameInfoScoreboard(playerJoinEvent.getPlayer()));
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"advancement grant @a only uncsurvival:root");
    }
}
