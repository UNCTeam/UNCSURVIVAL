package teamunc.uncsurvival.logic.manager;

import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.player.GamePlayer;
import teamunc.uncsurvival.logic.team.Team;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TabManager extends AbstractManager {

    private List<ChatComponentText> headers = new ArrayList<>();
    private List<ChatComponentText> footers = new ArrayList<>();

    public TabManager(UNCSurvival plugin) {
        super(plugin);
    }

    public void showTab() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                HashMap<GamePlayer, Team> players = plugin.getGameManager().getParticipantManager().getPlayersByTeam();
                for(Player player : Bukkit.getOnlinePlayers()) {
                    String spacer = ChatColor.WHITE + "" + ChatColor.BOLD + " ---------------------- \n";
                    String header = "\n" + ChatColor.RED + "" +ChatColor.BOLD + "----- | UNC SURVIVAL | -----\n\n" + spacer;
                    if(players.containsKey(new GamePlayer(player))) {
                        GamePlayer gamePlayer = new GamePlayer(player);
                        Team teamPlayer = players.get(gamePlayer);
                        player.setPlayerListName(teamPlayer.getChatColor() + " " + ChatColor.BOLD + "[" + teamPlayer.getName() + "]"
                                + teamPlayer.getChatColor() + " " + gamePlayer.getBukkitPlayer().getName());
                        header += ChatColor.GOLD + "Team : " + teamPlayer.getChatColor() + teamPlayer.getName() + "\n";
                    } else {
                        header += ChatColor.GOLD + "Team : " + ChatColor.GRAY + "Spectator" + "\n";
                        player.setPlayerListName(ChatColor.GRAY + player.getName());
                    }
                    header+=ChatColor.GOLD + "Phase : 0  \n";
                    header+=ChatColor.GOLD + "Players : " + ChatColor.WHITE + Bukkit.getOnlinePlayers().size() + "\n" + spacer;
                    player.setPlayerListHeader(header);
                    DecimalFormat df = new DecimalFormat("#.##");
                    String footer = "\n" + ChatColor.GOLD + "" + ChatColor.BOLD + "TPS : " + df.format(MinecraftServer.getServer().recentTps[2]);
                    player.setPlayerListFooter(footer);
                }
            }
        }, 10, 40);
    }
}
