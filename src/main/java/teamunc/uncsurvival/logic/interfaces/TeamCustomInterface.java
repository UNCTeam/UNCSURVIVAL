package teamunc.uncsurvival.logic.interfaces;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import teamunc.uncsurvival.logic.team.Team;

public class TeamCustomInterface extends GameCustomInterface{

    private Team team;
    public TeamCustomInterface(Team team) {
        super(5);
        this.team = team;
    }

    @Override
    public Inventory update() {
        // TODO interface de team
        String teamName = this.team.getChatColor() + this.team.getName();

        String teamNameTrad = this.reduceAt(21) + "       " + teamName;
        this.inv = Bukkit.createInventory(null,27,name + teamNameTrad);
        return this.inv;
    }
}
