package fr.azodox.rb.home;

import fr.azodox.rb.util.HeadUtil;
import fr.azodox.rb.util.ItemBuilder;
import me.saiintbrisson.minecraft.PaginatedView;
import me.saiintbrisson.minecraft.PaginatedViewSlotContext;
import me.saiintbrisson.minecraft.ViewItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class HomeListView extends PaginatedView<Home> {

    public HomeListView(String title, List<Home> homes) {
        super(9 * 6, title);
        setSource(homes);
        setPreviousPageItem((context, item) -> item.withItem(new ItemBuilder(HeadUtil.getHead("previousPage")).displayname("§c§lPage précédente").build()));
        setNextPageItem((context, item) -> item.withItem(new ItemBuilder(HeadUtil.getHead("nextPage")).displayname("§c§lPage suivante").build()));
    }

    @Override
    protected void onItemRender(@NotNull PaginatedViewSlotContext<Home> context, @NotNull ViewItem viewItem, @NotNull Home value) {
        var randomHouse = randomHouse();
        viewItem.withItem(new ItemBuilder(
            randomHouse
        )
                .displayname("§c" + value.getName())
                .lore("§8§m                       ",
                        "§ex : " + value.getLocation().getX(),
                        "§ey : " + value.getLocation().getY(),
                        "§ez : " + value.getLocation().getZ(),
                        "§eworld : " + value.getLocation().getWorld().getName()
                )
                .build());
    }

    private ItemStack randomHouse(){
        var houses = HeadUtil.getTranslator().keySet().stream().filter(s -> s.startsWith("house_")).toList();
        return HeadUtil.getHead(houses.get(new Random().nextInt(houses.size())));
    }
}
