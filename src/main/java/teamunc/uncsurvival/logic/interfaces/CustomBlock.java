package teamunc.uncsurvival.logic.interfaces;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import java.io.Serializable;

public class CustomBlock implements Serializable {
    private Location location;
    private transient ArmorStand armorStand;

    public CustomBlock(Location location, ArmorStand armorStand) {
        this.location = location;
        this.armorStand = armorStand;
    }

    public Location getLocation() {
        return location;
    }

    public ArmorStand getArmorStand() {
        return armorStand;
    }

    public void setArmorStand(ArmorStand armorStand) {
        this.armorStand = armorStand;
    }
}
