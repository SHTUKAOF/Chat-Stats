package com.shtuka.chatstats.commands;

import com.shtuka.chatstats.ChatStats;
import com.shtuka.chatstats.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class HelpCommand implements CommandExecutor {
    private final ChatStats plugin;

    public HelpCommand(ChatStats plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            showHelp(sender);
            return true;
        }

        String subcommand = args[0].toLowerCase();

        if (subcommand.equals("stats")) {
            return handleStats(sender, args);
        }

        if (subcommand.equals("top")) {
            return handleTop(sender);
        }

        if (subcommand.equals("setlang")) {
            if (!sender.isOp()) {
                sender.sendMessage(MessageUtil.colorize(plugin.getConfigManager().getMessage("no-permission")));
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage(MessageUtil.colorize("&cUsage: /chatstats setlang <ru|en>"));
                return true;
            }
            String lang = args[1].toLowerCase();
            if (lang.equals("ru") || lang.equals("en")) {
                plugin.getConfigManager().setLanguage(lang);
                if (lang.equals("ru")) {
                    sender.sendMessage(MessageUtil.colorize("&aЯзык изменён на: " + lang));
                } else {
                    sender.sendMessage(MessageUtil.colorize("&aLanguage changed to: " + lang));
                }
            } else {
                sender.sendMessage(MessageUtil.colorize("&cAvailable languages: ru, en"));
            }
            return true;
        }

        if (subcommand.equals("setcolor")) {
            if (!sender.isOp()) {
                sender.sendMessage(MessageUtil.colorize(plugin.getConfigManager().getMessage("no-permission")));
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage(MessageUtil.colorize("&cUsage: /chatstats setcolor <code>"));
                sender.sendMessage(MessageUtil.colorize("&cAvailable colors:"));
                sender.sendMessage(MessageUtil.colorize("&00 &1 1 &2 2 &3 3 &4 4 &5 5 &6 6 &7 7"));
                sender.sendMessage(MessageUtil.colorize("&88 &9 9 &a a &b b &c c &d d &e e &f f"));
                return true;
            }
            String colorCode = args[1].toLowerCase();
            if (isValidColorCode(colorCode)) {
                plugin.getConfigManager().setBarColor("&" + colorCode);
                sender.sendMessage(MessageUtil.colorize("&aInterface color changed to: &" + colorCode + colorCode));
            } else {
                sender.sendMessage(MessageUtil.colorize("&cInvalid color code!"));
                sender.sendMessage(MessageUtil.colorize("&cAvailable: 0-9, a-f"));
            }
            return true;
        }

        if (subcommand.equals("antispam")) {
            if (!sender.isOp()) {
                sender.sendMessage(MessageUtil.colorize(plugin.getConfigManager().getMessage("no-permission")));
                return true;
            }
            if (args.length < 2) {
                showAntiSpamHelp(sender);
                return true;
            }

            String antispamCmd = args[1].toLowerCase();
            
            if (antispamCmd.equals("on")) {
                plugin.getConfigManager().setAntiSpamEnabled(true);
                plugin.getAntiSpamManager().setEnabled(true);
                sender.sendMessage(MessageUtil.colorize("&a✓ " + plugin.getConfigManager().getMessage("enable-antispam")));
                return true;
            }
            
            if (antispamCmd.equals("off")) {
                plugin.getConfigManager().setAntiSpamEnabled(false);
                plugin.getAntiSpamManager().setEnabled(false);
                sender.sendMessage(MessageUtil.colorize("&a✓ " + plugin.getConfigManager().getMessage("disable-antispam")));
                return true;
            }
            
            if (antispamCmd.equals("cooldown")) {
                if (args.length < 3) {
                    sender.sendMessage(MessageUtil.colorize("&cUsage: /chatstats antispam cooldown <seconds>"));
                    return true;
                }
                try {
                    int cooldown = Integer.parseInt(args[2]);
                    if (cooldown < 1) {
                        sender.sendMessage(MessageUtil.colorize("&cCooldown must be greater than 0"));
                        return true;
                    }
                    plugin.getConfigManager().setAntiSpamCooldown(cooldown);
                    plugin.getAntiSpamManager().setCooldownSeconds(cooldown);
                    sender.sendMessage(MessageUtil.colorize("&a✓ " + plugin.getConfigManager().getMessage("set-interval") + " " + cooldown + " sec"));
                } catch (NumberFormatException e) {
                    sender.sendMessage(MessageUtil.colorize(plugin.getConfigManager().getMessage("invalid-number")));
                }
                return true;
            }
            
            if (antispamCmd.equals("maxmsg")) {
                if (args.length < 3) {
                    sender.sendMessage(MessageUtil.colorize("&cUsage: /chatstats antispam maxmsg <count>"));
                    return true;
                }
                try {
                    int maxMsg = Integer.parseInt(args[2]);
                    if (maxMsg < 1) {
                        sender.sendMessage(MessageUtil.colorize("&cMaximum must be greater than 0"));
                        return true;
                    }
                    plugin.getConfigManager().setAntiSpamMaxMessages(maxMsg);
                    plugin.getAntiSpamManager().setMaxMessagesPerInterval(maxMsg);
                    sender.sendMessage(MessageUtil.colorize("&a✓ " + plugin.getConfigManager().getMessage("set-limit") + " " + maxMsg));
                } catch (NumberFormatException e) {
                    sender.sendMessage(MessageUtil.colorize(plugin.getConfigManager().getMessage("invalid-number")));
                }
                return true;
            }
            
            if (antispamCmd.equals("help")) {
                showAntiSpamHelp(sender);
                return true;
            }
            
            sender.sendMessage(MessageUtil.colorize("&cUnknown subcommand. Use: /chatstats antispam help"));
            return true;
        }

        if (subcommand.equals("help")) {
            showHelp(sender);
            return true;
        }

        sender.sendMessage(MessageUtil.colorize("&cUnknown command. Use: /chatstats help"));
        return true;
    }

    private boolean handleStats(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtil.colorize(plugin.getConfigManager().getMessage("only-players")));
            return true;
        }

        String targetPlayer;
        if (args.length > 1) {
            if (!player.isOp()) {
                sender.sendMessage(MessageUtil.colorize(plugin.getConfigManager().getMessage("no-permission")));
                return true;
            }
            targetPlayer = args[1];
        } else {
            targetPlayer = player.getName();
        }

        int messageCount = plugin.getStatsManager().getMessageCount(targetPlayer);
        String barColor = plugin.getConfigManager().getBarColor();
        
        player.sendMessage(MessageUtil.colorize(barColor + "&m" + "=".repeat(50)));
        player.sendMessage(MessageUtil.colorize(barColor + plugin.getConfigManager().getMessage("stats-title")));
        player.sendMessage(MessageUtil.colorize("&f" + plugin.getConfigManager().getMessage("player") + ": &e" + targetPlayer));
        player.sendMessage(MessageUtil.colorize("&f" + plugin.getConfigManager().getMessage("messages") + ": &e" + messageCount));
        player.sendMessage(MessageUtil.colorize(barColor + "&m" + "=".repeat(50)));

        return true;
    }

    private boolean handleTop(CommandSender sender) {
        int limit = 10;
        var topPlayers = plugin.getStatsManager().getTopPlayers(limit);
        
        String barColor = plugin.getConfigManager().getBarColor();
        sender.sendMessage(MessageUtil.colorize(barColor + "&m" + "=".repeat(50)));
        sender.sendMessage(MessageUtil.colorize(barColor + "&l" + plugin.getConfigManager().getMessage("top-title")));
        sender.sendMessage("");

        if (topPlayers.isEmpty()) {
            sender.sendMessage(MessageUtil.colorize(plugin.getConfigManager().getMessage("no-stats")));
        } else {
            int position = 1;
            for (Map.Entry<String, Integer> entry : topPlayers) {
                String medal = getMedal(position);
                sender.sendMessage(MessageUtil.colorize(barColor + medal + " #" + position + " &f" + 
                    entry.getKey() + " &6- &f" + entry.getValue() + " " + 
                    plugin.getConfigManager().getMessage("messages")));
                position++;
            }
        }

        sender.sendMessage("");
        sender.sendMessage(MessageUtil.colorize(barColor + "&m" + "=".repeat(50)));
        return true;
    }

    private void showHelp(CommandSender sender) {
        String barColor = plugin.getConfigManager().getBarColor();
        sender.sendMessage(MessageUtil.colorize(barColor + "&m" + "=".repeat(50)));
        sender.sendMessage(MessageUtil.colorize(barColor + "&lChat-Stats &f" + plugin.getConfigManager().getMessage("help-title")));
        sender.sendMessage("");
        
        sender.sendMessage(MessageUtil.colorize("&e" + plugin.getConfigManager().getMessage("commands-for-all")));
        sender.sendMessage(MessageUtil.colorize("&f/chatstats stats &7- " + plugin.getConfigManager().getMessage("show-stats")));
        sender.sendMessage(MessageUtil.colorize("&f/chatstats top &7- " + plugin.getConfigManager().getMessage("show-top")));
        sender.sendMessage(MessageUtil.colorize("&f/chatstats help &7- " + plugin.getConfigManager().getMessage("show-help")));
        
        if (sender.isOp()) {
            sender.sendMessage("");
            sender.sendMessage(MessageUtil.colorize("&e" + plugin.getConfigManager().getMessage("commands-for-admins")));
            sender.sendMessage(MessageUtil.colorize("&f/chatstats stats <player> &7- " + plugin.getConfigManager().getMessage("show-player-stats")));
            sender.sendMessage(MessageUtil.colorize("&f/chatstats setlang <ru/en> &7- " + plugin.getConfigManager().getMessage("change-language")));
            sender.sendMessage(MessageUtil.colorize("&f/chatstats setcolor <code> &7- " + plugin.getConfigManager().getMessage("change-color")));
            sender.sendMessage(MessageUtil.colorize("&f/chatstats antispam <on/off/cooldown/maxmsg> &7- " + plugin.getConfigManager().getMessage("manage-antispam")));
        }
        
        sender.sendMessage("");
        sender.sendMessage(MessageUtil.colorize("&7" + plugin.getConfigManager().getMessage("author") + ": &fSHTUKA"));
        sender.sendMessage(MessageUtil.colorize(barColor + "&m" + "=".repeat(50)));
    }

    private boolean isValidColorCode(String code) {
        return code.matches("[0-9a-f]");
    }

    private String getMedal(int position) {
        return switch (position) {
            case 1 -> "🥇";
            case 2 -> "🥈";
            case 3 -> "🥉";
            default -> "  ";
        };
    }

    private void showAntiSpamHelp(CommandSender sender) {
        String barColor = plugin.getConfigManager().getBarColor();
        boolean antiSpamEnabled = plugin.getAntiSpamManager().isEnabled();
        int cooldown = plugin.getAntiSpamManager().getCooldownSeconds();
        int maxMsg = plugin.getAntiSpamManager().getMaxMessagesPerInterval();
        
        sender.sendMessage(MessageUtil.colorize(barColor + "&m" + "=".repeat(50)));
        sender.sendMessage(MessageUtil.colorize(barColor + "&l" + plugin.getConfigManager().getMessage("anti-spam-help")));
        sender.sendMessage("");
        
        sender.sendMessage(MessageUtil.colorize("&e" + plugin.getConfigManager().getMessage("current-settings") + ":"));
        sender.sendMessage(MessageUtil.colorize("&f  " + plugin.getConfigManager().getMessage("status") + ": " + 
            (antiSpamEnabled ? "&a" + plugin.getConfigManager().getMessage("enabled") : "&c" + plugin.getConfigManager().getMessage("disabled"))));
        sender.sendMessage(MessageUtil.colorize("&f  " + plugin.getConfigManager().getMessage("interval") + ": &e" + cooldown + " sec"));
        sender.sendMessage(MessageUtil.colorize("&f  " + plugin.getConfigManager().getMessage("max-messages") + ": &e" + maxMsg));
        sender.sendMessage("");
        
        sender.sendMessage(MessageUtil.colorize("&e" + plugin.getConfigManager().getMessage("how-it-works") + ":"));
        sender.sendMessage(MessageUtil.colorize("&f  Player can send max &e" + maxMsg + " &fmessages per &e" + cooldown + " &fseconds"));
        sender.sendMessage(MessageUtil.colorize("&f  If limit exceeded - message will be blocked"));
        sender.sendMessage(MessageUtil.colorize("&f  Blocked messages don't count toward statistics"));
        sender.sendMessage("");
        
        sender.sendMessage(MessageUtil.colorize("&e" + plugin.getConfigManager().getMessage("available-commands") + ":"));
        sender.sendMessage(MessageUtil.colorize("&f  /chatstats antispam on &7- " + plugin.getConfigManager().getMessage("enable-antispam")));
        sender.sendMessage(MessageUtil.colorize("&f  /chatstats antispam off &7- " + plugin.getConfigManager().getMessage("disable-antispam")));
        sender.sendMessage(MessageUtil.colorize("&f  /chatstats antispam cooldown <sec> &7- " + plugin.getConfigManager().getMessage("set-interval")));
        sender.sendMessage(MessageUtil.colorize("&f  /chatstats antispam maxmsg <count> &7- " + plugin.getConfigManager().getMessage("set-limit")));
        sender.sendMessage("");
        
        sender.sendMessage(MessageUtil.colorize("&e" + plugin.getConfigManager().getMessage("examples") + ":"));
        sender.sendMessage(MessageUtil.colorize("&f  /chatstats antispam cooldown 5 &7/ Interval 5 seconds"));
        sender.sendMessage(MessageUtil.colorize("&f  /chatstats antispam maxmsg 3 &7/ Maximum 3 messages"));
        sender.sendMessage(MessageUtil.colorize("&f  /chatstats antispam cooldown 10 &7/ Strict protection (10 sec)"));
        sender.sendMessage("");
        
        sender.sendMessage(MessageUtil.colorize(barColor + "&m" + "=".repeat(50)));
    }
}
