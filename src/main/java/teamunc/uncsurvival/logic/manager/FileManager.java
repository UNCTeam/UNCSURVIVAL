package teamunc.uncsurvival.logic.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.interfaces.GameInterfaceList;
import teamunc.uncsurvival.logic.player.PlayersInformations;
import teamunc.uncsurvival.logic.team.TeamList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * S'occupe d'importer, de serializer et de sauvegarder tout données nécessaires
 */
public class FileManager extends AbstractManager{
    private String teamList_path;
    private String gameConfiguration_path;
    private String playersInfos_path;
    private File pluginDataFile;

    public FileManager(UNCSurvival plugin) {
        super(plugin);
        this.pluginDataFile = this.plugin.getDataFolder();
        if (!this.pluginDataFile.exists()) {
            this.pluginDataFile.mkdir();
        }

        // init paths
        this.teamList_path = this.pluginDataFile.getPath() + "/teams.unc_save";
        this.gameConfiguration_path = this.pluginDataFile.getPath() + "/game_config.unc_save";
        this.playersInfos_path = this.pluginDataFile.getPath() + "/players_infos.unc_save";
    }

    public boolean saveTeams(TeamList teamList) {
        try {
            this.save(teamList,teamList_path);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public TeamList loadTeams() {
        try {
            TeamList teamList = (TeamList) this.load(teamList_path);
            return teamList;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean savePlayersInfos(PlayersInformations playersInfos) {
        try {
            this.save(playersInfos,playersInfos_path);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public PlayersInformations loadPlayersInfos() {
        try {
            PlayersInformations playersInfos = (PlayersInformations) this.load(playersInfos_path);
            return playersInfos;
        } catch (Exception e) {
            return null;
        }
    }

    private void save(Object o, String path) throws Exception{
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(new GZIPOutputStream(new FileOutputStream(path)));
            out.writeObject(o);
            out.close();
    }

    private Object load(String path) throws Exception{
            BukkitObjectInputStream in = new BukkitObjectInputStream(new GZIPInputStream(new FileInputStream(path)));
            Object res = in.readObject();
            in.close();
            return res;
    }

    private void saveJson(Object o, String path) throws Exception {
        new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()
                .toJson(o, new FileWriter(path));
    }

    private Object loadJson(String path) throws Exception {
        Gson gson = new Gson();
        Object res = gson.fromJson(Files.newBufferedReader(Paths.get(path)), Object.class);
        return res;
    }
}
