package teamunc.uncsurvival.commandesListeners;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.phase.PhaseEnum;

public class TimerCmdExec extends AbstractCommandExecutor {

    public TimerCmdExec(UNCSurvival plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(s.equalsIgnoreCase("nextphase")) {
            if(this.plugin.getGameManager().getGameStats().getCurrentPhase() != PhaseEnum.FIN) {
                this.plugin.getGameManager().goNextPhase();
                commandSender.sendMessage("§aPassage à la phase suivante");
                return true;
            } else {
                commandSender.sendMessage("§cLa partie est déjà fini");
            }
        }
        int jours = 0;
        int heures = 0;
        int minutes = 0;
        int secondes = 0;
        if(strings.length<2) {
            commandSender.sendMessage("§cIl manques des arguments");
        }
        switch (strings[0]) {
            case "jours":
                jours = Integer.parseInt(strings[1]);
                break;
            case "heures":
                heures = Integer.parseInt(strings[1]);
                break;
            case "minutes":
                minutes = Integer.parseInt(strings[1]);
                break;
            case "secondes":
                secondes = Integer.parseInt(strings[1]);
                break;
        }
        switch (s) {
            case "addtime":
                commandSender.sendMessage("jour : " + jours + "heures = " + heures);
                this.plugin.getGameManager().getTimerTask().addTime(jours, heures, minutes, secondes);
                commandSender.sendMessage("§aAjout de temps réussi");
                break;
            case "removetime":
                this.plugin.getGameManager().getTimerTask().removeTime(jours, heures, minutes, secondes);
                commandSender.sendMessage("§Baisse de temps réussi");
                break;
        }
        return false;
    }
}
