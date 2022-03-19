package teamunc.uncsurvival.logic.advancements;

public class PeteMaBiere extends Advancement{
    public PeteMaBiere() {
        super("pete_ma_biere");
    }

    @Override
    public int givenPoints() {
        return 30000;
    }

    @Override
    public String DisplayedName() {
        return "Je pete ma bière, ma lubulule";
    }
}
