package com.shtuka.chatstats.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class StatsManager {
    private final JavaPlugin plugin;
    private FileConfiguration statsConfig;
    private final Map<String, Integer> playerStats = new HashMap<>();

    public StatsManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadStats() {
        File statsFile = new File(plugin.getDataFolder(), "stats.yml");
        if (!statsFile.exists()) {
            try {
                statsFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to create stats file: " + e.getMessage());
            }
        }
        
        statsConfig = YamlConfiguration.loadConfiguration(statsFile);
        
        if (statsConfig.contains("players")) {
            for (String playerName : statsConfig.getConfigurationSection("players").getKeys(false)) {
                int messages = statsConfig.getInt("players." + playerName + ".messages", 0);
                playerStats.put(playerName, messages);
            }
        }
    }

    public void saveStats() {
        try {
            for (Map.Entry<String, Integer> entry : playerStats.entrySet()) {
                statsConfig.set("players." + entry.getKey() + ".messages", entry.getValue());
            }
            statsConfig.save(new File(plugin.getDataFolder(), "stats.yml"));
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save stats: " + e.getMessage());
        }
    }

    public void addMessage(String playerName) {
        playerStats.put(playerName, playerStats.getOrDefault(playerName, 0) + 1);
    }

    public int getMessageCount(String playerName) {
        return playerStats.getOrDefault(playerName, 0);
    }

    public List<Map.Entry<String, Integer>> getTopPlayers(int limit) {
        return playerStats.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(limit)
                .toList();
    }

    public Map<String, Integer> getAllStats() {
        return new HashMap<>(playerStats);
    }
}
