package fr.azodox.rb;

import co.aikar.commands.PaperCommandManager;
import fr.azodox.rb.commands.*;
import fr.azodox.rb.home.Home;
import fr.azodox.rb.home.HomeManager;
import fr.azodox.rb.listener.PlayerMoveListener;
import fr.azodox.rb.teleport.TeleportationManager;
import fr.azodox.rb.util.HeadUtil;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class RBSurvie extends JavaPlugin {

    private final @Getter TeleportationManager teleportationManager = new TeleportationManager(this);
    private final @Getter HomeManager homeManager = new HomeManager(this);
    private final HeadUtil headUtil = new HeadUtil();

    @Override
    public void onEnable() {
        saveDefaultConfig();

        registerHeads();

        PaperCommandManager manager = new PaperCommandManager(this);
        manager.getCommandCompletions().registerCompletion("players", c -> getServer().getOnlinePlayers().stream().map(Player::getName).toList());
        manager.getCommandCompletions().registerCompletion("homes", completion -> homeManager.getHomes(completion.getPlayer().getUniqueId()).stream().map(Home::getName).toList());
        manager.registerCommand(new HomeCommand(this));
        manager.registerCommand(new SetHomeCommand(this));
        manager.registerCommand(new DelHomeCommand(this));
        manager.registerCommand(new TeleportAskCommand(this));
        manager.registerCommand(new SurvieCommand(this));
        manager.registerCommand(new HomesCommand(this));

        getServer().getPluginManager().registerEvents(new PlayerMoveListener(this), this);
        getLogger().info("Enabled!");
    }

    private void registerHeads() {
        headUtil.addHead("97f82aceb98fe069e8c166ced00242a76660bbe07091c92cdde54c6ed10dcff9", "house_oak");
        headUtil.addHead("e19997593f2c592b9fbd4f15ead1673b76f519d7ab3efa15edd19448d1a20bfc", "house_birch");
        headUtil.addHead("441215d106cd5adedae717364f643c619a0c8826f2c6ac4e97ee8c944540b90e", "house_spruce");
        headUtil.addHead("2a648e357a60c257f3bf3840fa532cfd6adce2f88dab9fd0c0f9303cf5aeb889", "house_dark_oak");
        headUtil.addHead("95db849a15ef01a03213d36c7c62e07b88c277dba869317b7e27571fddb293d4", "house_jungle");
        headUtil.addHead("19e316ba48e51c6eb73cb068a2d468f678eed82d4529d4b12eec8bfa7819aa86", "house_acacia");
        headUtil.addHead("9796de601b51b98cea5b899944b1a487184840174d848f8c57fe0b44c849ce40", "next_page");
        headUtil.addHead("4fe668e0d3141978b8ae27bf211b01b44f106b9d647417b8520a0adbcf2ef35f", "previous_page");
    }
}
