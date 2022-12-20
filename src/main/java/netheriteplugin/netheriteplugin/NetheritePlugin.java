package netheriteplugin.netheriteplugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class NetheritePlugin extends JavaPlugin {
    public static JavaPlugin plugin;
    public static FileConfiguration config;

    @Override
    public void onEnable() {
        plugin = this;
        config = plugin.getConfig();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
