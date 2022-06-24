package com.github.redreaperlp.reddragondeathtimer.handler;

import com.github.redreaperlp.reddragondeathtimer.RedDragonDeathTimer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ActionBarHandler {

    public RedDragonDeathTimer plugin;

    public ActionBarHandler(RedDragonDeathTimer plugin) {
        this.plugin = plugin;
    }

    public void start() {

        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (!plugin.isPaused) {
                plugin.sec++;
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + plugin.getTime()));
                }
            } else if (plugin.complete) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GOLD + plugin.getTime() + " Challenge Completed"));
                }
            } else if (plugin.failed) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.YELLOW + plugin.getTime() + ChatColor.DARK_RED + "Challenge failed"));
                }
            } else {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.YELLOW + plugin.getTime() + ChatColor.RED + "Challenge Paused"));
                }
            }
        }, 20, 20);
    }
}
