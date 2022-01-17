package teamunc.uncsurvival.eventsListeners.playerChat;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.eventsListeners.AbstractEventsListener;
import teamunc.uncsurvival.logic.team.Team;

public class PlayerChatListener extends AbstractEventsListener {
    public PlayerChatListener(UNCSurvival plugin) {
        super(plugin);
    }

    @EventHandler
    public void onExecute(AsyncPlayerChatEvent event) {
        event.setFormat("%s: %s");
        if(this.plugin.getGameManager().getParticipantManager().getTeamForPlayer(event.getPlayer()) != null) {
            Team teamPlayer = this.plugin.getGameManager().getParticipantManager().getTeamForPlayer(event.getPlayer());
            String format = teamPlayer.getChatColor() + "" + ChatColor.BOLD + "[" + teamPlayer.getName()+ "]"
                    + ChatColor.GRAY + " %s:" + ChatColor.WHITE + " %s";
            event.setFormat(format);
        }

        if(event.getPlayer().getName().equals("UNCDelsus") || event.getPlayer().getName().equals("ValkyrieHD")) {
            String format = ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "[STAFF]"
                    + ChatColor.GRAY + " %s:" + ChatColor.WHITE + " %s";
            event.setFormat(format);
        }
    }
}
