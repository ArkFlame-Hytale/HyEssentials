package com.arkflame.hyessentials.listeners;

public class PlayerRespawnListener {
    
    
    private void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        
        // Teleport to spawn if configured
        if (configManager.isRespawnAtSpawn()) {
            Vector3 spawn = spawnManager.getSpawn();
            if (spawn != null) {
                event.setRespawnLocation(spawn);
            }
        }
    }
}
