package netheriteplugin.netheriteplugin;

import me.kodysimpson.simpapi.menu.MenuManager;
import netheriteplugin.netheriteplugin.cofig.ConfigManager;
import netheriteplugin.netheriteplugin.recipe.HealthManager;
import netheriteplugin.netheriteplugin.utils.LifestealUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Iterator;

public final class NetheritePlugin extends JavaPlugin {
    public static JavaPlugin plugin;
    public static FileConfiguration config;

    @Override
    public void onEnable() {
        plugin = this;
        config = plugin.getConfig();
        this.config.options().copyDefaults();
        this.saveDefaultConfig();

        try {
            this.loadData();
        } catch (IOException var2) {
            var2.printStackTrace();
        }

        this.syncData();
        MenuManager.setup(this.getServer(), this);
        this.registerEvents();
        this.registerCommands();
        this.registerItems();

    }

    @Override
    public void onDisable() {
        try {
            this.saveData();
        } catch (IOException var2) {
            var2.printStackTrace();
        }
    }

    private void registerCommands() {
        this.getCommand("withdraw").setExecutor(new Withdraw());
        this.getCommand("modify").setExecutor(new Modify());
        this.getCommand("lsconfig").setExecutor(new LsConfig());
        this.getCommand("eliminate").setExecutor(new Eliminate());
        this.getCommand("revive").setExecutor(new Revive());
        this.getCommand("lsitem").setExecutor(new LsItem());
        this.getCommand("kls").setExecutor(new KlsCommand());
        this.getCommand("lsconfig").setTabCompleter(new LsConfigCompletion());
        this.getCommand("revive").setTabCompleter(new LifeCompletion());
        this.getCommand("eliminate").setTabCompleter(new LifeCompletion());
        this.getCommand("modify").setTabCompleter(new ModifyCompletion());
        this.getCommand("lsitem").setTabCompleter(new LsItemCompletion());
    }

    private void registerEvents() {
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
    }

    private void registerItems() {
        HeartItem.registerItem();
        ReviveBeconItem.registerItem();
        ReviveBeconItem.registerRecipe();
    }

    private void loadData() throws IOException {
        ConfigManager.load();
        HealthManager.load();
        RecipeManager.load();
    }

    private void saveData() throws IOException {
        ConfigManager.save();
        HealthManager.save();
        RecipeManager.save();
        this.saveDefaultConfig();
    }

    private void syncData() {
        Iterator var1 = Bukkit.getOnlinePlayers().iterator();

        while(var1.hasNext()) {
            Player player = (Player)var1.next();
            LifestealUtils.updatePlayerDataOnLoad(player);
        }

    }
}
