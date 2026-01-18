package com.arkflame.hyessentials.listeners;

public class PlayerJoinListener {
    private void onPlayerJoin(PlayerReadyEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        
        // Load player data asynchronously
        taskRunner.runAsync(() -> {
            userDataManager.loadUser(uuid);
            
            // Check if first join
            boolean isFirstJoin = userDataManager.isFirstJoin(uuid);
            
            // Sync back to main thread for messages
            taskRunner.runSync(() -> {
                // Custom join message
                if (configManager.isJoinMessageEnabled()) {
                    String joinMsg = languageManager.getMessage(player, "join_message")
                        .replace("{player}", player.getName());
                    broadcastMessage(joinMsg);
                }
                
                // First join message
                if (isFirstJoin && configManager.isFirstJoinEnabled()) {
                    String firstJoinMsg = languageManager.getMessage(player, "first_join")
                        .replace("{player}", player.getName());
                    broadcastMessage(firstJoinMsg);
                    userDataManager.setFirstJoin(uuid, false);
                }
                
                // MOTD
                if (configManager.isMotdEnabled()) {
                    String motd = languageManager.getMessage(player, "motd");
                    player.sendMessage(motd);
                }
            });
        });
    }
}
