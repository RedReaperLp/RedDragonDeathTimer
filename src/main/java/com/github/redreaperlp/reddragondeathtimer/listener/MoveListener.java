package com.github.redreaperlp.reddragondeathtimer.listener;

import com.github.redreaperlp.reddragondeathtimer.RedDragonDeathTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

    public RedDragonDeathTimer plugin;

    public MoveListener(RedDragonDeathTimer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (plugin.isPaused && !plugin.wait && plugin.autostart && !plugin.failed && !plugin.complete) {
            if (event.getTo() == null) {
                return;
            }

            Location fromLoc = event.getFrom().clone();
            Location toLoc = event.getTo().clone();

            fromLoc.setPitch(0);
            fromLoc.setYaw(0);

            toLoc.setPitch(0);
            toLoc.setYaw(0);

            if (fromLoc.distance(toLoc) > 0.05) {
                plugin.isPaused = false;
                if (plugin.sec <= 10) {
                    Bukkit.broadcastMessage(plugin.prefix + ChatColor.GREEN + "challenge started");
                    plugin.GamerulesOn();
                } else {
                    Bukkit.broadcastMessage(plugin.prefix + ChatColor.GREEN + "challenge resumed");
                    plugin.GamerulesOn();
                }
            }
        }
    }
}
