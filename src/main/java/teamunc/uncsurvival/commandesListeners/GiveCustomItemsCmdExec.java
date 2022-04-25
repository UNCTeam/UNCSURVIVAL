package teamunc.uncsurvival.commandesListeners;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
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
                        case "diamondApple":
                            p.getInventory().addItem(this.plugin.getGameManager().getItemsManager().createDiamondApple());
                            break;
                        case "wrench":
                            p.getInventory().addItem(this.plugin.getGameManager().getItemsManager().createWrenchItem(0, 0));
                            break;
                        case "healPatch":
                            p.getInventory().addItem(this.plugin.getGameManager().getItemsManager().createHealPatch());
                            break;
                        case "vaccin":
                            p.getInventory().addItem(this.plugin.getGameManager().getItemsManager().createVaccin());
                            break;
                        case "alcool":
                            p.getInventory().addItem(this.plugin.getGameManager().getItemsManager().createAlcool());
                            break;
                        case "module":
                            p.getInventory().addItem(this.plugin.getGameManager().getItemsManager().createModule());
                            break;
                        case "growthBlock":
                            p.getInventory().addItem(this.plugin.getGameManager().getItemsManager().createGrowthItemBlock());
                            break;
                        case "mincer":
                            p.getInventory().addItem(this.plugin.getGameManager().getItemsManager().createMincerItemBlock());
                            break;
                        case "mincedMeat":
                            p.getInventory().addItem(this.plugin.getGameManager().getItemsManager().createMincedMeat());
                            break;
                        case "mincedTofuMeat":
                            p.getInventory().addItem(this.plugin.getGameManager().getItemsManager().createMincedTofuMeat());
                            break;
                        case "burger":
                            p.getInventory().addItem(this.plugin.getGameManager().getItemsManager().createBurger());
                            break;
                        case "veggieBurger":
                            p.getInventory().addItem(this.plugin.getGameManager().getItemsManager().createVeggieBurger());
                            break;
                        case "glowingCarrot":
                            p.getInventory().addItem(this.plugin.getGameManager().getItemsManager().createGlowingCarrot());
                            break;
                        case "glowingDripstone":
                            p.getInventory().addItem(this.plugin.getGameManager().getItemsManager().createGlowingDripStone());
                            break;
                        case "cactusJuice":
                            p.getInventory().addItem(this.plugin.getGameManager().getItemsManager().createCactusJuice());
                            break;
                        case "wheatFlour":
                            p.getInventory().addItem(this.plugin.getGameManager().getItemsManager().createWheatFlour());
                            break;
                        case "amethystIngot":
                            p.getInventory().addItem(this.plugin.getGameManager().getItemsManager().createAmethystIngot());
                            break;
                        case "amethystSword":
                            p.getInventory().addItem(this.plugin.getGameManager().getItemsManager().createAmethystSword());
                            break;
                        case "amethystPickaxe":
                            p.getInventory().addItem(this.plugin.getGameManager().getItemsManager().createAmethystPickaxe());
                            break;
                        case "famineSoup":
                            p.getInventory().addItem(this.plugin.getGameManager().getItemsManager().createFamineSoup());
                            break;
                        case "gourde":
                            p.getInventory().addItem(this.plugin.getGameManager().getItemsManager().createGourde());
                            break;
                    }
                }
            }
        }
        return true;
    }
}
