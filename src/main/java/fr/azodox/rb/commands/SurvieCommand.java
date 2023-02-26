package fr.azodox.rb.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import fr.azodox.rb.RBSurvie;
import org.bukkit.entity.Player;

@CommandAlias("survie")
public class SurvieCommand extends BaseCommand {

    private final RBSurvie plugin;

    public SurvieCommand(RBSurvie plugin) {
        this.plugin = plugin;
    }

    @Subcommand("reload")
    @CommandPermission("survie.admin.reload")
    public void onReload(Player player){
        plugin.reloadConfig();
        player.sendMessage("§e§lRBSurvie >> §7Configuration rechargée.");
    }
}
