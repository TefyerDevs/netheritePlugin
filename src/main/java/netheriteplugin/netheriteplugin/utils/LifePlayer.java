package netheriteplugin.netheriteplugin.utils;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class LifePlayer {
    private final String uuid;
    private String name;
    private double health;
    private LifeState lifeState;

    public LifePlayer(String uuid, int health) {
        this.uuid = uuid;
        this.name = Utils.getNameFromUuid(uuid);
        this.health = (double)health;
        this.lifeState = LifeState.ALIVE;
    }

    public LifePlayer(String uuid, int health, LifeState lifeState) {
        this.uuid = uuid;
        this.name = Utils.getNameFromUuid(uuid);
        this.health = (double)health;
        this.lifeState = lifeState;
    }

    public LifePlayer(Player player) {
        this.uuid = player.getUniqueId().toString();
        this.name = player.getName();
        this.health = Utils.getHealth(player);
        this.lifeState = LifeState.ALIVE;
    }

    public LifeState getLifeState() {
        return this.lifeState;
    }

    public void setLifeState(LifeState lifeState) {
        this.lifeState = lifeState;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getHealth() {
        return this.health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void updateName() {
        Player player = Bukkit.getPlayer(UUID.fromString(this.getUuid()));
        if (player != null) {
            this.setName(player.getName());
        }
    }
}

