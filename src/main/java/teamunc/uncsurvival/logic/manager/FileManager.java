package teamunc.uncsurvival.logic.manager;

import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.team.TeamList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * S'occupe d'importer, de serializer et de sauvegarder tout données nécessaires
 */
public class FileManager extends AbstractManager{
    private String teamList_path;
    private String GameConfiguration_path;
    private File pluginDataFile;

    public FileManager(UNCSurvival plugin) {
        super(plugin);
        this.pluginDataFile = this.plugin.getDataFolder();
        if (!this.pluginDataFile.exists()) {
            this.pluginDataFile.mkdir();
        }

        // init paths
        this.teamList_path = this.pluginDataFile.getPath() + "/teams.unc_save";
        this.GameConfiguration_path = this.pluginDataFile.getPath() + "/game_config.unc_save";
    }

    public boolean saveTeams( TeamList teamList) {
        try {
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(new GZIPOutputStream(new FileOutputStream(teamList_path)));
            out.writeObject(teamList);
            out.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public TeamList loadTeams() {
        try {
            BukkitObjectInputStream in = new BukkitObjectInputStream(new GZIPInputStream(new FileInputStream(teamList_path)));
            TeamList teamList = (TeamList) in.readObject();
            in.close();
            return teamList;
        } catch (Exception e) {
            return null;
        }
    }
}
