package com.shtuka.chatstats.managers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private final JavaPlugin plugin;
    private FileConfiguration config;
    private FileConfiguration messagesRu;
    private FileConfiguration messagesEn;
    private String language;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        File configFile = new File(plugin.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
        language = config.getString("language", "en");

        loadLanguageFiles();
    }

    private void loadLanguageFiles() {
        File messagesDir = new File(plugin.getDataFolder(), "messages");
        if (!messagesDir.exists()) {
            messagesDir.mkdirs();
        }

        File ruFile = new File(messagesDir, "ru.yml");
        if (!ruFile.exists()) {
            plugin.saveResource("messages/ru.yml", false);
        }
        messagesRu = YamlConfiguration.loadConfiguration(ruFile);

        File enFile = new File(messagesDir, "en.yml");
        if (!enFile.exists()) {
            plugin.saveResource("messages/en.yml", false);
        }
        messagesEn = YamlConfiguration.loadConfiguration(enFile);
    }

    public String getMessage(String key) {
        FileConfiguration messages = language.equalsIgnoreCase("ru") ? messagesRu : messagesEn;
        String message = messages.getString(key, key);
        return message != null ? message : key;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String lang) {
        this.language = lang;
        config.set("language", lang);
        try {
            config.save(new File(plugin.getDataFolder(), "config.yml"));
            config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save config: " + e.getMessage());
        }
        reloadLanguageFiles();
    }

    public String getBarColor() {
        return config.getString("bar-color", "&6");
    }

    public void setBarColor(String color) {
        config.set("bar-color", color);
        saveConfig();
    }

    public void reloadLanguageFiles() {
        File messagesDir = new File(plugin.getDataFolder(), "messages");
        File ruFile = new File(messagesDir, "ru.yml");
        File enFile = new File(messagesDir, "en.yml");
        
        messagesRu = YamlConfiguration.loadConfiguration(ruFile);
        messagesEn = YamlConfiguration.loadConfiguration(enFile);
    }

    public boolean isAntiSpamEnabled() {
        return config.getBoolean("anti-spam.enabled", false);
    }

    public void setAntiSpamEnabled(boolean enabled) {
        config.set("anti-spam.enabled", enabled);
        saveConfig();
    }

    public int getAntiSpamCooldown() {
        return config.getInt("anti-spam.cooldown-seconds", 5);
    }

    public void setAntiSpamCooldown(int seconds) {
        config.set("anti-spam.cooldown-seconds", seconds);
        saveConfig();
    }

    public int getAntiSpamMaxMessages() {
        return config.getInt("anti-spam.max-messages-per-interval", 3);
    }

    public void setAntiSpamMaxMessages(int max) {
        config.set("anti-spam.max-messages-per-interval", max);
        saveConfig();
    }

    public void saveConfig() {
        try {
            config.save(new File(plugin.getDataFolder(), "config.yml"));
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save config: " + e.getMessage());
        }
    }
}
