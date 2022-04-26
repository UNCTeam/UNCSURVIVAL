package teamunc.uncsurvival.eventsListeners;

import net.minecraft.world.entity.EnumCreatureType;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.advancements.Advancement;
import teamunc.uncsurvival.logic.manager.AdvancementManager;
import teamunc.uncsurvival.logic.phase.PhaseEnum;
import teamunc.uncsurvival.logic.player.GamePlayer;
import teamunc.uncsurvival.logic.team.Team;

import java.util.List;
import java.util.Random;

public class MobListener extends AbstractEventsListener{
    public MobListener(UNCSurvival plugin) {
        super(plugin);
    }

    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent e) {
        PhaseEnum phase = this.plugin.getGameManager().getGameStats().getCurrentPhase();
        boolean modifyNeeded = false;
        String prefix = "";
        double healthFact = 1;
        double damageFact = 1;
        Random r = new Random();
        String baseName = e.getEntity().getName();
        if (baseName.contains("Super") || baseName.contains("§1§lU§2§ll§3§lt§4§lr§5§la§6§l-") || baseName.contains("Mega-")) return;
        // modifications par phases
        switch (phase) {
            case PHASE2:
                prefix = "§6§lSuper ";
                healthFact = 1.5;
                damageFact = 1.25;
                modifyNeeded = true;
                break;
            case PHASE3: case FIN:
                if (r.nextInt(300) <= 1 && e.getEntity().getEquipment() != null
                        && e.getEntity().getType() != EntityType.ARMOR_STAND
                        && e.getEntity().getType() != EntityType.ENDER_DRAGON
                        && e.getEntity().getType() != EntityType.BOAT
                        && e.getEntity().getType() != EntityType.BAT
                        && e.getEntity().getType() != EntityType.BEE) {
                    String temp = "§1§lU§2§ll§3§lt§4§lr§5§la§6§l-";
                    int i = 7;
                    List<String> colors = List.of("§1§l","§2§l","§3§l","§4§l","§5§l","§6§l","§7§l","§8§l","§9§l","§a§l","§b§l","§c§l","§d§l","§e§l","§f§l");
                    for (char c : baseName.toCharArray()) {
                        temp += colors.get(i) + c;
                        i = (i+1)%15;
                    }
                    baseName = temp;
                    prefix = "";
                    healthFact = 3;
                    damageFact = 3;
                    e.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,9999999,1,false,true,true));
                    e.getEntity().getEquipment().setItemInOffHand(this.plugin.getGameManager().getItemsManager().createAmethystIngot());
                    e.getEntity().getEquipment().setItemInOffHandDropChance(0.75f);
                    modifyNeeded = true;
                } else {
                    prefix = "§c§lMega-";
                    healthFact = 2;
                    damageFact = 1.75;
                    modifyNeeded = true;
                }
                break;
        }

        if (modifyNeeded
                && e.getEntity().getType() != EntityType.ARMOR_STAND
                && e.getEntity().getType() != EntityType.ENDER_DRAGON
                && e.getEntity().getType() != EntityType.BOAT
                && e.getEntity().getType() != EntityType.BAT
                && e.getEntity().getType() != EntityType.BEE) {
            if (e.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH) != null) {
                e.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(
                        e.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() * healthFact
                );
            }

            if (e.getEntity().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null) {
                e.getEntity().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(
                        e.getEntity().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue() * damageFact
                );
            }

            e.getEntity().setHealth(e.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
            e.getEntity().setCustomName(prefix + baseName);
            e.getEntity().setCustomNameVisible(true);
        }
    }

    @EventHandler
    public void onMobKill(EntityDeathEvent e) {
        AdvancementManager advancementManager = this.plugin.getGameManager().getAdvancementManager();
        Advancement advancement = advancementManager.getAdvancement("raciste");

        if (e.getEntity().getKiller() != null
                && e.getEntity().getType() == EntityType.WITHER) {
            Player p = e.getEntity().getKiller();
            Team t = this.plugin.getGameManager().getParticipantManager().getTeamForPlayer(p);
            if ( !advancement.alreadyGranted() ) {
                advancementManager.grantToATeam(t, advancement);
            } else if (
                    advancement.getTeamColor() != t.getChatColor() &&
                            !this.plugin.getGameManager().getAdvancementManager().isTeamHalfPointed(t.getChatColor(), advancement)
            ) {
                this.plugin.getGameManager().getAdvancementManager().setTeamHalfPointedAndGiveItPoints(t.getChatColor(), advancement);
            }
        }
    }

    @EventHandler
    public void onMoutMoutEat(EntityChangeBlockEvent e) {
        if (e.getEntity().getType() == EntityType.SHEEP) {
            e.setCancelled(true);
            Sheep s = (Sheep) e.getEntity();
            s.setSheared(false);
        } else if (e.getEntity().getType() == EntityType.ENDERMAN) {
            e.setCancelled(true);
        } else if (e.getEntity().getType() == EntityType.CREEPER) {
            e.setCancelled(true);
        }
    }
}
