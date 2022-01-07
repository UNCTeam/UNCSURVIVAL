package teamunc.uncsurvival.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class LocationManager {

    //# SINGLETON
    private static LocationManager instance;
    private LocationManager() {}
    public static LocationManager getInstance() {
        if (LocationManager.instance == null) LocationManager.instance = new LocationManager();
        return LocationManager.instance;
    }
    //# END SINGLETON

    private ArrayList<Location> spawnPoints = new ArrayList<>();

    public ArrayList<Location> getSpawnPoints() {
        return spawnPoints;
    }

    public void setSpawnPoints(ArrayList<Location> spawnPoints) {
        this.spawnPoints = spawnPoints;
    }

    public boolean addNewSpawnPoint(Location loc) {
        return this.spawnPoints.add(loc);
    }

    public boolean removeSpawnPoint(Location loc) {
        boolean res = false;
        ArrayList<Location> spawnPointsCopy = new ArrayList<>(this.spawnPoints);

        for (Location location : spawnPointsCopy) {
            if ( location.getBlockX() == loc.getBlockX()
                    && location.getBlockY() == loc.getBlockY()
                    && location.getBlockZ() == loc.getBlockZ() ) {
                res = this.spawnPoints.remove(location);
            }
        }

        return res;
    }

    public void clearSpawnPoint() {
        this.spawnPoints = new ArrayList<>();
    }


    public void spreadPlayerWithSpawnPointList(Collection<Player> players) {
        ArrayList<Player> playersArray = new ArrayList<>(players);

        ArrayList<Location> shuffledSpawnPoints = this.spawnPoints;
        Collections.shuffle(shuffledSpawnPoints);

        shuffledSpawnPoints.forEach(location -> {
            if( playersArray.size() > 0) {
                Player playerActual = playersArray.remove(0);
                playerActual.teleport(location);
            }
        });

    }

    public void swapPlayerRandomly() {
        //TODO
    }

}
