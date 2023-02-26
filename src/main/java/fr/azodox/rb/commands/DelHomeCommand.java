package fr.azodox.rb.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import fr.azodox.rb.RBSurvie;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("delhome")
public class DelHomeCommand extends BaseCommand {

    private final RBSurvie plugin;

    public DelHomeCommand(RBSurvie plugin) {
        this.plugin = plugin;
    }

    @Default
    @CommandCompletion("@homes")
    @Syntax("<homeName>")
    public void onDelHome(Player player, String homeName){
        var optionalHome = plugin.getHomeManager().getHome(player.getUniqueId(), homeName);
        if (optionalHome.isPresent()) {
            plugin.getHomeManager().removeHome(player.getUniqueId(), optionalHome.get());
            player.sendMessage("§e§lRBSurvie >> §7Home supprimé avec succès.");
        }else{
            player.sendMessage("§e§lRBSurvie >> §7Home " + homeName + " non trouvé.");
        }
    }

    @CatchUnknown
    @Syntax("<player>:<homeName>")
    @CommandCompletion("@players")
    @CommandPermission("rb.delhome.other")
    public void onDelHomeOther(Player player, String playerName, String homeName){
        var uuid = Bukkit.getOfflinePlayer(playerName).getUniqueId();
        var optionalHome = plugin.getHomeManager().getHome(uuid, homeName);
        if (optionalHome.isPresent()) {
            plugin.getHomeManager().removeHome(uuid, optionalHome.get());
            player.sendMessage("§e§lRBSurvie >> §7Home supprimé avec succès.");
        }else{
            player.sendMessage("§e§lRBSurvie >> §7Home " + homeName + " non trouvé.");
        }
    }
}
