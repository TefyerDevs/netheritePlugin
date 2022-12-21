package netheriteplugin.netheriteplugin.utils;


import netheriteplugin.netheriteplugin.cofig.ConfigManager;
import netheriteplugin.netheriteplugin.recipe.HealthManager;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class LifestealUtils {
    public LifestealUtils() {
    }

    public static void withdraw(Player player, int hearts) {
        int amount = hearts * 2;
        HealthManager.removeHealth(player, (double)amount);
        HealthManager.syncPlayer(player);
        Utils.safeGiveItem(player, HeartItem.getItem(hearts));
    }

    public static void useHeart(Player player) {
        double hearts = 0.0;
        if (HeartItem.isItem(player.getInventory().getItemInMainHand())) {
            Utils.removeItemInHand(player, 1, false);
            hearts += 2.0;
        }

        if (HeartItem.isItem(player.getInventory().getItemInOffHand())) {
            Utils.removeItemInHand(player, 1, true);
            hearts += 2.0;
        }

        player.updateInventory();
        if (isPassingHeartLimit(HealthManager.getHealth(player) + hearts)) {
            player.sendMessage(Utils.formatError("Cannot use heart, reached heart limit"));
        }

        player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
        safeAddHeart(player, hearts);
        HealthManager.syncPlayer(player);
    }

    public static void useReviveBook(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();

        assert item.getItemMeta() != null;

        String reviveName = item.getItemMeta().getDisplayName();
        LifePlayer lifePlayer = HealthManager.getPlayerByName(reviveName);
        if (lifePlayer == null) {
            player.sendMessage(Utils.formatError("Specified player doesn't exist"));
        } else if (lifePlayer.getLifeState() != LifeState.ELIMINATED) {
            player.sendMessage(Utils.formatError("Specified player isn't eliminated"));
        } else {
            revivePlayer(lifePlayer);
            if (ReviveBookItem.isItem(player.getInventory().getItemInMainHand())) {
                Utils.removeItemInHand(player, 1, false);
            }

            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0F, 1.0F);
        }
    }

    public static void eliminateKick(Player player) {
        if (player != null) {
            String banMessage = Utils.col("&cYou have lost all your hearts and have been eliminated!");
            player.kickPlayer(banMessage);
        }
    }

    public static boolean eliminatePlayer(LifePlayer player) {
        if (player.getLifeState() != LifeState.ALIVE) {
            return false;
        } else {
            player.setLifeState(LifeState.ELIMINATED);
            OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(player.getUuid()));
            if (op.getPlayer() != null) {
                eliminateKick(op.getPlayer());
            }

            if ((Boolean)ConfigManager.getField("BroadcastEvents")) {
                Bukkit.broadcastMessage(Utils.col("&cKLS: &4&l" + player.getName() + " &cHas been eliminated!"));
            }

            return true;
        }
    }

    public static boolean revivePlayer(LifePlayer player) {
        if (player.getLifeState() != LifeState.ELIMINATED) {
            return false;
        } else {
            player.setLifeState(LifeState.REVIVED);
            if ((Boolean)ConfigManager.getField("BroadcastEvents")) {
                Bukkit.broadcastMessage(Utils.col("&aKLS: &2&l" + player.getName() + " &aHas been revived!"));
            }

            return true;
        }
    }

    public static void onRevive(Player player) {
        LifePlayer lifePlayer = HealthManager.getPlayer(player);
        if (lifePlayer.getLifeState() == LifeState.REVIVED) {
            lifePlayer.setLifeState(LifeState.ALIVE);
            double hearts = (Double)ConfigManager.getField("RevivalHearts");
            lifePlayer.setHealth(hearts);
            if (!(Boolean)player.getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY)) {
                player.getInventory().clear();
            }

            if ((Boolean)ConfigManager.getField("BroadcastEvents")) {
                Bukkit.broadcastMessage(Utils.col("&aKLS: &2&l" + player.getName() + " &aHas joined after being revived!"));
            }

            HealthManager.syncPlayer(player);
        }
    }

    public static void safeSetHeart(Player player, double amount) {
        if (!(amount <= 0.0)) {
            HealthManager.setHealth(player, amount);
            HealthManager.syncPlayer(player);
        }
    }

    public static boolean isPassingHeartLimit(double amount) {
        boolean hasHeartLimit = (Boolean)ConfigManager.getField("HasHeartLimit");
        double heartLimit = (Double)ConfigManager.getField("HeartLimit");
        return hasHeartLimit && heartLimit < amount;
    }

    public static void safeAddHeart(Player player, double amount) {
        double newHeartsAmount = HealthManager.getHealth(player) + amount;
        double heartLimit = (Double)ConfigManager.getField("HeartLimit");
        if (newHeartsAmount <= 0.0) {
            eliminatePlayer(HealthManager.getPlayer(player));
        } else {
            if ((Boolean)ConfigManager.getField("HasHeartLimit")) {
                double difference = newHeartsAmount - heartLimit;
                if (difference > 0.0) {
                    newHeartsAmount = heartLimit;
                    Utils.safeGiveItem(player, HeartItem.getItem((int)(difference / 2.0)));
                }
            }

            HealthManager.setHealth(player, newHeartsAmount);
            HealthManager.syncPlayer(player);
        }
    }

    public static void safeRemoveHeart(Player player, double amount) {
        safeAddHeart(player, -amount);
    }

    public static void calculateDeath(Player player) {
        Player killer = player.getKiller();
        boolean naturalCauseHeartLoss = (Boolean)ConfigManager.getField("NaturalCauseHeartLoss");
        double gainRate = (Double)ConfigManager.getField("HeartGainRate");
        double lostRate = (Double)ConfigManager.getField("HeartLostRate");
        if (killer != null || naturalCauseHeartLoss) {
            if (killer == null || player.getUniqueId() != killer.getUniqueId()) {
                safeRemoveHeart(player, lostRate);
                if (killer != null) {
                    safeAddHeart(killer, gainRate);
                }

            }
        }
    }

    public static void updatePlayerDataOnLoad(Player player) {
        LifePlayer lifePlayer = HealthManager.getPlayer(player);
        if (lifePlayer == null) {
            HealthManager.create(player, (Boolean) ConfigManager.getField("CopyOldHealth"));
        } else {
            switch (lifePlayer.getLifeState()) {
                case ELIMINATED:
                    eliminateKick(player);
                    break;
                case REVIVED:
                    onRevive(player);
            }

            lifePlayer.updateName();
        }

        HealthManager.syncPlayer(player);
    }
}
