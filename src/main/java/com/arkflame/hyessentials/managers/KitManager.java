package com.arkflame.hyessentials.managers;

import com.arkflame.hyessentials.HyEssentials;
import com.arkflame.hyessentials.data.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class KitManager {
    private final HyEssentials plugin;
    private final Map<String, Kit> kits = new ConcurrentHashMap<>();
    private final Map<UUID, Map<String, Long>> kitCooldowns = new ConcurrentHashMap<>();
    private final File kitsFile;
    
    public KitManager(HyEssentials plugin) {
        this.plugin = plugin;
        this.kitsFile = new File(plugin.getDataFolder(), "kits.yml");
    }
    
    public void load() {
        plugin.getTaskRunner().runAsync(() -> {
            if (kitsFile.exists()) {
                // Load kits from file
            }
        });
    }
    
    public void createKit(String name, List<ItemStack> items, long cooldown, double price) {
        kits.put(name.toLowerCase(), new Kit(name, items, cooldown, price));
    }
    
    public Kit getKit(String name) {
        return kits.get(name.toLowerCase());
    }
    
    public void deleteKit(String name) {
        kits.remove(name.toLowerCase());
    }
    
    public Set<String> getKitNames() {
        return kits.keySet();
    }
    
    public boolean canUseKit(UUID uuid, String kitName) {
        return plugin.getPermissionManager().hasPermission(uuid, "essentials.kits." + kitName) ||
               plugin.getPermissionManager().hasPermission(uuid, "essentials.kits.*");
    }
    
    public boolean isOnCooldown(UUID uuid, String kitName) {
        Map<String, Long> cooldowns = kitCooldowns.get(uuid);
        if (cooldowns == null) return false;
        
        Long cooldownEnd = cooldowns.get(kitName);
        return cooldownEnd != null && System.currentTimeMillis() < cooldownEnd;
    }
    
    public void setCooldown(UUID uuid, String kitName, long cooldown) {
        kitCooldowns.computeIfAbsent(uuid, k -> new ConcurrentHashMap<>())
            .put(kitName, System.currentTimeMillis() + cooldown);
    }
}