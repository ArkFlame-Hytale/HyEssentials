package com.arkflame.hyessentials.managers;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.arkflame.hyessentials.HyEssentials;
import com.hypixel.hytale.builtin.hytalegenerator.fields.FastNoiseLite.Vector3;

public class HomeManager {
    private final HyEssentials plugin;
    private final Map<UUID, Map<String, Vector3>> homes = new ConcurrentHashMap<>();
    
    public HomeManager(HyEssentials plugin) {
        this.plugin = plugin;
    }
    
    public void setHome(UUID uuid, String name, Vector3 location) {
        homes.computeIfAbsent(uuid, k -> new ConcurrentHashMap<>()).put(name, location);
    }
    
    public Vector3 getHome(UUID uuid, String name) {
        Map<String, Vector3> playerHomes = homes.get(uuid);
        return playerHomes != null ? playerHomes.get(name) : null;
    }
    
    public void deleteHome(UUID uuid, String name) {
        Map<String, Vector3> playerHomes = homes.get(uuid);
        if (playerHomes != null) {
            playerHomes.remove(name);
        }
    }
    
    public Set<String> getHomes(UUID uuid) {
        Map<String, Vector3> playerHomes = homes.get(uuid);
        return playerHomes != null ? playerHomes.keySet() : Collections.emptySet();
    }
    
    public int getHomeCount(UUID uuid) {
        Map<String, Vector3> playerHomes = homes.get(uuid);
        return playerHomes != null ? playerHomes.size() : 0;
    }
    
    public int getMaxHomes(UUID uuid) {
        // Check permissions for max homes
        if (plugin.getPermissionManager().hasPermission(uuid, "essentials.sethome.unlimited")) {
            return Integer.MAX_VALUE;
        }
        
        for (int i = 100; i >= 1; i--) {
            if (plugin.getPermissionManager().hasPermission(uuid, "essentials.sethome." + i)) {
                return i;
            }
        }
        
        return plugin.getConfigManager().getDefaultMaxHomes();
    }
}