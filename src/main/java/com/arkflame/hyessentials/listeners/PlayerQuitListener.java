package com.arkflame.hyessentials.listeners;

import java.util.UUID;

import com.hypixel.hytale.server.core.entity.entities.Player;

public class PlayerQuitListener {
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUuid();
        
        // Custom leave message
        if (configManager.isLeaveMessageEnabled()) {
            String leaveMsg = languageManager.getMessage(player, "leave_message")
                .replace("{player}", player.getName());
            broadcastMessage(leaveMsg);
        }
        
        // Save player data asynchronously
        taskRunner.runAsync(() -> {
            userDataManager.saveUser(uuid);
        });
        
        // Cancel any pending teleports
        teleportManager.cancelTeleport(uuid);
    }
}
