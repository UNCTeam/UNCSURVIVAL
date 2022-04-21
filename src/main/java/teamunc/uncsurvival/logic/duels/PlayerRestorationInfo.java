package teamunc.uncsurvival.logic.duels;
import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import teamunc.uncsurvival.logic.player.GamePlayer;

/**
 * PlayerRestorationInfo - Used to restore a player to their previous status after a duel
 * @author Austin Dart (Dartanman)
 */
public class PlayerRestorationInfo {

    private UUID uuid;
    private Location loc;
    private ItemStack[] inventoryContents;
    private ItemStack[] armorContents;
    private int xpLevel;
    private GameMode gameMode;
    private double maxHealth;
    private double currentHealth;
    private GamePlayer gp;
    private Collection<PotionEffect> effects;

    /**
     * Constructs a new PlayerRestorationInfo object for the given player
     * @param gamePlayer
     */
    public PlayerRestorationInfo(GamePlayer gamePlayer) {
        if(gamePlayer == null) {
            return;
        }
        this.gp = gamePlayer;
        Player player = gamePlayer.getBukkitPlayer();

        this.uuid = player.getUniqueId();

        loc = player.getLocation();

        // inventory
        ArrayList<ItemStack> invTemp = new ArrayList<>();
        ItemStack[] invContents = player.getInventory().getContents();
        for (ItemStack it : invContents) {
            if ( it != null ) invTemp.add(it.clone());
            else invTemp.add(null);
        }
        this.inventoryContents = invTemp.toArray(new ItemStack[0]);

        // armor content
        ArrayList<ItemStack> armorTemp = new ArrayList<>();
        ItemStack[] ArmorContents = player.getInventory().getArmorContents();
        for (ItemStack it : ArmorContents) {
            if ( it != null ) armorTemp.add(it.clone());
            else armorTemp.add(null);
        }
        this.armorContents = armorTemp.toArray(new ItemStack[0]);

        xpLevel = player.getLevel();
        gameMode = player.getGameMode();
        maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        currentHealth = player.getHealth();
        effects = player.getActivePotionEffects();
    }

    /**
     * Returns the player this PRI was made for
     * @return
     *   The player
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    /**
     * Applies the previous statuses to the player
     */
    public void apply() {

        getPlayer().teleport(loc);
        //getPlayer().getInventory().setContents(inventoryContents);
        //getPlayer().getInventory().setArmorContents(armorContents);
        getPlayer().setLevel(xpLevel);
        getPlayer().setGameMode(gameMode);
        getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
        if (getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() >= currentHealth)
            getPlayer().setHealth(currentHealth);
        else
            getPlayer().setHealth(getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());

        for (PotionEffect pE : this.effects) {
            getPlayer().addPotionEffect(pE);
        }
    }

    public GamePlayer getGamePlayer() {
        return this.gp;
    }
}
