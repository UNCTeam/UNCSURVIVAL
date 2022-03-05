package teamunc.uncsurvival.utils;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import teamunc.uncsurvival.UNCSurvival;

import java.io.Serializable;

public class Region implements Serializable{
    private int minX;
    private int maxX;
    private int minZ;
    private int maxZ;

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
