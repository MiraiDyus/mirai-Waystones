package gg.discord.angryzero.zeroWays.Helper;

import gg.discord.angryzero.zeroWays.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {

    public void loadPlayerHomes(Player player) {
        PersistentDataContainer container = player.getPersistentDataContainer();
        int locationCount = container.getOrDefault(NamespacedKeysCollection.LOCATION_COUNT.getKey(), PersistentDataType.INTEGER, 0);

        Map<String, Location> homes = new ConcurrentHashMap<>();

        for (int i = 0; i < locationCount; i++) {
            if (container.has(new NamespacedKey(Main.instance, "location." + i), PersistentDataType.STRING)) {
                String locationData = container.get(new NamespacedKey(Main.instance, "location." + i), PersistentDataType.STRING);

                if (locationData == null || locationData.isEmpty()) {
                    continue;
                }

                String[] parts = locationData.split(";");
                if (parts.length == 5) {
                    String name = parts[0];
                    String worldName = parts[1];
                    double x = Double.parseDouble(parts[2]);
                    double y = Double.parseDouble(parts[3]);
                    double z = Double.parseDouble(parts[4]);

                    homes.put(
                            name,
                            new Location(
                                    Bukkit.getWorld(worldName),
                                    x,
                                    y,
                                    z
                            )
                    );
                }
            }
        }

        if (!homes.isEmpty()) {
            Main.homesCache.put(player.getUniqueId(), homes);
        }
    }

    public void savePlayerHomes(Player player) {

        PersistentDataContainer container = player.getPersistentDataContainer();
        Map<String, Location> homes = Main.homesCache.get(player.getUniqueId());

        if (homes == null || homes.isEmpty()) {
            container.remove(NamespacedKeysCollection.LOCATION_COUNT.getKey());
            return;
        }

        container.set(NamespacedKeysCollection.LOCATION_COUNT.getKey(), PersistentDataType.INTEGER, homes.size());

        int index = 0;
        for (Map.Entry<String, Location> entry : homes.entrySet()) {
            // Skip home names with spaces
            if (entry.getKey().contains(" ")) {
                continue;
            }
            String locationData = String.join(";",
                    entry.getKey(),
                    Objects.requireNonNull(entry.getValue().getWorld()).getName(),
                    String.valueOf(entry.getValue().getX()),
                    String.valueOf(entry.getValue().getY()),
                    String.valueOf(entry.getValue().getZ())
            );

            container.set(new NamespacedKey(Main.instance, "location." + index), PersistentDataType.STRING, locationData);
            index++;
        }
    }
}