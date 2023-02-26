package fr.azodox.rb.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import fr.azodox.rb.RBSurvie;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("home")
public class HomeCommand extends BaseCommand {

    private final RBSurvie plugin;

    public HomeCommand(RBSurvie plugin) {
        this.plugin = plugin;
    }

    @Default
    @Syntax("<homeName>")
    @CommandCompletion("@homes")
    public void onHome(Player player, String homeName){
        plugin.getHomeManager().getHome(player.getUniqueId(), homeName).ifPresent(h -> {
            player.teleport(h.getLocation());
            player.sendMessage("§e§lRBSurvie >> §7Téléporté à " + homeName + ".");
        });
    }

    @CatchUnknown
    @Syntax("[player]:<homeName>")
    @CommandPermission("rb.home.see.other")
    public void onTargetHome(Player player, @Split(":") String[] homeName){
        var target = Bukkit.getOfflinePlayer(homeName[0]).getUniqueId();
        var home = homeName[1];
        plugin.getHomeManager().getHome(target, home).ifPresent(h -> {
            player.teleport(h.getLocation());
            player.sendMessage("§e§lRBSurvie >> §7Téléporté à " + homeName[0] + "/" + home + ".");
        });
    }

    @HelpCommand
    public void onHelp(Player player){
        player.sendMessage("§c/home [player]:<homeName>");
    }
}
