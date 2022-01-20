package teamunc.uncsurvival.logic.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.configuration.GameConfiguration;
import teamunc.uncsurvival.logic.configuration.GameRuleConfiguration;
import teamunc.uncsurvival.logic.gameStats.GameStats;
import teamunc.uncsurvival.logic.phase.PhaseEnum;
import teamunc.uncsurvival.logic.team.TeamList;
import teamunc.uncsurvival.utils.serializerAdapter.GameConfigurationDeserializer;
import teamunc.uncsurvival.utils.serializerAdapter.GameConfigurationSerializer;
import teamunc.uncsurvival.utils.serializerAdapter.LocalDateTimeSerializer;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * S'occupe d'importer, de serializer et de sauvegarder tout données nécessaires
 */
public class FileManager extends AbstractManager{
    private String teamList_path;
    private String gameRuleConfiguration_path;
    private String gameConfiguration_path;
    private String gameStats_path;
    private String playersInfos_path;
    private String participants_path;
    private File pluginDataFile;

    public FileManager(UNCSurvival plugin) {
        super(plugin);
        this.pluginDataFile = this.plugin.getDataFolder();
        if (!this.pluginDataFile.exists()) {
            this.pluginDataFile.mkdir();
        }

        // init paths
        this.teamList_path = this.pluginDataFile.getPath() + "/teams.unc_save";
        this.gameRuleConfiguration_path = this.pluginDataFile.getPath() + "/gamerule-config.json";
        this.gameConfiguration_path = this.pluginDataFile.getPath() + "/game-config.json";
        this.gameStats_path = this.pluginDataFile.getPath() + "/game-stats.json";
        this.playersInfos_path = this.pluginDataFile.getPath() + "/players-infos.unc_save";
        this.participants_path = this.pluginDataFile.getPath() + "/participants.unc_save";
    }

    public GameConfiguration loadGameConfiguration() {
        try {
            GameConfiguration gameConfiguration = (GameConfiguration) this.loadJson(gameConfiguration_path, GameConfiguration.class);
            return gameConfiguration;
        } catch (NoSuchFileException e) {
            // Le fichier n'existe pas alors on l'init et le créer
            Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "Creation du fichier de gameconfig");
            LocalDateTime phase = LocalDateTime.now();

            ArrayList<Material> goalItems = new ArrayList<>();
            goalItems.add(Material.BRICK);
            goalItems.add(Material.STONE);
            goalItems.add(Material.STONE);
            goalItems.add(Material.STONE);
            goalItems.add(Material.STONE);

            ArrayList<Integer> goalItemsPrices = new ArrayList<>();
            goalItemsPrices.add(1);
            goalItemsPrices.add(2);
            goalItemsPrices.add(3);
            goalItemsPrices.add(4);
            goalItemsPrices.add(5);


            GameConfiguration gameConfiguration = new GameConfiguration(phase, phase, phase, goalItems,goalItemsPrices);
            this.plugin.getFileManager().saveGameConfiguration(gameConfiguration);
            return gameConfiguration;
        } catch (Exception e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + e.toString());
            return null;
        }
    }

    public boolean saveGameConfiguration(GameConfiguration gameConfiguration) {
        try {
            this.saveJson(gameConfiguration, gameConfiguration_path);
            return true;
        } catch (Exception e) {
            Bukkit.getServer().getConsoleSender().sendMessage(e.toString());
            return false;
        }
    }

    public GameStats loadGameStats() {
        try {
            GameStats gameStats = this.loadJson(gameStats_path, GameStats.class);
            return gameStats;
        } catch (NoSuchFileException e) {
            // Le fichier n'existe pas alors on l'init et le créer
            Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "Creation du fichier de gameStats");
            GameStats gameStats = new GameStats(false, PhaseEnum.INIT);
            this.plugin.getFileManager().saveGameStats(gameStats);
            return gameStats;
        } catch (Exception e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + e.toString());
            return null;
        }
    }

    public boolean saveGameStats(GameStats gameStats) {
        try {
            Gson gson = new Gson();
            this.saveJson(gameStats, gameStats_path);
            return true;
        } catch (Exception e) {
            Bukkit.getServer().getConsoleSender().sendMessage(e.toString());
            return false;
        }
    }

    public GameRuleConfiguration loadGameRuleConfiguration() {
        try {
            // Type utile pour save des hashmap
            //Type type = new TypeToken<HashMap<GameRule, Boolean>>(){}.getType();
            GameRuleConfiguration gameRuleConfiguration = this.loadJson(this.gameRuleConfiguration_path, GameRuleConfiguration.class);
            return gameRuleConfiguration;
        } catch (NoSuchFileException e) {
            // Le fichier n'existe pas alors on l'init et le créer
            Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "Creation du fichier de gamerule");
            HashMap<GameRule, Boolean> gamerules = new HashMap<>();
            gamerules.put(GameRule.DISABLE_ELYTRA_MOVEMENT_CHECK, true);
            gamerules.put(GameRule.DO_FIRE_TICK, true);
            gamerules.put(GameRule.DROWNING_DAMAGE, false);
            GameRuleConfiguration gameRuleConfiguration = new GameRuleConfiguration(gamerules);
            this.plugin.getFileManager().saveGameRuleConfiguration(gameRuleConfiguration);
            return gameRuleConfiguration;
        } catch (Exception e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + e.toString());
            return null;
        }
    }

    public boolean saveGameRuleConfiguration(GameRuleConfiguration gameRuleConfiguration) {
        try {
            this.saveJson(gameRuleConfiguration.getGamerules(),gameRuleConfiguration_path);
            return true;
        } catch (Exception e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + e.toString());
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

            this.getGson().toJson(o, writer);
        }
    }

    public Gson getGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
                .disableHtmlEscaping()
                .create();
    }

    private <T> T loadJson(String path, Type type) throws Exception {
        Reader reader = Files.newBufferedReader(Paths.get(path));
        T object = this.getGson().fromJson(reader, type);
        reader.close();
        return object;
    }
}
