package netheriteplugin.netheriteplugin.recipe;


import com.google.gson.Gson;
import netheriteplugin.netheriteplugin.utils.LifePlayer;
import netheriteplugin.netheriteplugin.utils.Utils;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class HealthManager {
    private static ArrayList<LifePlayer> lifePlayers = new ArrayList();

    public HealthManager() {
    }

    public static void create(Player player, boolean usePlayerMaxHealth) {
        LifePlayer lifePlayer = new LifePlayer(player);
        lifePlayer.setHealth(Utils.getHealth(player));
        lifePlayers.add(lifePlayer);
    }

    public static LifePlayer read(String uuid) {
        Iterator var1 = lifePlayers.iterator();

        LifePlayer lifePlayer;
        do {
            if (!var1.hasNext()) {
                return null;
            }

            lifePlayer = (LifePlayer)var1.next();
        } while(!lifePlayer.getUuid().equals(uuid));

        return lifePlayer;
    }

    public static LifePlayer getPlayer(Player player) {
        return player != null ? read(player.getUniqueId().toString()) : null;
    }

    public static LifePlayer getPlayerByName(String name) {
        Iterator var1 = lifePlayers.iterator();

        LifePlayer lifePlayer;
        do {
            if (!var1.hasNext()) {
                return null;
            }

            lifePlayer = (LifePlayer)var1.next();
        } while(!lifePlayer.getName().equals(name));

        return lifePlayer;
    }

    public static void update(String uuid, LifePlayer lifePlayer) {
        LifePlayer oldLifePlayer = read(uuid);
        if (oldLifePlayer != null) {
            lifePlayers.set(lifePlayers.indexOf(oldLifePlayer), lifePlayer);
        }
    }

    public static void delete(String uuid) {
        LifePlayer lifePlayer = read(uuid);
        if (lifePlayer != null) {
            lifePlayers.remove(lifePlayer);
        }
    }

    public static void save() throws IOException {
        Gson gson = new Gson();
        File file = Utils.getDataFile("lifePlayers.json");
        file.getParentFile().mkdir();
        file.getParentFile().getParentFile().mkdir();
        file.createNewFile();
        Writer writer = new FileWriter(file, false);
        gson.toJson(lifePlayers, writer);
        writer.flush();
        writer.close();
        Utils.log("Saved Lifesteal Players Data");
    }

    public static void load() throws FileNotFoundException {
        Gson gson = new Gson();
        File file = Utils.getDataFile("lifePlayer.json");
        if (file.exists()) {
            Reader reader = new FileReader(file);
            LifePlayer[] lp = (LifePlayer[])gson.fromJson(reader, LifePlayer[].class);
            lifePlayers = new ArrayList(Arrays.asList(lp));
        }
    }

    public static double getHealth(Player player) {
        LifePlayer lifePlayer = getPlayer(player);
        return lifePlayer == null ? -1.0 : lifePlayer.getHealth();
    }

    public static void setHealth(Player player, double amount) {
        LifePlayer lifePlayer = getPlayer(player);
        if (lifePlayer != null) {
            lifePlayer.setHealth(amount);
        }
    }

    public static void addHealth(Player player, double amount) {
        setHealth(player, getHealth(player) + amount);
    }

    public static void removeHealth(Player player, double amount) {
        addHealth(player, -amount);
    }

    public static void syncPlayer(Player player) {
        LifePlayer lifePlayer = getPlayer(player);
        if (lifePlayer != null) {
            Utils.setMaxHealth(player, lifePlayer.getHealth());
            player.damage(0.1);
        }
    }
}
