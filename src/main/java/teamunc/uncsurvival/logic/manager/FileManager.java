package teamunc.uncsurvival.logic.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.configuration.GameConfiguration;
import teamunc.uncsurvival.logic.configuration.GameRuleConfiguration;
import teamunc.uncsurvival.logic.interfaces.GameInterfaceList;
import teamunc.uncsurvival.logic.player.PlayersInformations;
import teamunc.uncsurvival.logic.team.TeamList;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * S'occupe d'importer, de serializer et de sauvegarder tout données nécessaires
 */
public class FileManager extends AbstractManager{
    private String teamList_path;
    private String gameRuleConfiguration_path;
    private String gameConfiguration_path;
    private String playersInfos_path;
    private String interfaces_path;
    private File pluginDataFile;

    public FileManager(UNCSurvival plugin) {
        super(plugin);
        this.pluginDataFile = this.plugin.getDataFolder();
        if (!this.pluginDataFile.exists()) {
            this.pluginDataFile.mkdir();
        }

        // init paths
        this.teamList_path = this.pluginDataFile.getPath() + "/teams.unc_save";
        this.gameRuleConfiguration_path = this.pluginDataFile.getPath() + "/game-config.json";
        this.gameConfiguration_path = this.pluginDataFile.getPath() + "/gamerule-config.json";
        this.playersInfos_path = this.pluginDataFile.getPath() + "/players-infos.unc_save";
        this.interfaces_path = this.pluginDataFile.getPath() + "/interfaces.unc_save";
    }

    public GameConfiguration loadGameConfiguration() {
        try {
            GameConfiguration gameConfiguration = (GameConfiguration) this.loadJson(gameConfiguration_path, GameConfiguration.class);
            return gameConfiguration;
        } catch (Exception e) {
            Bukkit.getServer().getConsoleSender().sendMessage(e.toString());
            return null;
        }
    }

    public boolean saveGameConfiguration(GameConfiguration gameConfiguration) {
        try {
            Gson gson = new Gson();
            Bukkit.broadcastMessage(gameConfiguration.getDatePhase3().toString());
            Bukkit.broadcastMessage(gson.toJson(gameConfiguration));
            this.saveJson(gameConfiguration, gameConfiguration_path);
            return true;
        } catch (Exception e) {
            Bukkit.getServer().getConsoleSender().sendMessage(e.toString());
            return false;
        }
    }

    public GameRuleConfiguration loadGameRuleConfiguration() {
        try {
            GameRuleConfiguration gameRuleConfiguration = (GameRuleConfiguration) this.loadJson(gameRuleConfiguration_path, GameRuleConfiguration.class);
            return gameRuleConfiguration;
        } catch (Exception e) {
            Bukkit.getServer().getConsoleSender().sendMessage(e.toString());
            return null;
        }
    }

    public boolean saveGameRuleConfiguration(GameRuleConfiguration gameRuleConfiguration) {
        try {
            this.saveJson(gameRuleConfiguration,gameRuleConfiguration_path);
            return true;
        } catch (Exception e) {
            Bukkit.getServer().getConsoleSender().sendMessage(e.toString());
            return false;
        }
    }

    public boolean saveTeams(TeamList teamList) {
        try {
            this.save(teamList,teamList_path);
            return true;
        } catch (Exception e) {
            Bukkit.getServer().getConsoleSender().sendMessage(e.toString());
            return false;
        }
    }

    public TeamList loadTeams() {
        try {
            TeamList teamList = (TeamList) this.load(teamList_path);
            return teamList;
        } catch (Exception e) {
            Bukkit.getServer().getConsoleSender().sendMessage(e.toString());
            return null;
        }
    }

    public boolean savePlayersInfos(PlayersInformations playersInfos) {
        try {
            this.save(playersInfos,playersInfos_path);
            return true;
        } catch (Exception e) {
            Bukkit.getServer().getConsoleSender().sendMessage(e.toString());
            return false;
        }
    }

    public PlayersInformations loadPlayersInfos() {
        try {
            PlayersInformations playersInfos = (PlayersInformations) this.load(playersInfos_path);
            return playersInfos;
        } catch (Exception e) {
            Bukkit.getServer().getConsoleSender().sendMessage(e.toString());
            return null;
        }
    }

    public boolean saveInterfaces(GameInterfaceList gameInterfaceList) {
        try {
            plugin.getMessageTchatManager().sendGeneralMesssage(gameInterfaceList.getInterfaces().values().toString());
            this.save(gameInterfaceList,interfaces_path);
            return true;
        } catch (Exception e) {
            Bukkit.getServer().getConsoleSender().sendMessage(e.toString());
            return false;
        }
    }

    public GameInterfaceList loadInterfaces() {
        try {
            GameInterfaceList gameInterfaceList = (GameInterfaceList) this.load(interfaces_path);
            plugin.getMessageTchatManager().sendGeneralMesssage(gameInterfaceList.getInterfaces().values().toString());
            return gameInterfaceList;
        } catch (Exception e) {
            Bukkit.getServer().getConsoleSender().sendMessage(e.toString());
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
        try (Writer writer = new FileWriter(path)) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .disableHtmlEscaping()
                    .create();
            gson.toJson(o, writer);
        }
    }

    private <T> T loadJson(String path, Class<T> clazz) throws Exception {
        Reader reader = Files.newBufferedReader(Paths.get(path));
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .disableHtmlEscaping()
                .create();
        T object = gson.fromJson(reader, clazz);
        reader.close();
        return object;
    }
}
