package com.github.redreaperlp.reddragondeathtimer.commands;

import com.github.redreaperlp.reddragondeathtimer.RedDragonDeathTimer;
import com.github.redreaperlp.reddragondeathtimer.handler.ActionBarHandler;
import com.github.redreaperlp.reddragondeathtimer.listener.PlayerDeath;
import com.github.redreaperlp.redpermsapi.Managers.PermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;


public class Timer implements CommandExecutor, TabCompleter {

    public RedDragonDeathTimer plugin;

    public Timer(RedDragonDeathTimer OurMain) {
        plugin = OurMain;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if (plugin.redPermsAPI) {
            PermissionManager permissionManager = new PermissionManager();
            if (permissionManager.askPermission(null, "deathtimer_all", sender) == false) {
                if (permissionManager.askPermission(null, "deathtimer_use", sender) == false) {
                    sender.sendMessage(plugin.prefix + "§cYou don't have permission to use this command!");
                    return true;
                }
            }
        } else {
            if (!sender.isOp()) {
                sender.sendMessage(plugin.prefix + "§cYou don't have permission to use this command!");
                return true;
            }
        }

        if (args.length == 0) {
            sender.sendMessage("");
            sender.sendMessage(plugin.prefix + ChatColor.RED + "enter a command:");
            sender.sendMessage(plugin.prefix + ChatColor.YELLOW + "toggle");
            sender.sendMessage(plugin.prefix + ChatColor.YELLOW + "reset");
            sender.sendMessage(plugin.prefix + ChatColor.YELLOW + "auto");
            sender.sendMessage(plugin.prefix + ChatColor.YELLOW + "hint");
            sender.sendMessage("");
            return true;
        }
        switch (args[0].toLowerCase(Locale.ROOT)) {
            case "toggle":
                toggle(sender);
                break;
            case "reset":
                reset(sender);
                break;
            case "auto":
                auto(sender);
                break;
            case "hint":
                hint(sender);
                break;
            default:
                sender.sendMessage(plugin.prefix + ChatColor.DARK_RED + "this command doesn't exist!");
                break;
        }
        return true;
    }

