package com.github.redreaperlp.reddragondeathtimer.listener;

import com.github.redreaperlp.reddragondeathtimer.RedDragonDeathTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageListener implements Listener {

    public RedDragonDeathTimer plugin;

    public PlayerDamageListener(RedDragonDeathTimer plugin) {
        this.plugin = plugin;
    }

    public String Cause;
    public double damage;
    public Player players;
    public double healths;

    @EventHandler
    public void DamageListener(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player player && plugin.hint && !plugin.isPaused && !plugin.failed) {
            damage = Math.round(e.getFinalDamage());
            players = player;
            healths = player.getHealth();
            if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                Cause = "Falling";
            } else if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                Cause = "Attacked by an Entity";
            } else if (e.getCause() == EntityDamageEvent.DamageCause.CONTACT) {
                Cause = "something sharp";
            } else if (e.getCause() == EntityDamageEvent.DamageCause.FIRE || e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
                Cause = "burning";
            } else if (e.getCause() == EntityDamageEvent.DamageCause.HOT_FLOOR) {
                Cause = "Standing on MagmaBlock";
            } else {
                Cause = e.getCause().toString();
            }
            Bukkit.getScheduler().runTaskLater(plugin, this::getHealth, 5);


        }
    }

    public void getHealth() {
        double health = Math.ceil(players.getHealth());
        damage = Math.round(healths - health);
        if (players.getHealth() <= 0 || players.getHealth() == healths) {
            return;
        }
        if (damage <= 0.4444) {
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(plugin.prefix + ChatColor.RED + players.getName() + ChatColor.GREEN + " took damage through:");
            Bukkit.broadcastMessage(plugin.prefix + ChatColor.YELLOW + Cause);
            Bukkit.broadcastMessage(plugin.prefix + ChatColor.GREEN + "has " + ChatColor.RED + health + ChatColor.GREEN + " Lives");
        } else {
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(plugin.prefix + ChatColor.RED + players.getName() + ChatColor.GREEN + " has taken " + ChatColor.RED + Math.ceil(healths - health) + ChatColor.GREEN + " damage through: ");
            Bukkit.broadcastMessage(plugin.prefix + ChatColor.YELLOW + Cause);
            Bukkit.broadcastMessage(plugin.prefix + ChatColor.GREEN + "has " + ChatColor.RED + health + ChatColor.GREEN + " Lives");
        }
        Bukkit.broadcastMessage("");
    }
}
