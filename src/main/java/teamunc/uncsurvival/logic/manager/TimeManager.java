package teamunc.uncsurvival.logic.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.features.thirst.ThirstActualiser;
import teamunc.uncsurvival.logic.phase.PhaseEnum;
import teamunc.uncsurvival.logic.player.GamePlayer;
import teamunc.uncsurvival.logic.tasks.CountdownPhaseTask;
import teamunc.uncsurvival.utils.LoggerFile;
import teamunc.uncsurvival.utils.Region;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class TimeManager extends AbstractManager{

    public TimeManager(UNCSurvival plugin) {
        super(plugin);
    }

    private BukkitScheduler scheduler = plugin.getServer().getScheduler();

    private int secondes = 0;
    private int minutes = 0;
    private int heures = 0;
    private int jours = 0;

    // ID OF SCHEDULE
    private int eachSecondsTimerID;

    public void startTimer() {
        this.eachSecondsTimerID =
                scheduler.scheduleSyncRepeatingTask(
                        this.plugin,
                        () -> {
                            // seconds
                            this.secondes++;
                            this.actionsEachSeconds();

                            // minutes
                            if (this.secondes >= 60) {
                                this.secondes = 0;
                                this.minutes++;
                                this.actionsEachMinutes();
                            }

                            // heures
                            if (this.minutes >= 60) {
                                this.minutes = 0;
                                this.heures++;
                                this.actionsEachHours();
                            }

                            // jours
                            if (this.heures >= 24) {
                                this.heures = 0;
                                this.jours++;
                                this.actionsEachDays();
                            }
                        },
                        0L,
                        20L
                );
    }

    public void checkNewPhase() {
        CountdownPhaseTask timer = this.plugin.getGameManager().getTimerTask();
        if(timer != null) {
            if(timer.getJours() <= 0 && timer.getHeures() <= 0 && timer.getMinutes() <= 0 && timer.getSecondes() <= 0) {
                this.plugin.getGameManager().goNextPhase();
            }
        }
    }

    public void actionsEachSeconds() {
        // Vérifi si une phase est terminé
        checkNewPhase();
        PhaseEnum phase = plugin.getGameManager().getGameStats().getCurrentPhase();

        // Update les scoreboards
        plugin.getGameManager().getScoreboardManager().update();

        // damage due to Water
        if (this.secondes%5 == 0) ThirstActualiser.getInstance().damageAllnoWater();

        // Actualise Water Level Display
        ThirstActualiser.getInstance().actualiseDisplay();

        // Events Application
        this.plugin.getGameManager().getGameEventsManager().appliqueCovid();
        this.plugin.getGameManager().getGameEventsManager().appliqueFamine();


        this.plugin.getGameManager().getCustomBlockManager().actualiseTickBlocks(this.secondes);

        this.plugin.getGameManager().getTeamsManager().getAllTeams().forEach(team -> {
            // test items Goals Consumes
            if (phase != PhaseEnum.FIN) team.ConsumeAllGoalItems();
        });

        if(plugin.getGameManager().getGameStats().getCurrentPhase() == PhaseEnum.LANCEMENT) {
            for(Player player : this.plugin.getGameManager().getParticipantManager().getOnlinePlayers()) {
                if(plugin.getGameManager().getTimerTask().getSecondes() == 13) {
                    player.sendTitle("§bDébut du jeu dans : ", "", 20, 30, 20);
                } else if(plugin.getGameManager().getTimerTask().getSecondes() <= 10){
                    player.sendTitle(" §c§l" + plugin.getGameManager().getTimerTask().getSecondes(), "", 10, 20, 10);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL,0.4F, 2);
                }
            }
        }

        // check for players locations
        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            plugin.getGameManager().getTeamsManager().getAllTeams().forEach(team -> {
                Region region = team.getRegion();
                // dans la liste des joueurs present dans la region mais les locations ne correspondent plus
                if (region.inRegion(player.getUniqueId().toString()) && !region.contains(player.getLocation())) {
                    plugin.getMessageTchatManager().sendMessageToPlayer("§6Vous sortez de la zone de la team " + team.getChatColor() + team.getName() + "§6.",player);
                    region.leaveTheRegion(player.getUniqueId().toString());
                } else // pas dans la liste mais present en locations
                    if (!region.inRegion(player.getUniqueId().toString()) && region.contains(player.getLocation())) {
                    plugin.getMessageTchatManager().sendMessageToPlayer("§6Vous entrez dans la zone de la team " + team.getChatColor() + team.getName() + "§6.", player);
                    region.enterInRegion(player.getUniqueId().toString());
                }
            });
        });

        // gourde ?
        for(GamePlayer player : this.plugin.getGameManager().getParticipantManager().getGamePlayers()) {
            if ( player.getBukkitPlayer() != null && player.getWaterLevel() <= 2 && this.plugin.getGameManager().getItemsManager().aUneGourde(player.getBukkitPlayer().getInventory())) {
                ItemStack gourde = (ItemStack) Arrays.stream(player.getBukkitPlayer().getInventory().getContents())
                        .filter(itemStack -> itemStack != null && this.plugin.getGameManager().getItemsManager().isCustomItem(itemStack, "GOURDE")).toArray()[0];
                if (gourde != null) {
                    this.plugin.getGameManager().getItemsManager().actionOfGourde(player.getBukkitPlayer(), gourde);
                }
            }
        }

        // writes logs
        LoggerFile.WriteNextLine();
    }

    public void actionsEachMinutes() {
        PhaseEnum phase = plugin.getGameManager().getGameStats().getCurrentPhase();

        // place all events that can occur each minutes
        this.plugin.getGameManager().getTeamsManager().getAllTeams().forEach(team -> {
            // Famine test
            if (this.plugin.getGameManager().getGameEventsManager().isItTimeForFamine(team))
                this.plugin.getGameManager().getGameEventsManager().actionFamine(team);
        });

        // dicrease Water Level of 1
        if (this.minutes%3 == 0 && phase != PhaseEnum.INIT && phase != PhaseEnum.FIN) ThirstActualiser.getInstance().decreaseWaterForAllRegisteredPlayers(1);

        //duels
        LocalDateTime now = LocalDateTime.now();

        if ((phase == PhaseEnum.PHASE1 || phase == PhaseEnum.PHASE2 || phase == PhaseEnum.PHASE3) &&
            now.getSecond() == 0 && now.getMinute() == 0 && now.getHour() > 9 && now.getHour()%2 == 0) {
            // choix des joueurs
            ArrayList<GamePlayer> playersSelected = this.plugin.getGameManager().getParticipantManager().getRandomOnlineGamePlayerFromDiffTeams(2);

            if (playersSelected != null) {
                this.plugin.getMessageTchatManager().sendGeneralMesssage("Un duel se prépare... Tenez-vous pret !");
                Bukkit.getScheduler().runTaskLater(
                        this.plugin,
                        () -> this.plugin.getGameManager().getGameEventsManager().startDuel(playersSelected),
                        100
                );
            } else {
                this.plugin.getMessageTchatManager().sendGeneralMesssage("Un duel a été annulé car il n'y a pas assez de joueurs connectés!",ChatColor.GOLD);
            }
        }
    }

    public void actionsEachHours() {
        // place all events that can occur each phases
    }

    public void actionsEachDays() {
        // place all events that can occur each phases
    }

    public int getSecondes() {
        return secondes;
    }
}
