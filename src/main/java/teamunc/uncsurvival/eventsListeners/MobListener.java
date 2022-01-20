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
        if (this.plugin.getGameManager().getGameStats().getCurrentPhase() == PhaseEnum.PHASE2 || this.plugin.getGameManager().getGameStats().getCurrentPhase() == PhaseEnum.PHASE3) {
            if ( e.getEntity().getType() != EntityType.ARMOR_STAND ) {
                e.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(
                        e.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() * 1.5
                );

                e.getEntity().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(
                        e.getEntity().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue() * 1.25
                );

                e.getEntity().setHealth(e.getEntity().getHealth() * 1.5);
                e.getEntity().setCustomName("§6§lSuper " + e.getEntity().getName());
                e.getEntity().setCustomNameVisible(true);
            }
        }
    }
}
