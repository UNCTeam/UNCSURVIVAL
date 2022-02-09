package teamunc.uncsurvival.eventsListeners;

import net.minecraft.world.entity.EnumCreatureType;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.phase.PhaseEnum;

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

        // modifications par phases
        switch (phase) {
            case PHASE2:
                prefix = "§6§lSuper ";
                healthFact = 1.5;
                damageFact = 1.25;
                modifyNeeded = true;
                break;
            case PHASE3: case FIN:
                prefix = "§c§lMega-";
                healthFact = 2;
                damageFact = 1.75;
                modifyNeeded = true;
                break;
        }

        if (modifyNeeded && e.getEntity().getType() != EntityType.ARMOR_STAND ) {
            e.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(
                    e.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() * healthFact
            );

            e.getEntity().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(
                    e.getEntity().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue() * damageFact
            );

            e.getEntity().setHealth(e.getEntity().getHealth() * healthFact);
            e.getEntity().setCustomName(prefix + e.getEntity().getName());
            e.getEntity().setCustomNameVisible(true);
        }
    }
}
