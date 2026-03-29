package com.shtuka.chatstats;

import com.shtuka.chatstats.commands.HelpCommand;
import com.shtuka.chatstats.commands.StatsCommand;
import com.shtuka.chatstats.commands.TopCommand;
import com.shtuka.chatstats.listeners.ChatListener;
import com.shtuka.chatstats.managers.AntiSpamManager;
import com.shtuka.chatstats.managers.ConfigManager;
import com.shtuka.chatstats.managers.StatsManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatStats extends JavaPlugin {
    private static ChatStats instance;
    private ConfigManager configManager;
    private StatsManager statsManager;
    private AntiSpamManager antiSpamManager;

    @Override
    public void onEnable() {
        instance = this;
        
        configManager = new ConfigManager(this);
        configManager.loadConfig();
        
        statsManager = new StatsManager(this);
        statsManager.loadStats();
        
        antiSpamManager = new AntiSpamManager(
            configManager.isAntiSpamEnabled(),
            configManager.getAntiSpamCooldown(),
            configManager.getAntiSpamMaxMessages()
        );
        
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        
        getCommand("chatstats").setExecutor(new HelpCommand(this));
        
        getLogger().info("ChatStats plugin loaded!");
    }

    @Override
    public void onDisable() {
        if (statsManager != null) {
            statsManager.saveStats();
        }
        getLogger().info("ChatStats plugin unloaded!");
    }

    public static ChatStats getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public StatsManager getStatsManager() {
        return statsManager;
    }

    public AntiSpamManager getAntiSpamManager() {
        return antiSpamManager;
    }
}
