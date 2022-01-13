package teamunc.uncsurvival.eventsListeners;

import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.eventsListeners.customsBlock.BlockListener;
import teamunc.uncsurvival.eventsListeners.vanillaItems.ConsumeListenerVanilla;
import teamunc.uncsurvival.eventsListeners.customsItems.ConsumeListenerCustom;
import teamunc.uncsurvival.logic.manager.AbstractManager;

import java.util.ArrayList;

public class EventsManager extends AbstractManager {

    private ArrayList<AbstractEventsListener> eventsListeners = new ArrayList<>();

    public EventsManager(UNCSurvival plugin) {
        super(plugin);

        this.eventsListeners.add(new ConsumeListenerVanilla(this.plugin));
        this.eventsListeners.add(new ConsumeListenerCustom(this.plugin));
        this.eventsListeners.add(new BlockListener(this.plugin));

        // register
        for (AbstractEventsListener evLi : this.eventsListeners)
            this.plugin.getServer().getPluginManager().registerEvents(evLi,this.plugin);
    }
}
