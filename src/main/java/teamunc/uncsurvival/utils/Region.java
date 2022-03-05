package teamunc.uncsurvival.utils;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import teamunc.uncsurvival.UNCSurvival;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Region implements Serializable{
    private int minX;
    private int maxX;
    private int minZ;
    private int maxZ;
    private ArrayList<String> uuidsInRegion = new ArrayList<>();

    public Region(Location loc, int range) {
        this.minX = loc.getBlockX()-range;
        this.maxX = loc.getBlockX()+range;
        this.minZ = loc.getBlockZ()-range;
        this.maxZ = loc.getBlockZ()+range;

        Location cornerMIN = new Location(loc.getWorld(),this.minX,0,this.minZ);
        Location cornerMAX = new Location(loc.getWorld(),this.maxX,0,this.maxZ);
        this.forceChunksWithinChunkLocation(cornerMIN.getChunk(),cornerMAX.getChunk());
    }

    public void forceChunksWithinChunkLocation(Chunk chunkMin, Chunk chunkMax) {
        for (int i = chunkMin.getX() ; i <= chunkMax.getX(); i++) {
            for (int j = chunkMin.getZ() ; j <= chunkMax.getZ(); j++) {
                if (!chunkMax.getWorld().getChunkAt(i,j).isForceLoaded()) {
                    Bukkit.getConsoleSender().sendMessage("force the chunk at : " + i + " " + j);
                    chunkMax.getWorld().getChunkAt(i, j).setForceLoaded(true);
                }
            }
        }
    }

    public void unforceChunksWithinChunkLocation(World world) {
        Location cornerMIN = new Location(world,this.minX,0,this.minZ);
        Location cornerMAX = new Location(world,this.maxX,0,this.maxZ);

        Chunk chunkMin = cornerMIN.getChunk();
        Chunk chunkMax = cornerMAX.getChunk();

        for (int i = chunkMin.getX() ; i <= chunkMax.getX(); i++) {
            for (int j = chunkMin.getZ() ; j <= chunkMax.getZ(); j++) {
                if (chunkMax.getWorld().getChunkAt(i,j).isForceLoaded()) {
                    Bukkit.getConsoleSender().sendMessage("unforce the chunk at : " + i + " " + j);
                    chunkMax.getWorld().getChunkAt(i, j).setForceLoaded(false);
                }
            }
        }
    }

    public void addRange(World world, int addedRange) {
        this.minX -= addedRange;
        this.maxX += addedRange;
        this.minZ -= addedRange;
        this.maxZ += addedRange;

        Location cornerMIN = new Location(world,this.minX,0,this.minZ);
        Location cornerMAX = new Location(world,this.maxX,0,this.maxZ);

        this.forceChunksWithinChunkLocation(cornerMIN.getChunk(),cornerMAX.getChunk());
    }

    public World getWorld() {
        return UNCSurvival.getInstance().getGameManager().getMainWorld();
    }

    public int getMinX() {
        return minX;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public boolean contains(Region region) {
        return region.getWorld().equals(getWorld()) &&
                region.getMinX() >= minX && region.getMaxX() <= maxX &&
                region.getMinZ() >= minZ && region.getMaxZ() <= maxZ;
    }

    public boolean contains(Location location) {
        return contains(location.getBlockX(), location.getBlockZ());
    }

    public boolean contains(int x, int z) {
        return x >= minX && x <= maxX &&
                z >= minZ && z <= maxZ;
    }

    public boolean overlaps(Region region) {
        return region.getWorld().equals(getWorld()) &&
                !(region.getMinX() > maxX || region.getMinZ() > maxZ ||
                        minZ > region.getMaxX() || minZ > region.getMaxZ());
    }

    public void enterInRegion(String uuid) {
        this.uuidsInRegion.add(uuid);
        LoggerFile.AppendLineToWrite("[REGION] L'UUID "+uuid+ " ENTRE DANS LA REGION " + this + " LE " + LocalDateTime.now());
    }

    public void leaveTheRegion(String uuid) {
        this.uuidsInRegion.remove(uuid);
        LoggerFile.AppendLineToWrite("[REGION] L'UUID "+uuid+ " QUITTE LA REGION " + this + " LE " + LocalDateTime.now());
    }

    public boolean inRegion(String uuid) {
        return this.uuidsInRegion.contains(uuid);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Region)) {
            return false;
        }
        final Region other = (Region) obj;
        return getWorld().equals(other.getWorld())
                && minX == other.minX
                && minZ == other.minZ
                && maxX == other.maxX
                && maxZ == other.maxZ;
    }

    @Override
    public String toString() {
        return "Region[world:" + getWorld().getName() +
                ", minX:" + minX +
                ", minZ:" + minZ +
                ", maxX:" + maxX +
                ", maxZ:" + maxZ + "]";
    }
}
