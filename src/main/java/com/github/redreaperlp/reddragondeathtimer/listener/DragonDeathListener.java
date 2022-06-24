package com.github.redreaperlp.reddragondeathtimer.listener;


import com.github.redreaperlp.reddragondeathtimer.RedDragonDeathTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import static org.bukkit.entity.EntityType.ENDER_DRAGON;


public class DragonDeathListener implements Listener {

    public RedDragonDeathTimer plugin;

    public DragonDeathListener(RedDragonDeathTimer plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void DragonListener(EntityDeathEvent event) {

        if (event.getEntity().getType() == ENDER_DRAGON && !plugin.isPaused) {
            plugin.GamerulesOff();
            plugin.complete = true;
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(plugin.prefix + ChatColor.GOLD + "-------------------------------------------------");
            Bukkit.broadcastMessage(plugin.prefix + ChatColor.GREEN + "You beat the Dragon in: " + ChatColor.YELLOW + plugin.getTime());
            Bukkit.broadcastMessage(plugin.prefix + ChatColor.GOLD + "-------------------------------------------------");
            Bukkit.broadcastMessage("");
        }
    }
}