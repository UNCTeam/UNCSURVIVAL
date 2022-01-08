package teamunc.uncsurvival.features.thirst;

import teamunc.uncsurvival.utils.timer.TimeManager;

public class ThirstDisplay {
    //# SINGLETON
    private static ThirstDisplay instance;
    private ThirstDisplay() {}
    public static ThirstDisplay getInstance() {
        if (ThirstDisplay.instance == null) ThirstDisplay.instance = new ThirstDisplay();
        return ThirstDisplay.instance;
    }
    //# END SINGLETON


}
