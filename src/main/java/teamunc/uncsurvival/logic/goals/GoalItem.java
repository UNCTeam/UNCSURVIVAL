package teamunc.uncsurvival.logic.goals;

import org.bukkit.Material;

public class GoalItem {

    private Material item;
    private int points;

    public GoalItem(Material item, int points) {
        this.item = item;
        this.points = points;
    }

    public Material getItem() {
        return item;
    }

    public void setItem(Material item) {
        this.item = item;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
