package teamunc.uncsurvival.eventsListeners;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.advancements.Advancement;
import teamunc.uncsurvival.logic.duels.Duel;
import teamunc.uncsurvival.logic.manager.AdvancementManager;
import teamunc.uncsurvival.logic.manager.MessageTchatManager;
import teamunc.uncsurvival.logic.phase.PhaseEnum;
import teamunc.uncsurvival.logic.player.GamePlayer;
import teamunc.uncsurvival.logic.team.Team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class playerInGameActionsListener extends AbstractEventsListener {
    private ShapedRecipe recipe;

    public playerInGameActionsListener(UNCSurvival plugin) {
        super(plugin);

        recipe = new ShapedRecipe(new NamespacedKey(this.plugin,"craftAlcool"),this.plugin.getGameManager().getItemsManager().createAlcool());
        recipe.shape("***","*-*","***");
        recipe.setIngredient('*', Material.ROTTEN_FLESH);
        recipe.setIngredient('-',Material.POTION);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();

        for (Team t : this.plugin.getGameManager().getTeamsManager().getAllTeams()) {
            if (this.plugin.getGameManager().getParticipantManager().getTeamForPlayer(p) != t) {
                t.addABonusScore(100);
            }
        }

        // advancement
        AdvancementManager advancementManager = this.plugin.getGameManager().getAdvancementManager();
        Advancement advancement = advancementManager.getAdvancement("mon_chandail");
        if(!advancement.alreadyGranted()) {
            Team t = this.plugin.getGameManager().getParticipantManager().getTeamForPlayer(p);
            advancementManager.grantToATeam(t,advancement);
        }

        this.plugin.getMessageTchatManager().sendGeneralMesssage("§6§lMerci pour la mort! §b§l+100 §6§lpoints pour les autres!");
    }

    @EventHandler
    public void onDamageTaken(EntityDamageEvent e) {

        if (e.getEntity() instanceof Player) {
            Player victim = (Player) e.getEntity();
            if (e.getDamage() >= victim.getHealth() && this.plugin.getGameManager().getParticipantManager().getGamePlayer(victim.getName()).isInDuel()) { // DUEL
                e.setCancelled(true);

                Duel duel = this.plugin.getGameManager().getGameEventsManager().getDuel();
                for (GamePlayer gp : duel.getGamePlayersInGame()) {
                    if (gp.getBukkitPlayer().getUniqueId() != victim.getUniqueId()) duel.endDuel(gp);
                }

                this.plugin.getGameManager().getGameEventsManager().endDuel();
            }
        }
    }

    @EventHandler
    public void onPlayerDeconnect(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        if (this.plugin.getGameManager().getParticipantManager().getGamePlayer(player.getName()).isInDuel()) {
            Duel duel = this.plugin.getGameManager().getGameEventsManager().getDuel();
            for (GamePlayer gp : duel.getGamePlayersInGame()) {
                if (gp.getBukkitPlayer().getUniqueId() != player.getUniqueId()) duel.endDuel(gp);
            }

            this.plugin.getGameManager().getGameEventsManager().endDuel();
        }
    }

    @EventHandler
    public void onToolsUse(PlayerItemDamageEvent e) {
        ItemStack itemStack = e.getItem();

        if (this.plugin.getGameManager().getGameStats().getCurrentPhase() == PhaseEnum.PHASE3) {
            // dont apply double damage for customItems
            if ( !this.plugin.getGameManager().getItemsManager().isCustomItem(itemStack,"WRENCH") ) {
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

                        Team team = this.plugin.getGameManager().getParticipantManager().getTeamForPlayer(inventoryView.getPlayer().getName());
                        if (team == null) {
                            event.setCancelled(true);
                            return;
                        }
                        if (team.getRange() < 105) team.addRange(10);
                        else {
                            UNCSurvival.getInstance().getMessageTchatManager().sendMessageToPlayer("Votre équipe possède déjà la protection maximale !",event.getWhoClicked(), ChatColor.RED);
                            event.setCancelled(true);
                        }

                        if (!event.isCancelled()) {
                            itemStack.setAmount(0);
                            Bukkit.getServer().getScheduler().runTask(this.plugin, () -> {
                                inventoryView.getPlayer().closeInventory();
                                this.plugin.getGameManager().getInterfacesManager().ouvrirInterface(
                                        this.plugin.getGameManager().getParticipantManager().getTeamForPlayer(inventoryView.getPlayer().getName()).getInterfaceTeam(),
                                        Bukkit.getPlayerExact(inventoryView.getPlayer().getName())
                                );
                            });
                        }
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

    @EventHandler
    public void onCraftPrepare(PrepareItemCraftEvent e) {
        ItemStack[] items = e.getInventory().getMatrix();
        if (
                this.plugin.getGameManager().isAlcoolQualityInGame()
                && items[0] != null && items[0].getType() == Material.ROTTEN_FLESH
                && items[1] != null && items[1].getType() == Material.ROTTEN_FLESH
                && items[2] != null && items[2].getType() == Material.ROTTEN_FLESH
                && items[3] != null && items[3].getType() == Material.ROTTEN_FLESH
                && items[4] != null && items[4].getType() == Material.POTION
                && items[5] != null && items[5].getType() == Material.ROTTEN_FLESH
                && items[6] != null && items[6].getType() == Material.ROTTEN_FLESH
                && items[7] != null && items[7].getType() == Material.ROTTEN_FLESH
                && items[8] != null && items[8].getType() == Material.ROTTEN_FLESH) {
            ItemStack item = this.recipe.getResult().clone();
            ItemStack itemIngredient = items[4];

            if (this.plugin.getGameManager().getItemsManager().isCustomItem(itemIngredient,"ALCOOL")) {
                ItemMeta meta = item.getItemMeta();
                int qualityLevel = 0;

                ItemMeta metaIngredient = itemIngredient.getItemMeta();
                String numberToConvert = metaIngredient.getLore().get(0);

                qualityLevel = Integer.parseInt(numberToConvert.replaceAll("[^0-9]", "").substring(1));
                qualityLevel += 1;

                meta.setLore(List.of("§r§8Qualité : §r§l"+qualityLevel));
                if(qualityLevel >= 64 ) meta.setDisplayName("§6§lGrand Cru");
                item.setItemMeta(meta);
                e.getInventory().setResult(item);
            }
        }
    }

    @EventHandler
    public void onCraftGet(CraftItemEvent e) {
        ItemStack[] items = e.getInventory().getMatrix();
        ItemStack itemIngr = e.getInventory().getMatrix()[4];
        ItemStack itemAlcool = this.plugin.getGameManager().getItemsManager().createAlcool();
        ItemStack itemWrench = this.plugin.getGameManager().getItemsManager().createWrenchItem(1,0);

        if ( e.getRecipe() != null
                && e.getRecipe().getResult().isSimilar(itemAlcool)
                && itemIngr.hasItemMeta()
                && this.plugin.getGameManager().getItemsManager().isCustomItem(itemIngr,"ALCOOL")) {
            ItemMeta meta = itemAlcool.getItemMeta();
            int qualityLevel = 0;

            ItemMeta metaIngredient = itemIngr.getItemMeta();
            String numberToConvert = metaIngredient.getLore().get(0);

            qualityLevel = Integer.parseInt(numberToConvert.replaceAll("[^0-9]", "").substring(1));
            qualityLevel += 1;

            meta.setLore(List.of("§r§8Qualité : §r§l"+qualityLevel));

            // advancement
            AdvancementManager advancementManager = this.plugin.getGameManager().getAdvancementManager();
            Advancement advancement = advancementManager.getAdvancement("pete_ma_biere");
            if(qualityLevel >= 64 ) {
                meta.setDisplayName("§6§lGrand Cru");
                if ( !advancement.alreadyGranted() ) {
                    Team t = this.plugin.getGameManager().getParticipantManager().getTeamForPlayer((Player) e.getWhoClicked());
                    advancementManager.grantToATeam(t, advancement);
                }
            }

            itemAlcool.setItemMeta(meta);
            e.getInventory().setResult(itemAlcool);

        } else if ( e.getRecipe().getResult().isSimilar(itemWrench)){
            // advancement
            AdvancementManager advancementManager = this.plugin.getGameManager().getAdvancementManager();
            Advancement advancement = advancementManager.getAdvancement("bob_le_bricoleur");
            if(!advancement.alreadyGranted()) {
                Team t = this.plugin.getGameManager().getParticipantManager().getTeamForPlayer((Player) e.getWhoClicked());
                advancementManager.grantToATeam(t,advancement);
            }
        }
    }
}
