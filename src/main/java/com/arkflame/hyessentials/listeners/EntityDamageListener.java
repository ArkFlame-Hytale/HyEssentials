package com.arkflame.hyessentials.listeners;

public class EntityDamageListener {
    
    
    private void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            UUID uuid = player.getUniqueId();
            
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
