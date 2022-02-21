package teamunc.uncsurvival.eventsListeners;

import net.minecraft.world.food.FoodMetaData;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.eventsListeners.AbstractEventsListener;
import teamunc.uncsurvival.features.thirst.ThirstActualiser;
import teamunc.uncsurvival.logic.manager.GameEventsManager;
import teamunc.uncsurvival.logic.manager.GameManager;
import teamunc.uncsurvival.logic.phase.PhaseEnum;
import teamunc.uncsurvival.logic.player.GamePlayer;

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

        GameManager gameManager = this.plugin.getGameManager();

        switch (itemStack.getType()) {
            case POTION:
            case MILK_BUCKET:
                boolean notFull = ThirstActualiser.getInstance().increaseWater(4, gameManager.getParticipantManager().getGamePlayer(player.getName()));
                if (!notFull) {
                    e.setCancelled(true);
                    plugin.getMessageTchatManager().sendMessageToPlayer("Vous ne pouvez plus boire, plus soif!",player, ChatColor.RED);
                }
                break;
        }

        // variety of food
        if (gameManager.getItemsManager().getAllFoodOfTheGame().contains(itemStack.getType()) && (
                gameManager.getGameStats().getCurrentPhase() == PhaseEnum.PHASE2 ||
                gameManager.getGameStats().getCurrentPhase() == PhaseEnum.PHASE3
            )
        ) {
            GamePlayer gamePlayer = gameManager.getParticipantManager().getGamePlayer(player.getName());
            if (gamePlayer.EatHowOften(itemStack.getType()) == 3) {
                this.plugin.getMessageTchatManager().sendMessageToPlayer("Vous avez mangé cette nourriture 4 fois ! changez de nourriture.",player,ChatColor.GOLD);
            } else if (gamePlayer.EatHowOften(itemStack.getType()) >= 4) {
                this.plugin.getMessageTchatManager().sendMessageToPlayer("Changez de nourriture! Gains nutritionnels divisés par 3!",player,ChatColor.GOLD);

                // new value of food
                int newVal = player.getFoodLevel() + gameManager.getItemsManager().getFoodLevelOf(itemStack.getType())/3;
                player.setFoodLevel(Math.min(newVal,20));

                // removing item
                gamePlayer.getBukkitPlayer().getItemInUse().setAmount(gamePlayer.getBukkitPlayer().getItemInUse().getAmount()-1);

                e.setCancelled(true);
            }

            if (gamePlayer.EatHowOften(itemStack.getType()) < 4) {
                gamePlayer.addToEatenFoodQueue(itemStack.getType());
            }

        }

        // covid ?
        GameEventsManager gameEventsManager = gameManager.getGameEventsManager();
        if(gameEventsManager.isItTimeForCovid()) gameEventsManager.actionCovid(player);
    }
}
