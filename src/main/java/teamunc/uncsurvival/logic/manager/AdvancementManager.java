package teamunc.uncsurvival.logic.manager;

import org.bukkit.Bukkit;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.advancements.*;
import teamunc.uncsurvival.logic.phase.PhaseEnum;
import teamunc.uncsurvival.logic.team.Team;
import teamunc.uncsurvival.utils.LoggerFile;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AdvancementManager extends AbstractManager implements Serializable {

    private AdvancementList advancements;

    public AdvancementManager(UNCSurvival plugin) {
        super(plugin);
        loadAdvancements();
    }

    public void loadAdvancements() {
        this.advancements = this.plugin.getFileManager().loadAdvancementList();
    }

    public Advancement getAdvancement(String name) {
        return this.advancements.getAdvancements().stream().filter(advancement -> advancement.getKey().getKey().contains(name)).findFirst().get();
    }

    public void grantToATeam(Team team,Advancement advancement) {
        if (team == null) return;
        PhaseEnum phase = this.plugin.getGameManager().getGameStats().getCurrentPhase();
        if (phase == PhaseEnum.INIT || phase == PhaseEnum.FIN) return;
        this.plugin.getMessageTchatManager().sendGeneralMesssage("§6La team " + team.getChatColor() + team.getName() + "§6 remporte l'achievement §b" + advancement.DisplayedName() + " !");
        LoggerFile.AppendLineToWrite("[ACHIEVEMENTS] LA TEAM " + team.getName() + " DEBLOQUE L ACHIEVEMENT " + advancement.DisplayedName() + " LE " + LocalDateTime.now());
        team.getMembers().forEach(gamePlayer -> {
            this.plugin.getFileManager().unlockAdvancement(gamePlayer.getUUID(),advancement);
        });
        advancement.setWinningTeam(team);
        team.addABonusScore(advancement.givenPoints());
    }

    public ArrayList<Advancement> getList() {
        return this.advancements.getAdvancements();
    }

    public void save() {
        this.plugin.getFileManager().saveAdvancementList(this.advancements);
    }

    public void clearAll() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"advancement revoke @a everything");
        this.advancements = new AdvancementList(
                new ArrayList<>(
                        List.of(
                                new BobLeBricoleur(),
                                new CaFaitBeaucoupLaNon(),
                                new ExtensionDuTerritoire(),
                                new GucciMan(),
                                new MonChandail(),
                                new PeteMaBiere(),
                                new Precoce(),
                                new Raciste()
                        )
                )
        );
        this.plugin.getFileManager().saveAdvancementList(this.advancements);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"advancement grant @a only uncsurvival:root");
    }
}
