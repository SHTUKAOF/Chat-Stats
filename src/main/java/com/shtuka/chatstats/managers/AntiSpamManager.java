package com.shtuka.chatstats.managers;

import java.util.HashMap;
import java.util.Map;

public class AntiSpamManager {
    private final Map<String, Long> lastMessageTime = new HashMap<>();
    private final Map<String, Integer> messageCount = new HashMap<>();
    private boolean enabled;
    private int cooldownSeconds;
    private int maxMessagesPerInterval;

    public AntiSpamManager(boolean enabled, int cooldownSeconds, int maxMessagesPerInterval) {
        this.enabled = enabled;
        this.cooldownSeconds = cooldownSeconds;
        this.maxMessagesPerInterval = maxMessagesPerInterval;
    }

    public boolean isSpam(String playerName) {
        if (!enabled) return false;

        long currentTime = System.currentTimeMillis();
        long lastTime = lastMessageTime.getOrDefault(playerName, 0L);
        int count = messageCount.getOrDefault(playerName, 0);

        if (currentTime - lastTime > cooldownSeconds * 1000L) {
            messageCount.put(playerName, 1);
            lastMessageTime.put(playerName, currentTime);
            return false;
        }

        if (count >= maxMessagesPerInterval) {
            return true;
        }

        messageCount.put(playerName, count + 1);
        lastMessageTime.put(playerName, currentTime);
        return false;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setCooldownSeconds(int seconds) {
        this.cooldownSeconds = seconds;
    }

    public int getCooldownSeconds() {
        return cooldownSeconds;
    }

    public void setMaxMessagesPerInterval(int max) {
        this.maxMessagesPerInterval = max;
    }

    public int getMaxMessagesPerInterval() {
        return maxMessagesPerInterval;
    }

    public void clearPlayerData(String playerName) {
        lastMessageTime.remove(playerName);
        messageCount.remove(playerName);
    }
}
