package com.arkflame.hyessentials.managers;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.arkflame.hyessentials.HyEssentials;
import com.hypixel.hytale.builtin.hytalegenerator.fields.FastNoiseLite.Vector3;
import com.hypixel.hytale.server.core.entity.entities.Player;

public class TeleportManager {
    private final HyEssentials plugin;
    private final Map<UUID, PendingTeleport> pendingTeleports = new ConcurrentHashMap<>();
    private final Map<UUID, Long> teleportCooldowns = new ConcurrentHashMap<>();
    private final Map<UUID, UUID> tpaRequests = new ConcurrentHashMap<>();
    
    public TeleportManager(HyEssentials plugin) {
        this.plugin = plugin;
    }
    
    public void requestTeleport(Player player, Vector3 destination, TeleportType type) {
        UUID uuid = player.getUuid();
        
        // Check cooldown
        if (isOnCooldown(uuid)) {
            long remaining = getCooldownRemaining(uuid);
            player.sendMessage(plugin.getLanguageManager().getMessage(player, "teleport_cooldown")
                .replace("{time}", String.valueOf(remaining)));
            return;
        }
        
        int warmup = plugin.getConfigManager().getTeleportWarmup();
        
        if (warmup > 0) {
            player.sendMessage(plugin.getLanguageManager().getMessage(player, "teleport_warmup")
                .replace("{time}", String.valueOf(warmup)));
            
            PendingTeleport pending = new PendingTeleport(player, destination, type);
            pendingTeleports.put(uuid, pending);
            
            plugin.getTaskRunner().runDelayed(() -> {
                if (pendingTeleports.remove(uuid) != null) {
                    executeTeleport(player, destination);
                    setCooldown(uuid);
                }
            }, warmup, TimeUnit.SECONDS);
        } else {
            executeTeleport(player, destination);
            setCooldown(uuid);
        }
    }
    
    private void executeTeleport(Player player, Vector3 destination) {
        // Store current location for /back
        plugin.getBackManager().addLocation(player.getUuid(), player.getPosition());
        
        player.setPosition(destination);
        player.sendMessage(plugin.getLanguageManager().getMessage(player, "teleport_success"));
    }
    
    public void cancelTeleport(UUID uuid) {
        pendingTeleports.remove(uuid);
    }
    
    public boolean hasPendingTeleport(UUID uuid) {
        return pendingTeleports.containsKey(uuid);
    }
    
    public void sendTpaRequest(UUID sender, UUID target) {
        tpaRequests.put(target, sender);
        
        // Auto-expire after 60 seconds
        plugin.getTaskRunner().runAsyncDelayed(() -> {
            tpaRequests.remove(target, sender);
        }, 60, TimeUnit.SECONDS);
    }
    
    public UUID getTpaRequest(UUID target) {
        return tpaRequests.get(target);
    }
    
    public void removeTpaRequest(UUID target) {
        tpaRequests.remove(target);
    }
    
    private boolean isOnCooldown(UUID uuid) {
        Long cooldownEnd = teleportCooldowns.get(uuid);
        return cooldownEnd != null && System.currentTimeMillis() < cooldownEnd;
    }
    
    private long getCooldownRemaining(UUID uuid) {
        Long cooldownEnd = teleportCooldowns.get(uuid);
        if (cooldownEnd == null) return 0;
        return (cooldownEnd - System.currentTimeMillis()) / 1000;
    }
    
    private void setCooldown(UUID uuid) {
        int cooldown = plugin.getConfigManager().getTeleportCooldown();
        if (cooldown > 0) {
            teleportCooldowns.put(uuid, System.currentTimeMillis() + (cooldown * 1000L));
        }
    }
}