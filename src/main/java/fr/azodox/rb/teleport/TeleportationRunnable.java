package fr.azodox.rb.teleport;

import fr.azodox.rb.RBSurvie;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class TeleportationRunnable implements Runnable {

    private final RBSurvie plugin;
    private final Player player;
    private final Location to;

    private int timer;

    public TeleportationRunnable(RBSurvie plugin, Player player, Location to) {
        this.plugin = plugin;
        this.player = player;
        this.to = to;

        this.timer = plugin.getConfig().getInt("teleportation.delay");
    }

    @Override
    public void run() {
        if(!player.hasPermission("rb.tp.bypass.delay")){
            if(timer > 0){
                plugin.adventure().player(player).sendActionBar(Component.text("Téléportation dans " + timer + " seconde(s)").color(NamedTextColor.GRAY));
                player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 1f);
                timer--;
                return;
            }
        }
        player.teleport(to);
        plugin.adventure().player(player).sendActionBar(Component.text("Swoooosh!").color(NamedTextColor.GRAY));
        player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 0.5f, 1f);
        plugin.getTeleportationManager().getTeleporting().remove(player);
        plugin.getTeleportationManager().getTeleportationRequests().invalidate(player);
    }
}
