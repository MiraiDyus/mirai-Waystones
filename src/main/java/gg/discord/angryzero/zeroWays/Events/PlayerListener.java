package gg.discord.angryzero.zeroWays.Events;

import gg.discord.angryzero.zeroWays.Main;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Iterator;
import java.util.Map;
import org.bukkit.Location;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Now you can use the manager instance you were given
        Main.playerDataManager.loadPlayerHomes(event.getPlayer());

        // Remove invalid homes (not a lodestone anymore)
        var homes = Main.homesCache.get(event.getPlayer().getUniqueId());
        if (homes != null && !homes.isEmpty()) {
            Iterator<Map.Entry<String, Location>> it = homes.entrySet().iterator();
            boolean changed = false;
            while (it.hasNext()) {
                Map.Entry<String, Location> entry = it.next();
                Location loc = entry.getValue();
                if (loc.getWorld() == null || loc.getBlock().getType() != Material.LODESTONE) {
                    it.remove();
                    changed = true;
                }
            }
            if (changed) {
                // Save updated homes to persistent data
                Main.playerDataManager.savePlayerHomes(event.getPlayer());
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Main.playerDataManager.savePlayerHomes(event.getPlayer());
    }
}