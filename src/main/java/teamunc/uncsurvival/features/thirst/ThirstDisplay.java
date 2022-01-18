package teamunc.uncsurvival.features.thirst;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.player.GamePlayer;

import java.util.ArrayList;
import java.util.HashMap;

public class ThirstDisplay {
    //# SINGLETON
    private static ThirstDisplay instance;
    private ThirstDisplay() {}
    public static ThirstDisplay getInstance() {
        if (ThirstDisplay.instance == null) ThirstDisplay.instance = new ThirstDisplay();
        return ThirstDisplay.instance;
    }
    //# END SINGLETON

    public void ActualiseDisplayForPlayers() {
        ArrayList<GamePlayer> players = UNCSurvival.getInstance().getGameManager().getParticipantManager().getGamePlayers();
        players.forEach(player -> {
            if (player.isOneline()) {
                String actionBarText = "thirst.normal." + player.getWaterLevel();
                player.getBukkitPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TranslatableComponent(actionBarText));
            }
        });
        }
}
