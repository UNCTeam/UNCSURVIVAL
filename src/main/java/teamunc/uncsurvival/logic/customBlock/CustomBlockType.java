package teamunc.uncsurvival.logic.customBlock;

public enum CustomBlockType {
    GROWTH_BLOCK(101),
    BOTTLER_BLOCK(102),
    MINCER_BLOCK(103);

    private Integer model;

    CustomBlockType(Integer model) {
        this.model = model;
    }

    public Integer getModel() {
        return model;
    }

    public void setModel(Integer model) {
        this.model = model;
    }
}
