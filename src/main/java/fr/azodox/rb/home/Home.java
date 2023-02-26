package fr.azodox.rb.home;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

public class Home {

    private @Getter String name;
    private @Getter Location location;
    private @Getter UUID owner;

    public Home(String name, Location location, UUID owner) {
        this.name = name;
        this.location = location;
        this.owner = owner;
    }

    @Override
    public String toString() {
        return name + ";" + location.getWorld().getName() + ";" + location.getX() + ";" + location.getY() + ";" + location.getZ() + ";" + location.getYaw() + ";" + location.getPitch() + ";" + owner.toString();
    }

    public static Home fromString(String string){
        var split = string.split(";");
        var name = split[0];
        var location = new Location(
                Bukkit.getWorld(split[1]),
                Double.parseDouble(split[2]),
                Double.parseDouble(split[3]),
                Double.parseDouble(split[4]),
                Float.parseFloat(split[5]),
                Float.parseFloat(split[6]));
        var owner = UUID.fromString(split[7]);
        return new Home(name, location, owner);
    }
}
