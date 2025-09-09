package gg.discord.angryzero.zeroWays.Helper;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LodestoneManager {

    private final JavaPlugin plugin;
    private final Map<String, Location> lodestones = new ConcurrentHashMap<>();
    private final File configFile;
    private FileConfiguration lodestoneConfig;

    public LodestoneManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), "lodestones.yml");
        load();
    }

    public void addLodestone(String name, Location loc) {
        Location blockLoc = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        lodestones.put(name, blockLoc);
    }

    public Location getLodestoneLocation(String name) {
        return lodestones.get(name);
    }

    public String getLodestoneName(Location loc) {
        Location blockLoc = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        for (Map.Entry<String, Location> entry : lodestones.entrySet()) {
            if (entry.getValue().equals(blockLoc)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void removeLodestoneByName(String name) {
        lodestones.remove(name);
    }

    public void removeLodestoneByLocation(Location loc) {
        String name = getLodestoneName(loc);
        if (name != null) {
            lodestones.remove(name);
        }
    }

    public void save() {
        try {
            lodestoneConfig.set("lodestones", null); // Clear old section
            for (Map.Entry<String, Location> entry : lodestones.entrySet()) {
                String serialized = LocationUtil.serialize(entry.getKey(), entry.getValue());
                lodestoneConfig.set("lodestones." + entry.getKey(), serialized);
            }
            lodestoneConfig.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save lodestones.yml!");
            e.printStackTrace();
        }
    }

    public void load() {
        if (!configFile.exists()) {
            plugin.saveResource("lodestones.yml", false);
        }
        lodestoneConfig = YamlConfiguration.loadConfiguration(configFile);
        lodestones.clear();

        ConfigurationSection section = lodestoneConfig.getConfigurationSection("lodestones");
        if (section != null) {
            for (String name : section.getKeys(false)) {
                String serialized = section.getString(name);
                Location loc = LocationUtil.deserialize(serialized);
                if (loc != null) {
                    // Only add if the block at the location is still a LODESTONE
                    if (loc.getWorld() != null && loc.getBlock().getType() == org.bukkit.Material.LODESTONE) {
                        lodestones.put(name, loc);
                    } else {
                        plugin.getLogger().warning("Lodestone '" + name + "' at " + loc + " is missing or invalid and will not be loaded.");
                    }
                }
            }
        }
        plugin.getLogger().info("Loaded " + lodestones.size() + " named lodestones.");
    }
}