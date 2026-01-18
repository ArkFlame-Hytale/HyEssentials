package com.arkflame.hyessentials.listeners;

public class PlayerQuitListener {
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        
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
