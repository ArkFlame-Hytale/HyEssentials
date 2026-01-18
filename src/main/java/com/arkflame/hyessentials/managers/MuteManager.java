package com.arkflame.hyessentials.managers;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MuteManager {
    private final Map<UUID, Long> mutedPlayers = new ConcurrentHashMap<>();
    
    public void mute(UUID uuid, long duration) {
        if (duration > 0) {
            mutedPlayers.put(uuid, System.currentTimeMillis() + duration);
        } else {
            mutedPlayers.put(uuid, Long.MAX_VALUE);
        }
    }
    
    public void unmute(UUID uuid) {
        mutedPlayers.remove(uuid);
    }
    
    public boolean isMuted(UUID uuid) {
        Long muteEnd = mutedPlayers.get(uuid);
        if (muteEnd == null) return false;
        
        if (muteEnd != Long.MAX_VALUE && System.currentTimeMillis() > muteEnd) {
            mutedPlayers.remove(uuid);
            return false;
        }
        
        return true;
    }
}