package teamunc.uncsurvival.eventsListeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.*;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.manager.ItemsManager;
import teamunc.uncsurvival.logic.team.Team;

import java.util.ArrayList;
import java.util.List;

public class BlockListener extends AbstractEventsListener {
    public BlockListener(UNCSurvival plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        if (block != null && event.getAction() == Action.RIGHT_CLICK_BLOCK && !event.getPlayer().isSneaking()) {
            if (CanDoThisHere(player,block.getLocation()) && !this.plugin.getGameManager().getItemsManager().isCustomItem(player.getInventory().getItemInMainHand(),"WRENCH")) {
                switch (block.getType()) {
                    case BARRIER:
                        this.plugin.getGameManager().getInterfacesManager().ouvrirInterface(block.getLocation(), event.getPlayer());
                        event.setCancelled(true);
                        break;
                    case SMOOTH_STONE:
                    case BREWING_STAND:
                        plugin.getGameManager().getCustomBlockManager().interactBlockEvent(event);
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

        if (block.getType() == Material.AMETHYST_BLOCK) {
            event.setDropItems(false);
        }

        if (CanDoThisHere(player,block.getLocation())) {

            if(block.getType() == Material.BREWING_STAND || block.getType() == Material.SMOOTH_STONE) {
                this.plugin.getGameManager().getCustomBlockManager().breakCustomBlock(block);
                return;
            }

            // test outil custom
            ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
            ItemMeta itemMeta = item.getItemMeta();

            if (itemMeta != null) {
                PersistentDataContainer data = itemMeta.getPersistentDataContainer();
                if (data != null && data.has(this.plugin.getGameManager().getItemsManager().getCustomitemKey(), PersistentDataType.STRING)) {
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
                                        switch (bl.getType()) {
                                            case BREWING_STAND:

                                                break;
                                            case SMOOTH_STONE:
                                                this.plugin.getGameManager().getCustomBlockManager().breakCustomBlock(bl);
                                                break;
                                            default:
                                                if (block.getType() == Material.AMETHYST_BLOCK) {
                                                    bl.setType(Material.AIR);
                                                } else bl.breakNaturally(item);
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
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (CanDoThisHere(player,block.getLocation())) {

            switch (block.getType()) {
                case DROPPER:
                    this.plugin.getGameManager().getCustomBlockManager().placeCustomBlock(event);
                    break;
            }
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMoveItemEvent(InventoryMoveItemEvent event) {
        if(this.plugin.getGameManager().getCustomBlockManager().getCustomBlock(event.getDestination().getLocation()) != null) event.setCancelled(true);
    }

    public boolean CanDoThisHere(Player player, Location loc) {
        boolean res = true;

        if(this.plugin.getGameManager().getParticipantManager().isPlaying(player)) {
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
