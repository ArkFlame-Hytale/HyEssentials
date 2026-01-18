package com.arkflame.hyessentials.managers;

import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.arkflame.hyessentials.HyEssentials;
import com.hypixel.hytale.builtin.hytalegenerator.fields.FastNoiseLite.Vector3;

public class BackManager {
    private final HyEssentials plugin;
    private final Map<UUID, LinkedList<Vector3>> locationHistory = new ConcurrentHashMap<>();
    private final int maxHistory = 5;
    
    public BackManager(HyEssentials plugin) {
        this.plugin = plugin;
    }
    
    public void addLocation(UUID uuid, Vector3 location) {
        LinkedList<Vector3> history = locationHistory.computeIfAbsent(uuid, k -> new LinkedList<>());
        history.addFirst(location);
        
        if (history.size() > maxHistory) {
            history.removeLast();
        }
    }
    
    public Vector3 getLastLocation(UUID uuid) {
        LinkedList<Vector3> history = locationHistory.get(uuid);
        return history != null && !history.isEmpty() ? history.removeFirst() : null;
    }
    
    public boolean hasHistory(UUID uuid) {
        LinkedList<Vector3> history = locationHistory.get(uuid);
        return history != null && !history.isEmpty();
    }
}