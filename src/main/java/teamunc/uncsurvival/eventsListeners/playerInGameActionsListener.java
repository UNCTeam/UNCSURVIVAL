package teamunc.uncsurvival.eventsListeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.phase.PhaseEnum;
import teamunc.uncsurvival.logic.team.Team;

public class playerInGameActionsListener extends AbstractEventsListener {
    public playerInGameActionsListener(UNCSurvival plugin) {
        super(plugin);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();

        for (Team t : this.plugin.getGameManager().getTeamsManager().getAllTeams()) {
            if (this.plugin.getGameManager().getParticipantManager().getTeamForPlayer(p) != t) {
                t.addABonusScore(100);
            }
        }

        this.plugin.getMessageTchatManager().sendGeneralMesssage("§6§lMerci pour la mort! §b§l+100 §6§lpoints pour les autres!");
    }

    @EventHandler
    public void onToolsUse(PlayerItemDamageEvent e) {
        ItemStack itemStack = e.getItem();

        if (this.plugin.getGameManager().getGameStats().getCurrentPhase() == PhaseEnum.PHASE3) {
            // dont apply double damage for customItems
            if ( !this.plugin.getGameManager().getItemsManager().isCustomItem(itemStack,"Wrench") ) {
                e.setDamage(e.getDamage() * 4);
            }
        }
    }
}
