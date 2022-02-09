package teamunc.uncsurvival.eventsListeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.eventsListeners.AbstractEventsListener;
import teamunc.uncsurvival.features.thirst.ThirstActualiser;
import teamunc.uncsurvival.logic.manager.GameEventsManager;

public class ConsumeListenerVanilla extends AbstractEventsListener {

    public ConsumeListenerVanilla(UNCSurvival plugin) {
        super(plugin);
    }

    @EventHandler
    public void onExecute(PlayerItemConsumeEvent e) {
        // taking Data of the player
        Player player = e.getPlayer();

        // taking Data of the item
        ItemStack itemStack = e.getItem();

        switch (itemStack.getType()) {
            case POTION:
                boolean notFull = ThirstActualiser.getInstance().increaseWater(4, this.plugin.getGameManager().getParticipantManager().getGamePlayer(player.getName()));
                if (!notFull) {
                    e.setCancelled(true);
                    plugin.getMessageTchatManager().sendMessageToPlayer("You can't drink, you are full !",player, ChatColor.RED);
                }
                break;
        }

        // covid ?
        GameEventsManager gameEventsManager = this.plugin.getGameManager().getGameEventsManager();
        if(gameEventsManager.isItTimeForCovid()) gameEventsManager.actionCovid(player);
    }
}
