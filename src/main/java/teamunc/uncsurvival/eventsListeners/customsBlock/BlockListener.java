package teamunc.uncsurvival.eventsListeners.customsBlock;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.eventsListeners.AbstractEventsListener;

public class BlockListener extends AbstractEventsListener {
    public BlockListener(UNCSurvival plugin) {
        super(plugin);
    }

    @EventHandler
    public void onBarrierClick(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        if(e.getAction() == Action.RIGHT_CLICK_AIR) {
            ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
            ItemMeta itemMeta = item.getItemMeta();
            PersistentDataContainer data = itemMeta.getPersistentDataContainer();
            if(data.has(this.plugin.getGameManager().getItemsManager().getWrenchKey(), PersistentDataType.INTEGER)) {
                Integer blockValue = data.get(this.plugin.getGameManager().getItemsManager().getWrenchKey(), PersistentDataType.INTEGER);
                e.getPlayer().getInventory().setItemInMainHand(plugin.getGameManager().getItemsManager().giveNextWrenchItem(blockValue));
            }
        }

        if (block != null && e.getAction() == Action.RIGHT_CLICK_BLOCK && !e.getPlayer().isSneaking()) {
            // TODO check si le joueur est de la bonne team
            if (block.getType() == Material.BARRIER) {
                this.plugin.getGameManager().getInterfacesManager().ouvrirInterface(block.getLocation(),e.getPlayer());
                e.setCancelled(true);
            }
        }

    }
}
