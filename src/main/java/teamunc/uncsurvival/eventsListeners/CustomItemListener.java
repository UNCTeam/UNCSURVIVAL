package teamunc.uncsurvival.eventsListeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.eventsListeners.AbstractEventsListener;
import teamunc.uncsurvival.logic.player.GamePlayer;

public class CustomItemListener extends AbstractEventsListener {

    public CustomItemListener(UNCSurvival plugin) {
        super(plugin);
    }

    @EventHandler
    public void onExecute(PlayerItemConsumeEvent e) {
        // taking Data of the player
        Player player = e.getPlayer();

        // taking Data of the item
        ItemStack itemStack = e.getItem();

        ItemMeta itemMeta = itemStack.getItemMeta();

        PersistentDataContainer data = itemMeta.getPersistentDataContainer();

        // case if objectType
        String customType = data.get(this.plugin.getGameManager().getItemsManager().getCustomitemKey(), PersistentDataType.STRING);
        if (customType != null) {
            switch (customType) {
                case "DiamondApple":
                    double baseValue = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();

                    if ( baseValue >= 40 ) {
                        this.plugin.getMessageTchatManager().sendMessageToPlayer(" Vous aviez déjà 2 barres de coeurs !", player, ChatColor.GOLD);
                        e.setCancelled(true);
                    } else {
                        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(baseValue + 2);
                        if ( (baseValue + 2) == 40 ) {
                            this.plugin.getMessageTchatManager().sendMessageToPlayer(" Vous avez maintenant votre vie au max !", player, ChatColor.GOLD);
                        }
                    }

                    break;
            }
        }
    }

    @EventHandler
    public void onCustomItemUse(PlayerInteractEvent e) {
        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
        ItemMeta itemMeta = item.getItemMeta();
        Player player = e.getPlayer();

        if (itemMeta == null) return;

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            PersistentDataContainer data = itemMeta.getPersistentDataContainer();
            String customType = data.get(this.plugin.getGameManager().getItemsManager().getCustomitemKey(), PersistentDataType.STRING);
            if (customType == null) return;
            boolean used = false;
            switch(customType) {
                case "HealPatch":
                    if (player.getHealth() + 2 <= player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()) {
                        player.setHealth(player.getHealth() + 2);
                        used = true;
                    } else if (player.getHealth() + 1 == player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()){
                        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
                        used = true;
                    }
                    if (used) item.setAmount(0);

                    break;

                case "Vaccin":
                    GamePlayer gp = this.plugin.getGameManager().getParticipantManager().getGamePlayer(player.getName());

                    if (gp != null || gp.isCovided()) {
                        gp.cureCovid();
                        used = true;
                    }
                    if (used) item.setAmount(0);
                    break;
            }
        }

    }
}
