package com.arkflame.hyessentials.managers;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.arkflame.hyessentials.HyEssentials;

public class WarpManager {
    private final HyEssentials plugin;
    private final Map<String, Warp> warps = new ConcurrentHashMap<>();
    private final File warpsFile;
    
    public WarpManager(HyEssentials plugin) {
        this.plugin = plugin;
        this.warpsFile = new File(plugin.getDataFolder(), "warps.yml");
    }
    
    public void load() {
        plugin.getTaskRunner().runAsync(() -> {
            if (warpsFile.exists()) {
                // Load warps from file
            }
        });
    }
    
    public void setWarp(String name, Vector3 location) {
        warps.put(name.toLowerCase(), new Warp(name, location));
    }
    
    public Warp getWarp(String name) {
        return warps.get(name.toLowerCase());
    }
    
    public void deleteWarp(String name) {
        warps.remove(name.toLowerCase());
    }
    
    public Set<String> getWarpNames() {
        return warps.keySet();
    }
    
    public boolean hasAccess(UUID uuid, String warpName) {
        return plugin.getPermissionManager().hasPermission(uuid, "essentials.warp." + warpName) ||
               plugin.getPermissionManager().hasPermission(uuid, "essentials.warp.*");
    }
}