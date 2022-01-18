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

    public ParticipantManager(UNCSurvival plugin) {
        super(plugin);
    }

    public HashMap<GamePlayer, Team> getPlayersByTeam() {
        HashMap<GamePlayer, Team> players = new HashMap<>();
        for(Team team : this.plugin.getGameManager().getTeamsManager().getAllTeams()) {
            for (GamePlayer player : team.getMembers()) {
                players.put(player, team);
            }
        }
        return players;
    }

    /**
     * Ajoute un joueur à une team
     *
     * @param team
     * @param player
     */
    public void addPlayer(Team team, GamePlayer player) {
        Team team1 = this.getPlayersByTeam().get(player);
        if(team1 != null) {
            team1.quit(player);
        }
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
            return true;
        } else {
            return false;
        }
    }

    public boolean removePlayer(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if(player == null) {
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
        if(this.getPlayersByTeam().get(player) != null && this.getPlayersByTeam().get(player).hasMember(player)) {
            this.getPlayersByTeam().get(player).quit(player);
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
        GamePlayer gamePlayer = new GamePlayer(player);
        return this.removePlayer(gamePlayer);
    }

    /**
     * Récupère un joueur par son pseudo
     * @param name
     * @return
     */
    public GamePlayer getGamePlayer(String name) {
        for (Map.Entry<GamePlayer, Team> entry : getPlayersByTeam().entrySet()) {
            GamePlayer player = entry.getKey();
            if(player.getOfflinePlayer().getName().equals(name)) {
                return player;
            }
        }
        return null;
    }

    public Team getTeamForPlayer(Player player) {
        return this.getPlayersByTeam().get(new GamePlayer(player));
    }

    public boolean hasPlayer(Player player) {
        return this.getPlayersByTeam().containsKey(new GamePlayer(player));
    }

    /**
     * Renvoi la liste des joueurs sous forme de liste GamePlayer
     * @return
     */
    public ArrayList<GamePlayer> getGamePlayers() {
        ArrayList<GamePlayer> players = new ArrayList<>();
        for (Map.Entry<GamePlayer, Team> entry : getPlayersByTeam().entrySet()) {
            players.add(entry.getKey());
        }
        return players;
    }

    /**
     * Renvoi la liste des joueurs sous forme de liste Player bukkit
     * @return
     */
    public List<Player> getOnelinePlayers() {
        return this.getGamePlayers().stream()
                .filter(gamePlayer -> gamePlayer.isOneline())
                .map(gamePlayer -> gamePlayer.getBukkitPlayer())
                .collect(Collectors.toList());
    }
}
