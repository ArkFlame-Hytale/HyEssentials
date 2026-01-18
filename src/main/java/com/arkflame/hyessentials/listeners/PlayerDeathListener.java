package com.arkflame.hyessentials.listeners;

public class PlayerDeathListener {
    
    
    private void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        Vector3 deathLocation = player.getPosition();
        
        // Store death location for /back
        backManager.addLocation(player.getUniqueId(), deathLocation);
    }
}
