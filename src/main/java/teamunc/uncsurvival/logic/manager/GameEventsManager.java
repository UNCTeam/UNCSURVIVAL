package teamunc.uncsurvival.logic.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.phase.PhaseEnum;
import teamunc.uncsurvival.logic.player.GamePlayer;

import java.util.*;

public class GameEventsManager extends AbstractManager{
    private final int TAUX_COVID = 3;

    public GameEventsManager(UNCSurvival plugin) {
        super(plugin);
    }

    public boolean isItTimeForCovid() {
        boolean result = false;
        if (this.plugin.getGameManager().getGameStats().getCurrentPhase() == PhaseEnum.PHASE2 ||
                this.plugin.getGameManager().getGameStats().getCurrentPhase() == PhaseEnum.PHASE3) {
            Random r = new Random();

            int res = r.nextInt(100);

            result = (res <= TAUX_COVID);
        }
        return result;
    }

    /**
     * Selection et application du covid a un joueur
     */
    public void actionCovid() {
        ParticipantManager participantManager = this.plugin.getGameManager().getParticipantManager();
        if (participantManager.getOnlinePlayers().size() == 0) return;

        ArrayList<GamePlayer> gamePlayers = new ArrayList<>();
        for (Player player : participantManager.getOnlinePlayers()) gamePlayers.add(participantManager.getGamePlayer(player.getName()));
        // shuffle list
        Collections.shuffle(gamePlayers);

        Iterator<GamePlayer> gpIterator = gamePlayers.iterator();
        boolean done = false;
        while (gpIterator.hasNext() && !done) {
            GamePlayer gp = gpIterator.next();
            if (!gp.isCovided()) {
                gp.ActiveCovided();
                gp.getBukkitPlayer().sendTitle("COVIDED",null,20,60,20);
                done = true;
            }
        }
    }

    public void appliqueCovid() {
        for (Player p: this.plugin.getGameManager().getParticipantManager().getOnlinePlayers()) {
            GamePlayer gp = this.plugin.getGameManager().getParticipantManager().getGamePlayer(p.getName());

            if (gp.isCovided()) {
                // Application des effets du covid
                gp.getBukkitPlayer().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,40,2,false,false));
                gp.getBukkitPlayer().addPotionEffect(new PotionEffect(PotionEffectType.HUNGER,40,1,false,false));

                // Effect propagation et temps
                boolean propagationAFaire = gp.passerUneSecondeCovid();
                if (propagationAFaire) {
                    Collection<Entity> covidedList = gp.getBukkitPlayer().getWorld().getNearbyEntities(
                            gp.getBukkitPlayer().getLocation(),
                            20, 20, 20,
                            entity ->
                                    entity.getType() == EntityType.PLAYER &&
                                            this.plugin.getGameManager().getParticipantManager().getGamePlayer(entity.getName()) != null &&
                                            !this.plugin.getGameManager().getParticipantManager().getGamePlayer(entity.getName()).isCovided()
                    );

                    for (Entity e : covidedList) {
                        GamePlayer gamePlayerToCovid = this.plugin.getGameManager().getParticipantManager().getGamePlayer(e.getName());
                        if (!gamePlayerToCovid.isCovided()) {
                            gamePlayerToCovid.ActiveCovided();
                            this.plugin.getMessageTchatManager().sendGeneralMesssage("§b§l" + gp.getBukkitPlayer().getName() + "§6§l vient de covider : §b§l" + gamePlayerToCovid.getBukkitPlayer().getName());
                        }
                    }
                }
            }
        }
    }
}
