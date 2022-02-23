package teamunc.uncsurvival.commandesListeners;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.manager.GameManager;
import teamunc.uncsurvival.logic.phase.PhaseEnum;
import teamunc.uncsurvival.logic.player.GamePlayer;

import java.util.Locale;

public class PlayerCmdExec extends AbstractCommandExecutor{
    public PlayerCmdExec(UNCSurvival plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        GameManager gameManager = this.plugin.getGameManager();
        boolean commandValid = true;

        if (sender instanceof Player )
            switch ( label ) {
                case "food":
                case "f":
                    GamePlayer gamePlayer = gameManager.getParticipantManager().getGamePlayer(((Player) sender).getDisplayName());
                    String sendedMsg = "§8-| §b§lHistorique Nourriture §8|---------------\n";
                    for (Material material : gamePlayer.getQueueOfEatenFood()) {
                        if (gamePlayer.EatHowOften(material) >= 4 && (
                                gameManager.getGameStats().getCurrentPhase() == PhaseEnum.PHASE2 ||
                                gameManager.getGameStats().getCurrentPhase() == PhaseEnum.PHASE3
                            )
                        )
                            sendedMsg += "§c" + material.name().toLowerCase(Locale.ROOT).replace('_',' ');
                        else
                            sendedMsg += "§a" + material.name().toLowerCase(Locale.ROOT).replace('_',' ');


                        sendedMsg += "§8§l | ";
                    }
                    sender.sendMessage(sendedMsg);
                    break;

                case "activealcoolquality":
                    // ALCOOL QUALITY
                    this.plugin.getGameManager().setAlcoolQualityInGame(true);
                    ShapedRecipe alcoolQuality = new ShapedRecipe(new NamespacedKey(this.plugin, "craftAlcoolQuality"), this.plugin.getGameManager().getItemsManager().createAlcool());
                    alcoolQuality.shape("***", "*-*", "***");
                    alcoolQuality.setIngredient('*', Material.ROTTEN_FLESH);
                    alcoolQuality.setIngredient('-', Material.POTION);
                    this.plugin.getServer().addRecipe(alcoolQuality);
                    sender.sendMessage("§8---------| §b§lAlcool Quality Activated §8|---------\n");
                    break;
            }
        return commandValid;
    }
}
