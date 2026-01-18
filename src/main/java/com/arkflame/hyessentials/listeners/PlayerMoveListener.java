package com.arkflame.hyessentials.listeners;

public class PlayerMoveListener {
    
    
    private void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        
        // Cancel teleport if player moves during warmup
        if (configManager.isCancelOnMove()) {
            if (teleportManager.hasPendingTeleport(player.getUniqueId())) {
                Vector3 from = event.getFrom();
                Vector3 to = event.getTo();
                
                if (!from.equals(to)) {
                    teleportManager.cancelTeleport(player.getUniqueId());
                    player.sendMessage(languageManager.getMessage(player, "teleport_cancelled_move"));
                }
            }
        }
    }
}
