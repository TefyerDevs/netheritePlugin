package netheriteplugin.netheriteplugin.utils;

import com.google.gson.Gson;
import netheriteplugin.netheriteplugin.NetheritePlugin;
import netheriteplugin.netheriteplugin.cofig.ConfigManager;
import netheriteplugin.netheriteplugin.dynamic.*;
import netheriteplugin.netheriteplugin.recipe.HealthManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.File;
import java.util.Arrays;
import java.util.UUID;

public class Utils {
    public static final double INVALID_HEALTH = -1.0;

    public static String getNameFromUuid(String uuid) {
        return Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
    }

    public static double getHealth(Player player) {
        if (player == null) {
            return -1.0;
        } else {
            AttributeInstance attributeInstance = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            return attributeInstance == null ? -1.0 : attributeInstance.getBaseValue();
        }
    }

    public static int getPlayerHearts(Player player) {
        return player == null ? -1 : (int)(HealthManager.getHealth(player) / 2.0);
    }

    public static void setMaxHealth(Player player, double health) {
        if (!(health <= 0.0) && player != null) {
            AttributeInstance attributeInstance = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            if (attributeInstance != null) {
                attributeInstance.setBaseValue(health);
            }
        }
    }
    public static File getDataFile(String fileName) {
        String var10002 = NetheritePlugin.plugin.getDataFolder().getAbsolutePath();
        return new File(var10002 + "/data/" + fileName);
    }

    public static void log(String message) {
        Bukkit.getLogger().warning(message);
    }

    public static boolean commandCheck(CommandSender sender, String[] args, int minArgs, String perm, boolean defaultPerm, boolean consoleUsage, boolean showErrorMessage) {
        String permissionError = "Missing permission \"" + perm + "\"";
        String senderError = "Console cannot execute this command";
        String argsError = "Not enough arguments to execute this command";
        defaultPerm = defaultPerm && (Boolean) ConfigManager.getField("DefaultPermissions");
        if (!defaultPerm && !sender.hasPermission(perm)) {
            if (showErrorMessage) {
                sender.sendMessage(formatError(permissionError));
            }

            return true;
        } else if (args.length < minArgs) {
            if (showErrorMessage) {
                sender.sendMessage(formatError(argsError));
            }

            return true;
        } else if (!consoleUsage && !(sender instanceof Player)) {
            if (showErrorMessage) {
                sender.sendMessage(formatError(senderError));
            }

            return true;
        } else {
            return false;
        }
    }
    public static String formatError(String s) {
        return ChatColor.RED + "Netherite Plugin Error: " + s;
    }

    public static String formatInfo(String s) {
        return ChatColor.GREEN + "Netherite Plugin Info: " + s;
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException var2) {
            return false;
        }
    }

    public static boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException var2) {
            return false;
        }
    }

    public static String col(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static void safeGiveItem(Player player, ItemStack item) {
        if (!isInventoryFull(player.getInventory())) {
            player.getInventory().addItem(new ItemStack[]{item});
        } else {
            player.getWorld().dropItem(player.getLocation(), item);
        }

    }

    public static boolean isInventoryFull(PlayerInventory inv) {
        ItemStack[] var1 = inv.getStorageContents();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            ItemStack item = var1[var3];
            if (item == null) {
                return false;
            }
        }

        return true;
    }

    public static void removeItemInHand(Player player, int amount, boolean isOffhand) {
        PlayerInventory inv = player.getInventory();
        ItemStack item = isOffhand ? inv.getItemInOffHand() : inv.getItemInMainHand();
        item.setAmount(item.getAmount() - amount);
        if (!isOffhand) {
            inv.setItemInMainHand(item);
        } else {
            inv.setItemInOffHand(item);
        }

    }

    public static DynamicTypeValue stringToDynamicValue(String s) {
        if (s.equalsIgnoreCase("TRUE")) {
            return new BooleanTypeValue(true);
        } else if (s.equalsIgnoreCase("FALSE")) {
            return new BooleanTypeValue(false);
        } else if (isDouble(s)) {
            return new DoubleTypeValue(Double.parseDouble(s));
        } else {
            Gson gson = new Gson();
            log(s);
            if (s.contains("type")) {
                ItemStack[] itemStacks = (ItemStack[])gson.fromJson(s, ItemStack[].class);
                return new ItemStackListTypeValue(Arrays.asList(itemStacks));
            } else {
                return new StringTypeValue(s);
            }
        }
    }

    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public static String getUuidFromName(String name) {
        LifePlayer player = HealthManager.getPlayerByName(name);
        return player != null ? player.getUuid() : "";
    }
}
