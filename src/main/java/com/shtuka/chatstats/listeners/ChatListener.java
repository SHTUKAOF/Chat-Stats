package com.shtuka.chatstats.listeners;

import com.shtuka.chatstats.ChatStats;
import com.shtuka.chatstats.utils.MessageUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    private final ChatStats plugin;

    public ChatListener(ChatStats plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String playerName = event.getPlayer().getName();
        
        if (plugin.getAntiSpamManager().isSpam(playerName)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(MessageUtil.colorize("&c⚠ You are writing too fast! Wait before your next message."));
            return;
        }
        
        plugin.getStatsManager().addMessage(playerName);
    }
}
