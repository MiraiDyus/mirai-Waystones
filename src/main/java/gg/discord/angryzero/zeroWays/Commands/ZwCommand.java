package gg.discord.angryzero.zeroWays.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.UUID;

import static org.bukkit.World.Environment.NETHER;
import static org.bukkit.World.Environment.THE_END;

public class ZwCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    // Constructor to get the main plugin instance for config access
    public ZwCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by a player.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "You must specify a command shortcut. Usage: /zw <shortcut>");
            return true;
        }

        // Special handling for home.<name> teleport with token
        if (args[0].startsWith("home.")) {
            String homeName = args[0].substring("home.".length());
            // Reject home names with spaces
            if (homeName.contains(" ")) {
                player.sendMessage(ChatColor.RED + "Home names cannot contain spaces.");
                return true;
            }
            // Token must be present as the last argument
            if (args.length < 2) {
                player.sendMessage(ChatColor.RED + "Invalid or missing token. Use the waystone menu.");
                return true;
            }
            String tokenStr = args[1];
            UUID expectedToken = gg.discord.angryzero.zeroWays.Main.waystoneTokens.get(player.getUniqueId());
            if (expectedToken == null || !expectedToken.toString().equals(tokenStr)) {
                player.sendMessage(ChatColor.RED + "Invalid or expired token. Use the waystone menu.");
                return true;
            }
            // Remove token after use
            gg.discord.angryzero.zeroWays.Main.waystoneTokens.remove(player.getUniqueId());

            // Get player's homes from cache
            var homes = gg.discord.angryzero.zeroWays.Main.homesCache.get(player.getUniqueId());
            if (homes == null || !homes.containsKey(homeName)) {
                player.sendMessage(ChatColor.RED + "Unknown home: '" + homeName + "'");
                return true;
            }

            var loc = homes.get(homeName);
            // Teleport as console (no permission required)
            String tpCmd = String.format("minecraft:execute in minecraft:%s as %s run tp %s %s %s",
                    getDimensionName(Objects.requireNonNull(loc.getWorld())),
                    player.getName(),
                    loc.getX(),
                    loc.getY(),
                    loc.getZ()
            );
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), tpCmd);
            plugin.getLogger().info("Teleported " + player.getName() + " to home: " + homeName);
            player.sendMessage(ChatColor.GREEN + "Teleported to home: " + homeName);
            return true;
        }

        // Retrieve the command string from the config file, searching under the "commands" section
        String commandToExecute = plugin.getConfig().getString("commands." + args[0]);

        // Check if the key was valid and a command was found
        if (commandToExecute == null || commandToExecute.isEmpty()) {
            player.sendMessage(ChatColor.RED + "Unknown command shortcut: '" + args[0] + "'");
            return true;
        }

        // Replace placeholders with actual values
        String finalCommand = commandToExecute.replace("%player%", player.getName());

        // Execute the command from the console
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCommand);

        // Optional: Log to console for security/auditing purposes
        plugin.getLogger().info("Executed command for " + player.getName() + " via /zw: \"" + finalCommand + "\"");

        // Optional: Send a confirmation message to the player
        player.sendMessage(ChatColor.GREEN + "Action performed!");

        return true;
    }


    public String getDimensionName(World world) {
        World.Environment environment = world.getEnvironment();

        switch (environment) {
            case NORMAL:
                return "overworld";
            case NETHER:
                return "nether";
            case THE_END:
                return "the_end";
            default:
                // For worlds created by other plugins or datapacks
                return "custom";
        }
    }
}