    private void toggle(CommandSender sender) {
        if (plugin.redPermsAPI) {
            PermissionManager permissionManager = new PermissionManager();
            if (permissionManager.askPermission(null, "deathtimer_all", sender) == false) {
                if (permissionManager.askPermission(null, "deathtimer_toggle_timer", sender) == false) {
                    sender.sendMessage(plugin.prefix + "§cYou don't have permission to use this command!");
                    return;
                }
            }
        } else {
            if (!sender.isOp()) {
                sender.sendMessage(plugin.prefix + "§cYou don't have permission to use this command!");
                return;
            }
        }

        if (!plugin.failed) {
            plugin.isPaused = !plugin.isPaused;
            if (plugin.isPaused) {
                plugin.wait = true;
                plugin.GamerulesOff();
                Bukkit.broadcastMessage(plugin.prefix + ChatColor.RED + "challenge Paused");
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, plugin::waitOff, plugin.delay);
            } else if (plugin.sec <= 10) {
                plugin.GamerulesOn();
                Bukkit.broadcastMessage(plugin.prefix + ChatColor.GREEN + "challenge started");
            } else {
                plugin.GamerulesOn();
                Bukkit.broadcastMessage(plugin.prefix + ChatColor.GREEN + "challenge resumed");
            }
        } else {
            sender.sendMessage("");
            sender.sendMessage(plugin.prefix + ChatColor.YELLOW + "I´m sorry, but the challenge" + ChatColor.RED + " failed");
            sender.sendMessage(plugin.prefix + ChatColor.YELLOW + "you can use " + ChatColor.RED + "- /timer reset - " + ChatColor.YELLOW + "to continue playing on");
            sender.sendMessage(plugin.prefix + ChatColor.YELLOW + "another world!");
            sender.sendMessage("");
        }
    }
    boolean isReset = false;
    private void reset(CommandSender sender) {
        if (plugin.redPermsAPI) {
            PermissionManager permissionManager = new PermissionManager();
            if (permissionManager.askPermission(null, "deathtimer_all", sender) == false) {
                if (permissionManager.askPermission(null, "deathtimer_reset", sender) == false) {
                    sender.sendMessage(plugin.prefix + "§cYou don't have permission to use this command!");
                    return;
                }
            }
        } else {
            if (!sender.isOp()) {
                sender.sendMessage(plugin.prefix + "§cYou don't have permission to use this command!");
                return;
            }
        }

        PlayerDeath bossBar = new PlayerDeath(plugin);
        if (isReset) {
            sender.sendMessage("");
            sender.sendMessage(plugin.prefix + ChatColor.RED + "i´m already about to reset!");


        } else {
            isReset = true;
            Bukkit.getScheduler().cancelTasks(RedDragonDeathTimer.instance);

            sender.sendMessage(plugin.prefix + ChatColor.GREEN + "the challenge has been reset");
            bossBar.time = 169;
            bossBar.total = 180;
            ActionBarHandler actionBarHandler = new ActionBarHandler(plugin);
            actionBarHandler.start();

            plugin.GamerulesOff();
            plugin.failed = true;
            bossBar.bossBar();
        }

    }

    private void auto(CommandSender sender) {
        if (plugin.redPermsAPI) {
            PermissionManager permissionManager = new PermissionManager();
            if (permissionManager.askPermission(null, "deathtimer_all", sender) == false) {
                if (permissionManager.askPermission(null, "deathtimer_toggle_auto", sender) == false) {
                    sender.sendMessage(plugin.prefix + "§cYou don't have permission to use this command!");
                    return;
                }
            }
        } else {
            if (!sender.isOp()) {
                sender.sendMessage(plugin.prefix + "§cYou don't have permission to use this command!");
                return;
            }
        }

        plugin.autostart = !plugin.autostart;
        if (plugin.autostart) {
            sender.sendMessage(plugin.prefix + ChatColor.GREEN + "Activated auto start");
        } else {
            sender.sendMessage(plugin.prefix + ChatColor.RED + "Deactivated auto start");
        }
    }


    private void hint(CommandSender sender) {

        if (plugin.redPermsAPI) {
            PermissionManager permissionManager = new PermissionManager();
            if (permissionManager.askPermission(null, "deathtimer_all", sender) == false) {
                if (permissionManager.askPermission(null, "deathtimer_toggle_hint", sender) == false) {
                    sender.sendMessage(plugin.prefix + "§cYou don't have permission to use this command!");
                    return;
                }
            }
        } else {
            if (!sender.isOp()) {
                sender.sendMessage(plugin.prefix + "§cYou don't have permission to use this command!");
                return;
            }
        }

        plugin.hint = !plugin.hint;
        if (plugin.hint) {
            sender.sendMessage(plugin.prefix + ChatColor.GREEN + "Activated Damage-Hint");
        } else {
            sender.sendMessage(plugin.prefix + ChatColor.RED + "Deactivated Damage-Hint");
        }
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (plugin.redPermsAPI) {
            PermissionManager permissionManager = new PermissionManager();
            if (permissionManager.askPermission(null, "deathtimer_all", sender) == false) {
                if (permissionManager.askPermission(null, "deathtimer_use", sender) == false) {
                    return List.of();
                }
            }
        } else {
            if (!sender.isOp()) {
                return List.of();
            }
        }

        List<String> suggest = new ArrayList<>();
        String value = args[0].toLowerCase(Locale.ROOT);

        if (plugin.redPermsAPI) {
            PermissionManager p = new PermissionManager();
            suggest = Stream.of(p.askPermission(null, "deathtimer_toggle_timer", sender) || p.askPermission(null, "deathtimer_all", sender) ? "toggle" : "",
                    p.askPermission(null, "deathtimer_reset", sender) || p.askPermission(null, "deathtimer_all", sender) ? "reset" : "",
                    p.askPermission(null, "deathtimer_toggle_auto", sender) || p.askPermission(null, "deathtimer_all", sender) ? "auto" : "",
                    p.askPermission(null, "deathtimer_toggle_hint", sender) || p.askPermission(null, "deathtimer_all", sender) ? "hint" : "").filter(s -> s.startsWith(value)).toList();
            return suggest;
        }

        suggest = Stream.of("toggle", "reset", "auto", "hint").filter(s -> s.startsWith(value)).collect(java.util.stream.Collectors.toList());
        return args.length == 1 ? suggest : List.of();
    }
}

