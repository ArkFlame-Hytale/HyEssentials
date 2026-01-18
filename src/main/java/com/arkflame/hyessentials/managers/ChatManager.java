package com.arkflame.hyessentials.managers;

import java.util.UUID;

import com.arkflame.hyessentials.HyEssentials;
import com.hypixel.hytale.server.core.entity.entities.Player;

public class ChatManager {
    private final HyEssentials plugin;
    
    public ChatManager(HyEssentials plugin) {
        this.plugin = plugin;
    }
    
    public String formatChat(Player player, String message) {
        UUID uuid = player.getUuid();
        
        // Get player's group
        String groupName = plugin.getPermissionManager().getGroup(uuid);
        PermissionGroup group = plugin.getPermissionManager().getGroupObject(groupName);
        
        String format = plugin.getConfigManager().getChatFormat();
        
        if (group != null) {
            String prefix = group.getPrefix() != null ? group.getPrefix() : "";
            String suffix = group.getSuffix() != null ? group.getSuffix() : "";
            
            format = format.replace("{prefix}", prefix)
                           .replace("{suffix}", suffix);
        } else {
            format = format.replace("{prefix}", "").replace("{suffix}", "");
        }
        
        format = format.replace("{player}", player.getDisplayName())
                       .replace("{message}", message);
        
        // Handle color permissions
        if (!plugin.getPermissionManager().hasPermission(uuid, "essentials.chat.color")) {
            message = stripColors(message);
        }
        
        return colorize(format);
    }
    
    private String colorize(String text) {
        return text.replace("&", "§");
    }
    
    private String stripColors(String text) {
        return text.replaceAll("&[0-9a-fk-or]", "");
    }
}