package fr.azodox.rb.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.Syntax;
import fr.azodox.rb.RBSurvie;
import fr.azodox.rb.home.Home;
import fr.azodox.rb.home.OverrideConfirmationView;
import me.saiintbrisson.minecraft.ViewFrame;
import org.bukkit.entity.Player;

@CommandAlias("sethome")
public class SetHomeCommand extends BaseCommand {

    private final RBSurvie plugin;

    public SetHomeCommand(RBSurvie plugin) {
        this.plugin = plugin;
    }

    @CatchUnknown
    @Syntax("<homeName>")
    @CommandCompletion("@homes")
    public void setHome(Player player, String homeName){
        if(!plugin.getHomeManager().hasHome(player.getUniqueId(), homeName)){
            plugin.getHomeManager().addHome(player.getUniqueId(),
                    new Home(homeName, player.getLocation(), player.getUniqueId()));
            player.sendMessage("§e§lRBSurvie >> §7Home créé avec succès.");
        }else{
            var frame = ViewFrame.of(plugin, new OverrideConfirmationView(plugin.getHomeManager(), homeName, player)).register();
            frame.open(OverrideConfirmationView.class, player);
        }
    }
}
