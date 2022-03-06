package teamunc.uncsurvival.logic.duels;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.player.GamePlayer;

import java.util.*;

public class Duel {

    // Static
    private static ArrayList<Location> spawns = new ArrayList<>(
        List.of(
            new Location(UNCSurvival.getInstance().getMainWorld(),0,45,15,180,0),
            new Location(UNCSurvival.getInstance().getMainWorld(),0,45,-15,0,0)
        )
    );

    // saved data of players
    private HashMap<UUID, ItemStack[]> inventories = new HashMap<>();
    private HashMap<UUID, Double> lifes = new HashMap<>();
    private HashMap<UUID, Location> locations = new HashMap<>();
    private HashMap<UUID, Collection<PotionEffect>> effects = new HashMap<>();


    private ArrayList<GamePlayer> gamePlayersInGame;
    private ItemStack loot;

    public Duel(ArrayList<GamePlayer> gamePlayersInGame) {
        this.gamePlayersInGame = gamePlayersInGame;
        randomLootGiven();
        saveDataOfPlayers();
    }

    private void randomLootGiven() {
        Random r = new Random();

        int probaTir = r.nextInt(100);

        if ( probaTir <= 5 ) {              // 0 a 5    = 5
            this.loot = new ItemStack(Material.TOTEM_OF_UNDYING,1);
        } else if ( probaTir <= 25 ) {      // 5 a 25   = 20
            this.loot = new ItemStack(Material.DIAMOND,16);
        } else if ( probaTir <= 60 ) {      // 25 a 60  = 35
            this.loot = new ItemStack(Material.GOLD_INGOT,32);
        } else {                            // 55 a 100 = 45
            this.loot = new ItemStack(Material.IRON_INGOT,48);
        }
    }

    private void saveDataOfPlayers() {
        for (GamePlayer p : this.gamePlayersInGame) {
            Player pl = p.getBukkitPlayer();
            UUID playerUUID = pl.getUniqueId();
            this.inventories.put(playerUUID,pl.getInventory().getContents());
            this.lifes.put(playerUUID,pl.getHealth());
            this.locations.put(playerUUID,pl.getLocation());
            this.effects.put(playerUUID,pl.getActivePotionEffects());
        }
    }

    public void startDuel() {
        ArrayList<GamePlayer> playersInGame = this.gamePlayersInGame;
        for (int i = 0; i < playersInGame.size(); i++) {
            GamePlayer gp = playersInGame.get(i);
            gp.setInDuel(true);
            Player p = gp.getBukkitPlayer();

            // tp
            p.teleport(spawns.get(i));

            // remove effects
            p.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(p::removePotionEffect);

            UNCSurvival.getInstance().getMessageTchatManager().sendMessageToPlayer("Info : vous récupérerez tous votre stuff !",p, ChatColor.GOLD);
            UNCSurvival.getInstance().getMessageTchatManager().sendMessageToPlayer("Loot possible en cas de victoire :\n" +
                    "40% -> 48 irons ingots.\n" +
                    "35% -> 32 golds ingots.\n" +
                    "20% -> 16 diamonds.\n" +
                    "5% -> totem of undying",
                    p, ChatColor.GOLD);

            p.sendTitle("§6§lFIGHT",null,1,40,60);
        }
    }

    public void endDuel(GamePlayer winner) {
        // regiving data for each Players
        for (GamePlayer gamePlayer : this.gamePlayersInGame) {
            Player pl = gamePlayer.getBukkitPlayer();
            UUID playerUUID = pl.getUniqueId();
            pl.getInventory().setContents(this.inventories.get(playerUUID));
            pl.setHealth(this.lifes.get(playerUUID));

            pl.getActivePotionEffects().stream().map(PotionEffect::getType).forEach(pl::removePotionEffect);
            for (PotionEffect pE : this.effects.get(playerUUID)) {
                pl.addPotionEffect(pE);
            }

            gamePlayer.setInDuel(false);
            pl.teleport(this.locations.get(playerUUID));
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
        } else {
            UNCSurvival.getInstance().getMessageTchatManager().sendGeneralMesssage("Le duel à été annulé !");
        }
    }

    public ArrayList<GamePlayer> getGamePlayersInGame() {
        return gamePlayersInGame;
    }
}
