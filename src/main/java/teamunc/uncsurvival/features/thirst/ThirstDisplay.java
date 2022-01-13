package teamunc.uncsurvival.features.thirst;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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

    public void ActualiseDisplayForPlayers(HashMap<String,Integer> playersScore) {
        playersScore.forEach((playerName, integer) -> {
            Player p = Bukkit.getPlayerExact(playerName);
            if (p != null) {
                String actionBarText = "thirst.normal." + integer;
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TranslatableComponent(actionBarText));
            }
        });
        }
}
