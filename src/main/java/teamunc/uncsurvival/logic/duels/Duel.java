package teamunc.uncsurvival.logic.duels;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.player.GamePlayer;
import teamunc.uncsurvival.utils.LoggerFile;

import java.time.LocalDateTime;
import java.util.*;

public class Duel {

    // Static
    private static final ArrayList<Location> spawns = new ArrayList<>(
        List.of(
            new Location(UNCSurvival.getInstance().getMainWorld(),0,45,15,180,0),
            new Location(UNCSurvival.getInstance().getMainWorld(),0,45,-15,0,0)
        )
    );

    private final ArrayList<PlayerRestorationInfo> playerRestorationInfos = new ArrayList<>();
    private ItemStack loot;

    public Duel(ArrayList<GamePlayer> gamePlayersInGame) {

        gamePlayersInGame.forEach(gamePlayer -> playerRestorationInfos.add(new PlayerRestorationInfo(gamePlayer)));

        randomLootGiven();
    }

    private void randomLootGiven() {
        Random r = new Random();

        int probaTir = r.nextInt(100);

        if ( probaTir <= 5 ) {              // 0 a 5    = 5
            this.loot = new ItemStack(Material.TOTEM_OF_UNDYING,1);
        } else if ( probaTir <= 25 ) {      // 5 a 25   = 20
            this.loot = new ItemStack(Material.DIAMOND,8);
        } else if ( probaTir <= 60 ) {      // 25 a 60  = 35
            this.loot = new ItemStack(Material.GOLD_INGOT,16);
        } else {                            // 55 a 100 = 45
            this.loot = new ItemStack(Material.IRON_INGOT,32);
        }
    }

    public void startDuel() {
        ArrayList<PlayerRestorationInfo> playersResto = this.playerRestorationInfos;
        for (int i = 0; i < playersResto.size(); i++) {
            GamePlayer gp = playersResto.get(i).getGamePlayer();
            Player p = playersResto.get(i).getPlayer();
            gp.setInDuel(true);

            // heal
            p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());

            // tp
            p.teleport(spawns.get(i));

            // remove effects
            p.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(p::removePotionEffect);

            UNCSurvival.getInstance().getMessageTchatManager().sendMessageToPlayer("Info : vous récupérerez tout votre stuff !",p, ChatColor.GOLD);
            UNCSurvival.getInstance().getMessageTchatManager().sendMessageToPlayer("Loot possible en cas de victoire :\n" +
                    "40% -> 32 irons ingots.\n" +
                    "35% -> 16 golds ingots.\n" +
                    "20% -> 8 diamonds.\n" +
                    "5% -> totem of undying",
                    p, ChatColor.GOLD);

            p.sendTitle("§6§lFIGHT",null,1,40,60);
        }
    }

    public void endDuel(GamePlayer winner) {
        // regiving data for each Players
        for (PlayerRestorationInfo playerRestorationInfo : this.playerRestorationInfos) {
            Player pl = playerRestorationInfo.getPlayer();
            GamePlayer gamePlayer = playerRestorationInfo.getGamePlayer();
            pl.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(pl::removePotionEffect);
            gamePlayer.setInDuel(false);

            try {
                playerRestorationInfo.apply();
            } catch (Exception e) {
                System.out.println("impossible d'apply les datas du duel pour le joueur : " + pl.getName());
            }
        }

        // giving loot
        if (winner != null) {
            winner.justWinADuel();
            HashMap<Integer, ItemStack> rest = winner.getBukkitPlayer().getInventory().addItem(this.loot);
            if ( !rest.isEmpty() ) { // drop les items si inventaire full
                winner.getBukkitPlayer().getLocation().getWorld().dropItemNaturally(
                        winner.getBukkitPlayer().getLocation(),
                        rest.get(0)
                );
            }
            ChatColor teamColor = UNCSurvival.getInstance().getGameManager().getParticipantManager().getTeamForPlayer(winner.getBukkitPlayer()).getChatColor();
            UNCSurvival.getInstance().getMessageTchatManager().sendGeneralMesssage("§6Le joueur " + teamColor + winner.getBukkitPlayer().getName() + " §6remporte le duel !");
            LoggerFile.AppendLineToWrite("[DUEL] LE DUEL LE " + LocalDateTime.now() + " A ETE WIN PAR " + winner.getBukkitPlayer().getName());
        } else {
            UNCSurvival.getInstance().getMessageTchatManager().sendGeneralMesssage("Le duel à été annulé !");
            LoggerFile.AppendLineToWrite("[DUEL] LE DUEL LE " + LocalDateTime.now() + " A ETE ANNULE");
        }
    }

    public ArrayList<GamePlayer> getGamePlayersInGame() {
        ArrayList<GamePlayer> res = new ArrayList<>();
        for (PlayerRestorationInfo pri : playerRestorationInfos) {
            res.add(pri.getGamePlayer());
        }
        return res;
    }
}
