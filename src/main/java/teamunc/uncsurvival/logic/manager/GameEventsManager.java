package teamunc.uncsurvival.logic.manager;

import com.google.common.base.MoreObjects;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.duels.Duel;
import teamunc.uncsurvival.logic.gameStats.GameStats;
import teamunc.uncsurvival.logic.phase.PhaseEnum;
import teamunc.uncsurvival.logic.player.GamePlayer;
import teamunc.uncsurvival.logic.team.Team;

import java.util.*;

public class GameEventsManager extends AbstractManager{
    private final int TAUX_COVID = 2;
    private final int TAUX_FAMINE = 1;
    private Duel actualDuel;

    public GameEventsManager(UNCSurvival plugin) {
        super(plugin);
    }

    public boolean isItTimeForCovid() {
        boolean result = false;
        if (this.plugin.getGameManager().getGameStats().getCurrentPhase() == PhaseEnum.PHASE3) {
            Random r = new Random();

            int res = r.nextInt(100);

            result = (res <= TAUX_COVID);
        }
        return result;
    }

    /**
     * Selection et application du covid a un joueur
     */
    public void actionCovid(Player p) {
        ParticipantManager participantManager = this.plugin.getGameManager().getParticipantManager();
        GamePlayer gp = participantManager.getGamePlayer(p.getName());
            if (!gp.isCovided()) {
                gp.ActiveCovided();
                gp.getBukkitPlayer().sendTitle("COVIDED",null,20,60,20);
            }
    }

    public void appliqueCovid() {
        for (Player p: this.plugin.getGameManager().getParticipantManager().getOnlinePlayers()) {
            GamePlayer gp = this.plugin.getGameManager().getParticipantManager().getGamePlayer(p.getName());

            if (gp.isCovided()) {
                // Application des effets du covid
                gp.getBukkitPlayer().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,100,2,false,false));
                gp.getBukkitPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW,40,1,false,false));
                gp.getBukkitPlayer().addPotionEffect(new PotionEffect(PotionEffectType.HARM,40,1,false,false));

                // Effect propagation et temps
                boolean propagationAFaire = gp.passerUneSecondeCovid();
                if (propagationAFaire) {
                    Collection<Entity> covidedList = gp.getBukkitPlayer().getWorld().getNearbyEntities(
                            gp.getBukkitPlayer().getLocation(),
                            10, 10, 10,
                            entity ->
                                    entity.getType() == EntityType.PLAYER &&
                                            this.plugin.getGameManager().getParticipantManager().getGamePlayer(entity.getName()) != null &&
                                            !this.plugin.getGameManager().getParticipantManager().getGamePlayer(entity.getName()).isCovided()
                    );

                    for (Entity e : covidedList) {
                        GamePlayer gamePlayerToCovid = this.plugin.getGameManager().getParticipantManager().getGamePlayer(e.getName());
                        if (!gamePlayerToCovid.isCovided()) {
                            gamePlayerToCovid.ActiveCovided();
                            this.plugin.getMessageTchatManager().sendGeneralMesssage("§b§l" + gp.getTeamColor() + gp.getBukkitPlayer().getName() + "§6§l vient de covider : §b§l" + gamePlayerToCovid.getTeamColor() + gamePlayerToCovid.getBukkitPlayer().getName());
                        }
                    }
                }
            }
        }
    }


    public boolean isItTimeForFamine(Team team) {
        GameStats gameStats = this.plugin.getGameManager().getGameStats();
        PhaseEnum phase = gameStats.getCurrentPhase();
        boolean min1playerOfTeamOnline = team.getMembers().stream().anyMatch(gamePlayer -> gamePlayer.isOnline());
        boolean result = false;

        if ( (phase == PhaseEnum.PHASE2 || phase == PhaseEnum.PHASE3 ) && !team.isFamined() && min1playerOfTeamOnline ) {
            Random r = new Random();

            int res = r.nextInt(180);

            result = (res <= TAUX_FAMINE);
        }
        return result;
    }

    /**
     * Selection et application de la famine a une team
     */
    public void actionFamine(Team team) {
        team.setFamined(true);
        team.getMembers().forEach(gamePlayer ->
                this.plugin.getMessageTchatManager().sendMessageToPlayer(
                        "Une lourde famine s'abat sur votre équipe ! Coup dur... il vous faut craft un remède !",
                        gamePlayer.getBukkitPlayer(),
                        ChatColor.BOLD)
        );

    }

    public void appliqueFamine() {
        GameManager gameManager = this.plugin.getGameManager();
        for (Team t : gameManager.getTeamsManager().getAllTeams()) {
            if (t.isFamined()) {
                t.getMembers().forEach(gamePlayer -> {
                    if (gamePlayer.isOnline()) {
                        Player p = gamePlayer.getBukkitPlayer();
                        p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER,60,2,false,false));
                    }
                });
            }
        }
    }

    public void startDuel() {

        // choix des joueurs
        ArrayList<GamePlayer> playersSelected = this.plugin.getGameManager().getParticipantManager().getRandomOnlineGamePlayer(2);
        this.startDuel(playersSelected);
    }

    public void startDuel(ArrayList<GamePlayer> playersSelected) {
        if (playersSelected != null) {
            plugin.getMessageTchatManager().sendGeneralMesssage(
                    "§6Les joueurs "
                    + playersSelected.get(0).getTeamColor()
                    + playersSelected.get(0).getBukkitPlayer().getName()
                    + "§6 et "
                    + playersSelected.get(1).getTeamColor()
                    + playersSelected.get(1).getBukkitPlayer().getName()
                    + "§6 vont se battre !"
            );
            this.actualDuel = new Duel(playersSelected);

            this.actualDuel.startDuel();

        } else {
            Bukkit.getConsoleSender().sendMessage("Une erreur avec le duel est survenu, aleatoire de joueur po bon");
        }
    }

    public Duel getDuel() {
        return this.actualDuel;
    }

    public void forceStopDuels() {
        if (this.actualDuel != null) this.actualDuel.endDuel(null);
    }

    public void endDuel() {
        this.actualDuel = null;
    }
}
