package com.github.redreaperlp.reddragondeathtimer;

import com.github.redreaperlp.reddragondeathtimer.commands.Timer;
import com.github.redreaperlp.reddragondeathtimer.handler.ActionBarHandler;
import com.github.redreaperlp.reddragondeathtimer.listener.DragonDeathListener;
import com.github.redreaperlp.reddragondeathtimer.listener.MoveListener;
import com.github.redreaperlp.reddragondeathtimer.listener.PlayerDamageListener;
import com.github.redreaperlp.reddragondeathtimer.listener.PlayerDeath;
import com.github.redreaperlp.redpermsapi.Managers.PermissionManager;
import com.github.redreaperlp.redpermsapi.RedPermsAPI;
import org.bukkit.*;
import org.bukkit.boss.BossBar;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RedDragonDeathTimer extends JavaPlugin implements Listener {


    public String prefix = "§7[§bDeathTimer§7] ";

    public static RedDragonDeathTimer getInstance() {
        return instance;
    }

    public static RedDragonDeathTimer instance;
    public boolean autostart = false;
    public boolean complete = false;
    public boolean isPaused = true;
    public BossBar bossBar;
    public boolean failed = false;
    public boolean hint = false;
    public boolean wait = false;
    public boolean firstday;
    public boolean redPermsAPI = false;
    public int sec = 0;
    public int delay;


    ConsoleCommandSender console = Bukkit.getConsoleSender();
    public ActionBarHandler actionBarHandler;

    @Override
    public void onEnable() {
        instance = this;

        console.sendMessage(prefix + "§b-----------------------------------------------------",
                prefix + "§aPlugin has been enabled!",
                prefix + "§ayou are using DeathTimer version §e" + getDescription().getVersion(),
                prefix + "§amade by §eRedReaperLp",
                prefix + "§awebsites: " + getDescription().getWebsite(),
                prefix + "§b-----------------------------------------------------");

        if (getServer().getPluginManager().getPlugin("RedPermsAPI") != null) {
            console.sendMessage(prefix + "§aRedPermsAPI has been detected!");
            PermissionManager permissionManager = new PermissionManager();
            permissionManager.addPermission(null, List.of(
                    "deathtimer_use",
                    "deathtimer_toggle_timer",
                    "deathtimer_toggle_hint",
                    "deathtimer_toggle_auto",
                    "deathtimer_reset",
                    "deathtimer_all"));
            redPermsAPI = true;
        }
        getCommand("timer").setExecutor(new Timer(this));
        getCommand("timer").setTabCompleter(new Timer(this));

        firstday = getConfig().getBoolean("firstday", true);
        sec = getConfig().getInt("seconds", 0);
        delay = getConfig().getInt("delay", 40);
        hint = getConfig().getBoolean("hint", false);
        autostart = getConfig().getBoolean("autotoggle", false);
        actionBarHandler = new ActionBarHandler(this);
        actionBarHandler.start();

        Bukkit.getPluginManager().registerEvents(new PlayerDamageListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeath(this), this);
        Bukkit.getPluginManager().registerEvents(new DragonDeathListener(this), this);
        Bukkit.getPluginManager().registerEvents(new MoveListener(this), this);
        Bukkit.getPluginManager().registerEvents(this, this);
        if (delay >= 0 && delay <= 20) {
            delay = 40;
        }
    }

    @Override
    public void onDisable() {
        getConfig().set("autotoggle", autostart);
        getConfig().set("firstday", firstday);
        getConfig().set("delay", delay);
        getConfig().set("seconds", sec);
        getConfig().set("hint", hint);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player != null && bossBar != null) {
                bossBar.removeAll();
            }
        }
        GamerulesOff();
        saveConfig();

    }


    public String getTime() {
        int days = sec / 86400;
        int hours = (sec % 86400) / 3600;
        int minutes = ((sec % 86400) % 3600) / 60;
        int seconds = ((sec % 86400) % 3600) % 60;

        return String.format("%d T  %d H  %d Min  %d Sec    ", days, hours, minutes, seconds);

    }

    public void GamerulesOn() {
        Bukkit.getWorld("world").setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
        Bukkit.getWorld("world").setGameRule(GameRule.DO_WEATHER_CYCLE, true);
        Bukkit.getWorld("world").setGameRule(GameRule.RANDOM_TICK_SPEED, 3);
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setGameMode(GameMode.SURVIVAL);
        }
    }

    public void GamerulesOff() {
        Bukkit.getWorld("world").setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        Bukkit.getWorld("world").setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        Bukkit.getWorld("world").setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
        isPaused = true;
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setGameMode(GameMode.SPECTATOR);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (firstday) {
            Bukkit.getWorld("world").setTime(1000);
            firstday = false;
            getConfig().set("firstday", firstday);
            saveConfig();
        }
        if (isPaused) {
            GamerulesOff();
        } else {
            GamerulesOn();
        }
    }

    public void waitOff() {
        wait = false;
    }

    @Override
    public void onLoad() {
        if (!getConfig().contains("isReset")) {
            getConfig().set("isReset", false);
            saveConfig();
            return;
        }
        if (getConfig().getBoolean("isReset")) {
            File world = new File(Bukkit.getWorldContainer(), "world");
            File nether = new File(Bukkit.getWorldContainer(), "world_nether");
            File end = new File(Bukkit.getWorldContainer(), "world_the_end");
            File data = new File(Bukkit.getWorldContainer(), "world/playerdata");


            DeleteDirectory(world);
            DeleteDirectory(nether);
            DeleteDirectory(end);

            world.mkdirs();
            nether.mkdirs();
            end.mkdirs();
            data.mkdirs();

            getConfig().set("isReset", false);
            saveConfig();
        }
    }

    public void shutDown() {
        if (redPermsAPI) {
            PermissionManager permissionManager = new PermissionManager();
            for (Player player : Bukkit.getOnlinePlayers()) {
                List<String> permissions = RedPermsAPI.instance.getConfig().getStringList("permissions");
                List<String> playerPerms = new ArrayList<>();
                for (String permission : permissions) {
                    if (permissionManager.askPermission(player.getName(), permission, null)) {
                        playerPerms.add(permission);
                    }
                }
                player.kickPlayer(this.prefix + ChatColor.RED + "Map Reset");
                for (String permission : playerPerms) {
                    permissionManager.givePlayerPermission(player.getName(), permission, null, true);
                }
            }
        } else {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.kickPlayer(this.prefix + ChatColor.RED + "Map Reset");
            }
        }
        this.getInstance().sec = 0;
        this.getInstance().wait = true;
        this.getInstance().failed = false;
        this.getInstance().firstday = true;
        this.getInstance().GamerulesOff();
        this.getInstance().getConfig().set("isReset", true);
        this.getInstance().getConfig().set("seconds", 0);
        Bukkit.spigot().restart();
    }

    boolean DeleteDirectory(File dirToDel) {
        File[] files = dirToDel.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    DeleteDirectory(f);
                } else {
                    f.delete();
                }
            }
        }
        return dirToDel.delete();
    }
}
