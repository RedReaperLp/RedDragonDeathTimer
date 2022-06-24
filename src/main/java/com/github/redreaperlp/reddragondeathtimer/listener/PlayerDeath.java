package com.github.redreaperlp.reddragondeathtimer.listener;

import com.github.redreaperlp.reddragondeathtimer.RedDragonDeathTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

public class PlayerDeath implements Listener {
    public RedDragonDeathTimer plugin;

    public PlayerDeath(RedDragonDeathTimer plugin) {
        this.plugin = plugin;
    }

    public String message;
    public double total = 180;
    public double time = 0;


    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        message = event.getDeathMessage();
        event.setDeathMessage(null);
        if (!plugin.isPaused && !plugin.failed) {
            plugin.failed = true;
            plugin.GamerulesOff();
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(plugin.prefix + ChatColor.GOLD + "-------------------------------------------------");
            Bukkit.broadcastMessage(plugin.prefix + ChatColor.RED + "the challenge failed because of " + ChatColor.YELLOW + event.getEntity().getName());
            Bukkit.broadcastMessage(plugin.prefix + ChatColor.RED + "reason:");
            Bukkit.broadcastMessage(plugin.prefix + ChatColor.YELLOW + message);
            Bukkit.broadcastMessage(plugin.prefix + ChatColor.GOLD + "-------------------------------------------------");
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(plugin.prefix + ChatColor.BLUE + "Server-Restart in 3 minutes");
            Bukkit.broadcastMessage(plugin.prefix + ChatColor.GREEN + "use" + ChatColor.RED + " - /timer reset - " + ChatColor.GREEN + "to speed up!");
            bossBar();
        }
    }
    public boolean isActive = false;

    int bb = 0;
    public void bossBar() {
        isActive = true;
        if (plugin.bossBar == null) {
            plugin.bossBar = Bukkit.createBossBar(ChatColor.RED + "" + ChatColor.BOLD + "Server-Restart", BarColor.GREEN, BarStyle.SEGMENTED_10);
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            plugin.bossBar.addPlayer(player);
            plugin.bossBar.setVisible(true);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                time++;
                bb = (int) total - (int) time;
                if (time <= 180 && total == 180) {

                    if (time != 180) {
                        plugin.bossBar.setTitle(ChatColor.RED + "" + ChatColor.BOLD + "Server-Restart: " + bb);
                    } else {
                        plugin.bossBar.setTitle(ChatColor.RED + "" + ChatColor.BOLD + "Server-Restart: " + "now");
                    }

                    plugin.bossBar.setProgress(time / total);
                    if (time == 170) {
                        Bukkit.broadcastMessage("");
                        Bukkit.broadcastMessage(plugin.prefix + ChatColor.YELLOW + "Server restart in:");
                        Bukkit.broadcastMessage(plugin.prefix + ChatColor.RED + "10");

                    } else if (time == 175) {
                        Bukkit.broadcastMessage(plugin.prefix + ChatColor.RED + "5");

                    } else if (time == 177) {
                        Bukkit.broadcastMessage(plugin.prefix + ChatColor.RED + "3");

                    } else if (time == 178) {
                        Bukkit.broadcastMessage(plugin.prefix + ChatColor.RED + "2");

                    } else if (time == 179) {
                        Bukkit.broadcastMessage(plugin.prefix + ChatColor.RED + "1");

                    } else if (time == 180) {
                        Bukkit.broadcastMessage(plugin.prefix + ChatColor.RED + "Server restart now");
                        try {
                            TimeUnit.SECONDS.sleep(1);
                            plugin.shutDown();
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 20, 20);
    }
}
