package fr.azodox.rb.home;

import fr.azodox.rb.util.ItemBuilder;
import me.saiintbrisson.minecraft.View;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class OverrideConfirmationView extends View {

    public OverrideConfirmationView(HomeManager manager, String home, Player player) {
        super(9, "Voulez-vous remplacer votre home ?");

        slot(3)
                .onRender(render -> render.setItem(new ItemBuilder(Material.LIME_BED).displayname("§aOui").build()))
                .onClick(click -> {
                    manager.replaceHome(player.getUniqueId(), home, new Home(
                            home, player.getLocation(), player.getUniqueId()));
                    player.sendMessage("§aHome remplacé avec succès.");
                    close();
                });
        slot(5)
                .onRender(render -> render.setItem(new ItemBuilder(Material.RED_BED).displayname("§cNon").build()))
                .onClick(click -> close());

        setCancelOnClick(true);
        setCancelOnClone(true);
        setCancelOnDrag(true);
        setCancelOnDrop(true);
        setCancelOnMoveOut(true);
        setCancelOnPickup(true);
        setCancelOnShiftClick(true);
    }
}
