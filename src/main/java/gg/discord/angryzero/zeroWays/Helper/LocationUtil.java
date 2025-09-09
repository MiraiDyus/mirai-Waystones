package gg.discord.angryzero.zeroWays.Helper;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationUtil {

    /**
     * Serialisiert eine Location in das Format: "name;world;x;y;z"
     */
    public static String serialize(String name, Location loc) {
        if (loc == null || name == null) return null;

        return String.join(";",
                name,
                loc.getWorld().getName(),
                String.valueOf(loc.getX()),
                String.valueOf(loc.getY()),
                String.valueOf(loc.getZ())
        );
    }

    /**
     * Deserialisiert einen String im Format "name;world;x;y;z" zu einem Location-Objekt.
     * Gibt null zurück, wenn das Format ungültig ist oder die Welt nicht existiert.
     */
    public static Location deserialize(String s) {
        if (s == null) return null;
        String[] parts = s.split(";");
        if (parts.length != 5) return null;

        try {
            World world = Bukkit.getWorld(parts[1]);
            if (world == null) return null;

            double x = Double.parseDouble(parts[2]);
            double y = Double.parseDouble(parts[3]);
            double z = Double.parseDouble(parts[4]);

            return new Location(world, x, y, z);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}