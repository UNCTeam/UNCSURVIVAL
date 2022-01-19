package teamunc.uncsurvival.eventsListeners.customsBlock;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.eventsListeners.AbstractEventsListener;
import teamunc.uncsurvival.logic.team.Team;

public class BlockListener extends AbstractEventsListener {
    public BlockListener(UNCSurvival plugin) {
        super(plugin);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta != null) {
            PersistentDataContainer data = itemMeta.getPersistentDataContainer();
            if(data.has(this.plugin.getGameManager().getItemsManager().getWrenchKey(), PersistentDataType.INTEGER)) {
                Integer blockValue = data.get(this.plugin.getGameManager().getItemsManager().getWrenchKey(), PersistentDataType.INTEGER);
                final Damageable im = (Damageable) itemMeta;
                if(e.getAction() == Action.RIGHT_CLICK_AIR) {
                    // Switch le mode de la wrench
                    e.getPlayer().getInventory().setItemInMainHand(plugin.getGameManager().getItemsManager().giveNextWrenchItem(blockValue, im.getDamage()));
                } else if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    Team team = plugin.getGameManager().getParticipantManager().getTeamForPlayer(e.getPlayer());
                    if(team != null) {
                        team.moveInterfaceGoal(blockValue, block.getLocation().add(e.getBlockFace().getDirection()));
                        // Augmente la dura
                        if(im.getDamage() > 30) {
                            // Casse la wrench
                            e.getPlayer().getInventory().setItemInMainHand(null);
                            e.getPlayer().sendMessage("soo");
                            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ITEM_SHIELD_BREAK, 1.0f, 1.0f);
                        } else {
                            e.getPlayer().getInventory().setItemInMainHand(plugin.getGameManager().getItemsManager().createWrenchItem(blockValue, im.getDamage()+2));
                        }
                    } else {
                        e.getPlayer().sendMessage("§cYou need to be in a team");
                    }
                }
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
