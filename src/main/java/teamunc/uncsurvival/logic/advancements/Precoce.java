package teamunc.uncsurvival.logic.advancements;

public class Precoce extends Advancement{
    public Precoce() {
        super("precoce");
    }

    @Override
    public int givenPoints() {
        return 3000;
    }

    @Override
    public String DisplayedName() {
        return "Précoce";
    }
}
