package fr.azodox.rb.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import fr.azodox.rb.RBSurvie;
import fr.azodox.rb.home.HomeListView;
import me.saiintbrisson.minecraft.ViewFrame;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("homes")
public class HomesCommand extends BaseCommand {

    private final RBSurvie plugin;

    public HomesCommand(RBSurvie plugin) {
        this.plugin = plugin;
    }

    @Default
    public void onHomes(Player player){
        var homes = plugin.getHomeManager().getHomes(player.getUniqueId());
        if (homes.isEmpty()) {
            player.sendMessage("§e§lRBSurvie >> §7Vous n'avez pas de home.");
        }else{
            var frame = ViewFrame.of(plugin, new HomeListView("Vos homes", homes)).register();
            frame.open(HomeListView.class, player);
        }
    }

    @CatchUnknown
    @Syntax("<target>")
    @CommandCompletion("@players")
    public void onHomesOther(Player player, String target){
        var homes = plugin.getHomeManager().getHomes(Bukkit.getOfflinePlayer(target).getUniqueId());
        if (homes.isEmpty()) {
            player.sendMessage("§e§lRBSurvie >> §7Ce joueur n'a pas de home.");
        }else{
            var frame = ViewFrame.of(plugin, new HomeListView("Homes de " + target, homes)).register();
            frame.open(HomeListView.class, player);
        }
    }
}
