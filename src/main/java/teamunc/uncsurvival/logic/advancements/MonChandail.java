package teamunc.uncsurvival.logic.advancements;

public class MonChandail extends Advancement{
    public MonChandail() {
        super("mon_chandail");
    }

    @Override
    public int givenPoints() {
        return 300;
    }

    @Override
    public String DisplayedName() {
        return "Oh non, mon chandail !";
    }
}
