package com.shtuka.chatstats.commands;

import com.shtuka.chatstats.ChatStats;
import com.shtuka.chatstats.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand implements CommandExecutor {
    private final ChatStats plugin;

    public StatsCommand(ChatStats plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(MessageUtil.colorize(plugin.getConfigManager().getMessage("only-players")));
            return true;
        }

        String targetPlayer;
        if (args.length > 0) {
            if (!player.isOp()) {
                sender.sendMessage(MessageUtil.colorize(plugin.getConfigManager().getMessage("no-permission")));
                return true;
            }
            targetPlayer = args[0];
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
}
