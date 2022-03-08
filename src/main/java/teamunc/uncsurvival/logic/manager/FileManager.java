package teamunc.uncsurvival.logic.manager;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.advancements.*;
import teamunc.uncsurvival.logic.configuration.GameConfiguration;
import teamunc.uncsurvival.logic.configuration.GameRuleConfiguration;
import teamunc.uncsurvival.logic.customBlock.customStorageBlock.CustomStorageBlock;
import teamunc.uncsurvival.logic.customBlock.customStorageBlock.GrowthBlock;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * S'occupe d'importer, de serializer et de sauvegarder tout données nécessaires
 */
public class FileManager extends AbstractManager{

    private String gameConfiguration_path;
    private String logFileNamePath;
    private File pluginDataFile;
    private File propertiesFile;

    public FileManager(UNCSurvival plugin) {
        super(plugin);
        this.pluginDataFile = this.plugin.getDataFolder();
        if (!this.pluginDataFile.exists()) {
            this.pluginDataFile.mkdir();
        }

        // init paths
        this.gameConfiguration_path = this.pluginDataFile.getPath() + "/game-config.json";
        this.logFileNamePath = pluginDataFile.getPath() + "/logs-" + LocalDateTime.now() + ".txt";

        // get server properties
        this.propertiesFile = new File("server.properties");
    }

    public GameConfiguration loadGameConfiguration() {
        try {
            GameConfiguration gameConfiguration = this.loadJson(this.pluginDataFile.getPath() + "/game-config.json", GameConfiguration.class);
            return gameConfiguration;
        } catch (NoSuchFileException e) {
            // Le fichier n'existe pas alors on l'init et le créer
            Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "Creation du fichier game-config");
            LocalDateTime phase = LocalDateTime.now();

            ArrayList<Integer> goalItemsPrices = new ArrayList<>();
            goalItemsPrices.add(1);
            goalItemsPrices.add(2);
            goalItemsPrices.add(3);
            goalItemsPrices.add(4);
            goalItemsPrices.add(5);
            goalItemsPrices.add(6);
            goalItemsPrices.add(7);


            GameConfiguration gameConfiguration = new GameConfiguration(phase, phase, phase, goalItemsPrices,false);
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
            // Le fichier n'existe pas alors on l'init et le créer
            Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "Creation du fichier custom-blocks");
            return new HashMap<>();
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

    public AdvancementList loadAdvancementList() {
        try {
            AdvancementList list = (AdvancementList) this.load(this.pluginDataFile.getPath() + "/advancements-unc.unc_save");
            return list;
        } catch (Exception e) {
            // Le fichier n'existe pas alors on l'init et le créer
            Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "Creation du fichier advancements-unc");
            AdvancementList list = new AdvancementList(
                new ArrayList<>(
                    List.of(
                            new BobLeBricoleur(),
                            new CaFaitBeaucoupLaNon(),
                            new ExtensionDuTerritoire(),
                            new GucciMan(),
                            new MonChandail(),
                            new PeteMaBiere(),
                            new Precoce(),
                            new Raciste()
                    )
                )
            );
            this.plugin.getFileManager().saveAdvancementList(list);
            return list;
        }
    }

    public boolean saveAdvancementList(AdvancementList list) {
        try {
            this.save(list, this.pluginDataFile.getPath() + "/advancements-unc.unc_save");
            return true;
        } catch (Exception e) {
            Bukkit.getServer().getConsoleSender().sendMessage(e.toString());
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
            // Le fichier n'existe pas alors on l'init et le créer
            Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "Creation du fichier teams");
            TeamList teamList = new TeamList();
            this.plugin.getFileManager().saveTeams(teamList);
            return teamList;
        }
    }

    public ArrayList<Location> loadBrewingStands() {
        try {
            ArrayList<Location> res = (ArrayList<Location>) this.load(this.pluginDataFile.getPath() + "/brewingstands_locations.unc_save");
            return res;
        } catch (Exception e) {
            // Le fichier n'existe pas alors on l'init et le créer
            Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "Creation du fichier brewingstands_locations");
            return new ArrayList<Location>();
        }
    }

    public boolean saveBrewingStands(ArrayList<Location> locations) {
        try {
            this.save(locations,this.pluginDataFile.getPath() + "/brewingstands_locations.unc_save");
            return true;
        } catch (Exception e) {
            Bukkit.getServer().getConsoleSender().sendMessage(e.toString());
            return false;
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

    public String getServerProperties(String s) {
        File f = this.propertiesFile;

        Properties pr = new Properties();
        try
        {
            FileInputStream in = new FileInputStream(f);
            pr.load(in);
            String string = pr.getProperty(s);

            return string;
        }
        catch (IOException e)
        {}
        return "";
    }

    public void unlockAdvancement(UUID uuid, Advancement advancement) {
        File worldFolder = Bukkit.getWorld(getServerProperties("level-name")).getWorldFolder();
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        if (player.isOnline()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"advancement grant " + player.getName() + " only " + advancement.getKey().toString());
        } else {
            String path = worldFolder.getPath() + "/advancements/" + uuid + ".json";

            try {
                FileReader reader = new FileReader(path);
                JsonObject jsonAdvancement = (JsonObject) JsonParser.parseReader(reader);
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

                if (!jsonAdvancement.has(advancement.getKey().toString())) {
                    JsonObject elem = new JsonObject();
                    JsonObject criteria = new JsonObject();
                    criteria.addProperty("unlock",format.format(new Date(System.currentTimeMillis())));
                    elem.add("criteria",criteria);
                    elem.addProperty("done",true);

                    jsonAdvancement.add(advancement.getKey().toString(),elem);
                }
                reader.close();
                FileWriter writer = new FileWriter(path);
                writer.write(jsonAdvancement.toString());
                writer.close();
            } catch (IOException e) {

            }

        }
    }

    public void writeInLogFile(String line) {
        try {
            FileWriter writer = new FileWriter(this.logFileNamePath,true);
            writer.append(line);
            writer.append(System.lineSeparator());
            writer.close();
        } catch (Exception e){}

    }
}
