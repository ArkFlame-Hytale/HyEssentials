package com.arkflame.hyessentials.listeners;

import java.util.UUID;

import com.hypixel.hytale.server.core.entity.entities.Player;

public class EntityDamageListener {
    
    private void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            UUID uuid = player.getUuid();
            
            // Cancel teleport if player takes damage during warmup
            if (configManager.isCancelOnDamage()) {
                if (teleportManager.hasPendingTeleport(uuid)) {
                    teleportManager.cancelTeleport(uuid);
                    player.sendMessage(languageManager.getMessage(player, "teleport_cancelled_damage"));
                }
            }
        }
    }
}
