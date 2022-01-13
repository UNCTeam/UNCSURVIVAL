package teamunc.uncsurvival.eventsListeners.vanillaItems;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.eventsListeners.AbstractEventsListener;
import teamunc.uncsurvival.features.thirst.ThirstActualiser;

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
                boolean notFull = ThirstActualiser.getInstance().increaseWater(3, player.getName());
                if (!notFull) {
                    e.setCancelled(true);
                    plugin.getMessageTchatManager().sendMessageToPlayer("You can't drink, you are full !",player, ChatColor.RED);
                }
                break;
        }
    }
}
