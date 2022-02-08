package teamunc.uncsurvival.eventsListeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.phase.PhaseEnum;
import teamunc.uncsurvival.logic.team.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class playerInGameActionsListener extends AbstractEventsListener {
    public playerInGameActionsListener(UNCSurvival plugin) {
        super(plugin);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();

        for (Team t : this.plugin.getGameManager().getTeamsManager().getAllTeams()) {
            if (this.plugin.getGameManager().getParticipantManager().getTeamForPlayer(p) != t) {
                t.addABonusScore(100);
            }
        }

        this.plugin.getMessageTchatManager().sendGeneralMesssage("§6§lMerci pour la mort! §b§l+100 §6§lpoints pour les autres!");
    }

    @EventHandler
    public void onToolsUse(PlayerItemDamageEvent e) {
        ItemStack itemStack = e.getItem();

        if (this.plugin.getGameManager().getGameStats().getCurrentPhase() == PhaseEnum.PHASE3) {
            // dont apply double damage for customItems
            if ( !this.plugin.getGameManager().getItemsManager().isCustomItem(itemStack,"Wrench") ) {
                e.setDamage(e.getDamage() * 3);
            }
        }
    }

    @EventHandler
    public void onItemDrag(InventoryDragEvent event) {

        for (String s : this.plugin.getGameManager().getInterfacesManager().getAllSymboles()) {
            if ( event.getView().getTitle().contains(s) && event.getRawSlots().stream().filter(integer -> integer < 27).toArray().length != 0 ) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInterfaceOpened(InventoryClickEvent event) {
        InventoryView inventoryView = event.getView();

        if (inventoryView.getTitle().contains("鼻")) {
            // TEAM INTERFACE
            ItemStack itemStack = null;
            if (!event.getAction().toString().contains("DROP")) {

                // Check l'action shift click ou non
                if ( event.getCursor() != null && (((event.isRightClick() || event.isLeftClick()) && event.getRawSlot() < 27) && !event.isShiftClick()) ) {
                    itemStack = event.getCursor();
                } else if (event.isShiftClick()) {
                    itemStack = event.getCurrentItem();
                }

                NamespacedKey key = this.plugin.getGameManager().getItemsManager().getCustomitemKey();

                if (itemStack != null) {
                    if ( itemStack.hasItemMeta() && itemStack.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING) && itemStack.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING).equals("MODULE") ) {

                        itemStack.setAmount(0);
                        this.plugin.getGameManager().getParticipantManager().getTeamForPlayer(inventoryView.getPlayer().getName()).addRange(10);

                        Bukkit.getServer().getScheduler().runTask(this.plugin, () -> {
                            inventoryView.getPlayer().closeInventory();
                            this.plugin.getGameManager().getInterfacesManager().ouvrirInterface(
                                    this.plugin.getGameManager().getParticipantManager().getTeamForPlayer(inventoryView.getPlayer().getName()).getInterfaceTeam(),
                                    Bukkit.getPlayerExact(inventoryView.getPlayer().getName())
                            );
                        });
                    } else event.setCancelled(true);
                }
            }

        } else if (inventoryView.getTitle().contains("本") ||
                    inventoryView.getTitle().contains("北") ||
                    inventoryView.getTitle().contains("被") ||
                    inventoryView.getTitle().contains("背") ||
                    inventoryView.getTitle().contains("备") ){
            // INTERFACE GOAL ITEM
            if (event.isShiftClick() || event.getRawSlot() < 27) event.setCancelled(true);

        } else {
            // OTHER INTERFACES
            plugin.getGameManager().getCustomBlockManager().interfaceInterfact(event);
        }

    }
}
