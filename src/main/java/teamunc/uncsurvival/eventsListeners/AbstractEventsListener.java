package teamunc.uncsurvival.eventsListeners;

import org.bukkit.event.Listener;
import teamunc.uncsurvival.UNCSurvival;

public class AbstractEventsListener implements Listener {

    protected final UNCSurvival plugin;

    public AbstractEventsListener(UNCSurvival plugin) {
        this.plugin = plugin;
    }
}
