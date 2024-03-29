package teamunc.uncsurvival.logic.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import teamunc.uncsurvival.UNCSurvival;
import teamunc.uncsurvival.logic.player.GamePlayer;
import teamunc.uncsurvival.logic.team.Team;

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
    private void addPlayer(Team team, GamePlayer player) {
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

            // applique if game started already
            if (this.plugin.getGameManager().getGameStats().isGameStarted())
                this.plugin.getGameManager().appliqueStartConstraints(player);
        } else {
            throw new Exception("Le joueur n'existe pas");
        }
    }

    /**
     * Retire un joueur du jeu
     * @param playerName est un nom de joueur co
     * @return
     */

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

    public Team getTeamForPlayer(String playerName) {
        return this.getPlayersByTeam().get(this.getGamePlayer(playerName));
    }

    public boolean isPlaying(Player player) {
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
    public List<Player> getOnlinePlayers() {
        return this.getGamePlayers().stream()
                .filter(gamePlayer -> gamePlayer.isOnline())
                .map(gamePlayer -> gamePlayer.getBukkitPlayer())
                .collect(Collectors.toList());
    }

    /**
     * Renvoi des gamePlayer en ligne aleatoirement de differentes team
     * @return
     */
    public ArrayList<GamePlayer> getRandomOnlineGamePlayerFromDiffTeams(int numberRequired) {
        ArrayList<GamePlayer> res = null;
        ArrayList<Team> teams = new ArrayList<>(this.plugin.getGameManager().getTeamsManager().getAllTeams());

        if (numberRequired <= teams.size()) {
            res = new ArrayList<>();
            Collections.shuffle(teams);
            for (Team t : teams) {
                ArrayList<GamePlayer> members = getOnlineGamePlayerForGivenList(new ArrayList<>(t.getMembers()));
                if (members.size() > 0 && res.size() < numberRequired) {
                    Collections.shuffle(members);
                    res.add(members.get(0));
                }
            }
            if (res.size() != numberRequired) return null;
        }

        return res;
    }


    public ArrayList<GamePlayer> getOnlineGamePlayerForGivenList(ArrayList<GamePlayer> gps) {
        ArrayList<GamePlayer> res = new ArrayList<>();
        for (GamePlayer gp : gps) {
            if (gp.isOnline()) res.add(gp);
        }
        return res;
    }
}
