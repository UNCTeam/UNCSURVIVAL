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
        String teamName = this.team.getChatColor() + this.team.getName();

        int addedSpaces = 23 - teamName.length();
        String nameFormated = teamName;

        for (int i = 0; i < addedSpaces; i++) {
            nameFormated += "\u258B";
        }

        String teamNameTrad = this.reduceAt(24) + "       " + nameFormated;
        teamNameTrad += this.translateInInterfaceDisplay("" + team.getRange(),5);
        this.inv = Bukkit.createInventory(null,27,name + teamNameTrad);
        return this.inv;
    }
}
