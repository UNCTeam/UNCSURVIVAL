package teamunc.uncsurvival.commandesListeners;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import teamunc.uncsurvival.UNCSurvival;

public class GiveCustomItemsCmdExec extends AbstractCommandExecutor implements CommandExecutor {

    public GiveCustomItemsCmdExec(UNCSurvival plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("givecustomitem")) {
            if(args.length > 0) {
                if (sender instanceof Player ) {
                    Player p = (Player) sender;
                    switch (args[0]) {
                        case "diamondapple":
                            p.getInventory().addItem(this.plugin.getGameManager().getItemsManager().createDiamondApple());
                            break;
                        case "wrench":
                            p.getInventory().addItem(this.plugin.getGameManager().getItemsManager().createWrenchItem(0, 0));
                            break;
                        case "healpatch":
                            p.getInventory().addItem(this.plugin.getGameManager().getItemsManager().createHealPatch());
                            break;
                        case "vaccin":
                            p.getInventory().addItem(this.plugin.getGameManager().getItemsManager().createVaccin());
                            break;
                        case "alcool":
                            p.getInventory().addItem(this.plugin.getGameManager().getItemsManager().createAlcool());
                            break;
                        case "mincer":
                            p.getInventory().addItem(this.plugin.getGameManager().getItemsManager().createMincerItemBlock());
                    }
                }
            }
        }
        return true;
    }
}
