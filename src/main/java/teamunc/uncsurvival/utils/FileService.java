package teamunc.uncsurvival.utils;

import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import teamunc.uncsurvival.logic.team.TeamList;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * S'occupe d'importer, de serializer et de sauvegarder tout données nécessaires
 */
public class FileService {
    private static String teamList_path = "UNCSurvival.teams";
    private static String GameConfiguration_path = "UNCSurvival.game_config";


    public static boolean saveTeams( TeamList teamList) {
        try {
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(new GZIPOutputStream(new FileOutputStream(teamList_path)));
            out.writeObject(teamList);
            out.close();
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public static TeamList loadTeams() {
        try {
            BukkitObjectInputStream in = new BukkitObjectInputStream(new GZIPInputStream(new FileInputStream(teamList_path)));
            TeamList teamList = (TeamList) in.readObject();
            in.close();
            return teamList;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }
}
