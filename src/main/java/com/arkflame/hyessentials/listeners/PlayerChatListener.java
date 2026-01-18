package com.arkflame.hyessentials.listeners;

public class PlayerChatListener {
    
    
    private void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        
        // Check if muted
        if (muteManager.isMuted(uuid)) {
            event.setCancelled(true);
            player.sendMessage(languageManager.getMessage(player, "muted"));
            return;
        }
        
        // Format chat message
        String formatted = chatManager.formatChat(player, event.getMessage());
        event.setMessage(formatted);
    }
}
