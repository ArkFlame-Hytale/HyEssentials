package com.arkflame.hyessentials.managers;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class IgnoreManager {
    private final Map<UUID, Set<UUID>> ignoredPlayers = new ConcurrentHashMap<>();
    
    public void ignore(UUID uuid, UUID target) {
        ignoredPlayers.computeIfAbsent(uuid, k -> new HashSet<>()).add(target);
    }
    
    public void unignore(UUID uuid, UUID target) {
        Set<UUID> ignored = ignoredPlayers.get(uuid);
        if (ignored != null) {
            ignored.remove(target);
        }
    }
    
    public boolean isIgnoring(UUID uuid, UUID target) {
        Set<UUID> ignored = ignoredPlayers.get(uuid);
        return ignored != null && ignored.contains(target);
    }
}