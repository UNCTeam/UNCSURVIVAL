package teamunc.uncsurvival.logic.manager;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.player.GamePlayer;
import teamunc.uncsurvival.logic.team.Team;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class ParticipantManager extends AbstractManager{

    private HashMap<GamePlayer, Team> playersByTeam = new HashMap<>();

    public ParticipantManager(UNCSurvival plugin) {
        super(plugin);
    }

    /**
     * Ajoute un joueur à une team
     *
     * @param team
     * @param player
     */
    public void addPlayer(Team team, GamePlayer player) {
            Team team1 = this.playersByTeam.get(player);
        if(team1 != null) {
            team1.quit(player);
        }
        this.playersByTeam.put(player, team);
        team.join(player);
    }

    /**
     * Ajoute un joueur via son pseudo à une team
     *
     * @param team
     * @param playerName
     */
    public void addPlayer(Team team, String playerName) throws Exception {
        Player player = Bukkit.getPlayer(playerName);
        if(player != null) {
            this.addPlayer(team, new GamePlayer(player));
        } else {
            throw new Exception("Le joueur n'existe pas");
        }
    }

    /**
     * Retire un joueur du jeu
     * @param team Prends la team en param
     * @param player et un gameplayer
     * @return
     */
    public boolean removePlayer(Team team, GamePlayer player) {
        if(team.hasMember(player)) {
            team.quit(player);
            this.playersByTeam.remove(player);
            return true;
        } else {
            return false;
        }
    }

    public boolean removePlayer(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if(player != null) {
            return false;
        }

        return this.removePlayer(player);
    }

    /**
     * Retire un joueur du jeu
     * @param player Prends un Gameplayer en paramètre
     * @return
     */
    public boolean removePlayer(GamePlayer player) {
        if(this.playersByTeam.get(player) != null && this.playersByTeam.get(player).hasMember(player)) {
            this.playersByTeam.get(player).quit(player);
            this.playersByTeam.remove(player);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retire un joueur via un Player
     * @param player
     * @return
     */
    public boolean removePlayer(Player player) {
        GamePlayer gamePlayer = this.getGamePlayerByUUID(player.getUniqueId());
        if(gamePlayer != null) {
            this.removePlayer(gamePlayer);
            return true;
        }
        return false;
    }

    /**
     * Récupère un joueur par son pseudo
     * @param name
     * @return
     */
    public GamePlayer getGamePlayer(String name) {
        for (Map.Entry<GamePlayer, Team> entry : playersByTeam.entrySet()) {
            GamePlayer player = entry.getKey();
            if(player.getBukkitPlayer().getName().equals(name)) {
                return player;
            }
        }
        return null;
    }

    public GamePlayer getGamePlayerByUUID(UUID uuid) {
        for (Map.Entry<GamePlayer, Team> entry : playersByTeam.entrySet()) {
            GamePlayer player = entry.getKey();
            if(player.getBukkitPlayer().getUniqueId().equals(uuid)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Renvoi la liste des joueurs sous forme de liste GamePlayer
     * @return
     */
    public ArrayList<GamePlayer> getGamePlayers() {
        ArrayList<GamePlayer> players = new ArrayList<>();
        for (Map.Entry<GamePlayer, Team> entry : playersByTeam.entrySet()) {
            players.add(entry.getKey());
        }
        return players;
    }

    public HashMap<GamePlayer, Team> getPlayersByTeam() {
        return playersByTeam;
    }

    public void saveParticipants() {
        this.plugin.getFileManager().saveParticipants(this.playersByTeam);
    }

    /**
     * Renvoi la liste des joueurs sous forme de liste Player bukkit
     * @return
     */
    public List<Player> getBukkitPlayers() {
        return this.getGamePlayers().stream()
                .map(gamePlayer -> gamePlayer.getBukkitPlayer())
                .collect(Collectors.toList());
    }

    public void loadParticipants() {
        HashMap<GamePlayer, Team> players = this.plugin.getFileManager().loadParticipants();
        if(players != null) {
            this.playersByTeam = players;
        }
    }
}
