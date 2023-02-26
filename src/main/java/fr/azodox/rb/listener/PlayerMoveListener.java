package fr.azodox.rb.listener;

import fr.azodox.rb.RBSurvie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    private final RBSurvie plugin;

    public PlayerMoveListener(RBSurvie plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        var player = event.getPlayer();
        if (plugin.getTeleportationManager().getTeleporting().containsKey(player)) {
            plugin.getServer().getScheduler().cancelTask(plugin.getTeleportationManager().getTeleporting().get(player));
            plugin.getTeleportationManager().getTeleporting().remove(player);
            player.sendMessage("§cTéléportation annulée.");
        }
    }
}
