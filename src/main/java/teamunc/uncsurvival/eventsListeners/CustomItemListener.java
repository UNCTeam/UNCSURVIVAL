package teamunc.uncsurvival.eventsListeners;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.advancements.Advancement;
import teamunc.uncsurvival.logic.customBlock.CustomBlockType;
import teamunc.uncsurvival.logic.customBlock.customStorageBlock.BottlerBlock;
import teamunc.uncsurvival.logic.manager.AdvancementManager;
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
        GamePlayer gamePlayer = this.plugin.getGameManager().getParticipantManager().getGamePlayer(player.getName());

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

                            // advancement
                            Team t = this.plugin.getGameManager().getParticipantManager().getTeamForPlayer(player);
                            AdvancementManager advancementManager = this.plugin.getGameManager().getAdvancementManager();
                            Advancement advancement = advancementManager.getAdvancement("ca_fait_beaucoup_la_non");
                            if(!advancement.alreadyGranted()) {
                                advancementManager.grantToATeam(t,advancement);
                            } else if (
                                    advancement.getTeamColor() != t.getChatColor() &&
                                            !this.plugin.getGameManager().getAdvancementManager().isTeamHalfPointed(t.getChatColor(),advancement)
                            ){
                                this.plugin.getGameManager().getAdvancementManager().setTeamHalfPointedAndGiveItPoints(t.getChatColor(),advancement);
                            }
                        }
                    }

                    break;
                case "BURGER":
                    if (gamePlayer != null) {
                        if ( gamePlayer.EatHowOften(itemStack.getType()) == 3 ) {
                            this.plugin.getMessageTchatManager().sendMessageToPlayer("Vous avez mangé cette nourriture 4 fois ! changez de nourriture.", player, ChatColor.GOLD);
                        } else if ( gamePlayer.EatHowOften(itemStack.getType()) >= 4 ) {
                            this.plugin.getMessageTchatManager().sendMessageToPlayer("Changez de nourriture! Gains nutritionnels divisés par 3!", player, ChatColor.GOLD);

                            // new value of food
                            int newVal = player.getFoodLevel() + 4;
                            player.setFoodLevel(Math.min(newVal, 20));

                            // removing item
                            gamePlayer.getBukkitPlayer().getItemInUse().setAmount(gamePlayer.getBukkitPlayer().getItemInUse().getAmount() - 1);

                            e.setCancelled(true);
                        }

                        if ( gamePlayer.EatHowOften(itemStack.getType()) < 4 ) {
                            e.getPlayer().setFoodLevel(20);
                            gamePlayer.addToEatenFoodQueue(itemStack.getType());
                        }
                    }
                    break;
                case "VEGGIEBURGER":
                    if (gamePlayer != null) {
                        if ( gamePlayer.EatHowOften(itemStack.getType()) == 3 ) {
                            this.plugin.getMessageTchatManager().sendMessageToPlayer("Vous avez mangé cette nourriture 4 fois ! changez de nourriture.", player, ChatColor.GOLD);
                        } else if ( gamePlayer.EatHowOften(itemStack.getType()) >= 4 ) {
                            this.plugin.getMessageTchatManager().sendMessageToPlayer("Changez de nourriture! Gains nutritionnels divisés par 3!", player, ChatColor.GOLD);

                            // new value of food
                            int newVal = player.getFoodLevel() + 3;
                            player.setFoodLevel(Math.min(newVal, 20));

                            // removing item
                            gamePlayer.getBukkitPlayer().getItemInUse().setAmount(gamePlayer.getBukkitPlayer().getItemInUse().getAmount() - 1);

                            e.setCancelled(true);
                        }

                        if ( gamePlayer.EatHowOften(itemStack.getType()) < 4 ) {
                            e.getPlayer().setFoodLevel(16);
                            gamePlayer.addToEatenFoodQueue(itemStack.getType());
                        }
                    }
                    break;
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCustomItemUse(PlayerInteractEvent e) {
        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
        ItemMeta itemMeta = item.getItemMeta();
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();
        if (!this.plugin.getGameManager().getItemsManager().isCustomItem(item)) return;

        if (e.getAction() != Action.LEFT_CLICK_BLOCK && block != null && !e.getPlayer().isSneaking() &&
                (this.plugin.getGameManager().getCustomBlockManager().getCustomBlock(block.getLocation()) != null || this.plugin.getGameManager().getInterfacesManager().getInterface(block.getLocation()) != null)) {
            e.setCancelled(true);
            return;
        }

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            PersistentDataContainer data = itemMeta.getPersistentDataContainer();

            if(data.has(this.plugin.getGameManager().getItemsManager().getCustomitemKey(), PersistentDataType.STRING)) {
                String customType = data.get(this.plugin.getGameManager().getItemsManager().getCustomitemKey(), PersistentDataType.STRING);
                if (customType == null) return;
                boolean used = false;
                switch(customType) {
                    case "HEALPATCH":
                        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,50,1,false,false,false));
                        used = true;
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
                                Boolean isBrewingStand = e.getClickedBlock().getType() == Material.BREWING_STAND;
                                if(isBrewingStand || team != null) {
                                    if(isBrewingStand && !player.isSneaking()) {
                                        // Creation d'un Bottler sur le brewing stand
                                        this.plugin.getGameManager().getCustomBlockManager().addCustomBlock(new BottlerBlock(block.getLocation(), CustomBlockType.BOTTLER_BLOCK));
                                    } else if (team != null){
                                        Location newloc = block.getLocation().add(e.getBlockFace().getDirection());

                                        if (newloc.getBlock().getType() == Material.AIR) {
                                            team.moveInterfaceGoal(blockValue,newloc);
                                        } else return;
                                    }
                                    // Augmente la dura
                                    if ( im.getDamage() > item.getType().getMaxDurability() ) {
                                        // Casse la wrench
                                        e.getPlayer().getInventory().setItemInMainHand(null);
                                        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ITEM_SHIELD_BREAK, 1.0f, 1.0f);
                                    } else {
                                        im.setDamage(im.getDamage() + 2);
                                        item.setItemMeta(im);
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
                if (used) {
                    e.setCancelled(true);
                    item.setAmount(0);
                }
            }

        }

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
