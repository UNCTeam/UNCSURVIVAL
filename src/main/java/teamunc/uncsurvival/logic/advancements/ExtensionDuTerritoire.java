package teamunc.uncsurvival.logic.advancements;

public class ExtensionDuTerritoire extends Advancement{

    public ExtensionDuTerritoire() {
        super("extension_de_territoire");
    }

    @Override
    public int givenPoints() {
        return 10000;
    }

    @Override
    public String DisplayedName() {
        return "Extension du territoire";
    }
}
