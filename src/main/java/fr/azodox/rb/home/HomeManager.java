package fr.azodox.rb.home;

import fr.azodox.rb.RBSurvie;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class HomeManager {

    private File homesFile;
    private YamlConfiguration homesData;

    public HomeManager(RBSurvie plugin){
        init(plugin);
    }

    @SneakyThrows
    public void init(RBSurvie plugin){
        homesFile = new File(plugin.getDataFolder(), "homes.yml");

        if (!homesFile.exists()) {
            if(!homesFile.getParentFile().exists())
                homesFile.getParentFile().mkdirs();
            homesFile.createNewFile();
        }

        homesData = YamlConfiguration.loadConfiguration(homesFile);
    }

    private void init(UUID uuid){
        if(!homesData.contains(uuid.toString())){
            homesData.set(uuid.toString(), new ArrayList<String>());
        }
        save();
    }

    public void addHome(UUID uuid, Home home){
        if(!homesData.contains(uuid.toString())){
            init(uuid);
        }

        var list = homesData.getStringList(uuid.toString());
        list.add(home.toString());
        homesData.set(uuid.toString(), list);
        save();
    }

    public void removeHome(UUID uuid, Home home){
        if(!homesData.contains(uuid.toString())){
            return;
        }

        var list = homesData.getStringList(uuid.toString());
        list.remove(home.toString());
        homesData.set(uuid.toString(), list);
        save();
    }

    public boolean hasHome(UUID uuid, String homeName){
        if(!homesData.contains(uuid.toString())){
            return false;
        }

        return getHomes(uuid).stream().anyMatch(home -> home.getName().equals(homeName));
    }

    public void replaceHome(UUID uuid, String homeName, Home replacement){
        if(!homesData.contains(uuid.toString())){
            return;
        }

        getHomes(uuid).stream().filter(home -> home.getName().equals(homeName)).findFirst().ifPresent(home -> {
            var list = homesData.getStringList(uuid.toString());
            list.remove(home.toString());
            list.add(replacement.toString());
            homesData.set(uuid.toString(), list);
            save();
        });
    }

    public Optional<Home> getHome(UUID uuid, String homeName){
        return getHomes(uuid).stream().filter(home -> home.getName().equals(homeName)).findFirst();
    }

    public List<Home> getHomes(UUID uuid){
        if(!homesData.contains(uuid.toString())){
            return List.of();
        }

        return homesData.getStringList(uuid.toString()).stream().map(Home::fromString).toList();
    }

    @SneakyThrows
    public void save(){
        homesData.save(homesFile);
    }
}
