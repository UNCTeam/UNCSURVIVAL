package teamunc.uncsurvival.commandesListeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.utils.MessageTchatManager;

public class AddSpawnPointCmdExec extends abstractCommandExecutor {
    public AddSpawnPointCmdExec(UNCSurvival api) {
        super(api);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // error if not enougth args
        if (args.length < 3) {
            MessageTchatManager.getInstance().sendMessageToPlayer("You need to specify X Y and Z !",sender, ChatColor.RED);
            return false;
        }

        // error if the sender is not a player (location need a world)
        if (!(sender instanceof Player)) {
            MessageTchatManager.getInstance().sendMessageToPlayer("You need to be a player !",sender, ChatColor.RED);
            return false;
        } else {
            World world = ((Player) sender).getWorld();
            Location loc =
                    new Location(world,
                        Double.parseDouble(args[0]),
                        Double.parseDouble(args[1]),
                        Double.parseDouble(args[2])
                    );

            boolean success = this.plugin.getLocationManager().addNewSpawnPoint(loc);

            if (success) {
                MessageTchatManager.getInstance().sendMessageToPlayer("The spawn point " + loc + " has been added to the game.",sender, ChatColor.GREEN);
            }
        }
        return true;
    }
}
