package teamunc.uncsurvival.commandesListeners;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import teamunc.uncsurvival.UNCSurvival;

public class GiveDiamondAppleCmdExec extends AbstractCommandExecutor implements CommandExecutor {

    public GiveDiamondAppleCmdExec(UNCSurvival plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player ) {
            Player p = (Player) sender;

            ItemStack item = this.plugin.getGameManager().getItemsManager().createDiamondApple();

            p.getInventory().addItem(item);
        }

        return true;
    }
}
