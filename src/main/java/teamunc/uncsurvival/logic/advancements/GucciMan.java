package teamunc.uncsurvival.logic.advancements;

public class GucciMan extends Advancement{
    public GucciMan() {
        super("gucci_man");
    }

    @Override
    public int givenPoints() {
        return 20000;
    }

    @Override
    public String DisplayedName() {
        return "Gucci-Man";
    }
}
