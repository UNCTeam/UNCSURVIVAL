package teamunc.uncsurvival.commandesListeners;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import teamunc.uncsurvival.UNCSurvival;

public class TimerCmdExec extends AbstractCommandExecutor {

    public TimerCmdExec(UNCSurvival plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        int jours = 0;
        int heures = 0;
        int minutes = 0;
        int secondes = 0;
        if(strings.length>1) {
            secondes = Integer.parseInt(strings[1]);
        }
        if(strings.length>2) {
            minutes = Integer.parseInt(strings[2]);
        }
        if(strings.length>3) {
            heures = Integer.parseInt(strings[3]);
        }
        if(strings.length>4) {
            jours = Integer.parseInt(strings[4]);
        }
        switch (s) {
            case "addtime":
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
