package teamunc.uncsurvival.logic.advancements;

public class Raciste extends Advancement{
    public Raciste() {
        super("raciste");
    }

    @Override
    public int givenPoints() {
        return 1500;
    }

    @Override
    public String DisplayedName() {
        return "Raciste";
    }
}
