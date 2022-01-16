package teamunc.uncsurvival.eventsListeners.customsBlock;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.eventsListeners.AbstractEventsListener;

public class BlockListener extends AbstractEventsListener {
    public BlockListener(UNCSurvival plugin) {
        super(plugin);
    }

    @EventHandler
    public void onBarrierClick(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();

        if (block != null && e.getAction() == Action.RIGHT_CLICK_BLOCK && !e.getPlayer().isSneaking()) {
            // TODO check si le joueur est de la bonne team
            if (block.getType() == Material.BARRIER) {
                this.plugin.getGameManager().getInterfacesManager().ouvrirInterface(block.getLocation(),e.getPlayer());
                e.setCancelled(true);
            }
        }

    }
}
