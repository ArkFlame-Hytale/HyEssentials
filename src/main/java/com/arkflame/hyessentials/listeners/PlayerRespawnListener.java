package com.arkflame.hyessentials.listeners;

import com.hypixel.hytale.builtin.hytalegenerator.fields.FastNoiseLite.Vector3;
import com.hypixel.hytale.server.core.entity.entities.Player;

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
