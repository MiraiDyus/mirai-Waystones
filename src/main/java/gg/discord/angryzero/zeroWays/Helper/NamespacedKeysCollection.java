package gg.discord.angryzero.zeroWays.Helper;

import gg.discord.angryzero.zeroWays.Main;
import org.bukkit.NamespacedKey;

public enum NamespacedKeysCollection {
    LOCATION_COUNT("location_count");

    private final NamespacedKey key;

    NamespacedKeysCollection(String key) {
        this.key = new NamespacedKey(Main.instance, key);
    }

    public NamespacedKey getKey() {
        return key;
    }
}