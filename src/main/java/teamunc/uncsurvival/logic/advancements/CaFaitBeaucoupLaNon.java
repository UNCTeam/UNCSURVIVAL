package teamunc.uncsurvival.logic.advancements;

public class CaFaitBeaucoupLaNon extends Advancement{
    public CaFaitBeaucoupLaNon() {
        super("ca_fait_beaucoup_la_non");
    }

    @Override
    public int givenPoints() {
        return 400;
    }

    @Override
    public String DisplayedName() {
        return "Ca fait beaucoup la non ?";
    }
}
