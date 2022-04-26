package teamunc.uncsurvival.logic.team;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.advancements.Advancement;
import teamunc.uncsurvival.logic.customBlock.CustomBlock;
import teamunc.uncsurvival.logic.interfaces.GoalCustomInterface;
import teamunc.uncsurvival.logic.interfaces.TeamCustomInterface;
import teamunc.uncsurvival.logic.manager.AdvancementManager;
import teamunc.uncsurvival.logic.manager.ItemsManager;
import teamunc.uncsurvival.logic.phase.PhaseEnum;
import teamunc.uncsurvival.logic.player.GamePlayer;
import teamunc.uncsurvival.utils.LoggerFile;
import teamunc.uncsurvival.utils.Region;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class Team implements Serializable {

    private final UUID uuid;
    private String name;
    private ChatColor chatColor;
    private ArrayList<Integer> itemsProduction = new ArrayList<>(Arrays.asList(0,0,0,0,0));
    private int bonusScore = 0;
    private Location spawnPoint;
    private ArrayList<CustomBlock> interfacesGoals = new ArrayList<>();
    private Location interfaceTeam;
    private int range = 5;
    private Region region;
    private final List<GamePlayer> members;
    private boolean isFamined;
    private int score;

    public Team(String name, ChatColor chatColor, Location spawnPoint) {
        this.score = 0;
        this.name = name;
        this.chatColor = chatColor;
        this.members = new ArrayList<>();


        this.uuid = UUID.randomUUID();

        this.spawnPoint = spawnPoint;
        this.region = new Region(spawnPoint, range);

        this.initInterfaceBlockAtStart();
    }

    public void sendToEveryOnlinePlayer(String message) {
        for (GamePlayer gp :
                this.getMembers()) {
            if ( gp.isOnline() )
                gp.getBukkitPlayer().sendMessage(message);
        }
    }

    public Location getSpawnPoint() {
        return spawnPoint;
    }

    public void initInterfaceBlockAtStart() {
        // block texture
        ItemStack texture = new ItemStack(Material.DROPPER, 1);
        ItemMeta textMeta = texture.getItemMeta();
        textMeta.setCustomModelData(2);
        texture.setItemMeta(textMeta);

        // calculs des locations interface goal
        ArrayList<Location> locationTemp = new ArrayList<>();

        locationTemp.add(new Location(spawnPoint.getWorld(),spawnPoint.getBlockX()+8,spawnPoint.getBlockY(),spawnPoint.getBlockZ()));
        locationTemp.add(new Location(spawnPoint.getWorld(),spawnPoint.getBlockX(),spawnPoint.getBlockY(),spawnPoint.getBlockZ()+8));
        locationTemp.add(new Location(spawnPoint.getWorld(),spawnPoint.getBlockX()+6,spawnPoint.getBlockY(),spawnPoint.getBlockZ()+6));
        locationTemp.add(new Location(spawnPoint.getWorld(),spawnPoint.getBlockX()-6,spawnPoint.getBlockY(),spawnPoint.getBlockZ()+6));
        locationTemp.add(new Location(spawnPoint.getWorld(),spawnPoint.getBlockX()-8,spawnPoint.getBlockY(),spawnPoint.getBlockZ()));

        // placement des block
        for (int i = 0; i < locationTemp.size(); i++) {
            locationTemp.get(i).getBlock().setType(Material.BARRIER);
        }

        // creating/assembling custom block
        for (int i = 0; i < locationTemp.size(); i++) {
            Location loc = locationTemp.get(i).clone().add(0.5,0,0.5);
            ArmorStand armorStand = (ArmorStand) loc.getWorld().spawnEntity(loc,EntityType.ARMOR_STAND);
            armorStand.setPersistent(true);
            armorStand.setVisible(false);
            armorStand.setMarker(true);
            armorStand.addScoreboardTag("INTERFACE_"+this.name+"_"+i);
            armorStand.getEquipment().setHelmet(texture);

            this.interfacesGoals.add(new CustomBlock(locationTemp.get(i),armorStand));
        }

        // interface team

        // block texture
        ItemStack textureTeamInterface = new ItemStack(Material.DROPPER, 1);
        ItemMeta textMetaTeamInterface = textureTeamInterface.getItemMeta();
        textMetaTeamInterface.setCustomModelData(3);
        textureTeamInterface.setItemMeta(textMetaTeamInterface);
        this.interfaceTeam = new Location(spawnPoint.getWorld(),spawnPoint.getBlockX(),spawnPoint.getBlockY(),spawnPoint.getBlockZ()-8);
        this.interfaceTeam.getBlock().setType(Material.BARRIER);

        Location loc = this.interfaceTeam.clone().add(0.5,0,0.5);
        ArmorStand armorStand = (ArmorStand) loc.getWorld().spawnEntity(loc,EntityType.ARMOR_STAND);
        armorStand.setPersistent(true);
        armorStand.setVisible(false);
        armorStand.setMarker(true);
        armorStand.addScoreboardTag("MAIN_INTERFACE_"+this.name);
        armorStand.getEquipment().setHelmet(textureTeamInterface);

        // register for the first time
        this.registerInterfaces();
    }

    public void moveInterfaceGoal(int itemNumber,Location newLocation) {
        // supression last pos
        CustomBlock cBlock = this.interfacesGoals.get(itemNumber);
        Location oldLoc = cBlock.getLocation();
        ArmorStand oldArmorStand = cBlock.getArmorStand();
        if (oldArmorStand != null) oldArmorStand.remove();
        oldLoc.getBlock().setType(Material.AIR);
        if (!this.region.contains(oldLoc)) oldLoc.getWorld().setChunkForceLoaded(oldLoc.getChunk().getX(), oldLoc.getChunk().getZ(), false);

        // nouvelle pos
        newLocation.getBlock().setType(Material.BARRIER);

        Location loc = newLocation.clone().add(0.5,0,0.5);
        loc.getWorld().setChunkForceLoaded(loc.getChunk().getX(),loc.getChunk().getZ(),true);
        // adding custom block style
        ItemStack texture = new ItemStack(Material.DROPPER, 1);
        ItemMeta textMeta = texture.getItemMeta();
        textMeta.setCustomModelData(2);
        texture.setItemMeta(textMeta);
        ArmorStand armorStand = (ArmorStand) loc.getWorld().spawnEntity(loc,EntityType.ARMOR_STAND);
        armorStand.setPersistent(true);
        armorStand.setVisible(false);
        armorStand.setMarker(true);
        armorStand.getEquipment().setHelmet(texture);

        this.interfacesGoals.set(itemNumber,new CustomBlock(newLocation,armorStand));
        UNCSurvival.getInstance().getGameManager().getInterfacesManager().addInterface(this.interfacesGoals.get(itemNumber).getLocation(),new GoalCustomInterface(itemNumber,this));

        LoggerFile.AppendLineToWrite("[TEAM-INTERFACE] LA TEAM "+ this.name +" DEPLACE SON INTERFACE DE " + oldLoc + " A " + loc + " LE " + LocalDateTime.now());
    }

    public void ConsumeAllGoalItems() {
        final ItemsManager itemsManager = UNCSurvival.getInstance().getGameManager().getItemsManager();
        final PhaseEnum phase = UNCSurvival.getInstance().getGameManager().getGameStats().getCurrentPhase();
        ArrayList<CustomBlock> goals = this.interfacesGoals;
        for (int i = 0, goalsSize = goals.size(); i < goalsSize; i++) {
            CustomBlock cBlock = goals.get(i);
            ArrayList<Block> blocksToCheck = new ArrayList<>(Arrays.asList(
                    cBlock.getLocation().clone().add(1, 0, 0).getBlock(),
                    cBlock.getLocation().clone().add(0, 0, 1).getBlock(),
                    cBlock.getLocation().clone().add(-1, 0, 0).getBlock(),
                    cBlock.getLocation().clone().add(0, 0, -1).getBlock()
            ));

            for (Block b : blocksToCheck) {
                if ( b.getType() == Material.CHEST ) {
                    Chest block = (Chest) b.getState();
                    for (ItemStack itemStack : block.getBlockInventory().getContents()) {
                        if (itemStack != null && itemStack.isSimilar(itemsManager.getGoalItem(i))) {

                            this.itemsProduction.set(i, this.itemsProduction.get(i) + itemStack.getAmount());
                            this.addScore(itemsManager.getGoalItemPrice(i, phase) * itemStack.getAmount());
                            block.getBlockInventory().remove(itemStack);
                            // advancement
                            AdvancementManager advancementManager = UNCSurvival.getInstance().getGameManager().getAdvancementManager();
                            Advancement advancement = advancementManager.getAdvancement("precoce");
                            if(!advancement.alreadyGranted()) {
                                advancementManager.grantToATeam(this,advancement);
                            }
                        }
                    }
                }
            }
        }
    }

    public void addScore(int score) {
        this.score += score;
    }

    public boolean hasMember(GamePlayer gamePlayer) {
        for (GamePlayer gamePlayer1 : this.members) {
            if(gamePlayer1.getUUID().equals(gamePlayer.getUUID())) { return true; }
        }
        return false;
    }

    public void quit(GamePlayer gamePlayer) {
        if(hasMember(gamePlayer)) {
            this.members.remove(gamePlayer);
        }
    }

    public void join(GamePlayer gamePlayer) {
        if(!hasMember(gamePlayer)) {
            this.members.add(gamePlayer);
        }
    }

    public int getItemsProduction(int itemNumber) {
        return itemsProduction.get(itemNumber);
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public void setChatColor(ChatColor chatColor) {
        this.chatColor = chatColor;
    }

    public List<GamePlayer> getMembers() {
        return members;
    }

    public int getScore() {
        return this.bonusScore + this.score;
    }

    public void addABonusScore(int score) {
        this.bonusScore += score;
    }

    public void registerInterfaces() {
        for (int i = 0; i < this.interfacesGoals.size(); i++) {
            UNCSurvival.getInstance().getGameManager().getInterfacesManager().addInterface(this.interfacesGoals.get(i).getLocation(),new GoalCustomInterface(i,this));
        }
        UNCSurvival.getInstance().getGameManager().getInterfacesManager().addInterface(this.interfaceTeam,new TeamCustomInterface(this));
    }

    public void postLoad() {
        registerInterfaces();
        this.interfacesGoals.forEach(customBlock -> {
            Location loc = customBlock.getLocation().clone().add(0.5,0,0.5);

            // adding custom block style
            ItemStack texture = new ItemStack(Material.DROPPER, 1);
            ItemMeta textMeta = texture.getItemMeta();
            textMeta.setCustomModelData(2);
            texture.setItemMeta(textMeta);
            ArmorStand armorStand = (ArmorStand) loc.getWorld().spawnEntity(loc,EntityType.ARMOR_STAND);
            armorStand.setPersistent(true);
            armorStand.setVisible(false);
            armorStand.setMarker(true);
            armorStand.getEquipment().setHelmet(texture);

            customBlock.setArmorStand(armorStand);
        });
    }

    public void onDisable() {
        this.interfacesGoals.forEach(customBlock -> {
            customBlock.getArmorStand().remove();
            customBlock.setArmorStand(null);
        });
    }

    public Region getRegion() {
        return region;
    }

    public void resetScore() {
        this.itemsProduction = new ArrayList<>(Arrays.asList(0,0,0,0,0));
        this.bonusScore = 0;
        this.score = 0;
    }

    public int getRange() {
        return range;
    }

    public void addRange(int rangeAdded) {
        this.range += rangeAdded;
        this.region.addRange(spawnPoint.getWorld(),rangeAdded);

        // advancement
        AdvancementManager advancementManager = UNCSurvival.getInstance().getGameManager().getAdvancementManager();
        Advancement advancement = advancementManager.getAdvancement("extension_de_territoire");
        if(this.range >= 105
            && !advancement.alreadyGranted()) {
            advancementManager.grantToATeam(this,advancement);
        }

        LoggerFile.AppendLineToWrite("[TEAM-MODULE] LA TEAM "+ this.name +" AUGMENTE SA PROTECTION A " + this.range + " LE " + LocalDateTime.now());
    }

    public Location getInterfaceTeam() {
        return interfaceTeam;
    }

    public boolean isFamined() {
        return this.isFamined;
    }

    public void setFamined(boolean famined) {
        isFamined = famined;
    }

    public String getStats(Player player) {
        String spacer = "§6-------------------------\n";
        StringBuilder statsBuild = new StringBuilder();
        statsBuild.append("§8--------------| §b§lStatistiques §8|---------------\n \n");
        statsBuild.append("§8----[§6Goal items§8]----\n");
        for (int i = 0; i < this.itemsProduction.size()-1; i++) {
            statsBuild
                    .append(UNCSurvival.getInstance().getGameManager().getItemsManager().getGoalItemName(i))
                    .append(": §6")
                    .append(this.itemsProduction.get(i))
                    .append("\n");
        }
        // last ItemPhase show
        statsBuild
                .append("Items de phase")
                .append(": §6")
                .append(this.itemsProduction.get(this.itemsProduction.size()-1))
                .append("\n");

        statsBuild.append("§8-----[§2Team§8]-----\n");
        statsBuild
                .append("§aTaille de la base : §6" + this.range*2 + "x" + this.range*2 + "\n")
                .append("§aMorts : §6" + this.getTotalDeaths() + "\n")
                .append("§aJoueurs tués : §6" + this.getTotalPlayerKilled() + "\n")
                .append("§aMobs tués : §6" + this.getTotalMobKilled() + "\n")
                .append("§aScore : §6" + this.getScore() + "\n")
                .append("§aScore Bonus compris : §6" + this.bonusScore + "\n");
        if (player != null)
            statsBuild.append("§8-----[§3Joueur§8]-----\n")
                .append("§bTemps de connexion : §6" + getTimePlayed(player) + "\n")
                .append("§bMorts : §6" + UNCSurvival.getInstance().getGameManager().getScoreboardManager().getDeathStats(player) + "\n")
                .append("§bJoueurs tués : §6" + UNCSurvival.getInstance().getGameManager().getScoreboardManager().getPlayerKill(player) + "\n")
                .append("§bDuels gagnés : §6" + UNCSurvival.getInstance().getGameManager().getParticipantManager().getGamePlayer(player.getName()).getDuelsWon() + "\n")
                .append("§bMobs tués : §6" + UNCSurvival.getInstance().getGameManager().getScoreboardManager().getMobKill(player) + "\n");

        return statsBuild.toString();
    }

    public int getClassement() {
        return UNCSurvival.getInstance().getGameManager().getTeamsManager().getClassement().indexOf(this)+1;
    }

    public int getTotalDeaths() {
        int total = 0;
        for(GamePlayer player : this.getMembers()) {
            total += UNCSurvival.getInstance().getGameManager().getScoreboardManager().getDeathStats(player.getOfflinePlayer());
        }
        return total;
    }

    public int getTotalPlayerKilled() {
        int total = 0;
        for(GamePlayer player : this.getMembers()) {
            total += UNCSurvival.getInstance().getGameManager().getScoreboardManager().getPlayerKill(player.getOfflinePlayer());
        }
        return total;
    }

    public int getTotalMobKilled() {
        int total = 0;
        for(GamePlayer player : this.getMembers()) {
            total += UNCSurvival.getInstance().getGameManager().getScoreboardManager().getMobKill(player.getOfflinePlayer());
        }
        return total;
    }

    public int getTotalStoneMined() {
        int total = 0;
        for(GamePlayer player : this.getMembers()) {
            total += UNCSurvival.getInstance().getGameManager().getScoreboardManager().getStoneMined(player.getOfflinePlayer());
        }
        return total;
    }

    public String getTimePlayed(Player player) {
        int total = UNCSurvival.getInstance().getGameManager().getScoreboardManager().getTimePlayed(player)/20;
        int secondes = total % 60;
        int minutes = (total / 60) % 60;
        int heures = (total / 3600) % 24;
        int jours = (total / 3600) / 24;
        return jours + " jour(s), " + heures + " heures, " + minutes + " m, " + secondes + " s";
    }
}
