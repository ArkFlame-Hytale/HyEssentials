package com.arkflame.hyessentials.listeners;

import com.hypixel.hytale.builtin.hytalegenerator.fields.FastNoiseLite.Vector3;
import com.hypixel.hytale.server.core.entity.entities.Player;

public class PlayerMoveListener {
    
    
    private void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        
        // Cancel teleport if player moves during warmup
        if (configManager.isCancelOnMove()) {
            if (teleportManager.hasPendingTeleport(player.getUuid())) {
                Vector3 from = event.getFrom();
                Vector3 to = event.getTo();
                
                if (!from.equals(to)) {
                    teleportManager.cancelTeleport(player.getUuid());
                    player.sendMessage(languageManager.getMessage(player, "teleport_cancelled_move"));
                }
            }
        }
    }
}
