package fr.azodox.rb.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import fr.azodox.rb.RBSurvie;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("tpa")
public class TeleportAskCommand extends BaseCommand {

    private final RBSurvie plugin;

    public TeleportAskCommand(RBSurvie plugin) {
        this.plugin = plugin;
    }

    @CatchUnknown
    @CommandPermission("rb.tp.ask")
    @CommandCompletion("@players")
    @Syntax("<player>")
    public void onTpa(Player player, String target){
        plugin.getTeleportationManager().requestTeleportation(player, Bukkit.getPlayerExact(target));
    }

    @Subcommand("accept")
    @Syntax("<player>")
    @CommandPermission("rb.tpa.accept")
    public void onTpaAccept(Player player, String sender){
        plugin.getTeleportationManager().acceptRequest(player, Bukkit.getPlayerExact(sender));
    }

    @Subcommand("deny")
    @Syntax("<player>")
    @CommandPermission("rb.tpa.deny")
    public void onTpaDeny(Player player, String sender){
        plugin.getTeleportationManager().denyRequest(player, Bukkit.getPlayerExact(sender));
    }

    @Subcommand("cancel")
    @Syntax("<player>")
    @CommandPermission("rb.tpa.cancel")
    public void onTpaCancel(Player player, @Optional String sender){
        plugin.getTeleportationManager().cancelRequest(player, sender != null ? Bukkit.getPlayerExact(sender) : null);
    }

    @HelpCommand
    public void onHelp(Player player){
        player.sendMessage("§6§lRBHome §7§l> §e/tpa <player>");
        player.sendMessage("§6§lRBHome §7§l> §e/tpa accept <player>");
        player.sendMessage("§6§lRBHome §7§l> §e/tpa deny <player>");
        player.sendMessage("§6§lRBHome §7§l> §e/tpa cancel <player>");
    }
}
