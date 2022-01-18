package teamunc.uncsurvival.eventsListeners.customsBlock;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.eventsListeners.AbstractEventsListener;
import teamunc.uncsurvival.logic.player.GamePlayer;
import teamunc.uncsurvival.logic.team.Team;

import java.util.ArrayList;

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
                        e.getPlayer().sendMessage("move");
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

    @EventHandler
    public void onBreakEvent(BlockBreakEvent event) {
        if(!this.plugin.getGameManager().getParticipantManager().hasPlayer(event.getPlayer()))
            return;
        this.handleTeamBlock(event, event.getPlayer(), event.getBlock());
    }

    @EventHandler
    public void onPlaceBlockEvent(BlockPlaceEvent event) {
        if(!this.plugin.getGameManager().getParticipantManager().hasPlayer(event.getPlayer()))
            return;
        this.handleTeamBlock(event, event.getPlayer(), event.getBlock());
    }

    public void handleTeamBlock(Cancellable event, Player player, Block block) {
        Team teamPlayer = this.plugin.getGameManager().getParticipantManager().getTeamForPlayer(player);
        ArrayList<Team> teams = (ArrayList<Team>) this.plugin.getGameManager().getTeamsManager().getAllTeams().clone();
        teams.remove(teamPlayer);
        for(Team team : teams) {
            if(team.getRegion().contains(block.getLocation())) {
                event.setCancelled(true);
                player.sendMessage("§cVous n'avez pas le droit de poser ou casser des blocs dans la base adverse.");
            }
        }
    }
}
