package teamunc.uncsurvival.eventsListeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
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
                case "DIAMONDAPPLE":
                    double baseValue = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();

                    if ( baseValue >= 32 ) {
                        this.plugin.getMessageTchatManager().sendMessageToPlayer(" Vous ne pouvez avoir plus de coeurs !", player, ChatColor.GOLD);
                        e.setCancelled(true);
                    } else {
                        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(baseValue + 2);
                        if ( (baseValue + 2) == 32 ) {
                            this.plugin.getMessageTchatManager().sendMessageToPlayer(" Vous avez maintenant votre vie au max ! Complétez votre vie avec l'armure en améthyste !", player, ChatColor.GOLD);
                        }
                    }

                    break;
                case "BURGER":
                    e.getPlayer().setFoodLevel(20);
                    break;
            }
        }
    }

    @EventHandler
    public void onCustomItemUse(PlayerInteractEvent e) {
        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
        ItemMeta itemMeta = item.getItemMeta();
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();

        if (itemMeta == null) return;

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            PersistentDataContainer data = itemMeta.getPersistentDataContainer();

            if(data.has(this.plugin.getGameManager().getItemsManager().getCustomitemKey(), PersistentDataType.STRING)) {
                String customType = data.get(this.plugin.getGameManager().getItemsManager().getCustomitemKey(), PersistentDataType.STRING);
                if (customType == null) return;
                boolean used = false;
                switch(customType) {
                    case "HEALPATCH":
                        if (player.getHealth() + 2 <= player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()) {
                            player.setHealth(player.getHealth() + 2);
                            used = true;
                        } else if (player.getHealth() + 1 == player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()){
                            player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
                            used = true;
                        }
                        break;

                    case "VACCIN":
                        GamePlayer gp = this.plugin.getGameManager().getParticipantManager().getGamePlayer(player.getName());

                        if (gp != null && gp.isCovided()) {
                            gp.cureCovid();
                            used = true;
                        }

                        break;

                    case "WRENCH":
                        if(data.has(this.plugin.getGameManager().getItemsManager().getWrenchKey(), PersistentDataType.INTEGER)) {
                            Integer blockValue = data.get(this.plugin.getGameManager().getItemsManager().getWrenchKey(), PersistentDataType.INTEGER);
                            final Damageable im = (Damageable) itemMeta;
                            if ( e.getAction() == Action.RIGHT_CLICK_AIR ) {
                                // Switch le mode de la wrench
                                e.getPlayer().getInventory().setItemInMainHand(plugin.getGameManager().getItemsManager().giveNextWrenchItem(blockValue, im.getDamage()));
                            } else if ( e.getAction() == Action.RIGHT_CLICK_BLOCK && CanDoThisHere(player,block.getLocation())) {
                                Team team = plugin.getGameManager().getParticipantManager().getTeamForPlayer(e.getPlayer());
                                if ( team != null ) {
                                    team.moveInterfaceGoal(blockValue, block.getLocation().add(e.getBlockFace().getDirection()));
                                    // Augmente la dura
                                    if ( im.getDamage() > 30 ) {
                                        // Casse la wrench
                                        e.getPlayer().getInventory().setItemInMainHand(null);
                                        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ITEM_SHIELD_BREAK, 1.0f, 1.0f);
                                    } else {
                                        e.getPlayer().getInventory().setItemInMainHand(plugin.getGameManager().getItemsManager().createWrenchItem(blockValue, im.getDamage() + 2));
                                    }
                                } else {
                                    e.getPlayer().sendMessage("§cVous devez être dans une équipe !");
                                }
                            }
                        }
                        break;
                    case "FAMINESOUP":
                        Team team = this.plugin.getGameManager().getParticipantManager().getTeamForPlayer(player);

                        if (team != null && team.isFamined()) {
                            team.setFamined(false);
                            team.sendToEveryOnlinePlayer("§6Votre Famine à été vaincu par " + player.getName() + "§6 !");
                            used = true;
                        }
                        break;
                }
                if (used) item.setAmount(0);
                e.setCancelled(true);
            }

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
