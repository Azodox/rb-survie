package fr.azodox.rb.teleport;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import fr.azodox.rb.RBSurvie;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TeleportationManager {

    private final @Getter Cache<Player, Player> teleportationRequests;
    private final @Getter Map<Player, Integer> teleporting = Collections.synchronizedMap(new HashMap<>());
    private final RBSurvie plugin;

    public TeleportationManager(RBSurvie plugin) {
        this.plugin = plugin;
        this.teleportationRequests = CacheBuilder.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(1))
                .build();
    }

    public void requestTeleportation(Player player, Player target) {
        if(teleportationRequests.asMap().containsKey(player)){
            player.sendMessage("§cVous avez déjà une demande en attente.");
            return;
        }

        player.sendMessage("§7Vous avez demandé à " + target.getName() + " de vous téléporter.");
        plugin.adventure().player(target).sendMessage(Component.text("Vous avez reçu une demande de téléportation de " + player.getName() + ".")
                .color(NamedTextColor.GRAY)
                .append(Component.space())
                .append(Component.text("[ACCEPTER]")
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa accept " + player.getName()))
                        .color(NamedTextColor.GREEN))
                .append(Component.space())
                .append(Component.text("[REFUSER]")
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/tpa deny " + player.getName()))
                        .color(NamedTextColor.RED)));
        teleportationRequests.put(player, target);
    }

    /**
     * Accept a teleportation request.
     * @param player : The player who accepts the request.
     * @param sender : The player who sent the request.
     */
    public void acceptRequest(Player player, Player sender){
        if(teleportationRequests.getIfPresent(sender) == null){
            plugin.adventure().player(player).sendMessage(Component.text("Vous n'avez pas de demande de téléportation en attente.").color(NamedTextColor.RED));
            return;
        }

        Player target = teleportationRequests.getIfPresent(sender);
        if(target.getUniqueId().equals(player.getUniqueId())){
            if(!sender.isOnline()){
                plugin.adventure().player(player).sendMessage(Component.text("Le joueur n'est plus connecté.").color(NamedTextColor.RED));
                return;
            }

            plugin.adventure().player(sender).sendMessage(Component.text(player.getName() + " a accepté votre demande de téléportation.").color(NamedTextColor.GRAY));
            plugin.adventure().player(sender).sendMessage(Component.text("\u26A0 Ne bougez pas.").color(NamedTextColor.RED));
            var task = plugin.getServer().getScheduler().runTaskTimer(plugin, new TeleportationRunnable(plugin, sender, player.getLocation()), 0, 20);

            if(sender.hasPermission("rb.tp.bypass.delay")){
                plugin.getServer().getScheduler().runTask(plugin, task::cancel);
            }else{
                plugin.getServer().getScheduler().runTaskLater(plugin, task::cancel, 20L * plugin.getConfig().getInt("teleportation.delay") + 1);
            }

            teleporting.put(sender, task.getTaskId());
            teleportationRequests.invalidate(sender);
        }else{
            plugin.adventure().player(player).sendMessage(Component.text("Vous n'avez pas de demande de téléportation en attente.").color(NamedTextColor.RED));
        }
    }

    public void denyRequest(Player player, Player sender){
        if(teleportationRequests.getIfPresent(sender) == null){
            plugin.adventure().player(player).sendMessage(Component.text("Vous n'avez pas de demande de téléportation en attente.").color(NamedTextColor.RED));
            return;
        }

        Player target = teleportationRequests.getIfPresent(sender);
        if(target.getUniqueId().equals(player.getUniqueId())){
            if(!sender.isOnline()){
                plugin.adventure().player(player).sendMessage(Component.text("Le joueur n'est plus connecté.").color(NamedTextColor.RED));
                return;
            }

            plugin.adventure().player(sender).sendMessage(Component.text(player.getName() + " a refusé votre demande de téléportation.").color(NamedTextColor.GRAY));
            teleportationRequests.invalidate(sender);
        }else{
            plugin.adventure().player(player).sendMessage(Component.text("Vous n'avez pas de demande de téléportation en attente.").color(NamedTextColor.RED));
        }
    }

    public void cancelRequest(Player player, Player target){
        if(teleportationRequests.getIfPresent(player) == null){
            plugin.adventure().player(player).sendMessage(Component.text("Vous n'avez pas de demande de téléportation en attente.").color(NamedTextColor.RED));
            return;
        }

        if(teleportationRequests.getIfPresent(player).getUniqueId().equals(target.getUniqueId())){
            if(teleporting.containsKey(player)){
                plugin.adventure().player(player).sendMessage(Component.text("Vous ne pouvez pas annulé une demande pendant que vous vous téléportez.").color(NamedTextColor.RED));
                return;
            }

            teleportationRequests.invalidate(player);
            plugin.getServer().getScheduler().getPendingTasks().stream().filter(bukkitTask -> bukkitTask.getTaskId() == teleporting.get(player)).findFirst().ifPresent(BukkitTask::cancel);
            teleporting.remove(player);
            plugin.adventure().player(player).sendMessage(Component.text("Vous avez annulé votre demande de téléportation.").color(NamedTextColor.GRAY));
        }else{
            plugin.adventure().player(player).sendMessage(Component.text("Vous n'avez pas de demande de téléportation en attente.").color(NamedTextColor.RED));
        }
    }

}
