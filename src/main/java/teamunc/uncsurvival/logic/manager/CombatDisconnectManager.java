package teamunc.uncsurvival.logic.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import teamunc.uncsurvival.UNCSurvival;

import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.HashMap;
import java.util.UUID;

public class CombatDisconnectManager extends AbstractManager {
    private HashMap<UUID, LocalDateTime> lastHit = new HashMap<UUID, LocalDateTime>();
    private HashMap<UUID, UUID> combatWithWho = new HashMap<>();
    private HashMap<UUID, BukkitTask> runTaskLater = new HashMap<>();

    public CombatDisconnectManager(UNCSurvival plugin) {
        super(plugin);
    }

    public void engageCombat(Player player, Player target) {
        // Si le combat est n'est pas encore engagé
        if(!lastHit.containsKey(player.getUniqueId())) {
            player.sendMessage("§f§l[UNC]§6 §6Attention tu rentres en combat ! §7Tu ne peux pas te déconnecter");
        }
        this.lastHit.put(player.getUniqueId(), LocalDateTime.now());
        this.combatWithWho.put(player.getUniqueId(), target.getUniqueId());
        BukkitTask task = Bukkit.getScheduler().runTaskLater(this.plugin, new Runnable() {
            @Override
            public void run() {
                if(!isPlayerStillInCombat(LocalDateTime.now(), player)
                        && lastHit.containsKey(player.getUniqueId())
                        && lastHit.get(player.getUniqueId()).plusSeconds(14).isBefore(LocalDateTime.now())) {
                    player.sendMessage("§f§l[UNC]§6 Tu n'es plus en combat");
                    lastHit.remove(player.getUniqueId());
                    combatWithWho.remove(player.getUniqueId());
                    runTaskLater.remove(player.getUniqueId());
                }
            }
        }, 300);
        if(this.runTaskLater.containsKey(player.getUniqueId())) {
            this.runTaskLater.get(player.getUniqueId()).cancel();
            this.runTaskLater.put(player.getUniqueId(), task);
        }
    }

    public Boolean isPlayerStillInCombat(LocalDateTime disconnectTime, Player player) {
        if(lastHit.containsKey(player.getUniqueId())) {
            LocalDateTime playerLastHit = lastHit.get(player.getUniqueId());
            // le délais des 20 secondes
            if(playerLastHit.plusSeconds(15).isBefore(disconnectTime)) {
                return true;
            }
        }
        return false;
    }

    public void triggerDisconnectCombat(Player player) {
        Player targetPlayer = Bukkit.getPlayer(this.combatWithWho.get(player.getUniqueId()));
        if(targetPlayer != null) {
            String line = "DISCONNECT AT " + LocalDateTime.now() + " ";
            line += player.getDisplayName() + "(disconnected) vs " + targetPlayer.getDisplayName();
            this.plugin.getFileManager().writeCombatLogFile(line);
        } else {
            Bukkit.getConsoleSender().sendMessage("TargetPlayer null");
        }
    }
}
