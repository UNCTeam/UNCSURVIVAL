package teamunc.uncsurvival.logic.configuration;

import org.checkerframework.checker.units.qual.A;
import teamunc.uncsurvival.logic.goals.GoalItem;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class GameConfiguration implements Serializable {
    private Date datePhase2;
    private Date datePhase3;
    private ArrayList<GoalItem> goalItems = new ArrayList<>();

    public GameConfiguration(Date datePhase2, Date datePhase3, ArrayList<GoalItem> goalItems) {
        this.datePhase2 = datePhase2;
        this.datePhase3 = datePhase3;
        this.goalItems = goalItems;
    }

    public Date getDatePhase2() {
        return datePhase2;
    }

    public void setDatePhase2(Date datePhase2) {
        this.datePhase2 = datePhase2;
    }

    public Date getDatePhase3() {
        return datePhase3;
    }

    public void setDatePhase3(Date datePhase3) {
        this.datePhase3 = datePhase3;
    }

    public ArrayList<GoalItem> getGoalItems() {
        return goalItems;
    }

    public void setGoalItems(ArrayList<GoalItem> goalItems) {
        this.goalItems = goalItems;
    }
}
