package teamunc.uncsurvival.logic.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import teamunc.uncsurvival.UNCSurvival;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

public class CombatDisconnectManager extends AbstractManager {
    private HashMap<UUID, LocalDateTime> lastHit = new HashMap<UUID, LocalDateTime>();
    private HashMap<UUID, UUID> combatWithWho = new HashMap<>();
    private HashMap<UUID, Integer> runTaskLater = new HashMap<>();

    public CombatDisconnectManager(UNCSurvival plugin) {
        super(plugin);
    }

    public void engageCombat(Player player, Player target) {
        // Si le combat est n'est pas encore engagé
        if(!(lastHit.containsKey(player.getUniqueId())
                && lastHit.get(player.getUniqueId()).plusSeconds(20l).isBefore(LocalDateTime.now()))) {
            player.sendMessage("§7§l[ UNC ]§6Attention tu rentres en combat ! §7Il te reste 15 secondes");
        }
        this.lastHit.put(player.getUniqueId(), LocalDateTime.now());
        this.combatWithWho.put(player.getUniqueId(), target.getUniqueId());
        Integer taskId = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if(!isPlayerStillInCombat(LocalDateTime.now(), player)) {

                    player.sendMessage("§7§l[ UNC ]§6Tu n'es plus en combat");
                }
            }
        }.runTaskLater(UNCSurvival.getInstance(), 400).getTaskId();
        Integer lastTaskId = this.runTaskLater.get(player.getUniqueId());
        if(lastTaskId != null) {
            Bukkit.getServer().getScheduler().cancelTask(lastTaskId);
            this.runTaskLater.put(player.getUniqueId(), taskId);
        }
    }

    public Boolean isPlayerStillInCombat(LocalDateTime disconnectTime, Player player) {
        if(lastHit.containsKey(player.getUniqueId())) {
            LocalDateTime playerLastHit = lastHit.get(player.getUniqueId());
            // le délais des 20 secondes
            if(playerLastHit.plusSeconds(20l).isBefore(disconnectTime)) {
                return true;
            }
        }
        return false;
    }

    public void triggerDisconnectCombat(Player player) {
        Player targetPlayer = Bukkit.getPlayer(this.combatWithWho.get(player.getUniqueId()));
        String line = "DISCONNECT AT " + LocalDateTime.now() + " ";
        line += player.getDisplayName() + "(disconnected) vs " + targetPlayer.getDisplayName();
        this.plugin.getFileManager().writeCombatLogFile(line);
    }
}
