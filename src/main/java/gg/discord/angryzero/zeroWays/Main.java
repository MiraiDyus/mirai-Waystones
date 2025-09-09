package gg.discord.angryzero.zeroWays;

import gg.discord.angryzero.zeroWays.Commands.ZwCommand;
import gg.discord.angryzero.zeroWays.Events.LodestoneInteractListener;
import gg.discord.angryzero.zeroWays.Events.LodestoneListener_Anvil;
import gg.discord.angryzero.zeroWays.Events.PlayerListener;
import gg.discord.angryzero.zeroWays.Helper.LodestoneManager;
import gg.discord.angryzero.zeroWays.Helper.PlayerDataManager;
import gg.discord.angryzero.zeroWays.Helper.SaveTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class Main extends JavaPlugin {
    public static PlayerDataManager playerDataManager;
    public static final Map<UUID, Map<String, Location>> homesCache = new ConcurrentHashMap<>();
    public static final Map<UUID, UUID> waystoneTokens = new ConcurrentHashMap<>();
    public static Main instance;
    public static LodestoneManager lodestoneManager;

    @Override
    public void onEnable() {
        instance = this;

        playerDataManager = new PlayerDataManager();
        lodestoneManager = new LodestoneManager(this);

        // Load and Store the Locations for the Lodestone (Waystone).
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new LodestoneListener_Anvil(this, lodestoneManager), this);
        getServer().getPluginManager().registerEvents(new LodestoneInteractListener(), this);

        getServer().getPluginCommand("zw").setExecutor(new ZwCommand(this));

        // ToDo: Fix this storing event stufff this.
        new SaveTask(playerDataManager, lodestoneManager).runTaskTimer(this, 20L * 60, 20L * 60 * 4); // Every 5 mins

        getLogger().info("HomePlugin has been enabled with a proper manager!");
    }

    @Override
    public void onDisable() {
        // Save data for all online players before shutdown
        lodestoneManager.save();
        if (playerDataManager != null) {
            getLogger().info("Saving all player data before shutdown...");
            for (Player player : Bukkit.getOnlinePlayers()) {
                playerDataManager.savePlayerHomes(player);
            }
            getLogger().info("All player data saved.");
        }
    }
}
