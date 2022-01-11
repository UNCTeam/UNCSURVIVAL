package teamunc.uncsurvival.eventsListeners;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import teamunc.uncsurvival.UNCSurvival;

public class AppleListener extends AbstractEventsListener{

    public AppleListener(UNCSurvival plugin) {
        super(plugin);
    }

    @EventHandler
    public void onAppleEated(PlayerItemConsumeEvent e) {
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
}
