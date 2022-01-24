package teamunc.uncsurvival.logic.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.configuration.GameConfiguration;
import teamunc.uncsurvival.logic.configuration.GameRuleConfiguration;
import teamunc.uncsurvival.logic.customBlock.customStorageBlock.CustomStorageBlock;
import teamunc.uncsurvival.logic.gameStats.GameStats;
import teamunc.uncsurvival.logic.phase.PhaseEnum;
import teamunc.uncsurvival.logic.team.TeamList;
import teamunc.uncsurvival.utils.serializerAdapter.*;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * S'occupe d'importer, de serializer et de sauvegarder tout données nécessaires
 */
public class FileManager extends AbstractManager{

    private String gameConfiguration_path;
    private File pluginDataFile;

    public FileManager(UNCSurvival plugin) {
        super(plugin);
        this.pluginDataFile = this.plugin.getDataFolder();
        if (!this.pluginDataFile.exists()) {
            this.pluginDataFile.mkdir();
        }

        // init paths
        this.gameConfiguration_path = this.pluginDataFile.getPath() + "/game-config.json";
    }

    public GameConfiguration loadGameConfiguration() {
        try {
            GameConfiguration gameConfiguration = this.loadJson(this.pluginDataFile.getPath() + "/game-config.json", GameConfiguration.class);
            return gameConfiguration;
        } catch (NoSuchFileException e) {
            // Le fichier n'existe pas alors on l'init et le créer
            Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "Creation du fichier de gameconfig");
            LocalDateTime phase = LocalDateTime.now();

            ArrayList<Integer> goalItemsPrices = new ArrayList<>();
            goalItemsPrices.add(1);
            goalItemsPrices.add(2);
            goalItemsPrices.add(3);
            goalItemsPrices.add(4);
            goalItemsPrices.add(5);


            GameConfiguration gameConfiguration = new GameConfiguration(phase, phase, phase, goalItemsPrices);
            this.plugin.getFileManager().saveGameConfiguration(gameConfiguration);
            return gameConfiguration;
        } catch (Exception e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + e.toString());
            return null;
        }
    }

    public boolean saveGameConfiguration(GameConfiguration gameConfiguration) {
        try {
            this.saveJson(gameConfiguration, this.pluginDataFile.getPath() + "/game-config.json");
            return true;
        } catch (Exception e) {
            Bukkit.getServer().getConsoleSender().sendMessage(e.toString());
            return false;
        }
    }

    public HashMap<Location, CustomStorageBlock> loadBlockManager() {
        try {
            Type typeOfHashMap = new TypeToken<HashMap<Location, CustomStorageBlock>>() { }.getType();
            HashMap<Location, CustomStorageBlock> customBlocks = this.loadJson(this.pluginDataFile.getPath() + "/custom-blocks.json", typeOfHashMap);
            return customBlocks;
        } catch (Exception e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + e.toString());
            return null;
        }
    }

    public boolean saveBlockManager(HashMap<Location, CustomStorageBlock> customBlocks) {
        try {
            this.saveJson(customBlocks, this.pluginDataFile.getPath() + "/custom-blocks.json");
            return true;
        } catch (Exception e) {
            Bukkit.getServer().getConsoleSender().sendMessage(e.toString());
            return false;
        }
    }

    public GameStats loadGameStats() {
        try {
            GameStats gameStats = this.loadJson(this.pluginDataFile.getPath() + "/game-stats.json", GameStats.class);
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
            this.saveJson(gameStats, this.pluginDataFile.getPath() + "/game-stats.json");
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
            GameRuleConfiguration gameRuleConfiguration = this.loadJson(this.pluginDataFile.getPath() + "/gamerule-config.json", GameRuleConfiguration.class);
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
            this.saveJson(gameRuleConfiguration.getGamerules(),this.pluginDataFile.getPath() + "/gamerule-config.json");
            return true;
        } catch (Exception e) {
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + e.toString());
            return false;
        }
    }

    public boolean saveTeams(TeamList teamList) {
        try {
            this.save(teamList,this.pluginDataFile.getPath() + "/teams.unc_save");
            return true;
        } catch (Exception e) {
            Bukkit.getServer().getConsoleSender().sendMessage(e.toString());
            return false;
        }
    }

    public TeamList loadTeams() {
        try {
            TeamList teamList = (TeamList) this.load(this.pluginDataFile.getPath() + "/teams.unc_save");
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
        try (FileOutputStream writer = new FileOutputStream(path)) {
            byte[] objet = this.getGson().toJson(o).getBytes(StandardCharsets.UTF_8);
            writer.write(objet);
        }
    }

    public Gson getGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .registerTypeAdapter(Inventory.class, new SerializeInventory())
                .registerTypeAdapter(Location.class, new LocationSerializer())
                .registerTypeAdapter(CustomStorageBlock.class, new CustomStorageBlockInterfaceCreator())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
                .disableHtmlEscaping()
                .enableComplexMapKeySerialization()
                .create();
    }

    private <T> T loadJson(String path, Type type) throws Exception {
        Reader reader = Files.newBufferedReader(Paths.get(path));
        T object = this.getGson().fromJson(reader, type);
        reader.close();
        return object;
    }
}
