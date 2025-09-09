package gg.discord.angryzero.zeroWays.Events;

import gg.discord.angryzero.zeroWays.Helper.LodestoneManager;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;

public class LodestoneListener_Anvil implements Listener {

    private final JavaPlugin plugin;
    private final LodestoneManager lodestoneManager;

    public LodestoneListener_Anvil(JavaPlugin plugin, LodestoneManager lodestoneManager) {
        this.plugin = plugin;
        this.lodestoneManager = lodestoneManager;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlockPlaced().getType() != Material.LODESTONE) {
            return;
        }

        new AnvilGUI.Builder()
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) return Collections.emptyList();
                    String text = stateSnapshot.getText();
                    if (text == null || text.trim().isEmpty()) {
                        stateSnapshot.getPlayer().sendMessage(ChatColor.RED + "You must provide a name!");
                        return AnvilGUI.Response.text("Please enter a name.");
                    }
                    // Remove all spaces from the name
                    String sanitized = text.replace(" ", "");
                    if (sanitized.isEmpty()) {
                        stateSnapshot.getPlayer().sendMessage(ChatColor.RED + "Name cannot be only spaces!");
                        return AnvilGUI.Response.text("Please enter a valid name.");
                    }
                    lodestoneManager.addLodestone(sanitized, event.getBlock().getLocation());
                    stateSnapshot.getPlayer().sendMessage(ChatColor.GREEN + "Lodestone successfully named '" + sanitized + "'!");
                    return AnvilGUI.Response.close();
                })
                .onClose(stateSnapshot -> {
                    stateSnapshot.getPlayer().sendMessage(ChatColor.YELLOW + "Lodestone naming cancelled.");
                })
                .title(ChatColor.DARK_PURPLE + "Enter Lodestone Name")
                .text("My special place...")
                .plugin(plugin)
                .open(event.getPlayer());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() != Material.LODESTONE) {
            return;
        }
        String lodestoneName = lodestoneManager.getLodestoneName(event.getBlock().getLocation());
        if (lodestoneName != null) {
            // Remove from all users' homes
            gg.discord.angryzero.zeroWays.Main.homesCache.forEach((uuid, homes) -> {
                homes.entrySet().removeIf(entry -> {
                    // Remove if the home location matches the broken lodestone
                    var loc = entry.getValue();
                    return loc.getWorld().equals(event.getBlock().getWorld())
                            && loc.getBlockX() == event.getBlock().getX()
                            && loc.getBlockY() == event.getBlock().getY()
                            && loc.getBlockZ() == event.getBlock().getZ();
                });
            });
            // Remove from lodestone list
            lodestoneManager.removeLodestoneByLocation(event.getBlock().getLocation());
            event.getPlayer().sendMessage(ChatColor.GRAY + "You have broken a named lodestone. It has been removed from all users.");
        }
    }
}