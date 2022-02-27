package teamunc.uncsurvival.logic.advancements;

import java.io.Serializable;
import java.util.ArrayList;

public class AdvancementList implements Serializable {
    private ArrayList<Advancement> advancements;

    public AdvancementList(ArrayList<Advancement> advancements) {
        this.advancements = advancements;
    }

    public ArrayList<Advancement> getAdvancements() {
        return advancements;
    }

    public void setAdvancements(ArrayList<Advancement> advancements) {
        this.advancements = advancements;
    }
}
