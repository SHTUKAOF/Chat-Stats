package com.shtuka.chatstats.commands;

import com.shtuka.chatstats.ChatStats;
import com.shtuka.chatstats.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class TopCommand implements CommandExecutor {
    private final ChatStats plugin;

    public TopCommand(ChatStats plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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

    private String getMedal(int position) {
        return switch (position) {
            case 1 -> "🥇";
            case 2 -> "🥈";
            case 3 -> "🥉";
            default -> "  ";
        };
    }
}
