package teamunc.uncsurvival.logic.customBlock.customStorageBlock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.*;
import org.bukkit.block.data.Directional;
import org.bukkit.event.inventory.InventoryType;
import teamunc.uncsurvival.logic.customBlock.CustomBlock;
import teamunc.uncsurvival.logic.customBlock.CustomBlockType;

import java.util.UUID;

public class MincerBlock extends CustomStorageBlock {

    public MincerBlock(Location location, CustomBlockType customBlockType) {
        super(location, customBlockType);
    }

    @Override
    public void tickAction() {
        // Vérifi si le duration == 0
        // Si oui
            // Vérifi si il y a un output
            // Si pas output -> return / ne fait rien

        //

        // Check si il y a qqchose dans le storage

        // Si oui prends un itemStack et decrement duration

        // Check si input existant
            // Si oui check si storage plein
                // Si storage vide prends un itemstack
    }
}
