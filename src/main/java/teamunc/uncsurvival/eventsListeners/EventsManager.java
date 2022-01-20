package teamunc.uncsurvival.eventsListeners;

import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.manager.AbstractManager;

import java.util.ArrayList;

public class EventsManager extends AbstractManager {

    private ArrayList<AbstractEventsListener> eventsListeners = new ArrayList<>();

    public EventsManager(UNCSurvival plugin) {
        super(plugin);

        this.eventsListeners.add(new ConsumeListenerVanilla(this.plugin));
        this.eventsListeners.add(new ConsumeListenerCustom(this.plugin));
        this.eventsListeners.add(new BlockListener(this.plugin));
        this.eventsListeners.add(new PlayerChatListener(this.plugin));
        this.eventsListeners.add(new PlayerConnectionListener(this.plugin));
        this.eventsListeners.add(new playerDeathListener(this.plugin));
        this.eventsListeners.add(new MobListener(this.plugin));

        // register
        for (AbstractEventsListener evLi : this.eventsListeners)
            this.plugin.getServer().getPluginManager().registerEvents(evLi,this.plugin);
    }
}
