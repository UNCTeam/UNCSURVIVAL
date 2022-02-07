package teamunc.uncsurvival.eventsListeners;

import org.bukkit.Bukkit;
import org.bukkit.CropState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.*;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Crops;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.eventsListeners.AbstractEventsListener;
import teamunc.uncsurvival.logic.manager.ItemsManager;
import teamunc.uncsurvival.logic.player.GamePlayer;
import teamunc.uncsurvival.logic.team.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockListener extends AbstractEventsListener {
    public BlockListener(UNCSurvival plugin) {
        super(plugin);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if (block != null && event.getAction() == Action.RIGHT_CLICK_BLOCK && !event.getPlayer().isSneaking()) {
            if (CanDoThisHere(player,block.getLocation())) {
                switch (block.getType()) {
                    case BARRIER:
                        this.plugin.getGameManager().getInterfacesManager().ouvrirInterface(block.getLocation(), event.getPlayer());
                        event.setCancelled(true);
                        break;
                    case SMOOTH_STONE:
                        plugin.getGameManager().getCustomBlockManager().interactBlockEvent(event);
                        event.setCancelled(true);
                        break;
                }
            } else {
                event.setCancelled(true);
            }
        }

    }

    @EventHandler
    public void onBreakEvent(BlockBreakEvent event) {
        ItemsManager itemsManager = this.plugin.getGameManager().getItemsManager();

        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (CanDoThisHere(player,block.getLocation())) {

            switch (block.getType()) {
                case BREWING_STAND:
                    itemsManager.getBrewingControler().removeStand(block.getLocation());
                    break;
                case SMOOTH_STONE:
                    this.plugin.getGameManager().getCustomBlockManager().breakCustomBlock(block);
                    break;
            }

            // test outil custom
            ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
            ItemMeta itemMeta = item.getItemMeta();

            if (itemMeta != null) {
                PersistentDataContainer data = itemMeta.getPersistentDataContainer();
                if (data != null) {
                    switch (data.get(this.plugin.getGameManager().getItemsManager().getCustomitemKey(), PersistentDataType.STRING)) {
                        case "AMETHYSTPICKAXE" :
                            if (!player.isSneaking()) {
                                Location locationBlock = block.getLocation();

                                ArrayList<Location> blocksLoc = new ArrayList<>(List.of(
                                        locationBlock.clone().add(0, -1, 0),
                                        locationBlock.clone().add(0, -1, 1),
                                        locationBlock.clone().add(1, -1, 0),
                                        locationBlock.clone().add(1, -1, 1),
                                        locationBlock.clone().add(1, -1, -1),
                                        locationBlock.clone().add(0, -1, -1),
                                        locationBlock.clone().add(-1, -1, 0),
                                        locationBlock.clone().add(-1, -1, 1),
                                        locationBlock.clone().add(-1, -1, -1),

                                        locationBlock.clone().add(0, 0, 1),
                                        locationBlock.clone().add(1, 0, 0),
                                        locationBlock.clone().add(1, 0, 1),
                                        locationBlock.clone().add(1, 0, -1),
                                        locationBlock.clone().add(0, 0, -1),
                                        locationBlock.clone().add(-1, 0, 0),
                                        locationBlock.clone().add(-1, 0, 1),
                                        locationBlock.clone().add(-1, 0, -1),

                                        locationBlock.clone().add(0, 1, 0),
                                        locationBlock.clone().add(0, 1, 1),
                                        locationBlock.clone().add(1, 1, 0),
                                        locationBlock.clone().add(1, 1, 1),
                                        locationBlock.clone().add(1, 1, -1),
                                        locationBlock.clone().add(0, 1, -1),
                                        locationBlock.clone().add(-1, 1, 0),
                                        locationBlock.clone().add(-1, 1, 1),
                                        locationBlock.clone().add(-1, 1, -1)
                                ));

                                for (Location loc : blocksLoc) {
                                    Block bl = loc.getBlock();
                                    if (bl.getType() == event.getBlock().getType() && CanDoThisHere(player,bl.getLocation())) {
                                        bl.breakNaturally(item);
                                        switch (bl.getType()) {
                                            case BREWING_STAND:
                                                itemsManager.getBrewingControler().removeStand(bl.getLocation());
                                                break;
                                            case SMOOTH_STONE:
                                                this.plugin.getGameManager().getCustomBlockManager().breakCustomBlock(bl);
                                                break;
                                        }
                                    }
                                }
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlaceBlockEvent(BlockPlaceEvent event) {
        ItemsManager itemsManager = this.plugin.getGameManager().getItemsManager();

        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (CanDoThisHere(player,block.getLocation())) {

            switch (block.getType()) {
                case BREWING_STAND:
                    itemsManager.getBrewingControler().addStand((BrewingStand) block.getState());
                    break;
                case DROPPER:
                    this.plugin.getGameManager().getCustomBlockManager().placeCustomBlock(event);
                    break;
            }
        } else {
            event.setCancelled(true);
        }
    }

    public boolean CanDoThisHere(Player player, Location loc) {
        boolean res = true;

        if(this.plugin.getGameManager().getParticipantManager().hasPlayer(player)) {
            Team teamPlayer = this.plugin.getGameManager().getParticipantManager().getTeamForPlayer(player);
            ArrayList<Team> teams = (ArrayList<Team>) this.plugin.getGameManager().getTeamsManager().getAllTeams().clone();
            teams.remove(teamPlayer);
            for (Team team : teams) {
                if ( team.getRegion().contains(loc) ) {
                    player.sendMessage("§cVous n'avez pas le droit de poser ou casser des blocs dans la base adverse.");
                    res = false;
                }
            }
        }
        return res;
    }
}
