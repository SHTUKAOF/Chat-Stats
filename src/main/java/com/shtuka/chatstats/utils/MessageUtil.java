package com.shtuka.chatstats.utils;

import net.md_5.bungee.api.ChatColor;

public class MessageUtil {
    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String getHeader() {
        return colorize("&6&m" + "=".repeat(50));
    }

    public static String getFooter() {
        return colorize("&6&m" + "=".repeat(50));
    }
}
