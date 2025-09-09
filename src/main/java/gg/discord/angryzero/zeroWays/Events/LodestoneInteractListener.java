package gg.discord.angryzero.zeroWays.Events;

import gg.discord.angryzero.zeroWays.Helper.BookMenu;
import gg.discord.angryzero.zeroWays.Main;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.Location;
import net.md_5.bungee.api.chat.ComponentBuilder;

import java.util.*;

public class LodestoneInteractListener implements Listener {

    private static final int HOMES_PER_PAGE = 10;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null || clickedBlock.getType() != Material.LODESTONE) {
            return;
        }

        Player player = event.getPlayer();
        Location lodestoneLoc = clickedBlock.getLocation();

        // Lodestone-Name holen
        String lodestoneName = Main.lodestoneManager.getLodestoneName(lodestoneLoc);

        if (lodestoneName != null) {
            Map<String, Location> homes = Main.homesCache.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>());

            // Prüfen, ob der Spieler diesen Lodestone schon kennt (blockgenau)
            boolean alreadyKnown = homes.values().stream().anyMatch(loc ->
                    loc.getWorld().equals(lodestoneLoc.getWorld()) &&
                            loc.getBlockX() == lodestoneLoc.getBlockX() &&
                            loc.getBlockY() == lodestoneLoc.getBlockY() &&
                            loc.getBlockZ() == lodestoneLoc.getBlockZ()
            );

            if (!alreadyKnown) {
                String name = lodestoneName;
                int i = 1;
                while (homes.containsKey(name)) {
                    name = lodestoneName + "-" + i++;
                }
                homes.put(name, lodestoneLoc);
                player.sendMessage("§aDu hast einen neuen Waystone entdeckt: §e" + name);
            }

            // Generate and store a token for this player
            UUID token = UUID.randomUUID();
            Main.waystoneTokens.put(player.getUniqueId(), token);

            // Homes-Liste ohne aktuellen Lodestone
            BookMenu menu = new BookMenu()
                    .setTitle("§5Waystone")
                    .setAuthor("angryzero");

            List<Map.Entry<String, Location>> entries = new ArrayList<>(homes.entrySet());
            // Filter: aktuellen Lodestone nicht anzeigen
            entries.removeIf(e -> e.getValue().getWorld().equals(lodestoneLoc.getWorld())
                    && e.getValue().getBlockX() == lodestoneLoc.getBlockX()
                    && e.getValue().getBlockY() == lodestoneLoc.getBlockY()
                    && e.getValue().getBlockZ() == lodestoneLoc.getBlockZ());

            if (!entries.isEmpty()) {
                List<String> homeNames = new ArrayList<>();
                for (Map.Entry<String, Location> entry : entries) {
                    homeNames.add(entry.getKey());
                }
                int totalPages = (int) Math.ceil(homeNames.size() / (double) HOMES_PER_PAGE);

                for (int page = 0; page < totalPages; page++) {
                    int start = page * HOMES_PER_PAGE;
                    int end = Math.min(start + HOMES_PER_PAGE, homeNames.size());
                    List<String> pageHomes = homeNames.subList(start, end);

                    ComponentBuilder pageBuilder = new ComponentBuilder("§lChoose a Location (Seite " + (page + 1) + "/" + totalPages + "):\n\n");
                    for (String homeName : pageHomes) {
                        pageBuilder.append(BookMenu.createClickableText(
                                "[" + homeName + "]\n",
                                "zw home." + homeName + " " + token, // append token
                                "§aTeleport to home: " + homeName));
                    }
                    menu.addPage(pageBuilder);
                }
            } else {
                menu.addPage(
                        new ComponentBuilder("§lChoose a Location:\n\n§7No homes set.\n")
                );
            }

            menu.open(player);
            event.setCancelled(true);
        }
    }
}