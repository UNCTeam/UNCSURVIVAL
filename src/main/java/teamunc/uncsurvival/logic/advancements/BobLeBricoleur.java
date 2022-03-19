package teamunc.uncsurvival.logic.advancements;

public class BobLeBricoleur extends Advancement{
    public BobLeBricoleur() {
        super("bob_le_bricoleur");
    }

    @Override
    public int givenPoints() {
        return 5000;
    }

    @Override
    public String DisplayedName() {
        return "Bob le bricoleur";
    }
}
