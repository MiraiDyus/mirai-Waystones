package gg.discord.angryzero.zeroWays.Helper;

import gg.discord.angryzero.zeroWays.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SaveTask extends BukkitRunnable {
    private final PlayerDataManager dataManager;
    private final LodestoneManager lodestoneManager;

    public SaveTask(PlayerDataManager dataManager, LodestoneManager lodestoneManager) {
        this.dataManager = dataManager;
        this.lodestoneManager = lodestoneManager;
    }

    @Override
    public void run() {
        if (Bukkit.getOnlinePlayers().isEmpty()) {
            return;
        }

        Main.instance.getLogger().info("Running periodic data save for all online players...");

        for (Player player : Bukkit.getOnlinePlayers()) {
            dataManager.savePlayerHomes(player);
        }

        lodestoneManager.save();
        Main.instance.getLogger().info("Periodic save complete.");
    }
}